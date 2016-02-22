package org.silnith.browser.organic.property.accessor;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.ColorParser;
import org.silnith.css.model.data.PropertyName;


/**
 * A property accessor for computing the value of the "color" property.
 * 
 * @author kent
 * @see PropertyName#COLOR
 */
public class ColorAccessor extends PropertyAccessor<Color> {
    
    private final ColorParser colorParser;
    
    public ColorAccessor(final ColorParser colorParser) {
        super(PropertyName.COLOR, true);
        if (colorParser == null) {
            throw new NullPointerException();
        }
        this.colorParser = colorParser;
    }
    
    @Override
    public Color getInitialValue(final StyleData styleData) {
        return Color.BLACK;
    }
    
    @Override
    protected Color parse(final StyleData styleData, final String specifiedValue) {
        return colorParser.parse(specifiedValue);
    }
    
    @Override
    protected Color parse(final StyleData styleData, final List<Token> specifiedValue) throws IOException {
        return colorParser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
