package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#tokendef-close-paren">&lt;)
 *      -token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class RightParenthesisToken extends LexicalToken {
    
    public RightParenthesisToken() {
        super(LexicalType.RIGHT_PARENTHESIS_TOKEN);
    }
    
    @Override
    public String toString() {
        return "')'";
    }
    
}
