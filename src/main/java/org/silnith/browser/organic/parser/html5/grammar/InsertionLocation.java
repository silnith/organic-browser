package org.silnith.browser.organic.parser.html5.grammar;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface InsertionLocation {
    
    boolean isInsideTemplateElement();
    
    Document getOwnerDocument();
    
    void insert(Node node);

}
