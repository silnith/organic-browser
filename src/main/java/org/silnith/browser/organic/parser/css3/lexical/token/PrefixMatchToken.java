package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href=
 *      "http://dev.w3.org/csswg/css-syntax/#typedef-prefix-match-token">&lt;
 *      prefix-match-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PrefixMatchToken extends LexicalToken {
    
    public PrefixMatchToken() {
        super(LexicalType.PREFIX_MATCH_TOKEN);
    }
    
    @Override
    public String toString() {
        return "'^='";
    }
    
}
