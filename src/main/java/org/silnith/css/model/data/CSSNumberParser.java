package org.silnith.css.model.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A parser for CSS number values.
 * 
 * @author kent
 */
public class CSSNumberParser {
    
    /**
     * Numbers in CSS can either be integers or floating-point values. If they
     * are integers, they are a string of one or more digits with an optional
     * preceding sign. If they are floating-point values, they are a string of
     * zero or more digits, a dot, and a string of one or more digits, all with
     * an optional preceding sign.
     */
    private static final Pattern PATTERN = Pattern.compile("(\\+|-)?([0-9]+|[0-9]*\\.[0-9]+)");
    
    public CSSNumberParser() {
    }
    
    /**
     * Parses a CSS number value. This is a floating-point value with an
     * optional sign but no exponent.
     * 
     * @param specifiedValue the CSS value to parse
     * @return a CSS number
     * @see <a href="http://www.w3.org/TR/CSS2/syndata.html#value-def-length">http://www.w3.org/TR/CSS2/syndata.html#value-def-length</a>
     */
    public CSSNumber parse(final String specifiedValue) {
        final Matcher matcher = PATTERN.matcher(specifiedValue);
        if (matcher.matches()) {
//			try {
            return CSSNumber.valueOf(Float.parseFloat(specifiedValue));
//			} catch (final NumberFormatException e) {
//				return null;
//			}
        } else {
            return null;
        }
    }
    
}
