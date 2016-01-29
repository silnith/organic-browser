package org.silnith.browser.organic.parser.css3.grammar.token;

import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#simple-block">simple block</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class SimpleBlock extends ComponentValue {

	private final LexicalToken token;

	private final List<Token> value;

	public SimpleBlock(final LexicalToken token, final List<Token> value) {
		super();
		this.token = token;
		this.value = value;
	}

	@Override
	public ComponentValueType getComponentValueType() {
		return ComponentValueType.SIMPLE_BLOCK;
	}

	public LexicalToken getToken() {
		return token;
	}

}
