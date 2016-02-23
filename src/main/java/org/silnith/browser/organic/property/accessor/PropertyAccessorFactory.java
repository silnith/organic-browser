package org.silnith.browser.organic.property.accessor;

import java.util.EnumMap;
import java.util.Map;

import org.silnith.css.model.data.AbsoluteLengthParser;
import org.silnith.css.model.data.CSSNumberParser;
import org.silnith.css.model.data.ColorParser;
import org.silnith.css.model.data.FontFamilyNameParser;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PercentageLengthParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.RelativeLengthParser;


/**
 * A factory that provides access to all of the {@link PropertyAccessor}s.
 * 
 * @author kent
 */
public class PropertyAccessorFactory {
    
    public static PropertyAccessorFactory getInstance() {
        return new PropertyAccessorFactory();
    }
    
    private final ColorParser colorParser;
    
    private final CSSNumberParser cssNumberParser;
    
    private final AbsoluteLengthParser absoluteLengthParser;
    
    private final RelativeLengthParser relativeLengthParser;
    
    private final PercentageLengthParser percentageLengthParser;
    
    private final LengthParser<?> lengthParser;
    
    private final Map<PropertyName, PropertyAccessor<?>> accessors;
    
    public PropertyAccessorFactory() {
        this.accessors = new EnumMap<>(PropertyName.class);
        this.colorParser = new ColorParser();
        this.cssNumberParser = new CSSNumberParser();
        this.absoluteLengthParser = new AbsoluteLengthParser(this.cssNumberParser);
        this.relativeLengthParser = new RelativeLengthParser(this.cssNumberParser);
        this.percentageLengthParser = new PercentageLengthParser(this.cssNumberParser);
        this.lengthParser =
                new LengthParser<>(this.absoluteLengthParser, this.relativeLengthParser, this.percentageLengthParser);
                
        final ColorAccessor colorAccessor = new ColorAccessor(this.colorParser);
        final FontSizeAccessor fontSizeAccessor = new FontSizeAccessor(this.lengthParser);
        
        this.accessors.put(PropertyName.COLOR, colorAccessor);
        this.accessors.put(PropertyName.BACKGROUND_COLOR, new BackgroundColorAccessor(colorParser));
        this.accessors.put(PropertyName.DISPLAY, new DisplayAccessor());
        this.accessors.put(PropertyName.POSITION, new PositionAccessor());
        this.accessors.put(PropertyName.FONT_FAMILY, new FontFamilyAccessor(new FontFamilyNameParser()));
        this.accessors.put(PropertyName.FONT_SIZE, fontSizeAccessor);
        this.accessors.put(PropertyName.FONT_STYLE, new FontStyleAccessor());
        this.accessors.put(PropertyName.FONT_WEIGHT, new FontWeightAccessor());
        this.accessors.put(PropertyName.MARGIN_TOP, new MarginTopAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.MARGIN_RIGHT, new MarginRightAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.MARGIN_BOTTOM, new MarginBottomAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.MARGIN_LEFT, new MarginLeftAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.BORDER_TOP_STYLE, new BorderTopStyleAccessor());
        this.accessors.put(PropertyName.BORDER_RIGHT_STYLE, new BorderRightStyleAccessor());
        this.accessors.put(PropertyName.BORDER_BOTTOM_STYLE, new BorderBottomStyleAccessor());
        this.accessors.put(PropertyName.BORDER_LEFT_STYLE, new BorderLeftStyleAccessor());
        this.accessors.put(PropertyName.BORDER_TOP_WIDTH,
                new BorderTopWidthAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.BORDER_RIGHT_WIDTH,
                new BorderRightWidthAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.BORDER_BOTTOM_WIDTH,
                new BorderBottomWidthAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.BORDER_LEFT_WIDTH,
                new BorderLeftWidthAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.BORDER_TOP_COLOR, new BorderTopColorAccessor(this.colorParser, colorAccessor));
        this.accessors.put(PropertyName.BORDER_RIGHT_COLOR,
                new BorderRightColorAccessor(this.colorParser, colorAccessor));
        this.accessors.put(PropertyName.BORDER_BOTTOM_COLOR,
                new BorderBottomColorAccessor(this.colorParser, colorAccessor));
        this.accessors.put(PropertyName.BORDER_LEFT_COLOR,
                new BorderLeftColorAccessor(this.colorParser, colorAccessor));
        this.accessors.put(PropertyName.PADDING_TOP, new PaddingTopAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.PADDING_RIGHT, new PaddingRightAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.PADDING_BOTTOM, new PaddingBottomAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.PADDING_LEFT, new PaddingLeftAccessor(this.lengthParser, fontSizeAccessor));
        this.accessors.put(PropertyName.VISIBILITY, new VisibilityAccessor());
        this.accessors.put(PropertyName.LIST_STYLE_POSITION, new ListStylePositionAccessor());
        this.accessors.put(PropertyName.LIST_STYLE_TYPE, new ListStyleTypeAccessor());
    }
    
    public PropertyAccessor<?> getPropertyAccessor(final PropertyName propertyName) {
        return accessors.get(propertyName);
    }
    
}
