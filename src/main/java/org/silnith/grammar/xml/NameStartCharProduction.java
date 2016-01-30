package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalA;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalB;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalC;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalD;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalF;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalG;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalH;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalJ;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalK;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalL;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalM;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalO;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalP;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalQ;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalR;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalS;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalU;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalV;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalW;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalX;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalY;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalZ;
import static org.silnith.grammar.UnicodeTerminalSymbols.colon;
import static org.silnith.grammar.UnicodeTerminalSymbols.lowLine;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallA;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallB;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallC;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallD;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallE;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallF;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallG;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallH;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallI;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallJ;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallK;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallL;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallM;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallN;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallO;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallP;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallQ;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallR;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallS;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallT;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallU;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallV;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallW;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallX;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallY;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallZ;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [4] NameStartChar ::= ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] |
 * [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] |
 * [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] |
 * [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-NameStartChar">
 *      NameStartChar</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class NameStartCharProduction extends XMLProduction {
    
    private final NonTerminalSymbol NameStartChar;
    
    public NameStartCharProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
        super(grammar);
        NameStartChar = this.grammar.getNonTerminalSymbol("NameStartChar");
        
        this.grammar.addProduction(NameStartChar, characterHandler, colon);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalA, capitalB, capitalC, capitalD,
                capitalE, capitalF, capitalG, capitalH, capitalI, capitalJ, capitalK, capitalL, capitalM, capitalN,
                capitalO, capitalP, capitalQ, capitalR, capitalS, capitalT, capitalU, capitalV, capitalW, capitalX,
                capitalY, capitalZ }) {
            this.grammar.addProduction(NameStartChar, characterHandler, s);
        }
        this.grammar.addProduction(NameStartChar, characterHandler, lowLine);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallA, smallB, smallC, smallD, smallE,
                smallF, smallG, smallH, smallI, smallJ, smallK, smallL, smallM, smallN, smallO, smallP, smallQ, smallR,
                smallS, smallT, smallU, smallV, smallW, smallX, smallY, smallZ }) {
            this.grammar.addProduction(NameStartChar, characterHandler, s);
        }
        // TODO: upper characters
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return NameStartChar;
    }
    
}
