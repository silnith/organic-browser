package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;

import java.util.Arrays;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.PI;
import org.silnith.grammar.xml.syntax.PITarget;


/**
 * [16] PI ::= '&lt;?' {@linkplain PITargetProduction PITarget} (
 * {@linkplain SProduction S} ({@linkplain CharProduction Char}* - (
 * {@linkplain CharProduction Char}* '?&gt;' {@linkplain CharProduction Char}
 * *)))? '?&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PI">PI</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PIProduction extends XMLProduction {
    
    private static class PIHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final PI pi = new PI();
            pi.piTarget = (PITarget) rightHandSide.get(2);
            pi.content = (String) rightHandSide.get(4);
            return pi;
        }
        
    }
    
    private static class EmptyPIHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final PI pi = new PI();
            pi.piTarget = (PITarget) rightHandSide.get(2);
            return pi;
        }
        
    }
    
    private final NonTerminalSymbol PI;
    
    public PIProduction(final Grammar<UnicodeTerminalSymbols> grammar, final PITargetProduction piTargetProduction,
            final SProduction sProduction) {
        super(grammar);
        PI = this.grammar.getNonTerminalSymbol("PI");
        
        final NonTerminalSymbol PI_inner = this.grammar.getNonTerminalSymbol("PI-inner");
        final NonTerminalSymbol PI_Char = this.grammar.getNonTerminalSymbol("PI-Char");
        final NonTerminalSymbol PI_Char_Plus = this.grammar.getNonTerminalSymbol("PI-Char-Plus");
        
        final NonTerminalSymbol PITarget = piTargetProduction.getNonTerminalSymbol();
        final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
        
        final PIHandler piHandler = new PIHandler();
        final EmptyPIHandler emptyPIHandler = new EmptyPIHandler();
        this.grammar.addProduction(PI, piHandler, lessThanSign, questionMark, PITarget, S, PI_inner, questionMark,
                greaterThanSign);
        this.grammar.addProduction(PI, emptyPIHandler, lessThanSign, questionMark, PITarget, S, questionMark,
                greaterThanSign);
        this.grammar.addProduction(PI, emptyPIHandler, lessThanSign, questionMark, PITarget, questionMark,
                greaterThanSign);
                
        this.grammar.addProduction(PI_inner, stringHandler, PI_Char_Plus, S, PI_inner);
        this.grammar.addProduction(PI_inner, stringHandler, PI_Char_Plus);
        
        this.grammar.addProduction(PI_Char_Plus, stringHandler, PI_Char_Plus, PI_Char);
//		this.grammar.addProduction(PI_Char_Plus, PI_Char_Plus, literalCharacterQuestionMark, PI_Char);
        this.grammar.addProduction(PI_Char_Plus, nullHandler, PI_Char_Plus, greaterThanSign);
        this.grammar.addProduction(PI_Char_Plus, stringHandler, PI_Char);
//		this.grammar.addProduction(PI_Char_Plus, literalCharacterQuestionMark, PI_Char);
        this.grammar.addProduction(PI_Char_Plus, nullHandler, greaterThanSign);
        
        this.grammar.addProduction(PI_Char, characterHandler, exclamationMark);
        this.grammar.addProduction(PI_Char, characterHandler, quotationMark);
        this.grammar.addProduction(PI_Char, characterHandler, numberSign);
        this.grammar.addProduction(PI_Char, characterHandler, dollarSign);
        this.grammar.addProduction(PI_Char, characterHandler, percentSign);
        this.grammar.addProduction(PI_Char, characterHandler, ampersand);
        this.grammar.addProduction(PI_Char, characterHandler, apostrophe);
        this.grammar.addProduction(PI_Char, characterHandler, leftParenthesis);
        this.grammar.addProduction(PI_Char, characterHandler, rightParenthesis);
        this.grammar.addProduction(PI_Char, characterHandler, asterisk);
        this.grammar.addProduction(PI_Char, characterHandler, plusSign);
        this.grammar.addProduction(PI_Char, characterHandler, comma);
        this.grammar.addProduction(PI_Char, characterHandler, hyphenMinus);
        this.grammar.addProduction(PI_Char, characterHandler, fullStop);
        this.grammar.addProduction(PI_Char, characterHandler, solidus);
        for (final UnicodeTerminalSymbols s : Arrays.asList(digitZero, digitOne, digitTwo, digitThree, digitFour,
                digitFive, digitSix, digitSeven, digitEight, digitNine)) {
            this.grammar.addProduction(PI_Char, characterHandler, s);
        }
        this.grammar.addProduction(PI_Char, characterHandler, colon);
        this.grammar.addProduction(PI_Char, characterHandler, semicolon);
        this.grammar.addProduction(PI_Char, characterHandler, lessThanSign);
        this.grammar.addProduction(PI_Char, characterHandler, equalsSign);
        this.grammar.addProduction(PI_Char, characterHandler, atSymbol);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalA, capitalB, capitalC, capitalD,
                capitalE, capitalF, capitalG, capitalH, capitalI, capitalJ, capitalK, capitalL, capitalM, capitalN,
                capitalO, capitalP, capitalQ, capitalR, capitalS, capitalT, capitalU, capitalV, capitalW, capitalX,
                capitalY, capitalZ }) {
            this.grammar.addProduction(PI_Char, characterHandler, s);
        }
        this.grammar.addProduction(PI_Char, characterHandler, leftBracket);
        this.grammar.addProduction(PI_Char, characterHandler, reverseSolidus);
        this.grammar.addProduction(PI_Char, characterHandler, rightBracket);
        this.grammar.addProduction(PI_Char, characterHandler, circumflexAccent);
        this.grammar.addProduction(PI_Char, characterHandler, lowLine);
        this.grammar.addProduction(PI_Char, characterHandler, graveAccent);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallA, smallB, smallC, smallD, smallE,
                smallF, smallG, smallH, smallI, smallJ, smallK, smallL, smallM, smallN, smallO, smallP, smallQ, smallR,
                smallS, smallT, smallU, smallV, smallW, smallX, smallY, smallZ }) {
            this.grammar.addProduction(PI_Char, characterHandler, s);
        }
        this.grammar.addProduction(PI_Char, characterHandler, leftCurlyBrace);
        this.grammar.addProduction(PI_Char, characterHandler, verticalBar);
        this.grammar.addProduction(PI_Char, characterHandler, rightCurlyBrace);
        this.grammar.addProduction(PI_Char, characterHandler, tilde);
        this.grammar.addProduction(PI_Char, characterHandler, delete);
        this.grammar.addProduction(PI_Char, characterHandler, belowSurrogates);
        this.grammar.addProduction(PI_Char, characterHandler, aboveSurrogates);
        this.grammar.addProduction(PI_Char, characterHandler, supplementaryMultilingualPlane);
        this.grammar.addProduction(PI_Char, characterHandler, supplementaryIdeographicPlane);
        this.grammar.addProduction(PI_Char, characterHandler, supplementaryUnassigned);
        this.grammar.addProduction(PI_Char, characterHandler, supplementarySpecialPurposePlane);
        this.grammar.addProduction(PI_Char, characterHandler, supplementaryPrivateUsePlanes);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return PI;
    }
    
}
