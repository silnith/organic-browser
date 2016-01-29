package org.silnith.grammar.xml.syntax;

public class ETag {

	public String name;

	public ETag() {
	}

	@Override
	public String toString() {
		return "</" + name + '>';
	}

}
