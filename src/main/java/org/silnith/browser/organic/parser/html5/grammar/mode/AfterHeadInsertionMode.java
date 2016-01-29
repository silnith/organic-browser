package org.silnith.browser.organic.parser.html5.grammar.mode;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CARRIAGE_RETURN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Element;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#the-after-head-insertion-mode">12.2.5.4.6 The "after head" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AfterHeadInsertionMode extends InsertionMode {

	public AfterHeadInsertionMode(final Parser parser) {
		super(parser);
	}

	@Override
	public boolean insert(final Token token) {
		switch (token.getType()) {
		case CHARACTER: {
			final CharacterToken characterToken = (CharacterToken) token;
			final char character = characterToken.getCharacter();
			switch (character) {
			case CHARACTER_TABULATION: // fall through
			case LINE_FEED: // fall through
			case FORM_FEED: // fall through
			case CARRIAGE_RETURN: // fall through
			case SPACE: {
				insertCharacter(character);
				return TOKEN_HANDLED;
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
				throw new ParseErrorException("Unexpected DOCTYPE after head: " + token);
			}
		} // break;
		case START_TAG: {
			final StartTagToken startTagToken = (StartTagToken) token;
			final String tagName = startTagToken.getTagName();
			switch (tagName) {
			case "html": {
				return processUsingRulesFor(Parser.Mode.IN_BODY, startTagToken);
			} // break;
			case "body": {
				insertHTMLElement(startTagToken);
				setFramesetOKFlag(NOT_OK);
				setInsertionMode(Parser.Mode.IN_BODY);
				return TOKEN_HANDLED;
			} // break;
			case "frameset": {
				insertHTMLElement(startTagToken);
				setInsertionMode(Parser.Mode.IN_FRAMESET);
				return TOKEN_HANDLED;
			} // break;
			case "base": // fall through
			case "basefont": // fall through
			case "bgsound": // fall through
			case "link": // fall through
			case "meta": // fall through
			case "noframes": // fall through
			case "script": // fall through
			case "style": // fall through
			case "template": // fall through
			case "title": {
				if (isAllowParseErrors()) {
					assert getHeadElementPointer() != null;
					addToStackOfOpenElements(getHeadElementPointer());
					final boolean returnValue = processUsingRulesFor(Parser.Mode.IN_HEAD, startTagToken);
					Element popped;
					do {
						// TODO: remove all elements above this, or only this one element?
						popped = popCurrentNode();
					} while (popped != getHeadElementPointer());
					return returnValue;
				} else {
					throw new ParseErrorException("Unexpected start tag token after head: " + startTagToken);
				}
			} // break;
			case "head": {
				if (isAllowParseErrors()) {
					return IGNORE_TOKEN;
				} else {
					throw new ParseErrorException("Unexpected start tag token after head: " + startTagToken);
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
			case "template": {
				return processUsingRulesFor(Parser.Mode.IN_HEAD, endTagToken);
			} // break;
			case "body": // fall through
			case "html": // fall through
			case "br": {
				return anythingElse(endTagToken);
			} // break;
			default: {
				if (isAllowParseErrors()) {
					return IGNORE_TOKEN;
				} else {
					throw new ParseErrorException("Unexpected end tag token after head: " + endTagToken);
				}
			} // break;
			}
		} // break;
		default: {
			return anythingElse(token);
		} // break;
		}
	}

	private boolean anythingElse(final Token token) {
		insertHTMLElement("body");
		setInsertionMode(Parser.Mode.IN_BODY);
		return REPROCESS_TOKEN;
	}

}
