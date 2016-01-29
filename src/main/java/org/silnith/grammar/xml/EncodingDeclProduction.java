package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.apostrophe;
import static org.silnith.grammar.UnicodeTerminalSymbols.quotationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallC;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallD;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallE;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallG;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallI;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallN;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallO;

import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [80]   	EncodingDecl	   ::=   	{@linkplain SProduction S} 'encoding' {@linkplain EqProduction Eq} ('"' {@linkplain EncNameProduction EncName} '"' | "'" {@linkplain EncNameProduction EncName} "'" ) 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EncodingDecl">EncodingDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EncodingDeclProduction extends XMLProduction {

	private static class TwelfthElementHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			return rightHandSide.get(11);
		}

	}

	private final NonTerminalSymbol EncodingDecl;

	public EncodingDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction, final EqProduction eqProduction,
			final EncNameProduction encNameProduction) {
		super(grammar);
		EncodingDecl = this.grammar.getNonTerminalSymbol("EncodingDecl");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Eq = eqProduction.getNonTerminalSymbol();
		final NonTerminalSymbol EncName = encNameProduction.getNonTerminalSymbol();
		
		final TwelfthElementHandler twelfthElementHandler = new TwelfthElementHandler();
		this.grammar.addProduction(EncodingDecl, twelfthElementHandler, S, smallE, smallN, smallC, smallO, smallD, smallI, smallN, smallG, Eq, quotationMark, EncName, quotationMark);
		this.grammar.addProduction(EncodingDecl, twelfthElementHandler, S, smallE, smallN, smallC, smallO, smallD, smallI, smallN, smallG, Eq, apostrophe, EncName, apostrophe);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return EncodingDecl;
	}

}
