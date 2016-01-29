package org.silnith.grammar.xml;

import java.util.ArrayList;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.Comment;
import org.silnith.grammar.xml.syntax.Misc;
import org.silnith.grammar.xml.syntax.PI;
import org.silnith.grammar.xml.syntax.S;

/**
 * [27]   	Misc	   ::=   	{@linkplain CommentProduction Comment} | {@linkplain PIProduction PI} | {@linkplain SProduction S} 
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Misc">Misc</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class MiscProduction extends XMLProduction {

	private static class MiscHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final Misc misc = new Misc();
			final Object obj = rightHandSide.get(0);
			if (obj instanceof Comment) {
				misc.comment = (Comment) obj;
			} else if (obj instanceof PI) {
				misc.pi = (PI) obj;
			}
			return misc;
		}

	}

	private static class NewMiscListHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final List<Misc> list = new ArrayList<>();
			list.add((Misc) rightHandSide.get(rightHandSide.size() - 1));
			if (rightHandSide.size() > 1) {
				final Misc misc = new Misc();
				misc.s = (S) rightHandSide.get(0);
				list.add(misc);
			}
			return list;
		}

	}

	private static class AppendMiscListHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final List<Misc> list = (List<Misc>) rightHandSide.get(0);
			list.add((Misc) rightHandSide.get(rightHandSide.size() - 1));
			if (rightHandSide.size() > 2) {
				final Misc misc = new Misc();
				misc.s = (S) rightHandSide.get(1);
				list.add(misc);
			}
			return list;
		}

	}

	private final NonTerminalSymbol Misc;

	private final NonTerminalSymbol Misc_Plus;

	public MiscProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction, final CommentProduction commentProduction,
			final PIProduction piProduction) {
		super(grammar);
		Misc = this.grammar.getNonTerminalSymbol("Misc");
		Misc_Plus = this.grammar.getNonTerminalSymbol("Misc-Plus");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Comment = commentProduction.getNonTerminalSymbol();
		final NonTerminalSymbol PI = piProduction.getNonTerminalSymbol();
		
		final MiscHandler miscHandler = new MiscHandler();
		this.grammar.addProduction(Misc, miscHandler, Comment);
		this.grammar.addProduction(Misc, miscHandler, PI);
		
		final NewMiscListHandler newMiscListHandler = new NewMiscListHandler();
		final AppendMiscListHandler appendMiscListHandler = new AppendMiscListHandler();
		this.grammar.addProduction(Misc_Plus, appendMiscListHandler, Misc_Plus, S, Misc);
		this.grammar.addProduction(Misc_Plus, appendMiscListHandler, Misc_Plus, Misc);
		this.grammar.addProduction(Misc_Plus, newMiscListHandler, S, Misc);
		this.grammar.addProduction(Misc_Plus, newMiscListHandler, Misc);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return Misc;
	}

	public NonTerminalSymbol getPlus() {
		return Misc_Plus;
	}

}
