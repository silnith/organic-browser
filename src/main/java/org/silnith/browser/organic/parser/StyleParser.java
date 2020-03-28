package org.silnith.browser.organic.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import org.silnith.browser.organic.CSSPseudoElementRuleSet;
import org.silnith.browser.organic.CSSRule;
import org.silnith.browser.organic.network.Download;
import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.css.sac.helpers.ParserFactory;


public class StyleParser {
    
    public StyleParser() {
        super();
    }
    
    public void parseStyleRules(final Download download) throws Exception {
        final String content = download.getContent();
        
        final ParserFactory factory = new ParserFactory();
        
        final Parser parser = factory.makeParser();
        
        final SelectorList selectorList = parser.parseSelectors(new InputSource(new StringReader(content)));
        
        for (int i = 0; i < selectorList.getLength(); i++) {
            final Selector selector = selectorList.item(i);
            
            switch (selector.getSelectorType()) {
            case Selector.SAC_CONDITIONAL_SELECTOR: {
                final ConditionalSelector conditionalSelector = (ConditionalSelector) selector;
                conditionalSelector.getSimpleSelector();
                conditionalSelector.getCondition();
            } break;
            case Selector.SAC_ANY_NODE_SELECTOR: {
                final SimpleSelector simpleSelector = (SimpleSelector) selector;
            } break;
            case Selector.SAC_ROOT_NODE_SELECTOR: {
                final SimpleSelector simpleSelector = (SimpleSelector) selector;
            } break;
            case Selector.SAC_NEGATIVE_SELECTOR: {
                final NegativeSelector negativeSelector = (NegativeSelector) selector;
                negativeSelector.getSimpleSelector();
            } break;
            case Selector.SAC_ELEMENT_NODE_SELECTOR: {
                final ElementSelector elementSelector = (ElementSelector) selector;
                elementSelector.getNamespaceURI();
                elementSelector.getLocalName();
            } break;
            case Selector.SAC_TEXT_NODE_SELECTOR: {
                final CharacterDataSelector characterDataSelector = (CharacterDataSelector) selector;
                characterDataSelector.getData();
            } break;
            case Selector.SAC_CDATA_SECTION_NODE_SELECTOR: {
                final CharacterDataSelector characterDataSelector = (CharacterDataSelector) selector;
                characterDataSelector.getData();
            } break;
            case Selector.SAC_PROCESSING_INSTRUCTION_NODE_SELECTOR: {
                final ProcessingInstructionSelector processingInstructionSelector = (ProcessingInstructionSelector) selector;
                processingInstructionSelector.getTarget();
                processingInstructionSelector.getData();
            } break;
            case Selector.SAC_COMMENT_NODE_SELECTOR: {
                final CharacterDataSelector characterDataSelector = (CharacterDataSelector) selector;
                characterDataSelector.getData();
            } break;
            case Selector.SAC_PSEUDO_ELEMENT_SELECTOR: {
                final ElementSelector elementSelector = (ElementSelector) selector;
                elementSelector.getNamespaceURI();
                elementSelector.getLocalName();
            } break;
            case Selector.SAC_DESCENDANT_SELECTOR: {
                final DescendantSelector descendantSelector = (DescendantSelector) selector;
                descendantSelector.getAncestorSelector();
                descendantSelector.getSimpleSelector();
            } break;
            case Selector.SAC_CHILD_SELECTOR: {
                final DescendantSelector descendantSelector = (DescendantSelector) selector;
                descendantSelector.getAncestorSelector();
                descendantSelector.getSimpleSelector();
            } break;
            case Selector.SAC_DIRECT_ADJACENT_SELECTOR: {
                final SiblingSelector siblingSelector = (SiblingSelector) selector;
                siblingSelector.getSelector();
                siblingSelector.getSiblingSelector();
                siblingSelector.getNodeType();
            } break;
            }
        }
    }
    
    public Collection<CSSPseudoElementRuleSet> parsePseudoElementStyleRules(final Object documentText) {
        return createGeneratedContentRules();
    }
    
    private static Collection<CSSPseudoElementRuleSet> createGeneratedContentRules() {
        final ArrayList<CSSPseudoElementRuleSet> pseudoRules = new ArrayList<>();
        final ArrayList<CSSRule> beforeRules = new ArrayList<>();
        final ArrayList<CSSRule> afterRules = new ArrayList<>();
        
        pseudoRules.add(new CSSPseudoElementRuleSet("div", "Div: ", null));
        beforeRules.clear();
        afterRules.clear();
//        afterRules.add(new RawCSSRule(":after", PropertyName.DISPLAY, "none"));
//        afterRules.add(new RawCSSRule(":after", PropertyName.BACKGROUND_COLOR, "aqua"));
//        afterRules.add(new RawCSSRule(":after", PropertyName.BORDER_TOP_STYLE, "solid"));
//        afterRules.add(new RawCSSRule(":after", PropertyName.COLOR, "red"));
        pseudoRules.add(new CSSPseudoElementRuleSet("p", null, "yomama", beforeRules, afterRules));
        
        return pseudoRules;
    }
    
}
