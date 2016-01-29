package org.silnith.browser.organic.parser.html5.lexical.token;

public class EndTagToken extends TagToken {

	public EndTagToken() {
		super();
	}

	@Override
	public Type getType() {
		return Type.END_TAG;
	}

	@Override
	public String toString() {
		final StringBuilder value = new StringBuilder();
		value.append("EndTagToken ");
		value.append('<');
		value.append('/');
		value.append(getTagName());
		for (final Attribute attribute : getAttributes()) {
			value.append(' ');
			value.append(attribute.toString());
		}
		if (isSelfClosing()) {
			value.append('/');
		}
		value.append('>');
		return value.toString();
	}

}
