package org.silnith.browser.organic.parser.util;

/**
 * Why did I create these obvious constants?  Surely the code is just as
 * readable with the actual character literals used instead of these!
 * 
 * <p>Yes, it is, and the initial implementation used the character literals.
 * However, I want the implementation to look as much like the "spec" as
 * possible, and the spec uses these constants.  Also, it is easy to confuse
 * some such as (') and (`) when reading the code, and not everybody knows
 * the difference between U+000A and U+000C and U+000D off the top of their
 * head.  Being pedantic makes it easier to catch mistakes.
 * 
 * <p>Am I really this pedantic?
 * 
 * <p>Yes.  Yes I am.
 * 
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class UnicodeCodePoints {

	/**
	 * U+0000 NULL
	 */
	public static final char NULL = '\0';

	/**
	 * U+0008 BACKSPACE
	 */
	public static final char BACKSPACE = '\b';

	/**
	 * U+0009 CHARACTER TABULATION (tab)
	 */
	public static final char CHARACTER_TABULATION = '\t';

	/**
	 * U+000A LINE FEED (LF)
	 */
	public static final char LINE_FEED = '\n';

	/**
	 * U+000B LINE TABULATION
	 */
	public static final char LINE_TABULATION = '\u000B';

	/**
	 * U+000C FORM FEED (FF)
	 */
	public static final char FORM_FEED = '\f';

	/**
	 * U+000D CARRIAGE RETURN (CR)
	 */
	public static final char CARRIAGE_RETURN = '\r';

	/**
	 * U+000E SHIFT OUT
	 */
	public static final char SHIFT_OUT = '\u000E';

	/**
	 * U+001F INFORMATION SEPARATOR ONE
	 */
	public static final char INFORMATION_SEPARATOR_ONE = '\u001F';

	/**
	 * U+0020 SPACE
	 */
	public static final char SPACE = ' ';

	/**
	 * U+0021 EXCLAMATION MARK (!)
	 */
	public static final char EXCLAMATION_MARK = '!';

	/**
	 * U+0022 QUOTATION MARK (")
	 */
	public static final char QUOTATION_MARK = '"';

	/**
	 * U+0023 NUMBER SIGN (#)
	 */
	public static final char NUMBER_SIGN = '#';

	/**
	 * U+0024 DOLLAR SIGN ($)
	 */
	public static final char DOLLAR_SIGN = '$';

	/**
	 * U+0025 PERCENTAGE SIGN (%)
	 */
	public static final char PERCENTAGE_SIGN = '%';

	/**
	 * U+0026 AMPERSAND (&)
	 */
	public static final char AMPERSAND = '&';

	/**
	 * U+0027 APOSTROPHE (')
	 */
	public static final char APOSTROPHE = '\'';

	/**
	 * U+0028 LEFT PARENTHESIS (()
	 */
	public static final char LEFT_PARENTHESIS = '(';

	/**
	 * U+0029 RIGHT PARENTHESIS ())
	 */
	public static final char RIGHT_PARENTHESIS = ')';

	/**
	 * U+002A ASTERISK (*)
	 */
	public static final char ASTERISK = '*';

	/**
	 * U+002B PLUS SIGN (+)
	 */
	public static final char PLUS_SIGN = '+';

	/**
	 * U+002C COMMA (,)
	 */
	public static final char COMMA = ',';

	/**
	 * U+002D HYPHEN-MINUS (-)
	 */
	public static final char HYPHEN_MINUS = '-';

	/**
	 * U+002E FULL STOP (.)
	 */
	public static final char FULL_STOP = '.';

	/**
	 * U+002F SOLIDUS (/)
	 */
	public static final char SOLIDUS = '/';

	/**
	 * U+0030 DIGIT ZERO (0)
	 */
	public static final char DIGIT_ZERO = '0';

	/**
	 * U+0039 DIGIT NINE (9)
	 */
	public static final char DIGIT_NINE = '9';

	/**
	 * U+003A COLON (:)
	 */
	public static final char COLON = ':';

	/**
	 * U+003B SEMICOLON (;)
	 */
	public static final char SEMICOLON = ';';

	/**
	 * U+003C LESS-THAN SIGN (<)
	 */
	public static final char LESS_THAN_SIGN = '<';

	/**
	 * U+003D EQUALS SIGN (=)
	 */
	public static final char EQUALS_SIGN = '=';

	/**
	 * U+003E GREATER-THAN SIGN (>)
	 */
	public static final char GREATER_THAN_SIGN = '>';

	/**
	 * U+003F QUESTION MARK (?)
	 */
	public static final char QUESTION_MARK = '?';

	/**
	 * U+0040 COMMERCIAL AT (@)
	 */
	public static final char COMMERCIAL_AT = '@';

	/**
	 * U+0041 LATIN CAPITAL LETTER A (A)
	 */
	public static final char LATIN_CAPITAL_LETTER_A = 'A';

	/**
	 * U+0045 LATIN CAPITAL LETTER E (E)
	 */
	public static final char LATIN_CAPITAL_LETTER_E = 'E';

	/**
	 * U+0046 LATIN CAPITAL LETTER F (F)
	 */
	public static final char LATIN_CAPITAL_LETTER_F = 'F';

	/**
	 * U+0055 LATIN CAPITAL LETTER U (U)
	 */
	public static final char LATIN_CAPITAL_LETTER_U = 'U';

	/**
	 * U+0058 LATIN CAPITAL LETTER X (X)
	 */
	public static final char LATIN_CAPITAL_LETTER_X = 'X';

	/**
	 * U+005A LATIN CAPITAL LETTER Z (Z)
	 */
	public static final char LATIN_CAPITAL_LETTER_Z = 'Z';

	/**
	 * U+005B LEFT SQUARE BRACKET ([)
	 */
	public static final char LEFT_SQUARE_BRACKET = '[';

	/**
	 * U+005C REVERSE SOLIDUS (\)
	 */
	public static final char REVERSE_SOLIDUS = '\\';

	/**
	 * U+005D RIGHT SQUARE BRACKET (])
	 */
	public static final char RIGHT_SQUARE_BRACKET = ']';

	/**
	 * U+005E CIRCUMFLEX ACCENT (^)
	 */
	public static final char CIRCUMFLEX_ACCENT = '^';

	/**
	 * U+005F LOW LINE (_)
	 */
	public static final char LOW_LINE = '_';

	/**
	 * U+0060 GRAVE ACCENT (`)
	 */
	public static final char GRAVE_ACCENT = '`';

	/**
	 * U+0061 LATIN SMALL LETTER A (a)
	 */
	public static final char LATIN_SMALL_LETTER_A = 'a';

	/**
	 * U+0065 LATIN SMALL LETTER E (e)
	 */
	public static final char LATIN_SMALL_LETTER_E = 'e';

	/**
	 * U+0066 LATIN SMALL LETTER F (f)
	 */
	public static final char LATIN_SMALL_LETTER_F = 'f';

	/**
	 * U+0075 LATIN SMALL LETTER U (u)
	 */
	public static final char LATIN_SMALL_LETTER_U = 'u';

	/**
	 * U+0078 LATIN SMALL LETTER X (x)
	 */
	public static final char LATIN_SMALL_LETTER_X = 'x';

	/**
	 * U+007A LATIN SMALL LETTER Z (z)
	 */
	public static final char LATIN_SMALL_LETTER_Z = 'z';

	/**
	 * U+007B LEFT CURLY BRACKET ({)
	 */
	public static final char LEFT_CURLY_BRACKET = '{';

	/**
	 * U+007C VERTICAL LINE (|)
	 */
	public static final char VERTICAL_LINE = '|';

	/**
	 * U+007D RIGHT CURLY BRACKET (})
	 */
	public static final char RIGHT_CURLY_BRACKET = '}';

	/**
	 * U+007E TILDE (~)
	 */
	public static final char TILDE = '~';

	/**
	 * U+007F DELETE
	 */
	public static final char DELETE = '\u007F';

	/**
	 * U+0080 &lt;control&gt;
	 */
	public static final char CONTROL = '\u0080';

	/**
	 * U+FFFD REPLACEMENT CHARACTER
	 */
	public static final char REPLACEMENT_CHARACTER = '\uFFFD';

	private UnicodeCodePoints() {
	}

}
