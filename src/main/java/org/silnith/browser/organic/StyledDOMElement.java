package org.silnith.browser.organic;

import java.util.Locale;

import org.w3c.dom.Element;


public class StyledDOMElement extends StyledElement {
    
    private final Element element;
    
    private Locale language;
    
    public StyledDOMElement(final StyledDOMElement parent, final Element element, final StyleData styleData) {
        super(parent, styleData);
        if (element == null) {
            throw new NullPointerException();
        }
        this.element = element;
    }
    
    @Override
    public Locale getLanguage() {
        return language;
    }
    
    public void setLanguage(final Locale language) {
        this.language = language;
    }

    public Element getElement() {
        return element;
    }

    @Override
    public String getTagName() {
        return element.getTagName();
    }
    
}
