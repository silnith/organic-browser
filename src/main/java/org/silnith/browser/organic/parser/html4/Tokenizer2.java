package org.silnith.browser.organic.parser.html4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.silnith.browser.organic.parser.html4.token.Comment;
import org.silnith.browser.organic.parser.html4.token.EndTag;
import org.silnith.browser.organic.parser.html4.token.EntityReference;
import org.silnith.browser.organic.parser.html4.token.HexadecimalEntityReference;
import org.silnith.browser.organic.parser.html4.token.NamedEntityReference;
import org.silnith.browser.organic.parser.html4.token.NumericEntityReference;
import org.silnith.browser.organic.parser.html4.token.ProcessingInstruction;
import org.silnith.browser.organic.parser.html4.token.StartTag;
import org.silnith.browser.organic.parser.html4.token.Token;

public class Tokenizer2 {

	private final BufferedReader reader;

	public Tokenizer2(final Reader reader) {
		super();
		this.reader = new BufferedReader(reader);
	}

	public void dispose() throws IOException {
		reader.close();
	}

	private boolean isWhitespace(final int character) {
		return (character == ' ' || character == '\r' || character == '\n' || character == '\t');
	}

	private boolean isDecimal(final int character) {
		return (character >= '0' && character <= '9');
	}

	private boolean isHexadecimal(final int character) {
		return isDecimal(character) || (character >= 'a' && character <= 'f') || (character >= 'A' && character <= 'F');
	}

	private boolean isNameStart(final int character) {
		return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
	}

	private boolean isName(final int character) {
		return isNameStart(character) || isDecimal(character)
				|| character == '.' || character == '_' || character == '-' || character == ':';
	}

	public void eatWhitespace() throws IOException {
		reader.mark(1);
		int character = reader.read();
		while (isWhitespace(character)) {
			reader.mark(1);
			character = reader.read();
		}
		reader.reset();
	}

	public String readContent() throws IOException {
		return readUntilWithEntityReferences('<');
	}

	public Token.Type sniffNextToken() throws IOException {
		reader.mark(16);
		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			int character = reader.read();
			if (character == -1) {
				break;
			} else {
				stringBuilder.append((char) character);
			}
		}
		reader.reset();
		
