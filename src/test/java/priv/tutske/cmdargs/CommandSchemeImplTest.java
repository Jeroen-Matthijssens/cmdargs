package priv.tutske.cmdargs;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.tutske.cmdargs.Option.Requirement.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
import org.tutske.cmdargs.*;


public class CommandSchemeImplTest {

	private List<Option> options;
	private List<Command> commands;
	private List<Argument<?>> arguments;

	private CommandScheme scheme;

	@Before
	public void setup () {
		options = Arrays.<Option>asList (
			new BasicOption ("first", "f", RequirePresence),
			new BasicOption ("second", "s", RequireValue),
			new BasicOption ("third", "t", RequireNone),
			new BasicOption ("forth", "F")
		);
		commands = Arrays.<Command>asList (
			new CommandImpl ("first"),
			new CommandImpl ("second")
		);
		arguments = Arrays.<Argument<?>>asList (
			new StringArgument ("first", 0),
			new StringArgument ("second", 1)
		);
	}

	@Test
	public void sanity_construct_check () {
		CommandSchemeImpl.newInstance ();
		CommandSchemeImpl.newInstance (options);
		CommandSchemeImpl.withCommands (commands);
		CommandSchemeImpl.withCommands (options, commands);
		CommandSchemeImpl.withArguments (arguments);
		CommandSchemeImpl.withArguments (options, arguments);
	}

	@Test
	public void it_should_find_its_own_options () {
		scheme = CommandSchemeImpl.newInstance (options);
		Option first = scheme.getOption ("first");
		assertThat (first.getRepresentation (), is ("--first"));
		assertThat (first.getShortRepresentation (), is ("-f"));

		Option second = scheme.getOption ("s");
		assertThat (second.getRepresentation (), is ("--second"));
		assertThat (second.getShortRepresentation (), is ("-s"));
	}

	@Test
	public void it_should_know_its_required_options () {
		scheme = CommandSchemeImpl.newInstance (options);
		Option first = scheme.getOption ("first");
		Set<Option> required = scheme.getByRequirement (RequirePresence);

		assertThat (required.size (), is (1));
		assertThat (required, contains (first));
	}

	@Test
	public void it_should_know_its_optios_with_required_values () {
		scheme = CommandSchemeImpl.newInstance (options);
		Option second = scheme.getOption ("second");
		Set<Option> required = scheme.getByRequirement (RequireValue);

		assertThat (required.size (), is (1));
		assertThat (required, contains (second));
	}

	@Test
	public void it_should_know_its_options_without_requirements () {
		scheme = CommandSchemeImpl.newInstance (options);
		Option third = scheme.getOption ("third");
		Option forth = scheme.getOption ("forth");
		Set<Option> required = scheme.getByRequirement (RequireNone);

		assertThat (required.size (), is (2));
		assertThat (required, hasItem (third));
		assertThat (required, hasItem (forth));
	}

}
