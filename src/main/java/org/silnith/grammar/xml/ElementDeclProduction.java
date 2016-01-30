package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalL;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalM;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.exclamationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [45] elementdecl ::= '&lt;!ELEMENT' {@linkplain SProduction S}
 * {@linkplain NameProduction Name} {@linkplain SProduction S}
 * {@linkplain ContentSpecProduction contentspec} {@linkplain SProduction S}?
 * '&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-elementdecl">
 *      elementdecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ElementDeclProduction extends XMLProduction {
    
    final NonTerminalSymbol elementdecl;
    
    public ElementDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction,
            final NameProduction nameProduction, final ContentSpecProduction contentSpecProduction) {
        super(grammar);
        elementdecl = this.grammar.getNonTerminalSymbol("elementdecl");
        
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
        final NonTerminalSymbol contentspec = contentSpecProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(elementdecl, nullHandler, lessThanSign, exclamationMark, capitalE, capitalL,
                capitalE, capitalM, capitalE, capitalN, capitalT, S, Name, S, contentspec, S, greaterThanSign);
        this.grammar.addProduction(elementdecl, nullHandler, lessThanSign, exclamationMark, capitalE, capitalL,
                capitalE, capitalM, capitalE, capitalN, capitalT, S, Name, S, contentspec, greaterThanSign);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return elementdecl;
    }
    
}
