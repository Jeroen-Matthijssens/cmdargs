package priv.tutske.cmdargs;

public class ArgsTokens {

	public static enum TokenType {
		SHORT, LONG, BREAK, NONE
	}

	private String [] args;
	private int pos;

	public ArgsTokens (String [] args) {
		this.args = args;
		pos = 0;
	}

	public String peek () { return args[pos].trim (); }
	public String consume () { return args[pos++].trim (); }
	public void skip () { consume (); }
	public boolean atEnd () { return pos == args.length; }
	public int size () { return args.length; }

	public TokenType typeOfNext () {
		String token = peek ();
		if ( "--".equals (token) ) { return TokenType.BREAK; }
		else if ( token.startsWith ("--") ) { return TokenType.LONG; }
		else if (token.startsWith ("-") ) { return TokenType.SHORT; }
		else { return TokenType.NONE; }
	}

}
