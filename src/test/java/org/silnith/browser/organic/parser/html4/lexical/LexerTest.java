package org.silnith.browser.organic.parser.html4.lexical;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class LexerTest {

	@Test
	public void testValidateWhitespace() throws IOException {
		final Reader reader = new StringReader(" \n\r\t");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateWhitespace();
	}

	@Test
	public void testValidate() throws IOException {
		final Reader reader = new StringReader("<!-- this is funny -->\n<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"> <html> <head> ... </head> <body> <![CDATA[...]]> </body> </html>");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validate();
	}

	@Test
	public void testValidatePCData() throws IOException {
		final Reader reader = new StringReader("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"> <html> <head> ... </head> <body> ... </body> </html>");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validatePCData();
	}

	@Test
	public void testValidateMarkup() throws IOException {
		final Reader reader = new StringReader("<HTML>this is a test.</HTML>");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateMarkup();
	}

	@Test
	public void testValidateMarkupShort() throws IOException {
		final Reader reader = new StringReader("<HTML/this is a test./");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateMarkup();
	}

	@Test
	public void testValidateMarkupDeclarationDoctype() throws IOException {
		final Reader reader = new StringReader("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"\n \"http://www.w3.org/TR/html4/strict.dtd\">");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateMarkupDeclaration();
	}

	@Test
	public void testValidateMarkupDeclarationCDATA() throws IOException {
		final Reader reader = new StringReader("<![CDATA[This is a test.]]>");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateMarkupDeclaration();
	}

	@Test
	public void testValidateMarkupDeclarationCDATAWithBrackets() throws IOException {
		final Reader reader = new StringReader("<![CDATA[This ]]is a test.]]>");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateMarkupDeclaration();
	}

	@Test
	public void testValidateMarkupDeclarationCDATAWithFakeClose() throws IOException {
		final Reader reader = new StringReader("<![CDATA[This ]>is a test.]]>");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateMarkupDeclaration();
	}

	@Test
	public void testValidateMarkupComment() throws IOException {
		final Reader reader = new StringReader("<!-- this is a test. -->");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateMarkupDeclaration();
	}

	@Test
	public void testValidateMarkupCommentWithDashes() throws IOException {
		final Reader reader = new StringReader("<!-- this-is-a-test. -->");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateMarkupDeclaration();
	}

	@Test
	public void testValidateProcessingInstruction() throws IOException {
		final Reader reader = new StringReader("<? this is a test. >");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateProcessingInstruction();
	}

	@Test
	public void testValidateAttributeValueDoubleQuotes() throws IOException {
		final Reader reader = new StringReader("\"This is a test.\"");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateAttributeValue();
	}

	@Test
	public void testValidateAttributeValueSingleQuotes() throws IOException {
		final Reader reader = new StringReader("'This is a test.'");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateAttributeValue();
	}

	@Test
	public void testValidateAttributeValueUnquotedCloseTag() throws IOException {
		final Reader reader = new StringReader("Thisisatest>");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateAttributeValue();
	}

	@Test
	public void testValidateNamePlusWhitespace() throws IOException {
		final Reader reader = new StringReader("foo1 ");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateName();
	}

	@Test
	public void testValidateNamePlusLeftBracket() throws IOException {
		final Reader reader = new StringReader("foo1[");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateName();
	}

	@Test
	public void testValidateNamePlusCloseTag() throws IOException {
		final Reader reader = new StringReader("foo1>");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateName();
	}

	@Test
	public void testValidateEntity() throws IOException {
		final Reader reader = new StringReader("&amp;");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateEntity();
	}

	@Test
	public void testValidateEntityNumeric() throws IOException {
		final Reader reader = new StringReader("&#224;");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateEntity();
	}

	@Test
	public void testValidateEntityHex() throws IOException {
		final Reader reader = new StringReader("&#x224;");
		final Lexer2 lexer = new Lexer2(reader);
		
		lexer.validateEntity();
	}

}
