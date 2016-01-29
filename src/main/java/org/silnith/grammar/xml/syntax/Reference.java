package org.silnith.grammar.xml.syntax;

public class Reference {

	public EntityRef entityRef;

	public CharRef charRef;

	public Reference() {
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		if (entityRef != null) {
			stringBuilder.append(entityRef);
		}
		if (charRef != null) {
			stringBuilder.append(charRef);
		}
		return stringBuilder.toString();
	}

}
