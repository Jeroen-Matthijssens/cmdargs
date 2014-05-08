package org.tutske.cmdargs;

public interface Validator<T> {

	public boolean isValid (String value);
	public T parse (String value);
	public boolean hasDefault ();
	public T defaultValue ();

}
