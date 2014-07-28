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
		ValueOption<String> option = new StringOption ("name", "n");
		String value = "The Value";
		parsed.addOption (option, value);

		assertThat (parsed.hasOption (option), is (true));
		assertThat (parsed.getOptionValue (option), is (value));
	}

	@Test
	public void it_should_find_the_option_with_new_created_option () {
		ValueOption<String> name = new StringOption ("name", "n");
		String value = "The Value";
		parsed.addOption (name, value);

		Option newname = new BasicOption ("name");
		assertThat (parsed.hasOption (newname), is (true));
	}

	@Test
	public void it_should_find_the_value_with_new_created_option () {
		ValueOption<String> name = new StringOption ("name", "n");
		String value = "The Value";
		parsed.addOption (name, value);

		ValueOption<String> newname = new StringOption ("name");
		assertThat (parsed.hasOption (newname), is (true));
	}

	@Test
	public void it_should_find_its_command () {
		Command command = new CommandImpl ("MAIN");
		parsed.setCommand (command);

		assertThat (parsed.getCommand (), is (command));
	}

	@Test
	public void it_should_find_the_default_values () {
		ValueOption<String> option = new StringOption ("name", "n");
		String standard = "standard value";
		parsed.addDefaultedOption (option, standard);

		assertThat (parsed.getOptionValue (option), is (standard));
	}

	@Test
	public void it_should_say_option_is_not_present_if_it_is_a_default () {
		ValueOption<String> option = new StringOption ("name", "n");
		String standard = "standard value";
		parsed.addDefaultedOption(option,standard);

		assertThat (parsed.hasOption (option), is (false));
	}

	@Test
	public void it_should_give_a_default_value_if_no_option_is_there () {
		ValueOption<String> option = new StringOption ("name", "n");
		String standard = "standard value";

		assertThat (parsed.getOptionValue (option, standard), is (standard));
	}

	@Test
	public void it_should_give_the_real_value_even_if_defaul_is_specified () {
		ValueOption<String> option = new StringOption ("name", "n");
		String standard = "standard value";
		String real = "real value";

		parsed.addOption (option, real);

		assertThat (parsed.getOptionValue (option, standard), is (real));
	}

	@Test
	public void it_should_pick_method_defaul_over_the_construction_default () {
		ValueOption<String> option = new StringOption ("name", "n");
		String method = "method value";
		String construction = "construction value";

		parsed.addDefaultedOption (option, construction);

		assertThat (parsed.getOptionValue (option, method), is (method));
	}

}
