package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.space;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [6]   	Names	   ::=   	{@linkplain NameProduction Name} (#x20 {@linkplain NameProduction Name})*
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Names">Names</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class NamesProduction extends XMLProduction {

	private final NonTerminalSymbol Names;

	public NamesProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final NameProduction nameProduction) {
		super(grammar);
		Names = this.grammar.getNonTerminalSymbol("Names");
		
		final NonTerminalSymbol Names_inner_Plus = this.grammar.getNonTerminalSymbol("Names-inner-Plus");
		
		final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(Names, nullHandler, Name, Names_inner_Plus);
		this.grammar.addProduction(Names, nullHandler, Name);
		this.grammar.addProduction(Names_inner_Plus, nullHandler, space, Name, Names_inner_Plus);
		this.grammar.addProduction(Names_inner_Plus, nullHandler, space, Name);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return Names;
	}

}
