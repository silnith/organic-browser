package org.silnith.grammar.xml;

import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.CharRef;
import org.silnith.grammar.xml.syntax.EntityRef;
import org.silnith.grammar.xml.syntax.Reference;


/**
 * [67] Reference ::= {@linkplain EntityRefProduction EntityRef} |
 * {@linkplain CharRefProduction CharRef}
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Reference">
 *      Reference</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ReferenceProduction extends XMLProduction {
    
    private static class EntityRefHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final Reference reference = new Reference();
            reference.entityRef = (EntityRef) rightHandSide.get(0);
            return reference;
        }
        
    }
    
    private static class CharRefHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final Reference reference = new Reference();
            reference.charRef = (CharRef) rightHandSide.get(0);
            return reference;
        }
        
    }
    
    private final NonTerminalSymbol Reference;
    
    public ReferenceProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final EntityRefProduction entityRefProduction, final CharRefProduction charRefProduction) {
        super(grammar);
        Reference = this.grammar.getNonTerminalSymbol("Reference");
        
        final NonTerminalSymbol EntityRef = entityRefProduction.getNonTerminalSymbol();
        final NonTerminalSymbol CharRef = charRefProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(Reference, new EntityRefHandler(), EntityRef);
        this.grammar.addProduction(Reference, new CharRefHandler(), CharRef);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return Reference;
    }
    
}
