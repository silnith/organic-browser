package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.percentSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.semicolon;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [69] PEReference ::= '%' {@linkplain NameProduction Name} ';'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PEReference">
 *      PEReference</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PEReferenceProduction extends XMLProduction {
    
    private final NonTerminalSymbol PEReference;
    
    public PEReferenceProduction(final Grammar<UnicodeTerminalSymbols> grammar, final NameProduction nameProduction) {
        super(grammar);
        PEReference = this.grammar.getNonTerminalSymbol("PEReference");
        
        final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(PEReference, nullHandler, percentSign, Name, semicolon);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return PEReference;
    }
    
}
