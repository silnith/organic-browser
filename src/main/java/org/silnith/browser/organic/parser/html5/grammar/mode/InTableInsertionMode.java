package org.silnith.browser.organic.parser.html5.grammar.mode;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Element;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intable">12.2.5.4.9 The "in table" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class InTableInsertionMode extends InsertionMode {

	public InTableInsertionMode(final Parser parser) {
		super(parser);
	}

	@Override
	public boolean insert(final Token token) {
		switch (token.getType()) {
		case CHARACTER: {
			final CharacterToken characterToken = (CharacterToken) token;
			final Element currentNode = getCurrentNode();
			switch (currentNode.getTagName()) {
			case "table": // fall through
			case "tbody": // fall through
			case "tfoot": // fall through
			case "thead": // fall through
			case "tr": {
				setPendingTableCharacterTokens();
				setOriginalInsertionMode(getInsertionMode());
				setInsertionMode(Parser.Mode.IN_TABLE_TEXT);
				return REPROCESS_TOKEN;
			} // break;
			default: {
				return anythingElse(characterToken);
			} // break;
			}
		} // break;
		case COMMENT: {
			final CommentToken commentToken = (CommentToken) token;
			insertComment(commentToken);
			return TOKEN_HANDLED;
		} // break;
		case DOCTYPE: {
			if (isAllowParseErrors()) {
				return IGNORE_TOKEN;
			} else {
				throw new ParseErrorException("Unexpected DOCTYPE token in table: " + token);
			}
		} // break;
		case START_TAG: {
			final StartTagToken startTagToken = (StartTagToken) token;
			final String tagName = startTagToken.getTagName();
			switch (tagName) {
			case "caption": {
				clearStackBackToTableContext();
				insertMarkerAtEndOfListOfActiveFormattingElements();
				insertHTMLElement(startTagToken);
				setInsertionMode(Parser.Mode.IN_CAPTION);
				return TOKEN_HANDLED;
			} // break;
			case "colgroup": {
				clearStackBackToTableContext();
				insertHTMLElement(startTagToken);
				setInsertionMode(Parser.Mode.IN_COLUMN_GROUP);
				return TOKEN_HANDLED;
			} // break;
			case "col": {
				clearStackBackToTableContext();
				insertHTMLElement("colgroup");
				setInsertionMode(Parser.Mode.IN_COLUMN_GROUP);
				return REPROCESS_TOKEN;
			} // break;
			case "tbody": // fall through
			case "tfoot": // fall through
			case "thead": {
				clearStackBackToTableContext();
				insertHTMLElement(startTagToken);
				setInsertionMode(Parser.Mode.IN_TABLE_BODY);
				return TOKEN_HANDLED;
			} // break;
			case "td": // fall through
			case "th": // fall through
			case "tr": {
				clearStackBackToTableContext();
				insertHTMLElement("tbody");
				setInsertionMode(Parser.Mode.IN_TABLE_BODY);
				return REPROCESS_TOKEN;
			} // break;
			case "table": {
				if (isAllowParseErrors()) {
					if ( !hasParticularElementInTableScope("table")) {
						return IGNORE_TOKEN;
					}
					Element popped;
					do {
						popped = popCurrentNode();
					} while ( !isElementA(popped, "table"));
					resetInsertionModeAppropriately();
					return REPROCESS_TOKEN;
				} else {
					throw new ParseErrorException("Unexpected start tag token in table: " + startTagToken);
				}
			} // break;
			case "style": // fall through
			case "script": // fall through
			case "template": {
				return processUsingRulesFor(Parser.Mode.IN_HEAD, startTagToken);
			} // break;
			case "input": {
				final TagToken.Attribute typeAttribute = getAttributeNamed(startTagToken, "type");
				if (typeAttribute == null || typeAttribute.getValue().equalsIgnoreCase("hidden")) {
					return anythingElse(startTagToken);
				}
				if (isAllowParseErrors()) {
					insertHTMLElement(startTagToken);
					popCurrentNode();
					acknowledgeTokenSelfClosingFlag(startTagToken);
					return TOKEN_HANDLED;
				} else {
					throw new ParseErrorException("Unexpected start tag token in table: " + startTagToken);
				}
			} // break;
			case "form": {
				if (isAllowParseErrors()) {
					if (isStackOfOpenElementsContains("template") || getFormElementPointer() != null) {
						return IGNORE_TOKEN;
					}
					final Element formElement = insertHTMLElement(startTagToken);
					setFormElementPointer(formElement);
					popCurrentNode();
					return TOKEN_HANDLED;
				} else {
					throw new ParseErrorException("Unexpected start tag token in table: " + startTagToken);
				}
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
			case "table": {
				if ( !hasParticularElementInTableScope("table")) {
					if (isAllowParseErrors()) {
						return IGNORE_TOKEN;
					} else {
						throw new ParseErrorException("End tag token for table with no matching start tag in table scope.");
					}
				}
				Element poppedElement;
				do {
					poppedElement = popCurrentNode();
				} while ( !isElementA(poppedElement, "table"));
				resetInsertionModeAppropriately();
				return TOKEN_HANDLED;
			} // break;
			case "body": // fall through
			case "caption": // fall through
			case "col": // fall through
			case "colgroup": // fall through
			case "html": // fall through
			case "tbody": // fall through
			case "td": // fall through
			case "tfoot": // fall through
			case "th": // fall through
			case "thead": // fall through
			case "tr": {
				if (isAllowParseErrors()) {
					return IGNORE_TOKEN;
				} else {
					throw new ParseErrorException("Unexpected end tag token in table: " + endTagToken);
				}
			} // break;
			case "template": {
				return processUsingRulesFor(Parser.Mode.IN_HEAD, endTagToken);
			} // break;
			default: {
				return anythingElse(endTagToken);
			} // break;
			}
		} // break;
		case EOF: {
			return processUsingRulesFor(Parser.Mode.IN_BODY, token);
		} // break;
		default: {
			return anythingElse(token);
		} // break;
		}
	}

	private boolean anythingElse(final Token token) {
		if (isAllowParseErrors()) {
			enableFosterParenting();
			final boolean returnValue = processUsingRulesFor(Parser.Mode.IN_BODY, token);
			disableFosterParenting();
			return returnValue;
		} else {
			throw new ParseErrorException("Unexpected token in table: " + token);
		}
	}

}
