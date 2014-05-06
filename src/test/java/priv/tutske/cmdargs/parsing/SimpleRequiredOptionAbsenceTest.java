package priv.tutske.cmdargs.parsing;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;
import priv.tutske.cmdargs.ParserImpl;


@RunWith (Parameterized.class)
public class SimpleRequiredOptionAbsenceTest {

	private String [] args;
	private Parser parser;

	public SimpleRequiredOptionAbsenceTest (String cmd) {
		this.args = cmd.split (" ");
	}

	@Parameters (name="`{0}` is missing required value option")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{""}
			, {"-v --two-words"}
			, {"-H"}
			, {"--path"} /* no value given. */
		});
	}

	@Before
	public void setup () {
		CommandScheme scheme = new SchemeBuilderSimple ().buildWithRequired ();
		parser = new ParserImpl (scheme);
	}

	@Ignore ("Not implemented yet.")
	@Test (expected = CommandLineException.class)
	public void it_should_complain_about_missing_required_option () {
		parser.parse (args);
		fail ("Failed to complain about missing required option.");
	}

}
