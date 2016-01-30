package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [54] AttType ::= {@linkplain StringTypeProduction StringType} |
 * {@linkplain TokenizedTypeProduction TokenizedType} |
 * {@linkplain EnumeratedTypeProduction EnumeratedType}
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-AttType">AttType
 *      </a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AttTypeProduction extends XMLProduction {
    
    private final NonTerminalSymbol AttType;
    
    public AttTypeProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final StringTypeProduction stringTypeProduction, final TokenizedTypeProduction tokenizedTypeProduction,
            final EnumeratedTypeProduction enumeratedTypeProduction) {
        super(grammar);
        AttType = this.grammar.getNonTerminalSymbol("AttType");
        
        final NonTerminalSymbol StringType = stringTypeProduction.getNonTerminalSymbol();
        final NonTerminalSymbol TokenizedType = tokenizedTypeProduction.getNonTerminalSymbol();
        final NonTerminalSymbol EnumeratedType = enumeratedTypeProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(AttType, nullHandler, StringType);
        this.grammar.addProduction(AttType, nullHandler, TokenizedType);
        this.grammar.addProduction(AttType, nullHandler, EnumeratedType);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return AttType;
    }
    
}
