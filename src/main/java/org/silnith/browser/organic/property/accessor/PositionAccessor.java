package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.Position;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;
import org.silnith.parser.css3.grammar.Parser;
import org.silnith.parser.css3.lexical.TokenListStream;
import org.silnith.parser.css3.lexical.token.IdentToken;
import org.silnith.parser.css3.lexical.token.LexicalToken;


/**
 * A property accessor for the "position" property.
 * 
 * @author kent
 * @see PropertyName#POSITION
 * @see Position
 */
public class PositionAccessor extends PropertyAccessor<Position> {
    
    public PositionAccessor() {
        super(PropertyName.POSITION, false);
    }
    
    @Override
    public Position getInitialValue(final StyleData styleData) {
        return Position.STATIC;
    }
    
    @Override
    protected Position parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token token = cssParser.parseComponentValue();
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                final Position position = Position.getFromValue(identToken.getStringValue());
                if (position != null) {
                    return position;
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
