package org.silnith.browser.organic.parser.html5.lexical.token;

/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#tokenization">
 *      12.2.4 Tokenization</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public abstract class Token {
    
    public enum Type {
        DOCTYPE,
        START_TAG,
        END_TAG,
        COMMENT,
        CHARACTER,
        EOF
    }
    
    public Token() {
        super();
    }
    
    public abstract Type getType();
    
}
