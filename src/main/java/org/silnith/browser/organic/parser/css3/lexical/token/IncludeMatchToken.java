package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href=
 *      "http://dev.w3.org/csswg/css-syntax/#typedef-include-match-token">&lt;
 *      include-match-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class IncludeMatchToken extends LexicalToken {
    
    public IncludeMatchToken() {
        super(LexicalType.INCLUDE_MATCH_TOKEN);
    }
    
    @Override
    public String toString() {
        return "'~='";
    }
    
}
