package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;

import java.util.Arrays;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.Symbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [14] CharData ::= [^&lt;&amp;]* - ([^&lt;&amp;]* ']]&gt;' [^&lt;&amp;]*)
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CharData">
 *      CharData</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CharDataProduction extends XMLProduction {
    
    private final NonTerminalSymbol CharData;
    
    private final NonTerminalSymbol CharData_Plus;
    
    public CharDataProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
        super(grammar);
        CharData = this.grammar.getNonTerminalSymbol("CharData");
        CharData_Plus = this.grammar.getNonTerminalSymbol("CharData-Plus");
        
        final NonTerminalSymbol CharData_Single = this.grammar.getNonTerminalSymbol("CharData-Single");
        
        this.grammar.addProduction(CharData, stringHandler, CharData_Plus);
        this.grammar.addProduction(CharData, stringHandler, new Symbol[0]);
        
        this.grammar.addProduction(CharData_Plus, stringHandler, CharData_Plus, CharData_Single);
        this.grammar.addProduction(CharData_Plus, stringHandler, CharData_Single);
        
        this.grammar.addProduction(CharData_Single, characterHandler, tab);
        this.grammar.addProduction(CharData_Single, characterHandler, lineFeed);
        this.grammar.addProduction(CharData_Single, characterHandler, carriageReturn);
        this.grammar.addProduction(CharData_Single, characterHandler, space);
        this.grammar.addProduction(CharData_Single, characterHandler, exclamationMark);
        this.grammar.addProduction(CharData_Single, characterHandler, quotationMark);
        this.grammar.addProduction(CharData_Single, characterHandler, numberSign);
        this.grammar.addProduction(CharData_Single, characterHandler, dollarSign);
        this.grammar.addProduction(CharData_Single, characterHandler, percentSign);
        this.grammar.addProduction(CharData_Single, characterHandler, apostrophe);
        this.grammar.addProduction(CharData_Single, characterHandler, leftParenthesis);
        this.grammar.addProduction(CharData_Single, characterHandler, rightParenthesis);
        this.grammar.addProduction(CharData_Single, characterHandler, asterisk);
        this.grammar.addProduction(CharData_Single, characterHandler, plusSign);
        this.grammar.addProduction(CharData_Single, characterHandler, comma);
        this.grammar.addProduction(CharData_Single, characterHandler, hyphenMinus);
        this.grammar.addProduction(CharData_Single, characterHandler, fullStop);
        this.grammar.addProduction(CharData_Single, characterHandler, solidus);
        for (final UnicodeTerminalSymbols s : Arrays.asList(digitZero, digitOne, digitTwo, digitThree, digitFour,
                digitFive, digitSix, digitSeven, digitEight, digitNine)) {
            this.grammar.addProduction(CharData_Single, characterHandler, s);
        }
        this.grammar.addProduction(CharData_Single, characterHandler, colon);
        this.grammar.addProduction(CharData_Single, characterHandler, semicolon);
        this.grammar.addProduction(CharData_Single, characterHandler, equalsSign);
        this.grammar.addProduction(CharData_Single, characterHandler, questionMark);
        this.grammar.addProduction(CharData_Single, characterHandler, atSymbol);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalA, capitalB, capitalC, capitalD,
                capitalE, capitalF, capitalG, capitalH, capitalI, capitalJ, capitalK, capitalL, capitalM, capitalN,
                capitalO, capitalP, capitalQ, capitalR, capitalS, capitalT, capitalU, capitalV, capitalW, capitalX,
                capitalY, capitalZ }) {
            this.grammar.addProduction(CharData_Single, characterHandler, s);
        }
        this.grammar.addProduction(CharData_Single, characterHandler, leftBracket);
        this.grammar.addProduction(CharData_Single, characterHandler, reverseSolidus);
        this.grammar.addProduction(CharData_Single, characterHandler, circumflexAccent);
        this.grammar.addProduction(CharData_Single, characterHandler, lowLine);
        this.grammar.addProduction(CharData_Single, characterHandler, graveAccent);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallA, smallB, smallC, smallD, smallE,
                smallF, smallG, smallH, smallI, smallJ, smallK, smallL, smallM, smallN, smallO, smallP, smallQ, smallR,
                smallS, smallT, smallU, smallV, smallW, smallX, smallY, smallZ }) {
            this.grammar.addProduction(CharData_Single, characterHandler, s);
        }
        this.grammar.addProduction(CharData_Single, characterHandler, leftCurlyBrace);
        this.grammar.addProduction(CharData_Single, characterHandler, verticalBar);
        this.grammar.addProduction(CharData_Single, characterHandler, rightCurlyBrace);
        this.grammar.addProduction(CharData_Single, characterHandler, tilde);
        this.grammar.addProduction(CharData_Single, characterHandler, delete);
        this.grammar.addProduction(CharData_Single, characterHandler, belowSurrogates);
        this.grammar.addProduction(CharData_Single, characterHandler, aboveSurrogates);
        this.grammar.addProduction(CharData_Single, characterHandler, supplementaryMultilingualPlane);
        this.grammar.addProduction(CharData_Single, characterHandler, supplementaryIdeographicPlane);
        this.grammar.addProduction(CharData_Single, characterHandler, supplementaryUnassigned);
        this.grammar.addProduction(CharData_Single, characterHandler, supplementarySpecialPurposePlane);
        this.grammar.addProduction(CharData_Single, characterHandler, supplementaryPrivateUsePlanes);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return CharData;
    }
    
    public NonTerminalSymbol getPlus() {
        return CharData_Plus;
    }
    
}
