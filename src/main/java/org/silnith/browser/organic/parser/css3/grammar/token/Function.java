package org.silnith.browser.organic.parser.css3.grammar.token;

import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;


/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#function">function</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class Function extends ComponentValue {
    
    private final String name;
    
    private final List<Token> value;
    
    public Function(final String name, final List<Token> value) {
        super(ComponentValueType.FUNCTION);
        this.name = name;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "Function {"
                + "name=" + name
                + ", "
                + "value=" + value
                + "}";
    }
    
}
