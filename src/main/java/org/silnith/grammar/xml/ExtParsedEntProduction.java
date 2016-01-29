package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [78]   	extParsedEnt	   ::=   	{@linkplain TextDeclProduction TextDecl}? {@linkplain ElementProduction content} 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-extParsedEnt">extParsedEnt</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ExtParsedEntProduction extends XMLProduction {

	private final NonTerminalSymbol extParsedEnt;

	public ExtParsedEntProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final TextDeclProduction textDeclProduction,
			final ElementProduction elementProduction) {
		super(grammar);
		extParsedEnt = this.grammar.getNonTerminalSymbol("extParsedEnt");
		
		final NonTerminalSymbol TextDecl = textDeclProduction.getNonTerminalSymbol();
		final NonTerminalSymbol content = elementProduction.getContentNonTerminalSymbol();
		
		this.grammar.addProduction(extParsedEnt, nullHandler, TextDecl, content);
		this.grammar.addProduction(extParsedEnt, nullHandler, content);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return extParsedEnt;
	}

}
