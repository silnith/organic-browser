package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-dash-match-token">
 *      &lt;dash-match-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DashMatchToken extends LexicalToken {
    
    public DashMatchToken() {
        super(LexicalType.DASH_MATCH_TOKEN);
    }
    
    @Override
    public String toString() {
        return "'|='";
    }
    
}
