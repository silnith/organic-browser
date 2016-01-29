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

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-afterframeset">12.2.5.4.21 The "after frameset" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AfterFramesetInsertionMode extends InsertionMode {

	public AfterFramesetInsertionMode(final Parser parser) {
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
				throw new ParseErrorException("Unexpected DOCTYPE token after frameset: " + token);
			}
		} // break;
		case START_TAG: {
			final StartTagToken startTagToken = (StartTagToken) token;
			final String tagName = startTagToken.getTagName();
			switch (tagName) {
			case "html": {
				return processUsingRulesFor(Parser.Mode.IN_BODY, startTagToken);
			} // break;
			case "noframes": {
				return processUsingRulesFor(Parser.Mode.IN_HEAD, startTagToken);
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
			case "html": {
				setInsertionMode(Parser.Mode.AFTER_AFTER_FRAMESET);
				return TOKEN_HANDLED;
			} // break;
			default: {
				return anythingElse(endTagToken);
			} // break;
			}
		} // break;
		case EOF: {
			stopParsing();
			return TOKEN_HANDLED;
		} // break;
		default: {
			return anythingElse(token);
		} // break;
		}
	}

	private boolean anythingElse(final Token token) {
		if (isAllowParseErrors()) {
			return IGNORE_TOKEN;
		} else {
			throw new ParseErrorException("Unexpected token after frameset: " + token);
		}
	}

}
