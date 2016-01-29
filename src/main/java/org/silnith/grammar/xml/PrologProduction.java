package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [22]   	prolog	   ::=   	{@linkplain XMLDeclProduction XMLDecl}? {@linkplain MiscProduction Misc}* ({@linkplain DoctypedeclProduction doctypedecl} {@linkplain MiscProduction Misc}*)?
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-prolog">prolog</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PrologProduction extends XMLProduction {

	public PrologProduction(Grammar<UnicodeTerminalSymbols> grammar) {
		super(grammar);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		// TODO Auto-generated method stub
		return null;
	}

}
