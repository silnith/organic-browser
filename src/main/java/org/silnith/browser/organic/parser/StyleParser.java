package org.silnith.browser.organic.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.silnith.browser.organic.CSSPseudoElementRuleSet;
import org.silnith.browser.organic.CSSRule;
import org.silnith.browser.organic.RawCSSRule;
import org.silnith.browser.organic.network.Download;
import org.silnith.css.model.data.PropertyName;
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

import com.steadystate.css.parser.CSSOMParser;


public class StyleParser {
    
    public StyleParser() {
        super();
    }
    
    public void parseStyleRules(final Download download) throws Exception {
        final String content = download.getContent();
        
        Class.forName(CSSOMParser.class.getName());
        new CSSOMParser();
        
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
    
    public Collection<CSSRule> parseStyleRules(final Object documentText) {
        final Collection<CSSRule> defaultHTMLRules = defaultHTMLRules();
        final List<CSSRule> rules = new ArrayList<>(defaultHTMLRules);
        rules.addAll(createStyleList());
        return rules;
    }
    
    public Collection<CSSPseudoElementRuleSet> parsePseudoElementStyleRules(final Object documentText) {
        return createGeneratedContentRules();
    }
    
    private static Collection<CSSRule> createStyleList() {
        final Collection<CSSRule> cssRules = new ArrayList<>();
        cssRules.add(new RawCSSRule("html", PropertyName.COLOR, "black"));
        cssRules.add(new RawCSSRule("head", PropertyName.FONT_SIZE, "x-small"));
        cssRules.add(new RawCSSRule("body", PropertyName.FONT_SIZE, "12pt"));
        cssRules.add(new RawCSSRule("body", PropertyName.BACKGROUND_COLOR, "silver"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_TOP_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_TOP_COLOR, "blue"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_RIGHT_COLOR, "blue"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_BOTTOM_COLOR, "blue"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_LEFT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_LEFT_COLOR, "blue"));
        cssRules.add(new RawCSSRule("p", PropertyName.FONT_SIZE, "16pt"));
        cssRules.add(new RawCSSRule("p", PropertyName.MARGIN_TOP, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.MARGIN_RIGHT, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.MARGIN_BOTTOM, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.MARGIN_LEFT, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.BACKGROUND_COLOR, "white"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_TOP_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_TOP_COLOR, "lime"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_RIGHT_COLOR, "lime"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_BOTTOM_COLOR, "lime"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_LEFT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_LEFT_COLOR, "lime"));
        cssRules.add(new RawCSSRule("p", PropertyName.PADDING_TOP, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.PADDING_RIGHT, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.PADDING_BOTTOM, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.PADDING_LEFT, "3px"));
        cssRules.add(new RawCSSRule("em", PropertyName.FONT_STYLE, "italic"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_TOP_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_TOP_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_RIGHT_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_BOTTOM_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_LEFT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_LEFT_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("strong", PropertyName.FONT_WEIGHT, "bold"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BACKGROUND_COLOR, "red"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_TOP_WIDTH, "8px"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_TOP_COLOR, "gray"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_RIGHT_WIDTH, "8px"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_RIGHT_COLOR, "gray"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_BOTTOM_WIDTH, "8px"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_BOTTOM_COLOR, "gray"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_LEFT_WIDTH, "8px"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_LEFT_COLOR, "gray"));
        cssRules.add(new RawCSSRule("div", PropertyName.MARGIN_TOP, "2em"));
        cssRules.add(new RawCSSRule("div", PropertyName.MARGIN_RIGHT, "2em"));
        cssRules.add(new RawCSSRule("div", PropertyName.MARGIN_BOTTOM, "2em"));
        cssRules.add(new RawCSSRule("div", PropertyName.MARGIN_LEFT, "2em"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_TOP_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_TOP_COLOR, "red"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_RIGHT_COLOR, "red"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_BOTTOM_COLOR, "red"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_LEFT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_LEFT_COLOR, "red"));
        cssRules.add(new RawCSSRule("div", PropertyName.PADDING_TOP, "6px"));
        cssRules.add(new RawCSSRule("div", PropertyName.PADDING_RIGHT, "6px"));
        cssRules.add(new RawCSSRule("div", PropertyName.PADDING_BOTTOM, "6px"));
        cssRules.add(new RawCSSRule("div", PropertyName.PADDING_LEFT, "6px"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_TOP_WIDTH, "3px"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_TOP_COLOR, "aqua"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_RIGHT_WIDTH, "3px"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_RIGHT_COLOR, "aqua"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_BOTTOM_WIDTH, "3px"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_BOTTOM_COLOR, "aqua"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_LEFT_WIDTH, "3px"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_LEFT_COLOR, "aqua"));
        cssRules.add(new RawCSSRule("a", PropertyName.PADDING_TOP, "1px"));
        cssRules.add(new RawCSSRule("a", PropertyName.PADDING_RIGHT, "1px"));
        cssRules.add(new RawCSSRule("a", PropertyName.PADDING_BOTTOM, "1px"));
        cssRules.add(new RawCSSRule("a", PropertyName.PADDING_LEFT, "1px"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_TOP_STYLE, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_TOP_WIDTH, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_TOP_COLOR, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_RIGHT_STYLE, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_RIGHT_WIDTH, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_RIGHT_COLOR, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_BOTTOM_STYLE, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_BOTTOM_WIDTH, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_BOTTOM_COLOR, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_LEFT_STYLE, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_LEFT_WIDTH, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_LEFT_COLOR, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.PADDING_TOP, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.PADDING_RIGHT, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.PADDING_BOTTOM, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.PADDING_LEFT, "inherit"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_TOP_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_TOP_COLOR, "black"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_RIGHT_COLOR, "black"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_BOTTOM_COLOR, "black"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_LEFT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_LEFT_COLOR, "black"));
        cssRules.add(new RawCSSRule("ol", PropertyName.PADDING_TOP, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.PADDING_RIGHT, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.PADDING_BOTTOM, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.PADDING_LEFT, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_TOP_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_TOP_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_RIGHT_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_BOTTOM_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_LEFT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_LEFT_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("ul", PropertyName.PADDING_TOP, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.PADDING_RIGHT, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.PADDING_BOTTOM, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.PADDING_LEFT, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.LIST_STYLE_POSITION, "inside"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_TOP_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_TOP_COLOR, "orange"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_RIGHT_COLOR, "orange"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_BOTTOM_COLOR, "orange"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_LEFT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_LEFT_COLOR, "orange"));
        cssRules.add(new RawCSSRule("li", PropertyName.PADDING_TOP, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.PADDING_RIGHT, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.PADDING_BOTTOM, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.PADDING_LEFT, "5px"));
        
        return cssRules;
    }
    
    private static Collection<CSSPseudoElementRuleSet> createGeneratedContentRules() {
        final ArrayList<CSSPseudoElementRuleSet> pseudoRules = new ArrayList<>();
        final ArrayList<CSSRule> beforeRules = new ArrayList<>();
        final ArrayList<CSSRule> afterRules = new ArrayList<>();
        
        pseudoRules.add(new CSSPseudoElementRuleSet("div", "Div: ", null));
        beforeRules.clear();
        afterRules.clear();
        afterRules.add(new RawCSSRule(":after", PropertyName.DISPLAY, "none"));
        afterRules.add(new RawCSSRule(":after", PropertyName.BACKGROUND_COLOR, "aqua"));
        afterRules.add(new RawCSSRule(":after", PropertyName.BORDER_TOP_STYLE, "solid"));
        afterRules.add(new RawCSSRule(":after", PropertyName.COLOR, "red"));
        pseudoRules.add(new CSSPseudoElementRuleSet("p", null, "yomama", beforeRules, afterRules));
        
        return pseudoRules;
    }
    
    private static Collection<CSSRule> defaultHTMLRules() {
        final List<CSSRule> rules = new ArrayList<>();
        
        rules.add(new RawCSSRule("html", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("address", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("blockquote", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("body", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("dd", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("div", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("dl", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("dt", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("fieldset", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("form", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("frame", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("frameset", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h1", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h2", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h3", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h4", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h5", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h6", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("noframes", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("ol", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("p", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("ul", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("center", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("dir", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("hr", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("menu", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("pre", PropertyName.DISPLAY, "block"));
        
        rules.add(new RawCSSRule("html", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("address", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("blockquote", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("body", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("dd", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("div", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("dl", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("dt", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("fieldset", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("form", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("frame", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("frameset", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h1", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h2", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h3", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h4", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h5", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h6", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("noframes", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("ol", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("p", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("ul", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("center", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("dir", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("hr", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("menu", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("pre", PropertyName.UNICODE_BIDI, "embed"));
        
        rules.add(new RawCSSRule("li", PropertyName.DISPLAY, "list-item"));
        rules.add(new RawCSSRule("head", PropertyName.DISPLAY, "none"));
        rules.add(new RawCSSRule("table", PropertyName.DISPLAY, "table"));
        rules.add(new RawCSSRule("tr", PropertyName.DISPLAY, "table-row"));
        rules.add(new RawCSSRule("thead", PropertyName.DISPLAY, "table-header-group"));
        rules.add(new RawCSSRule("tbody", PropertyName.DISPLAY, "table-row-group"));
        rules.add(new RawCSSRule("tfoot", PropertyName.DISPLAY, "table-footer-group"));
        rules.add(new RawCSSRule("col", PropertyName.DISPLAY, "table-column"));
        rules.add(new RawCSSRule("colgroup", PropertyName.DISPLAY, "table-column-group"));
        rules.add(new RawCSSRule("td", PropertyName.DISPLAY, "table-cell"));
        rules.add(new RawCSSRule("th", PropertyName.DISPLAY, "table-cell"));
        rules.add(new RawCSSRule("caption", PropertyName.DISPLAY, "table-caption"));
        rules.add(new RawCSSRule("th", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("th", PropertyName.TEXT_ALIGN, "center"));
        rules.add(new RawCSSRule("caption", PropertyName.TEXT_ALIGN, "center"));
        
        rules.add(new RawCSSRule("body", PropertyName.MARGIN_TOP, "8px"));
        rules.add(new RawCSSRule("body", PropertyName.MARGIN_RIGHT, "8px"));
        rules.add(new RawCSSRule("body", PropertyName.MARGIN_BOTTOM, "8px"));
        rules.add(new RawCSSRule("body", PropertyName.MARGIN_LEFT, "8px"));
        
        rules.add(new RawCSSRule("h1", PropertyName.FONT_SIZE, "2em"));
        rules.add(new RawCSSRule("h1", PropertyName.MARGIN_TOP, ".67em"));
        rules.add(new RawCSSRule("h1", PropertyName.MARGIN_BOTTOM, ".67em"));
        
        rules.add(new RawCSSRule("h2", PropertyName.FONT_SIZE, "1.5em"));
        rules.add(new RawCSSRule("h2", PropertyName.MARGIN_TOP, ".75em"));
        rules.add(new RawCSSRule("h2", PropertyName.MARGIN_BOTTOM, ".75em"));
        
        rules.add(new RawCSSRule("h3", PropertyName.FONT_SIZE, "1.17em"));
        rules.add(new RawCSSRule("h3", PropertyName.MARGIN_TOP, ".83em"));
        rules.add(new RawCSSRule("h3", PropertyName.MARGIN_BOTTOM, ".83em"));
        
        rules.add(new RawCSSRule("h4", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("h4", PropertyName.MARGIN_BOTTOM, "1.12em"));
        
        rules.add(new RawCSSRule("p", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("p", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("blockquote", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("blockquote", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("ul", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("ul", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("fieldset", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("fieldset", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("form", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("form", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("ol", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("ol", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("dl", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("dl", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("dir", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("dir", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("menu", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("menu", PropertyName.MARGIN_BOTTOM, "1.12em"));
        
        rules.add(new RawCSSRule("h5", PropertyName.FONT_SIZE, ".83em"));
        rules.add(new RawCSSRule("h5", PropertyName.MARGIN_TOP, "1.5em"));
        rules.add(new RawCSSRule("h5", PropertyName.MARGIN_BOTTOM, "1.5em"));
        
        rules.add(new RawCSSRule("h6", PropertyName.FONT_SIZE, ".75em"));
        rules.add(new RawCSSRule("h6", PropertyName.MARGIN_TOP, "1.67em"));
        rules.add(new RawCSSRule("h6", PropertyName.MARGIN_BOTTOM, "1.67em"));
        
        rules.add(new RawCSSRule("h1", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("h2", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("h3", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("h4", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("h5", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("h6", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("b", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("strong", PropertyName.FONT_WEIGHT, "bolder"));
        
        rules.add(new RawCSSRule("blockquote", PropertyName.MARGIN_RIGHT, "40px"));
        rules.add(new RawCSSRule("blockquote", PropertyName.MARGIN_LEFT, "40px"));
        
        rules.add(new RawCSSRule("i", PropertyName.FONT_STYLE, "italic"));
        rules.add(new RawCSSRule("cite", PropertyName.FONT_STYLE, "italic"));
        rules.add(new RawCSSRule("em", PropertyName.FONT_STYLE, "italic"));
        rules.add(new RawCSSRule("var", PropertyName.FONT_STYLE, "italic"));
        rules.add(new RawCSSRule("address", PropertyName.FONT_STYLE, "italic"));
        
        rules.add(new RawCSSRule("pre", PropertyName.FONT_FAMILY, "monospace"));
        rules.add(new RawCSSRule("tt", PropertyName.FONT_FAMILY, "monospace"));
        rules.add(new RawCSSRule("code", PropertyName.FONT_FAMILY, "monospace"));
        rules.add(new RawCSSRule("kbd", PropertyName.FONT_FAMILY, "monospace"));
        rules.add(new RawCSSRule("samp", PropertyName.FONT_FAMILY, "monospace"));
        
        rules.add(new RawCSSRule("pre", PropertyName.WHITE_SPACE, "pre"));
        
        rules.add(new RawCSSRule("button", PropertyName.DISPLAY, "inline-block"));
        rules.add(new RawCSSRule("textarea", PropertyName.DISPLAY, "inline-block"));
        rules.add(new RawCSSRule("input", PropertyName.DISPLAY, "inline-block"));
        rules.add(new RawCSSRule("select", PropertyName.DISPLAY, "inline-block"));
        
        rules.add(new RawCSSRule("big", PropertyName.FONT_SIZE, "1.17em"));
        rules.add(new RawCSSRule("small", PropertyName.FONT_SIZE, ".83em"));
        rules.add(new RawCSSRule("sub", PropertyName.FONT_SIZE, ".83em"));
        rules.add(new RawCSSRule("sup", PropertyName.FONT_SIZE, ".83em"));
        rules.add(new RawCSSRule("sub", PropertyName.VERTICAL_ALIGN, "sub"));
        rules.add(new RawCSSRule("sup", PropertyName.VERTICAL_ALIGN, "super"));
        
        rules.add(new RawCSSRule("table", PropertyName.BORDER_SPACING, "2px"));
        rules.add(new RawCSSRule("thead", PropertyName.VERTICAL_ALIGN, "middle"));
        rules.add(new RawCSSRule("tbody", PropertyName.VERTICAL_ALIGN, "middle"));
        rules.add(new RawCSSRule("tfoot", PropertyName.VERTICAL_ALIGN, "middle"));
        rules.add(new RawCSSRule("td", PropertyName.VERTICAL_ALIGN, "inherit"));
        rules.add(new RawCSSRule("th", PropertyName.VERTICAL_ALIGN, "inherit"));
        rules.add(new RawCSSRule("tr", PropertyName.VERTICAL_ALIGN, "inherit"));
        
        rules.add(new RawCSSRule("s", PropertyName.TEXT_DECORATION, "line-through"));
        rules.add(new RawCSSRule("strike", PropertyName.TEXT_DECORATION, "line-through"));
        rules.add(new RawCSSRule("del", PropertyName.TEXT_DECORATION, "line-through"));
        
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_TOP_WIDTH, "1px"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_RIGHT_WIDTH, "1px"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_BOTTOM_WIDTH, "1px"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_LEFT_WIDTH, "1px"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_TOP_STYLE, "inset"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_RIGHT_STYLE, "inset"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_BOTTOM_STYLE, "inset"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_LEFT_STYLE, "inset"));
        
//		rules.add(new CSSRule("ol", PropertyName.MARGIN_LEFT, "40px"));
//		rules.add(new CSSRule("ul", PropertyName.MARGIN_LEFT, "40px"));
        rules.add(new RawCSSRule("dir", PropertyName.MARGIN_LEFT, "40px"));
        rules.add(new RawCSSRule("menu", PropertyName.MARGIN_LEFT, "40px"));
        rules.add(new RawCSSRule("dd", PropertyName.MARGIN_LEFT, "40px"));
        
        rules.add(new RawCSSRule("ol", PropertyName.LIST_STYLE_TYPE, "decimal"));
        
        rules.add(new RawCSSRule("u", PropertyName.TEXT_DECORATION, "underline"));
        rules.add(new RawCSSRule("ins", PropertyName.TEXT_DECORATION, "underline"));
        
        rules.add(new RawCSSRule("br:before", PropertyName.CONTENT, "\\A"));
        rules.add(new RawCSSRule("br:before", PropertyName.WHITE_SPACE, "pre-line"));
        
        rules.add(new RawCSSRule("center", PropertyName.TEXT_ALIGN, "center"));
        
        rules.add(new RawCSSRule(":link", PropertyName.TEXT_DECORATION, "underline"));
        rules.add(new RawCSSRule(":visited", PropertyName.TEXT_DECORATION, "underline"));
        
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_TOP_WIDTH, "thin"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_RIGHT_WIDTH, "thin"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_BOTTOM_WIDTH, "thin"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_LEFT_WIDTH, "thin"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_TOP_STYLE, "dotted"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_RIGHT_STYLE, "dotted"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_BOTTOM_STYLE, "dotted"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_LEFT_STYLE, "dotted"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_TOP_COLOR, "invert"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_RIGHT_COLOR, "invert"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_BOTTOM_COLOR, "invert"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_LEFT_COLOR, "invert"));
        
        return rules;
    }
    
}
