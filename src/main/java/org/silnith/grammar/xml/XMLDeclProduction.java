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
 * [23]   	XMLDecl	   ::=   	'&lt;?xml' {@linkplain VersionInfoProduction VersionInfo} {@linkplain EncodingDeclProduction EncodingDecl}? {@linkplain SDDeclProduction SDDecl}? {@linkplain SProduction S}? '?&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-XMLDecl">XMLDecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class XMLDeclProduction extends XMLProduction {

	private final NonTerminalSymbol XMLDecl;

	public XMLDeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final VersionInfoProduction versionInfoProduction,
			final EncodingDeclProduction encodingDeclProduction,
			final SDDeclProduction sdDeclProduction,
			final SProduction sProduction) {
		super(grammar);
		XMLDecl = this.grammar.getNonTerminalSymbol("XMLDecl");
		
		final NonTerminalSymbol VersionInfo = versionInfoProduction.getNonTerminalSymbol();
		final NonTerminalSymbol EncodingDecl = encodingDeclProduction.getNonTerminalSymbol();
		final NonTerminalSymbol SDDecl = sdDeclProduction.getNonTerminalSymbol();
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(XMLDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, EncodingDecl, SDDecl, S, questionMark, greaterThanSign);
		this.grammar.addProduction(XMLDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, SDDecl, S, questionMark, greaterThanSign);
		this.grammar.addProduction(XMLDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, EncodingDecl, S, questionMark, greaterThanSign);
		this.grammar.addProduction(XMLDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, S, questionMark, greaterThanSign);
		this.grammar.addProduction(XMLDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, EncodingDecl, SDDecl, questionMark, greaterThanSign);
		this.grammar.addProduction(XMLDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, SDDecl, questionMark, greaterThanSign);
		this.grammar.addProduction(XMLDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, EncodingDecl, questionMark, greaterThanSign);
		this.grammar.addProduction(XMLDecl, nullHandler, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, questionMark, greaterThanSign);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return XMLDecl;
	}

}
