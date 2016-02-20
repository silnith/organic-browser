package org.silnith.browser.organic;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.grammar.token.Declaration;
import org.silnith.browser.organic.parser.css3.grammar.token.DeclarationNode;
import org.silnith.browser.organic.parser.css3.lexical.TokenListStream;
import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Applies the arbitrary selector "html > body h1 + p em, strong, a[href]".
 * <p>
 * This should include three state machines.
 * <ol>
 *   <li>html > body h1 + p em
 *   <li>strong
 *   <li>a[href]
 * </ol>
 * 
 * @author kent
 */
public class TrivialCSSRule implements SelfApplyingCSSRule {
    
    private enum State {
        STATE_1(false) {
            
            @Override
            State transition(Element element) {
                if (element.getTagName().equals("html")) {
                    return STATE_2;
                } else {
                    return STATE_1;
                }
            }
            
        },
        STATE_2(false) {
            
            @Override
            State transition(Element element) {
                if (element.getTagName().equals("body")) {
                    return STATE_3;
                } else {
                    return STATE_1;
                }
            }
            
        },
        STATE_3(false) {
            
            @Override
            State transition(Element element) {
                Node previousSibling = element.getPreviousSibling();
                while (previousSibling != null && previousSibling.getNodeType() != Node.ELEMENT_NODE) {
                    previousSibling = previousSibling.getPreviousSibling();
                }
                final Element previousElement;
                if (previousSibling == null) {
                    previousElement = null;
                } else {
                    previousElement = (Element) previousSibling;
                }
                if (element.getTagName().equals("p") && previousElement != null && previousElement.getTagName().equals("h1")) {
                    return STATE_4;
                } else {
                    return STATE_3;
                }
            }
            
        },
        STATE_4(false) {
            
            @Override
            State transition(Element element) {
                if (element.getTagName().equals("em")) {
                    return STATE_5;
                } else {
                    return STATE_4;
                }
            }
            
        },
        STATE_5(true) {
            
            @Override
            State transition(Element element) {
                return STATE_6;
            }
            
        },
        STATE_6(false) {
            
            @Override
            State transition(Element element) {
                return STATE_6;
            }
            
        };
        
        private final boolean acceptState;
        
        private State(final boolean isAcceptState) {
            this.acceptState = isAcceptState;
        }
        
        abstract State transition(Element element);
        
        public boolean isAcceptState() {
            return acceptState;
        }
        
    }
    
    private final Map<PropertyName, List<Token>> declarationsToApply;
    
    public TrivialCSSRule(final List<Token> selector, final List<Token> declarationTokens) throws IOException {
        super();
        this.declarationsToApply = new EnumMap<>(PropertyName.class);
        
        final Parser declarationParser = new Parser(new TokenListStream(declarationTokens));
        declarationParser.prime();
        final List<Declaration> declarations = declarationParser.parseListOfDeclarations();
        
        for (final Declaration declaration : declarations) {
            if (declaration instanceof DeclarationNode) {
                final DeclarationNode declarationNode = (DeclarationNode) declaration;
                
                final String name = declarationNode.getName();
                final List<Token> value = declarationNode.getValue();
                
                // for each declaration, invoke a property-specific parse
                final PropertyName propertyName = PropertyName.getPropertyName(name);
                
                this.declarationsToApply.put(propertyName, value);
            }
        }
    }
    
    @Override
    public void applyToDocument(StyledContent styledContent) {
        if (styledContent instanceof StyledDOMElement) {
            final StyledDOMElement styledDOMElement = (StyledDOMElement) styledContent;
            applyToElement(styledDOMElement, State.STATE_1);
        }
    }
    
    private void applyToElement(final StyledDOMElement styledDOMElement, final State state) {
        final Element element = styledDOMElement.getElement();
//        final String tagName = element.getTagName();
//        final Attr attrClassNode = element.getAttributeNode("class");
        
        final State stateAfterTransition = state.transition(element);
        
        if (stateAfterTransition.isAcceptState()) {
            final StyleData styleData = styledDOMElement.getStyleData();
            applyDeclarationsToNode(styleData);
        }
        
        final List<StyledContent> children = styledDOMElement.getChildren();
        for (final StyledContent child : children) {
            if (child instanceof StyledDOMElement) {
                final StyledDOMElement childElement = (StyledDOMElement) child;
                applyToElement(childElement, stateAfterTransition);
            }
        }
    }

    private void applyDeclarationsToNode(final StyleData styleData) {
        for (final Map.Entry<PropertyName, List<Token>> entry : declarationsToApply.entrySet()) {
            final PropertyName propertyName = entry.getKey();
            final List<Token> propertyValue = entry.getValue();
            
            styleData.setParsedSpecifiedValue(propertyName, propertyValue);
        }
    }
    
}
