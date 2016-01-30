package org.silnith.browser.organic.box;

import java.awt.Color;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.StyledContent;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.BorderStyle;
import org.silnith.css.model.data.PropertyName;


public class BorderInformation {
    
    private static final PropertyAccessorFactory propertyAccessorFactory = PropertyAccessorFactory.getInstance();
    
    private static final PropertyAccessor<BorderStyle> borderTopStyleAccessor =
            (PropertyAccessor<BorderStyle>) propertyAccessorFactory.getPropertyAccessor(PropertyName.BORDER_TOP_STYLE);
            
    private static final PropertyAccessor<BorderStyle> borderRightStyleAccessor =
            (PropertyAccessor<BorderStyle>) propertyAccessorFactory.getPropertyAccessor(
                    PropertyName.BORDER_RIGHT_STYLE);
                    
    private static final PropertyAccessor<BorderStyle> borderBottomStyleAccessor =
            (PropertyAccessor<BorderStyle>) propertyAccessorFactory.getPropertyAccessor(
                    PropertyName.BORDER_BOTTOM_STYLE);
                    
    private static final PropertyAccessor<BorderStyle> borderLeftStyleAccessor =
            (PropertyAccessor<BorderStyle>) propertyAccessorFactory.getPropertyAccessor(PropertyName.BORDER_LEFT_STYLE);
            
    private static final PropertyAccessor<Color> borderTopColorAccessor =
            (PropertyAccessor<Color>) propertyAccessorFactory.getPropertyAccessor(PropertyName.BORDER_TOP_COLOR);
            
    private static final PropertyAccessor<Color> borderRightColorAccessor =
            (PropertyAccessor<Color>) propertyAccessorFactory.getPropertyAccessor(PropertyName.BORDER_RIGHT_COLOR);
            
    private static final PropertyAccessor<Color> borderBottomColorAccessor =
            (PropertyAccessor<Color>) propertyAccessorFactory.getPropertyAccessor(PropertyName.BORDER_BOTTOM_COLOR);
            
    private static final PropertyAccessor<Color> borderLeftColorAccessor =
            (PropertyAccessor<Color>) propertyAccessorFactory.getPropertyAccessor(PropertyName.BORDER_LEFT_COLOR);
            
    private static final PropertyAccessor<AbsoluteLength> borderTopWidthAccessor =
            (PropertyAccessor<AbsoluteLength>) propertyAccessorFactory.getPropertyAccessor(
                    PropertyName.BORDER_TOP_WIDTH);
                    
    private static final PropertyAccessor<AbsoluteLength> borderRightWidthAccessor =
            (PropertyAccessor<AbsoluteLength>) propertyAccessorFactory.getPropertyAccessor(
                    PropertyName.BORDER_RIGHT_WIDTH);
                    
    private static final PropertyAccessor<AbsoluteLength> borderBottomWidthAccessor =
            (PropertyAccessor<AbsoluteLength>) propertyAccessorFactory.getPropertyAccessor(
                    PropertyName.BORDER_BOTTOM_WIDTH);
                    
    private static final PropertyAccessor<AbsoluteLength> borderLeftWidthAccessor =
            (PropertyAccessor<AbsoluteLength>) propertyAccessorFactory.getPropertyAccessor(
                    PropertyName.BORDER_LEFT_WIDTH);
                    
    @Deprecated
    public static BorderInformation getBorderInformation(final StyledContent styledContent) {
        final StyleData styleData = styledContent.getStyleData();
        
        return getBorderInformation(styleData);
    }
    
    public static BorderInformation getBorderInformation(final StyleData styleData) {
        final BorderStyle borderTopStyle = borderTopStyleAccessor.getComputedValue(styleData);
        final BorderStyle borderRightStyle = borderRightStyleAccessor.getComputedValue(styleData);
        final BorderStyle borderBottomStyle = borderBottomStyleAccessor.getComputedValue(styleData);
        final BorderStyle borderLeftStyle = borderLeftStyleAccessor.getComputedValue(styleData);
        
        final Color borderTopColor = borderTopColorAccessor.getComputedValue(styleData);
        final Color borderRightColor = borderRightColorAccessor.getComputedValue(styleData);
        final Color borderBottomColor = borderBottomColorAccessor.getComputedValue(styleData);
        final Color borderLeftColor = borderLeftColorAccessor.getComputedValue(styleData);
        
        final AbsoluteLength borderTopWidth = borderTopWidthAccessor.getComputedValue(styleData);
        final AbsoluteLength borderRightWidth = borderRightWidthAccessor.getComputedValue(styleData);
        final AbsoluteLength borderBottomWidth = borderBottomWidthAccessor.getComputedValue(styleData);
        final AbsoluteLength borderLeftWidth = borderLeftWidthAccessor.getComputedValue(styleData);
        
        return new BorderInformation(borderTopStyle, borderRightStyle, borderBottomStyle, borderLeftStyle,
                borderTopColor, borderRightColor, borderBottomColor, borderLeftColor, borderTopWidth, borderRightWidth,
                borderBottomWidth, borderLeftWidth);
    }
    
