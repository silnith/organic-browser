package org.silnith.browser.organic.parser.html4.lexical;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

import org.silnith.browser.organic.parser.html4.lexical.Token;

/**
 * Lexer for PCDATA
 * 
 * <dl>
 *   <dt>initial state
 *   <dd>
 *     <dl>
 *       <dt>a-zA-Z<dd>[{@link Token.Type#NAME}]
 *       <dt>0-9_.-:<dd>[{@link Token.Type#WORD}]
 *       <dt>-<dd>[{@link Token.Type#WORD}-1]
 *       <dt>&lt;<dd>[{@link Token.Type#TAG_OPEN}]
 *       <dt>&gt;<dd>[{@link Token.Type#TAG_CLOSE}]
 *       <dt>/<dd>[{@link Token.Type#SLASH}]
 *       <dt>[<dd>[{@link Token.Type#LEFT_BRACKET}]
 *       <dt>]<dd>[{@link Token.Type#RIGHT_BRACKET}]
 *       <dt>'<dd>[{@link Token.Type#SINGLE_QUOTE}]
 *       <dt>"<dd>[{@link Token.Type#DOUBLE_QUOTE}]
 *       <dt>&amp;<dd>[{@link Token.Type#AMPERSAND}]
 *       <dt>;<dd>[{@link Token.Type#SEMICOLON}]
 *       <dt>\n<dd>[{@link Token.Type#NEWLINE}]
 *       <dt> \r\t<dd>[{@link Token.Type#WHITESPACE}]
 *       <dt>[^a-zA-Z0-9_.-:&lt;&gt;/[]'"&amp;;\n \r\t]<dd>[{@link Token.Type#TEXT}]
 *     </dl>
 *   <dt>[{@link Token.Type#NAME}]
 *   <dd>
 *     <dl>
 *       <dt>a-zA-Z0-9_.-:<dd>[{@link Token.Type#NAME}]
 *     </dl>
 *   <dt>[{@link Token.Type#WORD}]
 *   <dd>
 *     <dl>
 *       <dt>a-zA-Z0-9_.-:<dd>[{@link Token.Type#WORD}]
 *     </dl>
 *   <dt>[{@link Token.Type#WORD}-1]
 *   <dd>
 *     <dl>
 *       <dt>-<dd>(COMMENT2)
 *       <dt>a-zA-Z0-9_.:<dd>[{@link Token.Type#WORD}]
 *     </dl>
 *   <dt>[{@link Token.Type#TAG_OPEN}]
 *   <dd>
 *     <dl>
 *       <dt>!<dd>[{@link Token.Type#MARKUP_DECLARATION}]
 *       <dt>?<dd>[{@link Token.Type#PROCESSING_INSTRUCTION}]
 *       <dt>/<dd>[{@link Token.Type#ENDING_TAG}]
 *       <dt>-<dd>(COMMENT1)
 *     </dl>
 *   <dt>[{@link Token.Type#MARKUP_DECLARATION}]
 *   <dd>
 *   <dt>[{@link Token.Type#PROCESSING_INSTRUCTION}]
 *   <dd>
 *   <dt>[{@link Token.Type#ENDING_TAG}]
 *   <dd>
 *   <dt>(COMMENT1)
 *   <dd>
 *     <dl>
 *       <dt>-<dd>[{@link Token.Type#COMMENT_DELIMITER}]
 *     </dl>
 *   <dt>[{@link Token.Type#TAG_CLOSE}]
 *   <dd>
 *   <dt>[{@link Token.Type#COMMENT_DELIMITER}]
 *   <dd>
 *   <dt>[{@link Token.Type#SLASH}]
 *   <dd>
 *   <dt>[{@link Token.Type#LEFT_BRACKET}]
 *   <dd>
 *   <dt>[{@link Token.Type#RIGHT_BRACKET}]
 *   <dd>
 *     <dl>
 *       <dt>]<dd>(BLOCK1)
 *     </dl>
 *   <dt>(BLOCK1)
 *   <dd>
 *     <dl>
 *       <dt>&gt;<dd>[{@link Token.Type#END_BLOCK}]
 *     </dl>
 *   <dt>[{@link Token.Type#END_BLOCK}]
 *   <dd>
 *   <dt>[{@link Token.Type#SINGLE_QUOTE}]
 *   <dd>
 *   <dt>[{@link Token.Type#DOUBLE_QUOTE}]
 *   <dd>
 *   <dt>[{@link Token.Type#AMPERSAND}]
 *   <dd>
 *   <dt>[{@link Token.Type#SEMICOLON}]
 *   <dd>
 *   <dt>[{@link Token.Type#NEWLINE}]
 *   <dd>
 *   <dt>[{@link Token.Type#WHITESPACE}]
 *   <dd>
 *     <dl>
 *       <dt>\r \t<dd>[{@link Token.Type#WHITESPACE}]
 *     </dl>
 *   <dt>[{@link Token.Type#TEXT}]
 *   <dd>
 *     <dl>
 *       <dt>[^a-zA-Z0-9_.-:&lt;&gt;/[]'"&amp;;\n\r \t]<dd>[{@link Token.Type#TEXT}]
 *     </dl>
 * </dl>
 * 
 * Lexer for comments
 * 
 * <dl>
 *   <dt>initial state
 *   <dd>
 *     <dl>
 *       <dt>-<dd>[{@link Token.Type#TEXT}-ENDS-WITH-DASH]
 *       <dt>[ \n\r\t]<dd>[{@link Token.Type#WHITESPACE}]
 *       <dt>[^- \n\r\t]<dd>[{@link Token.Type#TEXT}]
 *     </dl>
 *   <dt>[{@link Token.Type#TEXT}-ENDS-WITH-DASH]
 *   <dd>
 *     <dl>
 *       <dt>-<dd>[{@link Token.Type#COMMENT_DELIMITER}]
 *       <dt>[^-]<dd>[{@link Token.Type#TEXT}]
 *     </dl>
 *   <dt>[{@link Token.Type#COMMENT_DELIMITER}]
 *   <dd>ACCEPT
 *   <dt>[{@link Token.Type#WHITESPACE}]
 *   <dd>
 *     <dl>
 *       <dt> \n\r\t<dd>[{@link Token.Type#WHITESPACE}]
 *     </dl>
 *   <dt>[{@link Token.Type#TEXT}]
 *   <dd>
 *     <dl>
 *       <dt>-<dd>[{@link Token.Type#TEXT}-ENDS-WITH-DASH]
 *       <dt>[^-]<dd>[{@link Token.Type#TEXT}]
 *     </dl>
 * </dl>
 * @author kent
 */
public class Tokenizer {

	private enum State {
		INITIAL_STATE,
		NAME,
		WORD,
		COMMENT
	}

	private final PushbackReader reader;

	private State state;

	public Tokenizer(final Reader reader) {
		super();
		this.reader = new PushbackReader(reader, 16);
		this.state = State.INITIAL_STATE;
	}

	public Token getNextToken() throws IOException {
		final StringBuilder content = new StringBuilder();
		do {
			final char[] buf = new char[1];
			final int numRead = reader.read(buf);
			
			if (numRead == -1) {
				return new Token(Token.Type.EOF, "");
			}
			
			switch (buf[0]) {
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
			{} break;
			default: {} break;
			}
			
		} while (true);
	}

}
