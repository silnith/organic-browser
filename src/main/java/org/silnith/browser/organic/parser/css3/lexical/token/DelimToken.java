package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * A delimiter containing one Unicode code point.  If this is a surrogate pair,
 * the integer constructor is preferred over the character array constructor.
 * 
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-delim-token">&lt;
 *      delim-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DelimToken extends LexicalToken {
    
    private final char[] chars;
    
    public DelimToken(final char character) {
        super(LexicalType.DELIM_TOKEN);
        this.chars = new char[] { character };
    }
    
    public DelimToken(final int character) {
        super(LexicalType.DELIM_TOKEN);
        this.chars = Character.toChars(character);
    }
    
    public DelimToken(final char[] surrogatePair) {
        super(LexicalType.DELIM_TOKEN);
        this.chars = surrogatePair;
    }
    
    public String getChar() {
        return String.valueOf(chars);
    }

    @Override
    public String toString() {
        return "'" + String.valueOf(chars) + "'";
    }
    
}
