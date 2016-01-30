package org.silnith.grammar.xml;

import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [28a] DeclSep ::= {@linkplain PEReferenceProduction PEReference} |
 * {@linkplain SProduction S}
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-DeclSep">DeclSep
 *      </a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DeclSepProduction extends XMLProduction {
    
    private static class PassThroughHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            return rightHandSide.get(0);
        }
        
    }
    
    private final NonTerminalSymbol DeclSep;
    
    public DeclSepProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final PEReferenceProduction peReferenceProduction) {
        super(grammar);
        DeclSep = this.grammar.getNonTerminalSymbol("DeclSep");
        
        final NonTerminalSymbol PEReference = peReferenceProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(DeclSep, new PassThroughHandler(), PEReference);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return DeclSep;
    }
    
}
