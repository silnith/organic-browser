package org.silnith.browser.organic;

import java.io.IOException;
import java.io.StringReader;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.lexical.Tokenizer;
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
    
    private final Set<PropertyName> propertyInherited;
    
    private final Set<PropertyName> propertySpecified;
    
    private final Map<PropertyName, List<Token>> specifiedValues;
    
    /**
     * A map that records which properties have been computed and which have not.
     */
    private final Set<PropertyName> propertyComputed;
    
    private final Map<PropertyName, Object> computedValue;
    
    public StyleData(final StyleData parentStyleData) {
        super();
        this.parentStyleData = parentStyleData;
        
        this.propertyInherited = EnumSet.noneOf(PropertyName.class);
        this.propertySpecified = EnumSet.noneOf(PropertyName.class);
        this.specifiedValues = new EnumMap<>(PropertyName.class);
        this.propertyComputed = EnumSet.noneOf(PropertyName.class);
        this.computedValue = new EnumMap<>(PropertyName.class);
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
     * keyword <kbd>inherit</kbd>.
     * 
     * @param propertyName the name of the property to check
     * @return {@code true} if the named property is inherited in this styling data
     */
    public boolean isInherit(final PropertyName propertyName) {
        if ( !isPropertySpecified(propertyName)) {
            throw new IllegalStateException();
        }
        return propertyInherited.contains(propertyName);
    }
    
    /**
     * Returns whether the named property has been specified explicitly in this
     * style data.
     * 
     * @param propertyName
     * @return
     */
    public boolean isPropertySpecified(final PropertyName propertyName) {
        return propertySpecified.contains(propertyName);
    }
    
    /**
     * Specifies a value for the given property in this style data.  This marks
     * the property as being specified, records the specified value, and checks
     * whether the specified value is the special value "inherit".
     * 
     * @param propertyName
     * @param propertyValue
     * @throws IOException 
     */
    @Deprecated
    public void setSpecifiedValue(final PropertyName propertyName, final String propertyValue) throws IOException {
        final Parser cssParser = new Parser(new Tokenizer(new StringReader(propertyValue)));
        cssParser.prime();
        final List<Token> tokens = cssParser.parseListOfComponentValues();
        setParsedSpecifiedValue(propertyName, tokens);
    }
    
    public void setInherit(final PropertyName propertyName) {
        propertySpecified.add(propertyName);
        propertyInherited.add(propertyName);
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
        propertySpecified.add(propertyName);
        specifiedValues.put(propertyName, propertyValue);
    }
    
    /**
     * Returns the specified value for the given property.  If the property is
     * not specified, this throws a runtime exception.
     * 
     * @param propertyName
     * @return
     */
    public List<Token> getSpecifiedValue(final PropertyName propertyName) {
        if ( !isPropertySpecified(propertyName)) {
            throw new IllegalStateException();
        }
        return specifiedValues.get(propertyName);
    }
    
    /**
     * Returns whether the property has been computed yet.
     * 
     * @param propertyName
     * @return
     */
    public boolean isPropertyComputed(final PropertyName propertyName) {
        return propertyComputed.contains(propertyName);
    }
    
    /**
     * Sets the computed value.  The type of the value will be whatever type is
     * associated with the {@link PropertyAccessor} for the given property.
     * 
     * @param propertyName
     * @param value
     */
    public void setComputedValue(final PropertyName propertyName, final Object value) {
        propertyComputed.add(propertyName);
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
        return "StyleData {parsedSpecifiedValues: " + specifiedValues + ", computedValues: " + computedValue + ", propertyInherited: " + propertyInherited + "}";
    }
    
}
