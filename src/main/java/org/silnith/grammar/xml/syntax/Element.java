package org.silnith.grammar.xml.syntax;

public class Element {

	public EmptyElemTag emptyElemTag;

	public STag sTag;

	public Content content;

	public ETag eTag;

	public Element() {
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		if (emptyElemTag == null) {
			stringBuilder.append(sTag);
//			stringBuilder.append('\n');
			stringBuilder.append(content);
//			stringBuilder.append('\n');
			stringBuilder.append(eTag);
		} else {
			stringBuilder.append(emptyElemTag);
		}
		return stringBuilder.toString();
	}

}
