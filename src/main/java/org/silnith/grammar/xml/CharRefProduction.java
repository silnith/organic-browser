package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.ampersand;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalA;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalB;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalC;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalD;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalF;
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
import static org.silnith.grammar.UnicodeTerminalSymbols.numberSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.semicolon;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallA;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallB;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallC;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallD;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallE;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallF;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallX;

import java.util.Arrays;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.CharRef;

/**
 * [66]   	CharRef	   ::=   	'&amp;#' [0-9]+ ';'
 * 			| '&amp;#x' [0-9a-fA-F]+ ';'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CharRef">CharRef</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CharRefProduction extends XMLProduction {

	private static class DecimalReferenceHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final CharRef charRef = new CharRef();
			final String decimal = rightHandSide.get(2).toString();
			charRef.codePoint = Integer.parseInt(decimal);
			return charRef;
		}

	}

	private static class HexadecimalReferenceHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final CharRef charRef = new CharRef();
			final String decimal = rightHandSide.get(3).toString();
			charRef.codePoint = Integer.parseInt(decimal, 16);
			return charRef;
		}

	}

	private final NonTerminalSymbol CharRef;

	public CharRefProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final DecimalNumberLiteralProduction decimalNumberLiteralProduction) {
		super(grammar);
		CharRef = this.grammar.getNonTerminalSymbol("CharRef");
		
		final NonTerminalSymbol DECIMAL_DIGIT_Plus = decimalNumberLiteralProduction.getNonTerminalSymbol();
		final NonTerminalSymbol HEX_DIGIT = this.grammar.getNonTerminalSymbol("HEX-DIGIT");
		final NonTerminalSymbol HEX_DIGIT_Plus = this.grammar.getNonTerminalSymbol("HEX-DIGIT-Plus");
		
		this.grammar.addProduction(CharRef, new DecimalReferenceHandler(), ampersand, numberSign, DECIMAL_DIGIT_Plus, semicolon);
		this.grammar.addProduction(CharRef, new HexadecimalReferenceHandler(), ampersand, numberSign, smallX, HEX_DIGIT_Plus, semicolon);
		this.grammar.addProduction(HEX_DIGIT_Plus, stringHandler, HEX_DIGIT, HEX_DIGIT_Plus);
		this.grammar.addProduction(HEX_DIGIT_Plus, stringHandler, HEX_DIGIT);
		
		for (final UnicodeTerminalSymbols s : Arrays.asList(
				digitZero, digitOne, digitTwo, digitThree, digitFour,
				digitFive, digitSix, digitSeven, digitEight, digitNine,
				capitalA, capitalB, capitalC, capitalD, capitalE, capitalF,
				smallA, smallB, smallC, smallD, smallE, smallF)) {
			this.grammar.addProduction(HEX_DIGIT, characterHandler, s);
		}
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return CharRef;
	}

}
