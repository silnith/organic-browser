package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.Symbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [28b] intSubset ::= ({@linkplain MarkupDeclProduction markupdecl} |
 * {@linkplain DeclSepProduction DeclSep})*
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-intSubset">
 *      intSubset</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class IntSubsetProduction extends XMLProduction {
    
    private final NonTerminalSymbol intSubset;
    
    public IntSubsetProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction,
            final MarkupDeclProduction markupDeclProduction, final DeclSepProduction declSepProduction) {
        super(grammar);
        intSubset = this.grammar.getNonTerminalSymbol("intSubset");
        
        final NonTerminalSymbol intSubset_inner = this.grammar.getNonTerminalSymbol("intSubset-inner");
        final NonTerminalSymbol intSubset_inner_Plus = this.grammar.getNonTerminalSymbol("intSubset-inner-Plus");
        
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        final NonTerminalSymbol markupdecl = markupDeclProduction.getNonTerminalSymbol();
        final NonTerminalSymbol DeclSep = declSepProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(intSubset, nullHandler, S, intSubset_inner_Plus);
        this.grammar.addProduction(intSubset, nullHandler, intSubset_inner_Plus);
        this.grammar.addProduction(intSubset, nullHandler, S);
        this.grammar.addProduction(intSubset, nullHandler, new Symbol[0]);
        
        this.grammar.addProduction(intSubset_inner_Plus, nullHandler, intSubset_inner, intSubset_inner_Plus);
        this.grammar.addProduction(intSubset_inner_Plus, nullHandler, intSubset_inner);
        
        this.grammar.addProduction(intSubset_inner, nullHandler, markupdecl, S);
        this.grammar.addProduction(intSubset_inner, nullHandler, markupdecl);
        this.grammar.addProduction(intSubset_inner, nullHandler, DeclSep, S);
        this.grammar.addProduction(intSubset_inner, nullHandler, DeclSep);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return intSubset;
    }
    
}
