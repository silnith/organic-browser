package org.silnith.browser.organic.parser;

public class ParseErrorException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public ParseErrorException() {
    }
    
    public ParseErrorException(final String message) {
        super(message);
    }
    
    public ParseErrorException(final Throwable cause) {
        super(cause);
    }
    
    public ParseErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ParseErrorException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
