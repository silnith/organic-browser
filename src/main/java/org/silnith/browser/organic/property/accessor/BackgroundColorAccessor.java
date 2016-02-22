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
 * A property accessor for computing the value of the "background-color" property.
 * 
 * @author kent
 * @see PropertyName#BACKGROUND_COLOR
 */
public class BackgroundColorAccessor extends PropertyAccessor<Color> {
    
    private final ColorParser colorParser;
    
    public BackgroundColorAccessor(final ColorParser colorParser) {
        super(PropertyName.BACKGROUND_COLOR, false);
        if (colorParser == null) {
            throw new NullPointerException();
        }
        this.colorParser = colorParser;
    }
    
    @Override
    public Color getInitialValue(final StyleData styleData) {
        return colorParser.parse("transparent");
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
