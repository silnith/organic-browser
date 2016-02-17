package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.Length;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PercentageLength;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.RelativeLength;


public abstract class PaddingAccessor extends PropertyAccessor<Length<?>> {
    
    private final LengthParser<?> lengthParser;
    
    private final PropertyAccessor<AbsoluteLength> fontSizeAccessor;
    
    public PaddingAccessor(final PropertyName propertyName, final LengthParser<?> lengthParser,
            final PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
        super(propertyName, false);
        this.lengthParser = lengthParser;
        this.fontSizeAccessor = fontSizeAccessor;
    }
    
    @Override
    public Length<?> getInitialValue(final StyleData styleData) {
        return AbsoluteLength.ZERO;
    }
    
    @Override
    protected Length<?> parse(final StyleData styleData, final String specifiedValue) {
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
            final PercentageLength percentageLength = (PercentageLength) length;
            if (percentageLength.getLength().floatValue() < 0) {
                throw new IllegalArgumentException(
                        "Padding values cannot be negative: " + getPropertyName() + ": " + percentageLength);
            }
            return percentageLength;
        } // break;
        default:
            throw new IllegalArgumentException();
        }
        
        if (absoluteLength.getLength().floatValue() < 0) {
            throw new IllegalArgumentException(
                    "Padding values cannot be negative: " + getPropertyName() + ": " + absoluteLength);
        }
        return absoluteLength;
    }
    
    @Override
    protected Length<?> parse(StyleData styleData, List<Token> specifiedValue) {
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
            final PercentageLength percentageLength = (PercentageLength) length;
            if (percentageLength.getLength().floatValue() < 0) {
                throw new IllegalArgumentException(
                        "Padding values cannot be negative: " + getPropertyName() + ": " + percentageLength);
            }
            return percentageLength;
        } // break;
        default:
            throw new IllegalArgumentException();
        }
        
        if (absoluteLength.getLength().floatValue() < 0) {
            throw new IllegalArgumentException(
                    "Padding values cannot be negative: " + getPropertyName() + ": " + absoluteLength);
        }
        return absoluteLength;
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.singleton(PropertyName.FONT_SIZE);
    }
    
}
