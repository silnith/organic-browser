package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [74]   	PEDef	   ::=   	{@linkplain EntityValueProduction EntityValue} | {@linkplain ExternalIDProduction ExternalID} 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PEDef">PEDef</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PEDefProduction extends XMLProduction {

	private final NonTerminalSymbol PEDef;

	public PEDefProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final EntityValueProduction entityValueProduction,
			final ExternalIDProduction externalIDProduction) {
		super(grammar);
		PEDef = this.grammar.getNonTerminalSymbol("PEDef");
		
		final NonTerminalSymbol EntityValue = entityValueProduction.getNonTerminalSymbol();
		final NonTerminalSymbol ExternalID = externalIDProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(PEDef, nullHandler, EntityValue);
		this.grammar.addProduction(PEDef, nullHandler, ExternalID);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return PEDef;
	}

}
