package org.silnith.grammar.xml;

import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [53] AttDef ::= {@linkplain SProduction S} {@linkplain NameProduction Name}
 * {@linkplain SProduction S} {@linkplain AttTypeProduction AttType}
 * {@linkplain SProduction S} {@linkplain DefaultDeclProduction DefaultDecl}
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-AttDef">AttDef
 *      </a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AttDefProduction extends XMLProduction {
    
    private static class ListHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            @SuppressWarnings("unchecked")
            final List<Object> list = (List<Object>) rightHandSide.get(2);
            list.add(0, rightHandSide.get(0));
            return list;
        }
        
    }
    
    private static class FirstElementHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            return rightHandSide.get(0);
        }
        
    }
    
    private final NonTerminalSymbol AttDef;
    
    private final NonTerminalSymbol AttDef_Plus;
    
    public AttDefProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction,
            final NameProduction nameProduction, final AttTypeProduction attTypeProduction,
            final DefaultDeclProduction defaultDeclProduction) {
        super(grammar);
        AttDef = this.grammar.getNonTerminalSymbol("AttDef");
        AttDef_Plus = this.grammar.getNonTerminalSymbol("AttDef_Plus");
        
        final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        final NonTerminalSymbol AttType = attTypeProduction.getNonTerminalSymbol();
        final NonTerminalSymbol DefaultDecl = defaultDeclProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(AttDef, nullHandler, Name, S, AttType, S, DefaultDecl);
        
        final FirstElementHandler firstElementHandler = new FirstElementHandler();
        this.grammar.addProduction(AttDef_Plus, new ListHandler(), AttDef, S, AttDef_Plus);
        this.grammar.addProduction(AttDef_Plus, firstElementHandler, AttDef, S);
        this.grammar.addProduction(AttDef_Plus, firstElementHandler, AttDef);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return AttDef;
    }
    
    public NonTerminalSymbol getPlus() {
        return AttDef_Plus;
    }
    
}
