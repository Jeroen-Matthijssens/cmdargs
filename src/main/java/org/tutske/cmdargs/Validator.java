package org.tutske.cmdargs;

public interface Validator<T> {

	/**
	 * Check whether the string can be parsed to an actual value.
	 * 
	 * @param value The value to be checked.
	 * @return 
	 */
	public boolean isValid (String value);

	/**
	 * Parse the value from the string to the actual representation.
	 * 
	 * @throws WrongValueException if `validate (value)` would have returned false;
	 * @param value The value to be converted to the actual value.
	 * @return
	 */
	public T parse (String value);

}
