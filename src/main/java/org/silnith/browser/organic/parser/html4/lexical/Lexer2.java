package org.silnith.browser.organic.parser.html4.lexical;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Locale;

import org.silnith.browser.organic.network.Download;


public class Lexer2 {
    
    private final BufferedReader reader;
    
    public Lexer2(final Reader reader) {
        super();
        this.reader = new BufferedReader(reader);
    }
    
    public void close() throws IOException {
        reader.close();
    }
    
    public boolean isWhitespace(final char character) {
        return character == ' ' || character == '\r' || character == '\n' || character == '\t';
    }
    
    public boolean isNameStart(final char character) {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
    }
    
    public boolean isNumber(final char character) {
        return (character >= '0' && character <= '9');
    }
    
    public boolean isName(final char character) {
        return isNameStart(character) || isNumber(character) || character == '.' || character == '_' || character == '-'
                || character == ':';
    }
    
    public boolean isHexNumber(final char character) {
        return isNumber(character) || (character >= 'a' && character <= 'f') || (character >= 'A' && character <= 'F');
    }
    
    private char peekNextChar() throws IOException {
        final char[] buf = new char[1];
        reader.mark(1);
        final int numRead = reader.read(buf);
        reader.reset();
        assert numRead == 1;
        return buf[0];
    }
    
    private char getNextChar() throws IOException {
        final char[] buf = new char[1];
        final int numRead = reader.read(buf);
        assert numRead == 1;
        return buf[0];
    }
    
    public void validateWhitespace() throws IOException {
        final char[] buf = new char[1];
        int numRead;
        
        do {
            reader.mark(1);
            numRead = reader.read(buf);
        } while (numRead == 1 && isWhitespace(buf[0]));
        reader.reset();
    }
    
    public void validate() throws IOException {
        validatePCData();
    }
    
    public void validatePCData() throws IOException {
        final char[] buf = new char[1];
        int numRead;
        
        reader.mark(2);
        numRead = reader.read(buf);
        while (numRead != -1) {
            if (buf[0] == '<') {
                // is this an open or close tag?
                numRead = reader.read(buf);
                assert numRead == 1;
                if (buf[0] == '/') {
                    // close tag
                    reader.reset();
                    return;
                } else {
                    // parse tag
                    reader.reset();
                    validateMarkup();
                }
            } else if (buf[0] == '&') {
                // parse entity
                reader.reset();
                validateEntity();
            }
            
            reader.mark(1);
            numRead = reader.read(buf);
        }
    }
    
    public void validatePCDataInShort() throws IOException {
        final char[] buf = new char[1];
        int numRead;
        
        reader.mark(1);
        numRead = reader.read(buf);
        while (numRead != -1) {
            if (buf[0] == '<') {
                // parse tag
                reader.reset();
                validateMarkup();
            } else if (buf[0] == '/') {
                // close tag
                reader.reset();
                return;
            } else if (buf[0] == '&') {
                // parse entity
                reader.reset();
                validateEntity();
            }
            
            reader.mark(1);
            numRead = reader.read(buf);
        }
    }
    
    public void validateCData() throws IOException {
        final char[] buf = new char[1];
        int numRead;
        
        reader.mark(2);
        numRead = reader.read(buf);
        while (numRead != -1) {
            if (buf[0] == '<') {
                // is this an open or close tag?
                numRead = reader.read(buf);
                assert numRead == 1;
                if (buf[0] == '/') {
                    // close tag
                    reader.reset();
                    return;
                }
            }
            
            reader.mark(1);
            numRead = reader.read(buf);
        }
    }
    
    public void validateMarkup() throws IOException {
        reader.mark(2);
        char character = getNextChar();
        assert character == '<';
        
        character = getNextChar();
        if (character == '!') {
            reader.reset();
            validateMarkupDeclaration();
        } else if (character == '?') {
            reader.reset();
            validateProcessingInstruction();
        } else {
            reader.reset();
            character = getNextChar();
            assert character == '<';
            final String tagName = validateName();
            validateWhitespace();
            
            character = peekNextChar();
            while (isNameStart(character)) {
                validateName();
                validateWhitespace();
                character = peekNextChar();
                if (character == '=') {
                    character = getNextChar();
                    assert character == '=';
                    validateWhitespace();
                    validateAttributeValue();
                    validateWhitespace();
                    character = peekNextChar();
                }
            }
            
            if (character == '>') {
                character = getNextChar();
                assert character == '>';
                
                if (tagName.toUpperCase(Locale.ENGLISH).equals("SCRIPT")
                        || tagName.toUpperCase(Locale.ENGLISH).equals("STYLE")) {
                    validateCData();
                } else {
                    validatePCData();
                }
                
                character = getNextChar();
                assert character == '<';
                character = getNextChar();
                assert character == '/';
                validateName();
                validateWhitespace();
                character = getNextChar();
                assert character == '>';
            } else if (character == '/') {
                character = getNextChar();
                assert character == '/';
                
                validatePCDataInShort();
                
                character = getNextChar();
                assert character == '/';
            } else {
                assert false;
            }
        }
    }
    
