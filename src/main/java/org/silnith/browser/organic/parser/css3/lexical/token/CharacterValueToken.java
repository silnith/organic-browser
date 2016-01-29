package org.silnith.browser.organic.parser.css3.lexical.token;

public abstract class CharacterValueToken extends LexicalToken {

	private final char character;

	public CharacterValueToken(final char character) {
		super();
		this.character = character;
	}

	public char getCharacter() {
		return character;
	}

}
