package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalA;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalC;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalD;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [55]   	StringType	   ::=   	'CDATA'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-StringType">StringType</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class StringTypeProduction extends XMLProduction {

	private final NonTerminalSymbol StringType;

	public StringTypeProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
		super(grammar);
		StringType = this.grammar.getNonTerminalSymbol("StringType");
		
		this.grammar.addProduction(StringType, nullHandler, capitalC, capitalD, capitalA, capitalT, capitalA);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return StringType;
	}

}
