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
	private Parser parser;

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
		parser.parse (args);
	}

	@Test
	public void it_should_know_its_command () {
		ParsedCommand parsed = parser.getOptions ();
		Command cmd = parsed.getCommand ();
		assertThat (cmd.getRepresentation (), is (command));
	}

}
