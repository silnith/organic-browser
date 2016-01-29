package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;

import java.util.Arrays;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.Symbol;
import org.silnith.grammar.UnicodeTerminalSymbols;

/**
 * [64]   	ignoreSectContents	   ::=   	{@linkplain IgnoreSectContentsProduction Ignore} ('&lt;![' {@linkplain IgnoreSectContentsProduction ignoreSectContents} ']]&gt;' {@linkplain IgnoreSectContentsProduction Ignore})*
 * [65]   	Ignore	   ::=   	{@linkplain CharProduction Char}* - ({@linkplain CharProduction Char}* ('&lt;![' | ']]&gt;') {@linkplain CharProduction Char}*) 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-ignoreSectContents">ignoreSectContents</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Ignore">Ignore</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class IgnoreSectContentsProduction extends XMLProduction {

	private final NonTerminalSymbol ignoreSectContents;

	private final NonTerminalSymbol ignoreSectContents_Plus;

	public IgnoreSectContentsProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
		super(grammar);
		ignoreSectContents = this.grammar.getNonTerminalSymbol("ignoreSectContents");
		ignoreSectContents_Plus = this.grammar.getNonTerminalSymbol("ignoreSectContents-Plus");
		
		final NonTerminalSymbol Ignore = this.grammar.getNonTerminalSymbol("Ignore");
		final NonTerminalSymbol Ignore_Char = this.grammar.getNonTerminalSymbol("Ignore-Char");
		final NonTerminalSymbol Ignore_Char_Plus = this.grammar.getNonTerminalSymbol("Ignore-Char-Plus");
		final NonTerminalSymbol ignoreSectContents_inner_Plus = this.grammar.getNonTerminalSymbol("ignoreSectContents-inner-Plus");
		
		this.grammar.addProduction(ignoreSectContents, nullHandler, Ignore, ignoreSectContents_inner_Plus);
		this.grammar.addProduction(ignoreSectContents, nullHandler, Ignore);
		
		this.grammar.addProduction(ignoreSectContents_Plus, nullHandler, ignoreSectContents, ignoreSectContents_Plus);
		this.grammar.addProduction(ignoreSectContents_Plus, nullHandler, ignoreSectContents);
		
		this.grammar.addProduction(ignoreSectContents_inner_Plus, nullHandler, lessThanSign, exclamationMark, leftBracket, ignoreSectContents, rightBracket, rightBracket, greaterThanSign, Ignore, ignoreSectContents_inner_Plus);
		this.grammar.addProduction(ignoreSectContents_inner_Plus, nullHandler, lessThanSign, exclamationMark, leftBracket, ignoreSectContents, rightBracket, rightBracket, greaterThanSign, Ignore);
		
		this.grammar.addProduction(Ignore, stringHandler, Ignore_Char_Plus);
		this.grammar.addProduction(Ignore, stringHandler, new Symbol[0]);
		
		this.grammar.addProduction(Ignore_Char_Plus, stringHandler, Ignore_Char, Ignore_Char_Plus);
		this.grammar.addProduction(Ignore_Char_Plus, stringHandler, Ignore_Char);
		
		this.grammar.addProduction(Ignore_Char, characterHandler, tab);
		this.grammar.addProduction(Ignore_Char, characterHandler, lineFeed);
		this.grammar.addProduction(Ignore_Char, characterHandler, carriageReturn);
		this.grammar.addProduction(Ignore_Char, characterHandler, space);
		this.grammar.addProduction(Ignore_Char, characterHandler, quotationMark);
		this.grammar.addProduction(Ignore_Char, characterHandler, numberSign);
		this.grammar.addProduction(Ignore_Char, characterHandler, dollarSign);
		this.grammar.addProduction(Ignore_Char, characterHandler, percentSign);
		this.grammar.addProduction(Ignore_Char, characterHandler, ampersand);
		this.grammar.addProduction(Ignore_Char, characterHandler, apostrophe);
		this.grammar.addProduction(Ignore_Char, characterHandler, leftParenthesis);
		this.grammar.addProduction(Ignore_Char, characterHandler, rightParenthesis);
		this.grammar.addProduction(Ignore_Char, characterHandler, asterisk);
		this.grammar.addProduction(Ignore_Char, characterHandler, plusSign);
		this.grammar.addProduction(Ignore_Char, characterHandler, comma);
		this.grammar.addProduction(Ignore_Char, characterHandler, hyphenMinus);
		this.grammar.addProduction(Ignore_Char, characterHandler, fullStop);
		this.grammar.addProduction(Ignore_Char, characterHandler, solidus);
		for (final UnicodeTerminalSymbols s : Arrays.asList(
				digitZero, digitOne, digitTwo, digitThree, digitFour,
				digitFive, digitSix, digitSeven, digitEight, digitNine)) {
			this.grammar.addProduction(Ignore_Char, characterHandler, s);
		}
		this.grammar.addProduction(Ignore_Char, characterHandler, colon);
		this.grammar.addProduction(Ignore_Char, characterHandler, semicolon);
		this.grammar.addProduction(Ignore_Char, characterHandler, equalsSign);
		this.grammar.addProduction(Ignore_Char, characterHandler, questionMark);
		this.grammar.addProduction(Ignore_Char, characterHandler, atSymbol);
		for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] {
				capitalA, capitalB, capitalC, capitalD, capitalE, capitalF,
				capitalG, capitalH, capitalI, capitalJ, capitalK,
				capitalL, capitalM, capitalN, capitalO, capitalP,
				capitalQ, capitalR, capitalS, capitalT, capitalU,
				capitalV, capitalW, capitalX, capitalY, capitalZ}) {
			this.grammar.addProduction(Ignore_Char, characterHandler, s);
		}
		this.grammar.addProduction(Ignore_Char, characterHandler, reverseSolidus);
		this.grammar.addProduction(Ignore_Char, characterHandler, circumflexAccent);
		this.grammar.addProduction(Ignore_Char, characterHandler, lowLine);
		this.grammar.addProduction(Ignore_Char, characterHandler, graveAccent);
		for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] {
				smallA, smallB, smallC, smallD, smallE, smallF,
				smallG, smallH, smallI, smallJ, smallK,
				smallL, smallM, smallN, smallO, smallP,
				smallQ, smallR, smallS, smallT, smallU,
				smallV, smallW, smallX, smallY, smallZ}) {
			this.grammar.addProduction(Ignore_Char, characterHandler, s);
		}
		this.grammar.addProduction(Ignore_Char, characterHandler, leftCurlyBrace);
		this.grammar.addProduction(Ignore_Char, characterHandler, verticalBar);
		this.grammar.addProduction(Ignore_Char, characterHandler, rightCurlyBrace);
		this.grammar.addProduction(Ignore_Char, characterHandler, tilde);
		this.grammar.addProduction(Ignore_Char, characterHandler, delete);
		this.grammar.addProduction(Ignore_Char, characterHandler, belowSurrogates);
		this.grammar.addProduction(Ignore_Char, characterHandler, aboveSurrogates);
		this.grammar.addProduction(Ignore_Char, characterHandler, supplementaryMultilingualPlane);
		this.grammar.addProduction(Ignore_Char, characterHandler, supplementaryIdeographicPlane);
		this.grammar.addProduction(Ignore_Char, characterHandler, supplementaryUnassigned);
		this.grammar.addProduction(Ignore_Char, characterHandler, supplementarySpecialPurposePlane);
		this.grammar.addProduction(Ignore_Char, characterHandler, supplementaryPrivateUsePlanes);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return ignoreSectContents;
	}

	public NonTerminalSymbol getPlus() {
		return ignoreSectContents_Plus;
	}

}
