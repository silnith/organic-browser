package org.silnith.browser.organic;

import java.util.Collection;
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
    
    public void cascade(final StyledElement styledElement, final Collection<CSSRule> rules,
            final Collection<CSSPseudoElementRule> pseudoRules) {
        for (final CSSRule cssRule : rules) {
            if (cssRule.shouldApply(styledElement)) {
                cssRule.apply(styledElement);
            }
        }
        
        compute(styledElement);
        
        /*
         * Rules on pseudo-elements must be applied after the parent element is
         * computed, because they require access to the computed values of
         * properties.
         */
        for (final CSSPseudoElementRule pseudoRule : pseudoRules) {
            if (pseudoRule.shouldApply(styledElement)) {
                pseudoRule.apply(styledElement);
            }
        }
        
        for (final StyledContent child : styledElement.getChildren()) {
            if (child instanceof StyledElement) {
                final StyledElement childElement = (StyledElement) child;
                cascade(childElement, rules, pseudoRules);
//			} else if (child instanceof AnonymousStyledElement) {
//				compute(child);
            }
        }
    }
    
    public void compute(final StyledContent styledContent) {
        final StyleData styleData = styledContent.getStyleData();
        // run through all properties and set the computed value from the
        // specified value
        final Set<PropertyName> uncomputedPropertyNames = EnumSet.allOf(PropertyName.class);
        while ( !uncomputedPropertyNames.isEmpty()) {
            final int initialSize = uncomputedPropertyNames.size();
            
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
                if (Collections.disjoint(uncomputedPropertyNames, dependencies)) {
                    iterator.remove();
                    propertyAccessor.computeValue(styleData);
                }
            }
            
            final int finalSize = uncomputedPropertyNames.size();
            
            if (initialSize == finalSize) {
                throw new IllegalStateException("There is a circular dependency among the properties.");
            }
        }
    }
    
}
