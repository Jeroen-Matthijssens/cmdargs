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

	public String peek () { return args[pos]; }
	public String consume () { return args[pos++]; }
	public void skip () { consume (); }
	public boolean atEnd () { return pos == args.length; }
	public int size () { return args.length; }

	public TokenType typeOfNext () {
		String repr = peek ();
		if ( "--".equals (repr) ) { return TokenType.BREAK; }
		else if ( repr.startsWith ("--") ) { return TokenType.LONG; }
		else if (repr.startsWith ("-") ) { return TokenType.SHORT; }
		else { return TokenType.NONE; }
	}

}
