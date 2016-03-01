package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.BackgroundAttachment;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;


public class BackgroundAttachmentAccessor extends PropertyAccessor<BackgroundAttachment> {
    
    final KeywordParser<BackgroundAttachment> parser;
    
    public BackgroundAttachmentAccessor() {
        super(PropertyName.BACKGROUND_ATTACHMENT, false);
        this.parser = new KeywordParser<>(BackgroundAttachment.class);
    }

    @Override
    public BackgroundAttachment getInitialValue(StyleData styleData) {
        return BackgroundAttachment.SCROLL;
    }
    
    @Override
    protected BackgroundAttachment parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
