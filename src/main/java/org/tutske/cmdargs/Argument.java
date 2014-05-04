package org.tutske.cmdargs;


public interface Argument<T> {

	public T validateArgument (String arg);
	public boolean isRequired ();
	public String getRepresentation ();
	public int getPosition ();

}
