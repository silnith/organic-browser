package org.silnith.browser.organic;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.PropertyName;


/**
 * Performs the CSS cascade to apply stylesheet rules to a styled document.
 * <p>
 * This class is thread-safe and re-entrant. However, the {@link StyledElement}
 * passed to it may not be. A styled element must be used by at most one cascade
 * applier, but a cascade applier may be run on as many styled elements as
 * desired.
 * 
 * @author kent
 */
public class CascadeApplier {
    
    private final PropertyAccessorFactory propertyAccessorFactory;
    
    public CascadeApplier(final PropertyAccessorFactory propertyAccessorFactory) {
        this.propertyAccessorFactory = propertyAccessorFactory;
    }
    
    /**
     * The main entry point to apply Cascaded Style Sheet rules.  This applies
     * the given CSS rules to the provided element, and then recursively applies
     * the rules to all children.
     * 
     * @param styledElement
     */
    public void cascade(final StyledElement styledElement) {
        compute(styledElement);
        
        /*
         * Rules on pseudo-elements must be applied after the parent element is
         * computed, because they require access to the computed values of
         * properties.
         */
//        for (final CSSPseudoElementRuleSet pseudoRuleSet : stylesheet.getPseudoRules()) {
//            if (pseudoRuleSet.shouldApply(styledElement)) {
//                pseudoRuleSet.apply(styledElement);
//            }
//        }
        
        for (final StyledContent child : styledElement.getChildren()) {
            if (child instanceof StyledElement) {
                final StyledElement childElement = (StyledElement) child;
                cascade(childElement);
            }
        }
    }
    
    /**
     * Sets the computed value for every CSS property on the provided styled
     * content.  Typically this content is an element, but the logic applies
     * more generally.  All specified values must have been set prior to calling
     * this method.
     * 
     * @param styledContent
     */
    public void compute(final StyledContent styledContent) {
        final StyleData styleData = styledContent.getStyleData();
        // run through all properties and set the computed value from the
        // specified value
        final Set<PropertyName> uncomputedPropertyNames = EnumSet.allOf(PropertyName.class);
        while ( !uncomputedPropertyNames.isEmpty()) {
            final int initialSize = uncomputedPropertyNames.size();
            
            assert initialSize > 0;
            
            final Iterator<PropertyName> iterator = uncomputedPropertyNames.iterator();
            while (iterator.hasNext()) {
                final PropertyName propertyName = iterator.next();
                // process this mofo
                final PropertyAccessor<?> propertyAccessor = propertyAccessorFactory.getPropertyAccessor(propertyName);
                
                if (propertyAccessor == null) {
                    // TODO: This is temporary until all accessors are defined.
                    iterator.remove();
                    continue;
                }
                
                final Set<PropertyName> dependencies = propertyAccessor.getDependencies();
                // disjoint will iterate over the second parameter, so we want that to be the smaller collection
                if (Collections.disjoint(uncomputedPropertyNames, dependencies)) {
                    iterator.remove();
                    propertyAccessor.computeParsedValue(styleData);
                }
            }
            
            final int finalSize = uncomputedPropertyNames.size();
            
            if (initialSize == finalSize) {
                throw new IllegalStateException("There is a circular dependency among the properties.");
            }
        }
    }
    
}
