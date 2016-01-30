package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalA;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalM;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalP;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalY;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [46] contentspec ::= 'EMPTY' | 'ANY' | {@linkplain MixedProduction Mixed} |
 * {@linkplain ChildrenProduction children}
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-contentspec">
 *      contentspec</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ContentSpecProduction extends XMLProduction {
    
    private final NonTerminalSymbol contentspec;
    
    public ContentSpecProduction(final Grammar<UnicodeTerminalSymbols> grammar, final MixedProduction mixedProduction,
            final ChildrenProduction childrenProduction) {
        super(grammar);
        contentspec = this.grammar.getNonTerminalSymbol("contentspec");
        
        final NonTerminalSymbol Mixed = mixedProduction.getNonTerminalSymbol();
        final NonTerminalSymbol children = childrenProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(contentspec, nullHandler, capitalE, capitalM, capitalP, capitalT, capitalY);
        this.grammar.addProduction(contentspec, nullHandler, capitalA, capitalN, capitalY);
        this.grammar.addProduction(contentspec, nullHandler, Mixed);
        this.grammar.addProduction(contentspec, nullHandler, children);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return contentspec;
    }
    
}
