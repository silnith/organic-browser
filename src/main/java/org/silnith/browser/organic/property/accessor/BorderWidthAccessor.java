package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.Length;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.RelativeLength;


/**
 * An abstract base class for all the property accessors for the "border-*-width" properties.
 * <p>
 * Note that this is not an accessor for the "border-width" pseudo-property.
 * 
 * @author kent
 * @see BorderTopWidthAccessor
 * @see BorderRightWidthAccessor
 * @see BorderBottomWidthAccessor
 * @see BorderLeftWidthAccessor
 */
public abstract class BorderWidthAccessor extends PropertyAccessor<AbsoluteLength> {
    
    private final LengthParser<?> lengthParser;
    
    private final PropertyAccessor<AbsoluteLength> fontSizeAccessor;
    
    public BorderWidthAccessor(final PropertyName propertyName, final LengthParser<?> lengthParser,
            final PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
        super(propertyName, false);
        this.lengthParser = lengthParser;
        this.fontSizeAccessor = fontSizeAccessor;
    }
    
    @Override
    public AbsoluteLength getInitialValue(final StyleData styleData) {
        // "medium"
        return new AbsoluteLength(3, AbsoluteUnit.PX);
    }
    
    @Override
    protected AbsoluteLength parse(final StyleData styleData, final String specifiedValue) {
        // TODO: check for keywords "thin", "medium", "thick"
        final Length<?> length = lengthParser.parse(specifiedValue);
        
        final AbsoluteLength absoluteLength;
        switch (length.getType()) {
        case ABSOLUTE: {
            absoluteLength = (AbsoluteLength) length;
        }
            break;
        case RELATIVE: {
            final RelativeLength relativeLength = (RelativeLength) length;
            absoluteLength = relativeLength.resolve(fontSizeAccessor.getComputedValue(styleData));
        }
            break;
        case PERCENTAGE: {
            throw new IllegalArgumentException(
                    "Border width values cannot be percentages: " + getPropertyName() + ": " + length);
        } // break;
        default:
            throw new IllegalArgumentException();
        }
        
        if (absoluteLength.getLength().floatValue() < 0) {
            throw new IllegalArgumentException(
                    "Border width values cannot be negative: " + getPropertyName() + ": " + absoluteLength);
        }
        return absoluteLength;
    }
    
    @Override
    protected AbsoluteLength parse(StyleData styleData, List<Token> specifiedValue) {
        // TODO: check for keywords "thin", "medium", "thick"
        final Length<?> length = lengthParser.parse(specifiedValue);
        
        final AbsoluteLength absoluteLength;
        switch (length.getType()) {
        case ABSOLUTE: {
            absoluteLength = (AbsoluteLength) length;
        }
            break;
        case RELATIVE: {
            final RelativeLength relativeLength = (RelativeLength) length;
            absoluteLength = relativeLength.resolve(fontSizeAccessor.getComputedValue(styleData));
        }
            break;
        case PERCENTAGE: {
            throw new IllegalArgumentException(
                    "Border width values cannot be percentages: " + getPropertyName() + ": " + length);
        } // break;
        default:
            throw new IllegalArgumentException();
        }
        
        if (absoluteLength.getLength().floatValue() < 0) {
            throw new IllegalArgumentException(
                    "Border width values cannot be negative: " + getPropertyName() + ": " + absoluteLength);
        }
        return absoluteLength;
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.singleton(PropertyName.FONT_SIZE);
    }
    
}
