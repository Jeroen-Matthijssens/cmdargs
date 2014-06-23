package priv.tutske.cmdargs;


public class ReprNormalizer {

	private static final String BOTH = "Connot contain spaces and dashes: `%s`";
	private static final String SINGLE_DASH = "Connat start with a single dash: `%s`";
	private static final String TO_SHORT = "Must be longer than 1 character: `%s`";

	private String [] reprs;

	public ReprNormalizer (String ... representations) {
		this.reprs = representations;
	}

	public boolean isShort () {
		if ( reprs == null ) { return true; }
		if ( reprs.length != 1 ) { return false; }
		if ( reprs[0] == null ) { return true; }
		String repr = reprs[0];
		return repr.length () < 2 || (repr.length () < 3 && repr.startsWith ("-"));
	}

	public String getLong () {
		StringBuilder builder = new StringBuilder ().append ("-");
		for ( String repr : reprs ) {
			builder.append("-").append (normalizeSingle (repr));
		}
		return builder.toString ();
	}

	public String getShort () {
		if ( reprs == null || reprs[0] == null ) { return ""; }
		String repr = reprs[0].trim ();
		if ( repr.length () == 0 || repr.startsWith ("-") ) { return repr; }
		return "-" + repr;
	}

	private String normalizeSingle (String repr) {
		if ( repr == null ) { return ""; }
		// Remove double spaces, and spaces around a dash.
		repr = repr.trim ().replaceAll ("\\s+", " ");
		repr = repr.replace (" -", "-").replace ("- ", "-");

		checkForErrors (repr);

		repr = repr.toLowerCase ().replaceAll (" ", "-");
		if ( repr.startsWith ("--") ) { return repr.substring (2); }
		return repr;
	}

	private void checkForErrors (String repr) {
		if ( isShort () ) { return; }
		if ( repr.contains("\\s") && repr.contains ("-") ) {
			throw new RuntimeException (String.format (BOTH, repr));
		}
		if ( repr.startsWith ("-") && ! repr.startsWith ("--") ) {
			throw new RuntimeException (String.format (SINGLE_DASH, repr));
		}
		if ( repr.length () < 2 ) {
			throw new RuntimeException (String.format (TO_SHORT, repr));
		}
	}

}
