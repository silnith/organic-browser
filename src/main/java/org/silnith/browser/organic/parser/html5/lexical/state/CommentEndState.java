package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.EXCLAMATION_MARK;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.HYPHEN_MINUS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#comment-end-state">12.2.4.50 Comment end state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CommentEndState extends TokenizerState {

	public CommentEndState(final Tokenizer tokenizer) {
		super(tokenizer);
	}

	@Override
	public int getMaxPushback() {
		return 0;
	}

	@Override
	public List<Token> getNextTokens() throws IOException {
		final int ch = consume();
		switch (ch) {
		case GREATER_THAN_SIGN: {
			setTokenizerState(Tokenizer.State.DATA);
			final CommentToken commentToken = clearCommentToken();
			return one(commentToken);
		} // break;
		case NULL: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.COMMENT);
				appendToCommentToken(HYPHEN_MINUS, HYPHEN_MINUS, REPLACEMENT_CHARACTER);
				return NOTHING;
			} else {
				throw new ParseErrorException("Null character in comment end.");
			}
		} // break;
		case EXCLAMATION_MARK: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.COMMENT_END_BANG);
				return NOTHING;
			} else {
				throw new ParseErrorException("Unexpected exclamation mark in comment end.");
			}
		} // break;
		case HYPHEN_MINUS: {
			if (isAllowParseErrors()) {
				appendToCommentToken(HYPHEN_MINUS);
				return NOTHING;
			} else {
				throw new ParseErrorException("Unexpected '-' in comment end.");
			}
		} // break;
		case EOF: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.DATA);
				final CommentToken commentToken = clearCommentToken();
				return one(commentToken);
			} else {
				throw new ParseErrorException("Unexpected end-of-file in comment end.");
			}
		} // break;
		default: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.COMMENT);
				appendToCommentToken(HYPHEN_MINUS, HYPHEN_MINUS, (char) ch);
				return NOTHING;
			} else {
				throw new ParseErrorException("Unexpected character in comment end: " + (char) ch);
			}
		} // break;
		}
	}

}
