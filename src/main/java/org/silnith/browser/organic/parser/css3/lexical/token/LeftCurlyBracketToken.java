package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#tokendef-open-curly">&lt;{-
 *      token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class LeftCurlyBracketToken extends LexicalToken {
    
    public LeftCurlyBracketToken() {
        super(LexicalType.LEFT_BRACE_TOKEN);
    }
    
    @Override
    public String toString() {
        return "'{'";
    }
    
}
