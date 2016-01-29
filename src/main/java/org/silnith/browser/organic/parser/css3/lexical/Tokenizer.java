package org.silnith.browser.organic.parser.css3.lexical;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.APOSTROPHE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.ASTERISK;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.BACKSPACE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CIRCUMFLEX_ACCENT;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.COLON;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.COMMA;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.COMMERCIAL_AT;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CONTROL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.DELETE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.DIGIT_NINE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.DIGIT_ZERO;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.DOLLAR_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.EQUALS_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.EXCLAMATION_MARK;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FULL_STOP;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.HYPHEN_MINUS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.INFORMATION_SEPARATOR_ONE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LATIN_CAPITAL_LETTER_A;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LATIN_CAPITAL_LETTER_E;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LATIN_CAPITAL_LETTER_F;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LATIN_CAPITAL_LETTER_U;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LATIN_CAPITAL_LETTER_Z;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LATIN_SMALL_LETTER_A;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LATIN_SMALL_LETTER_E;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LATIN_SMALL_LETTER_F;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LATIN_SMALL_LETTER_U;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LATIN_SMALL_LETTER_Z;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LEFT_CURLY_BRACKET;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LEFT_PARENTHESIS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LEFT_SQUARE_BRACKET;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LESS_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LOW_LINE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NUMBER_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.PERCENTAGE_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.PLUS_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.QUESTION_MARK;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.QUOTATION_MARK;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REVERSE_SOLIDUS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.RIGHT_CURLY_BRACKET;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.RIGHT_PARENTHESIS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.RIGHT_SQUARE_BRACKET;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SEMICOLON;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SHIFT_OUT;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SOLIDUS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.TILDE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.VERTICAL_LINE;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.silnith.browser.organic.network.Download;
import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.css3.TokenStream;
import org.silnith.browser.organic.parser.css3.lexical.token.AtKeywordToken;
import org.silnith.browser.organic.parser.css3.lexical.token.BadStringToken;
import org.silnith.browser.organic.parser.css3.lexical.token.BadURLToken;
import org.silnith.browser.organic.parser.css3.lexical.token.CDCToken;
import org.silnith.browser.organic.parser.css3.lexical.token.CDOToken;
import org.silnith.browser.organic.parser.css3.lexical.token.ColonToken;
import org.silnith.browser.organic.parser.css3.lexical.token.ColumnToken;
import org.silnith.browser.organic.parser.css3.lexical.token.CommaToken;
import org.silnith.browser.organic.parser.css3.lexical.token.DashMatchToken;
import org.silnith.browser.organic.parser.css3.lexical.token.DelimToken;
import org.silnith.browser.organic.parser.css3.lexical.token.DimensionToken;
import org.silnith.browser.organic.parser.css3.lexical.token.EOFToken;
import org.silnith.browser.organic.parser.css3.lexical.token.FunctionToken;
import org.silnith.browser.organic.parser.css3.lexical.token.HashToken;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.IncludeMatchToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LeftCurlyBracketToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LeftParenthesisToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LeftSquareBracketToken;
import org.silnith.browser.organic.parser.css3.lexical.token.NumberToken;
import org.silnith.browser.organic.parser.css3.lexical.token.PercentageToken;
import org.silnith.browser.organic.parser.css3.lexical.token.PrefixMatchToken;
import org.silnith.browser.organic.parser.css3.lexical.token.RightCurlyBracketToken;
import org.silnith.browser.organic.parser.css3.lexical.token.RightParenthesisToken;
import org.silnith.browser.organic.parser.css3.lexical.token.RightSquareBracketToken;
import org.silnith.browser.organic.parser.css3.lexical.token.SemicolonToken;
import org.silnith.browser.organic.parser.css3.lexical.token.StringToken;
import org.silnith.browser.organic.parser.css3.lexical.token.SubstringMatchToken;
import org.silnith.browser.organic.parser.css3.lexical.token.SuffixMatchToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.silnith.browser.organic.parser.css3.lexical.token.TypedNumericValueToken;
import org.silnith.browser.organic.parser.css3.lexical.token.URLToken;
import org.silnith.browser.organic.parser.css3.lexical.token.UnicodeRangeToken;
import org.silnith.browser.organic.parser.css3.lexical.token.WhitespaceToken;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#tokenization">4 Tokenization</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class Tokenizer implements TokenStream {

	private static final int MAX_READAHEAD = 3;

	private static final int EOF = -1;

	private final PushbackReader pushbackReader;

	private boolean allowParseErrors;

	public Tokenizer(final Reader reader) {
		super();
		this.pushbackReader = new PushbackReader(new InputStreamPreprocessor(reader), MAX_READAHEAD);
		this.allowParseErrors = false;
	}

	/**
	 * @param allowParseErrors
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#parse-error">parse error</a>
	 */
	public void setAllowParseErrors(final boolean allowParseErrors) {
		this.allowParseErrors = allowParseErrors;
	}

	/**
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#parse-error">parse error</a>
	 */
	public boolean isAllowParseErrors() {
		return allowParseErrors;
	}

	/**
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#tokenizer-algorithms">4.3 Tokenizer Algorithms</a>
	 */
	@Override
	public LexicalToken getNextToken() throws IOException {
		return consumeToken();
	}

	protected int consume() throws IOException {
		return pushbackReader.read();
	}

	/**
	 * @param character
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#reconsume-the-current-input-code-point">reconsume the current input code point</a>
	 */
	protected void reconsume(int character) throws IOException {
		pushbackReader.unread(character);
	}

	protected int peek() throws IOException {
		final int character = pushbackReader.read();
		if (character == EOF) {
			return EOF;
		}
		pushbackReader.unread(character);
		return character;
	}

	protected int peek(final char[] buf) throws IOException {
		final int numRead = pushbackReader.read(buf);
		if (numRead == EOF) {
			return EOF;
		}
		pushbackReader.unread(buf, 0, numRead);
		return numRead;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href=""></a>
	 */
	protected boolean isEOFCodePoint(final int ch) {
		return ch == EOF;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#digit">digit</a>
	 */
	protected boolean isDigit(final int ch) {
		return ch >= DIGIT_ZERO && ch <= DIGIT_NINE;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#hex-digit">hex digit</a>
	 */
	protected boolean isHexDigit(final int ch) {
		return isDigit(ch)
				|| ch >= LATIN_CAPITAL_LETTER_A && ch <= LATIN_CAPITAL_LETTER_F
				|| ch >= LATIN_SMALL_LETTER_A && ch <= LATIN_SMALL_LETTER_F;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#uppercase-letter">uppercase letter</a>
	 */
	protected boolean isUppercaseLetter(final int ch) {
		return ch >= LATIN_CAPITAL_LETTER_A && ch <= LATIN_CAPITAL_LETTER_Z;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#lowercase-letter">lowercase letter</a>
	 */
	protected boolean isLowercaseLetter(final int ch) {
		return ch >= LATIN_SMALL_LETTER_A && ch <= LATIN_SMALL_LETTER_Z;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#letter">letter</a>
	 */
	protected boolean isLetter(final int ch) {
		return isUppercaseLetter(ch) || isLowercaseLetter(ch);
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#non-ascii-code-point">non-ASCII code point</a>
	 */
	protected boolean isNonASCIICodePoint(final int ch) {
		return ch >= CONTROL;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#name-start-code-point">name-start code point</a>
	 */
	protected boolean isNameStartCodePoint(final int ch) {
		return isLetter(ch) || isNonASCIICodePoint(ch) || ch == LOW_LINE;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#name-code-point">name code point</a>
	 */
	protected boolean isNameCodePoint(final int ch) {
		return isNameStartCodePoint(ch) || isDigit(ch) || ch == HYPHEN_MINUS;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#non-printable-code-point">non-printable code point</a>
	 */
	protected boolean isNonPrintableCodePoint(final int ch) {
		return ch >= NULL && ch <= BACKSPACE
				|| ch == LINE_TABULATION
				|| ch >= SHIFT_OUT && ch <= INFORMATION_SEPARATOR_ONE
				|| ch == DELETE;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#newline">newline</a>
	 */
	protected boolean isNewline(final int ch) {
		return ch == LINE_FEED;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#whitespace">whitespace</a>
	 */
	protected boolean isWhitespace(final int ch) {
		return isNewline(ch) || ch == CHARACTER_TABULATION || ch == SPACE;
	}

	/**
	 * @param ch
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#surrogate-code-point">surrogate code point</a>
	 */
	protected boolean isSurrogateCodePoint(final int ch) {
		return ch >= '\uD800' && ch <= '\uDFFF';
	}

	/**
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-a-token">4.3.1 Consume a token</a>
	 */
	protected LexicalToken consumeToken() throws IOException {
		consumeComments();
		int ch = consume();
		switch (ch) {
		case LINE_FEED: // fall through
		case CHARACTER_TABULATION: // fall through
		case SPACE: {
			consumeWhitespace();
			return new WhitespaceToken();
		} // break;
		case QUOTATION_MARK: {
			return consumeStringToken(QUOTATION_MARK);
		} // break;
		case NUMBER_SIGN: {
			final char[] buf = new char[3];
			final int numPeeked = peek(buf);
			if (numPeeked >= 1 && isNameCodePoint(buf[0])
					|| numPeeked >= 2 && startsValidEscapeSequence(buf[0], buf[1])) {
				final HashToken hashToken = new HashToken();
				if (numPeeked >= 3 && startsIdentifier(buf[0], buf[1], buf[2])) {
					hashToken.setTypeFlag(HashToken.TypeFlag.ID);
				}
				hashToken.setStringValue(consumeName());
				return hashToken;
			} else {
				return new DelimToken(NUMBER_SIGN);
			}
		} // break;
		case DOLLAR_SIGN: {
			ch = peek();
			if (ch == EQUALS_SIGN) {
				consume();
				return new SuffixMatchToken();
			} else {
				return new DelimToken(DOLLAR_SIGN);
			}
		} // break;
		case APOSTROPHE: {
			return consumeStringToken(APOSTROPHE);
		} // break;
		case LEFT_PARENTHESIS: {
			return new LeftParenthesisToken();
		} // break;
		case RIGHT_PARENTHESIS: {
			return new RightParenthesisToken();
		} // break;
		case ASTERISK: {
			ch = peek();
			if (ch == EQUALS_SIGN) {
				return new SubstringMatchToken();
			} else {
				return new DelimToken(ASTERISK);
			}
		} // break;
		case PLUS_SIGN: {
			if (startsNumber(PLUS_SIGN)) {
				reconsume(PLUS_SIGN);
				return consumeNumericToken();
			} else {
				return new DelimToken(PLUS_SIGN);
			}
		} // break;
		case COMMA: {
			return new CommaToken();
		} // break;
		case HYPHEN_MINUS: {
			if (startsNumber(HYPHEN_MINUS)) {
				reconsume(HYPHEN_MINUS);
				return consumeNumericToken();
			} else if (startsIdentifier(HYPHEN_MINUS)) {
				reconsume(HYPHEN_MINUS);
				return consumeIdentLikeToken();
			} else {
				final char[] buf = new char[2];
				final int numRead = peek(buf);
				if (numRead >= 2 && buf[0] == HYPHEN_MINUS && buf[1] == GREATER_THAN_SIGN) {
					return new CDCToken();
				} else {
					return new DelimToken(HYPHEN_MINUS);
				}
			}
		} // break;
		case FULL_STOP: {
			if (startsNumber(FULL_STOP)) {
				reconsume(FULL_STOP);
				return consumeNumericToken();
			} else {
				return new DelimToken(FULL_STOP);
			}
		} // break;
		case COLON: {
			return new ColonToken();
		} // break;
		case SEMICOLON: {
			return new SemicolonToken();
		} // break;
		case LESS_THAN_SIGN: {
			final char[] buf = new char[3];
			final int numRead = peek(buf);
			if (numRead >= 3 && buf[0] == EXCLAMATION_MARK
					&& buf[1] == HYPHEN_MINUS && buf[2] == HYPHEN_MINUS) {
				consume();
				consume();
				consume();
				return new CDOToken();
			} else {
				return new DelimToken(LESS_THAN_SIGN);
			}
		} // break;
		case COMMERCIAL_AT: {
			if (startsIdentifier(COMMERCIAL_AT)) {
				final AtKeywordToken atKeywordToken = new AtKeywordToken();
				atKeywordToken.setStringValue(consumeName());
				return atKeywordToken;
			} else {
				return new DelimToken(COMMERCIAL_AT);
			}
		} // break;
		case LEFT_SQUARE_BRACKET: {
			return new LeftSquareBracketToken();
		} // break;
		case REVERSE_SOLIDUS: {
			if (startsValidEscapeSequence(REVERSE_SOLIDUS)) {
				reconsume(REVERSE_SOLIDUS);
				return consumeIdentLikeToken();
			} else {
				if (isAllowParseErrors()) {
					return new DelimToken(REVERSE_SOLIDUS);
				} else {
					throw new ParseErrorException("Unexpected character tokenizing CSS: " + REVERSE_SOLIDUS);
				}
			}
		} // break;
		case RIGHT_SQUARE_BRACKET: {
			return new RightSquareBracketToken();
		} // break;
		case CIRCUMFLEX_ACCENT: {
			if (peek() == EQUALS_SIGN) {
				return new PrefixMatchToken();
			} else {
				return new DelimToken(CIRCUMFLEX_ACCENT);
			}
		} // break;
		case LEFT_CURLY_BRACKET: {
			return new LeftCurlyBracketToken();
		} // break;
		case RIGHT_CURLY_BRACKET: {
			return new RightCurlyBracketToken();
		} // break;
		case LATIN_CAPITAL_LETTER_U:
		case LATIN_SMALL_LETTER_U: {
			final char[] buf = new char[2];
			final int numRead = peek(buf);
			if (numRead >= 2 && buf[0] == PLUS_SIGN
					&& (isHexDigit(buf[1]) || buf[1] == QUESTION_MARK)) {
				consume();
				return consumeUnicodeRangeToken();
			} else {
				reconsume(ch);
				return consumeIdentLikeToken();
			}
		} // break;
		case VERTICAL_LINE: {
			ch = peek();
			if (ch == EQUALS_SIGN) {
				consume();
				return new DashMatchToken();
			} else if (ch == VERTICAL_LINE) {
				consume();
				return new ColumnToken();
			} else {
				return new DelimToken(VERTICAL_LINE);
			}
		} // break;
		case TILDE: {
			ch = peek();
			if (ch == EQUALS_SIGN) {
				consume();
				return new IncludeMatchToken();
			} else {
				return new DelimToken(TILDE);
			}
		} // break;
		case EOF: {
			return new EOFToken();
		} // break;
		default: {
			if (isDigit(ch)) {
				reconsume(ch);
				return consumeNumericToken();
			} else if (isNameStartCodePoint(ch)) {
				reconsume(ch);
				return consumeIdentLikeToken();
			} else {
				return new DelimToken((char) ch);
			}
		} // break;
		}
	}

	/**
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-comments">4.3.2 Consume comments</a>
	 */
	protected void consumeComments() throws IOException {
		final char[] nextTwo = new char[2];
		int numRead = peek(nextTwo);
		while (numRead == 2) {
			if (nextTwo[0] == SOLIDUS && nextTwo[1] == ASTERISK) {
				consume(); // the solidus
				consume(); // the asterisk
				do {
					int ch;
					do {
						ch = consume();
						if (ch == EOF) {
							return;
						}
					} while (ch != ASTERISK);
				} while (peek() != SOLIDUS);
				consume();
			} else {
				return;
			}
			numRead = peek(nextTwo);
		}
	}

	/**
	 * @param ch
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-a-string-token">4.3.5 Consume a string token</a>
	 */
	protected LexicalToken consumeStringToken(int ch) throws IOException {
		return consumeStringToken(ch, (char) ch);
	}

	/**
	 * @param ch
	 * @param endingCodePoint
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-a-string-token">4.3.5 Consume a string token</a>
	 */
	protected LexicalToken consumeStringToken(int ch, final char endingCodePoint) throws IOException {
		final StringToken token = new StringToken();
		do {
			ch = consume();
			switch (ch) {
			case EOF: {
				return token;
			} // break;
			case LINE_FEED: {
				if (isAllowParseErrors()) {
					reconsume(ch);
					return new BadStringToken();
				} else {
					throw new ParseErrorException("Newline in string.");
				}
			} // break;
			case REVERSE_SOLIDUS: {
				final int nextInputCodePoint = peek();
				switch (nextInputCodePoint) {
				case EOF: {
					// do nothing
				} break;
				case LINE_FEED: {
					consume();
				} break;
				default: {
					if (startsValidEscapeSequence(ch, nextInputCodePoint)) {
						token.append(consumeEscapedCodePoint());
					} else {
						token.append((char) ch);
					}
				} break;
				}
				return null;
			} // break;
			default: {
				if (ch == endingCodePoint) {
					return token;
				} else {
					token.append((char) ch);
				}
			} // break;
			}
		} while (true);
	}

	/**
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#starts-with-a-valid-escape">4.3.9 Check if two code points are a valid escape</a>
	 */
	protected boolean startsValidEscapeSequence(final int firstCharacter, final int secondCharacter) {
		if (firstCharacter != REVERSE_SOLIDUS) {
			return false;
		} else if (secondCharacter == LINE_FEED) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return
	 * @throws IOException 
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#starts-with-a-valid-escape">4.3.9 Check if two code points are a valid escape</a>
	 */
	protected boolean startsValidEscapeSequence(final int consumedCharacter) throws IOException {
		return startsValidEscapeSequence(consumedCharacter, peek());
	}

	/**
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-an-escaped-code-point">4.3.8 Consume an escaped code point</a>
	 */
	protected char[] consumeEscapedCodePoint() throws IOException {
		// assume REVERSE_SOLIDUS has already been consumed
		int ch = consume();
		if (ch == EOF) {
			return Character.toChars(REPLACEMENT_CHARACTER);
		} else if (ch >= '0' && ch <= '9'
				|| ch >= 'a' && ch <= 'f'
				|| ch >= 'A' && ch <= 'F') {
			final StringBuilder buf = new StringBuilder();
			do {
				buf.append(ch);
				ch = consume();
			} while ((ch >= '0' && ch <= '9'
					|| ch >= 'a' && ch <= 'f'
					|| ch >= 'A' && ch <= 'F') && buf.length() < 6);
			if (ch == LINE_FEED || ch == CHARACTER_TABULATION || ch == SPACE) {
				// do nothing
			} else {
				reconsume(ch);
			}
			final int val = Integer.parseInt(buf.toString(), 16);
			if (val == 0) {
				return Character.toChars(REPLACEMENT_CHARACTER);
			}
			final char[] chars = Character.toChars(val);
			if (chars.length == 1 && Character.isSurrogate(chars[0])) {
				return Character.toChars(REPLACEMENT_CHARACTER);
			}
			return chars;
		} else {
			return Character.toChars(ch);
		}
	}

	/**
	 * @param firstCodePoint
	 * @param secondCodePoint
	 * @param thirdCodePoint
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#would-start-an-identifier">4.3.10 Check if three code points would start an identifier</a>
	 */
	protected boolean startsIdentifier(final int firstCodePoint, final int secondCodePoint, final int thirdCodePoint) {
		if (firstCodePoint == HYPHEN_MINUS) {
			if (isNameStartCodePoint(secondCodePoint)
					|| secondCodePoint == HYPHEN_MINUS
					|| startsValidEscapeSequence(secondCodePoint, thirdCodePoint)) {
				return true;
			} else {
				return false;
			}
		} else if (isNameStartCodePoint(firstCodePoint)) {
			return true;
		} else if (firstCodePoint == REVERSE_SOLIDUS) {
			if (startsValidEscapeSequence(firstCodePoint, secondCodePoint)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * @param consumedCodePoint
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#would-start-an-identifier">4.3.10 Check if three code points would start an identifier</a>
	 */
	protected boolean startsIdentifier(final int consumedCodePoint) throws IOException {
		final int char2 = consume();
		if (char2 == EOF) {
			return false;
		}
		final int char3 = consume();
		if (char3 == EOF) {
			reconsume(char2);
			return false;
		}
		reconsume(char3);
		reconsume(char2);
		return startsIdentifier(consumedCodePoint, char2, char3);
	}

	/**
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-a-name">4.3.12 Consume a name</a>
	 */
	protected String consumeName() throws IOException {
		final StringBuilder buf = new StringBuilder();
		do {
			final int ch = consume();
			if (isNameCodePoint(ch)) {
				buf.append((char) ch);
			} else if (startsValidEscapeSequence(ch)) {
				buf.append(consumeEscapedCodePoint());
			} else if (ch == EOF) {
				return buf.toString();
			} else {
				reconsume(ch);
				return buf.toString();
			}
		} while (true);
	}

	/**
	 * @param firstCodePoint
	 * @param secondCodePoint
	 * @param thirdCodePoint
	 * @return
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#starts-with-a-number">4.3.11 Check if three code points would start a number</a>
	 */
	protected boolean startsNumber(final int firstCodePoint, final int secondCodePoint, final int thirdCodePoint) {
		if (firstCodePoint == PLUS_SIGN || firstCodePoint == HYPHEN_MINUS) {
			if (isDigit(secondCodePoint)) {
				return true;
			} else if (secondCodePoint == FULL_STOP && isDigit(thirdCodePoint)) {
				return true;
			} else {
				return false;
			}
		} else if (firstCodePoint == FULL_STOP) {
			if (isDigit(secondCodePoint)) {
				return true;
			} else {
				return false;
			}
		} else if (isDigit(firstCodePoint)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param consumedCharacter
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#starts-with-a-number">4.3.11 Check if three code points would start a number</a>
	 */
	protected boolean startsNumber(final int consumedCharacter) throws IOException {
		final int char2 = consume();
		if (char2 == EOF) {
			return false;
		}
		final int char3 = consume();
		if (char3 == EOF) {
			reconsume(char2);
			return false;
		}
		reconsume(char3);
		reconsume(char2);
		return startsNumber(consumedCharacter, char2, char3);
	}

	/**
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-a-numeric-token">4.3.3 Consume a numeric token</a>
	 */
	protected LexicalToken consumeNumericToken() throws IOException {
		final NumberToken numberToken = consumeNumber();
		final char[] buf = new char[3];
		final int numRead = peek(buf);
		if (numRead >= 3 && startsIdentifier(buf[0], buf[1], buf[2])) {
			final DimensionToken dimensionToken = new DimensionToken();
			dimensionToken.setNumericType(numberToken.getNumericType());
			dimensionToken.setNumericValue(numberToken.getNumericValue());
			dimensionToken.setStringValue(numberToken.getStringValue());
			final String unit = consumeName();
			dimensionToken.setUnit(unit);
			return dimensionToken;
		} else if (numRead >= 1 && buf[0] == PERCENTAGE_SIGN) {
			consume();
			final PercentageToken percentageToken = new PercentageToken();
			percentageToken.setNumericValue(numberToken.getNumericValue());
			percentageToken.setStringValue(numberToken.getStringValue());
			return percentageToken;
		} else {
			return numberToken;
		}
	}

	/**
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-a-number">4.3.13 Consume a number</a>
	 */
	protected NumberToken consumeNumber() throws IOException {
		final NumberToken numberToken = new NumberToken();
		final StringBuilder repr = new StringBuilder();
		numberToken.setNumericType(TypedNumericValueToken.NumericType.INTEGER);
		int ch = peek();
		if (ch == PLUS_SIGN || ch == HYPHEN_MINUS) {
			repr.append((char) consume());
		}
		ch = peek();
		while (isDigit(ch)) {
			repr.append((char) consume());
			ch = peek();
		}
		final char[] buf = new char[3];
		int numRead = peek(buf);
		if (numRead >= 2 && buf[0] == FULL_STOP && isDigit(buf[1])) {
			repr.append((char) consume());
			ch = consume();
			while (isDigit(ch)) {
				repr.append((char) ch);
				ch = consume();
			}
			reconsume(ch);
			numberToken.setNumericType(TypedNumericValueToken.NumericType.NUMBER);
		}
		numRead = peek(buf);
		if (buf[0] == LATIN_CAPITAL_LETTER_E || buf[0] == LATIN_SMALL_LETTER_E) {
			if ((numRead >= 2 && isDigit(buf[1])
					|| (numRead >= 3 && isDigit(buf[2]) && (buf[1] == PLUS_SIGN || buf[1] == HYPHEN_MINUS)))) {
				ch = consume();
				repr.append((char) ch);
				ch = consume();
				repr.append((char) ch);
				ch = consume();
				while (isDigit(ch)) {
					repr.append((char) ch);
					ch = consume();
				}
				reconsume(ch);
				numberToken.setNumericType(TypedNumericValueToken.NumericType.NUMBER);
			}
		}
		numberToken.setStringValue(repr.toString());
		final Number number = convertStringToNumber(repr.toString());
		numberToken.setNumericValue(number);
		return numberToken;
	}

	private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\+|-)?([0-9]*)(\\.)?([0-9]*)(e|E)?(\\+|-)?([0-9]*)");

	/**
	 * Converts a string into a number using exactly the algorithm specified in
	 * the CSS specification.
	 * 
	 * <p>Yes, I know Java already has libraries to convert strings into numbers.
	 * However, this implementation is intended to be reference-quality, and as
	 * such I am going out of my way to precisely implement the specifications
	 * as written.  Any novice programmer should be able to read the specification
	 * and read my source code and confirm that their behavior match.
	 * 
	 * @param str the string to convert
	 * @return the number
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#convert-a-string-to-a-number">4.3.14 Convert a string to a number</a>
	 */
	protected Number convertStringToNumber(final String str) {
		final Matcher matcher = NUMBER_PATTERN.matcher(str);
		if (matcher.matches()) {
			final String sign = matcher.group(1);
			final String integerPart = matcher.group(2);
			@SuppressWarnings("unused")
			final String decimalPoint = matcher.group(3);
			final String fractionalPart = matcher.group(4);
			@SuppressWarnings("unused")
			final String exponentIndicator = matcher.group(5);
			final String exponentSign = matcher.group(6);
			final String exponent = matcher.group(7);
			
			final int s;
			if (sign != null && sign.equals("-")) {
				s = -1;
			} else {
				s = 1;
			}
			final int i;
			if (integerPart != null && !integerPart.isEmpty()) {
				i = Integer.parseInt(integerPart);
			} else {
				i = 0;
			}
			// decimal point
			final int f;
			final int d;
			if (fractionalPart != null && !fractionalPart.isEmpty()) {
				f = Integer.parseInt(fractionalPart);
				d = fractionalPart.length();
			} else {
				f = 0;
				d = 0;
			}
			// exponent indicator
			final int t;
			if (exponentSign != null && exponentSign.equals("-")) {
				t = -1;
			} else {
				t = 1;
			}
			final int e;
			if (exponent != null && !exponent.isEmpty()) {
				e = Integer.parseInt(exponent);
			} else {
				e = 0;
			}
			
			final double value = s * (i + f * Math.pow(10, -d)) * Math.pow(10, t * e); 
			
			return Double.valueOf(value);
		} else {
			return Double.valueOf(0);
		}
	}

	/**
	 * @return
	 * @throws IOException 
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-an-ident-like-token">4.3.4 Consume an ident-like token</a>
	 */
	protected LexicalToken consumeIdentLikeToken() throws IOException {
		final String name = consumeName();
		final int ch = peek();
		if (name.toLowerCase(Locale.ENGLISH).equals("url") && ch == LEFT_PARENTHESIS) {
			consume();
			return consumeURLToken();
		} else if (ch == LEFT_PARENTHESIS) {
			consume();
			final FunctionToken functionToken = new FunctionToken();
			functionToken.setStringValue(name);
			return functionToken;
		} else {
			final IdentToken identToken = new IdentToken();
			identToken.setStringValue(name);
			return identToken;
		}
	}

	/**
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-a-url-token">4.3.6 Consume a url token</a>
	 */
	protected LexicalToken consumeURLToken() throws IOException {
		final URLToken urlToken = new URLToken();
		consumeWhitespace();
		int ch = peek();
		if (ch == EOF) {
			return urlToken;
		}
		if (ch == QUOTATION_MARK || ch == APOSTROPHE) {
			consume();
			final LexicalToken token = consumeStringToken(ch);
			if (token.getLexicalType() == LexicalToken.LexicalType.BAD_STRING_TOKEN) {
				consumeRemainsOfBadURL();
				return new BadURLToken();
			}
			assert token.getLexicalType() == LexicalToken.LexicalType.STRING_TOKEN;
			final StringToken stringToken = (StringToken) token;
			urlToken.setStringValue(stringToken.getStringValue());
			consumeWhitespace();
			ch = consume();
			if (ch == RIGHT_PARENTHESIS || ch == EOF) {
				return urlToken;
			} else {
				consumeRemainsOfBadURL();
				return new BadURLToken();
			}
		}
		ch = consume();
		while (ch != RIGHT_PARENTHESIS && ch != EOF) {
			if (isWhitespace(ch)) {
				consumeWhitespace();
				ch = peek();
				if (ch == RIGHT_PARENTHESIS || ch == EOF) {
					consume();
					return urlToken;
				} else {
					consumeRemainsOfBadURL();
					return new BadURLToken();
				}
			} else if (ch == QUOTATION_MARK || ch == APOSTROPHE
					|| ch == LEFT_PARENTHESIS || isNonPrintableCodePoint(ch)) {
				if (isAllowParseErrors()) {
					consumeRemainsOfBadURL();
					return new BadURLToken();
				} else {
					throw new ParseErrorException("Illegal character parsing URL token: " + (char) ch);
				}
			} else if (ch == REVERSE_SOLIDUS) {
				if (startsValidEscapeSequence(ch)) {
					urlToken.append(consumeEscapedCodePoint());
				} else {
					if (isAllowParseErrors()) {
						consumeRemainsOfBadURL();
						return new BadURLToken();
					} else {
						throw new ParseErrorException();
					}
				}
			} else {
				urlToken.append((char) ch);
			}
			ch = consume();
		}
		return urlToken;
	}

	/**
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-the-remnants-of-a-bad-url">4.3.15 Consume the remnants of a bad url</a>
	 */
	protected void consumeRemainsOfBadURL() throws IOException {
		int ch = consume();
		while (ch != RIGHT_PARENTHESIS && ch != EOF) {
			if (startsValidEscapeSequence(ch)) {
				consumeEscapedCodePoint();
			}
			ch = consume();
		}
	}

	/**
	 * @return
	 * @throws IOException
	 * @see <a href="http://dev.w3.org/csswg/css-syntax/#consume-a-unicode-range-token">4.3.7 Consume a unicode-range token</a>
	 */
	protected LexicalToken consumeUnicodeRangeToken() throws IOException {
		final StringBuilder startBuf = new StringBuilder();
		final StringBuilder endBuf = new StringBuilder();
		boolean containedQuestionMarks = false;
		int ch = consume();
		while (isHexDigit(ch) && startBuf.length() < 6) {
			startBuf.append((char) ch);
			endBuf.append((char) ch);
			ch = consume();
		}
		while (ch == QUESTION_MARK && startBuf.length() < 6) {
			containedQuestionMarks = true;
			startBuf.append(DIGIT_ZERO);
			endBuf.append(LATIN_CAPITAL_LETTER_F);
			ch = consume();
		}
		if (containedQuestionMarks) {
			final UnicodeRangeToken unicodeRangeToken = new UnicodeRangeToken();
			unicodeRangeToken.setStart(Integer.parseInt(startBuf.toString(), 16));
			unicodeRangeToken.setEnd(Integer.parseInt(endBuf.toString(), 16));
			return unicodeRangeToken;
		}
		final int start = Integer.parseInt(startBuf.toString(), 16);
		final int end;
		final int p = peek();
		if (ch == HYPHEN_MINUS && isHexDigit(p)) {
			endBuf.setLength(0);
			ch = consume();
			while (isHexDigit(ch) && endBuf.length() < 6) {
				endBuf.append((char) ch);
				ch = consume();
			}
			end = Integer.parseInt(endBuf.toString(), 16);
		} else {
			end = start;
		}
		reconsume(ch);
		final UnicodeRangeToken unicodeRangeToken = new UnicodeRangeToken();
		unicodeRangeToken.setStart(start);
		unicodeRangeToken.setEnd(end);
		return unicodeRangeToken;
	}

	protected void consumeWhitespace() throws IOException {
		int ch;
		do {
			ch = consume();
		} while (isWhitespace(ch));
		reconsume(ch);
	}

	public static void main(final String[] args) throws IOException {
		final URL url;
		url = new URL("http://rgsb.org/rgsb.css");
		final Download download = new Download(url);
		download.connect();
		download.download();
		System.out.println(download.getContent());
		System.out.println();
		System.out.println("--------------------------------------------------------------------------------------------------------");
		System.out.println();
		final String contentEncoding = download.getContentEncoding();
		final Reader reader;
		if (contentEncoding == null) {
			reader = new InputStreamReader(download.getInputStream(), Charset.forName("UTF-8"));
		} else {
			reader = new InputStreamReader(download.getInputStream(), contentEncoding);
		}
		final Tokenizer tokenizer = new Tokenizer(reader);
		tokenizer.setAllowParseErrors(false);
		
		LexicalToken token;
		do {
			token = tokenizer.getNextToken();
			System.out.println(token);
		} while (token.getLexicalType() != LexicalToken.LexicalType.EOF);
	}

}
