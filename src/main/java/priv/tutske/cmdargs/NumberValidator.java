package priv.tutske.cmdargs;

import org.tutske.cmdargs.Validator;


public class NumberValidator implements Validator<Long> {

	private static Validator<Long> INSTANCE = new NumberValidator ();

	public static Validator<Long> newInstance () {
		return INSTANCE;
	}

	private NumberValidator () {
	}

	@Override
	public boolean isValid (String value) {
		return value.matches ("\\d{1,19}");
	}

	@Override
	public Long parse (String value) {
		return Long.parseLong (value);
	}

}