		final String prefix = stringBuilder.toString();
		if (prefix.startsWith("<!--")) {
			// comment
			return Token.Type.COMMENT;
		} else if (prefix.startsWith("<![")) {
			// cdata?
			return Token.Type.DATA_BLOCK;
		} else if (prefix.startsWith("<!")) {
			// doctype?
			return Token.Type.DOCTYPE;
		} else if (prefix.startsWith("<?")) {
			// processing instruction
			return Token.Type.PROCESSING_INSTRUCTION;
		} else if (prefix.startsWith("</")) {
			// end tag
			return Token.Type.END_TAG;
		} else if (prefix.startsWith("<")) {
			// tag
			return Token.Type.START_TAG;
		}
		return null;
	}

	public StartTag readStartTag() throws IOException {
		readFixedString("<");
		
		final String name = readName();
		
		final Map<String, String> attributes = readAttributes();
		
		eatWhitespace();
		
		readFixedString(">");
		
		return new StartTag(name, attributes);
	}

	public EndTag readEndTag() throws IOException {
		readFixedString("</");
		
		final String name = readName();
		
		readFixedString(">");
		
		return new EndTag(name);
	}

	public Comment readComment() throws IOException {
		readFixedString("<!--");
		
		final String comment = readUntil("--");
		
		readFixedString("--");
		eatWhitespace();
		readFixedString(">");
		
		return new Comment(comment);
	}

	public ProcessingInstruction readProcessingInstruction() throws IOException {
		readFixedString("<?");
		
		final String processingInstruction = readUntil(">");

		readFixedString(">");
		
		return new ProcessingInstruction(processingInstruction);
	}

	protected String readDecimal() throws IOException {
		final StringBuilder value = new StringBuilder();
		reader.mark(1);
		int character = reader.read();
		while (isDecimal(character)) {
			value.append((char) character);
			reader.mark(1);
			character = reader.read();
		}
		reader.reset();
		
		return value.toString();
	}

	protected String readHexadecimal() throws IOException {
		final StringBuilder value = new StringBuilder();
		reader.mark(1);
		int character = reader.read();
		while (isHexadecimal(character)) {
			value.append((char) character);
			reader.mark(1);
			character = reader.read();
		}
		reader.reset();
		
		return value.toString();
	}

	protected void readFixedString(final String expectedString) throws IOException {
		final StringBuilder prefix = new StringBuilder();
		for (int i = 0; i < expectedString.length(); i++) {
			int character = reader.read();
			if (character == -1) {
				throw new RuntimeException("Expected \"" + expectedString + "\", found \"" + prefix + "\"");
			} else {
				prefix.append((char) character);
			}
		}
		if ( !prefix.toString().equals(expectedString)) {
			throw new RuntimeException("Expected \"" + expectedString + "\", found \"" + prefix + "\"");
		}
	}

	protected String readName() throws IOException {
		final StringBuilder name = new StringBuilder();
		final char[] buf = new char[1];
		int numRead = reader.read(buf);
		if (numRead == -1) {
			throw new RuntimeException();
		}
		assert numRead == 1;
		if ( !isNameStart(buf[0])) {
			throw new RuntimeException();
		}
		name.append(buf[0]);
		
		reader.mark(1);
		numRead = reader.read(buf);
		while (numRead != -1 && isName(buf[0])) {
			name.append(buf[0]);
			reader.mark(1);
			numRead = reader.read(buf);
		}
		reader.reset();
		
		return name.toString();
	}

	protected String readUntil(final String terminator) throws IOException {
		final StringBuilder content = new StringBuilder();
		final int bufferSize = 256;
		final char[] buf = new char[bufferSize];
		int endLocation;
		int totalSize = 0;
		do {
			reader.mark(bufferSize);
			int numRead  = reader.read(buf);
			if (numRead == -1) {
				throw new RuntimeException("Unexpected end of file looking for terminator: \"" + terminator + "\"");
			}
			for (int i = 0; i < numRead; i++) {
				content.append(buf[i]);
			}
			endLocation = content.indexOf(terminator);
			if (endLocation == -1) {
				totalSize += numRead;
			} else {
				totalSize = endLocation - totalSize;
			}
		} while (endLocation == -1);
		reader.reset();
		reader.read(buf, 0, totalSize);
		content.setLength(endLocation);
		return content.toString();
	}

	protected Map<String, String> readAttributes() throws IOException {
		final Map<String, String> attributes = new HashMap<>();
		
		eatWhitespace();
		
		reader.mark(1);
		int character = reader.read();
		reader.reset();
		
		while (isNameStart(character)) {
			final String name = readName();
			
			eatWhitespace();
			
			reader.mark(1);
			final int possibleSeparator = reader.read();
			reader.reset();
			
			if (possibleSeparator == '=') {
				readFixedString("=");
				
				eatWhitespace();
				
				final String value = readAttributeValue();
				
				attributes.put(name, value);
			} else {
				attributes.put(name, null);
			}
			
			eatWhitespace();
			
			reader.mark(1);
			character = reader.read();
			reader.reset();
		}
		
		return attributes;
	}

	protected String readAttributeValue() throws IOException {
		eatWhitespace();
		
		reader.mark(1);
		final int firstCharacter = reader.read();
		reader.reset();
		
		if (firstCharacter == '"') {
			readFixedString("\"");
			
			final String value = readUntilWithEntityReferences('"');
			
			readFixedString("\"");
			
			return value;
		} else if (firstCharacter == '\'') {
			readFixedString("'");
			
			final String value = readUntilWithEntityReferences('\'');
			
			readFixedString("'");
			
			return value;
		} else if (isNameStart(firstCharacter)) {
			return readName();
		} else {
			throw new RuntimeException("Expected attribute value.  Found: '" + (char) firstCharacter + "'");
		}
	}

	protected String readUntilWithEntityReferences(final char terminator) throws IOException {
		final StringBuilder value = new StringBuilder();
		final char[] buf = new char[1];
		do {
			reader.mark(1);
			int numRead  = reader.read(buf);
			if (numRead == -1) {
				throw new RuntimeException("Unexpected end of file looking for terminator: \"" + terminator + "\"");
			}
			if (buf[0] == '&') {
				reader.reset();
				
				final String entityValue = readEntityReference();
				
				value.append(entityValue);
			}
			if (buf[0] != terminator) {
				value.append(buf[0]);
			}
			
		} while (buf[0] != terminator);
		reader.reset();
		
		return value.toString();
	}

	protected String readEntityReference() throws IOException {
		readFixedString("&");
		
		reader.mark(1);
		int character = reader.read();
		reader.reset();
		
		final EntityReference entityReference;
		if (isNameStart(character)) {
			final String name = readName();
			
			entityReference = new NamedEntityReference(name);
		} else if (character == '#') {
			reader.read();
			
			reader.mark(1);
			character = reader.read();
			reader.reset();
			
			if (character == 'x' || character == 'X') {
				reader.read();
				
				final String hexadecimal = readHexadecimal();
				
				entityReference = new HexadecimalEntityReference(hexadecimal);
			} else {
				final String decimal = readDecimal();
				
				entityReference = new NumericEntityReference(decimal);
			}
		} else {
			throw new RuntimeException("Unexpected character in entity reference: '" + character + "'");
		}
		
		// check for semicolon
		reader.mark(1);
		character = reader.read();
		if (character != ';') {
			reader.reset();
		}
		
		return entityReference.getEntity();
	}

}
