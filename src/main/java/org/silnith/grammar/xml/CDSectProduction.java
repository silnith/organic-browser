package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;

import java.util.Arrays;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.CDSect;

/**
 * [18]   	CDSect	   ::=   	{@linkplain CDSectProduction CDStart} {@linkplain CDSectProduction CData} {@linkplain CDSectProduction CDEnd}
 * [19]   	CDStart	   ::=   	'&lt;![CDATA['
 * [20]   	CData	   ::=   	({@linkplain CharProduction Char}* - ({@linkplain CharProduction Char}* ']]&gt;' {@linkplain CharProduction Char}*))
 * [21]   	CDEnd	   ::=   	']]&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CDSect">CDSect</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CDStart">CDStart</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CData">CData</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CDEnd">CDEnd</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CDSectProduction extends XMLProduction {

	private static class StripTrailingBracketsHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final CDSect cdSect = new CDSect();
			final String body = rightHandSide.get(1).toString();
			cdSect.cData = body.substring(0, body.length() - 2);
			return cdSect;
		}

	}

	private static class GreaterThanSignHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final StringBuilder stringBuilder = new StringBuilder();
			for (final Object obj : rightHandSide.subList(0, rightHandSide.size() - 1)) {
				stringBuilder.append(obj);
			}
			stringBuilder.append('>');
			return stringBuilder.toString();
		}

	}

	private static class EndingRightBracketHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final StringBuilder stringBuilder = new StringBuilder();
			for (final Object obj : rightHandSide.subList(0, rightHandSide.size() - 1)) {
				stringBuilder.append(obj);
			}
			stringBuilder.append(']');
			return stringBuilder.toString();
		}

	}

	private static class PenultimateRightBracketHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final StringBuilder stringBuilder = new StringBuilder();
			for (final Object obj : rightHandSide.subList(0, rightHandSide.size() - 2)) {
				stringBuilder.append(obj);
			}
			stringBuilder.append(']');
			stringBuilder.append(rightHandSide.get(rightHandSide.size() - 1));
			return stringBuilder.toString();
		}

	}

	private final NonTerminalSymbol CDSect;

	public CDSectProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
		super(grammar);
		CDSect = this.grammar.getNonTerminalSymbol("CDSect");
		
		final NonTerminalSymbol CDStart = this.grammar.getNonTerminalSymbol("CDStart");
		final NonTerminalSymbol CData_Char = this.grammar.getNonTerminalSymbol("CData-Char");
		final NonTerminalSymbol CData_Char_Plus = this.grammar.getNonTerminalSymbol("CData-Char-Plus");
		final NonTerminalSymbol CData_body = this.grammar.getNonTerminalSymbol("CData-body");
		final NonTerminalSymbol CData_body_inner = this.grammar.getNonTerminalSymbol("CData-body-inner");
		final NonTerminalSymbol CData_body_inner_Plus = this.grammar.getNonTerminalSymbol("CData-body-inner-Plus");
		final NonTerminalSymbol CData_body_suffix_Plus = this.grammar.getNonTerminalSymbol("CData-body-suffix-Plus");
		
		this.grammar.addProduction(CDSect, new StripTrailingBracketsHandler(), CDStart, CData_body, greaterThanSign);
		
		this.grammar.addProduction(CDStart, nullHandler, lessThanSign, exclamationMark, leftBracket, capitalC, capitalD, capitalA, capitalT, capitalA, leftBracket);
		
		final EndingRightBracketHandler endingRightBracketHandler = new EndingRightBracketHandler();
		final PenultimateRightBracketHandler penultimateRightBracketHandler = new PenultimateRightBracketHandler();
		this.grammar.addProduction(CData_body, endingRightBracketHandler, CData_body_inner, rightBracket);
		this.grammar.addProduction(CData_body, penultimateRightBracketHandler, CData_body_inner, rightBracket, CData_body_suffix_Plus);
		
		this.grammar.addProduction(CData_body_suffix_Plus, endingRightBracketHandler, CData_body_suffix_Plus, CData_Char, CData_body_inner, rightBracket);
		this.grammar.addProduction(CData_body_suffix_Plus, endingRightBracketHandler, CData_Char, CData_body_inner, rightBracket);
		
		this.grammar.addProduction(CData_body_inner, penultimateRightBracketHandler, CData_Char_Plus, rightBracket, CData_body_inner_Plus);
		this.grammar.addProduction(CData_body_inner, endingRightBracketHandler, CData_Char_Plus, rightBracket);
		this.grammar.addProduction(CData_body_inner, endingRightBracketHandler, rightBracket);
		
		this.grammar.addProduction(CData_body_inner_Plus, endingRightBracketHandler, CData_body_inner_Plus, CData_Char_Plus, rightBracket);
		this.grammar.addProduction(CData_body_inner_Plus, endingRightBracketHandler, CData_Char_Plus, rightBracket);
		
		final GreaterThanSignHandler greaterThanSignHandler = new GreaterThanSignHandler();
		this.grammar.addProduction(CData_Char_Plus, stringHandler, CData_Char_Plus, CData_Char);
		this.grammar.addProduction(CData_Char_Plus, greaterThanSignHandler, CData_Char_Plus, greaterThanSign);
		this.grammar.addProduction(CData_Char_Plus, stringHandler, CData_Char);
		this.grammar.addProduction(CData_Char_Plus, greaterThanSignHandler, greaterThanSign);
		
		this.grammar.addProduction(CData_Char, characterHandler, tab);
		this.grammar.addProduction(CData_Char, characterHandler, lineFeed);
		this.grammar.addProduction(CData_Char, characterHandler, carriageReturn);
		this.grammar.addProduction(CData_Char, characterHandler, space);
		this.grammar.addProduction(CData_Char, characterHandler, exclamationMark);
		this.grammar.addProduction(CData_Char, characterHandler, quotationMark);
		this.grammar.addProduction(CData_Char, characterHandler, numberSign);
		this.grammar.addProduction(CData_Char, characterHandler, dollarSign);
		this.grammar.addProduction(CData_Char, characterHandler, percentSign);
		this.grammar.addProduction(CData_Char, characterHandler, ampersand);
		this.grammar.addProduction(CData_Char, characterHandler, apostrophe);
		this.grammar.addProduction(CData_Char, characterHandler, leftParenthesis);
		this.grammar.addProduction(CData_Char, characterHandler, rightParenthesis);
		this.grammar.addProduction(CData_Char, characterHandler, asterisk);
		this.grammar.addProduction(CData_Char, characterHandler, plusSign);
		this.grammar.addProduction(CData_Char, characterHandler, comma);
		this.grammar.addProduction(CData_Char, characterHandler, hyphenMinus);
		this.grammar.addProduction(CData_Char, characterHandler, fullStop);
		this.grammar.addProduction(CData_Char, characterHandler, solidus);
		for (final UnicodeTerminalSymbols s : Arrays.asList(
				digitZero, digitOne, digitTwo, digitThree, digitFour,
				digitFive, digitSix, digitSeven, digitEight, digitNine)) {
			this.grammar.addProduction(CData_Char, characterHandler, s);
		}
		this.grammar.addProduction(CData_Char, characterHandler, colon);
		this.grammar.addProduction(CData_Char, characterHandler, semicolon);
		this.grammar.addProduction(CData_Char, characterHandler, lessThanSign);
		this.grammar.addProduction(CData_Char, characterHandler, equalsSign);
		this.grammar.addProduction(CData_Char, characterHandler, questionMark);
		this.grammar.addProduction(CData_Char, characterHandler, atSymbol);
		for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] {
				capitalA, capitalB, capitalC, capitalD, capitalE, capitalF,
				capitalG, capitalH, capitalI, capitalJ, capitalK,
				capitalL, capitalM, capitalN, capitalO, capitalP,
				capitalQ, capitalR, capitalS, capitalT, capitalU,
				capitalV, capitalW, capitalX, capitalY, capitalZ}) {
			this.grammar.addProduction(CData_Char, characterHandler, s);
		}
		this.grammar.addProduction(CData_Char, characterHandler, leftBracket);
		this.grammar.addProduction(CData_Char, characterHandler, reverseSolidus);
		this.grammar.addProduction(CData_Char, characterHandler, circumflexAccent);
		this.grammar.addProduction(CData_Char, characterHandler, lowLine);
		this.grammar.addProduction(CData_Char, characterHandler, graveAccent);
		for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] {
				smallA, smallB, smallC, smallD, smallE, smallF,
				smallG, smallH, smallI, smallJ, smallK,
				smallL, smallM, smallN, smallO, smallP,
				smallQ, smallR, smallS, smallT, smallU,
				smallV, smallW, smallX, smallY, smallZ}) {
			this.grammar.addProduction(CData_Char, characterHandler, s);
		}
		this.grammar.addProduction(CData_Char, characterHandler, leftCurlyBrace);
		this.grammar.addProduction(CData_Char, characterHandler, verticalBar);
		this.grammar.addProduction(CData_Char, characterHandler, rightCurlyBrace);
		this.grammar.addProduction(CData_Char, characterHandler, tilde);
		this.grammar.addProduction(CData_Char, characterHandler, delete);
		this.grammar.addProduction(CData_Char, characterHandler, belowSurrogates);
		this.grammar.addProduction(CData_Char, characterHandler, aboveSurrogates);
		this.grammar.addProduction(CData_Char, characterHandler, supplementaryMultilingualPlane);
		this.grammar.addProduction(CData_Char, characterHandler, supplementaryIdeographicPlane);
		this.grammar.addProduction(CData_Char, characterHandler, supplementaryUnassigned);
		this.grammar.addProduction(CData_Char, characterHandler, supplementarySpecialPurposePlane);
		this.grammar.addProduction(CData_Char, characterHandler, supplementaryPrivateUsePlanes);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return CDSect;
	}

}
