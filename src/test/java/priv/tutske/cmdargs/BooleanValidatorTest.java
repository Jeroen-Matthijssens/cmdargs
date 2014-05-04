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
	public void is_should_complain_about_string_that_do_not_represent_booleans () {
		assertThat (validator.isValid ("valid"), is (false));
	}

}
