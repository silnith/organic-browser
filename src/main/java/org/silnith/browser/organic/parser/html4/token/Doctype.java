package org.silnith.browser.organic.parser.html4.token;

public class Doctype extends Token {

	private final String name;

	private final String publicId;

	private final String systemId;

	public Doctype(final String name, final String publicId, final String systemId) {
		super();
		this.name = name;
		this.publicId = publicId;
		this.systemId = systemId;
	}

	@Override
	public Type getType() {
		return Type.DOCTYPE;
	}

	public String getName() {
		return name;
	}

	public String getPublicId() {
		return publicId;
	}

	public String getSystemId() {
		return systemId;
	}

}
