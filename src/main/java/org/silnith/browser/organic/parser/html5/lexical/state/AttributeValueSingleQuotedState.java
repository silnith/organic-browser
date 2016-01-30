package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.AMPERSAND;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.APOSTROPHE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#attribute-value-%28single-quoted%29-state">
 *      12.2.4.39 Attribute value (single-quoted) state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AttributeValueSingleQuotedState extends TokenizerState {
    
    private final CharacterReferenceState characterReferenceState;
    
    public AttributeValueSingleQuotedState(final Tokenizer tokenizer) {
        super(tokenizer);
        this.characterReferenceState = new CharacterReferenceState(tokenizer, APOSTROPHE);
    }
    
    @Override
    public int getMaxPushback() {
        return characterReferenceState.getMaxPushback();
    }
    
    @Override
    public List<Token> getNextTokens() throws IOException {
        final int ch = consume();
        switch (ch) {
        case APOSTROPHE: {
            setTokenizerState(Tokenizer.State.AFTER_ATTRIBUTE_VALUE_QUOTED);
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
        case NULL: {
            if (isAllowParseErrors()) {
                appendToAttributeValue(REPLACEMENT_CHARACTER);
                return NOTHING;
            } else {
                throw new ParseErrorException("Null character in single-quoted attribute value.");
            }
        } // break;
        case EOF: {
            if (isAllowParseErrors()) {
                setTokenizerState(Tokenizer.State.DATA);
                return NOTHING;
            } else {
                throw new ParseErrorException("Unexpected end-of-file in single-quoted attribute value.");
            }
        } // break;
        default: {
            appendToAttributeValue((char) ch);
            return NOTHING;
        } // break;
        }
    }
    
}
