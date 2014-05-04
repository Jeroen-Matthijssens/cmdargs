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
public class SimpleParserValueTest {

	private String [] args;
	private String option;
	private Object value;
	private Parser parser;

	public SimpleParserValueTest (String cmd, String option, Object value) {
		this.args = cmd.split (" ");
		this.option = option;
		this.value = value;
	}

	@Parameters (name="`{1}` in `{0}` should be `{2}`")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"--not-enabled", "enabled", false}
			, {"--NOT-ENABLED", "enabled", false}
			, {"--enabled", "enabled", true}
			, {"--enabled=true", "enabled", true}
			, {"--enabled=false", "enabled", false}

			, {"--layout=simple", "layout", "simple"}
			, {"--LAYOUT=simple", "layout", "simple"}
			, {"--layout=SIMPLE", "layout", "SIMPLE"}

			, {"-p path/to/dir --recursive", "path", "path/to/dir"}
			, {"-p path/to/dir --not-recursive", "recursive", false}
			, {"--path=path/to/dir --layout=simple", "path", "path/to/dir"}
			, {"--path=path/to/dir --layout=simple", "layout", "simple"}
			, {"--path=path/to/dir -vH", "path", "path/to/dir"}
		});
	}

	@Before
	public void setup () throws CommandLineException {
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
		parser = new CmdSchemeParser (scheme);
	}

	@Test
	public void it_should_have_the_option () {
		ParsedCommand options = parser.parse (args);
		assertThat (options.getValue (option), is ((Object) value));
	}

}
