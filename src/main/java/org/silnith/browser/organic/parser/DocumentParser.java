package org.silnith.browser.organic.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.silnith.browser.organic.network.Download;
import org.silnith.parser.html5.Parser;
import org.silnith.parser.html5.lexical.Tokenizer;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;


public class DocumentParser {
    
    private static final String XML_SYSTEM_ID = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
    
    private static final String XML_PUBLIC_ID = "-//W3C//DTD XHTML 1.0 Strict//EN";
    
    private static final String XML_NAMESPACE = "http://www.w3.org/1999/xhtml";
    
    private static final String HTML_SYSTEM_ID = "http://www.w3.org/TR/html4/strict.dtd";
    
    private static final String HTML_PUBLIC_ID = "-//W3C//DTD HTML 4.01//EN";
//	private static final String HTML_NAMESPACE = "http://www.w3.org/1999/xhtml";
    
    private final DOMImplementationRegistry registry;
    
    public DocumentParser()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
        super();
        this.registry = DOMImplementationRegistry.newInstance();
    }
    
    public void printNode(final Node node) {
        try {
//			final DOMImplementation domImpl = registry.getDOMImplementation("Core 3.0 +LS 3.0");
            final DOMImplementation domImpl = registry.getDOMImplementation("HTML +LS");
            final DOMImplementationLS domImplLS = (DOMImplementationLS) domImpl.getFeature("+LS", null);
            final LSSerializer serializer = domImplLS.createLSSerializer();
            serializer.getDomConfig().setParameter("format-pretty-print", true);
            final LSOutput output = domImplLS.createLSOutput();
            output.setByteStream(System.out);
            serializer.write(node, output);
        } catch (final DOMException e) {
            e.printStackTrace();
        } catch (final ClassCastException e) {
            e.printStackTrace();
        }
    }
    
    public Document parseDocument(final Download download) {
        final InputStream inputStream = new ByteArrayInputStream(download.getContentBytes());
//		return parseDocument(inputStream, download.getContentEncoding());
        return parseDocumentExperimentally(inputStream, download.getContentEncoding());
    }
    
    public Document parseDocument(final InputStream inputStream, final String encoding) {
        final DOMImplementation domImpl = registry.getDOMImplementation("Core 3.0 +LS 3.0");
        final DOMImplementationLS domImplLS = (DOMImplementationLS) domImpl.getFeature("+LS", "3.0");
        
        /*
         * For XML Schema, set second parameter to createLSParser to
         * "http://www.w3.org/2001/XMLSchema". For XML DTD, set second parameter
         * to createLSParser to "http://www.w3.org/TR/REC-xml".
         */
//		final LSParser parser = domImplLS.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, "http://www.w3.org/2001/XMLSchema");
//		final LSParser parser = domImplLS.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, "http://www.w3.org/TR/REC-xml");
        final LSParser parser = domImplLS.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
        final DOMConfiguration domConfig = parser.getDomConfig();
        final DOMStringList parameterNames = domConfig.getParameterNames();
        int i = 0;
        while (i < parameterNames.getLength()) {
            final String parameterName = parameterNames.item(i++ );
            final Object parameter = domConfig.getParameter(parameterName);
            
            System.out.println(parameterName + " = " + parameter);
        }
        domConfig.setParameter("validate-if-schema", true);
        domConfig.setParameter("validate", true);
//		final LSParser parser = domImplLS.createLSParser(DOMImplementationLS.MODE_ASYNCHRONOUS, null);
        final LSInput input = domImplLS.createLSInput();
        input.setByteStream(inputStream);
        input.setPublicId(HTML_PUBLIC_ID);
        input.setSystemId(HTML_SYSTEM_ID);
        input.setEncoding(encoding);
        final Document document = parser.parse(input);
//		document.normalizeDocument();

//		final LSSerializer serializer = domImplLS.createLSSerializer();
//		serializer.getDomConfig().setParameter("format-pretty-print", true);

//		final LSOutput output = domImplLS.createLSOutput();
//		output.setByteStream(System.out);
//		serializer.write(document, output);
        
        return document;
    }
    
    public Document parseDocumentExperimentally(final InputStream inputStream, final String encoding) {
        final DOMImplementation domImpl = registry.getDOMImplementation("Core 3.0 +LS 3.0");
        final DOMImplementationLS domImplLS = (DOMImplementationLS) domImpl.getFeature("+LS", "3.0");
        
        /*
         * For XML Schema, set second parameter to createLSParser to
         * "http://www.w3.org/2001/XMLSchema". For XML DTD, set second parameter
         * to createLSParser to "http://www.w3.org/TR/REC-xml".
         */
        final Reader reader;
        try {
            reader = new InputStreamReader(inputStream, encoding);
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        final Tokenizer tokenizer = new Tokenizer(reader);
        final Parser parser = new Parser(tokenizer, domImpl);
        final Document document = parser.parse();
//		document.normalizeDocument();

//		final LSSerializer serializer = domImplLS.createLSSerializer();
//		serializer.getDomConfig().setParameter("format-pretty-print", true);

//		final LSOutput output = domImplLS.createLSOutput();
//		output.setByteStream(System.out);
//		serializer.write(document, output);
        
        return document;
    }
    
    public Document createDocument(final Object documentText) {
        final DOMImplementation domImpl = registry.getDOMImplementation("Core 3.0 +LS 3.0");
        return createDocument(domImpl);
    }
    
    private static Document createDocument(final DOMImplementation domImpl) {
        final DocumentType docType = domImpl.createDocumentType("html", XML_PUBLIC_ID, XML_SYSTEM_ID);
        final Document domDocument = domImpl.createDocument(XML_NAMESPACE, "html", docType);
        final Element documentElement = domDocument.getDocumentElement();
        
        final Element headElement = domDocument.createElement("head");
        documentElement.appendChild(headElement);
        final Element titleElement = domDocument.createElement("title");
        headElement.appendChild(titleElement);
        titleElement.appendChild(domDocument.createTextNode("Test Document"));
        final Element styleElement = domDocument.createElement("style");
        headElement.appendChild(styleElement);
        styleElement.setAttribute("type", "text/css");
        styleElement.appendChild(domDocument.createTextNode("H1 { padding: 1em; }"));
        
        final Element bodyElement = domDocument.createElement("body");
        documentElement.appendChild(bodyElement);
        
        final Element firstPara = domDocument.createElement("p");
        bodyElement.appendChild(firstPara);
        firstPara.appendChild(domDocument.createTextNode("This is a test."));
        
        final Element secondPara = domDocument.createElement("p");
        bodyElement.appendChild(secondPara);
        secondPara.appendChild(domDocument.createTextNode("This is a test."));
        
        final Element thirdPara = domDocument.createElement("p");
        bodyElement.appendChild(thirdPara);
        thirdPara.appendChild(domDocument.createTextNode("I never program without my test."));
        
        bodyElement.appendChild(domDocument.createTextNode("Ordered list:"));
        final Element orderedList = domDocument.createElement("ol");
        bodyElement.appendChild(orderedList);
        final Element firstOrderedItem = domDocument.createElement("li");
        orderedList.appendChild(firstOrderedItem);
        firstOrderedItem.appendChild(domDocument.createTextNode("First element."));
        final Element secondOrderedItem = domDocument.createElement("li");
        orderedList.appendChild(secondOrderedItem);
        final Element orderedListDiv = domDocument.createElement("div");
        secondOrderedItem.appendChild(orderedListDiv);
        orderedListDiv.appendChild(domDocument.createTextNode("Second element."));
        final Element thirdOrderedItem = domDocument.createElement("li");
        orderedList.appendChild(thirdOrderedItem);
        thirdOrderedItem.appendChild(domDocument.createTextNode("Third element."));
        
        bodyElement.appendChild(domDocument.createTextNode("Unordered list:"));
        final Element unorderedList = domDocument.createElement("ul");
        bodyElement.appendChild(unorderedList);
        final Element firstUnorderedItem = domDocument.createElement("li");
        unorderedList.appendChild(firstUnorderedItem);
        firstUnorderedItem.appendChild(domDocument.createTextNode("First element."));
        final Element secondUnorderedItem = domDocument.createElement("li");
        unorderedList.appendChild(secondUnorderedItem);
        final Element unorderedListDiv = domDocument.createElement("div");
        secondUnorderedItem.appendChild(unorderedListDiv);
        unorderedListDiv.appendChild(domDocument.createTextNode("Second element."));
        final Element thirdUnorderedItem = domDocument.createElement("li");
        unorderedList.appendChild(thirdUnorderedItem);
        thirdUnorderedItem.appendChild(domDocument.createTextNode("Third element."));
        
        final Element lineWrappingPara = domDocument.createElement("p");
        bodyElement.appendChild(lineWrappingPara);
        lineWrappingPara.appendChild(domDocument.createTextNode(
                "The Java language-sensitive text parsing routines seem to be flakey though. They keep breaking lines in the middle of words when there's no need to."));
                
        final Element badgerPara = domDocument.createElement("p");
        bodyElement.appendChild(badgerPara);
        badgerPara.appendChild(domDocument.createTextNode("badger"));
        for (int i = 0; i < 40; i++ ) {
//			badgerPara.appendChild(domDocument.createTextNode(""));
            badgerPara.appendChild(domDocument.createTextNode(" badger"));
        }
        
        final Element inlineElementsPara = domDocument.createElement("p");
        bodyElement.appendChild(inlineElementsPara);
        inlineElementsPara.appendChild(domDocument.createTextNode("Several "));
        final Element emElement = domDocument.createElement("em");
        inlineElementsPara.appendChild(emElement);
        emElement.appendChild(domDocument.createTextNode("emphasized words"));
        inlineElementsPara.appendChild(domDocument.createTextNode(" appear "));
        final Element strongElement = domDocument.createElement("strong");
        inlineElementsPara.appendChild(strongElement);
        strongElement.appendChild(domDocument.createTextNode("in this"));
        inlineElementsPara.appendChild(domDocument.createTextNode(" sentence, dear."));
        
        final Element divElement = domDocument.createElement("div");
        bodyElement.appendChild(divElement);
        divElement.appendChild(domDocument.createTextNode("Some text"));
        final Element innerPara = domDocument.createElement("p");
        divElement.appendChild(innerPara);
        innerPara.appendChild(domDocument.createTextNode("More text"));
        
        final Element nestedElementsPara = domDocument.createElement("p");
        bodyElement.appendChild(nestedElementsPara);
        nestedElementsPara.appendChild(
                domDocument.createTextNode("This is two inline elements nested inside each other: "));
        final Element anchorElement = domDocument.createElement("a");
        nestedElementsPara.appendChild(anchorElement);
        anchorElement.setAttribute("href", "http://www.w3.org/TR/html4/");
        final Element abbrElement = domDocument.createElement("abbr");
        anchorElement.appendChild(abbrElement);
        abbrElement.setAttribute("title", "HyperText Markup Language");
        abbrElement.appendChild(domDocument.createTextNode("HTML"));
        nestedElementsPara.appendChild(domDocument.createTextNode(" Someday this will be a link."));
        
        final Element emptyInlineElementPara = domDocument.createElement("p");
        bodyElement.appendChild(emptyInlineElementPara);
        emptyInlineElementPara.appendChild(domDocument.createTextNode("This is an empty element."));
        final Element emptyInlineElement = domDocument.createElement("a");
        emptyInlineElementPara.appendChild(emptyInlineElement);
        emptyInlineElementPara.appendChild(domDocument.createTextNode("That was an empty element."));
        
        return domDocument;
    }
    
}
