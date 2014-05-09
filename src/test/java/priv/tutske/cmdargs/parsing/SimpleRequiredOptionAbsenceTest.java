package priv.tutske.cmdargs.parsing;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.*;

import java.util.Collection;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;
import priv.tutske.cmdargs.ParserImpl;


@RunWith (Parameterized.class)
public class SimpleRequiredOptionAbsenceTest {

	private String [] args;
	private Class<?> clazz;
	private Parser parser;

	public SimpleRequiredOptionAbsenceTest (String cmd, Class<?> clazz) {
		this.args = cmd.split (" ");
		this.clazz = clazz;
	}

	@Parameters (name="`{0}` is missing required value option")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"", MissingOptionException.class}
			, {"-v --two-words", MissingOptionException.class}
			, {"-H", MissingOptionException.class}
			, {"--path", OptionValueException.class}
		});
	}

	@Before
	public void setup () {
		CommandScheme scheme = new SchemeBuilderSimple ().buildWithRequired ();
		parser = new ParserImpl (scheme);
	}

	@Test
	public void it_should_complain_about_missing_required_option () {
		try { parser.parse (args); }
		catch (Exception e) {
			assertThat (e, instanceOf (clazz));
			return;
		}
		fail ("Failed to complain about missing required option!");
	}

}
