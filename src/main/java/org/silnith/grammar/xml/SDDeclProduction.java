package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.apostrophe;
import static org.silnith.grammar.UnicodeTerminalSymbols.quotationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallA;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallD;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallE;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallL;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallN;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallO;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallS;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallT;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallY;

import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [32]   	SDDecl	   ::=   	{@linkplain SProduction S} 'standalone' {@linkplain EqProduction Eq} (("'" ('yes' | 'no') "'") | ('"' ('yes' | 'no') '"'))
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-SDDecl">SDDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class SDDeclProduction extends XMLProduction {

	private static class BooleanProductionHandler implements ProductionHandler {

		private final boolean bool;

		public BooleanProductionHandler(final boolean bool) {
			super();
			this.bool = bool;
		}

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			return bool;
		}

	}

	private final NonTerminalSymbol SDDecl;

	public SDDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction, final EqProduction eqProduction) {
		super(grammar);
		SDDecl = this.grammar.getNonTerminalSymbol("SDDecl");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Eq = eqProduction.getNonTerminalSymbol();
		
		final ProductionHandler yesHandler = new BooleanProductionHandler(true);
		final ProductionHandler noHandler = new BooleanProductionHandler(false);
		this.grammar.addProduction(SDDecl, yesHandler, S, smallS, smallT, smallA, smallN, smallD, smallA, smallL, smallO, smallN, smallE, Eq, apostrophe, smallY, smallE, smallS, apostrophe);
		this.grammar.addProduction(SDDecl, noHandler, S, smallS, smallT, smallA, smallN, smallD, smallA, smallL, smallO, smallN, smallE, Eq, apostrophe, smallN, smallO, apostrophe);
		this.grammar.addProduction(SDDecl, yesHandler, S, smallS, smallT, smallA, smallN, smallD, smallA, smallL, smallO, smallN, smallE, Eq, quotationMark, smallY, smallE, smallS, quotationMark);
		this.grammar.addProduction(SDDecl, noHandler, S, smallS, smallT, smallA, smallN, smallD, smallA, smallL, smallO, smallN, smallE, Eq, quotationMark, smallN, smallO, quotationMark);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return SDDecl;
	}

}
