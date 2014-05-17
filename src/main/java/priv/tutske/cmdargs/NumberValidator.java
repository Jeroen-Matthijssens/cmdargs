package priv.tutske.cmdargs;

import org.tutske.cmdargs.Validator;


public class NumberValidator implements Validator<Long> {

	private static final String NUMBER_SEQUENCE = "\\d{1,19}";
	private static Validator<Long> INSTANCE = new NumberValidator ();

	public static Validator<Long> newInstance () {
		return INSTANCE;
	}

	private NumberValidator () {
	}

	@Override
	public boolean isValid (String value) {
		return value.matches (NUMBER_SEQUENCE);
	}

	@Override
	public Long parse (String value) {
		return Long.parseLong (value);
	}

	@Override
	public boolean hasDefault () {
		return false;
	}

	@Override
	public Long defaultValue () {
		throw new RuntimeException ("no default value");
	}

}
