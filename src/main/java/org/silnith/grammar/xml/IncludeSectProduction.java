package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalC;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalD;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalL;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalU;
import static org.silnith.grammar.UnicodeTerminalSymbols.exclamationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftBracket;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.rightBracket;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [62] includeSect ::= '&lt;![' {@linkplain SProduction S}? 'INCLUDE'
 * {@linkplain SProduction S}? '[' {@linkplain ExtSubsetDeclProduction
 * extSubsetDecl} ']]&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-includeSect">
 *      includeSect</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class IncludeSectProduction extends XMLProduction {
    
    private final NonTerminalSymbol includeSect;
    
    public IncludeSectProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction,
            final ExtSubsetDeclProduction extSubsetDeclProduction) {
        super(grammar);
        includeSect = this.grammar.getNonTerminalSymbol("includeSect");
        
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        final NonTerminalSymbol extSubsetDecl = extSubsetDeclProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(includeSect, nullHandler, lessThanSign, exclamationMark, leftBracket, S, capitalI,
                capitalN, capitalC, capitalL, capitalU, capitalD, capitalE, S, leftBracket, extSubsetDecl, rightBracket,
                rightBracket, greaterThanSign);
        this.grammar.addProduction(includeSect, nullHandler, lessThanSign, exclamationMark, leftBracket, capitalI,
                capitalN, capitalC, capitalL, capitalU, capitalD, capitalE, S, leftBracket, extSubsetDecl, rightBracket,
                rightBracket, greaterThanSign);
        this.grammar.addProduction(includeSect, nullHandler, lessThanSign, exclamationMark, leftBracket, S, capitalI,
                capitalN, capitalC, capitalL, capitalU, capitalD, capitalE, leftBracket, extSubsetDecl, rightBracket,
                rightBracket, greaterThanSign);
        this.grammar.addProduction(includeSect, nullHandler, lessThanSign, exclamationMark, leftBracket, capitalI,
                capitalN, capitalC, capitalL, capitalU, capitalD, capitalE, leftBracket, extSubsetDecl, rightBracket,
                rightBracket, greaterThanSign);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return includeSect;
    }
    
}
