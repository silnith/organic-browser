package org.silnith.browser.organic.parser.css3.grammar.token;

import org.silnith.browser.organic.parser.css3.Token;


/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#component-value">component
 *      value</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public abstract class ComponentValue extends Token {
    
    public enum ComponentValueType {
        /**
         * @see org.silnith.browser.organic.parser.css3.grammar.token.Function
         */
        FUNCTION,
        /**
         * @see org.silnith.browser.organic.parser.css3.grammar.token.SimpleBlock
         */
        SIMPLE_BLOCK
    }
    
    private final ComponentValueType componentValueType;
    
    public ComponentValue(final ComponentValueType componentValueType) {
        super(Type.COMPONENT_VALUE);
        this.componentValueType = componentValueType;
    }
    
    public final ComponentValueType getComponentValueType() {
        return componentValueType;
    }
    
}
