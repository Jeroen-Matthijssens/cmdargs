package priv.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;
import org.tutske.cmdargs.BasicOption;
import org.tutske.cmdargs.Option;
import org.tutske.cmdargs.CommandScheme;

import priv.tutske.cmdargs.CommandSchemeBuilder;


public class OptionSchemeBuilderTest {

	private Option first;
	private Option unknown;

	private CommandSchemeBuilder schemeBuilder;

	@Before
	public void setup () {
		first = new BasicOption ("basic option", "f");
		unknown = new BasicOption ("unknown option");

		schemeBuilder = new CommandSchemeBuilder ();
	}

	@Test
	public void it_should_build_a_scheme () {
		schemeBuilder.add (first);
		CommandScheme scheme = schemeBuilder.buildScheme ();

		assertThat (scheme.hasOption ("basic option"), is (true));
	}

	@Test (expected = IllegalArgumentException.class)
	public void it_should_complain_when_adding_options_twice () {
		schemeBuilder.add (first);
		schemeBuilder.add (first);
		fail ("Only one option with a specific long representation is allowed");
	}

	@Test
	public void it_should_produce_a_scheme_that_knows_only_the_added_options () {
		schemeBuilder.add (first);
		CommandScheme scheme = schemeBuilder.buildScheme ();

		assertThat (scheme.hasOption ("unknown option"), is (false));
	}

	@Test
	public void it_should_produce_an_empty_scheme () {
		assertThat (schemeBuilder.buildScheme (), not (nullValue ()));
	}

	@Test
	public void it_should_reset_properly () {
		schemeBuilder.add (unknown);
		schemeBuilder.reset ();
		schemeBuilder.add (first);

		CommandScheme scheme = schemeBuilder.buildScheme ();
		assertThat (scheme.hasOption ("unknown option"), is (false));
		assertThat (scheme.hasOption ("basic option"), is (true));
	}

	@Test
	public void it_should_reset_properly_after_building_scheme () {
		schemeBuilder.add (unknown);
		schemeBuilder.buildScheme ();
		schemeBuilder.reset ();
		schemeBuilder.add (first);

		CommandScheme scheme = schemeBuilder.buildScheme ();
		assertThat (scheme.hasOption ("unknown option"), is (false));
		assertThat (scheme.hasOption ("basic option"), is (true));
	}

}
