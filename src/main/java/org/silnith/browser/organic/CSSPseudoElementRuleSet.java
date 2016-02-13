package org.silnith.browser.organic;

import java.util.ArrayList;
import java.util.List;

import org.silnith.browser.organic.property.accessor.DisplayAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.Display;
import org.silnith.css.model.data.PropertyName;


/**
 * A set of CSS rules to apply to a pseudo-element.
 * 
 * @author kent
 */
public class CSSPseudoElementRuleSet {
    
    private final String elementName;
    
    private final String beforeString;
    
    private final List<CSSRule> beforeRules;
    
    private final String afterString;
    
    private final List<CSSRule> afterRules;
    
    private final PropertyAccessorFactory propertyAccessorFactory;
    
    public CSSPseudoElementRuleSet(final String elementName, final String beforeString, final String afterString) {
        this.elementName = elementName;
        this.beforeString = beforeString;
        this.afterString = afterString;
        this.beforeRules = new ArrayList<>();
        this.afterRules = new ArrayList<>();
        this.propertyAccessorFactory = new PropertyAccessorFactory();
    }
    
    public CSSPseudoElementRuleSet(final String elementName, final String beforeString, final String afterString,
            final List<CSSRule> beforeRules, final List<CSSRule> afterRules) {
        this(elementName, beforeString, afterString);
        this.beforeRules.addAll(beforeRules);
        this.afterRules.addAll(afterRules);
    }
    
    public boolean shouldApply(final StyledElement styledElement) {
        final String tagName = styledElement.getTagName();
        
        if (tagName.equalsIgnoreCase(elementName)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Sets the computed values for all CSS properties for the pseudo-elements
     * this rule set applies to with the given element as the parent of the
     * pseudo-elements.  The computed values for the element must already have
     * been set before calling this method.
     * 
     * @param styledElement
     */
    public void apply(final StyledElement styledElement) {
        final DisplayAccessor displayAccessor = new DisplayAccessor();
        if (beforeString != null) {
            // create pseudo element as first child of styledElement
            final StyleData beforeStyleData = StyleData.getAnonymousElementStyle(styledElement.getStyleData());
            final PseudoElement beforeElement = new PseudoElement(styledElement, ":before", beforeStyleData);
            final StyledText beforeContent = new StyledText(beforeElement, beforeString);
            beforeElement.addChild(beforeContent);
            // add style rules to pseudo element
            for (final CSSRule rule : beforeRules) {
                if (rule.shouldApply(beforeElement)) {
                    rule.apply(beforeElement);
                    final PropertyName propertyName = rule.getPropertyName();
                    final PropertyAccessor<?> propertyAccessor =
                            propertyAccessorFactory.getPropertyAccessor(propertyName);
                    propertyAccessor.computeValue(beforeStyleData);
                }
            }
            final Display beforeDisplay = displayAccessor.getComputedValue(beforeElement.getStyleData());
            if (beforeDisplay != Display.NONE) {
                styledElement.setBeforeContent(beforeElement);
            }
        }
        if (afterString != null) {
            // create pseudo element as last child of styledElement
            final StyleData afterStyleData = StyleData.getAnonymousElementStyle(styledElement.getStyleData());
            final PseudoElement afterElement = new PseudoElement(styledElement, ":after", afterStyleData);
            final StyledText afterContent = new StyledText(afterElement, afterString);
            afterElement.addChild(afterContent);
            // add style rules to pseudo element
            for (final CSSRule rule : afterRules) {
                if (rule.shouldApply(afterElement)) {
                    rule.apply(afterElement);
                    final PropertyName propertyName = rule.getPropertyName();
                    final PropertyAccessor<?> propertyAccessor =
                            propertyAccessorFactory.getPropertyAccessor(propertyName);
                    propertyAccessor.computeValue(afterStyleData);
                }
            }
            final Display afterDisplay = displayAccessor.getComputedValue(afterElement.getStyleData());
            if (afterDisplay != Display.NONE) {
                styledElement.setAfterContent(afterElement);
            }
        }
    }
    
    @Override
    public String toString() {
        return "CSSPseudoElementRules {before: " + beforeString + ", after: " + afterString + ", before rules: "
                + beforeRules + ", after rules: " + afterRules + "}";
    }
    
}
