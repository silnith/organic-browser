package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;
import static org.silnith.grammar.UnicodeTerminalSymbols.apostrophe;
import static org.silnith.grammar.UnicodeTerminalSymbols.asterisk;
import static org.silnith.grammar.UnicodeTerminalSymbols.atSymbol;
import static org.silnith.grammar.UnicodeTerminalSymbols.belowSurrogates;
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
import static org.silnith.grammar.UnicodeTerminalSymbols.circumflexAccent;
import static org.silnith.grammar.UnicodeTerminalSymbols.colon;
import static org.silnith.grammar.UnicodeTerminalSymbols.comma;
import static org.silnith.grammar.UnicodeTerminalSymbols.delete;
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
import static org.silnith.grammar.UnicodeTerminalSymbols.graveAccent;
import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.hyphenMinus;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftBracket;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftCurlyBrace;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.lineFeed;
import static org.silnith.grammar.UnicodeTerminalSymbols.lowLine;
import static org.silnith.grammar.UnicodeTerminalSymbols.numberSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.plusSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.questionMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.quotationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.reverseSolidus;
import static org.silnith.grammar.UnicodeTerminalSymbols.rightBracket;
import static org.silnith.grammar.UnicodeTerminalSymbols.rightCurlyBrace;
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
import static org.silnith.grammar.UnicodeTerminalSymbols.tab;
import static org.silnith.grammar.UnicodeTerminalSymbols.tilde;
import static org.silnith.grammar.UnicodeTerminalSymbols.verticalBar;

import java.util.Arrays;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [9]   	EntityValue	   ::=   	'"' ([^%&amp;"] | {@linkplain PEReferenceProduction PEReference} | {@linkplain ReferenceProduction Reference})* '"'
 * 			|  "'" ([^%&amp;'] | {@linkplain PEReferenceProduction PEReference} | {@linkplain ReferenceProduction Reference})* "'"
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EntityValue">EntityValue</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EntityValueProduction extends XMLProduction {

	private final NonTerminalSymbol EntityValue;

	public EntityValueProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final PEReferenceProduction peReferenceProduction,
			final ReferenceProduction referenceProduction) {
		super(grammar);
		EntityValue = this.grammar.getNonTerminalSymbol("EntityValue");
		
		final NonTerminalSymbol EntityValue_Char = this.grammar.getNonTerminalSymbol("EntityValue-Char");
		final NonTerminalSymbol EntityValue_inner_quot = this.grammar.getNonTerminalSymbol("EntityValue-inner-quot");
		final NonTerminalSymbol EntityValue_inner_apos = this.grammar.getNonTerminalSymbol("EntityValue-inner-apos");
		final NonTerminalSymbol EntityValue_inner_quot_Plus = this.grammar.getNonTerminalSymbol("EntityValue-inner-quot-Plus");
		final NonTerminalSymbol EntityValue_inner_apos_Plus = this.grammar.getNonTerminalSymbol("EntityValue-inner-apos-Plus");

		final NonTerminalSymbol PEReference = peReferenceProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Reference = referenceProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(EntityValue, nullHandler, quotationMark, EntityValue_inner_quot_Plus, quotationMark);
		this.grammar.addProduction(EntityValue, nullHandler, apostrophe, EntityValue_inner_apos_Plus, apostrophe);
		
		this.grammar.addProduction(EntityValue_inner_quot_Plus, nullHandler, EntityValue_inner_quot, EntityValue_inner_quot_Plus);
		this.grammar.addProduction(EntityValue_inner_quot_Plus, nullHandler, EntityValue_inner_quot);

		this.grammar.addProduction(EntityValue_inner_apos_Plus, nullHandler, EntityValue_inner_apos, EntityValue_inner_apos_Plus);
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
		for (final UnicodeTerminalSymbols s : Arrays.asList(
				digitZero, digitOne, digitTwo, digitThree, digitFour,
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
		for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] {
				capitalA, capitalB, capitalC, capitalD, capitalE, capitalF,
				capitalG, capitalH, capitalI, capitalJ, capitalK,
				capitalL, capitalM, capitalN, capitalO, capitalP,
				capitalQ, capitalR, capitalS, capitalT, capitalU,
				capitalV, capitalW, capitalX, capitalY, capitalZ}) {
			this.grammar.addProduction(EntityValue_Char, characterHandler, s);
		}
		this.grammar.addProduction(EntityValue_Char, characterHandler, leftBracket);
		this.grammar.addProduction(EntityValue_Char, characterHandler, reverseSolidus);
		this.grammar.addProduction(EntityValue_Char, characterHandler, rightBracket);
		this.grammar.addProduction(EntityValue_Char, characterHandler, circumflexAccent);
		this.grammar.addProduction(EntityValue_Char, characterHandler, lowLine);
		this.grammar.addProduction(EntityValue_Char, characterHandler, graveAccent);
		for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] {
				smallA, smallB, smallC, smallD, smallE, smallF,
				smallG, smallH, smallI, smallJ, smallK,
				smallL, smallM, smallN, smallO, smallP,
				smallQ, smallR, smallS, smallT, smallU,
				smallV, smallW, smallX, smallY, smallZ}) {
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
