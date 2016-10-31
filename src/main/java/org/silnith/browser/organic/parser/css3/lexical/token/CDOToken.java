package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-cdo-token">&lt;CDO-
 *      token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CDOToken extends LexicalToken {
    
    public CDOToken() {
        super(LexicalType.CDO_TOKEN);
    }
    
    @Override
    public String toString() {
        return "<CDO-token>";
    }
    
}
