package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.AMPERSAND;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LESS_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NUMBER_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SEMICOLON;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#tokenizing-character-references">
 *      12.2.4.69 Tokenizing character references</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CharacterReferenceState extends TokenizerState {
    
    private final int additionalAllowedCharacter;
    
    public CharacterReferenceState(final Tokenizer tokenizer, final char additionalAllowedCharacter) {
        super(tokenizer);
        this.additionalAllowedCharacter = additionalAllowedCharacter;
    }
    
    public CharacterReferenceState(final Tokenizer tokenizer) {
        super(tokenizer);
        this.additionalAllowedCharacter = -1;
    }
    
    @Override
    public int getMaxPushback() {
        return CharacterReferenceData.longestEntityName + 2;
    }
    
    private void unreadCharArray(final char... characters) throws IOException {
        for (int i = characters.length - 1; i >= 0; i-- ) {
            unconsume(characters[i]);
        }
    }
    
    private List<Token> handlePrefix(final int ch, final String name) throws IOException {
        final String prefix = findPrefixMatch(name);
        if (prefix == null) {
            unreadCharArray(name.toCharArray());
            return null;
        } else {
            // return the prefix, unconsume the unused
            final String unused = name.substring(prefix.length(), name.length());
            unreadCharArray(unused.toCharArray());
            final StringBuilder characters = new StringBuilder();
            for (final int codePoint : CharacterReferenceData.entityMap.get(prefix)) {
                characters.append(Character.toChars(codePoint));
            }
            return toTokens(characters.toString().toCharArray());
        }
    }
    
    private String findPrefixMatch(final String name) {
        for (int i = name.length() - 2; i > 0; i-- ) {
            final String substring = name.substring(0, i);
            if (CharacterReferenceData.entityMap.containsKey(substring)) {
                return substring;
            }
        }
        return null;
    }
    
    private char[] replaceDisallowedCharacters(final int codePoint) {
        if (CharacterReferenceData.replacementMap.containsKey(codePoint)) {
            if (isAllowParseErrors()) {
                return Character.toChars(CharacterReferenceData.replacementMap.get(codePoint));
            } else {
                throw new ParseErrorException("Disallowed character: (code point " + codePoint + ")");
            }
        }
        
        if (codePoint >= 0x10FFFF) {
            if (isAllowParseErrors()) {
                return Character.toChars(REPLACEMENT_CHARACTER);
            } else {
                throw new ParseErrorException("Disallowed character: (code point " + codePoint + ")");
            }
        }
        
        if (CharacterReferenceData.disallowedCharacters.contains(codePoint)) {
            if (isAllowParseErrors()) {
                return Character.toChars(REPLACEMENT_CHARACTER);
            } else {
                throw new ParseErrorException("Disallowed character: (code point " + codePoint + ")");
            }
        }
        
        return Character.toChars(codePoint);
    }
    
    private List<Token> toTokens(final char[] characters) {
        final List<Token> tokens = new ArrayList<>();
        for (final char ch : characters) {
            tokens.add(new CharacterToken(ch));
        }
        return tokens;
    }
    
    @Override
    public List<Token> getNextTokens() throws IOException {
        final StringBuilder content = new StringBuilder();
        
        int ch = consume();
        switch (ch) {
        case NUMBER_SIGN: {
            ch = consume();
            char x = 'X';
            switch (ch) {
            case 'x':
                x = 'x';// fall through
            case 'X': {
                ch = consume();
                while (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F') {
                    content.append((char) ch);
                    ch = consume();
                }
                if (content.length() == 0) {
                    // parse error
                    if (isAllowParseErrors()) {
                        unreadCharArray(NUMBER_SIGN, x, (char) ch);
                        return null;
                    } else {
                        throw new ParseErrorException("Expected number after \"&#\"" + x + ", found: " + (char) ch);
                    }
                }
                if (ch != SEMICOLON) {
                    if (isAllowParseErrors()) {
                        unconsume(ch);
                    } else {
                        throw new ParseErrorException(
                                "Missing semicolon after numeric character reference: &#" + x + content);
                    }
                }
                // parse hex number
                final int num = Integer.parseInt(content.toString(), 16);
                return toTokens(replaceDisallowedCharacters(num));
            }
            default: {
                while (ch >= '0' && ch <= '9') {
                    content.append((char) ch);
                    ch = consume();
                }
                if (content.length() == 0) {
                    if (isAllowParseErrors()) {
                        unreadCharArray(NUMBER_SIGN, (char) ch);
                        return null;
                    } else {
                        throw new ParseErrorException("Expected number after \"&#\"" + x + ", found: " + (char) ch);
                    }
                }
                if (ch != SEMICOLON) {
                    if (isAllowParseErrors()) {
                        unconsume(ch);
                    } else {
                        throw new ParseErrorException(
                                "Missing semicolon after numeric character reference: &#" + content);
                    }
                }
                // parse decimal number
                final int num = Integer.parseInt(content.toString());
                return toTokens(replaceDisallowedCharacters(num));
            }
            }
        } // break;
        case CHARACTER_TABULATION: // fall through
        case LINE_FEED: // fall through
        case FORM_FEED: // fall through
        case SPACE: // fall through
        case LESS_THAN_SIGN: // fall through
        case AMPERSAND: // fall through
        case EOF: {
            unconsume(ch);
            return null;
        } // break;
        default: {
            // check for additional allowed character
            // can't use a variable as a case in a switch statement
            if (ch == additionalAllowedCharacter) {
                unconsume(ch);
                return null;
            }
            
            while (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
                content.append((char) ch);
                ch = consume();
                if (content.length() >= CharacterReferenceData.longestEntityName) {
                    break;
                }
            }
            
            final String name = content.toString();
            if (name.isEmpty()) {
                unconsume(ch);
                return null;
            }
            if (ch == SEMICOLON) {
                final int[] codePoints = CharacterReferenceData.entityMap.get(name);
                if (codePoints == null) {
                    if (isAllowParseErrors()) {
                        unconsume(ch);
                        return handlePrefix(ch, name);
                    } else {
                        throw new ParseErrorException("Unrecognized entity reference: &" + name + ";");
                    }
                } else {
                    final StringBuilder characters = new StringBuilder();
                    for (final int codePoint : codePoints) {
                        characters.append(Character.toChars(codePoint));
                    }
                    return toTokens(characters.toString().toCharArray());
                }
            } else {
                if (isAllowParseErrors()) {
                    unconsume(ch);
                    return handlePrefix(ch, name);
                } else {
                    throw new ParseErrorException("Entity reference is not closed by a semicolon: &" + name);
                }
            }
        } // break;
        }
    }
    
}
