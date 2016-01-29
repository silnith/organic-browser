package org.silnith.browser.organic.parser.html4.token;

public abstract class EntityReference extends Token {

	private final String content;

	public EntityReference(final String content) {
		super();
		this.content = content;
	}

	@Override
	public Type getType() {
		return Type.ENTITY_REFERENCE;
	}

	public String getContent() {
		return content;
	}

	public abstract String getEntity();

}
