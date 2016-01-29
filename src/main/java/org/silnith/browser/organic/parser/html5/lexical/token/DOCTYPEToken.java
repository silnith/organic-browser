package org.silnith.browser.organic.parser.html5.lexical.token;

public class DOCTYPEToken extends Token {

	private StringBuilder name;

	private StringBuilder publicIdentifier;

	private StringBuilder systemIdentifier;

	private boolean forceQuirks;

	public DOCTYPEToken() {
		super();
		this.name = null;
		this.publicIdentifier = null;
		this.systemIdentifier = null;
		this.forceQuirks = false;
	}

	public void setForceQuirks() {
		forceQuirks = true;
	}

	public void setName(final String name) {
		this.name = new StringBuilder(name);
	}

	public void setName(final char ch) {
		this.name = new StringBuilder(String.valueOf(ch));
	}

	public void setName(final char[] chars) {
		this.name = new StringBuilder(String.valueOf(chars));
	}

	public void appendToName(final char ch) {
		name.append(ch);
	}

	public void appendToName(final char[] ch) {
		name.append(ch);
	}

	public String getName() {
		if (name == null) {
			return null;
		} else {
			return name.toString();
		}
	}

	public void setPublicIdentifier(final String publicIdentifier) {
		this.publicIdentifier = new StringBuilder(publicIdentifier);
	}

	public void setPublicIdentifier(final char ch) {
		this.publicIdentifier = new StringBuilder(String.valueOf(ch));
	}

	public void setPublicIdentifier(final char[] publicIdentifier) {
		this.publicIdentifier = new StringBuilder(String.valueOf(publicIdentifier));
	}

	public void appendToPublicIdentifier(final char ch) {
		publicIdentifier.append(ch);
	}

	public void appendToPublicIdentifier(final char[] ch) {
		publicIdentifier.append(ch);
	}

	public String getPublicIdentifier() {
		if (publicIdentifier == null) {
			return null;
		} else {
			return publicIdentifier.toString();
		}
	}

	public void setSystemIdentifier(final String systemIdentifier) {
		this.systemIdentifier = new StringBuilder(systemIdentifier);
	}

	public void appendToSystemIdentifier(final char ch) {
		systemIdentifier.append(ch);
	}

	public void appendToSystemIdentifier(final char[] ch) {
		systemIdentifier.append(ch);
	}

	public String getSystemIdentifier() {
		if (systemIdentifier == null) {
			return null;
		} else {
			return systemIdentifier.toString();
		}
	}

	@Override
	public Type getType() {
		return Type.DOCTYPE;
	}

	@Override
	public String toString() {
		return "DOCTYPEToken <!DOCTYPE PUBLIC '" + publicIdentifier + "' '" + systemIdentifier + "'>";
	}

}
