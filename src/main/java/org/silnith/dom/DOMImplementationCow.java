package org.silnith.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;


public class DOMImplementationCow implements DOMImplementation {
    
    public DOMImplementationCow() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean hasFeature(final String feature, final String version) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public DocumentType createDocumentType(final String qualifiedName, final String publicId, final String systemId)
            throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Document createDocument(final String namespaceURI, final String qualifiedName, final DocumentType doctype)
            throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Object getFeature(final String feature, final String version) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
