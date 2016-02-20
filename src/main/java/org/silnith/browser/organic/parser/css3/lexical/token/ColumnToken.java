package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-column-token">&lt;
 *      column-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ColumnToken extends LexicalToken {
    
    public ColumnToken() {
        super(LexicalType.COLUMN_TOKEN);
    }
    
    @Override
    public String toString() {
        return "'||'";
    }
    
}
