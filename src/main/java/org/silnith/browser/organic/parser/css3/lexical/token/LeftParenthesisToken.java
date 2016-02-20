package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#tokendef-open-paren">&lt;(-
 *      token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class LeftParenthesisToken extends LexicalToken {
    
    public LeftParenthesisToken() {
        super(LexicalType.LEFT_PARENTHESIS_TOKEN);
    }
    
    @Override
    public String toString() {
        return "'('";
    }
    
}
