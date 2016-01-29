package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.space;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [8]   	Nmtokens	   ::=   	{@linkplain NmtokenProduction Nmtoken} (#x20 {@linkplain NmtokenProduction Nmtoken})*
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Nmtokens">Nmtokens</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class NmtokensProduction extends XMLProduction {

	private final NonTerminalSymbol Nmtokens;

	public NmtokensProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final NmtokenProduction nmtokenProduction) {
		super(grammar);
		Nmtokens = this.grammar.getNonTerminalSymbol("Nmtokens");
		
		final NonTerminalSymbol Nmtokens_inner_Kleene = this.grammar.getNonTerminalSymbol("Nmtokens-inner-Kleene");
		
		final NonTerminalSymbol Nmtoken = nmtokenProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(Nmtokens, nullHandler, Nmtoken, Nmtokens_inner_Kleene);
		this.grammar.addProduction(Nmtokens_inner_Kleene, nullHandler, space, Nmtoken, Nmtokens_inner_Kleene);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return Nmtokens;
	}

}
