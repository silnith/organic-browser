package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href=
 *      "http://dev.w3.org/csswg/css-syntax/#typedef-substring-match-token">&lt;
 *      substring-match-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class SubstringMatchToken extends LexicalToken {
    
    public SubstringMatchToken() {
        super();
    }
    
    @Override
    public LexicalType getLexicalType() {
        return LexicalType.SUBSTRING_MATCH_TOKEN;
    }
    
    @Override
    public String toString() {
        return "*=";
    }
    
}
