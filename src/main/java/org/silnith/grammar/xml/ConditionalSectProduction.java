package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [61]   	conditionalSect	   ::=   	{@linkplain IncludeSectProduction includeSect} | {@linkplain IgnoreSectProduction ignoreSect} 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-conditionalSect">conditionalSect</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ConditionalSectProduction extends XMLProduction {

	private final NonTerminalSymbol conditionalSect;

	public ConditionalSectProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final IncludeSectProduction includeSectProduction,
			final IgnoreSectProduction ignoreSectProduction) {
		super(grammar);
		conditionalSect = this.grammar.getNonTerminalSymbol("conditionalSect");
		
		final NonTerminalSymbol includeSect = includeSectProduction.getNonTerminalSymbol();
		final NonTerminalSymbol ignoreSect = ignoreSectProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(conditionalSect, nullHandler, includeSect);
		this.grammar.addProduction(conditionalSect, nullHandler, ignoreSect);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return conditionalSect;
	}

}
