package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [57] EnumeratedType ::= {@linkplain NotationTypeProduction NotationType} |
 * {@linkplain EnumerationProduction Enumeration}
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EnumeratedType">
 *      EnumeratedType</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EnumeratedTypeProduction extends XMLProduction {
    
    private final NonTerminalSymbol EnumeratedType;
    
    public EnumeratedTypeProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final NotationTypeProduction notationTypeProduction, final EnumerationProduction enumerationProduction) {
        super(grammar);
        EnumeratedType = this.grammar.getNonTerminalSymbol("EnumeratedType");
        
        final NonTerminalSymbol NotationType = notationTypeProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Enumeration = enumerationProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(EnumeratedType, nullHandler, NotationType);
        this.grammar.addProduction(EnumeratedType, nullHandler, Enumeration);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return EnumeratedType;
    }
    
}
