package priv.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;

@RunWith (Parameterized.class)
public class SimpleParserPresenceTest {

	private String [] args;
	private String option;
	private boolean present;
	private Parser parser;

	public SimpleParserPresenceTest (String cmd, String option, boolean present) {
		this.args = cmd.split (" ");
		this.option = option;
		this.present = present;
	}

	@Parameters (name="`{1}` in `{0}` should be {2}")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"--verbose", "verbose", true}
			, {"--path", "path", true}
			, {"--PATH", "path", true}
			, {"--enabled", "enabled", true}
			, {"--two-words", "two words", true}

			, {"--not-enabled", "enabled", true}
			, {"--NOT-ENABLED", "enabled", true}
			, {"--layout=simple", "layout", true}
			, {"--LAYOUT=simple", "layout", true}

			, {"-H", "human readable", true}
			, {"-h", "help", true}
			, {"-h", "human readable", false}
			, {"-vH", "verbose", true}
			, {"-vH", "human readable", true}
			, {"-vH", "help", false}
			, {"-hH", "human readable", true}
			, {"-hH", "help", true}

			, {"--enabled --recursive --verbose", "enabled", true}
			, {"--enabled --recursive --verbose", "recursive", true}
			, {"--enabled --recursive --verbose", "verbose", true}

			, {"-p path/to/dir --recursive", "path", true}
			, {"-p path/to/dir --not-recursive", "recursive", true}

			, {"--verbose -p path/to/dir", "verbose", true}
			, {"--verbose -p path/to/dir -Hh", "path", true}
		});
	}

	@Before
	public void setup () throws CommandLineException {
		CommandSchemeBuilder schemeBuilder = new CommandSchemeBuilder ();

		// Basic options.
		schemeBuilder.add (new BasicOption ("verbose", "v"));
		schemeBuilder.add (new BasicOption ("human readable", "H"));
		schemeBuilder.add (new BasicOption ("two words", "t"));
		// Value options
		schemeBuilder.add (new StringValueOption ("path", "p"));
		schemeBuilder.add (new StringValueOption ("layout", "l"));
		// Boolean options
		schemeBuilder.add (new BooleanOption ("enabled"));
		schemeBuilder.add (new BooleanOption ("recursive"));
		// Pretend this is real.
		schemeBuilder.add (new BasicOption ("help", "h"));

		CommandScheme scheme = schemeBuilder.buildScheme ();
		parser = new CmdSchemeParser (scheme);

		parser.parse (args);
	}

	@Test
	public void it_should_have_the_option () {
		ParsedCommand options = parser.getOptions ();
		assertThat (options.isPresent (option), is (present));
	}

}
