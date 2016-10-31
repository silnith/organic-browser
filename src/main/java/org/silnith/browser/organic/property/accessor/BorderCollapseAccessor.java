package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.BorderCollapse;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;


public class BorderCollapseAccessor extends PropertyAccessor<BorderCollapse> {
    
    private final KeywordParser<BorderCollapse> parser;
    
    public BorderCollapseAccessor() {
        super(PropertyName.BORDER_COLLAPSE, true);
        this.parser = new KeywordParser<>(BorderCollapse.class);
    }

    @Override
    public BorderCollapse getInitialValue(StyleData styleData) {
        return BorderCollapse.SEPARATE;
    }
    
    @Override
    protected BorderCollapse parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
