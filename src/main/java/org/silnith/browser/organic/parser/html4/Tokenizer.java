package org.silnith.browser.organic.parser.html4;

import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.silnith.browser.organic.parser.html4.token.Comment;
import org.silnith.browser.organic.parser.html4.token.DataBlock;
import org.silnith.browser.organic.parser.html4.token.Doctype;
import org.silnith.browser.organic.parser.html4.token.EmptyEndTag;
import org.silnith.browser.organic.parser.html4.token.EmptyStartTag;
import org.silnith.browser.organic.parser.html4.token.EndTag;
import org.silnith.browser.organic.parser.html4.token.EntityReference;
import org.silnith.browser.organic.parser.html4.token.HexadecimalEntityReference;
import org.silnith.browser.organic.parser.html4.token.NamedEntityReference;
import org.silnith.browser.organic.parser.html4.token.NumericEntityReference;
import org.silnith.browser.organic.parser.html4.token.ProcessingInstruction;
import org.silnith.browser.organic.parser.html4.token.StartTag;
import org.silnith.browser.organic.parser.html4.token.Token;

public class Tokenizer {

	private final CharacterIterator characterIterator;

	private char character;

	public Tokenizer(final CharacterIterator characterIterator) {
		super();
		this.characterIterator = characterIterator;
		this.character = this.characterIterator.first();
	}

	public boolean isWhitespace(final char ch) {
		return (ch == ' ' || ch == '\r' || ch == '\n' || ch == '\t');
	}

	public void eatWhitespace() {
		while (isWhitespace(character)) {
			character = characterIterator.next();
		}
	}

	public Token getNextToken(final boolean skipWhitespace) {
		eatWhitespace();
		
		if (character == '<') {
			character = characterIterator.next();
			if (character == '?') {
				character = characterIterator.next();
				return readProcessingInstruction();
			} else if (character == '!') {
				// could be doctype
				// could be CDATA
				// could be include
				// could be exclude
				// could be comment
				character = characterIterator.next();
				if (character == '[') {
					// could be CDATA, INCLUDE, or EXCLUDE
					character = characterIterator.next();
					final StringBuilder dataType = new StringBuilder();
					while (character != '[') {
						if (character == CharacterIterator.DONE) {
							throw new RuntimeException("Missing second '['.");
						}
						dataType.append(character);
						character = characterIterator.next();
					}
					character = characterIterator.next();
					// read until the closing "]]>"
					final StringBuilder dataContent = new StringBuilder();
					while (character != CharacterIterator.DONE) {
						if (character == ']') {
							final char nextCharacter = characterIterator.next();
							if (nextCharacter == ']') {
								// now read the '>'
								final char thirdCharacter = characterIterator.next();
								if (thirdCharacter == '>') {
									// woot, this is the end!
									character = characterIterator.next();
									return new DataBlock(dataType.toString(), dataContent.toString());
								} else {
									// just had "]]", not and end token
									// keep going
									dataContent.append(character);
									dataContent.append(nextCharacter);
									character = thirdCharacter;
								}
							} else {
								dataContent.append(character);
								character = nextCharacter;
							}
						} else if (character == '&') {
							character = characterIterator.next();
							final EntityReference entityReference = readEntityReference();
							dataContent.append(entityReference.getEntity());
						} else {
							dataContent.append(character);
							character = characterIterator.next();
						}
					}
					throw new RuntimeException("Missing second '['.");
				} else if (character == '-') {
					// comment
					character = characterIterator.next();
					if (character == '-') {
						// comment
						// read until next "--", then read whitespace, then read '>'
						character = characterIterator.next();
						return readComment();
					} else {
						// parse error?
						// What is "<!-"?
						throw new RuntimeException("Unrecognized markup: \"<!-\"");
					}
				} else if (character >= 'a' && character <= 'z'
						|| character >= 'A' && character <= 'Z') {
					// probably a doctype
					return readDoctype();
				} else {
					// no idea, fail
					throw new RuntimeException("Unrecognized content : \"<!" + character + "\"");
				}
			} else if (character == '>') {
				// this is an SGML shorttag
				// the tag name is implied by the document structure
				return new EmptyStartTag();
			} else if (character == '/') {
				// this is an element closing tag
				character = characterIterator.next();
				// if the next character is '>', it is an SGML shorttag
				if (character == '>') {
					return new EmptyEndTag();
				} else {
					// read the closed tag name
					final String name = readName();
					if (character == '>') {
						character = characterIterator.next();
						return new EndTag(name);
					} else if (character == '<') {
						// unclosed end tag
						return new EndTag(name);
					}
				}
			} else if (character == '&') {
				character = characterIterator.next();
				// character reference
				// read the character reference
				// the ending ';' may be omitted if the next token is a newline
				// or the start of a tag
				return readEntityReference();
			} else if (character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z') {
				// ID or NAME
				// read all [a-zA-Z]|[0-9]|'-'|'_'|':'|'.'
				final String name = readName();
				
				final Map<String, String> attributes = new HashMap<>();
				while (character != CharacterIterator.DONE) {
					// read whitespace-separated attributes?
					eatWhitespace();
					
					if (character == '>') {
						// simple tag
						// read closing '>'
						character = characterIterator.next();
						// line breaks after start tags must be ignored
						if (character == '\n') {
							character = characterIterator.next();
						}
						return new StartTag(name, attributes);
					} else if (character == '/') {
						// minimized tag
						// line breaks after start tags must be ignored
						if (character == '\n') {
							character = characterIterator.next();
						}
						return new StartTag(name, true, attributes);
					} else if (character == '<') {
						// unclosed tag
						// do not advance past the '<'
						return new StartTag(name, attributes);
					} else if (character >= 'a' && character <= 'z'
							|| character >= 'A' && character <= 'Z') {
						// read attribute
						final String attributeName = readName();
						// check for equal sign
						if (character == '=') {
							character = characterIterator.next();
							final String attributeValue = readAttributeValue();
							// add attribute to current
							attributes.put(attributeName, attributeValue);
						} else {
							// add attribute to current
							attributes.put(attributeName, null);
						}
					}
				}
			}
		}
		
		throw new RuntimeException("Unexpected character starting token: " + character);
	}

