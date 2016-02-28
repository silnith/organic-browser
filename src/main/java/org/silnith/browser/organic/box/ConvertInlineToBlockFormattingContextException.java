package org.silnith.browser.organic.box;


/**
 * Signals that an {@link InlineFormattingContext} contains a {@link BlockLevelBox}
 * and thus needs to be converted into a {@link BlockFormattingContext}.
 * 
 * @author kent
 */
public class ConvertInlineToBlockFormattingContextException extends Exception {

    public ConvertInlineToBlockFormattingContextException() {
        super();
        
    }

    public ConvertInlineToBlockFormattingContextException(String message) {
        super(message);
        
    }

    public ConvertInlineToBlockFormattingContextException(Throwable cause) {
        super(cause);
        
    }

    public ConvertInlineToBlockFormattingContextException(String message, Throwable cause) {
        super(message, cause);
        
    }

}
