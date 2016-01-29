package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [70]   	EntityDecl	   ::=   	{@linkplain GEDeclProduction GEDecl} | {@linkplain PEDeclProduction PEDecl} 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EntityDecl">EntityDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EntityDeclProduction extends XMLProduction {

	private final NonTerminalSymbol EntityDecl;

	public EntityDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final GEDeclProduction geDeclProduction,
			final PEDeclProduction peDeclProduction) {
		super(grammar);
		EntityDecl = this.grammar.getNonTerminalSymbol("EntityDecl");
		
		final NonTerminalSymbol GEDecl = geDeclProduction.getNonTerminalSymbol();
		final NonTerminalSymbol PEDecl = peDeclProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(EntityDecl, nullHandler, GEDecl);
		this.grammar.addProduction(EntityDecl, nullHandler, PEDecl);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return EntityDecl;
	}

}
