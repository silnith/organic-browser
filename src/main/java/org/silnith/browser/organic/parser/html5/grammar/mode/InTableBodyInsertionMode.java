package org.silnith.browser.organic.parser.html5.grammar.mode;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intbody">12.2.5.4.13 The "in table body" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class InTableBodyInsertionMode extends InsertionMode {

	public InTableBodyInsertionMode(final Parser parser) {
		super(parser);
	}

	@Override
	public boolean insert(final Token token) {
		switch (token.getType()) {
		case START_TAG: {
			final StartTagToken startTagToken = (StartTagToken) token;
			final String tagName = startTagToken.getTagName();
			switch (tagName) {
			case "tr": {
				clearStackBackToTableBodyContext();
				insertHTMLElement(startTagToken);
				setInsertionMode(Parser.Mode.IN_ROW);
				return TOKEN_HANDLED;
			} // break;
			case "th": // fall through
			case "td": {
				if (isAllowParseErrors()) {
					clearStackBackToTableBodyContext();
					insertHTMLElement("tr");
					setInsertionMode(Parser.Mode.IN_ROW);
					return REPROCESS_TOKEN;
				} else {
					throw new ParseErrorException("Unexpected start tag token in table body: " + startTagToken);
				}
			} // break;
			case "caption": // fall through
			case "col": // fall through
			case "colgroup": // fall through
			case "tbody": // fall through
			case "tfoot": // fall through
			case "thead": {
				if ( !hasParticularElementInTableScope("tbody")
						&& !hasParticularElementInTableScope("tfoot")
						&& !hasParticularElementInTableScope("thead")) {
					if (isAllowParseErrors()) {
						return IGNORE_TOKEN;
					} else {
						throw new ParseErrorException();
					}
				}
				clearStackBackToTableBodyContext();
				popCurrentNode();
				setInsertionMode(Parser.Mode.IN_TABLE);
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
			case "tbody": // fall through
			case "tfoot": // fall through
			case "thead": {
				// verify stack of open elements has matching start tag in table scope
				if ( !hasParticularElementInTableScope(tagName)) {
					if (isAllowParseErrors()) {
						return IGNORE_TOKEN;
					} else {
						throw new ParseErrorException("Unexpected end tag token with no matching element in table scope: " + endTagToken);
					}
				}
				clearStackBackToTableBodyContext();
				popCurrentNode();
				setInsertionMode(Parser.Mode.IN_TABLE);
				return TOKEN_HANDLED;
			} // break;
			case "table": {
				if ( !hasParticularElementInTableScope("tbody")
						&& !hasParticularElementInTableScope("tfoot")
						&& !hasParticularElementInTableScope("thead")) {
					if (isAllowParseErrors()) {
						return IGNORE_TOKEN;
					} else {
						throw new ParseErrorException();
					}
				}
				clearStackBackToTableBodyContext();
				popCurrentNode();
				setInsertionMode(Parser.Mode.IN_TABLE);
				return REPROCESS_TOKEN;
			} // break;
			case "body": // fall through
			case "caption": // fall through
			case "col": // fall through
			case "colgroup": // fall through
			case "html": // fall through
			case "td": // fall through
			case "th": // fall through
			case "tr": {
				if (isAllowParseErrors()) {
					return IGNORE_TOKEN;
				} else {
					throw new ParseErrorException("Unexpected end tag token in table body: " + endTagToken);
				}
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
		return processUsingRulesFor(Parser.Mode.IN_TABLE, token);
	}

}
