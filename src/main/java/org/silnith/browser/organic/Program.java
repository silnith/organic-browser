package org.silnith.browser.organic;

import java.awt.Dimension;
import java.net.URI;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import org.silnith.browser.organic.box.BlockLevelBox;
import org.silnith.browser.organic.box.Formatter;
import org.silnith.browser.organic.network.Download;
import org.silnith.browser.organic.parser.FileParser;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.Display;
import org.silnith.css.model.data.ListStylePosition;
import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;


public class Program {
    
    private static final String SYSTEM_ID = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
    
    private static final String PUBLIC_ID = "-//W3C//DTD XHTML 1.0 Strict//EN";
    
    private static final String NAMESPACE = "http://www.w3.org/1999/xhtml";
    
    public Program() {
    }
    
    public static void main(final String[] args) throws Exception {
        final DOMImplementationRegistry domImplRegistry = DOMImplementationRegistry.newInstance();
        final DOMImplementation domImpl = domImplRegistry.getDOMImplementation("Core 3.0 +LS 3.0");
        
        final Download download = null;
        final FileParser<Document> documentParser = null;
        
//        documentParser.parse(download);
        final Document domDocument = createDocument(domImpl);
        
        final DOMImplementationLS domImplLS = (DOMImplementationLS) domImpl.getFeature("+LS", "3.0");
        
        final LSSerializer serializer = domImplLS.createLSSerializer();
        serializer.getDomConfig().setParameter("format-pretty-print", true);
        
        final LSOutput output = domImplLS.createLSOutput();
        output.setByteStream(System.out);
        serializer.write(domDocument, output);
        
        System.out.println();
        
        final JFrame jFrame = new JFrame("Organic Browser");
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setLocationByPlatform(true);
        
        final JScrollPane jScrollPane = new JScrollPane();
        jFrame.getContentPane().add(jScrollPane);
        
        final StyleTreeBuilder styleTreeBuilder = new StyleTreeBuilder();
        final StyledElement styledElement = styleTreeBuilder.addStyleInformation(domDocument);
        
        final StylesheetBuilder stylesheetBuilder = new StylesheetBuilder();
        final Collection<Stylesheet> stylesheets = stylesheetBuilder.buildStylesheets(domDocument, URI.create(""));
        
        final PropertyAccessorFactory propertyAccessorFactory = new PropertyAccessorFactory();
        final CascadeApplier cascadeApplier = new CascadeApplier(propertyAccessorFactory);
        final long cascadeStartTime = System.currentTimeMillis();
        for (final Stylesheet stylesheet : stylesheets) {
            cascadeApplier.cascade(styledElement, stylesheet);
        }
        final long cascadeEndTime = System.currentTimeMillis();
        System.out.println("Cascade time: " + (cascadeEndTime - cascadeStartTime));
        
        // debug output of the style information
        final Document styledDocument = domImpl.createDocument("namespace", "Styled", null);
        styledDocument.getDocumentElement().appendChild(styledElement.createDOM(styledDocument));
        serializer.write(styledDocument, output);
        
        @SuppressWarnings("unchecked")
        final PropertyAccessor<Display> displayAccessor =
                (PropertyAccessor<Display>) propertyAccessorFactory.getPropertyAccessor(PropertyName.DISPLAY);
        @SuppressWarnings("unchecked")
        final PropertyAccessor<AbsoluteLength> fontSizeAccessor =
                (PropertyAccessor<AbsoluteLength>) propertyAccessorFactory.getPropertyAccessor(PropertyName.FONT_SIZE);
        @SuppressWarnings("unchecked")
        final PropertyAccessor<ListStylePosition> listStylePositionAccessor =
                (PropertyAccessor<ListStylePosition>) propertyAccessorFactory.getPropertyAccessor(
                        PropertyName.LIST_STYLE_POSITION);
        final Formatter formatter = new Formatter(displayAccessor, fontSizeAccessor, listStylePositionAccessor);
        final BlockLevelBox blockBox = formatter.createBlockBox(styledElement);
        
        // debug output of the formatting information
        final Document formattedDocument = domImpl.createDocument("namespace", "Formatted", null);
        formattedDocument.getDocumentElement().appendChild(blockBox.createDOM(formattedDocument));
        serializer.write(formattedDocument, output);
        
        final BoxRenderer boxRenderer = new BoxRenderer(blockBox);
        
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setViewportView(boxRenderer);
        jScrollPane.setPreferredSize(new Dimension(800, 600));
        jScrollPane.getViewport().addChangeListener(boxRenderer);
        
        // jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        jFrame.pack();
        jFrame.setVisible(true);
    }
    
    private static Document createDocument(final DOMImplementation domImpl) {
        final DocumentType docType = domImpl.createDocumentType("html", PUBLIC_ID, SYSTEM_ID);
        final Document domDocument = domImpl.createDocument(NAMESPACE, "html", docType);
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
