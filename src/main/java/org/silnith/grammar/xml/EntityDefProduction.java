package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [73]   	EntityDef	   ::=   	{@linkplain EntityValueProduction EntityValue} | ({@linkplain ExternalIDProduction ExternalID} {@linkplain NDataDeclProduction NDataDecl}?)
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EntityDef">EntityDef</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EntityDefProduction extends XMLProduction {

	private final NonTerminalSymbol EntityDef;

	public EntityDefProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final EntityValueProduction entityValueProduction,
			final ExternalIDProduction externalIDProduction,
			final SProduction sProduction, final NDataDeclProduction nDataDeclProduction) {
		super(grammar);
		EntityDef = this.grammar.getNonTerminalSymbol("EntityDef");
		
		final NonTerminalSymbol EntityValue = entityValueProduction.getNonTerminalSymbol();
		final NonTerminalSymbol ExternalID = externalIDProduction.getNonTerminalSymbol();
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol NDataDecl = nDataDeclProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(EntityDef, nullHandler, EntityValue, S);
		this.grammar.addProduction(EntityDef, nullHandler, EntityValue);
		this.grammar.addProduction(EntityDef, nullHandler, ExternalID, S, NDataDecl, S);
		this.grammar.addProduction(EntityDef, nullHandler, ExternalID, S, NDataDecl);
		this.grammar.addProduction(EntityDef, nullHandler, ExternalID, S);
		this.grammar.addProduction(EntityDef, nullHandler, ExternalID);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return EntityDef;
	}

}
