package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalG;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalO;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalR;
import static org.silnith.grammar.UnicodeTerminalSymbols.exclamationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftBracket;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.rightBracket;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [63] ignoreSect ::= '&lt;![' {@linkplain SProduction S}? 'IGNORE'
 * {@linkplain SProduction S}? '[' {@linkplain IgnoreSectContentsProduction
 * ignoreSectContents}* ']]&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-ignoreSect">
 *      ignoreSect</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class IgnoreSectProduction extends XMLProduction {
    
    private final NonTerminalSymbol ignoreSect;
    
    public IgnoreSectProduction(final Grammar<UnicodeTerminalSymbols> grammar, final SProduction sProduction,
            final IgnoreSectContentsProduction ignoreSectContentsProduction) {
        super(grammar);
        ignoreSect = this.grammar.getNonTerminalSymbol("ignoreSect");
        
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        final NonTerminalSymbol ignoreSectContents_Plus = ignoreSectContentsProduction.getPlus();
        
        this.grammar.addProduction(ignoreSect, nullHandler, lessThanSign, exclamationMark, leftBracket, S, capitalI,
                capitalG, capitalN, capitalO, capitalR, capitalE, S, leftBracket, ignoreSectContents_Plus, rightBracket,
                rightBracket, greaterThanSign);
        this.grammar.addProduction(ignoreSect, nullHandler, lessThanSign, exclamationMark, leftBracket, S, capitalI,
                capitalG, capitalN, capitalO, capitalR, capitalE, S, leftBracket, rightBracket, rightBracket,
                greaterThanSign);
        this.grammar.addProduction(ignoreSect, nullHandler, lessThanSign, exclamationMark, leftBracket, capitalI,
                capitalG, capitalN, capitalO, capitalR, capitalE, S, leftBracket, ignoreSectContents_Plus, rightBracket,
                rightBracket, greaterThanSign);
        this.grammar.addProduction(ignoreSect, nullHandler, lessThanSign, exclamationMark, leftBracket, capitalI,
                capitalG, capitalN, capitalO, capitalR, capitalE, S, leftBracket, rightBracket, rightBracket,
                greaterThanSign);
        this.grammar.addProduction(ignoreSect, nullHandler, lessThanSign, exclamationMark, leftBracket, S, capitalI,
                capitalG, capitalN, capitalO, capitalR, capitalE, leftBracket, ignoreSectContents_Plus, rightBracket,
                rightBracket, greaterThanSign);
        this.grammar.addProduction(ignoreSect, nullHandler, lessThanSign, exclamationMark, leftBracket, S, capitalI,
                capitalG, capitalN, capitalO, capitalR, capitalE, leftBracket, rightBracket, rightBracket,
                greaterThanSign);
        this.grammar.addProduction(ignoreSect, nullHandler, lessThanSign, exclamationMark, leftBracket, capitalI,
                capitalG, capitalN, capitalO, capitalR, capitalE, leftBracket, ignoreSectContents_Plus, rightBracket,
                rightBracket, greaterThanSign);
        this.grammar.addProduction(ignoreSect, nullHandler, lessThanSign, exclamationMark, leftBracket, capitalI,
                capitalG, capitalN, capitalO, capitalR, capitalE, leftBracket, rightBracket, rightBracket,
                greaterThanSign);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return ignoreSect;
    }
    
}
