package org.tutske.cmdargs;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.*;


public class StringArgumentTest {

	@Before
	public void setUp () throws Exception {}

	@Test
	public void sanity_instantiating_check () {
		new StringArgument ("name", 0);
	}

	@Test
	@SuppressWarnings ({"rawtypes", "unchecked"})
	public void it_should_order_itself_by_position () {
		Argument first = new StringArgument ("first", 0);
		Argument second = new StringArgument ("second", 1);

		assertThat (second, greaterThan (first));
	}

}
