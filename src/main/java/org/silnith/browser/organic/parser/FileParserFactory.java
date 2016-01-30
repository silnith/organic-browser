package org.silnith.browser.organic.parser;

import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;


public class FileParserFactory {
    
    public FileParserFactory() {
    }
    
    public FileParser<?> getParser(final String mimeType) {
        if (mimeType == null) {
            throw new NullPointerException();
        }
        
        if (mimeType.equals("text/html")) {
            // Create an HTML parser.
            return null;
        } else if (mimeType.equals("text/xhtml+xml")) {
            // Create an XHTML parser.
            return null;
        } else if (mimeType.equals("text/xml")) {
            // xml
        } else if (mimeType.equals("application/xml")) {
            // xml
        } else if (mimeType.endsWith("+xml")) {
            // xml
        }
        
        final Iterator<ImageReader> imageReadersByMIMEType = ImageIO.getImageReadersByMIMEType(mimeType);
        if (imageReadersByMIMEType.hasNext()) {
            // Create a file parser wrapping the given image reader.
            return new ImageParser<>(imageReadersByMIMEType.next());
        }
        
        return null;
    }
    
}
