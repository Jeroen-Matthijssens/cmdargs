package priv.tutske.cmdargs.parsing;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;
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
public class SimpleOptionListPresenceTest {

	private String [] args;
	private String optname;
	private List<Object> values;

	private CommandScheme scheme;
	private Parser parser;

	public SimpleOptionListPresenceTest (String args, String optname, Object [] values) {
		this.args = args.split (" ");
		this.optname = optname;
		this.values = Arrays.asList (values);
	}

	@Parameters (name="`{1}` in `{0}` should be {2}")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{"--number=1,2,3", "number", new Long [] {1L, 2L, 3L}}
			, {"--number=1 --number=2 --number=3", "number", new Long [] {1L, 2L, 3L}}
			, {"--layout=flat,colorful", "layout", new String [] {"flat", "colorful"}}
			, {"--layout=flat --layout=colorful", "layout", new String [] {"flat", "colorful"}}
			, {"-l flat -l colorful", "layout", new String [] {"flat", "colorful"}}
			, {"-n 1 -n 2 -n 3", "number", new Long [] {1L, 2L, 3L}}
		});
	}

	@Before
	public void setup () {
		scheme = new SchemeBuilderSimple ().build ();
		parser = new ParserImpl (scheme);
	}

	@Test
	public void it_should_have_the_list_with_values () {
		 ParsedCommand parsed = parser.parse (args);
		 ValueOption<Object> option = (ValueOption<Object>) scheme.getOption (optname);
		 assertThat (parsed.getOptionValues (option), is (values));
	}

}
