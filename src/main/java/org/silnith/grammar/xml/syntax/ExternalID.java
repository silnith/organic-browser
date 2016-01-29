package org.silnith.grammar.xml.syntax;

public class ExternalID {

	public String pubidLiteral;

	public String systemLiteral;

	public ExternalID() {
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		if (pubidLiteral == null) {
			stringBuilder.append(" SYSTEM ");
			stringBuilder.append('"');
			stringBuilder.append(systemLiteral);
			stringBuilder.append('"');
		} else {
			stringBuilder.append(" PUBLIC ");
			stringBuilder.append('"');
			stringBuilder.append(pubidLiteral);
			stringBuilder.append('"');
			stringBuilder.append(' ');
			stringBuilder.append('"');
			stringBuilder.append(systemLiteral);
			stringBuilder.append('"');
		}
		return stringBuilder.toString();
	}

}
