package org.silnith.browser.organic;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.css.DOMImplementationCSS;
import org.w3c.dom.ls.DOMImplementationLS;


public class TestDOM {
    
    public TestDOM() {
    }
    
    /**
     * @param args
     */
    public static void main(final String[] args) throws Exception {
        final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        final DOMImplementationList domImplementationList = registry.getDOMImplementationList("HTML");
        for (int i = 0; i < domImplementationList.getLength(); i++ ) {
            System.out.println(domImplementationList.item(i).hasFeature("+Stylesheet", null));
        }
//		final DOMImplementation domImplementation = registry.getDOMImplementation("HTML +CSS +LS");
        final DOMImplementation domImplementation = registry.getDOMImplementation("HTML");
        
        final DocumentType htmlDocType = domImplementation.createDocumentType("HTML", "-//W3C//DTD HTML 4.01//EN",
                "http://www.w3.org/TR/html4/strict.dtd");
        final Document document = domImplementation.createDocument(null, "HTML", htmlDocType);
        
        final DOMImplementationLS loadStoreFeature = (DOMImplementationLS) domImplementation.getFeature("+LS", null);
        
        final DOMImplementationCSS cssFeature = (DOMImplementationCSS) domImplementation.getFeature("+CSS", null);
    }
    
}
