package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.asterisk;
import static org.silnith.grammar.UnicodeTerminalSymbols.atSymbol;
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
import static org.silnith.grammar.UnicodeTerminalSymbols.carriageReturn;
import static org.silnith.grammar.UnicodeTerminalSymbols.colon;
import static org.silnith.grammar.UnicodeTerminalSymbols.comma;
import static org.silnith.grammar.UnicodeTerminalSymbols.digitEight;
import static org.silnith.grammar.UnicodeTerminalSymbols.digitFive;
import static org.silnith.grammar.UnicodeTerminalSymbols.digitFour;
import static org.silnith.grammar.UnicodeTerminalSymbols.digitNine;
import static org.silnith.grammar.UnicodeTerminalSymbols.digitOne;
import static org.silnith.grammar.UnicodeTerminalSymbols.digitSeven;
import static org.silnith.grammar.UnicodeTerminalSymbols.digitSix;
import static org.silnith.grammar.UnicodeTerminalSymbols.digitThree;
import static org.silnith.grammar.UnicodeTerminalSymbols.digitTwo;
import static org.silnith.grammar.UnicodeTerminalSymbols.digitZero;
import static org.silnith.grammar.UnicodeTerminalSymbols.dollarSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.equalsSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.exclamationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.fullStop;
import static org.silnith.grammar.UnicodeTerminalSymbols.hyphenMinus;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.lineFeed;
import static org.silnith.grammar.UnicodeTerminalSymbols.lowLine;
import static org.silnith.grammar.UnicodeTerminalSymbols.numberSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.percentSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.plusSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.questionMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.rightParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.semicolon;
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
import static org.silnith.grammar.UnicodeTerminalSymbols.solidus;
import static org.silnith.grammar.UnicodeTerminalSymbols.space;

import java.util.Arrays;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [13] PubidChar ::= #x20 | #xD | #xA | [a-zA-Z0-9] | [-()+,./:=?;!*#@$_%]
 * <p>
 * This production has been altered from the specification to remove the
 * apostrophe.
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PubidChar">
 *      PubidChar</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PubidCharProduction extends XMLProduction {
    
    private final NonTerminalSymbol PubidChar;
    
    private final NonTerminalSymbol PubidChar_Plus;
    
    public PubidCharProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
        super(grammar);
        PubidChar = this.grammar.getNonTerminalSymbol("PubidChar");
        PubidChar_Plus = this.grammar.getNonTerminalSymbol("PubidChar-Plus");
        
        this.grammar.addProduction(PubidChar_Plus, stringHandler, PubidChar);
        this.grammar.addProduction(PubidChar_Plus, stringHandler, PubidChar_Plus, PubidChar);
        
        this.grammar.addProduction(PubidChar, characterHandler, lineFeed);
        this.grammar.addProduction(PubidChar, characterHandler, carriageReturn);
        this.grammar.addProduction(PubidChar, characterHandler, space);
        this.grammar.addProduction(PubidChar, characterHandler, exclamationMark);
        this.grammar.addProduction(PubidChar, characterHandler, numberSign);
        this.grammar.addProduction(PubidChar, characterHandler, dollarSign);
        this.grammar.addProduction(PubidChar, characterHandler, percentSign);
        this.grammar.addProduction(PubidChar, characterHandler, leftParenthesis);
        this.grammar.addProduction(PubidChar, characterHandler, rightParenthesis);
        this.grammar.addProduction(PubidChar, characterHandler, asterisk);
        this.grammar.addProduction(PubidChar, characterHandler, plusSign);
        this.grammar.addProduction(PubidChar, characterHandler, comma);
        this.grammar.addProduction(PubidChar, characterHandler, hyphenMinus);
        this.grammar.addProduction(PubidChar, characterHandler, fullStop);
        this.grammar.addProduction(PubidChar, characterHandler, solidus);
        for (final UnicodeTerminalSymbols s : Arrays.asList(digitZero, digitOne, digitTwo, digitThree, digitFour,
                digitFive, digitSix, digitSeven, digitEight, digitNine)) {
            this.grammar.addProduction(PubidChar, characterHandler, s);
        }
        this.grammar.addProduction(PubidChar, characterHandler, colon);
        this.grammar.addProduction(PubidChar, characterHandler, semicolon);
        this.grammar.addProduction(PubidChar, characterHandler, equalsSign);
        this.grammar.addProduction(PubidChar, characterHandler, questionMark);
        this.grammar.addProduction(PubidChar, characterHandler, atSymbol);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalA, capitalB, capitalC, capitalD,
                capitalE, capitalF, capitalG, capitalH, capitalI, capitalJ, capitalK, capitalL, capitalM, capitalN,
                capitalO, capitalP, capitalQ, capitalR, capitalS, capitalT, capitalU, capitalV, capitalW, capitalX,
                capitalY, capitalZ }) {
            this.grammar.addProduction(PubidChar, characterHandler, s);
        }
        this.grammar.addProduction(PubidChar, characterHandler, lowLine);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallA, smallB, smallC, smallD, smallE,
                smallF, smallG, smallH, smallI, smallJ, smallK, smallL, smallM, smallN, smallO, smallP, smallQ, smallR,
                smallS, smallT, smallU, smallV, smallW, smallX, smallY, smallZ }) {
            this.grammar.addProduction(PubidChar, characterHandler, s);
        }
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return PubidChar;
    }
    
    public NonTerminalSymbol getPlus() {
        return PubidChar_Plus;
    }
    
}
