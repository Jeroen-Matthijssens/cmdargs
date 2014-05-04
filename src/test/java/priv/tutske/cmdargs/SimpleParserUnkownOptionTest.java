package priv.tutske.cmdargs;

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


@RunWith (Parameterized.class)
public class SimpleParserUnkownOptionTest {

	private String [] args;
	private ParserImpl parser;

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
	public void setup () {
		CommandSchemeBuilder schemeBuilder = new CommandSchemeBuilder ();

		// Basic options.
		schemeBuilder.add (new BasicOption ("verbose", "v"));
		schemeBuilder.add (new BasicOption ("human readable", "H"));
		// Value options
		schemeBuilder.add (new StringValueOption ("path", "p"));
		schemeBuilder.add (new StringValueOption ("layout", "l"));
		// Boolean options
		schemeBuilder.add (new BooleanOption ("enabled"));
		schemeBuilder.add (new BooleanOption ("recursive"));
		// Pretend this is real.
		schemeBuilder.add (new BasicOption ("help", "h"));

		CommandScheme scheme = schemeBuilder.buildScheme ();
		parser = new ParserImpl (scheme);
	}

	@Test (expected = UnknownOptionException.class)
	public void it_should_complain_about_unknown_options ()
	throws CommandLineException {
		parser.parse (args);
		fail ("parser should complain when an option is not known");
	}

}
