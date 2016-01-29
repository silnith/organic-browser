package org.silnith.browser.organic.parser.html4.lexical;

import java.io.IOException;
import java.io.PushbackReader;

/**
 * Lexer for comments
 * 
 * <dl>
 *   <dt>initial state
 *   <dd>
 *     <dl>
 *       <dt>-<dd>(DASH)
 *       <dt>[ \n\r\t]<dd>[{@link Token.Type#WHITESPACE}]
 *       <dt>[^- \n\r\t]<dd>[{@link Token.Type#TEXT}]
 *     </dl>
 *   <dt>(DASH)
 *   <dd>
 *     <dl>
 *       <dt>-<dd>[{@link Token.Type#COMMENT_DELIMITER}]
 *       <dt>[^-]<dd>[{@link Token.Type#TEXT}]
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
 * 
 * @author kent
 */
public class CommentTokenizer {

	private enum State {
		DASH,
		TEXT,
		TEXT_ENDING_WITH_DASH,
		WHITESPACE
	}

	public CommentTokenizer() {
	}

	public Token getNextToken(final PushbackReader reader) throws IOException {
		final char[] buf = new char[1];
		final StringBuilder content = new StringBuilder();
		int numRead;
		State state;
		
		numRead = reader.read(buf);

		if (numRead == -1) {
			return new Token(Token.Type.EOF, "");
		}
		
		switch (buf[0]) {
		case '-': {
			state = State.DASH;
			content.append(buf[0]);
		} break;
		case ' ': // fall through
		case '\n': // fall through
		case '\r': // fall through
		case '\t': {
			state = State.WHITESPACE;
			content.append(buf[0]);
		} break;
		default: {
			state = State.TEXT;
			content.append(buf[0]);
		} break;
		}

		numRead = reader.read(buf);
		while (numRead == 1) {
			switch (state) {
			case DASH: {
				assert content.length() == 1;
				
				switch (buf[0]) {
				case '-': {
					content.append(buf[0]);
					return new Token(Token.Type.COMMENT_DELIMITER, content.toString());
				} // break;
				case ' ': // fall through
				case '\n': // fall through
				case '\r': // fall through
				case '\t': {
					reader.unread(buf[0]);
					return new Token(Token.Type.TEXT, content.toString());
				} // break;
				default: {
					state = State.TEXT;
					content.append(buf[0]);
				} break;
				}
			} break;
			case TEXT: {
				assert content.length() > 0;
				
				switch (buf[0]) {
				case '-': {
					state = State.TEXT_ENDING_WITH_DASH;
					content.append(buf[0]);
				} break;
				case ' ': // fall through
				case '\n': // fall through
				case '\r': // fall through
				case '\t': {
					reader.unread(buf[0]);
					return new Token(Token.Type.TEXT, content.toString());
				} // break;
				default: {
					content.append(buf[0]);
				} break;
				}
			} break;
			case TEXT_ENDING_WITH_DASH: {
				assert content.length() > 0;
				assert content.lastIndexOf("-") == content.length() - 1;
				
				switch (buf[0]) {
				case '-': {
					reader.unread('-');
					reader.unread('-');
					return new Token(Token.Type.TEXT, content.substring(0, content.length() - 1));
				} // break;
				case ' ': // fall through
				case '\n': // fall through
				case '\r': // fall through
				case '\t': {
					reader.unread(buf[0]);
					return new Token(Token.Type.TEXT, content.toString());
				} // break;
				default: {
					state = State.TEXT;
					content.append(buf[0]);
				} break;
				}
			} break;
			case WHITESPACE: {
				assert content.length() > 0;
				
				switch (buf[0]) {
				case '-': {
					reader.unread(buf[0]);
					return new Token(Token.Type.WHITESPACE, content.toString());
				} // break;
				case ' ': // fall through
				case '\n': // fall through
				case '\r': // fall through
				case '\t': {
					content.append(buf[0]);
				} break;
				default: {
					reader.unread(buf[0]);
					return new Token(Token.Type.WHITESPACE, content.toString());
				} // break;
				}
			} break;
			default: throw new IllegalStateException("Unknown state: " + state);
			}
			numRead = reader.read(buf);
		}
		
		switch (state) {
		case DASH: return new Token(Token.Type.TEXT, content.toString());
		case TEXT: return new Token(Token.Type.TEXT, content.toString());
		case TEXT_ENDING_WITH_DASH: return new Token(Token.Type.TEXT, content.toString());
		case WHITESPACE: return new Token(Token.Type.WHITESPACE, content.toString());
		default: throw new IllegalStateException("Unknown state: " + state);
		}
	}

}
