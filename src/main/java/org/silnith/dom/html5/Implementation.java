package org.silnith.dom.html5;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.html.HTMLDOMImplementation;
import org.w3c.dom.html.HTMLDocument;


public class Implementation implements HTMLDOMImplementation {
    
    /*
     * the DOM feature "CSS 2.0" depends on: "Core 2.0" "Views 2.0" the DOM
     * feature "Views 2.0" depends on: "Core 2.0" the DOM feature "HTML 2.0"
     * depends on: "Core 2.0" supporting both "HTML 2.0" and "XML 2.0" implies
     * support for "XHTML 2.0"
     */
    
    public Implementation() {
    }
    
    @Override
    public boolean hasFeature(final String feature, final String version) {
        return false;
    }
    
    @Override
    public DocumentType createDocumentType(final String qualifiedName, final String publicId, final String systemId)
            throws DOMException {
        return null;
    }
    
    @Override
    public Document createDocument(final String namespaceURI, final String qualifiedName, final DocumentType doctype)
            throws DOMException {
        return null;
    }
    
    @Override
    public Object getFeature(final String feature, final String version) {
        return null;
    }
    
    @Override
    public HTMLDocument createHTMLDocument(final String title) {
        return null;
    }
    
}
