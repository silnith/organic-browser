package org.silnith.browser.organic;

import java.util.Locale;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class LanguageResolver {
    
    private static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
    private static final String HTML_NAMESPACE = "http://www.w3.org/1999/xhtml";
    
    public void setLanguage(final StyledDOMElement styledDOMElement) {
        final Locale language = resolveLanguage(styledDOMElement.getElement());
        styledDOMElement.setLanguage(language);
        for (final StyledContent styledContent : styledDOMElement.getChildren()) {
            if (styledContent instanceof StyledDOMElement) {
                final StyledDOMElement child = (StyledDOMElement) styledContent;
                setLanguage(child);
            }
        }
    }
    
    public Locale resolveLanguage(final Node node) {
        switch (node.getNodeType()) {
        case Node.ELEMENT_NODE: {
            final Element element = (Element) node;
            
            final Attr xmlLangAttr = element.getAttributeNodeNS(XML_NAMESPACE, "lang");
            if (xmlLangAttr != null) {
                return Locale.forLanguageTag(xmlLangAttr.getValue());
            }
            
            if (HTML_NAMESPACE.equals(element.getNamespaceURI())) {
                final Attr htmlLangAttr = element.getAttributeNodeNS(null, "lang");
                if (htmlLangAttr != null) {
                    return Locale.forLanguageTag(htmlLangAttr.getValue());
                }
                
                final Attr langNode = element.getAttributeNode("lang");
                if (langNode != null) {
                    return Locale.forLanguageTag(langNode.getValue());
                }
            }
        } break;
        default: break;
        }
        
        final Node parentNode = node.getParentNode();
        if (parentNode != null) {
            return resolveLanguage(parentNode);
        }
        
        return Locale.ROOT;
    }

}
