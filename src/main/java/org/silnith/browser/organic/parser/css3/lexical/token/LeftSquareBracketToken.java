package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#tokendef-open-square">&lt;[
 *      -token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class LeftSquareBracketToken extends LexicalToken {
    
    public LeftSquareBracketToken() {
        super(LexicalType.LEFT_BRACKET_TOKEN);
    }
    
    @Override
    public String toString() {
        return "'['";
    }
    
}
