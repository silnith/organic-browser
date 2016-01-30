package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;

import java.util.Arrays;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [2] Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] |
 * [#x10000-#x10FFFF]
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Char">Char</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CharProduction extends XMLProduction {
    
    private final NonTerminalSymbol Char;
    
    public CharProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
        super(grammar);
        Char = this.grammar.getNonTerminalSymbol("Char");
        
        this.grammar.addProduction(Char, characterHandler, tab);
        this.grammar.addProduction(Char, characterHandler, lineFeed);
        this.grammar.addProduction(Char, characterHandler, carriageReturn);
        this.grammar.addProduction(Char, characterHandler, space);
        this.grammar.addProduction(Char, characterHandler, exclamationMark);
        this.grammar.addProduction(Char, characterHandler, quotationMark);
        this.grammar.addProduction(Char, characterHandler, numberSign);
        this.grammar.addProduction(Char, characterHandler, dollarSign);
        this.grammar.addProduction(Char, characterHandler, percentSign);
        this.grammar.addProduction(Char, characterHandler, ampersand);
        this.grammar.addProduction(Char, characterHandler, apostrophe);
        this.grammar.addProduction(Char, characterHandler, leftParenthesis);
        this.grammar.addProduction(Char, characterHandler, rightParenthesis);
        this.grammar.addProduction(Char, characterHandler, asterisk);
        this.grammar.addProduction(Char, characterHandler, plusSign);
        this.grammar.addProduction(Char, characterHandler, comma);
        this.grammar.addProduction(Char, characterHandler, hyphenMinus);
        this.grammar.addProduction(Char, characterHandler, fullStop);
        this.grammar.addProduction(Char, characterHandler, solidus);
        for (final UnicodeTerminalSymbols s : Arrays.asList(digitZero, digitOne, digitTwo, digitThree, digitFour,
                digitFive, digitSix, digitSeven, digitEight, digitNine)) {
            this.grammar.addProduction(Char, characterHandler, s);
        }
        this.grammar.addProduction(Char, characterHandler, colon);
        this.grammar.addProduction(Char, characterHandler, semicolon);
        this.grammar.addProduction(Char, characterHandler, lessThanSign);
        this.grammar.addProduction(Char, characterHandler, equalsSign);
        this.grammar.addProduction(Char, characterHandler, greaterThanSign);
        this.grammar.addProduction(Char, characterHandler, questionMark);
        this.grammar.addProduction(Char, characterHandler, atSymbol);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalA, capitalB, capitalC, capitalD,
                capitalE, capitalF, capitalG, capitalH, capitalI, capitalJ, capitalK, capitalL, capitalM, capitalN,
                capitalO, capitalP, capitalQ, capitalR, capitalS, capitalT, capitalU, capitalV, capitalW, capitalX,
                capitalY, capitalZ }) {
            this.grammar.addProduction(Char, characterHandler, s);
        }
        this.grammar.addProduction(Char, characterHandler, leftBracket);
        this.grammar.addProduction(Char, characterHandler, reverseSolidus);
        this.grammar.addProduction(Char, characterHandler, rightBracket);
        this.grammar.addProduction(Char, characterHandler, circumflexAccent);
        this.grammar.addProduction(Char, characterHandler, lowLine);
        this.grammar.addProduction(Char, characterHandler, graveAccent);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallA, smallB, smallC, smallD, smallE,
                smallF, smallG, smallH, smallI, smallJ, smallK, smallL, smallM, smallN, smallO, smallP, smallQ, smallR,
                smallS, smallT, smallU, smallV, smallW, smallX, smallY, smallZ }) {
            this.grammar.addProduction(Char, characterHandler, s);
        }
        this.grammar.addProduction(Char, characterHandler, leftCurlyBrace);
        this.grammar.addProduction(Char, characterHandler, verticalBar);
        this.grammar.addProduction(Char, characterHandler, rightCurlyBrace);
        this.grammar.addProduction(Char, characterHandler, tilde);
        this.grammar.addProduction(Char, characterHandler, delete);
        this.grammar.addProduction(Char, characterHandler, belowSurrogates);
        this.grammar.addProduction(Char, characterHandler, aboveSurrogates);
        this.grammar.addProduction(Char, characterHandler, supplementaryMultilingualPlane);
        this.grammar.addProduction(Char, characterHandler, supplementaryIdeographicPlane);
        this.grammar.addProduction(Char, characterHandler, supplementaryUnassigned);
        this.grammar.addProduction(Char, characterHandler, supplementarySpecialPurposePlane);
        this.grammar.addProduction(Char, characterHandler, supplementaryPrivateUsePlanes);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return Char;
    }
    
}
