package priv.tutske.cmdargs;

public class ReprNormalizer {

	private String [] reprs;
	
	public ReprNormalizer (String ... representations) {
		this.reprs = representations;
	}
	
	public String normalize () {
		StringBuilder builder = new StringBuilder ().append ("-");
		for ( String repr : reprs ) {
			builder.append("-").append (normalizeSingle (repr));
		}
		return builder.toString ();
	}

	private String normalizeSingle (String repr) {
		// Remove double spaces, and spaces around a dash.
		repr = repr.trim ().replaceAll ("\\s+", " ").replace (" -", "-").replace ("- ", "-");

		checkForErrors (repr);

		repr = repr.toLowerCase ().replaceAll (" ", "-");
		if ( repr.startsWith ("--") ) { return repr.substring (2); }
		return repr;
	}
	
	private void checkForErrors (String repr) {
		if ( repr.contains("\\s") && repr.contains ("-") ) {
			throw new RuntimeException ("can not contain spaces and whitespace: `" + repr + "`");
		}
		if ( repr.startsWith ("-") && ! repr.startsWith ("--") ) {
			throw new RuntimeException ("can not start with a single dash `" + repr + "`");
		}
		if ( repr.length () < 2 ) {
			throw new RuntimeException ("Must be longer than 1 character `" + repr + "`");
		}
	}

}
