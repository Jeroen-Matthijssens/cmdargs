package priv.tutske.cmdargs.parsing;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;
import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;

import priv.tutske.cmdargs.CmdSchemeParser;


@RunWith (Parameterized.class)
public class SubCommandParserGlobalOptionTest {

	private String [] args;
	private String option;
	private boolean present;

	private Parser parser;
	CommandScheme cmdscheme;

	public SubCommandParserGlobalOptionTest (String args, String option, boolean present) {
		this.args = args.split (" ");
		this.option = option;
		this.present = present;
	}

	@Parameters (name="globals: `{1}` in `{0}` should be `{2}`")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"--enabled show", "enabled", true}
			, {"--enabled show", "path", false}

			, {"-p path/to/file show", "path", true}
			, {"-p create list push", "path", true}

			, {"--enabled -p path/to/file list push -h -- arg", "help", false}

			, {"show --help", "help", false}
			, {"--enabled list --help create", "help", false}

			, {"--enabled -h list --help create", "help", true}
		});
	}

	@Before
	public void setup () throws CommandLineException {
		cmdscheme = new SchemeBuilderComplex ().build ();
		parser = new CmdSchemeParser (cmdscheme);
	}

	@Test
	public void it_should () {
		ParsedCommand parsed = parser.parse (args);
		Option opt = cmdscheme.getOption (option);
		assertThat (parsed.isOptionPresent (opt), is (present));
	}

}
