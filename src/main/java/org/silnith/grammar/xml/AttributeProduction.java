package org.silnith.grammar.xml;

import java.util.ArrayList;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.AttValue;
import org.silnith.grammar.xml.syntax.Attribute;


/**
 * [41] Attribute ::= {@linkplain NameProduction Name} {@linkplain EqProduction
 * Eq} {@linkplain AttValueProduction AttValue}
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Attribute">
 *      Attribute</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AttributeProduction extends XMLProduction {
    
    private static class AttributeHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final Attribute attribute = new Attribute();
            attribute.name = (String) rightHandSide.get(0);
            attribute.attValue = (AttValue) rightHandSide.get(2);
            return attribute;
        }
        
    }
    
    private static class AppendAttributeListHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final List<Attribute> list = (List<Attribute>) rightHandSide.get(0);
            list.add((Attribute) rightHandSide.get(2));
            return list;
        }
        
    }
    
    private static class NewAttributeListHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final List<Attribute> list = new ArrayList<>();
            list.add((Attribute) rightHandSide.get(1));
            return list;
        }
        
    }
    
    private final NonTerminalSymbol Attribute;
    
    private final NonTerminalSymbol AttributeList_Plus;
    
    public AttributeProduction(final Grammar<UnicodeTerminalSymbols> grammar, final NameProduction nameProduction,
            final EqProduction eqProduction, final AttValueProduction attValueProduction,
            final SProduction sProduction) {
        super(grammar);
        Attribute = this.grammar.getNonTerminalSymbol("Attribute");
        AttributeList_Plus = this.grammar.getNonTerminalSymbol("AttributeList_Plus");
        
        final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Eq = eqProduction.getNonTerminalSymbol();
        final NonTerminalSymbol AttValue = attValueProduction.getNonTerminalSymbol();
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(Attribute, new AttributeHandler(), Name, Eq, AttValue);
        
        this.grammar.addProduction(AttributeList_Plus, new AppendAttributeListHandler(), AttributeList_Plus, S,
                Attribute);
        this.grammar.addProduction(AttributeList_Plus, new NewAttributeListHandler(), S, Attribute);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return Attribute;
    }
    
    public NonTerminalSymbol getPlus() {
        return AttributeList_Plus;
    }
    
}
