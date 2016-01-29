package org.silnith.grammar.xml.syntax;

import java.util.List;

public class STag {

	public String name;

	public List<Attribute> attributeList;

	public STag() {
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append('<');
		stringBuilder.append(name);
		if (attributeList != null) {
			for (final Attribute attribute : attributeList) {
				stringBuilder.append(' ');
				stringBuilder.append(attribute);
			}
		}
		stringBuilder.append('>');
		return stringBuilder.toString();
	}

}
