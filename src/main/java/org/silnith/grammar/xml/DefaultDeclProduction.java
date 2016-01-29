package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalD;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalF;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalL;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalM;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalP;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalQ;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalR;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalU;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalX;
import static org.silnith.grammar.UnicodeTerminalSymbols.numberSign;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [60]   	DefaultDecl	   ::=   	'#REQUIRED' | '#IMPLIED'
 * 			| (('#FIXED' {@linkplain SProduction S})? {@linkplain AttValueProduction AttValue})
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-DefaultDecl">DefaultDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DefaultDeclProduction extends XMLProduction {

	private final NonTerminalSymbol DefaultDecl;

	public DefaultDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction, final AttValueProduction attValueProduction) {
		super(grammar);
		DefaultDecl = this.grammar.getNonTerminalSymbol("DefaultDecl");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol AttValue = attValueProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(DefaultDecl, nullHandler, numberSign, capitalR, capitalE, capitalQ, capitalU, capitalI, capitalR, capitalE, capitalD);
		this.grammar.addProduction(DefaultDecl, nullHandler, numberSign, capitalI, capitalM, capitalP, capitalL, capitalI, capitalE, capitalD);
		this.grammar.addProduction(DefaultDecl, nullHandler, numberSign, capitalF, capitalI, capitalX, capitalE, capitalD, S, AttValue);
		this.grammar.addProduction(DefaultDecl, nullHandler, AttValue);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return DefaultDecl;
	}

}
