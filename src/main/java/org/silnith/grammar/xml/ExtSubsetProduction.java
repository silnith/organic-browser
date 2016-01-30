package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [30] extSubset ::= {@linkplain TextDeclProduction TextDecl}?
 * {@linkplain ExtSubsetDeclProduction extSubsetDecl}
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-extSubset">
 *      extSubset</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ExtSubsetProduction extends XMLProduction {
    
    private final NonTerminalSymbol extSubset;
    
    public ExtSubsetProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final TextDeclProduction textDeclProduction, final ExtSubsetDeclProduction extSubsetDeclProduction) {
        super(grammar);
        extSubset = this.grammar.getNonTerminalSymbol("extSubset");
        
        final NonTerminalSymbol TextDecl = textDeclProduction.getNonTerminalSymbol();
        final NonTerminalSymbol extSubsetDecl = extSubsetDeclProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(extSubset, nullHandler, TextDecl, extSubsetDecl);
        this.grammar.addProduction(extSubset, nullHandler, extSubsetDecl);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return extSubset;
    }
    
}
