package priv.tutske.cmdargs;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

import org.tutske.cmdargs.Validator;


public class BooleanValidatorTest {

	Validator<Boolean> validator;

	@Before
	public void setUp() {
		this.validator = new BooleanValidator ();
	}

	@Test
	public void it_should_complain_about_string_that_do_not_represent_booleans () {
		assertThat (validator.isValid ("valid"), is (false));
	}

	@Test
	public void it_should_have_a_default_value () {
		assertThat (validator.hasDefault (), is (true));
	}

	@Test
	public void it_should_hove_true_as_its_default () {
		assertThat (validator.defaultValue (), is (true));
	}

	@Test
	public void it_should_have_false_as_its_default_when_reversed () {
		validator = new BooleanValidator (BooleanValidator.Type.Reversed);
		assertThat (validator.defaultValue (), is (false));
	}

}
