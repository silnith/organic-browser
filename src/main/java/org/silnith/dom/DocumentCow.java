package org.silnith.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;


public class DocumentCow extends NodeCow implements Document {
    
    public DocumentCow() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public DocumentType getDoctype() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public DOMImplementation getImplementation() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Element getDocumentElement() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Element createElement(final String tagName) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public DocumentFragment createDocumentFragment() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Text createTextNode(final String data) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Comment createComment(final String data) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public CDATASection createCDATASection(final String data) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ProcessingInstruction createProcessingInstruction(final String target, final String data)
            throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Attr createAttribute(final String name) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public EntityReference createEntityReference(final String name) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public NodeList getElementsByTagName(final String tagname) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Node importNode(final Node importedNode, final boolean deep) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Element createElementNS(final String namespaceURI, final String qualifiedName) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Attr createAttributeNS(final String namespaceURI, final String qualifiedName) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public NodeList getElementsByTagNameNS(final String namespaceURI, final String localName) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Element getElementById(final String elementId) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getInputEncoding() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getXmlEncoding() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean getXmlStandalone() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void setXmlStandalone(final boolean xmlStandalone) throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public String getXmlVersion() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setXmlVersion(final String xmlVersion) throws DOMException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean getStrictErrorChecking() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void setStrictErrorChecking(final boolean strictErrorChecking) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public String getDocumentURI() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setDocumentURI(final String documentURI) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Node adoptNode(final Node source) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public DOMConfiguration getDomConfig() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void normalizeDocument() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Node renameNode(final Node n, final String namespaceURI, final String qualifiedName) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
