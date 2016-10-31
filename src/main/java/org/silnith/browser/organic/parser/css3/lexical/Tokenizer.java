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
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.silnith.browser.organic.network.Download;
import org.silnith.browser.organic.parser.ParseErrorException;
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
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.silnith.browser.organic.parser.css3.lexical.token.NumberToken;
import org.silnith.browser.organic.parser.css3.lexical.token.NumericValueToken;
import org.silnith.browser.organic.parser.css3.lexical.token.PercentageToken;
import org.silnith.browser.organic.parser.css3.lexical.token.PrefixMatchToken;
import org.silnith.browser.organic.parser.css3.lexical.token.RightCurlyBracketToken;
import org.silnith.browser.organic.parser.css3.lexical.token.RightParenthesisToken;
import org.silnith.browser.organic.parser.css3.lexical.token.RightSquareBracketToken;
import org.silnith.browser.organic.parser.css3.lexical.token.SemicolonToken;
import org.silnith.browser.organic.parser.css3.lexical.token.StringToken;
import org.silnith.browser.organic.parser.css3.lexical.token.SubstringMatchToken;
import org.silnith.browser.organic.parser.css3.lexical.token.SuffixMatchToken;
import org.silnith.browser.organic.parser.css3.lexical.token.TypedNumericValueToken;
import org.silnith.browser.organic.parser.css3.lexical.token.URLToken;
import org.silnith.browser.organic.parser.css3.lexical.token.UnicodeRangeToken;
import org.silnith.browser.organic.parser.css3.lexical.token.WhitespaceToken;


