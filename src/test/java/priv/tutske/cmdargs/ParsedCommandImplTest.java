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
		ValueOption<String> option = new StringValueOption ("name", "e");
		String value = "The Value";

		parsed.add (option, value);

		assertThat (parsed.isPresent (option), is (true));
		assertThat (parsed.getValue (option), is (value));
	}

	@Test
	public void it_should_find_the_value_with_the_option_name () {
		ValueOption<String> option = new StringValueOption ("name", "e");
		String value = "The Value";

		parsed.add (option, value);

		assertThat (parsed.isPresent ("name"), is (true));
		assertThat (parsed.getValue ("name"), is ((Object) value));
	}

	@Test
	public void it_should_find_its_command () {
		Command command = new MainCommand ();
		parsed.setCommand(command);;

		assertThat (parsed.getCommand (), is (command));
	}

}
