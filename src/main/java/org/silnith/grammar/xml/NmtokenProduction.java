package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [7] Nmtoken ::= ({@linkplain NameCharProduction NameChar})+
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Nmtoken">Nmtoken
 *      </a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class NmtokenProduction extends XMLProduction {
    
    private final NonTerminalSymbol Nmtoken;
    
    public NmtokenProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final NameCharProduction nameCharProduction) {
        super(grammar);
        Nmtoken = this.grammar.getNonTerminalSymbol("Nmtoken");
        
        final NonTerminalSymbol NameChar_Plus = nameCharProduction.getPlus();
        
        this.grammar.addProduction(Nmtoken, nullHandler, NameChar_Plus);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return Nmtoken;
    }
    
}
