package priv.tutske.cmdargs;

import org.tutske.cmdargs.Validator;


public class NoopValidator implements Validator<String> {

	private static Validator<String> INSTANCE = new NoopValidator ();

	public static Validator<String> newInstance () {
		return INSTANCE;
	}

	private NoopValidator () {
	}

	@Override
	public boolean isValid (String value) {
		return true;
	}

	@Override
	public String parse (String value) {
		return value;
	}

	@Override
	public boolean hasDefault () {
		return false;
	}

	@Override
	public String defaultValue () {
		throw new RuntimeException ("no default value");
	}

}
