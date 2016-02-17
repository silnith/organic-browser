package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-hash-token">&lt;
 *      hash-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class HashToken extends StringValueToken {
    
    public enum TypeFlag {
        ID,
        UNRESTRICTED
    }
    
    private TypeFlag typeFlag;
    
    public HashToken() {
        super(LexicalType.HASH_TOKEN);
        this.typeFlag = TypeFlag.UNRESTRICTED;
    }
    
    public void setTypeFlag(final TypeFlag typeFlag) {
        this.typeFlag = typeFlag;
    }
    
    @Override
    public String toString() {
        return "#" + getStringValue();
    }
    
}
