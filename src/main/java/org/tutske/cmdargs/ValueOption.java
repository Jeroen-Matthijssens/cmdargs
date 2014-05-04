package org.tutske.cmdargs;


public interface ValueOption<T> extends Option {

	public Validator<T> getValidator (String representation);

}
