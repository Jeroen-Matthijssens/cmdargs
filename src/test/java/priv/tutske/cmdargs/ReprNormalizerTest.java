package priv.tutske.cmdargs;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;


@RunWith (Parameterized.class)
public class ReprNormalizerTest {

	private String [] inputs;
	private String expected;

	public ReprNormalizerTest (String [] inputs, String expected) {
		this.inputs = inputs;
		this.expected = expected;
	}

	@Parameters (name="`{0}` has command `{1}`")
	public static Collection<Object []> arguments () {
		return Arrays.asList (new Object [] [] {
			{new String [] {"option"}, "--option"}
			, {new String [] {"long option"}, "--long-option"}
			, {new String [] {"long    option"}, "--long-option"}
			, {new String [] {"--long-option"}, "--long-option"}
			, {new String [] {"long-option"}, "--long-option"}
			, {new String [] {"long - option"}, "--long-option"}
			, {new String [] {"long  -  option"}, "--long-option"}
			, {new String [] {"long\t-  option"}, "--long-option"}
			, {new String [] {"long \t -  \toption"}, "--long-option"}
			, {new String [] {"  long-option"}, "--long-option"}
			, {new String [] {"long-option   "}, "--long-option"}
			, {new String [] {"  long-option   "}, "--long-option"}
			, {new String [] {"--long-option   "}, "--long-option"}
			, {new String [] {"    --long-option   "}, "--long-option"}
			, {new String [] {"long", "option"}, "--long-option"}
			, {new String [] {"--long", "--option"}, "--long-option"}
			, {new String [] {"--realy-long", "--option"}, "--realy-long-option"}
			, {new String [] {"realy long", "option"}, "--realy-long-option"}
			, {new String [] {"realy  - long", "  option"}, "--realy-long-option"}
		});
	}

	@Test
	public void it_should_normalize_the_input () {
		ReprNormalizer normalizer = new ReprNormalizer (inputs);
		assertThat (normalizer.normalize (), is (expected));
	}

}
