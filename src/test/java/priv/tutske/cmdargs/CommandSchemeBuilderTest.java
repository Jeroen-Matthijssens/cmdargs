package priv.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;
import org.tutske.cmdargs.BasicOption;
import org.tutske.cmdargs.Option;
import org.tutske.cmdargs.CommandScheme;

import org.tutske.cmdargs.*;
import priv.tutske.cmdargs.CommandSchemeBuilder;


public class CommandSchemeBuilderTest {

	private Option firstOpt;
	private Option unkownOpt;

	private Command firstCmd;
	private Command secondCmd;
	private Command unknownCmd;

	private Argument<String> firstArg;

	private CommandSchemeBuilder schemeBuilder;

	@Before
	public void setup () {
		firstOpt = new BasicOption ("basic option", "f");
		unkownOpt = new BasicOption ("unknown option");

		firstCmd = new CommandImpl ("first");
		secondCmd = new CommandImpl ("second");
		unknownCmd = new CommandImpl ("unknown");

		firstArg = new StringArgument ("first", 0);

		schemeBuilder = new CommandSchemeBuilder ();
	}

	@Test
	public void it_should_build_a_scheme () {
		schemeBuilder.add (firstOpt);
		CommandScheme scheme = schemeBuilder.buildScheme ();

		assertThat (scheme.hasOption ("basic option"), is (true));
	}

	@Test (expected = IllegalArgumentException.class)
	public void it_should_complain_when_adding_options_twice () {
		schemeBuilder.add (firstOpt);
		schemeBuilder.add (firstOpt);
		fail ("Only one option with a specific long representation is allowed");
	}

	@Test
	public void it_should_produce_a_scheme_that_knows_only_the_added_options () {
		schemeBuilder.add (firstOpt);
		CommandScheme scheme = schemeBuilder.buildScheme ();

		assertThat (scheme.hasOption ("unknown option"), is (false));
	}

	@Test
	public void it_should_produce_an_empty_scheme () {
		assertThat (schemeBuilder.buildScheme (), not (nullValue ()));
	}

	@Test
	public void it_should_reset_properly () {
		schemeBuilder.add (unkownOpt);
		schemeBuilder.reset ();
		schemeBuilder.add (firstOpt);

		CommandScheme scheme = schemeBuilder.buildScheme ();
		assertThat (scheme.hasOption ("unknown option"), is (false));
		assertThat (scheme.hasOption ("basic option"), is (true));
	}

	@Test
	public void it_should_reset_properly_after_building_scheme () {
		schemeBuilder.add (unkownOpt);
		schemeBuilder.buildScheme ();
		schemeBuilder.reset ();
		schemeBuilder.add (firstOpt);

		CommandScheme scheme = schemeBuilder.buildScheme ();
		assertThat (scheme.hasOption ("unknown option"), is (false));
		assertThat (scheme.hasOption ("basic option"), is (true));
	}

	@Test
	public void it_should_produce_a_scheme_with_the_proper_commands () {
		schemeBuilder.addCommand (firstCmd);
		schemeBuilder.addCommand (secondCmd);

		CommandScheme scheme = schemeBuilder.buildScheme ();
		assertThat (scheme.hasCommand (firstCmd), is (true));
		assertThat (scheme.hasCommand (secondCmd), is(true));
	}

	@Test
	public void it_should_produce_a_scheme_with_the_proper_commands_with_strings () {
		schemeBuilder.addCommand (firstCmd);
		schemeBuilder.addCommand (secondCmd);

		CommandScheme scheme = schemeBuilder.buildScheme ();
		assertThat (scheme.hasCommand (firstCmd.getRepresentation ()), is (true));
		assertThat (scheme.hasCommand (secondCmd.getRepresentation ()), is(true));
	}

	@Test
	public void it_should_produce_a_scheme_without_not_provided_commands_from_string () {
		schemeBuilder.addCommand (firstCmd);
		schemeBuilder.addCommand (firstCmd);

		CommandScheme scheme = schemeBuilder.buildScheme ();
		assertThat (scheme.hasCommand (unknownCmd), is (false));
	}

	@Test
	public void it_should_produce_a_scheme_without_not_provided_commands () {
		schemeBuilder.addCommand (firstCmd);
		schemeBuilder.addCommand (firstCmd);

		CommandScheme scheme = schemeBuilder.buildScheme ();
		assertThat (scheme.hasCommand ("third"), is (false));
	}

	@Test (expected = RuntimeException.class)
	public void it_should_complain_when_both_arguments_and_commands_are_used () {
		schemeBuilder.addCommand (firstCmd);
		schemeBuilder.addArgument (firstArg);
		fail ("should not allow use of both arguments and commands");
	}

}
