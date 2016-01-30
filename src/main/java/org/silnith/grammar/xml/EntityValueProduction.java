package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;

import java.util.Arrays;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [9] EntityValue ::= '"' ([^%&amp;"] | {@linkplain PEReferenceProduction
 * PEReference} | {@linkplain ReferenceProduction Reference})* '"' | "'"
 * ([^%&amp;'] | {@linkplain PEReferenceProduction PEReference} |
 * {@linkplain ReferenceProduction Reference})* "'"
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EntityValue">
 *      EntityValue</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EntityValueProduction extends XMLProduction {
    
    private final NonTerminalSymbol EntityValue;
    
    public EntityValueProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final PEReferenceProduction peReferenceProduction, final ReferenceProduction referenceProduction) {
        super(grammar);
        EntityValue = this.grammar.getNonTerminalSymbol("EntityValue");
        
        final NonTerminalSymbol EntityValue_Char = this.grammar.getNonTerminalSymbol("EntityValue-Char");
        final NonTerminalSymbol EntityValue_inner_quot = this.grammar.getNonTerminalSymbol("EntityValue-inner-quot");
        final NonTerminalSymbol EntityValue_inner_apos = this.grammar.getNonTerminalSymbol("EntityValue-inner-apos");
        final NonTerminalSymbol EntityValue_inner_quot_Plus =
                this.grammar.getNonTerminalSymbol("EntityValue-inner-quot-Plus");
        final NonTerminalSymbol EntityValue_inner_apos_Plus =
                this.grammar.getNonTerminalSymbol("EntityValue-inner-apos-Plus");
                
        final NonTerminalSymbol PEReference = peReferenceProduction.getNonTerminalSymbol();
        final NonTerminalSymbol Reference = referenceProduction.getNonTerminalSymbol();
        
        this.grammar.addProduction(EntityValue, nullHandler, quotationMark, EntityValue_inner_quot_Plus, quotationMark);
        this.grammar.addProduction(EntityValue, nullHandler, apostrophe, EntityValue_inner_apos_Plus, apostrophe);
        
        this.grammar.addProduction(EntityValue_inner_quot_Plus, nullHandler, EntityValue_inner_quot,
                EntityValue_inner_quot_Plus);
        this.grammar.addProduction(EntityValue_inner_quot_Plus, nullHandler, EntityValue_inner_quot);
        
        this.grammar.addProduction(EntityValue_inner_apos_Plus, nullHandler, EntityValue_inner_apos,
                EntityValue_inner_apos_Plus);
        this.grammar.addProduction(EntityValue_inner_apos_Plus, nullHandler, EntityValue_inner_apos);
        
        this.grammar.addProduction(EntityValue_inner_quot, nullHandler, EntityValue_Char);
        this.grammar.addProduction(EntityValue_inner_quot, nullHandler, PEReference);
        this.grammar.addProduction(EntityValue_inner_quot, nullHandler, Reference);
        this.grammar.addProduction(EntityValue_inner_quot, nullHandler, apostrophe);
        
        this.grammar.addProduction(EntityValue_inner_apos, nullHandler, EntityValue_Char);
        this.grammar.addProduction(EntityValue_inner_apos, nullHandler, PEReference);
        this.grammar.addProduction(EntityValue_inner_apos, nullHandler, Reference);
        this.grammar.addProduction(EntityValue_inner_apos, nullHandler, quotationMark);
        
        this.grammar.addProduction(EntityValue_Char, characterHandler, tab);
        this.grammar.addProduction(EntityValue_Char, characterHandler, lineFeed);
        this.grammar.addProduction(EntityValue_Char, characterHandler, carriageReturn);
        this.grammar.addProduction(EntityValue_Char, characterHandler, space);
        this.grammar.addProduction(EntityValue_Char, characterHandler, exclamationMark);
        this.grammar.addProduction(EntityValue_Char, characterHandler, numberSign);
        this.grammar.addProduction(EntityValue_Char, characterHandler, dollarSign);
        this.grammar.addProduction(EntityValue_Char, characterHandler, leftParenthesis);
        this.grammar.addProduction(EntityValue_Char, characterHandler, rightParenthesis);
        this.grammar.addProduction(EntityValue_Char, characterHandler, asterisk);
        this.grammar.addProduction(EntityValue_Char, characterHandler, plusSign);
        this.grammar.addProduction(EntityValue_Char, characterHandler, comma);
        this.grammar.addProduction(EntityValue_Char, characterHandler, hyphenMinus);
        this.grammar.addProduction(EntityValue_Char, characterHandler, fullStop);
        this.grammar.addProduction(EntityValue_Char, characterHandler, solidus);
        for (final UnicodeTerminalSymbols s : Arrays.asList(digitZero, digitOne, digitTwo, digitThree, digitFour,
                digitFive, digitSix, digitSeven, digitEight, digitNine)) {
            this.grammar.addProduction(EntityValue_Char, characterHandler, s);
        }
        this.grammar.addProduction(EntityValue_Char, characterHandler, colon);
        this.grammar.addProduction(EntityValue_Char, characterHandler, semicolon);
        this.grammar.addProduction(EntityValue_Char, characterHandler, lessThanSign);
        this.grammar.addProduction(EntityValue_Char, characterHandler, equalsSign);
        this.grammar.addProduction(EntityValue_Char, characterHandler, greaterThanSign);
        this.grammar.addProduction(EntityValue_Char, characterHandler, questionMark);
        this.grammar.addProduction(EntityValue_Char, characterHandler, atSymbol);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalA, capitalB, capitalC, capitalD,
                capitalE, capitalF, capitalG, capitalH, capitalI, capitalJ, capitalK, capitalL, capitalM, capitalN,
                capitalO, capitalP, capitalQ, capitalR, capitalS, capitalT, capitalU, capitalV, capitalW, capitalX,
                capitalY, capitalZ }) {
            this.grammar.addProduction(EntityValue_Char, characterHandler, s);
        }
        this.grammar.addProduction(EntityValue_Char, characterHandler, leftBracket);
        this.grammar.addProduction(EntityValue_Char, characterHandler, reverseSolidus);
        this.grammar.addProduction(EntityValue_Char, characterHandler, rightBracket);
        this.grammar.addProduction(EntityValue_Char, characterHandler, circumflexAccent);
        this.grammar.addProduction(EntityValue_Char, characterHandler, lowLine);
        this.grammar.addProduction(EntityValue_Char, characterHandler, graveAccent);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallA, smallB, smallC, smallD, smallE,
                smallF, smallG, smallH, smallI, smallJ, smallK, smallL, smallM, smallN, smallO, smallP, smallQ, smallR,
                smallS, smallT, smallU, smallV, smallW, smallX, smallY, smallZ }) {
            this.grammar.addProduction(EntityValue_Char, characterHandler, s);
        }
        this.grammar.addProduction(EntityValue_Char, characterHandler, leftCurlyBrace);
        this.grammar.addProduction(EntityValue_Char, characterHandler, verticalBar);
        this.grammar.addProduction(EntityValue_Char, characterHandler, rightCurlyBrace);
        this.grammar.addProduction(EntityValue_Char, characterHandler, tilde);
        this.grammar.addProduction(EntityValue_Char, characterHandler, delete);
        this.grammar.addProduction(EntityValue_Char, characterHandler, belowSurrogates);
        this.grammar.addProduction(EntityValue_Char, characterHandler, aboveSurrogates);
        this.grammar.addProduction(EntityValue_Char, characterHandler, supplementaryMultilingualPlane);
        this.grammar.addProduction(EntityValue_Char, characterHandler, supplementaryIdeographicPlane);
        this.grammar.addProduction(EntityValue_Char, characterHandler, supplementaryUnassigned);
        this.grammar.addProduction(EntityValue_Char, characterHandler, supplementarySpecialPurposePlane);
        this.grammar.addProduction(EntityValue_Char, characterHandler, supplementaryPrivateUsePlanes);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return EntityValue;
    }
    
}
