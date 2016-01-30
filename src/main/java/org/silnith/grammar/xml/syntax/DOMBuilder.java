package org.silnith.grammar.xml.syntax;

import java.util.List;
import java.util.regex.Pattern;


public class DOMBuilder {
    
    private final org.w3c.dom.bootstrap.DOMImplementationRegistry registry;
    
    protected org.w3c.dom.DOMImplementation domImplementation;
    
    protected org.w3c.dom.Document domDocument;
    
    public DOMBuilder(final org.w3c.dom.bootstrap.DOMImplementationRegistry registry) {
        super();
        this.registry = registry;
    }
    
    protected org.w3c.dom.DocumentType getDocumentType(final Prolog prolog) {
        String publicId;
        String systemId;
        if (prolog == null) {
            domImplementation = registry.getDOMImplementation("XML 3.0");
        } else {
            final StringBuilder features = new StringBuilder();
            if (prolog.xmlDecl != null) {
                features.append("XML 3.0 ");
            }
            final DocTypeDecl docTypeDecl = prolog.doctypedecl;
            final String name = docTypeDecl.name;
            final ExternalID externalID = docTypeDecl.externalID;
            if (externalID == null) {
                // check if name is "html"
                if ("html".equalsIgnoreCase(docTypeDecl.name)) {
                    features.append("HTML 2.0 ");
                }
            } else {
                publicId = externalID.pubidLiteral;
                systemId = externalID.systemLiteral;
                // parse public id
                final String[] split = publicId.split(Pattern.quote("//"));
                if (split.length >= 2) {
                    // assert split[0] = "-"
                    // assert split[1] = "W3C"
                    // assert split[2] = "DTD HTML 4*"
                    final String[] split2 = split[2].split(Pattern.quote(" "));
                    // assert split2[0] = "DTD"
                    if (split2[1].equals("HTML")) {
                        features.append("HTML 2.0 ");
                    } else if (split2[1].equals("XHTML")) {
                        features.append("XML 3.0 HTML 2.0 ");
                    }
                    // get version from split2[2]
                }
            }
            domImplementation = registry.getDOMImplementation(features.toString());
        }
//		org.w3c.dom.DocumentType docType = domImplementation.createDocumentType(qualifiedName, publicId, systemId);
//		domDocument = domImplementation.createDocument(namespaceURI, qualifiedName, docType);
        return null;
    }
    
    public org.w3c.dom.Document createDOM(final Document document) {
//		final org.w3c.dom.DocumentType docType = getDocumentType(document.prolog);
        domImplementation = registry.getDOMImplementation("XML 3.0");
        final Element rootElement = document.element;
        final String elementName = getTagName(rootElement);
        domDocument = domImplementation.createDocument("", elementName, null);
        final org.w3c.dom.Element documentElement = createElement(rootElement);
        domDocument.removeChild(domDocument.getDocumentElement());
        domDocument.appendChild(documentElement);
        return domDocument;
    }
    
    private String getTagName(final Element element) {
        final String elementName;
        if (element.emptyElemTag != null) {
            elementName = element.emptyElemTag.name;
        } else {
            elementName = element.sTag.name;
        }
        return elementName;
    }
    
    protected org.w3c.dom.Element createElement(final Element element) {
        final String tagName;
        final List<Attribute> attributes;
        if (element.emptyElemTag != null) {
            tagName = element.emptyElemTag.name;
            attributes = element.emptyElemTag.attributeList;
        } else {
            tagName = element.sTag.name;
            attributes = element.sTag.attributeList;
        }
        final org.w3c.dom.Element domElement = domDocument.createElement(tagName);
        for (final Attribute attribute : attributes) {
            final org.w3c.dom.Attr domAttribute = domDocument.createAttribute(attribute.name);
            for (final Object obj : attribute.attValue.contents) {
                if (obj instanceof Reference) {
                    final Reference reference = (Reference) obj;
                    domAttribute.appendChild(createReference(reference));
                } else {
                    domAttribute.appendChild(domDocument.createTextNode((String) obj));
                }
            }
            domElement.setAttributeNode(domAttribute);
        }
        if (element.content != null) {
            for (final Object obj : element.content.contents) {
                if (obj instanceof Element) {
                    domElement.appendChild(createElement((Element) obj));
                } else if (obj instanceof String) {
                    domElement.appendChild(domDocument.createTextNode((String) obj));
                } else if (obj instanceof Reference) {
                    domElement.appendChild(createReference((Reference) obj));
                } else if (obj instanceof CDSect) {
                    domElement.appendChild(createCData((CDSect) obj));
                } else if (obj instanceof PI) {
                    domElement.appendChild(createPI((PI) obj));
                } else if (obj instanceof Comment) {
                    domElement.appendChild(createComment((Comment) obj));
                }
            }
        }
        return domElement;
    }
    
    protected org.w3c.dom.Node createReference(final Reference reference) {
        if (reference.charRef != null) {
            final String dereferenced = String.valueOf(Character.toChars(reference.charRef.codePoint));
            return domDocument.createTextNode(dereferenced);
        }
        if (reference.entityRef != null) {
            return domDocument.createEntityReference(reference.entityRef.name);
        }
        throw new IllegalStateException();
    }
    
    protected org.w3c.dom.CDATASection createCData(final CDSect cdSect) {
        return domDocument.createCDATASection(cdSect.cData);
    }
    
    protected org.w3c.dom.ProcessingInstruction createPI(final PI pi) {
        return domDocument.createProcessingInstruction(pi.piTarget.name, pi.content);
    }
    
    protected org.w3c.dom.Comment createComment(final Comment comment) {
        return domDocument.createComment(comment.content);
    }
    
}
