package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalA;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalL;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalS;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.exclamationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [52] AttlistDecl ::= '&lt;!ATTLIST' {@linkplain SProduction S}
 * {@linkplain NameProduction Name} {@linkplain AttDefProduction AttDef}*
 * {@linkplain SProduction S}? '&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-AttlistDecl">
 *      AttlistDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AttlistDeclProduction extends XMLProduction {
    
    private final NonTerminalSymbol AttlistDecl;
    
    public AttlistDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction,
            final NameProduction nameProduction, final AttDefProduction attDefProduction) {
        super(grammar);
        AttlistDecl = this.grammar.getNonTerminalSymbol("AttlistDecl");
        
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
        final NonTerminalSymbol AttDef_Plus = attDefProduction.getPlus();
        
        this.grammar.addProduction(AttlistDecl, nullHandler, lessThanSign, exclamationMark, capitalA, capitalT,
                capitalT, capitalL, capitalI, capitalS, capitalT, S, Name, S, AttDef_Plus, greaterThanSign);
        this.grammar.addProduction(AttlistDecl, nullHandler, lessThanSign, exclamationMark, capitalA, capitalT,
                capitalT, capitalL, capitalI, capitalS, capitalT, S, Name, S, greaterThanSign);
        this.grammar.addProduction(AttlistDecl, nullHandler, lessThanSign, exclamationMark, capitalA, capitalT,
                capitalT, capitalL, capitalI, capitalS, capitalT, S, Name, greaterThanSign);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return AttlistDecl;
    }
    
}
