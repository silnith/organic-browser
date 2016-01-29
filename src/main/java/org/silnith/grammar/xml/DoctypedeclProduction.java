package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalC;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalD;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalO;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalP;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalY;
import static org.silnith.grammar.UnicodeTerminalSymbols.exclamationMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftBracket;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.rightBracket;

import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.DocTypeDecl;
import org.silnith.grammar.xml.syntax.ExternalID;
import org.silnith.grammar.xml.syntax.IntSubset;

/**
 * [28]   	doctypedecl	   ::=   	'&lt;!DOCTYPE' {@linkplain SProduction S} {@linkplain NameProduction Name} ({@linkplain SProduction S} {@linkplain ExternalIDProduction ExternalID})? {@linkplain SProduction S}? ('[' {@linkplain IntSubsetProduction intSubset} ']' {@linkplain SProduction S}?)? '&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-doctypedecl">doctypedecl</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DoctypedeclProduction extends XMLProduction {

	private static class FullHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			DocTypeDecl docTypeDecl = new DocTypeDecl();
			docTypeDecl.name = (String) rightHandSide.get(10);
			if (rightHandSide.get(12) instanceof ExternalID) {
				docTypeDecl.externalID = (ExternalID) rightHandSide.get(12);
			}
			for (final Object obj : rightHandSide) {
				if (obj instanceof IntSubset) {
					docTypeDecl.intSubset = (IntSubset) obj;
				}
			}
			return docTypeDecl;
		}

	}

	private final NonTerminalSymbol doctypedecl;

	public DoctypedeclProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction, final NameProduction nameProduction,
			final ExternalIDProduction externalIDProduction,
			final IntSubsetProduction intSubsetProduction) {
		super(grammar);
		doctypedecl = this.grammar.getNonTerminalSymbol("doctypedecl");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
		final NonTerminalSymbol ExternalID = externalIDProduction.getNonTerminalSymbol();
		final NonTerminalSymbol intSubset = intSubsetProduction.getNonTerminalSymbol();
		
		final FullHandler fullHandler = new FullHandler();
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, S, ExternalID, S, leftBracket, intSubset, rightBracket, S, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, S, ExternalID, S, leftBracket, intSubset, rightBracket, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, S, ExternalID, S, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, S, ExternalID, leftBracket, intSubset, rightBracket, S, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, S, ExternalID, leftBracket, intSubset, rightBracket, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, S, ExternalID, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, S, leftBracket, intSubset, rightBracket, S, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, S, leftBracket, intSubset, rightBracket, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, S, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, leftBracket, intSubset, rightBracket, S, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, leftBracket, intSubset, rightBracket, greaterThanSign);
		this.grammar.addProduction(doctypedecl, fullHandler, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT, capitalY, capitalP, capitalE, S, Name, greaterThanSign);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return doctypedecl;
	}

}
