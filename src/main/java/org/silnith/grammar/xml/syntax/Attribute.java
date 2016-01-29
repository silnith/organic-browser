package org.silnith.grammar.xml.syntax;

public class Attribute {

	public String name;

	public AttValue attValue;

	public Attribute() {
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(name);
		stringBuilder.append('=');
		stringBuilder.append('"');
		stringBuilder.append(attValue);
		stringBuilder.append('"');
		return stringBuilder.toString();
	}

}
