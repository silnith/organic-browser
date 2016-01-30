package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.FontAbsoluteSize;
import org.silnith.css.model.data.FontRelativeSize;
import org.silnith.css.model.data.Length;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PercentageLength;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.RelativeLength;


public class FontSizeAccessor extends PropertyAccessor<AbsoluteLength> {
    
    private static final Map<FontAbsoluteSize, AbsoluteLength> fontSizeTable;
    
    static {
        fontSizeTable = new EnumMap<>(FontAbsoluteSize.class);
        fontSizeTable.put(FontAbsoluteSize.XX_SMALL, new AbsoluteLength(8, AbsoluteUnit.PT));
        fontSizeTable.put(FontAbsoluteSize.X_SMALL, new AbsoluteLength(10, AbsoluteUnit.PT));
        fontSizeTable.put(FontAbsoluteSize.SMALL, new AbsoluteLength(12, AbsoluteUnit.PT));
        fontSizeTable.put(FontAbsoluteSize.MEDIUM, new AbsoluteLength(14, AbsoluteUnit.PT));
        fontSizeTable.put(FontAbsoluteSize.LARGE, new AbsoluteLength(16, AbsoluteUnit.PT));
        fontSizeTable.put(FontAbsoluteSize.X_LARGE, new AbsoluteLength(18, AbsoluteUnit.PT));
        fontSizeTable.put(FontAbsoluteSize.XX_LARGE, new AbsoluteLength(20, AbsoluteUnit.PT));
    }
    
    private final LengthParser lengthParser;
    
    public FontSizeAccessor(final LengthParser lengthParser) {
        super(PropertyName.FONT_SIZE, true);
        this.lengthParser = lengthParser;
    }
    
    private FontAbsoluteSize getFontAbsoluteSize(final String value) {
        return FontAbsoluteSize.getFontAbsoluteSize(value);
    }
    
    private FontRelativeSize getFontRelativeSize(final String value) {
        return FontRelativeSize.getFontRelativeSize(value);
    }
    
    @Override
    public AbsoluteLength getInitialValue(final StyleData styleData) {
        return fontSizeTable.get(FontAbsoluteSize.MEDIUM);
    }
    
    @Override
    protected AbsoluteLength parse(final StyleData styleData, final String specifiedValue) {
        /*
         * Font sizes less than 9 pixels per em unit should be restricted.
         */
        final FontAbsoluteSize fontAbsoluteSize = getFontAbsoluteSize(specifiedValue);
        if (fontAbsoluteSize != null) {
            return fontSizeTable.get(fontAbsoluteSize);
        } else {
            final FontRelativeSize fontRelativeSize = getFontRelativeSize(specifiedValue);
            if (fontRelativeSize != null) {
                throw new UnsupportedOperationException("Font sizes 'smaller' and 'larger' not yet implemented.");
            } else {
                final Length<?> length = lengthParser.parse(specifiedValue);
                
                final AbsoluteLength absoluteLength;
                switch (length.getType()) {
                case ABSOLUTE: {
                    absoluteLength = (AbsoluteLength) length;
                }
                    break;
                case RELATIVE: {
                    final RelativeLength relativeLength = (RelativeLength) length;
                    absoluteLength = relativeLength.resolve(getParentValue(styleData));
                }
                    break;
                case PERCENTAGE: {
                    final PercentageLength percentageLength = (PercentageLength) length;
                    absoluteLength = percentageLength.resolve(getParentValue(styleData));
                }
                    break;
                default:
                    throw new IllegalArgumentException();
                }
                
                if (absoluteLength.getLength().floatValue() < 0) {
                    throw new IllegalArgumentException("Font sizes cannot be negative: " + absoluteLength);
                }
                return absoluteLength;
            }
        }
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
