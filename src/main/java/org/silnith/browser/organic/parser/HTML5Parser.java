package org.silnith.browser.organic.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.silnith.browser.organic.network.Download;
import org.silnith.parser.html5.Parser;
import org.silnith.parser.html5.lexical.Tokenizer;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;


public class HTML5Parser implements FileParser<Document> {
    
    private final DOMImplementationRegistry registry;
    
    public HTML5Parser(final DOMImplementationRegistry registry) {
        super();
        this.registry = registry;
    }
    
    @Override
    public Document parse(final Download download) throws IOException {
        final InputStream in = download.getInputStream();
        final String contentEncoding = download.getContentEncoding();
        final Reader reader;
        if (contentEncoding == null) {
            reader = new InputStreamReader(in, Charset.forName("UTF-8"));
        } else {
            reader = new InputStreamReader(in, contentEncoding);
        }
        final Tokenizer tokenizer = new Tokenizer(reader);
        final Parser parser = new Parser(tokenizer, registry.getDOMImplementation("Core 3.0 +LS 3.0"));
        return parser.parse();
    }
    
    @Override
    public void dispose() {
    }
    
}
