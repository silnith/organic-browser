package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-delim-token">&lt;
 *      delim-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DelimToken extends LexicalToken {
    
    private final char[] chars;
    
    public DelimToken(final char ch) {
        super(LexicalType.DELIM_TOKEN);
        this.chars = new char[] { ch };
    }
    
    public DelimToken(final char[] characters) {
        super(LexicalType.DELIM_TOKEN);
        this.chars = characters;
    }
    
    public String getChars() {
        return String.valueOf(chars);
    }

    @Override
    public String toString() {
        return "'" + String.valueOf(chars) + "'";
    }
    
}
