package org.silnith.browser.organic.property.accessor;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.ColorParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;


/**
 * An abstract base class for property accessors where the property depends on
 * the computed value of the "color" property.
 * 
 * @author kent
 * @see PropertyName#COLOR
 */
public abstract class PropertyThatDependsOnTheColorPropertyAccessor extends PropertyAccessor<Color> {
    
    private final ColorParser colorParser;
    
    private final PropertyAccessor<Color> colorPropertyAccessor;
    
    public PropertyThatDependsOnTheColorPropertyAccessor(final PropertyName propertyName, final ColorParser colorParser,
            final PropertyAccessor<Color> colorPropertyAccessor) {
        super(propertyName, false);
        this.colorParser = colorParser;
        this.colorPropertyAccessor = colorPropertyAccessor;
    }
    
    @Override
    public Color getInitialValue(final StyleData styleData) {
        return colorPropertyAccessor.getComputedValue(styleData);
    }
    
    @Override
    protected Color parse(final StyleData styleData, final List<Token> specifiedValue) throws IOException {
        return colorParser.parse(specifiedValue);
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.singleton(PropertyName.COLOR);
    }
    
}
