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
import priv.tutske.cmdargs.ParserImpl;


@RunWith (Parameterized.class)
public class SubCommandParserTest {

	private String [] args;
	private String command;
	private Parser parser;

	public SubCommandParserTest (String cmd, String command) {
		this.args = cmd.split (" ");
		this.command = command;
	}

	@Parameters (name="`{0}` has command `{1}`")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"init", "init"}
			, {"config", "config"}
			, {"list create", "list"}

			, {"--enabled show", "show"}
			, {"--enabled init", "init"}
			, {"-p list show", "show"}
			, {"-p create list pop", "list"}

			, {"-h show --human-readable", "show"}

			, {"--enabled list --human-readable create", "list"}
			, {"--enabled show --paginated --page-size=10", "show"}
		});
	}

	@Before
	public void setup () throws CommandLineException {
		CommandScheme cmdscheme = new SchemeBuilderComplex ().build ();
		parser = new ParserImpl (cmdscheme);
	}

	@Test
	public void it_should_know_its_command () {
		ParsedCommand parsed = parser.parse (args);
		Command cmd = parsed.getCommand ();
		assertThat (cmd.getRepresentation (), is (command));
	}

}
