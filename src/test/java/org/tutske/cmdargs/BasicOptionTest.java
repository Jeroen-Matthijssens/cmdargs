package org.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BasicOptionTest {

	@Test (expected = RuntimeException.class)
	public void it_should_not_allow_options_to_start_with_a_dash () {
		new BasicOption ("-long-option");
		fail ("Optios can not start with a dash.");
	}

	@Test
	public void it_should_provide_the_right_representation () {
		Option spaces = new BasicOption ("long option");
		assertThat (spaces.getRepresentation (), is ("--long-option"));

		Option dashes = new BasicOption ("long-option");
		assertThat (dashes.getRepresentation (), is ("--long-option"));
	}

	@Test
	public void it_should_match_representations_with_dashes () {
		Option option = new BasicOption ("long option");
		assertThat (option.matches ("long-option"), is (true));
	}

	@Test
	public void it_should_match_representations_with_spaces () {
		Option option = new BasicOption ("long option");
		assertThat (option.matches ("long option"), is (true));
	}

	@Test
	public void it_should_match_with_spaces_when_constructed_with_dashes () {
		Option option = new BasicOption ("long-option");
		assertThat (option.matches ("long option"), is (true));
	}

	@Test
	public void it_should_match_with_dashes_when_constructed_with_dashes () {
		Option option = new BasicOption ("long-option");
		assertThat (option.matches ("long-option"), is (true));
	}

	@Test
	public void it_should_match_with_short_descriptions () {
		Option option = new BasicOption ("long-option", "l");
		assertThat (option.matches ("l"), is (true));
	}
	
	@Test
	public void it_should_not_match_with_wrong_short_option () {
		Option option = new BasicOption ("long-option", "l");
		assertThat (option.matches ("t"), is (false));
	}
	
	@Test
	public void it_should_not_match_if_there_is_no_short_option () {
		Option option = new BasicOption ("long-option");
		assertThat (option.matches ("l"), is (false));
	}

	@Test
	public void it_should_not_match_with_wrong_long_option () {
		Option option = new BasicOption ("long-option", "l");
		assertThat (option.matches ("longer option"), is (false));
	}

	@Test
	public void it_should_know_when_it_is_required () {
		Option required = new BasicOption ("long option", true);
		assertThat (required.isRequired (), is (true));

		Option optional = new BasicOption ("long option", false);
		assertThat (optional.isRequired (), is (false));

		Option standard = new BasicOption ("long option");
		assertThat (standard.isRequired (), is (false));
	}

	@Test
	public void it_should_handle_both_dashes_and_spaces_in_representation () {
		Option spaces = new BasicOption ("long option");
		Option dashes = new BasicOption ("long-option");

		assertThat (spaces.equals (dashes), is (true));
		assertThat (dashes.equals (spaces), is (true));
	}

	@Test
	public void it_should_not_complain_if_one_option_has_no_short_representation () {
		Option first = new BasicOption ("long option");
		Option second = new BasicOption ("long option", "s");

		assertThat (first.equals (second), is (true));
	}
	
	@Test
	public void it_should_not_equal_when_long_representations_are_different () {
		Option first = new BasicOption ("first option");
		Option second = new BasicOption ("second option", "s");
		
		assertThat (first.equals (second), is (false));
	}
	

}
