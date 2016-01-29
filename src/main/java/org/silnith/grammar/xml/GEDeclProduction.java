package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalY;
import static org.silnith.grammar.UnicodeTerminalSymbols.exclamationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [71]   	GEDecl	   ::=   	'&lt;!ENTITY' {@linkplain SProduction S} {@linkplain NameProduction Name} {@linkplain SProduction S} {@linkplain EntityDefProduction EntityDef} {@linkplain SProduction S}? '&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-GEDecl">GEDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class GEDeclProduction extends XMLProduction {

	private final NonTerminalSymbol GEDecl;

	public GEDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction, final NameProduction nameProduction,
			final EntityDefProduction entityDefProduction) {
		super(grammar);
		GEDecl = this.grammar.getNonTerminalSymbol("GEDecl");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
		final NonTerminalSymbol EntityDef = entityDefProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(GEDecl, nullHandler, lessThanSign, exclamationMark, capitalE, capitalN, capitalT, capitalI, capitalT, capitalY, S, Name, S, EntityDef, greaterThanSign);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return GEDecl;
	}

}
