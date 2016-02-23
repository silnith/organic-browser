package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.lexical.TokenListStream;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.silnith.browser.organic.parser.css3.lexical.token.NumberToken;
import org.silnith.css.model.data.FontWeight;
import org.silnith.css.model.data.PropertyName;


public class FontWeightAccessor extends PropertyAccessor<FontWeight> {
    
    public FontWeightAccessor() {
        super(PropertyName.FONT_WEIGHT, true);
    }

    @Override
    public FontWeight getInitialValue(StyleData styleData) {
        return FontWeight.getFontWeight("normal");
    }
    
    @Override
    protected FontWeight parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token token = cssParser.parseComponentValue();
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                final String identifier = identToken.getStringValue();
                if (identifier.equals("lighter")) {
                    return getParentValue(styleData).getLighter();
                }
                if (identifier.equals("bolder")) {
                    return getParentValue(styleData).getBolder();
                }
                final FontWeight fontWeight = FontWeight.getFontWeight(identifier);
                if (fontWeight != null) {
                    return fontWeight;
                }
            } break;
            case NUMBER_TOKEN: {
                final NumberToken numberToken = (NumberToken) lexicalToken;
                final FontWeight fontWeight = FontWeight.getFontWeight(numberToken.getStringValue());
                if (fontWeight != null) {
                    return fontWeight;
                }
            } break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        throw new IllegalArgumentException(
                "Illegal value for property: " + getPropertyName() + ": " + token);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
