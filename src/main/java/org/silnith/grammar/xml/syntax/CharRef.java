package org.silnith.grammar.xml.syntax;

public class CharRef {

	public int codePoint;

	public CharRef() {
	}

	@Override
	public String toString() {
		return "&#" + codePoint + ';';
	}

}
