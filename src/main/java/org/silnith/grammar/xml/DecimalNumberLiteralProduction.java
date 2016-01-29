package org.silnith.grammar.xml;

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

import java.util.Arrays;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

public class DecimalNumberLiteralProduction extends XMLProduction {

	private final NonTerminalSymbol DECIMAL_DIGIT_Plus;

	public DecimalNumberLiteralProduction(
			final Grammar<UnicodeTerminalSymbols> grammar) {
		super(grammar);
		DECIMAL_DIGIT_Plus = this.grammar.getNonTerminalSymbol("DECIMAL-DIGIT-Plus");
		final NonTerminalSymbol DECIMAL_DIGIT = this.grammar.getNonTerminalSymbol("DECIMAL-DIGIT");
		
		this.grammar.addProduction(DECIMAL_DIGIT_Plus, stringHandler, DECIMAL_DIGIT_Plus, DECIMAL_DIGIT);
		this.grammar.addProduction(DECIMAL_DIGIT_Plus, stringHandler, DECIMAL_DIGIT);
		
		for (final UnicodeTerminalSymbols s : Arrays.asList(
				digitZero, digitOne, digitTwo, digitThree, digitFour,
				digitFive, digitSix, digitSeven, digitEight, digitNine)) {
			this.grammar.addProduction(DECIMAL_DIGIT, characterHandler, s);
		}
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return DECIMAL_DIGIT_Plus;
	}

}
