package org.silnith.browser.organic;

import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.WhiteSpace;

/**
 * Applies the CSS whitespace rules to the text content of a styled element tree.
 * The whitespace property must already have been computed for the entire tree.
 * 
 * @author kent
 */
public class WhitespaceCollapser {
    
    private final PropertyAccessor<WhiteSpace> whitespaceAccessor = PropertyAccessorFactory.getInstance().getPropertyAccessor(PropertyName.WHITE_SPACE);
    
    public void collapseWhitespace(final StyledContent styledContent) {
        if (styledContent instanceof StyledText) {
            final StyledText styledText = (StyledText) styledContent;
            final WhiteSpace whitespace = whitespaceAccessor.getComputedValue(styledText.getStyleData());
            switch (whitespace) {
            case NORMAL: {} break;
            case NOWRAP: {} break;
            case PRE: {} break;
            case PRE_LINE: {} break;
            case PRE_WRAP: {} break;
            default: {} break;
            }
        } else if (styledContent instanceof StyledElement) {
            final StyledElement styledElement = (StyledElement) styledContent;
            for (final StyledContent child : styledElement.getChildren()) {
                collapseWhitespace(child);
            }
        }
    }

}
