package priv.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.ArrayList;
import java.util.MissingResourceException;

import org.junit.Test;
import org.junit.Before;
import org.tutske.cmdargs.BasicOption;
import org.tutske.cmdargs.BooleanOption;
import org.tutske.cmdargs.Option;
import org.tutske.cmdargs.CommandScheme;

import org.tutske.cmdargs.StringValueOption;
import priv.tutske.cmdargs.CommandSchemeImpl;

public class OptionSchemeImplTest {

	Option first;
	Option enabled;
	Option value;
	Option unknown;

	List<Option> options;

	CommandScheme scheme;

	@Before
	public void setup () {
		first = new BasicOption ("basic option", "f");
		enabled = new BooleanOption ("boolean option", "b");
		value = new StringValueOption ("value option", "v");
		unknown = new BasicOption ("unknown option");

		options = new ArrayList<Option> ();
		options.add (first);
		options.add (enabled);
		options.add (value);

		scheme = new CommandSchemeImpl (options);
	}

	@Test
	public void it_should_have_the_first_option () {
		assertThat (scheme.hasOption ("basic option"), is (true));;
		assertThat (scheme.hasOption (first), is (true));
	}

	@Test
	public void it_should_have_the_boolean_option () {
		assertThat (scheme.hasOption ("boolean-option"), is (true));
		assertThat (scheme.hasOption (enabled), is (true));
	}

	@Test
	public void it_should_have_the_value_option () {
		assertThat (scheme.hasOption ("value option"), is (true));
		assertThat (scheme.hasOption (value), is (true));
	}

	@Test
	public void it_should_not_have_the_unknown_option () {
		assertThat (scheme.hasOption ("unknown option"), is (false));
	}

	@Test
	public void it_should_retrieve_the_first_option () {
		assertThat (scheme.getOption ("basic option"), is (first));
	}

	@Test
	public void it_should_retrieve_the_boolean_option () {
		assertThat (scheme.getOption ("boolean option"), is (enabled));
	}

	@Test
	public void it_should_retrieve_the_value_option () {
		assertThat (scheme.getOption ("value option"), is (value));
	}

	@Test (expected = MissingResourceException.class)
	public void it_should_complain_when_retrieving_options_that_are_not_there () {
		scheme.getOption ("unknown option");
		fail ("OptionScheme schould complain when the option is not there.");
	}

}
