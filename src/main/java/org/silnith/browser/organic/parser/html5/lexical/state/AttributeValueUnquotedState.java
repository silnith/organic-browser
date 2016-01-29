package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.AMPERSAND;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.APOSTROPHE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.EQUALS_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GRAVE_ACCENT;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LESS_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.QUOTATION_MARK;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#attribute-value-%28unquoted%29-state">12.2.4.40 Attribute value (unquoted) state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AttributeValueUnquotedState extends TokenizerState {

	private final CharacterReferenceState characterReferenceState;

	public AttributeValueUnquotedState(final Tokenizer tokenizer) {
		super(tokenizer);
		this.characterReferenceState = new CharacterReferenceState(tokenizer, GREATER_THAN_SIGN);
	}

	@Override
	public int getMaxPushback() {
		return characterReferenceState.getMaxPushback();
	}

	@Override
	public List<Token> getNextTokens() throws IOException {
		final int ch = consume();
		switch (ch) {
		case CHARACTER_TABULATION: // fall through
		case LINE_FEED: // fall through
		case FORM_FEED: // fall through
		case SPACE: {
			setTokenizerState(Tokenizer.State.BEFORE_ATTRIBUTE_NAME);
			return NOTHING;
		} // break;
		case AMPERSAND: {
			List<Token> characterReferences = characterReferenceState.getNextTokens();
			if (characterReferences == null || characterReferences.isEmpty()) {
				characterReferences = one(new CharacterToken(AMPERSAND));
			}
			
			for (final Token token : characterReferences) {
				final CharacterToken characterReference = (CharacterToken) token;
				appendToAttributeValue(characterReference.getCharacter());
			}
			
			return NOTHING;
		} // break;
		case GREATER_THAN_SIGN: {
			setTokenizerState(Tokenizer.State.DATA);
			final TagToken pendingToken = clearPendingTag();
			return one(pendingToken);
		} // break;
		case NULL: {
			if (isAllowParseErrors()) {
				appendToAttributeValue(REPLACEMENT_CHARACTER);
				return NOTHING;
			} else {
				throw new ParseErrorException("Null character in unquoted attribute value.");
			}
		} // break;
		case QUOTATION_MARK: // fall through
		case APOSTROPHE: // fall through
		case LESS_THAN_SIGN: // fall through
		case EQUALS_SIGN: // fall through
		case GRAVE_ACCENT: {
			// grave accent
			if (isAllowParseErrors()) {
				return defaultCase(ch);
			} else {
				throw new ParseErrorException("Illegal character in unquoted attribute value: " + (char) ch);
			}
		} // break;
		case EOF: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.DATA);
				return NOTHING;
			} else {
				throw new ParseErrorException("Unexpected end-of-file in unquoted attribute value.");
			}
		} // break;
		default: {
			return defaultCase(ch);
		} // break;
		}
	}

	private List<Token> defaultCase(final int ch) {
		appendToAttributeValue((char) ch);
		return NOTHING;
	}

}
