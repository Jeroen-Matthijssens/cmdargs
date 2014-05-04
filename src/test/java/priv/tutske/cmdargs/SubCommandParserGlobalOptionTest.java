package priv.tutske.cmdargs;

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


@RunWith (Parameterized.class)
public class SubCommandParserGlobalOptionTest {

	private String [] args;
	private String option;
	private boolean present;
	private ParserImpl parser;

	public SubCommandParserGlobalOptionTest (String args, String option, boolean present) {
		this.args = args.split (" ");
		this.option = option;
		this.present = present;
	}

	@Parameters (name="globals: `{1}` in `{0}` should be `{2}`")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"--enabled create", "enabled", true}
			, {"--enabled create", "path", false}

			, {"-p path/to/file create", "path", true}
			, {"-p create list", "path", true}

			, {"--enabled -p list create -h -- arg", "help", false}

			, {"list --help", "help", false}
			, {"--enabled list --help", "help", false}

			, {"--enabled -h list --help", "help", true}
		});
	}

	@Before
	public void setup () throws CommandLineException {
		CommandSchemeBuilder schemeBuilder = new CommandSchemeBuilder ();

		schemeBuilder.add (new BooleanOption ("enabled"));
		schemeBuilder.add (new StringValueOption ("path", "p"));
		schemeBuilder.add (new BasicOption ("help", "h"));
		CommandScheme globalOptions = schemeBuilder.buildScheme ();

		schemeBuilder.reset ();

		schemeBuilder.add (new StringValueOption ("template", "p"));
		schemeBuilder.add (new StringValueOption ("layout", "l"));
		schemeBuilder.add (new BasicOption ("interactive", "i"));
		schemeBuilder.add (new BasicOption ("help", "h"));
		CommandScheme createScheme = schemeBuilder.buildScheme ();
		Command create = new SubCommandImpl ("create", createScheme);

		schemeBuilder.reset ();

		schemeBuilder.add (new BooleanOption ("recursive", "r"));
		schemeBuilder.add (new StringValueOption ("depth", "d"));
		schemeBuilder.add (new BasicOption ("verbose", "v"));
		schemeBuilder.add (new BasicOption ("human readable", "H"));
		schemeBuilder.add (new BasicOption ("help", "h"));
		CommandScheme listScheme = schemeBuilder.buildScheme ();
		Command list = new SubCommandImpl ("list", listScheme);

		parser = new ParserImpl (globalOptions, Arrays.asList (create, list));
		parser.parse (args);
	}

	@Test
	public void it_should () {
		ParsedOptions globalOptions = parser.getGlobalOptions ();
		assertThat (globalOptions.isPresent (option), is (present));
	}

}
