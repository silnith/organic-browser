package org.silnith.grammar.xml.syntax;

public class DocTypeDecl {

	public String name;

	public ExternalID externalID;

	public IntSubset intSubset;

	public DocTypeDecl() {
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<!DOCTYPE ");
		stringBuilder.append(name);
		if (externalID != null) {
			stringBuilder.append(externalID);
		}
		if (intSubset != null) {
			stringBuilder.append(' ');
			stringBuilder.append('[');
			stringBuilder.append(intSubset);
			stringBuilder.append(']');
		}
		stringBuilder.append('>');
		return stringBuilder.toString();
	}

}
