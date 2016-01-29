package org.silnith.browser.organic.parser.html5.lexical;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class InputStreamPreprocessorTest {

	@Test
	public void testReadSingleCharacters() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("abc"))) {
			assertEquals('a', in.read());
			assertEquals('b', in.read());
			assertEquals('c', in.read());
			assertEquals(-1, in.read());
		}
	}

	@Test
	public void testReadSingleCharactersCarriageReturn() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("a\rc"))) {
			assertEquals('a', in.read());
			assertEquals('\n', in.read());
			assertEquals('c', in.read());
			assertEquals(-1, in.read());
		}
	}

	@Test
	public void testReadSingleCharactersLineFeed() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("a\nc"))) {
			assertEquals('a', in.read());
			assertEquals('\n', in.read());
			assertEquals('c', in.read());
			assertEquals(-1, in.read());
		}
	}

	@Test
	public void testReadSingleCharactersCarriageReturnLineFeed() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("a\r\nc"))) {
			assertEquals('a', in.read());
			assertEquals('\n', in.read());
			assertEquals('c', in.read());
			assertEquals(-1, in.read());
		}
	}

	@Test
	public void testReadEmpty() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader(""))) {
			final char[] buf = new char[1];
			final int numRead = in.read(buf, 0, buf.length);
			
			assertEquals(-1, numRead);
		}
	}

	@Test
	public void testReadSingleCharacter() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("a"))) {
			final char[] buf = new char[1];
			final int numRead = in.read(buf, 0, buf.length);
			
			assertEquals(1, numRead);
			assertArrayEquals(new char[] {'a'}, buf);
		}
	}

	@Test
	public void testReadSingleCharacterBufferTooLarge() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("a"))) {
			final char[] buf = new char[2];
			final int numRead = in.read(buf, 0, buf.length);
			
			assertEquals(1, numRead);
			assertArrayEquals(new char[] {'a', 0}, buf);
		}
	}

	@Test
	public void testReadSingleCarriageReturn() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("\r"))) {
			final char[] buf = new char[1];
			final int numRead = in.read(buf, 0, buf.length);
			
			assertEquals(1, numRead);
			assertArrayEquals(new char[] {'\n'}, buf);
		}
	}

	@Test
	public void testReadSingleCarriageReturnBufferTooLarge() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("\r"))) {
			final char[] buf = new char[2];
			final int numRead = in.read(buf, 0, buf.length);
			
			assertEquals(1, numRead);
			assertArrayEquals(new char[] {'\n', 0}, buf);
		}
	}

	@Test
	public void testReadSingleLineFeed() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("\n"))) {
			final char[] buf = new char[1];
			final int numRead = in.read(buf, 0, buf.length);
			
			assertEquals(1, numRead);
			assertArrayEquals(new char[] {'\n'}, buf);
		}
	}

	@Test
	public void testReadSingleLineFeedBufferTooLarge() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("\n"))) {
			final char[] buf = new char[2];
			final int numRead = in.read(buf, 0, buf.length);
			
			assertEquals(1, numRead);
			assertArrayEquals(new char[] {'\n', 0}, buf);
		}
	}

	@Test
	public void testReadSingleCarriageReturnLineFeedBufferTooLarge() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("\r\n"))) {
			final char[] buf = new char[2];
			final int numRead = in.read(buf, 0, buf.length);
			
			assertEquals(1, numRead);
			assertArrayEquals(new char[] {'\n', 0}, buf);
		}
	}

	@Test
	public void testReadSingleCharacterAfterCarriageReturn() throws IOException {
		try (final InputStreamPreprocessor in = new InputStreamPreprocessor(new StringReader("\ra"))) {
			final char[] buf = new char[1];
			in.read(buf, 0, buf.length);
			final int numRead = in.read(buf, 0, buf.length);
			
			assertEquals(1, numRead);
			assertArrayEquals(new char[] {'a'}, buf);
		}
	}

}
