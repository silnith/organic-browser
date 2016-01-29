package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalB;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalC;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalL;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalP;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalU;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [83]   	PublicID	   ::=   	'PUBLIC' {@linkplain SProduction S} {@linkplain PubidLiteralProduction PubidLiteral} 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PublicID">PublicID</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PublicIDProduction extends XMLProduction {

	private final NonTerminalSymbol PublicID;

	public PublicIDProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction,
			final PubidLiteralProduction pubidLiteralProduction) {
		super(grammar);
		PublicID = this.grammar.getNonTerminalSymbol("PublicID");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol PubidLiteral = pubidLiteralProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(PublicID, nullHandler, capitalP, capitalU, capitalB, capitalL, capitalI, capitalC, S, PubidLiteral);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return PublicID;
	}

}
