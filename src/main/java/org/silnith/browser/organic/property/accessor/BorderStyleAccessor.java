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
import org.silnith.css.model.data.BorderStyle;
import org.silnith.css.model.data.PropertyName;


/**
 * An abstract base class for all the property accessors for the "border-*-style" properties.
 * <p>
 * Note that this is not an accessor for the "border-style" pseudo-property.
 * 
 * @author kent
 * @see BorderTopStyleAccessor
 * @see BorderRightStyleAccessor
 * @see BorderBottomStyleAccessor
 * @see BorderLeftStyleAccessor
 */
public abstract class BorderStyleAccessor extends PropertyAccessor<BorderStyle> {
    
    public BorderStyleAccessor(final PropertyName propertyName) {
        super(propertyName, false);
    }
    
    @Override
    public BorderStyle getInitialValue(final StyleData styleData) {
        return BorderStyle.NONE;
    }
    
    @Override
    protected BorderStyle parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token token = cssParser.parseComponentValue();
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                final BorderStyle borderStyle = BorderStyle.getFromValue(identToken.getStringValue());
                if (borderStyle == null) {
                    throw new IllegalArgumentException(
                            "Border style illegal value: " + getPropertyName() + ": " + specifiedValue);
                }
                return borderStyle;
            } // break;
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