	private Token readDoctype() {
		final String name = readName();
		// assert name == "DOCTYPE"
		if ( !name.toUpperCase(Locale.ENGLISH).equals("DOCTYPE")) {
			throw new RuntimeException("Expected <!DOCTYPE");
		}

		eatWhitespace();
		
		final String type;
		if (character >= 'a' && character <= 'z'
				|| character >= 'A' && character <= 'Z') {
			// read document type
			type = readName();
		} else {
			throw new RuntimeException("Unexpected character in <!DOCTYPE " + character);
		}

		eatWhitespace();
		
		// read PUBLIC
		final String publicStr = readName();
		if ( !publicStr.toUpperCase(Locale.ENGLISH).equals("PUBLIC")) {
			throw new RuntimeException("Expected PUBLIC");
		}

		eatWhitespace();
		
		final StringBuilder publicId = new StringBuilder();
		if (character == '"') {
			character = characterIterator.next();
			// read string
			while (character != '"') {
				if (character == CharacterIterator.DONE) {
					throw new RuntimeException("Public ID not closed.");
				}
				publicId.append(character);
				character = characterIterator.next();
			}
			character = characterIterator.next();
		} else {
			throw new RuntimeException("Public ID must be quoted.");
		}

		eatWhitespace();
		
		final StringBuilder systemId = new StringBuilder();
		if (character == '"') {
			character = characterIterator.next();
			// read string
			while (character != '"') {
				if (character == CharacterIterator.DONE) {
					throw new RuntimeException("System ID not closed.");
				}
				systemId.append(character);
				character = characterIterator.next();
			}
			character = characterIterator.next();
			eatWhitespace();
		}
		
		if (character == '>') {
			character = characterIterator.next();
			
			return new Doctype(type, publicId.toString(), systemId.toString());
		} else {
			throw new RuntimeException("Unclosed DOCTYPE.");
		}
	}

	private EntityReference readEntityReference() {
		if (character == '#') {
			// this is a numeric character reference
			character = characterIterator.next();
			if (character == 'x' || character == 'X') {
				// this is a hexadecimal numeric character reference
				character = characterIterator.next();
				final String hexValue = readEntityReferenceBody();
				final HexadecimalEntityReference entityReference = new HexadecimalEntityReference(hexValue);
				return entityReference;
			} else {
				// this is a decimal numeric character reference
				final String decimalValue = readEntityReferenceBody();
				final NumericEntityReference entityReference = new NumericEntityReference(decimalValue);
				return entityReference;
			}
		} else {
			// read until the closing ';'
			final String entityName = readEntityReferenceBody();
			final NamedEntityReference entityReference = new NamedEntityReference(entityName);
			return entityReference;
		}
	}

