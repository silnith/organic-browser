package org.silnith.grammar.xml;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.Symbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [31]   	extSubsetDecl	   ::=   	( {@linkplain MarkupDeclProduction markupdecl} | {@linkplain ConditionalSectProduction conditionalSect} | {@linkplain DeclSepProduction DeclSep})*
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-extSubsetDecl">extSubsetDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ExtSubsetDeclProduction extends XMLProduction {

	private final NonTerminalSymbol extSubsetDecl;

	public ExtSubsetDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final MarkupDeclProduction markupDeclProduction,
			final ConditionalSectProduction conditionalSectProduction,
			final DeclSepProduction declSepProduction) {
		super(grammar);
		extSubsetDecl = this.grammar.getNonTerminalSymbol("extSubsetDecl");
		
		final NonTerminalSymbol extSubsetDecl_inner = this.grammar.getNonTerminalSymbol("extSubsetDecl-inner");
		final NonTerminalSymbol extSubsetDecl_inner_Plus = this.grammar.getNonTerminalSymbol("extSubsetDecl-inner-Plus");
		
		final NonTerminalSymbol markupdecl = markupDeclProduction.getNonTerminalSymbol();
		final NonTerminalSymbol conditionalSect = conditionalSectProduction.getNonTerminalSymbol();
		final NonTerminalSymbol DeclSep = declSepProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(extSubsetDecl, nullHandler, extSubsetDecl_inner_Plus);
		this.grammar.addProduction(extSubsetDecl, nullHandler, new Symbol[0]);
		
		this.grammar.addProduction(extSubsetDecl_inner_Plus, nullHandler, extSubsetDecl_inner, extSubsetDecl_inner_Plus);
		this.grammar.addProduction(extSubsetDecl_inner_Plus, nullHandler, extSubsetDecl_inner);
		
		this.grammar.addProduction(extSubsetDecl_inner, nullHandler, markupdecl);
		this.grammar.addProduction(extSubsetDecl_inner, nullHandler, conditionalSect);
		this.grammar.addProduction(extSubsetDecl_inner, nullHandler, DeclSep);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return extSubsetDecl;
	}

}
