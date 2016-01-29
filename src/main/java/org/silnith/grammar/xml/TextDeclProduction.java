package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.questionMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallL;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallM;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallX;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [77]   	TextDecl	   ::=   	'&lt;?xml' {@linkplain VersionInfoProduction VersionInfo}? {@linkplain EncodingDeclProduction EncodingDecl} {@linkplain SProduction S}? '?&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-TextDecl">TextDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class TextDeclProduction extends XMLProduction {

	private final NonTerminalSymbol TextDecl;

	public TextDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final VersionInfoProduction versionInfoProduction,
			final EncodingDeclProduction encodingDeclProduction,
			final SProduction sProduction) {
		super(grammar);
		TextDecl = this.grammar.getNonTerminalSymbol("TextDecl");
		
		final NonTerminalSymbol VersionInfo = versionInfoProduction.getNonTerminalSymbol();
		final NonTerminalSymbol EncodingDecl = encodingDeclProduction.getNonTerminalSymbol();
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(TextDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, EncodingDecl, S, questionMark, greaterThanSign);
		this.grammar.addProduction(TextDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, EncodingDecl, S, questionMark, greaterThanSign);
		this.grammar.addProduction(TextDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, S, questionMark, greaterThanSign);
		this.grammar.addProduction(TextDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, S, questionMark, greaterThanSign);
		this.grammar.addProduction(TextDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, EncodingDecl, questionMark, greaterThanSign);
		this.grammar.addProduction(TextDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, EncodingDecl, questionMark, greaterThanSign);
		this.grammar.addProduction(TextDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, questionMark, greaterThanSign);
		this.grammar.addProduction(TextDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, questionMark, greaterThanSign);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return TextDecl;
	}

}
