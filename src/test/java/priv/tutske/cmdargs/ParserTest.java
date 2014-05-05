package priv.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;

public class ParserTest {

	private CommandSchemeBuilder schemeBuilder;
	private Parser parser;

	@Before
	public void setup () {
		schemeBuilder = new CommandSchemeBuilder ();

		schemeBuilder.add (new BooleanOption ("enabled"));
		schemeBuilder.add (new StringValueOption ("path", "p"));
		schemeBuilder.add (new BasicOption ("help", "h"));
		CommandScheme createScheme = schemeBuilder.buildScheme ();
		Command create = new CommandImpl ("create", createScheme);

		schemeBuilder.reset ();

		schemeBuilder.add (new BasicOption ("verbose", "v"));
		schemeBuilder.add (new BasicOption ("human readable", "H"));
		schemeBuilder.add (new BasicOption ("help", "h"));
		CommandScheme listScheme = schemeBuilder.buildScheme ();
		Command list = new CommandImpl ("list", listScheme);

		schemeBuilder.reset ();

		schemeBuilder.add (new BasicOption ("working directory", "w"));
		schemeBuilder.add (new StringValueOption ("layout", "l"));
		schemeBuilder.add (new StringValueOption ("root", "r"));
		schemeBuilder.add (new BasicOption ("help", "h"));
		schemeBuilder.add (create);
		schemeBuilder.add (list);
		CommandScheme cmdscheme = schemeBuilder.buildScheme ();

		parser = new CmdSchemeParser (cmdscheme);
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

		assertThat (subparsed.isOptionPresent (path), is (true));
		assertThat (subparsed.isOptionPresent (help), is (true));
	}

	@Test (expected = MissingSubCommandException.class)
	public void it_should_complain_about_missing_subcommand ()
	throws CommandLineException {
		String [] args = {"--layout=simple", "-r", "create"};
		parser.parse (args);
		fail ("When a subcommand is possible, a subcommand must always be used.");
	}

}
