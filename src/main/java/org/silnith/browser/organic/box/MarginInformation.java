package org.silnith.browser.organic.box;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.Length;
import org.silnith.css.model.data.PercentageLength;
import org.silnith.css.model.data.PropertyName;


public class MarginInformation {
    
    private static final PropertyAccessorFactory propertyAccessorFactory = PropertyAccessorFactory.getInstance();
    
    private static final PropertyAccessor<Length<?>> marginTopAccessor =
            propertyAccessorFactory.getPropertyAccessor(PropertyName.MARGIN_TOP);
            
    private static final PropertyAccessor<Length<?>> marginRightAccessor =
            propertyAccessorFactory.getPropertyAccessor(PropertyName.MARGIN_RIGHT);
            
    private static final PropertyAccessor<Length<?>> marginBottomAccessor =
            propertyAccessorFactory.getPropertyAccessor(PropertyName.MARGIN_BOTTOM);
            
    private static final PropertyAccessor<Length<?>> marginLeftAccessor =
            propertyAccessorFactory.getPropertyAccessor(PropertyName.MARGIN_LEFT);
            
    public static MarginInformation getMarginInformation(final StyleData styleData) {
        final Length<?> marginTop = marginTopAccessor.getComputedValue(styleData);
        final Length<?> marginRight = marginRightAccessor.getComputedValue(styleData);
        final Length<?> marginBottom = marginBottomAccessor.getComputedValue(styleData);
        final Length<?> marginLeft = marginLeftAccessor.getComputedValue(styleData);
        
        return new MarginInformation(marginTop, marginRight, marginBottom, marginLeft);
    }
    
    private final Length<?> marginTop;
    
    private final Length<?> marginRight;
    
    private final Length<?> marginBottom;
    
    private final Length<?> marginLeft;
    
    public MarginInformation(final Length<?> marginTop, final Length<?> marginRight, final Length<?> marginBottom,
            final Length<?> marginLeft) {
        super();
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
        this.marginLeft = marginLeft;
    }
    
    public Length<?> getMarginTop() {
        return marginTop;
    }
    
    public Length<?> getMarginRight() {
        return marginRight;
    }
    
    public Length<?> getMarginBottom() {
        return marginBottom;
    }
    
    public Length<?> getMarginLeft() {
        return marginLeft;
    }
    
    public ResolvedMarginInformation resolve(final AbsoluteLength containingBlockWidth) {
        final AbsoluteLength top = resolvePercentageLength(marginTop, containingBlockWidth);
        final AbsoluteLength right = resolvePercentageLength(marginRight, containingBlockWidth);
        final AbsoluteLength bottom = resolvePercentageLength(marginBottom, containingBlockWidth);
        final AbsoluteLength left = resolvePercentageLength(marginLeft, containingBlockWidth);
        return new ResolvedMarginInformation(top, right, bottom, left);
    }
    
    private AbsoluteLength resolvePercentageLength(final Length<?> length, final AbsoluteLength containingBlockWidth) {
        switch (length.getType()) {
        case ABSOLUTE: {
            final AbsoluteLength absoluteLength = (AbsoluteLength) length;
            return absoluteLength;
        } // break;
        case RELATIVE: {
//			final RelativeLength relativeLength = (RelativeLength) length;
            throw new IllegalStateException(
                    "Property should have been resolved into an absolute length before layout: " + length);
        } // break;
        case PERCENTAGE: {
            final PercentageLength percentageLength = (PercentageLength) length;
            final AbsoluteLength resolvedLength = percentageLength.resolve(containingBlockWidth);
            return resolvedLength;
        } // break;
        default: {
            throw new IllegalArgumentException("Unknown length type: " + length);
        } // break;
        }
    }
    
}
