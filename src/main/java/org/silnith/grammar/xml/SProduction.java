package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.carriageReturn;
import static org.silnith.grammar.UnicodeTerminalSymbols.lineFeed;
import static org.silnith.grammar.UnicodeTerminalSymbols.space;
import static org.silnith.grammar.UnicodeTerminalSymbols.tab;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [3]   	S	   ::=   	(#x20 | #x9 | #xD | #xA)+
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-S">S</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class SProduction extends XMLProduction {

	private final NonTerminalSymbol S;

	public SProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
		super(grammar);
		S = this.grammar.getNonTerminalSymbol("S");
		
		final NonTerminalSymbol WHITESPACE = this.grammar.getNonTerminalSymbol("WHITESPACE");
		
		this.grammar.addProduction(WHITESPACE, characterHandler, tab);
		this.grammar.addProduction(WHITESPACE, characterHandler, lineFeed);
		this.grammar.addProduction(WHITESPACE, characterHandler, carriageReturn);
		this.grammar.addProduction(WHITESPACE, characterHandler, space);
		
		this.grammar.addProduction(S, stringHandler, WHITESPACE);
		this.grammar.addProduction(S, stringHandler, S, WHITESPACE);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return S;
	}

}
