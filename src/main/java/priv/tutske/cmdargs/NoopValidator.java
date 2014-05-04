package priv.tutske.cmdargs;

import org.tutske.cmdargs.Validator;


public class NoopValidator implements Validator<String> {

	@Override
	public boolean isValid (String value) {
		return true;
	}

	@Override
	public String parse (String value) {
		return value;
	}

}
