package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#tokendef-close-square">&lt;
 *      ]-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class RightSquareBracketToken extends LexicalToken {
    
    public RightSquareBracketToken() {
        super(LexicalType.RIGHT_BRACKET_TOKEN);
    }
    
    @Override
    public String toString() {
        return "']'";
    }
    
}
