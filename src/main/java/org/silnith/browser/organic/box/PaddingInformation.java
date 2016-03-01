package org.silnith.browser.organic.box;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.Length;
import org.silnith.css.model.data.PercentageLength;
import org.silnith.css.model.data.PropertyName;


public class PaddingInformation {
    
    private static final PropertyAccessorFactory propertyAccessorFactory = PropertyAccessorFactory.getInstance();
    
    private static final PropertyAccessor<Length<?>> paddingTopAccessor =
            propertyAccessorFactory.getPropertyAccessor(PropertyName.PADDING_TOP);
            
    private static final PropertyAccessor<Length<?>> paddingRightAccessor =
            propertyAccessorFactory.getPropertyAccessor(PropertyName.PADDING_RIGHT);
            
    private static final PropertyAccessor<Length<?>> paddingBottomAccessor =
            propertyAccessorFactory.getPropertyAccessor(PropertyName.PADDING_BOTTOM);
            
    private static final PropertyAccessor<Length<?>> paddingLeftAccessor =
            propertyAccessorFactory.getPropertyAccessor(PropertyName.PADDING_LEFT);
            
    public static PaddingInformation getPaddingInformation(final StyleData styleData) {
        final Length<?> paddingTop = paddingTopAccessor.getComputedValue(styleData);
        final Length<?> paddingRight = paddingRightAccessor.getComputedValue(styleData);
        final Length<?> paddingBottom = paddingBottomAccessor.getComputedValue(styleData);
        final Length<?> paddingLeft = paddingLeftAccessor.getComputedValue(styleData);
        
        return new PaddingInformation(paddingTop, paddingRight, paddingBottom, paddingLeft);
    }
    
    private final Length<?> paddingTop;
    
    private final Length<?> paddingRight;
    
    private final Length<?> paddingBottom;
    
    private final Length<?> paddingLeft;
    
    public PaddingInformation(final Length<?> paddingTop, final Length<?> paddingRight, final Length<?> paddingBottom,
            final Length<?> paddingLeft) {
        super();
        if (paddingTop == null) {
            throw new NullPointerException("padding-top is null");
        }
        if (paddingRight == null) {
            throw new NullPointerException("padding-right is null");
        }
        if (paddingBottom == null) {
            throw new NullPointerException("padding-bottom is null");
        }
        if (paddingLeft == null) {
            throw new NullPointerException("padding-left is null");
        }
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
        this.paddingLeft = paddingLeft;
    }
    
    public Length<?> getPaddingTop() {
        return paddingTop;
    }
    
    public Length<?> getPaddingRight() {
        return paddingRight;
    }
    
    public Length<?> getPaddingBottom() {
        return paddingBottom;
    }
    
    public Length<?> getPaddingLeft() {
        return paddingLeft;
    }
    
    public ResolvedPaddingInformation resolve(final AbsoluteLength containingBlockWidth) {
        final AbsoluteLength top = resolvePercentageLength(paddingTop, containingBlockWidth);
        final AbsoluteLength right = resolvePercentageLength(paddingRight, containingBlockWidth);
        final AbsoluteLength bottom = resolvePercentageLength(paddingBottom, containingBlockWidth);
        final AbsoluteLength left = resolvePercentageLength(paddingLeft, containingBlockWidth);
        return new ResolvedPaddingInformation(top, right, bottom, left);
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
                    "Property should have been resolved into an absolute length or percentage before layout: "
                            + length);
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
