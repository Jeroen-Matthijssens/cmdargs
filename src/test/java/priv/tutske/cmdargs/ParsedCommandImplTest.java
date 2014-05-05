package priv.tutske.cmdargs;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

import org.tutske.cmdargs.*;


public class ParsedCommandImplTest {

	private ParsedCommandImpl parsed;

	@Before
	public void setup () {
		parsed = new ParsedCommandImpl ();
	}

	@Test
	public void it_should_find_the_value_with_the_option () {
		ValueOption<String> option = new StringValueOption ("name", "n");
		String value = "The Value";
		parsed.add (option, value);

		assertThat (parsed.isOptionPresent (option), is (true));
		assertThat (parsed.getOptionValue (option), is (value));
	}

	@Test
	public void it_should_find_the_option_with_new_created_option () {
		ValueOption<String> name = new StringValueOption ("name", "n");
		String value = "The Value";
		parsed.add (name, value);

		Option newname = new BasicOption ("name");
		assertThat (parsed.isOptionPresent (newname), is (true));
	}

	@Test
	public void it_should_find_the_value_with_new_created_option () {
		ValueOption<String> name = new StringValueOption ("name", "n");
		String value = "The Value";
		parsed.add (name, value);

		ValueOption<String> newname = new StringValueOption ("name");
		assertThat (parsed.isOptionPresent (newname), is (true));
	}

	@Test
	public void it_should_find_its_command () {
		Command command = new MainCommand ();
		parsed.setCommand(command);;

		assertThat (parsed.getCommand (), is (command));
	}

}