	private String readEntityReferenceBody() {
		final StringBuilder stringBuilder = new StringBuilder();
		while (character != CharacterIterator.DONE) {
			if (character == ';') {
				// closed cleanly
				character = characterIterator.next();
				return stringBuilder.toString();
			}
			if (character == '\n') {
				// closed by line break
				return stringBuilder.toString();
			}
			if (character == '<') {
				// closed implicitly by the start of a new tag
				return stringBuilder.toString();
			}
			stringBuilder.append(character);
			character = characterIterator.next();
		}
		throw new RuntimeException("Unclosed charater entity reference: ");
	}

	private String readName() {
		final StringBuilder name = new StringBuilder();
		do {
			name.append(character);
			character = characterIterator.next();
		} while (character >= 'a' && character <= 'z'
				|| character >= 'A' && character <= 'Z'
				|| character >= '0' && character <= '9'
				|| character == '-'
				|| character == '.'
				|| character == '_'
				|| character == ':');
		return name.toString();
	}

	private String readAttributeValue() {
		final StringBuilder attributeValue = new StringBuilder();
		if (character == '"') {
			// read everything until the next "
			character = characterIterator.next();
			while (character != CharacterIterator.DONE) {
				if (character == '"') {
					character = characterIterator.next();
					// return
					return attributeValue.toString();
				} else {
					attributeValue.append(character);
					character = characterIterator.next();
				}
			}
			throw new RuntimeException("Document ended before attribute value's closing \" found.");
		} else if (character == '\'') {
			// read everything until the next '
			character = characterIterator.next();
			while (character != CharacterIterator.DONE) {
				character = characterIterator.next();
				if (character == '\'') {
					character = characterIterator.next();
					// return
					return attributeValue.toString();
				} else {
					attributeValue.append(character);
					character = characterIterator.next();
				}
			}
			throw new RuntimeException("Document ended before attribute value's closing ' found.");
		} else if (character >= 'a' && character <= 'z'
				|| character >= 'A' && character <= 'Z'
				|| character >= '0' && character <= '9'
				|| character == '-'
				|| character == '.'
				|| character == '_'
				|| character == ':') {
			// unquoted attribute value
			do {
				attributeValue.append(character);
				character = characterIterator.next();
			} while (character >= 'a' && character <= 'z'
					|| character >= 'A' && character <= 'Z'
					|| character >= '0' && character <= '9'
					|| character == '-'
					|| character == '.'
					|| character == '_'
					|| character == ':');
		}
		throw new RuntimeException("Illegal character for attribute value start: " + character);
	}

	private ProcessingInstruction readProcessingInstruction() {
		final StringBuilder stringBuilder = new StringBuilder();
		while (character != '>') {
			if (character == CharacterIterator.DONE) {
				// parse error
				throw new RuntimeException("Document ended before closing '>' found for processing instruction.");
			}
			stringBuilder.append(character);
			character = characterIterator.next();
		}
		character = characterIterator.next();
		return new ProcessingInstruction(stringBuilder.toString());
	}

	private Token readComment() {
		final StringBuilder stringBuilder = new StringBuilder();
		while (character != CharacterIterator.DONE) {
			final char nextCharacter = characterIterator.next();
			if (character == '-' && nextCharacter == '-') {
				// this is the end of the comment.
				final Comment comment = new Comment(stringBuilder.toString());
				// ignore character and nextCharacter
				character = characterIterator.next();
				// read whitespace
				while (character == ' ' || character == '\r' || character == '\n' || character == '\t') {
					character = characterIterator.next();
				}
				// read closing '>'
				if (character != '>') {
					throw new RuntimeException("Missing closing '>' for comment.");
				}
				character = characterIterator.next();
				return comment;
			} else {
				stringBuilder.append(character);
				character = nextCharacter;
			}
		}
		throw new RuntimeException("Document ended before closing \"--\" found for comment.");
	}

}
