package org.silnith.browser.organic.parser.html5.grammar.mode;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Element;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intd">12.2.5.4.15 The "in cell" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class InCellInsertionMode extends InsertionMode {

	public InCellInsertionMode(final Parser parser) {
		super(parser);
	}

	@Override
	public boolean insert(final Token token) {
		switch (token.getType()) {
		case START_TAG: {
			final StartTagToken startTagToken = (StartTagToken) token;
			final String tagName = startTagToken.getTagName();
			switch (tagName) {
			case "caption": // fall through
			case "col": // fall through
			case "colgroup": // fall through
			case "tbody": // fall through
			case "td": // fall through
			case "tfoot": // fall through
			case "th": // fall through
			case "thead": // fall through
			case "tr": {
				if ( !hasParticularElementInTableScope("td") && !hasParticularElementInTableScope("th")) {
					if (isAllowParseErrors()) {
						return IGNORE_TOKEN;
					} else {
						throw new ParseErrorException("Expected to find table cell in table scope.");
					}
				}
				closeCell();
				return REPROCESS_TOKEN;
			} // break;
			default: {
				return anythingElse(startTagToken);
			} // break;
			}
		} // break;
		case END_TAG: {
			final EndTagToken endTagToken = (EndTagToken) token;
			final String tagName = endTagToken.getTagName();
			switch (tagName) {
			case "td": // fall through
			case "th": {
				if ( !hasParticularElementInTableScope(tagName)) {
					if (isAllowParseErrors()) {
						return IGNORE_TOKEN;
					} else {
						throw new ParseErrorException("Expected to find " + tagName + " element in table scope.");
					}
				}
				generateImpliedEndTags();
				if ( !isElementA(getCurrentNode(), tagName)) {
					if (isAllowParseErrors()) {
						// do nothing?
					} else {
						throw new ParseErrorException("Expected current node to be a " + tagName + ", instead it was: " + getCurrentNode().getTagName());
					}
				}
				Element popped;
				do {
					popped = popCurrentNode();
				} while ( !isElementA(popped, tagName));
				clearListOfActiveFormattingElementsUpToLastMarker();
				setInsertionMode(Parser.Mode.IN_ROW);
				return TOKEN_HANDLED;
			} // break;
			case "body": // fall through
			case "caption": // fall through
			case "col": // fall through
			case "colgroup": // fall through
			case "html": {
				if (isAllowParseErrors()) {
					return IGNORE_TOKEN;
				} else {
					throw new ParseErrorException("Unexpected end tag token in table cell: " + endTagToken);
				}
			} // break;
			case "table": // fall through
			case "tbody": // fall through
			case "tfoot": // fall through
			case "thead": // fall through
			case "tr": {
				if ( !hasParticularElementInTableScope(tagName)) {
					if (isAllowParseErrors()) {
						return IGNORE_TOKEN;
					} else {
						throw new ParseErrorException("Expected to find " + tagName + " element in table scope.");
					}
				}
				closeCell();
				return REPROCESS_TOKEN;
			} // break;
			default: {
				return anythingElse(endTagToken);
			} // break;
			}
		} // break;
		default: {
			return anythingElse(token);
		} // break;
		}
	}

	private boolean anythingElse(final Token token) {
		return processUsingRulesFor(Parser.Mode.IN_BODY, token);
	}

}
