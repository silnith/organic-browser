package org.silnith.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;


public class ElementCow extends NodeCow implements Element {
    
    public ElementCow() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public String getTagName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getAttribute(final String name) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setAttribute(final String name, final String value) throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void removeAttribute(final String name) throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Attr getAttributeNode(final String name) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Attr setAttributeNode(final Attr newAttr) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Attr removeAttributeNode(final Attr oldAttr) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public NodeList getElementsByTagName(final String name) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getAttributeNS(final String namespaceURI, final String localName) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String value)
            throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void removeAttributeNS(final String namespaceURI, final String localName) throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Attr getAttributeNodeNS(final String namespaceURI, final String localName) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Attr setAttributeNodeNS(final Attr newAttr) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public NodeList getElementsByTagNameNS(final String namespaceURI, final String localName) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean hasAttribute(final String name) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean hasAttributeNS(final String namespaceURI, final String localName) throws DOMException {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public TypeInfo getSchemaTypeInfo() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setIdAttribute(final String name, final boolean isId) throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void setIdAttributeNS(final String namespaceURI, final String localName, final boolean isId)
            throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void setIdAttributeNode(final Attr idAttr, final boolean isId) throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
}
