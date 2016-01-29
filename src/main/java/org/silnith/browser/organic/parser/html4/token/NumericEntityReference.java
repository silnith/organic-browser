package org.silnith.browser.organic.parser.html4.token;

import java.util.regex.Pattern;

public class NumericEntityReference extends EntityReference {

	private static final Pattern PATTERN = Pattern.compile("[0-9]+");

	private final String entity;

	public NumericEntityReference(final String content) {
		super(content);
		if ( !PATTERN.matcher(content).matches()) {
			throw new IllegalArgumentException("Numeric character references may only contain digits: " + content);
		}
		this.entity = String.valueOf(Character.toChars(Integer.parseInt(content)));
	}

	@Override
	public String getEntity() {
		return entity;
	}

}
