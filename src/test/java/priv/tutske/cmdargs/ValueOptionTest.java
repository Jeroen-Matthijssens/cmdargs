package priv.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.CommandLineException;


public class ValueOptionTest {

	@Test (expected = RuntimeException.class)
	public void it_should_not_allow_options_to_start_with_a_dash () {
		new StringValueOption ("-long-option");
		fail ("Optios can not start with a dash.");
	}

	@Test
	public void it_should_provide_the_right_representation () {
		Option spaces = new StringValueOption ("long option");
		assertThat (spaces.getRepresentation (), is ("--long-option"));

		Option dashes = new StringValueOption ("long-option");
		assertThat (dashes.getRepresentation (), is ("--long-option"));
	}

	@Test
	public void it_should_match_representations_with_dashes () {
		Option option = new StringValueOption ("long option");
		assertThat (option.matches ("long-option"), is (true));
	}

	@Test
	public void it_should_match_representations_with_spaces () {
		Option option = new StringValueOption ("long option");
		assertThat (option.matches ("long option"), is (true));
	}

	@Test
	public void it_should_match_with_spaces_when_constructed_with_dashes () {
		Option option = new StringValueOption ("long-option");
		assertThat (option.matches ("long option"), is (true));
	}

	@Test
	public void it_should_match_with_dashes_when_constructed_with_dashes () {
		Option option = new StringValueOption ("long-option");
		assertThat (option.matches ("long-option"), is (true));
	}

	@Test
	public void it_should_know_when_it_is_required () {
		Option required = new StringValueOption ("long option", true);
		assertThat (required.isRequired (), is (true));

		Option optional = new StringValueOption ("long option", false);
		assertThat (optional.isRequired (), is (false));

		Option standard = new StringValueOption ("long option");
		assertThat (standard.isRequired (), is (false));
	}

	@Test
	public void it_should_handle_both_dashes_and_spaces_in_representation () {
		Option spaces = new StringValueOption ("long option");
		Option dashes = new StringValueOption ("long-option");

		assertThat (spaces.equals (dashes), is (true));
		assertThat (dashes.equals (spaces), is (true));
	}

	@Test
	public void it_should_not_complain_if_one_option_has_no_short_representation () {
		Option first = new StringValueOption ("long option");
		Option second = new StringValueOption ("long option", "s");

		assertThat (first.equals (second), is (true));
	}

	@Test
	public void it_should_validate_every_string ()
	throws CommandLineException {
		ValueOption<String> option = new StringValueOption ("silent");
		Validator<String> validator = option.getValidator ("silent");

		String expected = "Some String";
		assertThat (validator.isValid ("Some String"), is (true));
		assertThat (validator.parse ("Some String"), is (expected));
	}

}
