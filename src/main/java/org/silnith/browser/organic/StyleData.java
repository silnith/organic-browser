package org.silnith.browser.organic;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.PropertyName;


/**
 * A container for the styling information associated with a Node.  This is just
 * a dumb container for values computed by other classes.
 * 
 * @author kent
 */
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
    private final Map<PropertyName, List<Token>> parsedSpecifiedValues;
    
    /**
     * A map that records which properties have been computed and which have not.
     */
    private final Map<PropertyName, Boolean> propertyComputed;
    
    private final Map<PropertyName, Object> computedValue;
    
    public StyleData(final StyleData parentStyleData) {
        super();
        this.parentStyleData = parentStyleData;
        
        this.propertyInherited = new EnumMap<>(PropertyName.class);
        this.propertySpecified = new EnumMap<>(PropertyName.class);
        this.specifiedValues = new EnumMap<>(PropertyName.class);
        this.parsedSpecifiedValues = new EnumMap<>(PropertyName.class);
        this.propertyComputed = new EnumMap<>(PropertyName.class);
        this.computedValue = new EnumMap<>(PropertyName.class);
        
        for (final PropertyName propertyName : PropertyName.values()) {
            this.propertySpecified.put(propertyName, false);
            this.propertyComputed.put(propertyName, false);
        }
    }
    
    /**
     * Returns the style data from which this style data inherits.  May be {@code null}.
     * 
     * @return
     */
    public StyleData getParentStyleData() {
        return parentStyleData;
    }
    
    /**
     * Returns {@code true} if the property is specified and the value is the
     * string <kbd>"inherit"</kbd>.
     * 
     * @param propertyName the name of the property to check
     * @return {@code true} if the named property is inherited in this styling data
     */
    public boolean getInherit(final PropertyName propertyName) {
        if ( !isPropertySpecified(propertyName)) {
            throw new IllegalStateException();
        }
        return propertyInherited.get(propertyName);
    }
    
    /**
     * Returns whether the named property has been specified explicitly in this
     * style data.
     * 
     * @param propertyName
     * @return
     */
    public boolean isPropertySpecified(final PropertyName propertyName) {
        return propertySpecified.get(propertyName);
    }
    
    /**
     * Specifies a value for the given property in this style data.  This marks
     * the property as being specified, records the specified value, and checks
     * whether the specified value is the special value "inherit".
     * 
     * @param propertyName
     * @param propertyValue
     */
    public void setSpecifiedValue(final PropertyName propertyName, final String propertyValue) {
        propertySpecified.put(propertyName, true);
        specifiedValues.put(propertyName, propertyValue);
        propertyInherited.put(propertyName, "inherit".equals(propertyValue));
    }

    /**
     * Specifies a value for the given property in this style data.  This marks
     * the property as being specified, records the specified value, and checks
     * whether the specified value is the special value "inherit".
     * 
     * @param propertyName
     * @param propertyValue
     */
    public void setParsedSpecifiedValue(final PropertyName propertyName, final List<Token> propertyValue) {
        propertySpecified.put(propertyName, true);
        parsedSpecifiedValues.put(propertyName, propertyValue);
        propertyInherited.put(propertyName, "inherit".equals(propertyValue));
    }
    
    /**
     * Returns the specified value for the given property.  If the property is
     * not specified, this throws a runtime exception.
     * 
     * @param propertyName
     * @return
     */
    public String getSpecifiedValue(final PropertyName propertyName) {
        if ( !isPropertySpecified(propertyName)) {
            throw new IllegalStateException();
        }
        return specifiedValues.get(propertyName);
    }

    /**
     * Returns the specified value for the given property.  If the property is
     * not specified, this throws a runtime exception.
     * 
     * @param propertyName
     * @return
     */
    public List<Token> getParsedSpecifiedValue(final PropertyName propertyName) {
        if ( !isPropertySpecified(propertyName)) {
            throw new IllegalStateException();
        }
        return parsedSpecifiedValues.get(propertyName);
    }
    
    /**
     * Returns whether the property has been computed yet.
     * 
     * @param propertyName
     * @return
     */
    public boolean isPropertyComputed(final PropertyName propertyName) {
        return propertyComputed.get(propertyName);
    }
    
    /**
     * Sets the computed value.  The type of the value will be whatever type is
     * associated with the {@link PropertyAccessor} for the given property.
     * 
     * @param propertyName
     * @param value
     */
    public void setComputedValue(final PropertyName propertyName, final Object value) {
        propertyComputed.put(propertyName, true);
        computedValue.put(propertyName, value);
    }
    
    /**
     * Returns the computed property value.  If the property value has not been
     * computed yet, throws a runtime exception.
     * 
     * @param propertyName
     * @return
     */
    public Object getComputedValue(final PropertyName propertyName) {
        if ( !isPropertyComputed(propertyName)) {
            throw new IllegalStateException();
        }
        return computedValue.get(propertyName);
    }
    
    @Override
    public String toString() {
        return "StyleData {specifiedValues: " + specifiedValues + ", parsedSpecifiedValues: " + parsedSpecifiedValues + ", computedValues: " + computedValue + "}";
    }
    
}
