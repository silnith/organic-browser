package org.silnith.browser.organic.parser.html5.grammar;

import org.w3c.dom.Element;


public class ElementType {
    
    private final String namespace;
    
    private final String name;
    
    public ElementType(final String namespace, final String name) {
        super();
        this.namespace = namespace;
        this.name = name;
    }
    
    public String getNamespace() {
        return namespace;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean matches(final Element element) {
        return namespace.equals(element.getNamespaceURI()) && name.equals(element.getLocalName());
    }
    
}
