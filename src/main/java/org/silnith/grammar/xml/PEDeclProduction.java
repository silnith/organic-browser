package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalY;
import static org.silnith.grammar.UnicodeTerminalSymbols.exclamationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.percentSign;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [72] PEDecl ::= '&lt;!ENTITY' {@linkplain SProduction S} '%'
 * {@linkplain SProduction S} {@linkplain NameProduction Name}
 * {@linkplain SProduction S} {@linkplain PEDefProduction PEDef}
 * {@linkplain SProduction S}? '&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PEDecl">PEDecl
 *      </a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PEDeclProduction extends XMLProduction {
    
    private final NonTerminalSymbol PEDecl;
    
    public PEDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction,
            final NameProduction nameProduction, final PEDefProduction peDefProduction) {
        super(grammar);
        PEDecl = this.grammar.getNonTerminalSymbol("PEDecl");
        
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
        final NonTerminalSymbol PEDef = peDefProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(PEDecl, nullHandler, lessThanSign, exclamationMark, capitalE, capitalN, capitalT,
                capitalI, capitalT, capitalY, S, percentSign, S, Name, S, PEDef, S, greaterThanSign);
        this.grammar.addProduction(PEDecl, nullHandler, lessThanSign, exclamationMark, capitalE, capitalN, capitalT,
                capitalI, capitalT, capitalY, S, percentSign, S, Name, S, PEDef, greaterThanSign);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return PEDecl;
    }
    
}
