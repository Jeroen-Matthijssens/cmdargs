package org.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.tutske.cmdargs.Option.Requirement.*;

import org.junit.Test;
import org.tutske.cmdargs.exceptions.CommandLineException;


public class BooleanOptionTest {

	@Test (expected = RuntimeException.class)
	public void it_should_not_allow_options_to_start_with_a_dash () {
		new BooleanOption ("-long-option");
		fail ("Optios can not start with a single dash.");
	}

	@Test
	public void it_should_provide_the_right_representation () {
		Option spaces = new BooleanOption ("long option");
		assertThat (spaces.getRepresentation (), is ("--long-option"));

		Option dashes = new BooleanOption ("long-option");
		assertThat (dashes.getRepresentation (), is ("--long-option"));
	}

	@Test
	public void it_should_match_representations_with_dashes () {
		Option option = new BooleanOption ("long option");
		assertThat (option.matches ("long-option"), is (true));
	}

	@Test
	public void it_should_match_representations_with_spaces () {
		Option option = new BooleanOption ("long option");
		assertThat (option.matches ("long option"), is (true));
	}

	@Test
	public void it_should_match_with_spaces_when_constructed_with_dashes () {
		Option option = new BooleanOption ("long-option");
		assertThat (option.matches ("long option"), is (true));
	}

	@Test
	public void it_should_match_with_dashes_when_constructed_with_dashes () {
		Option option = new BooleanOption ("long-option");
		assertThat (option.matches ("long-option"), is (true));
	}

	@Test
	public void it_should_match_with_short_descriptions () {
		Option option = new BooleanOption ("long-option", "l");
		assertThat (option.matches ("l"), is (true));
	}

	@Test
	public void it_should_know_when_it_is_required () {
		Option required = new BooleanOption ("long option", RequirePresence);
		assertThat (required.hasRequirement (RequirePresence), is (true));

		Option optional = new BooleanOption ("long option", RequireNone);
		assertThat (optional.hasRequirement (RequirePresence), is (false));

		Option standard = new BooleanOption ("long option");
		assertThat (standard.hasRequirement (RequirePresence), is (false));
	}

	@Test
	public void it_should_handle_both_dashes_and_spaces_in_representation () {
		Option spaces = new BooleanOption ("long option");
		Option dashes = new BooleanOption ("long-option");

		assertThat (spaces.equals (dashes), is (true));
		assertThat (dashes.equals (spaces), is (true));
	}

	@Test
	public void it_should_not_complain_if_one_option_has_no_short_representation () {
		Option first = new BooleanOption ("long option");
		Option second = new BooleanOption ("long option", "s");

		assertThat (first.equals (second), is (true));
	}

	@Test
	public void it_should_match_negations_of_the_option () {
		Option option = new BooleanOption ("silent");
		assertThat (option.matches ("not-silent"), is (true));
	}

	@Test
	public void it_should_validate_empty_strings ()
	throws CommandLineException {
		BooleanOption option = new BooleanOption ("silent");
		Validator<Boolean> validator = option.getValidator ("silent");
		assertThat (validator.isValid (""), is (true));
		assertThat (validator.parse (""), is (true));
	}

	@Test
	public void it_should_validate_false_strings ()
	throws CommandLineException {
		BooleanOption option = new BooleanOption ("silent");
		Validator<Boolean> validator = option.getValidator ("silent");
		assertThat (validator.isValid ("false"), is (true));
		assertThat (validator.parse ("false"), is (false));
	}

	@Test
	public void it_should_validate_true_strings ()
	throws CommandLineException {
		BooleanOption option = new BooleanOption ("silent");
		Validator<Boolean> validator = option.getValidator ("silent");
		assertThat (validator.isValid ("true"), is (true));
		assertThat (validator.parse ("true"), is (true));
	}

	@Test
	public void it_should_reverse_validate_false_strings_when_starting_with_not ()
	throws CommandLineException {
		BooleanOption option = new BooleanOption ("silent");
		Validator<Boolean> validator = option.getValidator ("--not-silent");
		assertThat (validator.isValid ("false"), is (true));
		assertThat (validator.parse ("false"), is (true));
	}

	@Test
	public void it_should_reverse_validate_true_strings_when_starting_with_not ()
	throws CommandLineException {
		BooleanOption option = new BooleanOption ("silent");
		Validator<Boolean> validator = option.getValidator ("--not-silent");
		assertThat (validator.isValid ("true"), is (true));
		assertThat (validator.parse ("true"), is (false));
	}

	@Test
	public void it_should_reverse_validate_false_strings_when_starting_with_no ()
	throws CommandLineException {
		BooleanOption option = new BooleanOption ("silent");
		Validator<Boolean> validator = option.getValidator ("--no-silent");
		assertThat (validator.isValid ("false"), is (true));
		assertThat (validator.parse ("false"), is (true));
	}

	@Test
	public void it_should_reverse_validate_true_strings_when_starting_with_no ()
	throws CommandLineException {
		BooleanOption option = new BooleanOption ("silent");
		Validator<Boolean> validator = option.getValidator ("--no-silent");
		assertThat (validator.isValid ("true"), is (true));
		assertThat (validator.parse ("true"), is (false));
	}

}
