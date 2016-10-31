package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.FontAbsoluteSize;
import org.silnith.css.model.data.FontRelativeSize;
import org.silnith.css.model.data.Length;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PercentageLength;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.RelativeLength;
import org.silnith.parser.css3.Token;
import org.silnith.parser.css3.grammar.Parser;
import org.silnith.parser.css3.lexical.TokenListStream;
import org.silnith.parser.css3.lexical.token.IdentToken;
import org.silnith.parser.css3.lexical.token.LexicalToken;


public class FontSizeAccessor extends PropertyAccessor<AbsoluteLength> {
    
    private static final Map<FontAbsoluteSize, AbsoluteLength> absoluteFontSize;
    private static final Map<FontRelativeSize, PercentageLength> relativeFontSize;
    
    static {
        absoluteFontSize = new EnumMap<>(FontAbsoluteSize.class);
        absoluteFontSize.put(FontAbsoluteSize.XX_SMALL, new AbsoluteLength(8, AbsoluteUnit.PT));
        absoluteFontSize.put(FontAbsoluteSize.X_SMALL, new AbsoluteLength(10, AbsoluteUnit.PT));
        absoluteFontSize.put(FontAbsoluteSize.SMALL, new AbsoluteLength(12, AbsoluteUnit.PT));
        absoluteFontSize.put(FontAbsoluteSize.MEDIUM, new AbsoluteLength(14, AbsoluteUnit.PT));
        absoluteFontSize.put(FontAbsoluteSize.LARGE, new AbsoluteLength(16, AbsoluteUnit.PT));
        absoluteFontSize.put(FontAbsoluteSize.X_LARGE, new AbsoluteLength(18, AbsoluteUnit.PT));
        absoluteFontSize.put(FontAbsoluteSize.XX_LARGE, new AbsoluteLength(20, AbsoluteUnit.PT));
        
        relativeFontSize = new EnumMap<>(FontRelativeSize.class);
        relativeFontSize.put(FontRelativeSize.SMALLER, new PercentageLength(0.85f));
        relativeFontSize.put(FontRelativeSize.LARGER, new PercentageLength(1.15f));
    }
    
    private final LengthParser<?> lengthParser;
    
    public FontSizeAccessor(final LengthParser<?> lengthParser) {
        super(PropertyName.FONT_SIZE, true);
        this.lengthParser = lengthParser;
    }
    
    private FontAbsoluteSize getFontAbsoluteSize(final String value) {
        return FontAbsoluteSize.getFontAbsoluteSize(value);
    }
    
    private FontRelativeSize getFontRelativeSize(final String value) {
        return FontRelativeSize.getFontRelativeSize(value);
    }
    
    @Override
    public AbsoluteLength getInitialValue(final StyleData styleData) {
        return absoluteFontSize.get(FontAbsoluteSize.MEDIUM);
    }
    
    @Override
    protected AbsoluteLength parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token token = cssParser.parseComponentValue();
        /*
         * Font sizes less than 9 pixels per em unit should be restricted.
         */
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                final String ident = identToken.getStringValue();
                final FontAbsoluteSize fontAbsoluteSize = getFontAbsoluteSize(ident);
                if (fontAbsoluteSize != null) {
                    return absoluteFontSize.get(fontAbsoluteSize);
                }
                final FontRelativeSize fontRelativeSize = getFontRelativeSize(ident);
                if (fontRelativeSize != null) {
                    return resolveLength(styleData, relativeFontSize.get(fontRelativeSize));
                }
            } break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        final Length<?> length = lengthParser.parse(specifiedValue);
        final AbsoluteLength absoluteLength = resolveLength(styleData, length);
        return absoluteLength;
    }

    private AbsoluteLength resolveLength(final StyleData styleData, final Length<?> length) {
        final AbsoluteLength absoluteLength;
        switch (length.getType()) {
        case ABSOLUTE: {
            absoluteLength = (AbsoluteLength) length;
        } break;
        case RELATIVE: {
            final RelativeLength relativeLength = (RelativeLength) length;
            absoluteLength = relativeLength.resolve(getParentValue(styleData));
        } break;
        case PERCENTAGE: {
            final PercentageLength percentageLength = (PercentageLength) length;
            absoluteLength = percentageLength.resolve(getParentValue(styleData));
        } break;
        default: throw new IllegalArgumentException("Unknown length type: " + length.getType());
        }
        
        if (absoluteLength.getLength().floatValue() < 0) {
            throw new IllegalArgumentException("Font sizes cannot be negative: " + absoluteLength);
        }
        return absoluteLength;
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
