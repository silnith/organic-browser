package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalA;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalD;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [76]   	NDataDecl	   ::=   	{@linkplain SProduction S} 'NDATA' {@linkplain SProduction S} {@linkplain NameProduction Name} 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-NDataDecl">NDataDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class NDataDeclProduction extends XMLProduction {

	private final NonTerminalSymbol NDataDecl;

	public NDataDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction, final NameProduction nameProduction) {
		super(grammar);
		NDataDecl = this.grammar.getNonTerminalSymbol("NDataDecl");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(NDataDecl, nullHandler, capitalN, capitalD, capitalA, capitalT, capitalA, S, Name);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return NDataDecl;
	}

}
