package org.silnith.browser.organic.box;

import java.awt.Color;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.PropertyName;


public class BackgroundInformation {
    
    private static final PropertyAccessorFactory propertyAccessorFactory = PropertyAccessorFactory.getInstance();
    
    private static final PropertyAccessor<Color> backgroundColorAccessor =
            (PropertyAccessor<Color>) propertyAccessorFactory.getPropertyAccessor(PropertyName.BACKGROUND_COLOR);
            
    public static BackgroundInformation getBackgroundInformation(final StyleData styleData) {
        final Color color = backgroundColorAccessor.getComputedValue(styleData);
        return new BackgroundInformation(color);
    }
    
    private final Color color;
    
    public BackgroundInformation(final Color color) {
        super();
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
    
}