    private final BorderStyle borderTopStyle;
    
    private final BorderStyle borderRightStyle;
    
    private final BorderStyle borderBottomStyle;
    
    private final BorderStyle borderLeftStyle;
    
    private final Color borderTopColor;
    
    private final Color borderRightColor;
    
    private final Color borderBottomColor;
    
    private final Color borderLeftColor;
    
    private final AbsoluteLength borderTopWidth;
    
    private final AbsoluteLength borderRightWidth;
    
    private final AbsoluteLength borderBottomWidth;
    
    private final AbsoluteLength borderLeftWidth;
    
    public BorderInformation(final BorderStyle borderTopStyle, final BorderStyle borderRightStyle,
            final BorderStyle borderBottomStyle, final BorderStyle borderLeftStyle, final Color borderTopColor,
            final Color borderRightColor, final Color borderBottomColor, final Color borderLeftColor,
            final AbsoluteLength borderTopWidth, final AbsoluteLength borderRightWidth,
            final AbsoluteLength borderBottomWidth, final AbsoluteLength borderLeftWidth) {
        super();
        this.borderTopStyle = borderTopStyle;
        this.borderRightStyle = borderRightStyle;
        this.borderBottomStyle = borderBottomStyle;
        this.borderLeftStyle = borderLeftStyle;
        this.borderTopColor = borderTopColor;
        this.borderRightColor = borderRightColor;
        this.borderBottomColor = borderBottomColor;
        this.borderLeftColor = borderLeftColor;
        this.borderTopWidth = borderTopWidth.convertTo(AbsoluteUnit.PX);
        this.borderRightWidth = borderRightWidth.convertTo(AbsoluteUnit.PX);
        this.borderBottomWidth = borderBottomWidth.convertTo(AbsoluteUnit.PX);
        this.borderLeftWidth = borderLeftWidth.convertTo(AbsoluteUnit.PX);
    }
    
    public BorderStyle getBorderTopStyle() {
        return borderTopStyle;
    }
    
    public BorderStyle getBorderRightStyle() {
        return borderRightStyle;
    }
    
    public BorderStyle getBorderBottomStyle() {
        return borderBottomStyle;
    }
    
    public BorderStyle getBorderLeftStyle() {
        return borderLeftStyle;
    }
    
    public Color getBorderTopColor() {
        return borderTopColor;
    }
    
    public Color getBorderRightColor() {
        return borderRightColor;
    }
    
    public Color getBorderBottomColor() {
        return borderBottomColor;
    }
    
    public Color getBorderLeftColor() {
        return borderLeftColor;
    }
    
    public AbsoluteLength getBorderTopWidth() {
        if (borderTopStyle == BorderStyle.NONE || borderTopStyle == BorderStyle.HIDDEN) {
            return AbsoluteLength.ZERO;
        } else {
            return borderTopWidth;
        }
    }
    
    public AbsoluteLength getBorderRightWidth() {
        if (borderRightStyle == BorderStyle.NONE || borderRightStyle == BorderStyle.HIDDEN) {
            return AbsoluteLength.ZERO;
        } else {
            return borderRightWidth;
        }
    }
    
    public AbsoluteLength getBorderBottomWidth() {
        if (borderBottomStyle == BorderStyle.NONE || borderBottomStyle == BorderStyle.HIDDEN) {
            return AbsoluteLength.ZERO;
        } else {
            return borderBottomWidth;
        }
    }
    
    public AbsoluteLength getBorderLeftWidth() {
        if (borderLeftStyle == BorderStyle.NONE || borderLeftStyle == BorderStyle.HIDDEN) {
            return AbsoluteLength.ZERO;
        } else {
            return borderLeftWidth;
        }
    }
    
    public BorderInformation getOpenAtStart() {
        return new BorderInformation(borderTopStyle, borderRightStyle, borderBottomStyle, BorderStyle.NONE,
                borderTopColor, borderRightColor, borderBottomColor, borderLeftColor, borderTopWidth, borderRightWidth,
                borderBottomWidth, borderLeftWidth);
    }
    
    public BorderInformation getOpenAtEnd() {
        return new BorderInformation(borderTopStyle, BorderStyle.NONE, borderBottomStyle, borderLeftStyle,
                borderTopColor, borderRightColor, borderBottomColor, borderLeftColor, borderTopWidth, borderRightWidth,
                borderBottomWidth, borderLeftWidth);
    }
    
}
