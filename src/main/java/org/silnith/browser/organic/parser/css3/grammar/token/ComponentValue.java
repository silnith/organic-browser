package org.silnith.browser.organic.parser.css3.grammar.token;

import org.silnith.browser.organic.parser.css3.Token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#component-value">component value</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public abstract class ComponentValue extends Token {

	public enum ComponentValueType {
		FUNCTION,
		SIMPLE_BLOCK
	}

	public ComponentValue() {
		super(Token.Type.COMPONENT_VALUE);
	}

	public abstract ComponentValueType getComponentValueType();

}
