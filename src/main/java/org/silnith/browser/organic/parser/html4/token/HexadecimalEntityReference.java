package org.silnith.browser.organic.parser.html4.token;

import java.util.regex.Pattern;

public class HexadecimalEntityReference extends EntityReference {

	private static final Pattern PATTERN = Pattern.compile("[0-9a-fA-F]+");

	private final String entity;

	public HexadecimalEntityReference(final String content) {
		super(content);
		if ( !PATTERN.matcher(content).matches()) {
			throw new IllegalArgumentException("Hexadecimal numeric character references may only contain hexadecimal digits: " + content);
		}
		this.entity = String.valueOf(Character.toChars(Integer.parseInt(content, 16)));
	}

	@Override
	public String getEntity() {
		return entity;
	}

}
