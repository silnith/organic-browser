package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.AMPERSAND;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#character-reference-in-data-state">
 *      12.2.4.2 Character reference in data state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CharacterReferenceInDataState extends TokenizerState {
    
    private final CharacterReferenceState characterReferenceState;
    
    public CharacterReferenceInDataState(final Tokenizer tokenizer) {
        super(tokenizer);
        this.characterReferenceState = new CharacterReferenceState(tokenizer);
    }
    
    @Override
    public int getMaxPushback() {
        return characterReferenceState.getMaxPushback();
    }
    
    @Override
    public List<Token> getNextTokens() throws IOException {
        setTokenizerState(Tokenizer.State.DATA);
        final List<Token> characterReference = characterReferenceState.getNextTokens();
        if (characterReference == null || characterReference.isEmpty()) {
            return one(new CharacterToken(AMPERSAND));
        } else {
            return characterReference;
        }
    }
    
}
