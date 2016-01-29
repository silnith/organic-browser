package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.DOCTYPEToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#before-doctype-name-state">12.2.4.53 Before DOCTYPE name state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class BeforeDOCTYPENameState extends TokenizerState {

	public BeforeDOCTYPENameState(final Tokenizer tokenizer) {
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
		case CHARACTER_TABULATION: // fall through
		case LINE_FEED: // fall through
		case FORM_FEED: // fall through
		case SPACE: {
			// ignore the character
			return NOTHING;
		} // break;
		case 'A': // fall through
		case 'B': // fall through
		case 'C': // fall through
		case 'D': // fall through
		case 'E': // fall through
		case 'F': // fall through
		case 'G': // fall through
		case 'H': // fall through
		case 'I': // fall through
		case 'J': // fall through
		case 'K': // fall through
		case 'L': // fall through
		case 'M': // fall through
		case 'N': // fall through
		case 'O': // fall through
		case 'P': // fall through
		case 'Q': // fall through
		case 'R': // fall through
		case 'S': // fall through
		case 'T': // fall through
		case 'U': // fall through
		case 'V': // fall through
		case 'W': // fall through
		case 'X': // fall through
		case 'Y': // fall through
		case 'Z': {
			createDOCTYPE(toLower((char) ch));
			setTokenizerState(Tokenizer.State.DOCTYPE_NAME);
			return NOTHING;
		} // break;
		case NULL: {
			if (isAllowParseErrors()) {
				createDOCTYPE(REPLACEMENT_CHARACTER);
				setTokenizerState(Tokenizer.State.DOCTYPE_NAME);
				return NOTHING;
			} else {
				throw new ParseErrorException("Null character in DOCTYPE name.");
			}
		} // break;
		case GREATER_THAN_SIGN: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.DATA);
				setForceQuirks();
				final DOCTYPEToken doctypeToken = clearDOCTYPEToken();
				return one(doctypeToken);
			} else {
				throw new ParseErrorException();
			}
		} // break;
		case EOF: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.DATA);
				setForceQuirks();
				final DOCTYPEToken doctypeToken = clearDOCTYPEToken();
				return one(doctypeToken);
			} else {
				throw new ParseErrorException();
			}
		} // break;
		default: {
			createDOCTYPE((char) ch);
			setTokenizerState(Tokenizer.State.DOCTYPE_NAME);
			return NOTHING;
		} // break;
		}
	}

}
