package org.silnith.browser.organic;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


/**
 * Creates a decorated document tree for a DOM that provides styling
 * information.
 * <p>
 * This class is thread-safe and re-entrant.
 * 
 * @author kent
 */
public class StyleTreeBuilder {
    
    public StyleTreeBuilder() {
    }
    
    public StyledDOMElement addStyleInformation(final Document document) {
        final Element element = document.getDocumentElement();
        final StyledDOMElement styledElement = createStyleTree(null, element);
        
        return styledElement;
    }
    
    private StyledDOMElement createStyleTree(final StyledDOMElement parent, final Element element) {
        final StyleData parentStyleData;
        if (parent == null) {
            parentStyleData = null;
        } else {
            parentStyleData = parent.getStyleData();
        }
        final StyleData styleData = new StyleData(parentStyleData);
        final StyledDOMElement styledElement = new StyledDOMElement(parent, element, styleData);
        
        Node child = element.getFirstChild();
        while (child != null) {
            switch (child.getNodeType()) {
            case Node.ELEMENT_NODE: {
                final Element childElement = (Element) child;
                
                final StyledElement childStyledElement = createStyleTree(styledElement, childElement);
                styledElement.addChild(childStyledElement);
            }
                break;
            case Node.TEXT_NODE: {
                final Text childText = (Text) child;
                
                final StyledText styledText = new StyledText(styledElement, childText.getData());
                styledElement.addChild(styledText);
            }
                break;
            case Node.CDATA_SECTION_NODE: {
                final CDATASection childCDATASection = (CDATASection) child;
                
                final StyledText styledText = new StyledText(styledElement, childCDATASection.getData());
                styledElement.addChild(styledText);
            }
                break;
            default: {
            }
                break;
            }
            child = child.getNextSibling();
        }
        
        return styledElement;
    }
    
}
