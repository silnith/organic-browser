package org.silnith.browser.organic;

import java.util.EnumMap;
import java.util.Map;

import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.PropertyName;


public class StyleData {
    
    public static StyleData getAnonymousElementStyle(final StyleData parentElementStyle) {
        final StyleData styleData = new StyleData(parentElementStyle);
        final PropertyAccessorFactory factory = PropertyAccessorFactory.getInstance();
        for (final PropertyName propertyName : PropertyName.values()) {
            final PropertyAccessor<?> propertyAccessor = factory.getPropertyAccessor(propertyName);
            if (propertyAccessor == null) {
                continue;
            }
            
            if (propertyAccessor.isInheritedByDefault()) {
                styleData.setComputedValue(propertyName, propertyAccessor.getComputedValue(parentElementStyle));
            } else {
                styleData.setComputedValue(propertyName, propertyAccessor.getInitialValue(parentElementStyle));
            }
        }
        return styleData;
    }
    
    public static StyleData getPseudoElementStyle(final StyleData parentElementStyle) {
        final StyleData styleData = new StyleData(parentElementStyle);
        final PropertyAccessorFactory factory = PropertyAccessorFactory.getInstance();
        for (final PropertyName propertyName : PropertyName.values()) {
            final PropertyAccessor<?> propertyAccessor = factory.getPropertyAccessor(propertyName);
            if (propertyAccessor == null) {
                continue;
            }
            
            if (propertyAccessor.isInheritedByDefault()) {
                styleData.setComputedValue(propertyName, propertyAccessor.getComputedValue(parentElementStyle));
            } else {
                styleData.setComputedValue(propertyName, propertyAccessor.getInitialValue(parentElementStyle));
            }
        }
        return styleData;
    }
    
    private final StyleData parentStyleData;
    
    private final Map<PropertyName, Boolean> propertyInherited;
    
    private final Map<PropertyName, Boolean> propertySpecified;
    
    private final Map<PropertyName, String> specifiedValues;
    
    private final Map<PropertyName, Boolean> propertyComputed;
    
    private final Map<PropertyName, Object> computedValue;
    
    public StyleData(final StyleData parentStyleData) {
        super();
        this.parentStyleData = parentStyleData;
        
        this.propertyInherited = new EnumMap<>(PropertyName.class);
        this.propertySpecified = new EnumMap<>(PropertyName.class);
        this.specifiedValues = new EnumMap<>(PropertyName.class);
        this.propertyComputed = new EnumMap<>(PropertyName.class);
        this.computedValue = new EnumMap<>(PropertyName.class);
        
        for (final PropertyName propertyName : PropertyName.values()) {
            this.propertySpecified.put(propertyName, false);
            this.propertyComputed.put(propertyName, false);
        }
    }
    
    public StyleData getParentStyleData() {
        return parentStyleData;
    }
    
    /**
     * Returns {@code true} if the property is specified and the value is the
     * string <kbd>"inherit"</kbd>.
     * 
     * @param propertyName
     * @return
     */
    public boolean getInherit(final PropertyName propertyName) {
        if ( !isPropertySpecified(propertyName)) {
            throw new NullPointerException();
        }
        return propertyInherited.get(propertyName);
    }
    
    public boolean isPropertySpecified(final PropertyName propertyName) {
        return propertySpecified.get(propertyName);
    }
    
    public void setSpecifiedValue(final PropertyName propertyName, final String propertyValue) {
        propertySpecified.put(propertyName, true);
        specifiedValues.put(propertyName, propertyValue);
        propertyInherited.put(propertyName, "inherit".equals(propertyValue));
    }
    
    public String getSpecifiedValue(final PropertyName propertyName) {
        if ( !isPropertySpecified(propertyName)) {
            throw new NullPointerException();
        }
        return specifiedValues.get(propertyName);
    }
    
    public boolean isPropertyComputed(final PropertyName propertyName) {
        return propertyComputed.get(propertyName);
    }
    
    public void setComputedValue(final PropertyName propertyName, final Object value) {
        propertyComputed.put(propertyName, true);
        computedValue.put(propertyName, value);
    }
    
    public Object getComputedValue(final PropertyName propertyName) {
        if ( !isPropertyComputed(propertyName)) {
            throw new NullPointerException();
        }
        return computedValue.get(propertyName);
    }
    
    @Override
    public String toString() {
        return "StyleData {specifiedValues: " + specifiedValues + ", computedValues: " + computedValue + "}";
    }
    
}
