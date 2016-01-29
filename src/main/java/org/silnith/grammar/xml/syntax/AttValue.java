package org.silnith.grammar.xml.syntax;

import java.util.List;

public class AttValue {

	public List<Object> contents;

	public AttValue() {
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (final Object obj : contents) {
			stringBuilder.append(obj);
		}
		return stringBuilder.toString();
	}

}
