package priv.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;
import org.tutske.cmdargs.Command;

import priv.tutske.cmdargs.CommandImpl;

public class CommandImplTest {

	Command cmd;

	@Before
	public void setup () {
		cmd = new CommandImpl ("sub");
	}

	@Test
	public void it_should_know_its_representation () {
		assertThat (cmd.getRepresentation (), is ("sub"));
	}

	@Test
	public void it_should_know_its_options_scheme () {
		assertThat (cmd.getOptionScheme (), not (nullValue ()));
	}

	@Test
	public void it_should_match_its_representation () {
		assertThat (cmd.matches ("sub"), is (true));
	}

	@Test
	public void it_should_lower_case_representations () {
		Command cmd = new CommandImpl ("SuB");
		assertThat (cmd.getRepresentation (), is ("sub"));
	}

	@Test (expected = IllegalArgumentException.class)
	public void it_should_complain_when_command_is_null () {
		new CommandImpl (null);
		fail ("SubCommand should not allow the command string to be null");
	}

	@Test (expected = IllegalArgumentException.class)
	public void it_should_complain_when_command_is_empty_string () {
		new CommandImpl ("");
		fail ("SubCommand should not allow the command string to be empty");
	}

	@Test (expected = IllegalArgumentException.class)
	public void it_should_complain_when_command_contains_spaces () {
		new CommandImpl ("sub command");
		fail ("Subcommands can not contain spaces");
	}

}
