package org.silnith.browser.organic.parser.html4;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;

import org.silnith.browser.organic.network.Download;
import org.silnith.browser.organic.parser.FileParser;
import org.silnith.browser.organic.parser.html4.token.StartTag;
import org.silnith.browser.organic.parser.html4.token.Token;
import org.w3c.dom.Document;


public class HTML4Parser implements FileParser<Document> {
    
    public HTML4Parser() {
    }
    
    @Override
    public Document parse(final Download download) throws IOException {
        final CharacterIterator characterIterator = new StringCharacterIterator(download.getContent());
        final Tokenizer tokenizer = new Tokenizer(characterIterator);
        tokenizer.eatWhitespace();
        Token token = tokenizer.getNextToken(true);
        // if processing instruction, ignore
        // if comment, ignore
        // if doctype, continue
        // else, error
        while (token != null && token.getType() != Token.Type.DOCTYPE) {
            token = tokenizer.getNextToken(true);
        }
        if (token == null) {
            // there is no doctype, fail completely
            return null;
        }
        
        // process the doctype
        assert token.getType() == Token.Type.DOCTYPE;
        
        token = tokenizer.getNextToken(true);
        
        return null;
    }
    
    public Document parse2(final Download download) throws IOException {
        final Reader characterIterator =
                new InputStreamReader(download.getInputStream(), download.getContentEncoding());
        final Tokenizer2 tokenizer = new Tokenizer2(characterIterator);
        
        tokenizer.eatWhitespace();
        
        Token.Type nextToken = tokenizer.sniffNextToken();
        
        while (nextToken == Token.Type.COMMENT) {
            tokenizer.readComment();
            
            tokenizer.eatWhitespace();
            
            nextToken = tokenizer.sniffNextToken();
        }
        
        if (nextToken == Token.Type.DOCTYPE) {
            // read doctype
            
            tokenizer.eatWhitespace();
            
            nextToken = tokenizer.sniffNextToken();
        }
        
        while (nextToken == Token.Type.COMMENT) {
            tokenizer.readComment();
            
            tokenizer.eatWhitespace();
            
            nextToken = tokenizer.sniffNextToken();
        }
        
        if (nextToken == Token.Type.START_TAG) {
            // read HTML
            final StartTag startTag = tokenizer.readStartTag();
            
            final String name = startTag.getName().toUpperCase(Locale.ENGLISH);
            
            if (name.equals("HTML")) {
                // parse HTML
            } else if (name.equals("HEAD")) {
                // assume HTML, parse HEAD
            } else if (name.equals("TITLE")) {
                // assume HTML and HEAD, parse TITLE
            } else {
                // parse error
            }
        } else {
            // parse error
        }
        
        return null;
    }
    
    @Override
    public void dispose() {
    }
    
    public static void main(final String[] args) throws IOException {
        final Download download = new Download(new URL("http://www.w3.org/TR/CSS2/"));
        download.connect();
        download.download();
        final HTML4Parser parser = new HTML4Parser();
        final Document document = parser.parse(download);
        System.out.println(document);
    }
    
}
