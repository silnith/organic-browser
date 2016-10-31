package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.PropertyName;


public class ListStyleImageAccessor extends PropertyAccessor<URI> {
    
    public ListStyleImageAccessor() {
        super(PropertyName.LIST_STYLE_IMAGE, true);
    }

    @Override
    public URI getInitialValue(StyleData styleData) {
        return null;
    }
    
    @Override
    protected URI parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return null;
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
