package org.silnith.browser.organic.parser.html5.grammar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class InsideAfterLastChild implements InsertionLocation {
    
    private Element target;
    
    public InsideAfterLastChild(Element target) {
        super();
    }

    @Override
    public boolean isInsideTemplateElement() {
        return target.getTagName().equals("template");
    }

    @Override
    public Document getOwnerDocument() {
        return target.getOwnerDocument();
    }

    @Override
    public void insert(Node node) {
        target.appendChild(node);
    }

}
