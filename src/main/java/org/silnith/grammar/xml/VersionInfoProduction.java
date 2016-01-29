package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.apostrophe;
import static org.silnith.grammar.UnicodeTerminalSymbols.quotationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallE;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallI;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallN;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallO;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallR;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallS;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallV;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [24]   	VersionInfo	   ::=   	{@linkplain SProduction S} 'version' {@linkplain EqProduction Eq} ("'" {@linkplain VersionNumProduction VersionNum} "'" | '"' {@linkplain VersionNumProduction VersionNum} '"')
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-VersionInfo">VersionInfo</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class VersionInfoProduction extends XMLProduction {

	private final NonTerminalSymbol VersionInfo;

	public VersionInfoProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction, final EqProduction eqProduction,
			final VersionNumProduction versionNumProduction) {
		super(grammar);
		VersionInfo = this.grammar.getNonTerminalSymbol("VersionInfo");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Eq = eqProduction.getNonTerminalSymbol();
		final NonTerminalSymbol VersionNum = versionNumProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(VersionInfo, nullHandler, S, smallV, smallE, smallR, smallS, smallI, smallO, smallN, Eq, apostrophe, VersionNum, apostrophe);
		this.grammar.addProduction(VersionInfo, nullHandler, S, smallV, smallE, smallR, smallS, smallI, smallO, smallN, Eq, quotationMark, VersionNum, quotationMark);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return VersionInfo;
	}

}
