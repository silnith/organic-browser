package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.digitOne;
import static org.silnith.grammar.UnicodeTerminalSymbols.fullStop;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [26]   	VersionNum	   ::=   	'1.' [0-9]+
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-VersionNum">VersionNum</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class VersionNumProduction extends XMLProduction {

	private final NonTerminalSymbol VersionNum;

	public VersionNumProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final DecimalNumberLiteralProduction decimalNumberLiteralProduction) {
		super(grammar);
		VersionNum = this.grammar.getNonTerminalSymbol("VersionNum");
		
		final NonTerminalSymbol DECIMAL_DIGIT_Plus = decimalNumberLiteralProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(VersionNum, stringHandler, digitOne, fullStop, DECIMAL_DIGIT_Plus);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return VersionNum;
	}

}
