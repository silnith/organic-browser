package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.AttValue;
import org.silnith.grammar.xml.syntax.CharRef;
import org.silnith.grammar.xml.syntax.EntityRef;
import org.silnith.grammar.xml.syntax.Reference;


/**
 * [10] AttValue ::= '"' ([^&lt;&amp;"] | {@linkplain ReferenceProduction
 * Reference})* '"' | "'" ([^&lt;&amp;'] | {@linkplain ReferenceProduction
 * Reference})* "'"
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-AttValue">
 *      AttValue</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AttValueProduction extends XMLProduction {
    
    private static class SecondElementHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final AttValue attValue = new AttValue();
            final List<Object> value = (List<Object>) rightHandSide.get(1);
            final Iterator<Object> iterator = value.iterator();
            StringBuilder currentText = new StringBuilder();
            final List<Object> contents = new ArrayList<>();
            while (iterator.hasNext()) {
                final Object obj = iterator.next();
                if (obj instanceof Reference) {
                    if (currentText.length() > 0) {
                        contents.add(currentText.toString());
                        currentText = new StringBuilder();
                    }
                    contents.add(obj);
                } else {
                    currentText.append(obj);
                }
                
            }
            if (currentText.length() > 0) {
                contents.add(currentText.toString());
            }
            attValue.contents = contents;
            return attValue;
        }
        
    }
    
    private static class EmptyStringHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final AttValue attValue = new AttValue();
            attValue.contents = new ArrayList<>();
            return attValue;
        }
        
    }
    
    private static class ReferenceHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final Reference reference = new Reference();
            final Object obj = rightHandSide.get(0);
            if (obj instanceof CharRef) {
                reference.charRef = (CharRef) obj;
            } else if (obj instanceof EntityRef) {
                reference.entityRef = (EntityRef) obj;
            } else {
                throw new IllegalStateException();
            }
            return reference;
        }
        
    }
    
    private static class PassThroughHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            return rightHandSide.get(0);
        }
        
    }
    
    private static class CreateListHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final List<Object> contents = new LinkedList<>();
            contents.add(rightHandSide.get(0));
            return contents;
        }
        
    }
    
    private static class AppendListHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final List<Object> contents = (List<Object>) rightHandSide.get(1);
            contents.add(0, rightHandSide.get(0));
            return contents;
        }
        
    }
    
    private final NonTerminalSymbol AttValue;
    
    public AttValueProduction(final Grammar<UnicodeTerminalSymbols> grammar,
            final ReferenceProduction referenceProduction) {
        super(grammar);
        AttValue = this.grammar.getNonTerminalSymbol("AttValue");
        
        final NonTerminalSymbol AttValue_inner_Plus = this.grammar.getNonTerminalSymbol("AttValue-inner-Plus");
        final NonTerminalSymbol AttValue_inner = this.grammar.getNonTerminalSymbol("AttValue-inner");
        final NonTerminalSymbol AttValue_Char = this.grammar.getNonTerminalSymbol("AttValue-Char");
        
        final NonTerminalSymbol Reference = referenceProduction.getNonTerminalSymbol();
        
        final EmptyStringHandler emptyStringHandler = new EmptyStringHandler();
        final SecondElementHandler secondElementHandler = new SecondElementHandler();
        this.grammar.addProduction(AttValue, secondElementHandler, quotationMark, AttValue_inner_Plus, quotationMark);
        this.grammar.addProduction(AttValue, emptyStringHandler, quotationMark, quotationMark);
        this.grammar.addProduction(AttValue, secondElementHandler, apostrophe, AttValue_inner_Plus, apostrophe);
        this.grammar.addProduction(AttValue, emptyStringHandler, apostrophe, apostrophe);
        
        this.grammar.addProduction(AttValue_inner_Plus, new AppendListHandler(), AttValue_inner, AttValue_inner_Plus);
        this.grammar.addProduction(AttValue_inner_Plus, new CreateListHandler(), AttValue_inner);
        
        final PassThroughHandler passThroughHandler = new PassThroughHandler();
        this.grammar.addProduction(AttValue_inner, passThroughHandler, AttValue_Char);
        this.grammar.addProduction(AttValue_inner, passThroughHandler, Reference);
//		this.grammar.addProduction(AttValue_inner, new ReferenceHandler(), Reference);
        
        this.grammar.addProduction(AttValue_Char, characterHandler, tab);
        this.grammar.addProduction(AttValue_Char, characterHandler, lineFeed);
        this.grammar.addProduction(AttValue_Char, characterHandler, carriageReturn);
        this.grammar.addProduction(AttValue_Char, characterHandler, space);
        this.grammar.addProduction(AttValue_Char, characterHandler, exclamationMark);
        this.grammar.addProduction(AttValue_Char, characterHandler, numberSign);
        this.grammar.addProduction(AttValue_Char, characterHandler, dollarSign);
        this.grammar.addProduction(AttValue_Char, characterHandler, percentSign);
        this.grammar.addProduction(AttValue_Char, characterHandler, leftParenthesis);
        this.grammar.addProduction(AttValue_Char, characterHandler, rightParenthesis);
        this.grammar.addProduction(AttValue_Char, characterHandler, asterisk);
        this.grammar.addProduction(AttValue_Char, characterHandler, plusSign);
        this.grammar.addProduction(AttValue_Char, characterHandler, comma);
        this.grammar.addProduction(AttValue_Char, characterHandler, hyphenMinus);
        this.grammar.addProduction(AttValue_Char, characterHandler, fullStop);
        this.grammar.addProduction(AttValue_Char, characterHandler, solidus);
        for (final UnicodeTerminalSymbols s : Arrays.asList(digitZero, digitOne, digitTwo, digitThree, digitFour,
                digitFive, digitSix, digitSeven, digitEight, digitNine)) {
            this.grammar.addProduction(AttValue_Char, characterHandler, s);
        }
        this.grammar.addProduction(AttValue_Char, characterHandler, colon);
        this.grammar.addProduction(AttValue_Char, characterHandler, semicolon);
        this.grammar.addProduction(AttValue_Char, characterHandler, equalsSign);
        this.grammar.addProduction(AttValue_Char, characterHandler, greaterThanSign);
        this.grammar.addProduction(AttValue_Char, characterHandler, questionMark);
        this.grammar.addProduction(AttValue_Char, characterHandler, atSymbol);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalA, capitalB, capitalC, capitalD,
                capitalE, capitalF, capitalG, capitalH, capitalI, capitalJ, capitalK, capitalL, capitalM, capitalN,
                capitalO, capitalP, capitalQ, capitalR, capitalS, capitalT, capitalU, capitalV, capitalW, capitalX,
                capitalY, capitalZ }) {
            this.grammar.addProduction(AttValue_Char, characterHandler, s);
        }
        this.grammar.addProduction(AttValue_Char, characterHandler, leftBracket);
        this.grammar.addProduction(AttValue_Char, characterHandler, reverseSolidus);
        this.grammar.addProduction(AttValue_Char, characterHandler, rightBracket);
        this.grammar.addProduction(AttValue_Char, characterHandler, circumflexAccent);
        this.grammar.addProduction(AttValue_Char, characterHandler, lowLine);
        this.grammar.addProduction(AttValue_Char, characterHandler, graveAccent);
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallA, smallB, smallC, smallD, smallE,
                smallF, smallG, smallH, smallI, smallJ, smallK, smallL, smallM, smallN, smallO, smallP, smallQ, smallR,
                smallS, smallT, smallU, smallV, smallW, smallX, smallY, smallZ }) {
            this.grammar.addProduction(AttValue_Char, characterHandler, s);
        }
        this.grammar.addProduction(AttValue_Char, characterHandler, leftCurlyBrace);
        this.grammar.addProduction(AttValue_Char, characterHandler, verticalBar);
        this.grammar.addProduction(AttValue_Char, characterHandler, rightCurlyBrace);
        this.grammar.addProduction(AttValue_Char, characterHandler, tilde);
        this.grammar.addProduction(AttValue_Char, characterHandler, delete);
        this.grammar.addProduction(AttValue_Char, characterHandler, belowSurrogates);
        this.grammar.addProduction(AttValue_Char, characterHandler, aboveSurrogates);
        this.grammar.addProduction(AttValue_Char, characterHandler, supplementaryMultilingualPlane);
        this.grammar.addProduction(AttValue_Char, characterHandler, supplementaryIdeographicPlane);
        this.grammar.addProduction(AttValue_Char, characterHandler, supplementaryUnassigned);
        this.grammar.addProduction(AttValue_Char, characterHandler, supplementarySpecialPurposePlane);
        this.grammar.addProduction(AttValue_Char, characterHandler, supplementaryPrivateUsePlanes);
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return AttValue;
    }
    
}
