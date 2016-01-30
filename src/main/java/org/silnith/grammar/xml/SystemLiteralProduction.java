package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;

import java.util.Arrays;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [11] SystemLiteral ::= ('"' [^"]* '"') | ("'" [^']* "'")
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-SystemLiteral">
 *      SystemLiteral</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class SystemLiteralProduction extends XMLProduction {
    
    private static class EmptyStringHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            return "";
        }
        
    }
    
    private static class SecondElementHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            return rightHandSide.get(1);
        }
        
    }
    
    private final NonTerminalSymbol SystemLiteral;
    
    public SystemLiteralProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
        super(grammar);
        SystemLiteral = this.grammar.getNonTerminalSymbol("SystemLiteral");
        
        final NonTerminalSymbol SystemLiteral_inner_quot_Plus =
                this.grammar.getNonTerminalSymbol("SystemLiteral-inner-quot-Plus");
        final NonTerminalSymbol SystemLiteral_inner_apos_Plus =
                this.grammar.getNonTerminalSymbol("SystemLiteral-inner-apos-Plus");
        final NonTerminalSymbol SystemLiteral_Char = this.grammar.getNonTerminalSymbol("SystemLiteral-Char");
        
        final EmptyStringHandler emptyStringHandler = new EmptyStringHandler();
        final SecondElementHandler secondElementHandler = new SecondElementHandler();
        this.grammar.addProduction(SystemLiteral, secondElementHandler, quotationMark, SystemLiteral_inner_quot_Plus,
                quotationMark);
        this.grammar.addProduction(SystemLiteral, emptyStringHandler, quotationMark, quotationMark);
        this.grammar.addProduction(SystemLiteral, secondElementHandler, apostrophe, SystemLiteral_inner_apos_Plus,
                apostrophe);
        this.grammar.addProduction(SystemLiteral, emptyStringHandler, apostrophe, apostrophe);
        
        this.grammar.addProduction(SystemLiteral_inner_quot_Plus, stringHandler, SystemLiteral_inner_quot_Plus,
                SystemLiteral_Char);
        this.grammar.addProduction(SystemLiteral_inner_quot_Plus, stringHandler, SystemLiteral_inner_quot_Plus,
                apostrophe);
        this.grammar.addProduction(SystemLiteral_inner_quot_Plus, stringHandler, SystemLiteral_Char);
        this.grammar.addProduction(SystemLiteral_inner_quot_Plus, stringHandler, apostrophe);
        
        this.grammar.addProduction(SystemLiteral_inner_apos_Plus, stringHandler, SystemLiteral_inner_apos_Plus,
                SystemLiteral_Char);
        this.grammar.addProduction(SystemLiteral_inner_apos_Plus, stringHandler, SystemLiteral_inner_apos_Plus,
                quotationMark);
        this.grammar.addProduction(SystemLiteral_inner_apos_Plus, stringHandler, SystemLiteral_Char);
        this.grammar.addProduction(SystemLiteral_inner_apos_Plus, stringHandler, quotationMark);
        
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, tab);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, lineFeed);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, carriageReturn);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, space);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, exclamationMark);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, numberSign);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, dollarSign);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, percentSign);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, ampersand);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, leftParenthesis);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, rightParenthesis);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, asterisk);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, plusSign);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, comma);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, hyphenMinus);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, fullStop);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, solidus);
        for (final UnicodeTerminalSymbols s : Arrays.asList(digitZero, digitOne, digitTwo, digitThree, digitFour,
                digitFive, digitSix, digitSeven, digitEight, digitNine)) {
            this.grammar.addProduction(SystemLiteral_Char, characterHandler, s);
        }
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, colon);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, semicolon);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, lessThanSign);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, equalsSign);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, greaterThanSign);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, questionMark);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, atSymbol);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalA, capitalB, capitalC, capitalD,
                capitalE, capitalF, capitalG, capitalH, capitalI, capitalJ, capitalK, capitalL, capitalM, capitalN,
                capitalO, capitalP, capitalQ, capitalR, capitalS, capitalT, capitalU, capitalV, capitalW, capitalX,
                capitalY, capitalZ }) {
            this.grammar.addProduction(SystemLiteral_Char, characterHandler, s);
        }
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, leftBracket);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, reverseSolidus);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, rightBracket);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, circumflexAccent);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, lowLine);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, graveAccent);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallA, smallB, smallC, smallD, smallE,
                smallF, smallG, smallH, smallI, smallJ, smallK, smallL, smallM, smallN, smallO, smallP, smallQ, smallR,
                smallS, smallT, smallU, smallV, smallW, smallX, smallY, smallZ }) {
            this.grammar.addProduction(SystemLiteral_Char, characterHandler, s);
        }
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, leftCurlyBrace);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, verticalBar);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, rightCurlyBrace);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, tilde);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, delete);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, belowSurrogates);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, aboveSurrogates);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, supplementaryMultilingualPlane);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, supplementaryIdeographicPlane);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, supplementaryUnassigned);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, supplementarySpecialPurposePlane);
        this.grammar.addProduction(SystemLiteral_Char, characterHandler, supplementaryPrivateUsePlanes);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return SystemLiteral;
    }
    
}
