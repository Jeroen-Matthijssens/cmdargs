package priv.tutske.cmdargs.parsing;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;
import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;

import priv.tutske.cmdargs.CmdSchemeParser;


@RunWith (Parameterized.class)
public class SimpleParserUnkownOptionTest {

	private String [] args;
	private Parser parser;

	public SimpleParserUnkownOptionTest (String cmd) {
		this.args = cmd.split (" ");
	}

	@Parameters
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"--unknown"}
			, {"--verbose --unknown"}
			, {"--unknown --verbose"}
			, {"--unknown=something"}
			, {"-V --unknown"}
			, {"-V --verbose"}
			, {"--verbose -V"}
		});
	}

	@Before
	public void setup () throws CommandLineException {
		CommandScheme scheme = new SchemeBuilderSimple ().build ();
		parser = new CmdSchemeParser (scheme);
	}

	@Test (expected = UnknownOptionException.class)
	public void it_should_complain_about_unknown_options ()
	throws CommandLineException {
		parser.parse (args);
		fail ("parser should complain when an option is not known");
	}

}
