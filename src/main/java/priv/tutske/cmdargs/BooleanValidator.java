package priv.tutske.cmdargs;

import org.tutske.cmdargs.Validator;
import org.tutske.cmdargs.exceptions.WrongValueException;


public class BooleanValidator implements Validator<Boolean> {

	private Type type;

	static public enum Type {
		Normal, Reversed;
	}

	public BooleanValidator () {
		this (Type.Normal);
	}

	public BooleanValidator (Type type) {
		this.type = type;
	}

	@Override
	public boolean isValid (String value) {
		return normalize (value).matches ("|(true)|(yes)|(false)|(no)");
	}

	@Override
	public Boolean parse (String value) {
		if ( type == Type.Normal ) {return parseString (value); } 
		else { return ! parseString (value); }
	}

	private boolean parseString (String value) {
		value = normalize (value);

		if ( value.matches ("|(true)|(yes)") ) { return true; }
		if ( value.matches ("(false)|(no)") ) { return false; }

		String msg = "The value is not a boolean value (" + value + ")";
		throw new WrongValueException (msg);
	}

	private String normalize (String value) {
		return value.trim ().toLowerCase ();
	}

}
