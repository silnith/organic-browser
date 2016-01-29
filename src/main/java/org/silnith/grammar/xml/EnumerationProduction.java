package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.leftParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.rightParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.verticalBar;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [59]   	Enumeration	   ::=   	'(' {@linkplain SProduction S}? {@linkplain NmtokenProduction Nmtoken} ({@linkplain SProduction S}? '|' {@linkplain SProduction S}? {@linkplain NmtokenProduction Nmtoken})* {@linkplain SProduction S}? ')'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Enumeration">Enumeration</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class EnumerationProduction extends XMLProduction {

	private final NonTerminalSymbol Enumeration;

	public EnumerationProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction, final NmtokenProduction nmtokenProduction) {
		super(grammar);
		Enumeration = this.grammar.getNonTerminalSymbol("Enumeration");
		
		final NonTerminalSymbol Enumeration_inner = this.grammar.getNonTerminalSymbol("Enumeration_inner");
		final NonTerminalSymbol Enumeration_inner_Plus = this.grammar.getNonTerminalSymbol("Enumeration_inner_Plus");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Nmtoken = nmtokenProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(Enumeration, nullHandler, leftParenthesis, S, Nmtoken, S, Enumeration_inner_Plus, rightParenthesis);
		this.grammar.addProduction(Enumeration, nullHandler, leftParenthesis, Nmtoken, S, Enumeration_inner_Plus, rightParenthesis);
		this.grammar.addProduction(Enumeration, nullHandler, leftParenthesis, S, Nmtoken, Enumeration_inner_Plus, rightParenthesis);
		this.grammar.addProduction(Enumeration, nullHandler, leftParenthesis, Nmtoken, Enumeration_inner_Plus, rightParenthesis);
		this.grammar.addProduction(Enumeration, nullHandler, leftParenthesis, S, Nmtoken, S, rightParenthesis);
		this.grammar.addProduction(Enumeration, nullHandler, leftParenthesis, Nmtoken, S, rightParenthesis);
		this.grammar.addProduction(Enumeration, nullHandler, leftParenthesis, S, Nmtoken, rightParenthesis);
		this.grammar.addProduction(Enumeration, nullHandler, leftParenthesis, Nmtoken, rightParenthesis);
		
		this.grammar.addProduction(Enumeration_inner_Plus, nullHandler, Enumeration_inner, Enumeration_inner_Plus);
		this.grammar.addProduction(Enumeration_inner_Plus, nullHandler, Enumeration_inner);
		
		this.grammar.addProduction(Enumeration_inner, nullHandler, verticalBar, S, Nmtoken, S);
		this.grammar.addProduction(Enumeration_inner, nullHandler, verticalBar, Nmtoken, S);
		this.grammar.addProduction(Enumeration_inner, nullHandler, verticalBar, S, Nmtoken);
		this.grammar.addProduction(Enumeration_inner, nullHandler, verticalBar, Nmtoken);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return Enumeration;
	}

}
