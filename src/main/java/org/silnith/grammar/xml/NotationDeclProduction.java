package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalA;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalO;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.exclamationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [82] NotationDecl ::= '&lt;!NOTATION' {@linkplain SProduction S}
 * {@linkplain NameProduction Name} {@linkplain SProduction S} (
 * {@linkplain ExternalIDProduction ExternalID} | {@linkplain PublicIDProduction
 * PublicID}) {@linkplain SProduction S}? '&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-NotationDecl">
 *      NotationDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class NotationDeclProduction extends XMLProduction {
    
    private final NonTerminalSymbol NotationDecl;
    
    public NotationDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction,
            final NameProduction nameProduction, final ExternalIDProduction externalIDProduction,
            final PublicIDProduction publicIDProduction) {
        super(grammar);
        NotationDecl = this.grammar.getNonTerminalSymbol("NotationDecl");
        
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
        final NonTerminalSymbol ExternalID = externalIDProduction.getNonTerminalSymbol();
        final NonTerminalSymbol PublicID = publicIDProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(NotationDecl, nullHandler, lessThanSign, exclamationMark, capitalN, capitalO,
                capitalT, capitalA, capitalT, capitalI, capitalO, capitalN, S, Name, S, ExternalID, S, greaterThanSign);
        this.grammar.addProduction(NotationDecl, nullHandler, lessThanSign, exclamationMark, capitalN, capitalO,
                capitalT, capitalA, capitalT, capitalI, capitalO, capitalN, S, Name, S, ExternalID, greaterThanSign);
        this.grammar.addProduction(NotationDecl, nullHandler, lessThanSign, exclamationMark, capitalN, capitalO,
                capitalT, capitalA, capitalT, capitalI, capitalO, capitalN, S, Name, S, PublicID, S, greaterThanSign);
        this.grammar.addProduction(NotationDecl, nullHandler, lessThanSign, exclamationMark, capitalN, capitalO,
                capitalT, capitalA, capitalT, capitalI, capitalO, capitalN, S, Name, S, PublicID, greaterThanSign);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return NotationDecl;
    }
    
}
