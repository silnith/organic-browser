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
import static org.silnith.grammar.UnicodeTerminalSymbols.fullStop;
import static org.silnith.grammar.UnicodeTerminalSymbols.hyphenMinus;
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

import java.util.Arrays;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [81]   	EncName	   ::=   	[A-Za-z] ([A-Za-z0-9._] | '-')*
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EncName">EncName</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EncNameProduction extends XMLProduction {

	private final NonTerminalSymbol EncName;

	public EncNameProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
		super(grammar);
		EncName = this.grammar.getNonTerminalSymbol("EncName");
		
		final NonTerminalSymbol EncName_StartChar = this.grammar.getNonTerminalSymbol("EncName-StartChar");
		final NonTerminalSymbol EncName_Char = this.grammar.getNonTerminalSymbol("EncName-Char");
		final NonTerminalSymbol EncName_Char_Plus = this.grammar.getNonTerminalSymbol("EncName-Char-Plus");
		
		this.grammar.addProduction(EncName, stringHandler, EncName_StartChar, EncName_Char_Plus);
		this.grammar.addProduction(EncName, stringHandler, EncName_StartChar);
		
		this.grammar.addProduction(EncName_Char_Plus, stringHandler, EncName_Char_Plus, EncName_Char);
		this.grammar.addProduction(EncName_Char_Plus, stringHandler, EncName_Char);
		
		this.grammar.addProduction(EncName_Char, characterHandler, hyphenMinus);
		this.grammar.addProduction(EncName_Char, characterHandler, fullStop);
		for (final UnicodeTerminalSymbols s : Arrays.asList(
				digitZero, digitOne, digitTwo, digitThree, digitFour,
				digitFive, digitSix, digitSeven, digitEight, digitNine)) {
			this.grammar.addProduction(EncName_Char, characterHandler, s);
		}
		for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] {
				capitalA, capitalB, capitalC, capitalD, capitalE, capitalF,
				capitalG, capitalH, capitalI, capitalJ, capitalK,
				capitalL, capitalM, capitalN, capitalO, capitalP,
				capitalQ, capitalR, capitalS, capitalT, capitalU,
				capitalV, capitalW, capitalX, capitalY, capitalZ}) {
			this.grammar.addProduction(EncName_StartChar, characterHandler, s);
			this.grammar.addProduction(EncName_Char, characterHandler, s);
		}
		this.grammar.addProduction(EncName_Char, characterHandler, lowLine);
		for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] {
				smallA, smallB, smallC, smallD, smallE, smallF,
				smallG, smallH, smallI, smallJ, smallK,
				smallL, smallM, smallN, smallO, smallP,
				smallQ, smallR, smallS, smallT, smallU,
				smallV, smallW, smallX, smallY, smallZ}) {
			this.grammar.addProduction(EncName_StartChar, characterHandler, s);
			this.grammar.addProduction(EncName_Char, characterHandler, s);
		}
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return EncName;
	}

}
