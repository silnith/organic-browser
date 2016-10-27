package org.silnith.browser.organic;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * 
 * 
 * @author kent
 */
public abstract class StyledContent {
    
    private final StyledDOMElement parent;
    
    public StyledContent(final StyledDOMElement parent) {
        // may be null
        this.parent = parent;
    }
    
    public abstract StyleData getStyleData();
    
    public abstract Locale getLanguage();
    
    public StyledDOMElement getParent() {
        return parent;
    }
    
    /**
     * Creates a DOM Node to represent the state of this styled content.  This
     * is primarily used for debugging, so that the state of the style information
     * can be output in an easily-readable format.
     * 
     * @param document the owning document, needed for creating new nodes
     * @return a Node representing this styled content
     */
    public abstract Node createDOM(final Document document);
    
}
