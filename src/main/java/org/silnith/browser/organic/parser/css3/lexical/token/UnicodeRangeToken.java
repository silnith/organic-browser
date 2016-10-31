package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href=
 *      "http://dev.w3.org/csswg/css-syntax/#typedef-unicode-range-token">&lt;
 *      unicode-range-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class UnicodeRangeToken extends LexicalToken {
    
    private int start;
    
    private int end;
    
    public UnicodeRangeToken() {
        super(LexicalType.UNICODE_RANGE_TOKEN);
    }
    
    public void setStart(final int start) {
        this.start = start;
    }
    
    public int getStart() {
        return start;
    }
    
    public void setEnd(final int end) {
        this.end = end;
    }
    
    public int getEnd() {
        return end;
    }
    
    @Override
    public String toString() {
        return getStart() + "-" + getEnd();
    }
    
}
