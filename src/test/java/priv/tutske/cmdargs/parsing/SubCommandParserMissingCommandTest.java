package priv.tutske.cmdargs.parsing;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;
import priv.tutske.cmdargs.*;
import priv.tutske.cmdargs.CommandSchemeBuilderImpl;


@RunWith (Parameterized.class)
public class SubCommandParserMissingCommandTest {

	private String [] args;
	private Class<?> clazz;
	private Parser parser;

	public SubCommandParserMissingCommandTest (String cmd, Class<?> clazz) {
		this.args = cmd.split (" ");
		this.clazz = clazz;
	}

	@Parameters (name = "`{0}` is missing one of the sub commands 'list' or 'create'")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"", MissingCommandException.class}
			, {"--enabled", MissingCommandException.class}
			, {"-p create", MissingCommandException.class}
			, {"-p list", MissingCommandException.class}
			, {"--path=create update", CommandMismatchException.class}
			, {"-p create --enabled", MissingCommandException.class}
			, {"-p path/to/file -- create other but not --enabled", MissingCommandException.class}
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

	@Test
	public void it_should_complain_about_missing_subcommand ()
	throws CommandLineException {
		try { parser.parse (args); }
		catch (Exception e) {
			assertThat (e, instanceOf (clazz));
			return;
		}
		fail ("Failed to complain about missing or wrong command!");
	}

}
