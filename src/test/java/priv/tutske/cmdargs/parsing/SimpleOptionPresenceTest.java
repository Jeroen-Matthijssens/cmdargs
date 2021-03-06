package priv.tutske.cmdargs.parsing;

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
import priv.tutske.cmdargs.ParserImpl;


@RunWith (Parameterized.class)
public class SimpleOptionPresenceTest {

	private String [] args;
	private String option;
	private boolean present;
	private Parser parser;

	public SimpleOptionPresenceTest (String cmd, String option, boolean present) {
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
			, {"--enabled=false", "enabled", true}
			, {"--enabled=true", "enabled", true}
			, {"--no-enabled", "enabled", true}
			, {"--NO-ENABLED", "enabled", true}
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
	public void setup () {
		CommandScheme scheme = new SchemeBuilderSimple ().build ();
		parser = new ParserImpl (scheme);
	}

	@Test
	public void it_should_have_the_option () {
		ParsedCommand options = parser.parse (args);
		Option opt = new BasicOption (option);
		assertThat (options.hasOption (opt), is (present));
	}

}
