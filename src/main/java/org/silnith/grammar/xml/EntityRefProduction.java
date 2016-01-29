package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.ampersand;
import static org.silnith.grammar.UnicodeTerminalSymbols.semicolon;

import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.EntityRef;

/**
 * [68]   	EntityRef	   ::=   	'&amp;' {@linkplain NameProduction Name} ';'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EntityRef">EntityRef</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EntityRefProduction extends XMLProduction {

	private static class EntityRefHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final EntityRef entityRef = new EntityRef();
			entityRef.name = (String) rightHandSide.get(1);
			return entityRef;
		}

	}

	private final NonTerminalSymbol EntityRef;

	public EntityRefProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final NameProduction nameProduction) {
		super(grammar);
		EntityRef = this.grammar.getNonTerminalSymbol("EntityRef");
		
		final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(EntityRef, new EntityRefHandler(), ampersand, Name, semicolon);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return EntityRef;
	}

}
