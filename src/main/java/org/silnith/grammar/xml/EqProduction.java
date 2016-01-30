package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.equalsSign;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [25] Eq ::= {@linkplain SProduction S}? '=' {@linkplain SProduction S}?
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Eq">Eq</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EqProduction extends XMLProduction {
    
    private final NonTerminalSymbol Eq;
    
    public EqProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction) {
        super(grammar);
        this.Eq = this.grammar.getNonTerminalSymbol("Eq");
        
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(Eq, nullHandler, S, equalsSign, S);
        this.grammar.addProduction(Eq, nullHandler, S, equalsSign);
        this.grammar.addProduction(Eq, nullHandler, equalsSign, S);
        this.grammar.addProduction(Eq, nullHandler, equalsSign);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return Eq;
    }
    
}
