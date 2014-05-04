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
public class SubCommandParserTest {

	private String [] args;
	private String command;
	private ParserImpl parser;

	public SubCommandParserTest (String cmd, String command) {
		this.args = cmd.split (" ");
		this.command = command;
	}

	@Parameters (name="`{0}` has command `{1}`")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"create", "create"}
			, {"list", "list"}

			, {"--enabled list", "list"}
			, {"--enabled create", "create"}
			, {"-p list create", "create"}
			, {"-p create list", "list"}
			
			, {"-h create --interactive", "create"}

			, {"--enabled list --recursive", "list"}
			, {"--enabled create --layout=simple", "create"}
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
	public void it_should_know_its_command () {
		assertThat (parser.getSubCommand (), is (command));
	}

}
