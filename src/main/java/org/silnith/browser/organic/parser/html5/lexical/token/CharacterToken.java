package org.silnith.browser.organic.parser.html5.lexical.token;

import java.util.ArrayList;
import java.util.List;

public class CharacterToken extends Token {

	public static List<Token> toTokens(final char... characters) {
		final List<Token> tokens = new ArrayList<>();
		for (final char character : characters) {
			tokens.add(new CharacterToken(character));
		}
		return tokens;
	}

	public static List<Token> toTokens(final String string) {
		return toTokens(string.toCharArray());
	}

	public static List<Token> toTokens(final CharSequence charSequence) {
		return toTokens(charSequence.toString());
	}

	private final char character;

	public CharacterToken(final char character) {
		super();
		this.character = character;
	}

	public char getCharacter() {
		return character;
	}

	@Override
	public Type getType() {
		return Type.CHARACTER;
	}

	@Override
	public String toString() {
		return "char '" + character + "'";
	}

}