    public void validateMarkupDeclaration() throws IOException {
        reader.mark(3);
        char character = getNextChar();
        assert character == '<';
        character = getNextChar();
        assert character == '!';
        
        character = getNextChar();
        reader.reset();
        if (character == '-') {
            character = getNextChar();
            assert character == '<';
            character = getNextChar();
            assert character == '!';
            character = getNextChar();
            assert character == '-';
            character = getNextChar();
            assert character == '-';
            
            do {
                character = getNextChar();
                while (character != '-') {
                    character = getNextChar();
                }
                character = peekNextChar();
            } while (character != '-');
            character = getNextChar();
            assert character == '-';
            
            validateWhitespace();
            
            character = getNextChar();
            assert character == '>';
        } else if (character == '[') {
            // marked block
            character = getNextChar();
            assert character == '<';
            character = getNextChar();
            assert character == '!';
            character = getNextChar();
            assert character == '[';
            
            validateWhitespace();
            validateName();
            
            character = getNextChar();
            assert character == '[';
            
            do {
                do {
                    character = getNextChar();
                    while (character != ']') {
                        character = getNextChar();
                    }
                    character = peekNextChar();
                } while (character != ']');
                character = getNextChar();
                assert character == ']';
                character = peekNextChar();
            } while (character != '>');
            character = getNextChar();
            assert character == '>';
        } else if (isNameStart(character)) {
            // doctype or other instruction
            character = getNextChar();
            assert character == '<';
            character = getNextChar();
            assert character == '!';
            
            validateName();
            
            character = getNextChar();
            while (character != '>') {
                character = getNextChar();
            }
        }
    }
    
    public String validateProcessingInstruction() throws IOException {
        final StringBuilder current = new StringBuilder();
        char character = getNextChar();
        assert character == '<';
        current.append(character);
        character = getNextChar();
        assert character == '?';
        current.append(character);
        
        do {
            character = getNextChar();
            current.append(character);
        } while (character != '>');
        
        System.out.println(current);
        return current.toString();
    }
    
    public void validateAttributeValue() throws IOException {
        final StringBuilder current = new StringBuilder();
        char character = peekNextChar();
        if (character == '"') {
            character = getNextChar();
            assert character == '"';
            // double quoted string
            do {
                current.append(character);
                reader.mark(1);
                character = getNextChar();
                if (character == '&') {
                    reader.reset();
                    final String entity = validateEntity();
                    current.append(entity);
                }
            } while (character != '"');
            current.append(character);
        } else if (character == '\'') {
            // single quoted string
            character = getNextChar();
            assert character == '\'';
            // double quoted string
            do {
                current.append(character);
                reader.mark(1);
                character = getNextChar();
                if (character == '&') {
                    reader.reset();
                    final String entity = validateEntity();
                    current.append(entity);
                }
            } while (character != '\'');
            current.append(character);
        } else if (isNameStart(character)) {
            // unquoted name
            final String value = validateName();
            current.append(value);
        } else {
            assert false;
        }
        System.out.println(current);
    }
    
    public String validateName() throws IOException {
        final StringBuilder current = new StringBuilder();
        char character = getNextChar();
        assert isNameStart(character);
        while (isName(character)) {
            current.append(character);
            reader.mark(1);
            character = getNextChar();
        }
        reader.reset();
        System.out.println(current);
        return current.toString();
    }
    
    public String validateEntity() throws IOException {
        final StringBuilder current = new StringBuilder();
        char character = getNextChar();
        assert character == '&';
        current.append(character);
        
        character = peekNextChar();
        if (isName(character)) {
            final String name = validateName();
            current.append(name);
        } else if (character == '#') {
            character = getNextChar();
            assert character == '#';
            current.append(character);
            character = peekNextChar();
            if (character == 'x' || character == 'X') {
                character = getNextChar();
                assert character == 'x' || character == 'X';
                current.append(character);
                final String number = validateHexNumber();
                current.append(number);
            } else {
                final String number = validateNumber();
                current.append(number);
            }
        }
        
        reader.mark(1);
        character = getNextChar();
        assert character == ';' || isWhitespace(character) || character == '>' || character == '/';
        if (character != ';') {
            reader.reset();
        } else {
            current.append(character);
        }
        System.out.println(current);
        return current.toString();
    }
    
    public String validateHexNumber() throws IOException {
        final StringBuilder current = new StringBuilder();
        char character = getNextChar();
        assert isHexNumber(character);
        while (isHexNumber(character)) {
            current.append(character);
            reader.mark(1);
            character = getNextChar();
        }
        reader.reset();
        System.out.println(current);
        return current.toString();
    }
    
    public String validateNumber() throws IOException {
        final StringBuilder current = new StringBuilder();
        char character = getNextChar();
        assert isNumber(character);
        while (isNumber(character)) {
            current.append(character);
            reader.mark(1);
            character = getNextChar();
        }
        reader.reset();
        System.out.println(current);
        return current.toString();
    }
    
    public static void main(final String[] args) throws IOException {
        URL url;
        url = new URL("http://slashdot.org/");
        url = new URL("http://www.w3.org/");
        final Download download = new Download(url);
        download.connect();
        download.download();
        final String content = download.getContent();
        System.out.println(content);
        System.out.println();
        final Lexer2 lexer = new Lexer2(new StringReader(content));
        lexer.validate();
    }
    
}
