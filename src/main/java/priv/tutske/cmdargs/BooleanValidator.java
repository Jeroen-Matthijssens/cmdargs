package priv.tutske.cmdargs;

import org.tutske.cmdargs.Validator;


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
		if ( value == null ) { return false; }
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

		throw new IllegalArgumentException (value);
	}

	private String normalize (String value) {
		return value.trim ().toLowerCase ();
	}

}
