package org.silnith.browser.organic.parser.css3.selector;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


public class IDSelector implements SimpleSelector {
    
    private final String id;
    
    public IDSelector(String id) {
        super();
        this.id = id;
    }

    @Override
    public boolean matches(Element element) {
        final NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            final Node item = attributes.item(i);
            
            assert item.getNodeType() == Node.ATTRIBUTE_NODE;
            
            final Attr attr = (Attr) item;
            if (attr.isId()) {
                if (id.equals(attr.getValue())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "#" + id;
    }
    
}
