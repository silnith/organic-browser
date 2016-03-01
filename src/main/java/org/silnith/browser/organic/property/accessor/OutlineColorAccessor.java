package org.silnith.browser.organic.property.accessor;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.ColorParser;
import org.silnith.css.model.data.PropertyName;

public class OutlineColorAccessor extends PropertyThatDependsOnTheColorPropertyAccessor {

    public OutlineColorAccessor(ColorParser colorParser,
            PropertyAccessor<Color> colorPropertyAccessor) {
        super(PropertyName.OUTLINE_COLOR, colorParser, colorPropertyAccessor);
        
    }

    @Override
    public Color getInitialValue(StyleData styleData) {
        // should be "invert"
        return super.getInitialValue(styleData);
    }

    @Override
    protected Color parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        // TODO: accept "invert"
        return super.parse(styleData, specifiedValue);
    }

}
