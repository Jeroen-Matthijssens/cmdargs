package priv.tutske.cmdargs;

import static org.junit.Assert.fail;

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
public class SubCommandParserMissingCommandTest {

	private String [] args;
	private Parser parser;

	public SubCommandParserMissingCommandTest (String cmd) {
		this.args = cmd.split (" ");
	}

	@Parameters (name = "`{0}` is missing one of the sub commands 'list' or 'create'")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{""}
			, {"--enabled"}
			, {"-p create"}
			, {"-p list"}
			, {"--path=create update"}
			, {"-p create --enabled"}
			, {"-p path/to/file -- create other but not --enabled"}
		});
	}

	@Before
	public void setup () {
		CommandSchemeBuilder schemeBuilder = new CommandSchemeBuilder ();

		schemeBuilder.add (new StringValueOption ("template", "p"));
		schemeBuilder.add (new StringValueOption ("layout", "l"));
		schemeBuilder.add (new BasicOption ("interactive", "i"));
		schemeBuilder.add (new BasicOption ("help", "h"));
		CommandScheme createScheme = schemeBuilder.buildScheme ();
		Command create = new CommandImpl ("create", createScheme);

		schemeBuilder.reset ();

		schemeBuilder.add (new BooleanOption ("recursive", "r"));
		schemeBuilder.add (new StringValueOption ("depth", "d"));
		schemeBuilder.add (new BasicOption ("verbose", "v"));
		schemeBuilder.add (new BasicOption ("human readable", "H"));
		schemeBuilder.add (new BasicOption ("help", "h"));
		CommandScheme listScheme = schemeBuilder.buildScheme ();
		Command list = new CommandImpl ("list", listScheme);

		schemeBuilder.reset ();

		schemeBuilder.add (new BooleanOption ("enabled"));
		schemeBuilder.add (new StringValueOption ("path", "p"));
		schemeBuilder.add (new BasicOption ("help", "h"));
		schemeBuilder.add (create);
		schemeBuilder.add (list);
		CommandScheme cmdscheme = schemeBuilder.buildScheme ();

		parser = new CmdSchemeParser (cmdscheme);
	}

	@Test (expected = MissingSubCommandException.class)
	public void it_should_complain_about_missing_subcommand ()
	throws CommandLineException {
		parser.parse (args);
		fail ("When a subcommand is possible, a subcommand must always be used.");
	}

}
