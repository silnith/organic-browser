package org.silnith.browser.organic.parser;

import java.io.IOException;

import org.silnith.browser.organic.network.Download;


public interface FileParser<T> {
    
    T parse(Download download) throws IOException;
    
    void dispose();
    
}
