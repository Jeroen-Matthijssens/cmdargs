package org.tutske.cmdargs;


public interface Option {

	public String getRepresentation ();
	public String getDescription ();
	public boolean hasShortRepresentation ();
	public String getShortRepresentation ();

	public boolean matches (String representation);
	public boolean isRequired ();

}
