package priv.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;

public class ParserTest {

	private CommandSchemeBuilderImpl schemeBuilder;
	private Parser parser;

	@Before
	public void setup () {
		schemeBuilder = new CommandSchemeBuilderImpl ();

		schemeBuilder.addOption (new BooleanOption ("enabled"));
		schemeBuilder.addOption (new StringOption ("path", "p"));
		schemeBuilder.addOption (new BasicOption ("help", "h"));
		CommandScheme createScheme = schemeBuilder.buildScheme ();
		Command create = new CommandImpl ("create", createScheme);

		schemeBuilder.reset ();

		schemeBuilder.addOption (new BasicOption ("verbose", "v"));
		schemeBuilder.addOption (new BasicOption ("human readable", "H"));
		schemeBuilder.addOption (new BasicOption ("help", "h"));
		CommandScheme listScheme = schemeBuilder.buildScheme ();
		Command list = new CommandImpl ("list", listScheme);

		schemeBuilder.reset ();

		schemeBuilder.addOption (new BasicOption ("working directory", "w"));
		schemeBuilder.addOption (new StringOption ("layout", "l"));
		schemeBuilder.addOption (new StringOption ("root", "r"));
		schemeBuilder.addOption (new BasicOption ("help", "h"));
		schemeBuilder.addCommand (create);
		schemeBuilder.addCommand (list);
		CommandScheme cmdscheme = schemeBuilder.buildScheme ();

		parser = new ParserImpl (cmdscheme);
	}

	@Test
	public void it_should_find_sub_command_create ()
	throws CommandLineException {
		String [] args = {"--layout=simple", "-w", "create"};

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.hasCommand (), is (true));
		assertThat (parsed.getCommand ().getRepresentation (), is ("create"));
	}

	@Test
	public void it_should_find_sub_command_with_options ()
	throws CommandLineException {
		String [] args = {"--layout=simple", "-w", "create", "--enabled", "-p"};

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.hasCommand (), is (true));
		assertThat (parsed.getCommand ().getRepresentation (), is ("create"));
	}

	@Test
	public void it_should_find_sub_command_with_arguments ()
	throws CommandLineException {
		String [] args = {"--layout=simple", "-w", "create", "appname"};

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.hasCommand (), is (true));
		assertThat (parsed.getCommand ().getRepresentation (), is ("create"));
	}

	@Test
	public void it_should_find_sub_command_with_options_and_arguments ()
	throws CommandLineException {
		String [] args = {"--layout=simple", "-w", "create", "appname"};

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.hasCommand (), is (true));
		assertThat (parsed.getCommand ().getRepresentation (), is ("create"));
	}

	@Test
	public void it_should_find_sub_command_list ()
	throws CommandLineException {
		String [] args = {"--layout=simple", "-w", "list"};
		parser.parse (args);

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.hasCommand (), is (true));
		assertThat (parsed.getCommand ().getRepresentation (), is ("list"));
	}

	@Test
	public void it_should_find_sub_command_options ()
	throws CommandLineException {
		String [] args = {"create", "-h", "--path=path/to/file"};

		ParsedCommand parsed = parser.parse (args);
		ParsedCommand subparsed = parsed.getParsedCommand ();
		Option path = new BasicOption ("path");
		Option help = new BasicOption ("help");

		assertThat (subparsed.hasOption (path), is (true));
		assertThat (subparsed.hasOption (help), is (true));
	}

	@Test (expected = MissingCommandException.class)
	public void it_should_complain_about_missing_subcommand ()
	throws CommandLineException {
		String [] args = {"--layout=simple", "-r", "create"};
		parser.parse (args);
		fail ("When a subcommand is possible, a subcommand must always be used.");
	}

}
