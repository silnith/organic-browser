package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [86]   	Ideographic	   ::=   	[#x4E00-#x9FA5] | #x3007 | [#x3021-#x3029] 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Ideographic">Ideographic</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class IdeographicProduction extends XMLProduction {

	public IdeographicProduction(Grammar<UnicodeTerminalSymbols> grammar) {
		super(grammar);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		// TODO Auto-generated method stub
		return null;
	}

}
