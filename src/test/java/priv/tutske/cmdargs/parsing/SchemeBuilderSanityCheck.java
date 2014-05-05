package priv.tutske.cmdargs.parsing;

import org.junit.Test;


public class SchemeBuilderSanityCheck {

	@Test
	public void it_should_build_the_complex_scheme_propertly () {
		new SchemeBuilderComplex ().build ();
	}

	@Test
	public void it_should_build_the_simple_scheme_propertly () {
		new SchemeBuilderSimple ().build ();
	}

}
