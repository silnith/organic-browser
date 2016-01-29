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
	public boolean hasFeature(String feature, String version) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DocumentType createDocumentType(String qualifiedName,
			String publicId, String systemId) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document createDocument(String namespaceURI, String qualifiedName,
			DocumentType doctype) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getFeature(String feature, String version) {
		// TODO Auto-generated method stub
		return null;
	}

}
