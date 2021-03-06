package org.silnith.css.model.data;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.silnith.parser.css3.Token;
import org.silnith.parser.css3.grammar.Parser;
import org.silnith.parser.css3.lexical.TokenListStream;
import org.silnith.parser.css3.lexical.token.FunctionToken;
import org.silnith.parser.css3.lexical.token.HashToken;
import org.silnith.parser.css3.lexical.token.IdentToken;
import org.silnith.parser.css3.lexical.token.LexicalToken;


/**
 * A parser that can parse the various formats allowed for color values in CSS.
 * 
 * @author kent
 */
public class ColorParser {
    
    private static final Pattern SIX_HEX_PATTERN;
    
    private static final Pattern THREE_HEX_PATTERN;
    
    private static final Pattern RGB_CONSTANT_PATTERN;
    
    private static final Pattern RGB_PERCENT_PATTERN;
    
    private static final Map<String, Color> PREDEFINED_COLORS;
    
    static {
        SIX_HEX_PATTERN = Pattern.compile("#(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{2})");
        THREE_HEX_PATTERN = Pattern.compile("#(\\p{XDigit}{1})(\\p{XDigit}{1})(\\p{XDigit}{1})");
        RGB_CONSTANT_PATTERN = Pattern.compile("rgb\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)");
        RGB_PERCENT_PATTERN = Pattern.compile("rgb\\(\\s*(\\d{1,3})%\\s*,\\s*(\\d{1,3})%\\s*,\\s*(\\d{1,3})%\\s*\\)");
        
        PREDEFINED_COLORS = new HashMap<>();
        PREDEFINED_COLORS.put("maroon", parseInternal("#800000"));
        PREDEFINED_COLORS.put("red", parseInternal("#ff0000"));
        PREDEFINED_COLORS.put("orange", parseInternal("#ffA500"));
        PREDEFINED_COLORS.put("yellow", parseInternal("#ffff00"));
        PREDEFINED_COLORS.put("olive", parseInternal("#808000"));
        PREDEFINED_COLORS.put("purple", parseInternal("#800080"));
        PREDEFINED_COLORS.put("fuchsia", parseInternal("#ff00ff"));
        PREDEFINED_COLORS.put("white", parseInternal("#ffffff"));
        PREDEFINED_COLORS.put("lime", parseInternal("#00ff00"));
        PREDEFINED_COLORS.put("green", parseInternal("#008000"));
        PREDEFINED_COLORS.put("navy", parseInternal("#000080"));
        PREDEFINED_COLORS.put("blue", parseInternal("#0000ff"));
        PREDEFINED_COLORS.put("aqua", parseInternal("#00ffff"));
        PREDEFINED_COLORS.put("teal", parseInternal("#008080"));
        PREDEFINED_COLORS.put("black", parseInternal("#000000"));
        PREDEFINED_COLORS.put("silver", parseInternal("#c0c0c0"));
        PREDEFINED_COLORS.put("gray", parseInternal("#808080"));
        PREDEFINED_COLORS.put("transparent", new Color(0f, 0f, 0f, 0f));
    }
    
    private static Color parseInternal(final String specifiedValue) {
        final Matcher sixHexMatcher = SIX_HEX_PATTERN.matcher(specifiedValue);
        if (sixHexMatcher.matches()) {
            final String rStr = sixHexMatcher.group(1);
            final String gStr = sixHexMatcher.group(2);
            final String bStr = sixHexMatcher.group(3);
            
            return parseSixHex(rStr, gStr, bStr);
        }
        final Matcher threeHexMatcher = THREE_HEX_PATTERN.matcher(specifiedValue);
        if (threeHexMatcher.matches()) {
            final String rStr = threeHexMatcher.group(1);
            final String gStr = threeHexMatcher.group(2);
            final String bStr = threeHexMatcher.group(3);
            
            return parseThreeHex(rStr, gStr, bStr);
        }
        final Matcher rgbMatcher = RGB_CONSTANT_PATTERN.matcher(specifiedValue);
        if (rgbMatcher.matches()) {
            final String rStr = rgbMatcher.group(1);
            final String gStr = rgbMatcher.group(2);
            final String bStr = rgbMatcher.group(3);
            
            final int r = Integer.parseInt(rStr);
            final int g = Integer.parseInt(gStr);
            final int b = Integer.parseInt(bStr);
            
            return new Color(r, g, b);
        }
        final Matcher rgbPercentMatcher = RGB_PERCENT_PATTERN.matcher(specifiedValue);
        if (rgbPercentMatcher.matches()) {
            final String rStr = rgbPercentMatcher.group(1);
            final String gStr = rgbPercentMatcher.group(2);
            final String bStr = rgbPercentMatcher.group(3);
            
            final int r = Integer.parseInt(rStr);
            final int g = Integer.parseInt(gStr);
            final int b = Integer.parseInt(bStr);
            
            return new Color(r / 100.0f, g / 100.0f, b / 100.0f);
        }
        
        return null;
    }

    private static Color parseThreeHex(final String rStr, final String gStr, final String bStr) {
        final int r = Integer.parseInt(rStr + rStr, 16);
        final int g = Integer.parseInt(gStr + gStr, 16);
        final int b = Integer.parseInt(bStr + bStr, 16);
        
        return new Color(r, g, b);
    }

    private static Color parseSixHex(final String rStr, final String gStr, final String bStr) {
        final int r = Integer.parseInt(rStr, 16);
        final int g = Integer.parseInt(gStr, 16);
        final int b = Integer.parseInt(bStr, 16);
        
        return new Color(r, g, b);
    }
    
    public ColorParser() {
        super();
    }
    
    public Color parse(final String specifiedValue) {
        final Color color = PREDEFINED_COLORS.get(specifiedValue);
        
        if (color != null) {
            return color;
        }
        
        final Color parsedColor = parseInternal(specifiedValue);
        
        if (parsedColor != null) {
            return parsedColor;
        }
        
        throw new IllegalArgumentException("Invalid color: " + specifiedValue);
    }
    
    public Color parse(final List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token token = cssParser.parseComponentValue();
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case HASH_TOKEN: {
                final HashToken hashToken = (HashToken) lexicalToken;
                final String hash = hashToken.getStringValue();
                switch (hash.length()) {
                case 3: {
                    return parseThreeHex(hash.substring(0, 1), hash.substring(1, 2), hash.substring(2, 3));
                } // break;
                case 6: {
                    return parseSixHex(hash.substring(0, 2), hash.substring(2, 4), hash.substring(4, 6));
                } // break;
                default: throw new IllegalArgumentException("Illegal hash value: #" + hash);
                }
            } // break;
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                return PREDEFINED_COLORS.get(identToken.getStringValue());
                // transparent | inherit
                // aqua, black, blue, fuchsia, gray, green, lime, maroon, navy, olive, orange, purple, red, silver, teal, white, and yellow
            } // break;
            case FUNCTION_TOKEN: {
                final FunctionToken functionToken = (FunctionToken) lexicalToken;
                functionToken.getStringValue();
                throw new UnsupportedOperationException();
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        throw new IllegalArgumentException("Unrecognized color value: " + specifiedValue);
    }
    
}
