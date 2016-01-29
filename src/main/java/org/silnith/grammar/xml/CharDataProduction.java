package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;
import static org.silnith.grammar.UnicodeTerminalSymbols.aboveSurrogates;
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
import static org.silnith.grammar.UnicodeTerminalSymbols.hyphenMinus;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftBracket;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftCurlyBrace;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.lineFeed;
import static org.silnith.grammar.UnicodeTerminalSymbols.lowLine;
import static org.silnith.grammar.UnicodeTerminalSymbols.numberSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.percentSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.plusSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.questionMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.quotationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.reverseSolidus;
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
import org.silnith.grammar.Symbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [14]   	CharData	   ::=   	[^&lt;&amp;]* - ([^&lt;&amp;]* ']]&gt;' [^&lt;&amp;]*)
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CharData">CharData</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CharDataProduction extends XMLProduction {

	private final NonTerminalSymbol CharData;

	private final NonTerminalSymbol CharData_Plus;

	public CharDataProduction(Grammar<UnicodeTerminalSymbols> grammar) {
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
		for (final UnicodeTerminalSymbols s : Arrays.asList(
				digitZero, digitOne, digitTwo, digitThree, digitFour,
				digitFive, digitSix, digitSeven, digitEight, digitNine)) {
			this.grammar.addProduction(CharData_Single, characterHandler, s);
		}
		this.grammar.addProduction(CharData_Single, characterHandler, colon);
		this.grammar.addProduction(CharData_Single, characterHandler, semicolon);
		this.grammar.addProduction(CharData_Single, characterHandler, equalsSign);
		this.grammar.addProduction(CharData_Single, characterHandler, questionMark);
		this.grammar.addProduction(CharData_Single, characterHandler, atSymbol);
		for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] {
				capitalA, capitalB, capitalC, capitalD, capitalE, capitalF,
				capitalG, capitalH, capitalI, capitalJ, capitalK,
				capitalL, capitalM, capitalN, capitalO, capitalP,
				capitalQ, capitalR, capitalS, capitalT, capitalU,
				capitalV, capitalW, capitalX, capitalY, capitalZ}) {
			this.grammar.addProduction(CharData_Single, characterHandler, s);
		}
		this.grammar.addProduction(CharData_Single, characterHandler, leftBracket);
		this.grammar.addProduction(CharData_Single, characterHandler, reverseSolidus);
		this.grammar.addProduction(CharData_Single, characterHandler, circumflexAccent);
		this.grammar.addProduction(CharData_Single, characterHandler, lowLine);
		this.grammar.addProduction(CharData_Single, characterHandler, graveAccent);
		for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] {
				smallA, smallB, smallC, smallD, smallE, smallF,
				smallG, smallH, smallI, smallJ, smallK,
				smallL, smallM, smallN, smallO, smallP,
				smallQ, smallR, smallS, smallT, smallU,
				smallV, smallW, smallX, smallY, smallZ}) {
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
