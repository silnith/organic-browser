package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalM;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalS;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalY;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [75]   	ExternalID	   ::=   	'SYSTEM' {@linkplain SProduction S} {@linkplain SystemLiteralProduction SystemLiteral}
 * 			| 'PUBLIC' {@linkplain SProduction S} {@linkplain PubidLiteralProduction PubidLiteral} {@linkplain SProduction S} {@linkplain SystemLiteralProduction SystemLiteral} 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-ExternalID">ExternalID</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ExternalIDProduction extends XMLProduction {

	private final NonTerminalSymbol ExternalID;

	public ExternalIDProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final PublicIDProduction publicIDProduction,
			final SProduction sProduction,
			final SystemLiteralProduction systemLiteralProduction) {
		super(grammar);
		ExternalID = this.grammar.getNonTerminalSymbol("ExternalID");
		
		final NonTerminalSymbol PublicID = publicIDProduction.getNonTerminalSymbol();
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol SystemLiteral = systemLiteralProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(ExternalID, nullHandler, PublicID, S, SystemLiteral);
		this.grammar.addProduction(ExternalID, nullHandler, capitalS, capitalY, capitalS, capitalT, capitalE, capitalM, S, SystemLiteral);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return ExternalID;
	}

}
