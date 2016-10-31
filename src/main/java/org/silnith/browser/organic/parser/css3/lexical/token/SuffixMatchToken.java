package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href=
 *      "http://dev.w3.org/csswg/css-syntax/#typedef-suffix-match-token">&lt;
 *      suffix-match-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class SuffixMatchToken extends LexicalToken {
    
    public SuffixMatchToken() {
        super(LexicalType.SUFFIX_MATCH_TOKEN);
    }
    
    @Override
    public String toString() {
        return "'$='";
    }
    
}
