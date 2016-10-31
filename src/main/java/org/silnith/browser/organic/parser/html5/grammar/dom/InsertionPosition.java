package org.silnith.browser.organic.parser.html5.grammar.dom;

import org.w3c.dom.Node;


public interface InsertionPosition {
    
    Node getContainingNode();
    
    Node getNodeImmediatelyBefore();
    
    void insert(Node node);
    
}
