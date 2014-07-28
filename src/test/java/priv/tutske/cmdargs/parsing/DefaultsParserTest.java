package priv.tutske.cmdargs.parsing;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.CommandLineException;
import priv.tutske.cmdargs.ParserImpl;


public class DefaultsParserTest {

	private static final ValueOption<String> name = new StringOption ("name", "n");
	private static final ValueOption<Long> age = new NumberOption ("age", "a");
	private static final ValueOption<Boolean> test = new BooleanOption ("test", "t");
	private static final ValueOption<String> required = new StringOption (
		"required", "r", Option.Requirement.RequireValue
	);

	private CommandScheme scheme = CommandSchemeBuilderFactory.newInstance ()
		.addOption (name, "Jhon")
		.addOption (required, "required")
		.addOption (age)
		.addOption (test, true)
		.buildScheme ();

	private Parser parser;

	@Before
	public void setup () {
		parser = new ParserImpl (scheme);
	}

	@Test
	public void it_should_know_about_defaulted_values () {
		String [] args = new String [] {};

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.getOptionValue (name), is ("Jhon"));
	}

	@Test
	public void it_should_pick_up_values_even_if_there_is_a_default () {
		String [] args = "--name=Jhon".split (" ");

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.getOptionValue (name), is ("Jhon"));
	}

	@Test
	public void it_should_say_defaulted_boolean_is_not_present_when_not_given () {
		String [] args = "--name".split (" ");

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.hasOption (test), is (false));
	}

	@Test
	public void it_should_say_defaulted_boolean_is_false_when_cmd_provided_false_with_assignment () {
		String [] args = "--test=false".split (" ");

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.hasOption (test), is (true));
		assertThat (parsed.getOptionValue (test), is (false));
	}

	@Test
	public void it_should_say_defaulted_boolean_is_true_when_cmd_provided_true_with_assignment () {
		String [] args = "--test=true".split (" ");

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.hasOption (test), is (true));
		assertThat (parsed.getOptionValue (test), is (true));
	}

	@Test
	public void it_should_say_defaulted_boolean_is_true_when_cmd_provided_true () {
		String [] args = "--test".split (" ");

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.hasOption (test), is (true));
		assertThat (parsed.getOptionValue (test), is (true));
	}

	@Test
	public void it_should_say_defaulted_boolean_is_false_when_cmd_provided_false () {
		String [] args = "--no-test".split (" ");
		ParsedCommand parsed = new ParserImpl (scheme).parse (args);
		assertThat (parsed.getOptionValue (test), is (false));

		args = "--not-test".split (" ");
		parsed = new ParserImpl (scheme).parse (args);
		assertThat (parsed.getOptionValue (test), is (false));
	}

	@Test
	public void it_should_say_the_default_value_when_not_given () {
		String [] args = "--name=jhon".split (" ");

		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.hasOption (test), is (false));
		assertThat (parsed.getOptionValue (test), is (scheme.getDefault (test)));
	}

	@Test (expected = CommandLineException.class)
	public void it_should_still_throw_exceptions_when_values_are_required () {
		String [] args = "--required".split (" ");
		parser.parse (args);
	}

}
