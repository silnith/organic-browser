package org.silnith.browser.organic.property.accessor;

import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.PropertyName;


/**
 * The base class for all CSS property accessors. This contains methods to
 * compute the value from the specified value, and to handle inheritance and
 * default values. Each property accessor will subclass this and provide
 * implementations for parsing specified values into the type {@code &lt;T&gt;}
 * as well as providing initial values.
 * <p>
 * Property accessors must all be thread-safe and re-entrant.
 * 
 * @param <T> the type of value the property contains
 * @author kent
 */
public abstract class PropertyAccessor<T> {
    
    private final PropertyName propertyName;
    
    private final boolean inherited;
    
    /**
     * Constructs a new {@code PropertyAccessor} with the given
     * {@code propertyName} and value for whether the property is inherited by
     * default. Note that a stylesheet may specify any particular property as
     * inherited for a specific element, the {@code inherited} value here is
     * simply what the default is for unspecified properties.
     * 
     * @param propertyName the name of the property
     * @param inherited whether the property is inherited by default
     */
    public PropertyAccessor(final PropertyName propertyName, final boolean inherited) {
        if (propertyName == null) {
            throw new NullPointerException();
        }
        this.propertyName = propertyName;
        this.inherited = inherited;
    }
    
    /**
     * Returns the property that this accessor computes.
     * 
     * @return the property name that this accessor understands
     */
    public final PropertyName getPropertyName() {
        return propertyName;
    }
    
    public final boolean isInheritedByDefault() {
        return inherited;
    }
    
    /**
     * Returns the computed value of the parent style of {@code styleData}. If
     * {@code styleData} is the root style, then this returns
     * {@link #getInitialValue(StyleData)}.
     * 
     * @param styleData
     * @return
     */
    protected T getParentValue(final StyleData styleData) {
        final StyleData parentStyle = styleData.getParentStyleData();
        
        if (parentStyle == null) {
            return getInitialValue(styleData);
        } else {
            @SuppressWarnings("unchecked")
            final T computedValue = (T) parentStyle.getComputedValue(propertyName);
            return computedValue;
        }
    }
    
    /**
     * Returns the initial value for the property. For non-inherited properties,
     * this is used whenever the property is not specified. For inherited
     * properties, this is used only for the root element, and only when it is
     * not specified on the root element.
     * 
     * @param styleData a few properties are defined in terms of other
     *        properties on the same element (e.g. border-color)
     * @return the initial value for the property
     */
    public abstract T getInitialValue(StyleData styleData);
    
    /**
     * Parses a specified value into the type {@code <T>}. The returned value
     * cannot be {@code null}.
     * 
     * @param styleData
     * @param specifiedValue the value to parse
     * @return the specified value parsed into a typed value
     * @throws IllegalArgumentException if the specified value is invalid for
     *         the property
     */
    protected abstract T parse(StyleData styleData, String specifiedValue);

    /**
     * Parses a specified value into the type {@code <T>}. The returned value
     * cannot be {@code null}.
     * 
     * @param styleData
     * @param specifiedValue the parsed list of tokens
     * @return the specified value parsed into a typed value
     * @throws IllegalArgumentException if the specified value is invalid for
     *         the property
     */
    protected abstract T parse(StyleData styleData, List<Token> specifiedValue);
    
