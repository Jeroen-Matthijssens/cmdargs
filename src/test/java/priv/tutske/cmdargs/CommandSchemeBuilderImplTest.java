package priv.tutske.cmdargs;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.tutske.cmdargs.*;


public class CommandSchemeBuilderImplTest {

	private static final ValueOption<String> name = new StringOption ("name", "n");
	private CommandScheme scheme;

	@Test
	public void it_should_give_a_scheme_that_has_defaults () {
		scheme = new CommandSchemeBuilderImpl ()
			.addOption (name, "Jhon")
			.buildScheme ();

		assertThat (scheme.hasDefault (name), is (true));
		assertThat (scheme.getDefault (name), is ("Jhon"));
	}

}
