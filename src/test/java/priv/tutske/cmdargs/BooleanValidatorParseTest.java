package priv.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.tutske.cmdargs.Validator;
import priv.tutske.cmdargs.BooleanValidator.Type;


@RunWith (Parameterized.class)
public class BooleanValidatorParseTest {

	private String value;
	private boolean expected;
	private Validator<Boolean> validator;

	public BooleanValidatorParseTest (String value, boolean expected, Type type) {
		if ( type == null) { this.validator = new BooleanValidator (); }
		else { this.validator = new BooleanValidator (type); }
		this.value = value;
		this.expected = expected;
	}

	@Parameters (name="`{0}` should parse to `{1}` when {2}")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"True", true, null}
			, {"False", false, null}
			, {"true", true, null}
			, {"false", false, null}
			, {"TRUE", true, null}
			, {"FALSE", false, null}
			, {"yes", true, null}
			, {"no", false, null}
			, {"Yes", true, null}
			, {"No", false, null}
			, {"YES", true, null}
			, {"NO", false, null}
			, {"", true, null}

			, {"True", true, Type.Normal}
			, {"False", false, Type.Normal}
			, {"true", true, Type.Normal}
			, {"false", false, Type.Normal}
			, {"TRUE", true, Type.Normal}
			, {"FALSE", false, Type.Normal}
			, {"yes", true, Type.Normal}
			, {"no", false, Type.Normal}
			, {"Yes", true, Type.Normal}
			, {"No", false, Type.Normal}
			, {"YES", true, Type.Normal}
			, {"NO", false, Type.Normal}
			, {"", true, Type.Normal}

			, {"True", false, Type.Reversed}
			, {"False", true, Type.Reversed}
			, {"true", false, Type.Reversed}
			, {"false", true, Type.Reversed}
			, {"TRUE", false, Type.Reversed}
			, {"FALSE", true, Type.Reversed}
			, {"yes", false, Type.Reversed}
			, {"no", true, Type.Reversed}
			, {"Yes", false, Type.Reversed}
			, {"No", true, Type.Reversed}
			, {"YES", false, Type.Reversed}
			, {"NO", true, Type.Reversed}
			, {"", false, Type.Reversed}
		});
	}

	@Test
	public void it_should_say_it_is_valid () {
		assertThat (validator.isValid (value), is (true));
	}

	@Test
	public void it_should_parse_the_value () {
		assertThat (validator.parse (value), is (expected));
	}

}
