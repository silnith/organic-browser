package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-eof-token">&lt;EOF-
 *      token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EOFToken extends LexicalToken {
    
    public EOFToken() {
        super(LexicalType.EOF);
    }
    
    @Override
    public String toString() {
        return "<EOF>";
    }
    
}
