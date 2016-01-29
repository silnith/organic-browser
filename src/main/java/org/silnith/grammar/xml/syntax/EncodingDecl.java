package org.silnith.grammar.xml.syntax;

public class EncodingDecl {

	public EncName encName;

	public EncodingDecl() {
	}

	@Override
	public String toString() {
		return " encoding=\"" + encName + '"';
	}

}
