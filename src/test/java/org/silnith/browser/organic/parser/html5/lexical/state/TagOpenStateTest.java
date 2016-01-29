package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;

public class TagOpenStateTest {

	private Tokenizer tokenizer;

	private TagOpenState tagOpenState;

	@Test(expected=ParseErrorException.class)
	public void testGetNextTokenInvalidCharacter() throws IOException {
		tokenizer = new Tokenizer(new StringReader(" "));
		tokenizer.setState(Tokenizer.State.TAG_OPEN);
		tokenizer.setAllowParseErrors(false);
		tagOpenState = new TagOpenState(tokenizer);
		
		tagOpenState.getNextTokens();
	}

	@Test
	public void testGetNextTokenInvalidCharacterLenient() throws IOException {
		tokenizer = new Tokenizer(new StringReader(" "));
		tokenizer.setState(Tokenizer.State.TAG_OPEN);
		tokenizer.setAllowParseErrors(true);
		tagOpenState = new TagOpenState(tokenizer);
		
		final List<Token> tokens = tagOpenState.getNextTokens();
		
		assertNotNull(tokens);
		final Token token = tokens.get(0);
		assertEquals(Token.Type.CHARACTER, token.getType());
		final CharacterToken characterToken = (CharacterToken) token;
		assertEquals('<', characterToken.getCharacter());
		
		assertEquals(Tokenizer.State.DATA, tokenizer.getState());
		assertEquals(' ', tokenizer.consume());
		assertEquals(-1, tokenizer.consume());
	}

}
