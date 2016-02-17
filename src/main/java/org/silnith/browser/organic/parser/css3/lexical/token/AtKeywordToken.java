package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-at-keyword-token">
 *      &lt;at-keyword-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AtKeywordToken extends StringValueToken {
    
    public AtKeywordToken() {
        super(LexicalType.AT_KEYWORD_TOKEN);
    }
    
    @Override
    public String toString() {
        return "@" + getStringValue();
    }
    
}