/**
 * Produces a stream of tokens from an input source of code points.
 * 
 * @see <a href="https://www.w3.org/TR/css-syntax-3/">CSS Syntax Module Level 3</a>
 * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenization">4. Tokenization</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class Tokenizer implements TokenStream {
    
    private static final int EOF = -1;
    
    /**
     * The greatest code point defined by Unicode: U+10FFFF.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#maximum-allowed-code-point">maximum allowed code point</a>
     */
    private static final int MAXIMUM_ALLOWED_CODE_POINT = 0x10FFFF;

    private final Reader reader;
    
    private int currentInputCodePoint;
    
    private int nextInputCodePoint;
    
    private final int[] lookahead;
    
    private boolean reconsumeCurrentInputCodePoint;
    
    private boolean allowParseErrors;
    
    private boolean primed;
    
    /**
     * Constructs a new tokenizer using the given character reader.  Any reader
     * will work, it does not need to be wrapped in an {@link InputStreamPreprocessor}.
     * 
     * @param reader the character stream to tokenize
     */
    public Tokenizer(final Reader reader) {
        super();
        this.reader = new InputStreamPreprocessor(reader);
        this.lookahead = new int[3];
        this.reconsumeCurrentInputCodePoint = false;
        this.allowParseErrors = false;
        this.primed = false;
    }
    
    /**
     * Sets whether the tokenizer will attempt to recover from basic parse errors.
     * 
     * @param allowParseErrors whether to allow recovery from parse errors
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizing-and-parsing">3. Tokenizing and Parsing CSS</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#parse-error">parse errors</a>
     */
    public void setAllowParseErrors(final boolean allowParseErrors) {
        this.allowParseErrors = allowParseErrors;
    }

    /**
     * Returns whether the tokenizer will attempt to recover from basic parse errors.
     * 
     * @return whether the tokenizer will attempt recovery from parse errors
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizing-and-parsing">3. Tokenizing and Parsing CSS</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#parse-error">parse errors</a>
     */
    public boolean isAllowParseErrors() {
        return allowParseErrors;
    }
    
    /**
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-algorithms">4.3. Tokenizer Algorithms</a>
     */
    @Override
    public LexicalToken getNextToken() throws IOException {
        if (!primed) {
            prime();
        }
        return consumeToken();
    }
    
    @PostConstruct
    protected void prime() throws IOException {
        if (primed) {
            throw new IllegalStateException("Cannot prime tokenizer twice.");
        }
        currentInputCodePoint = 0;
        nextInputCodePoint = readCodePoint();
        lookahead[0] = readCodePoint();
        lookahead[1] = readCodePoint();
        lookahead[2] = 0;
        /*
         * We only use three elements of look-ahead, kept in nextInputCodePoint,
         * lookahead[0], and lookahead[1].  The additional element is for
         * reconsume(), so we have a place to keep the last lookahead when
         * everything gets pushed back one slot.
         */
        primed = true;
        
//        Character.isHighSurrogate((char) currentInputCodePoint);
//        Character.isSurrogate((char) currentInputCodePoint);
//        Character.isSurrogatePair((char) currentInputCodePoint, (char) nextInputCodePoint);
//        Character.toCodePoint((char) currentInputCodePoint, (char) nextInputCodePoint);
    }

    /**
     * The last code point to have been consumed.
     * 
     * @return the last code point consumed
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#current-input-code-point">current input code point</a>
     */
    public int getCurrentInputCodePoint() {
        return currentInputCodePoint;
    }

    /**
     * The first code point in the input stream that has not yet been consumed.
     * 
     * @return the next code point to be consumed
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#next-input-code-point">next input code point</a>
     */
    public int getNextInputCodePoint() {
        return nextInputCodePoint;
    }
    
    /**
     * Consumes the next code point.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#next-input-code-point">next input code point</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#current-input-code-point">current input code point</a>
     */
    protected void consume() throws IOException {
        currentInputCodePoint = nextInputCodePoint;
        nextInputCodePoint = lookahead[0];
        lookahead[0] = lookahead[1];
        if (reconsumeCurrentInputCodePoint) {
            reconsumeCurrentInputCodePoint = false;
            lookahead[1] = lookahead[2];
            lookahead[2] = 0;
        } else {
            lookahead[1] = readCodePoint();
        }
        
//        System.out.print("consumed: '");
//        System.out.print(toChars(currentInputCodePoint));
//        System.out.println("'");
//        System.out.print("lookahead: '");
//        System.out.print(toChars(nextInputCodePoint));
//        System.out.print(toChars(lookahead[0]));
//        System.out.print(toChars(lookahead[1]));
//        System.out.println("'");
    }
    
    private char[] toChars(final int codePoint) {
        if (codePoint == EOF) {
            return new char[] { '-', '1' };
        } else {
            return Character.toChars(codePoint);
        }
    }

    private int readCodePoint() throws IOException {
        final int nextCharacter = reader.read();
        if (nextCharacter == EOF) {
            return nextCharacter;
        } else {
            final char highSurrogate = (char) nextCharacter;
            if (Character.isHighSurrogate(highSurrogate)) {
                final int charRead = reader.read();
                if (charRead == EOF) {
                    /*
                     * EOF can be ignored since further reads will return the same thing.
                     */
                    return nextCharacter;
                } else {
                    final char lowSurrogate = (char) charRead;
                    if (Character.isLowSurrogate(lowSurrogate)) {
                        return Character.toCodePoint(highSurrogate, lowSurrogate);
                    } else {
                        System.out.println("Discarding high surrogate not followed by a low surrogate.");
                        System.out.println("high surrogate: \\u" + Integer.toHexString(nextCharacter));
                        System.out.println("following character: \\u" + Integer.toHexString(charRead));
                        return charRead;
                    }
                }
            } else {
                return nextCharacter;
            }
        }
    }
    
    /**
     * Pushes the {@link #getCurrentInputCodePoint() current input code point}
     * back onto the front of the input stream,
     * so that the next time the {@link #getNextInputCodePoint() next input code point}
     * is consumed, it will instead reconsume the
     * {@link #getCurrentInputCodePoint() current input code point}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#reconsume-the-current-input-code-point">reconsume the current input code point</a>
     */
    protected void reconsume() throws IOException {
        assert !reconsumeCurrentInputCodePoint;
        
        lookahead[2] = lookahead[1];
        lookahead[1] = lookahead[0];
        lookahead[0] = nextInputCodePoint;
        nextInputCodePoint = currentInputCodePoint;
        currentInputCodePoint = 0;
        
        reconsumeCurrentInputCodePoint = true;
        
//        System.out.println("reconsume");
//        System.out.print("lookahead: '");
//        System.out.print(toChars(nextInputCodePoint));
//        System.out.print(toChars(lookahead[0]));
//        System.out.print(toChars(lookahead[1]));
//        System.out.print(toChars(lookahead[2]));
//        System.out.println("'");
    }
    
    /**
     * A conceptual code point representing the end of the input stream.
     * Whenever the input stream is empty, the
     * {@link #getNextInputCodePoint() next input code point} is always an EOF code point.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#eof-code-point">EOF code point</a>
     */
    protected boolean isEOFCodePoint(final int ch) {
        return ch == EOF;
    }
    
    /**
     * A code point between {@link #DIGIT_ZERO U+0030 DIGIT ZERO (0)} and {@link #DIGIT_NINE U+0039 DIGIT NINE (9)}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#digit">digit</a>
     */
    protected boolean isDigit(final int ch) {
        return ch >= DIGIT_ZERO && ch <= DIGIT_NINE;
    }
    
    /**
     * A {@link #isDigit(int) digit}, or a code point between
     * {@link #LATIN_CAPITAL_LETTER_A U+0041 LATIN CAPITAL LETTER A (A)}
     * and {@link #LATIN_CAPITAL_LETTER_F U+0046 LATIN CAPITAL LETTER F (F)},
     * or a code point between
     * {@link #LATIN_SMALL_LETTER_A U+0061 LATIN SMALL LETTER A (a)} and
     * {@link #LATIN_SMALL_LETTER_F U+0066 LATIN SMALL LETTER F (f)}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#hex-digit">hex digit</a>
     */
    protected boolean isHexDigit(final int ch) {
        return isDigit(ch) || ch >= LATIN_CAPITAL_LETTER_A && ch <= LATIN_CAPITAL_LETTER_F
                || ch >= LATIN_SMALL_LETTER_A && ch <= LATIN_SMALL_LETTER_F;
    }
    
    /**
     * A code point between {@link #LATIN_CAPITAL_LETTER_A U+0041 LATIN CAPITAL LETTER A (A)}
     * and {@link #LATIN_CAPITAL_LETTER_Z U+005A LATIN CAPITAL LETTER Z (Z)}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#uppercase-letter">uppercase letter</a>
     */
    protected boolean isUppercaseLetter(final int ch) {
        return ch >= LATIN_CAPITAL_LETTER_A && ch <= LATIN_CAPITAL_LETTER_Z;
    }
    
    /**
     * A code point between {@link #LATIN_SMALL_LETTER_A U+0061 LATIN SMALL LETTER A (a)}
     * and {@link #LATIN_SMALL_LETTER_Z U+007A LATIN SMALL LETTER Z (z)}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#lowercase-letter">lowercase letter</a>
     */
    protected boolean isLowercaseLetter(final int ch) {
        return ch >= LATIN_SMALL_LETTER_A && ch <= LATIN_SMALL_LETTER_Z;
    }
    
    /**
     * An {@link #isUppercaseLetter(int) uppercase letter} or a {@link #isLowercaseLetter(int) lowercase letter}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#letter">letter</a>
     */
    protected boolean isLetter(final int ch) {
        return isUppercaseLetter(ch) || isLowercaseLetter(ch);
    }
    
    /**
     * A code point with a value equal to or greater than {@link #CONTROL U+0080 &lt;control&gt;}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#non-ascii-code-point">non-ASCII code point</a>
     */
    protected boolean isNonASCIICodePoint(final int ch) {
        return ch >= CONTROL;
    }
    
    /**
     * A {@link #isLetter(int) letter}, a {@link #isNonASCIICodePoint(int) non-ASCII code point},
     * or {@link #LOW_LINE U+005F LOW LINE (_)}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#name-start-code-point">name-start code point</a>
     */
    protected boolean isNameStartCodePoint(final int ch) {
        return isLetter(ch) || isNonASCIICodePoint(ch) || ch == LOW_LINE;
    }
    
    /**
     * A {@link #isNameStartCodePoint(int) name-start code point}, a {@link #isDigit(int) digit},
     * or {@link #HYPHEN_MINUS U+002D HYPHEN-MINUS (-)}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#name-code-point">name code point</a>
     */
    protected boolean isNameCodePoint(final int ch) {
        return isNameStartCodePoint(ch) || isDigit(ch) || ch == HYPHEN_MINUS;
    }
    
    /**
     * A code point between {@link #NULL U+0000 NULL} and {@link #BACKSPACE U+0008 BACKSPACE},
     * or {@link #LINE_TABULATION U+000B LINE TABULATION}, or a code point between
     * {@link #SHIFT_OUT U+000E SHIFT OUT} and {@link #INFORMATION_SEPARATOR_ONE U+001F INFORMATION SEPARATOR ONE},
     * or {@link #DELETE U+007F DELETE}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#non-printable-code-point">non-printable code point</a>
     */
    protected boolean isNonPrintableCodePoint(final int ch) {
        return ch >= NULL && ch <= BACKSPACE || ch == LINE_TABULATION
                || ch >= SHIFT_OUT && ch <= INFORMATION_SEPARATOR_ONE || ch == DELETE;
    }
    
    /**
     * {@link #LINE_FEED U+000A LINE FEED}.
     * <p>
     * Note that {@link org.silnith.browser.organic.parser.util.UnicodeCodePoints#CARRIAGE_RETURN U+000D CARRIAGE RETURN}
     * and {@link org.silnith.browser.organic.parser.util.UnicodeCodePoints#FORM_FEED U+000C FORM FEED}
     * are not included in this definition, as they are converted to {@link #LINE_FEED U+000A LINE FEED} during preprocessing.
     * 
     * @see InputStreamPreprocessor
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#newline">newline</a>
     */
    protected boolean isNewline(final int ch) {
        return ch == LINE_FEED;
    }
    
    /**
     * A {@link #isNewline(int) newline}, {@link #CHARACTER_TABULATION U+0009 CHARACTER TABULATION},
     * or {@link #SPACE U+0020 SPACE}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#whitespace">whitespace</a>
     */
    protected boolean isWhitespace(final int ch) {
        return isNewline(ch) || ch == CHARACTER_TABULATION || ch == SPACE;
    }
    
    /**
     * A code point between U+D800 and U+DFFF inclusive.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#tokenizer-definitions">4.2. Definitions</a>
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#surrogate-code-point">surrogate code point</a>
     */
    protected boolean isSurrogateCodePoint(final int ch) {
        return ch >= '\uD800' && ch <= '\uDFFF';
    }
    
    /**
     * Consumes a token from a stream of code points. This will return a single token of any type.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#consume-a-token">4.3.1. Consume a token</a>
     */
    protected LexicalToken consumeToken() throws IOException {
        consume();
        switch (currentInputCodePoint) {
        /*
         * Whitespace is in the default case.
         */
        case QUOTATION_MARK: {
            return consumeStringToken(QUOTATION_MARK);
        } // break;
        case NUMBER_SIGN: {
            if (isNameCodePoint(nextInputCodePoint) || isValidEscape(nextInputCodePoint, lookahead[0])) {
                final HashToken hashToken = new HashToken();
                if (wouldStartIdentifier(nextInputCodePoint, lookahead[0], lookahead[1])) {
                    hashToken.setTypeFlag(HashToken.TypeFlag.ID);
                }
                final String name = consumeName();
                hashToken.setStringValue(name);
                return hashToken;
            } else {
                assert currentInputCodePoint == NUMBER_SIGN;
                
                return new DelimToken(NUMBER_SIGN);
            }
        } // break;
        case DOLLAR_SIGN: {
            if (nextInputCodePoint == EQUALS_SIGN) {
                consume();
                return new SuffixMatchToken();
            } else {
                assert currentInputCodePoint == DOLLAR_SIGN;
                
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
            if (nextInputCodePoint == EQUALS_SIGN) {
                consume();
                return new SubstringMatchToken();
            } else {
                assert currentInputCodePoint == ASTERISK;
                
                return new DelimToken(ASTERISK);
            }
        } // break;
        case PLUS_SIGN: {
            if (wouldStartNumber()) {
                reconsume();
                return consumeNumericToken();
            } else {
                assert currentInputCodePoint == PLUS_SIGN;
                
                return new DelimToken(PLUS_SIGN);
            }
        } // break;
        case COMMA: {
            return new CommaToken();
        } // break;
        case HYPHEN_MINUS: {
            if (wouldStartNumber()) {
                reconsume();
                return consumeNumericToken();
            } else if (wouldStartIdentifier()) {
                reconsume();
                return consumeIdentLikeToken();
            } else if (nextInputCodePoint == HYPHEN_MINUS && lookahead[0] == GREATER_THAN_SIGN) {
                consume();
                consume();
                return new CDCToken();
            } else {
                assert currentInputCodePoint == HYPHEN_MINUS;
                
                return new DelimToken(HYPHEN_MINUS);
            }
        } // break;
        case FULL_STOP: {
            if (wouldStartNumber()) {
                reconsume();
                return consumeNumericToken();
            } else {
                assert currentInputCodePoint == FULL_STOP;
                
                return new DelimToken(FULL_STOP);
            }
        } // break;
        case SOLIDUS: {
            if (nextInputCodePoint == ASTERISK) {
                consume();
                while (nextInputCodePoint != EOF) {
                    consume();
                    if (currentInputCodePoint == ASTERISK && nextInputCodePoint == SOLIDUS) {
                        consume();
                        break;
                    }
                }
                return consumeToken();
            } else {
                assert currentInputCodePoint == SOLIDUS;
                
                return new DelimToken(SOLIDUS);
            }
        } // break;
        case COLON: {
            return new ColonToken();
        } // break;
        case SEMICOLON: {
            return new SemicolonToken();
        } // break;
        case LESS_THAN_SIGN: {
            if (nextInputCodePoint == EXCLAMATION_MARK && lookahead[0] == HYPHEN_MINUS && lookahead[1] == HYPHEN_MINUS) {
                consume();
                consume();
                consume();
                return new CDOToken();
            } else {
                assert currentInputCodePoint == LESS_THAN_SIGN;
                
                return new DelimToken(LESS_THAN_SIGN);
            }
        } // break;
        case COMMERCIAL_AT: {
            if (wouldStartIdentifier(nextInputCodePoint, lookahead[0], lookahead[1])) {
                final String name = consumeName();
                final AtKeywordToken atKeywordToken = new AtKeywordToken();
                atKeywordToken.setStringValue(name);
                return atKeywordToken;
            } else {
                assert currentInputCodePoint == COMMERCIAL_AT;
                
                return new DelimToken(COMMERCIAL_AT);
            }
        } // break;
        case LEFT_SQUARE_BRACKET: {
            return new LeftSquareBracketToken();
        } // break;
        case REVERSE_SOLIDUS: {
            if (isValidEscape()) {
                reconsume();
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
            if (nextInputCodePoint == EQUALS_SIGN) {
                consume();
                return new PrefixMatchToken();
            } else {
                assert currentInputCodePoint == CIRCUMFLEX_ACCENT;
                
                return new DelimToken(CIRCUMFLEX_ACCENT);
            }
        } // break;
        case LEFT_CURLY_BRACKET: {
            return new LeftCurlyBracketToken();
        } // break;
        case RIGHT_CURLY_BRACKET: {
            return new RightCurlyBracketToken();
        } // break;
        /*
         * Digit is handled in the default case.
         */
        case LATIN_CAPITAL_LETTER_U: // fall through
        case LATIN_SMALL_LETTER_U: {
            if (nextInputCodePoint == PLUS_SIGN && (isHexDigit(lookahead[0]) || lookahead[0] == QUESTION_MARK)) {
                consume();
                return consumeUnicodeRangeToken();
            } else {
                reconsume();
                return consumeIdentLikeToken();
            }
        } // break;
        /*
         * Name-start code point is handled in the default case.
         */
        case VERTICAL_LINE: {
            if (nextInputCodePoint == EQUALS_SIGN) {
                consume();
                return new DashMatchToken();
            } else if (nextInputCodePoint == VERTICAL_LINE) {
                consume();
                return new ColumnToken();
            } else {
                assert currentInputCodePoint == VERTICAL_LINE;
                
                return new DelimToken(VERTICAL_LINE);
            }
        } // break;
        case TILDE: {
            if (nextInputCodePoint == EQUALS_SIGN) {
                consume();
                return new IncludeMatchToken();
            } else {
                assert currentInputCodePoint == TILDE;
                
                return new DelimToken(TILDE);
            }
        } // break;
        case EOF: {
            return new EOFToken();
        } // break;
        default: {
            if (isWhitespace(currentInputCodePoint)) {
                while (isWhitespace(nextInputCodePoint)) {
                    consume();
                }
                return new WhitespaceToken();
            } else if (isDigit(currentInputCodePoint)) {
                reconsume();
                return consumeNumericToken();
            } else if (isNameStartCodePoint(currentInputCodePoint)) {
                reconsume();
                return consumeIdentLikeToken();
            } else {
                /*
                 * anything else
                 */
                return new DelimToken(currentInputCodePoint);
            }
        } // break;
        }
    }
    
    /**
     * Consumes a numeric token from a stream of code points.
     * This returns either a {@link NumberToken}, {@link PercentageToken}, or {@link DimensionToken}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#consume-a-numeric-token">4.3.2. Consume a numeric token</a>
     */
    protected NumericValueToken consumeNumericToken() throws IOException {
        final NumberToken numberToken = consumeNumber();
        if (wouldStartIdentifier(nextInputCodePoint, lookahead[0], lookahead[1])) {
            final DimensionToken dimensionToken = new DimensionToken(numberToken);
            final String unit = consumeName();
            dimensionToken.setUnit(unit);
            return dimensionToken;
        } else if (nextInputCodePoint == PERCENTAGE_SIGN) {
            consume();
            return new PercentageToken(numberToken);
        } else {
            return numberToken;
        }
    }
    
    /**
     * Consumes an ident-like token from a stream of code points.
     * This returns an {@link IdentToken}, {@link FunctionToken}, {@link URLToken}, or {@link BadURLToken}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#consume-an-ident-like-token">4.3.3. Consume an ident-like token</a>
     */
    protected LexicalToken consumeIdentLikeToken() throws IOException {
        final String name = consumeName();
        if (name.toLowerCase(Locale.ENGLISH).equals("url") && nextInputCodePoint == LEFT_PARENTHESIS) {
            consume();
            return consumeURLToken();
        } else if (nextInputCodePoint == LEFT_PARENTHESIS) {
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
     * Consumes a string token from a stream of code points. This returns either
     * a {@link StringToken} or {@link BadStringToken}.
     * <p>
     * This algorithm must be called with an ending code point, which denotes the code point that ends the string.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#consume-a-string-token">4.3.4. Consume a string token</a>
     */
    protected LexicalToken consumeStringToken(final int endingCodePoint) throws IOException {
        assert currentInputCodePoint == endingCodePoint;
        
        final StringToken stringToken = new StringToken();
        do {
            consume();
            switch (currentInputCodePoint) {
            case EOF: {
                return stringToken;
            } // break;
            case REVERSE_SOLIDUS: {
                if (nextInputCodePoint == EOF) {
                    // do nothing
                } else if (isNewline(nextInputCodePoint)) {
                    consume();
                } else if (isValidEscape()) {
                    final int escapedCodePoint = consumeEscapedCodePoint();
                    stringToken.append(Character.toChars(escapedCodePoint));
                }
            } break;
            default: {
                if (currentInputCodePoint == endingCodePoint) {
                    return stringToken;
                } else if (isNewline(currentInputCodePoint)) {
                    if (isAllowParseErrors()) {
                        reconsume();
                        return new BadStringToken();
                    } else {
                        throw new ParseErrorException("Newline in string.");
                    }
                } else {
                    stringToken.append(Character.toChars(currentInputCodePoint));
                }
            } break;
            }
        } while (true);
    }
    
    /**
     * Consumes a url token from a stream of code points.
     * This returns either a {@link URLToken} or a {@link BadURLToken}.
     * <p>
     * This assumes that the initial "url(" has already been consumed.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#consume-a-url-token">4.3.5. Consume a url token</a>
     */
    protected LexicalToken consumeURLToken() throws IOException {
        assert currentInputCodePoint == LEFT_PARENTHESIS;
        
        final URLToken urlToken = new URLToken();
        while (isWhitespace(nextInputCodePoint)) {
            consume();
        }
        if (nextInputCodePoint == EOF) {
            return urlToken;
        }
        if (nextInputCodePoint == QUOTATION_MARK || nextInputCodePoint == APOSTROPHE) {
            /*
             * This consume() is missing from the specification, but it clearly belongs here.
             */
            consume();
            final LexicalToken stringToken = consumeStringToken(currentInputCodePoint);
            if (stringToken instanceof BadStringToken) {
                consumeRemnantsOfBadURL();
                return new BadURLToken();
            }
            assert stringToken instanceof StringToken;
            
            final StringToken strTok = (StringToken) stringToken;
            urlToken.setStringValue(strTok.getStringValue());
            while (isWhitespace(nextInputCodePoint)) {
                consume();
            }
            if (nextInputCodePoint == RIGHT_PARENTHESIS || nextInputCodePoint == EOF) {
                consume();
                return urlToken;
            } else {
                consumeRemnantsOfBadURL();
                return new BadURLToken();
            }
        }
        do {
            consume();
            switch (currentInputCodePoint) {
            case RIGHT_PARENTHESIS: // fall through
            case EOF: {
                return urlToken;
            } // break;
            case QUOTATION_MARK: // fall through
            case APOSTROPHE: // fall through
            case LEFT_PARENTHESIS: {
                if (isAllowParseErrors()) {
                    consumeRemnantsOfBadURL();
                    return new BadURLToken();
                } else {
                    throw new ParseErrorException("Bad URL.");
                }
            } // break;
            case REVERSE_SOLIDUS: {
                if (isValidEscape()) {
                    final int escapedCodePoint = consumeEscapedCodePoint();
                    urlToken.append(Character.toChars(escapedCodePoint));
                } else {
                    if (isAllowParseErrors()) {
                        consumeRemnantsOfBadURL();
                        return new BadURLToken();
                    } else {
                        throw new ParseErrorException("Bad URL.");
                    }
                }
            } break;
            default: {
                if (isWhitespace(currentInputCodePoint)) {
                    while (isWhitespace(nextInputCodePoint)) {
                        consume();
                    }
                    if (nextInputCodePoint == RIGHT_PARENTHESIS || nextInputCodePoint == EOF) {
                        consume();
                        return urlToken;
                    } else {
                        consumeRemnantsOfBadURL();
                        return new BadURLToken();
                    }
                }
                if (isNonPrintableCodePoint(currentInputCodePoint)) {
                    if (isAllowParseErrors()) {
                        consumeRemnantsOfBadURL();
                        return new BadURLToken();
                    } else {
                        throw new ParseErrorException("Bad URL.");
                    }
                } else {
                    urlToken.append(Character.toChars(currentInputCodePoint));
                }
            } break;
            }
        } while (true);
    }
    
    /**
     * Consumes a unicode-range token. This returns a {@link UnicodeRangeToken}.
     * <p>
     * This assumes that the initial "u+" has been consumed, and the next code
     * point has been verified to be a {@link #isHexDigit(int) hex digit} or a {@link #QUESTION_MARK "?"}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#consume-a-unicode-range-token">4.3.6. Consume a unicode-range token</a>
     */
    protected UnicodeRangeToken consumeUnicodeRangeToken() throws IOException {
        assert currentInputCodePoint == PLUS_SIGN;
        assert isHexDigit(nextInputCodePoint) || nextInputCodePoint == QUESTION_MARK;
        
        final StringBuilder consumedStartBuf = new StringBuilder();
        int digitsConsumed = 0;
        while (isHexDigit(nextInputCodePoint) && digitsConsumed < 6) {
            consume();
            consumedStartBuf.append(Character.toChars(currentInputCodePoint));
            digitsConsumed++;
        }
        boolean containsQuestionMarks = false;
        while (nextInputCodePoint == QUESTION_MARK && digitsConsumed < 6) {
            consume();
            consumedStartBuf.append(Character.toChars(currentInputCodePoint));
            digitsConsumed++;
            containsQuestionMarks = true;
        }
        final String consumedValue = consumedStartBuf.toString();
        final int startRange;
        final int endRange;
        if (containsQuestionMarks) {
            final String lower = consumedValue.replace(QUESTION_MARK, DIGIT_ZERO);
            final String upper = consumedValue.replace(QUESTION_MARK, LATIN_CAPITAL_LETTER_F);
            startRange = Integer.parseInt(lower, 16);
            endRange = Integer.parseInt(upper, 16);
            final UnicodeRangeToken unicodeRangeToken = new UnicodeRangeToken();
            unicodeRangeToken.setStart(startRange);
            unicodeRangeToken.setEnd(endRange);
            return unicodeRangeToken;
        } else {
            startRange = Integer.parseInt(consumedValue, 16);
        }
        if (nextInputCodePoint == HYPHEN_MINUS && isHexDigit(lookahead[0])) {
            consume();
            final StringBuilder consumedEndBuffer = new StringBuilder();
            digitsConsumed = 0;
            while (isHexDigit(nextInputCodePoint) && digitsConsumed < 6) {
                consume();
                consumedEndBuffer.append(Character.toChars(currentInputCodePoint));
                digitsConsumed++;
            }
            endRange = Integer.parseInt(consumedEndBuffer.toString(), 16);
        } else {
            endRange = startRange;
        }
        
        final UnicodeRangeToken unicodeRangeToken = new UnicodeRangeToken();
        unicodeRangeToken.setStart(startRange);
        unicodeRangeToken.setEnd(endRange);
        return unicodeRangeToken;
    }
    
    /**
     * Consumes an escaped code point.
     * This will return a code point.
     * <p>
     * This assumes that the {@link #REVERSE_SOLIDUS U+005C REVERSE SOLIDUS (\)}
     * has already been consumed and that the {@link #getNextInputCodePoint() next input code point}
     * has already been verified to not be a {@link #isNewline(int) newline}.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#consume-an-escaped-code-point">4.3.7. Consume an escaped code point</a>
     */
    protected int consumeEscapedCodePoint() throws IOException {
        assert currentInputCodePoint == REVERSE_SOLIDUS;
        assert !isNewline(nextInputCodePoint);
        
        consume();
        switch (currentInputCodePoint) {
        case EOF: {
            return REPLACEMENT_CHARACTER;
        } // break;
        default: {
            if (isHexDigit(currentInputCodePoint)) {
                final StringBuilder buf = new StringBuilder();
                buf.append(Character.toChars(currentInputCodePoint));
                int digitsConsumed = 0;
                while (isHexDigit(nextInputCodePoint) && digitsConsumed < 5) {
                    consume();
                    buf.append(Character.toChars(currentInputCodePoint));
                    digitsConsumed++;
                }
                if (isWhitespace(nextInputCodePoint)) {
                    consume();
                }
                final int codePoint = Integer.parseInt(buf.toString(), 16);
                if (codePoint == 0 || isSurrogateCodePoint(codePoint) || codePoint > MAXIMUM_ALLOWED_CODE_POINT) {
                    return REPLACEMENT_CHARACTER;
                } else {
                    return codePoint;
                }
            } else {
                return currentInputCodePoint;
            }
        } // break;
        }
    }

    /**
     * Checks if two code points are a valid escape. This checks the input stream
     * itself. The two code points checked are the {@link #getCurrentInputCodePoint() current input code point}
     * and the {@link #getNextInputCodePoint() next input code point}, in that order.
     * <p>
     * This will not consume any additional code point.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#starts-with-a-valid-escape">4.3.8. Check if two code points are a valid escape</a>
     */
    protected boolean isValidEscape() {
        return isValidEscape(currentInputCodePoint, nextInputCodePoint);
    }

    /**
     * Checks if two code points are a valid escape.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#starts-with-a-valid-escape">4.3.8. Check if two code points are a valid escape</a>
     */
    protected boolean isValidEscape(final int firstCodePoint, final int secondCodePoint) {
        if (firstCodePoint != REVERSE_SOLIDUS) {
            return false;
        } else if (isNewline(secondCodePoint)) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Checks if three code points would start an identifier. This checks the input stream itself.
     * The three code points in question are the {@link #getCurrentInputCodePoint() current input code point}
     * and the {@link #getNextInputCodePoint() next two input code points}, in that order.
     * <p>
     * This will not consume any additional code points.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#would-start-an-identifier">4.3.9. Check if three code points would start an identifier</a>
     */
    protected boolean wouldStartIdentifier() {
        return wouldStartIdentifier(currentInputCodePoint, nextInputCodePoint, lookahead[0]);
    }
    
    /**
     * Checks if three code points would start an identifier.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#would-start-an-identifier">4.3.9. Check if three code points would start an identifier</a>
     */
    protected boolean wouldStartIdentifier(final int firstCodePoint, final int secondCodePoint, final int thirdCodePoint) {
        switch (firstCodePoint) {
        case HYPHEN_MINUS: {
            if (isNameStartCodePoint(secondCodePoint)) {
                return true;
            }
            if (isValidEscape(secondCodePoint, thirdCodePoint)) {
                return true;
            }
            return false;
        } // break;
        case REVERSE_SOLIDUS: {
            if (isValidEscape(firstCodePoint, secondCodePoint)) {
                return true;
            } else {
                return false;
            }
        } // break;
        default: {
            if (isNameStartCodePoint(firstCodePoint)) {
                return true;
            } else {
                return false;
            }
        } // break;
        }
    }
    
    /**
     * Checks if three code points would start a number.
     * This checks the input stream itself. The three code points in question are the
     * {@link #getCurrentInputCodePoint() current input code point} and the
     * {@link #getNextInputCodePoint() next two input code points}, in that order.
     * <p>
     * This will not consume any additional code points.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#starts-with-a-number">4.3.10. Check if three code points would start a number</a>
     */
    protected boolean wouldStartNumber() {
        return wouldStartNumber(currentInputCodePoint, nextInputCodePoint, lookahead[0]);
    }
    
    /**
     * Checks if three code points would start a number.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#starts-with-a-number">4.3.10. Check if three code points would start a number</a>
     */
    protected boolean wouldStartNumber(final int codePoint1, final int codePoint2, final int codePoint3) {
        switch (codePoint1) {
        case PLUS_SIGN: // fall through
        case HYPHEN_MINUS: {
            if (isDigit(codePoint2)) {
                return true;
            }
            if (codePoint2 == FULL_STOP && isDigit(codePoint3)) {
                return true;
            }
            return false;
        } // break;
        case FULL_STOP: {
            if (isDigit(codePoint2)) {
                return true;
            } else {
                return false;
            }
        } // break;
        default: {
            if (isDigit(codePoint1)) {
                return true;
            } else {
                return false;
            }
        } // break;
        }
    }
    
    /**
     * Consumes a name from the stream of code points.
     * This returns a string containing the largest name that can be formed from
     * adjacent code points in the stream, starting from the first.
     * <p>
     * This does not do the verification of the first few code points that are
     * necessary to ensure the returned code points would constitute an
     * {@link IdentToken}. If that is the intended use, ensure that the stream
     * {@link #wouldStartIdentifier() starts with an identifier} before calling
     * this.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#consume-a-name">4.3.11. Consume a name</a>
     */
    protected String consumeName() throws IOException {
        final StringBuilder buf = new StringBuilder();
        do {
            consume();
            if (isNameCodePoint(currentInputCodePoint)) {
                buf.append(Character.toChars(currentInputCodePoint));
            } else if (isValidEscape()) {
                final int escapedCodePoint = consumeEscapedCodePoint();
                buf.append(Character.toChars(escapedCodePoint));
            } else {
                /*
                 * TODO:
                 * This reconsume is not in the standard, but it appears necessary.
                 */
                reconsume();
                return buf.toString();
            }
        } while (true);
    }
    
    /**
     * Consumes a number from the stream of code points.
     * It returns a 3-tuple of a string representation, a numeric value, and a type flag which is either "integer" or "number".
     * <p>
     * This does not do the verification of the first few code points that are
     * necessary to ensure a number can be obtained from the stream. Ensure that
     * the stream {@link #wouldStartNumber() starts with a number} before
     * calling this.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#consume-a-number">4.3.12. Consume a number</a>
     */
    protected NumberToken consumeNumber() throws IOException {
        final StringBuilder rep = new StringBuilder();
        TypedNumericValueToken.NumericType type = TypedNumericValueToken.NumericType.INTEGER;
        if (nextInputCodePoint == PLUS_SIGN || nextInputCodePoint == HYPHEN_MINUS) {
            consume();
            rep.append(Character.toChars(currentInputCodePoint));
        }
        while (isDigit(nextInputCodePoint)) {
            consume();
            rep.append(Character.toChars(currentInputCodePoint));
        }
        if (nextInputCodePoint == FULL_STOP && isDigit(lookahead[0])) {
            consume();
            rep.append(Character.toChars(currentInputCodePoint));
            consume();
            rep.append(Character.toChars(currentInputCodePoint));
            type = TypedNumericValueToken.NumericType.NUMBER;
            while (isDigit(nextInputCodePoint)) {
                consume();
                rep.append(Character.toChars(currentInputCodePoint));
            }
        }
        final boolean nextInputCodePointIsE = nextInputCodePoint == LATIN_CAPITAL_LETTER_E || nextInputCodePoint == LATIN_SMALL_LETTER_E;
        if (nextInputCodePointIsE && isDigit(lookahead[0])) {
            consume();
            rep.append(Character.toChars(currentInputCodePoint));
            consume();
            rep.append(Character.toChars(currentInputCodePoint));
            type = TypedNumericValueToken.NumericType.NUMBER;
            while (isDigit(nextInputCodePoint)) {
                consume();
                rep.append(Character.toChars(currentInputCodePoint));
            }
        } else if (nextInputCodePointIsE && (lookahead[0] == HYPHEN_MINUS || lookahead[0] == PLUS_SIGN) && isDigit(lookahead[1])) {
            consume();
            rep.append(Character.toChars(currentInputCodePoint));
            consume();
            rep.append(Character.toChars(currentInputCodePoint));
            consume();
            rep.append(Character.toChars(currentInputCodePoint));
            type = TypedNumericValueToken.NumericType.NUMBER;
            while (isDigit(nextInputCodePoint)) {
                consume();
                rep.append(Character.toChars(currentInputCodePoint));
            }
        }
        
        final String repr = rep.toString();
        
        final Number value = convertStringToNumber(repr);
        
        final NumberToken numberToken = new NumberToken();
        numberToken.setStringValue(repr);
        numberToken.setNumericType(type);
        numberToken.setNumericValue(value);
        return numberToken;
    }
    
    /**
     * Converts a string to a number. This returns a number.
     * <p>
     * This does not do any verification to ensure that the string contains only
     * a number. Ensure that the string contains only a valid CSS number before calling this.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#convert-a-string-to-a-number">4.3.13. Convert a string to a number</a>
     */
    protected Number convertStringToNumber(final String string) {
        final CharacterIterator characterIterator = new StringCharacterIterator(string);
        char ch = characterIterator.first();
        final int s;
        if (ch == PLUS_SIGN) {
            s = 1;
            ch = characterIterator.next();
        } else if (ch == HYPHEN_MINUS) {
            s = -1;
            ch = characterIterator.next();
        } else {
            s = 1;
        }
        long i = 0;
        while (isDigit(ch)) {
            i = 10L * i + Character.digit(ch, 10);
            ch = characterIterator.next();
        }
        if (ch == FULL_STOP) {
            ch = characterIterator.next();
        }
        long f = 0;
        int d = 0;
        while (isDigit(ch)) {
            f = 10L * f + Character.digit(ch, 10);
            d++;
            ch = characterIterator.next();
        }
        if (ch == LATIN_CAPITAL_LETTER_E || ch == LATIN_SMALL_LETTER_E) {
            ch = characterIterator.next();
        }
        final int t;
        if (ch == PLUS_SIGN) {
            t = 1;
            ch = characterIterator.next();
        } else if (ch == HYPHEN_MINUS) {
            t = -1;
            ch = characterIterator.next();
        } else {
            t = 1;
        }
        long e = 0;
        while (isDigit(ch)) {
            e = 10 * e + Character.digit(ch, 10);
            ch = characterIterator.next();
        }
        
        return Double.valueOf(s * (i + f * Math.pow(10, -d)) * Math.pow(10, t * e));
    }

    /**
     * Consumes the remnants of a bad url from the stream of code points,
     * "cleaning up" after the tokenizer realizes that
     * it’s in the middle of a {@link BadURLToken} rather than a {@link URLToken}.
     * This returns nothing; its sole use is to consume enough of the input stream
     * to reach a recovery point where normal tokenizing can resume.
     * 
     * @see <a href="https://www.w3.org/TR/css-syntax-3/#consume-the-remnants-of-a-bad-url">4.3.14. Consume the remnants of a bad url</a>
     */
    protected void consumeRemnantsOfBadURL() throws IOException {
        do {
            consume();
            switch (currentInputCodePoint) {
            case RIGHT_PARENTHESIS: // fall through
            case EOF: {
                return;
            } // break;
            default: {
                if (isValidEscape()) {
                    consumeEscapedCodePoint();
                }
                /*
                 * Do nothing.
                 */
            } break;
            }
        } while (true);
    }
    
    public static void main(final String[] args) throws IOException {
        final URL url;
        url = new URL("http://rgsb.org/rgsb.css");
        final Download download = new Download(url);
        download.connect();
        download.download();
        System.out.println(download.getContent());
        System.out.println();
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");
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
