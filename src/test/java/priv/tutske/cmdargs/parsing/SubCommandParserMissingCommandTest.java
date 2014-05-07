package priv.tutske.cmdargs.parsing;

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

import priv.tutske.cmdargs.ParserImpl;
import priv.tutske.cmdargs.CommandImpl;
import priv.tutske.cmdargs.CommandSchemeBuilderImpl;


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
		CommandSchemeBuilderImpl schemeBuilder = new CommandSchemeBuilderImpl ();

		schemeBuilder.addOption (new StringOption ("template", "p"));
		schemeBuilder.addOption (new StringOption ("layout", "l"));
		schemeBuilder.addOption (new BasicOption ("interactive", "i"));
		schemeBuilder.addOption (new BasicOption ("help", "h"));
		CommandScheme createScheme = schemeBuilder.buildScheme ();
		Command create = new CommandImpl ("create", createScheme);

		schemeBuilder.reset ();

		schemeBuilder.addOption (new BooleanOption ("recursive", "r"));
		schemeBuilder.addOption (new StringOption ("depth", "d"));
		schemeBuilder.addOption (new BasicOption ("verbose", "v"));
		schemeBuilder.addOption (new BasicOption ("human readable", "H"));
		schemeBuilder.addOption (new BasicOption ("help", "h"));
		CommandScheme listScheme = schemeBuilder.buildScheme ();
		Command list = new CommandImpl ("list", listScheme);

		schemeBuilder.reset ();

		schemeBuilder.addOption (new BooleanOption ("enabled"));
		schemeBuilder.addOption (new StringOption ("path", "p"));
		schemeBuilder.addOption (new BasicOption ("help", "h"));
		schemeBuilder.addCommand (create);
		schemeBuilder.addCommand (list);
		CommandScheme cmdscheme = schemeBuilder.buildScheme ();

		parser = new ParserImpl (cmdscheme);
	}

	@Test (expected = MissingCommandException.class)
	public void it_should_complain_about_missing_subcommand ()
	throws CommandLineException {
		parser.parse (args);
		fail ("When a subcommand is possible, a subcommand must always be used.");
	}

}