    /**
     * Computes the property value for the given element.  The specified value
     * must have already been set, if any.
     * The algorithm is as follows:
     * <ol>
     * <li>If the property is specified explicitly for this element and is the
     * string "inherit", then the property value is the computed value of the
     * same property for the parent element.</li>
     * <li>If the property is specified explicitly for this element and is not
     * the string "inherit", {@link #parse} is called to convert the string
     * value into the type {@code &lt;T&gt;}.</li>
     * <li>If the property is not specified for this element but is inherited by
     * default, then the property value is the computed value of the same
     * property for the parent element.</li>
     * <li>If the property is not specified for this element and is not
     * inherited by default, then {@link #getInitialValue} is called to get the
     * computed value.</li>
     * </ol>
     * <p>
     * For inherited values, if the element is the root element then the value
     * is the initial value.
     * 
     * @param styleData the styling information for the node being computed
     */
    public void computeValue(final StyleData styleData) {
//        assert !styleData.isPropertyComputed(propertyName);
        
        final T computedValue;
        if (styleData.isPropertySpecified(getPropertyName())) {
            if (styleData.getInherit(getPropertyName())) {
                // inherit
                computedValue = getParentValue(styleData);
            } else {
                // parse
                final String specifiedValue = styleData.getSpecifiedValue(getPropertyName());
                
                T parsedValue;
                try {
                    parsedValue = parse(styleData, specifiedValue);
                } catch (final IllegalArgumentException e) {
                    // log that the rule was invalid
                    parsedValue = getValueWhenUnspecified(styleData);
                }
                computedValue = parsedValue;
            }
        } else {
            // find the initial value
            computedValue = getValueWhenUnspecified(styleData);
        }
        
        styleData.setComputedValue(propertyName, computedValue);
    }

    /**
     * Computes the property value for the given element.  The specified value
     * must have already been set, if any.
     * The algorithm is as follows:
     * <ol>
     * <li>If the property is specified explicitly for this element and is the
     * string "inherit", then the property value is the computed value of the
     * same property for the parent element.</li>
     * <li>If the property is specified explicitly for this element and is not
     * the string "inherit", {@link #parse} is called to convert the string
     * value into the type {@code &lt;T&gt;}.</li>
     * <li>If the property is not specified for this element but is inherited by
     * default, then the property value is the computed value of the same
     * property for the parent element.</li>
     * <li>If the property is not specified for this element and is not
     * inherited by default, then {@link #getInitialValue} is called to get the
     * computed value.</li>
     * </ol>
     * <p>
     * For inherited values, if the element is the root element then the value
     * is the initial value.
     * 
     * @param styleData the styling information for the node being computed
     */
    public void computeParsedValue(final StyleData styleData) {
//        assert !styleData.isPropertyComputed(propertyName);
        
        final T computedValue;
        if (styleData.isPropertySpecified(getPropertyName())) {
            if (styleData.getInherit(getPropertyName())) {
                // inherit
                computedValue = getParentValue(styleData);
            } else {
                // parse
                final List<Token> specifiedValue = styleData.getParsedSpecifiedValue(getPropertyName());
                
                T parsedValue;
                try {
                    parsedValue = parse(styleData, specifiedValue);
                } catch (final IllegalArgumentException e) {
                    // log that the rule was invalid
                    parsedValue = getValueWhenUnspecified(styleData);
                }
                computedValue = parsedValue;
            }
        } else {
            // find the initial value
            computedValue = getValueWhenUnspecified(styleData);
        }
        
        styleData.setComputedValue(propertyName, computedValue);
    }
    
    /**
     * Performs steps 3 and 4 of the algorithm defined for {@link #computeValue(StyleData)}.
     * 
     * @param styleData the styling information for the node being computed
     * @return the computed value
     */
    private T getValueWhenUnspecified(final StyleData styleData) {
        if (inherited) {
            // inherit
            return getParentValue(styleData);
        } else {
            // get initial value
            return getInitialValue(styleData);
        }
    }
    
    public T getComputedValue(final StyleData styleData) {
        assert styleData.isPropertyComputed(propertyName);
        
        @SuppressWarnings("unchecked")
        final T computedValue = (T) styleData.getComputedValue(propertyName);
        
        return computedValue;
    }
    
    /**
     * Returns the set of other property names that must be computed before this
     * property can be computed. This may be the empty set. It will never be
     * {@code null}.
     * 
     * @return a set containing the names of all properties that must be
     *         computed before this property can be computed
     */
    public abstract Set<PropertyName> getDependencies();
    
}
