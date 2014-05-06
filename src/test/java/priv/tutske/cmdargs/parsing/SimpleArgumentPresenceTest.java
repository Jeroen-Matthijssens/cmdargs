package priv.tutske.cmdargs.parsing;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.tutske.cmdargs.*;
import priv.tutske.cmdargs.*;


@RunWith (Parameterized.class)
public class SimpleArgumentPresenceTest {

	private String [] args;
	private String argname;
	private Object expected;

	private Parser parser;
	private Argument<?> argument;

	public SimpleArgumentPresenceTest (String args, String argname, Object expected) {
		this.args = args.split (" ");
		this.argname = argname;
		this.expected = expected;
	}

	@Parameters (name="globals: `{1}` in `{0}` should be `{2}`")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"TheValue second", "first", "TheValue"}
			, {"first TheValue", "second", "TheValue"}
			
			, {"TheValue second third", "first", "TheValue"}
			, {"first TheValue third", "second", "TheValue"}

			, {"first second TheValue", "third", "TheValue"}
		});
	}

	@Before
	public void setup () {
		CommandScheme cmdscheme = new SchemeBuilderSimple ().buildWitArguments ();
		argument = cmdscheme.getArgument (argname);
		parser = new ParserImpl (cmdscheme);
	}

	@Test
	public void it_should_recognize_its_arguments () {
		ParsedCommand parsed = parser.parse (args);
		assertThat (parsed.getArgumentValue (argument), is (expected));
	}

}
