package org.silnith.browser.organic;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.silnith.browser.organic.nfa.State;
import org.silnith.browser.organic.nfa.TransitionPredicate;
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
public class TrivialCSSRule2 implements SelfApplyingCSSRule {
    
    private final State<Element> initialState;
    
    {
        initialState = new State<>(false);
        final State<Element> matchedHTMLState = new State<>(false);
        final State<Element> matchedBodyState = new State<>(false);
        final State<Element> matchedPState = new State<>(false);
        final State<Element> matchedEMState = new State<>(true);
        
        initialState.addEdge(new TypeSelectorTransitionPredicate("html"), matchedHTMLState);
        initialState.setNoMatchState(initialState);
        
        matchedHTMLState.addEdge(new TypeSelectorTransitionPredicate("body"), matchedBodyState);
        matchedHTMLState.setNoMatchState(initialState);
        
        final TypeSelectorTransitionPredicate h1Predicate = new TypeSelectorTransitionPredicate("h1");
        final TransitionPredicate<Element> previousElementPredicate = new PreviousElementTransitionPredicate(h1Predicate);
        final TypeSelectorTransitionPredicate pPredicate = new TypeSelectorTransitionPredicate("p");
        final TransitionPredicateGroup<Element> combinationPredicate = new TransitionPredicateGroup<>(Arrays.asList(pPredicate, previousElementPredicate));
        matchedBodyState.addEdge(combinationPredicate, matchedPState);
        matchedBodyState.setNoMatchState(matchedBodyState);
        
        matchedPState.addEdge(new TypeSelectorTransitionPredicate("em"), matchedEMState);
        matchedPState.setNoMatchState(matchedPState);
        
        matchedEMState.addEdge(new TypeSelectorTransitionPredicate("em"), matchedEMState);
        matchedEMState.setNoMatchState(matchedPState);
    }
    
    private final Map<PropertyName, List<Token>> declarationsToApply;
    
    public TrivialCSSRule2(final List<Token> selector, final List<Token> declarationTokens) throws IOException {
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
            applyToElement(styledDOMElement, Collections.singleton(initialState));
        }
    }
    
    private void applyToElement(final StyledDOMElement styledDOMElement, final Set<State<Element>> states) {
        final Element element = styledDOMElement.getElement();
//        final String tagName = element.getTagName();
//        final Attr attrClassNode = element.getAttributeNode("class");
        
        final Set<State<Element>> statesAfterTransition = new HashSet<>();
        for (final State<Element> state : states) {
            statesAfterTransition.addAll(state.getNextState(element));
        }
        
        for (final State<Element> stateAfterTransition : statesAfterTransition) {
            if (stateAfterTransition.isAcceptState()) {
                final StyleData styleData = styledDOMElement.getStyleData();
                applyDeclarationsToNode(styleData);
                break;
            }
        }
        
        final List<StyledContent> children = styledDOMElement.getChildren();
        for (final StyledContent child : children) {
            if (child instanceof StyledDOMElement) {
                final StyledDOMElement childElement = (StyledDOMElement) child;
                applyToElement(childElement, statesAfterTransition);
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
