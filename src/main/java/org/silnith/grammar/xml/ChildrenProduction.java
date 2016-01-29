package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.asterisk;
import static org.silnith.grammar.UnicodeTerminalSymbols.comma;
import static org.silnith.grammar.UnicodeTerminalSymbols.leftParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.plusSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.questionMark;
import static org.silnith.grammar.UnicodeTerminalSymbols.rightParenthesis;
import static org.silnith.grammar.UnicodeTerminalSymbols.verticalBar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.CP;
import org.silnith.grammar.xml.syntax.Children;
import org.silnith.grammar.xml.syntax.Choice;
import org.silnith.grammar.xml.syntax.Seq;

/**
 * [47]   	children	   ::=   	({@linkplain ChildrenProduction choice} | {@linkplain ChildrenProduction seq}) ('?' | '*' | '+')?
 * [48]   	cp	   ::=   	({@linkplain NameProduction Name} | {@linkplain ChildrenProduction choice} | {@linkplain ChildrenProduction seq}) ('?' | '*' | '+')?
 * [49]   	choice	   ::=   	'(' {@linkplain SProduction S}? {@linkplain ChildrenProduction cp} ( {@linkplain SProduction S}? '|' {@linkplain SProduction S}? {@linkplain ChildrenProduction cp} )+ {@linkplain SProduction S}? ')'
 * [50]   	seq	   ::=   	'(' {@linkplain SProduction S}? {@linkplain ChildrenProduction cp} ( {@linkplain SProduction S}? ',' {@linkplain SProduction S}? {@linkplain ChildrenProduction cp} )* {@linkplain SProduction S}? ')'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-children">children</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-cp">cp</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-choice">choice</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-seq">seq</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ChildrenProduction extends XMLProduction {

	private static class ChildrenChoiceHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final Children children = new Children();
			children.choice = (Choice) rightHandSide.get(0);
			if (rightHandSide.size() > 1) {
				final UnicodeTerminalSymbols suffix = (UnicodeTerminalSymbols) rightHandSide.get(1);
				if (suffix == questionMark) {
					children.suffix = '?';
				} else if (suffix == asterisk) {
					children.suffix = '*';
				} else if (suffix == plusSign) {
					children.suffix = '+';
				}
			}
			return children;
		}

	}

	private static class ChildrenSeqHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final Children children = new Children();
			children.seq = (Seq) rightHandSide.get(0);
			if (rightHandSide.size() > 1) {
				final UnicodeTerminalSymbols suffix = (UnicodeTerminalSymbols) rightHandSide.get(1);
				if (suffix == questionMark) {
					children.suffix = '?';
				} else if (suffix == asterisk) {
					children.suffix = '*';
				} else if (suffix == plusSign) {
					children.suffix = '+';
				}
			}
			return children;
		}

	}

	private static class AppendListHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final List<Object> list = (List<Object>) rightHandSide.get(1);
			list.add(rightHandSide.get(0));
			return list;
		}

	}

	private static class NewListHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final List<Object> list = new LinkedList<>();
			list.add(rightHandSide.get(0));
			return list;
		}

	}

	private static class SecondHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			return rightHandSide.get(1);
		}

	}

	private static class ThirdHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			return rightHandSide.get(2);
		}

	}

	private static class CPNameHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final CP cp = new CP();
			cp.name = (String) rightHandSide.get(0);
			if (rightHandSide.size() > 1) {
				final UnicodeTerminalSymbols suffix = (UnicodeTerminalSymbols) rightHandSide.get(1);
				if (suffix == questionMark) {
					cp.suffix = '?';
				} else if (suffix == asterisk) {
					cp.suffix = '*';
				} else if (suffix == plusSign) {
					cp.suffix = '+';
				}
			}
			return cp;
		}

	}

	private static class CPChoiceHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final CP cp = new CP();
			cp.choice = (Choice) rightHandSide.get(0);
			if (rightHandSide.size() > 1) {
				final UnicodeTerminalSymbols suffix = (UnicodeTerminalSymbols) rightHandSide.get(1);
				if (suffix == questionMark) {
					cp.suffix = '?';
				} else if (suffix == asterisk) {
					cp.suffix = '*';
				} else if (suffix == plusSign) {
					cp.suffix = '+';
				}
			}
			return cp;
		}

	}

	private static class CPSeqHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final CP cp = new CP();
			cp.seq = (Seq) rightHandSide.get(0);
			if (rightHandSide.size() > 1) {
				final UnicodeTerminalSymbols suffix = (UnicodeTerminalSymbols) rightHandSide.get(1);
				if (suffix == questionMark) {
					cp.suffix = '?';
				} else if (suffix == asterisk) {
					cp.suffix = '*';
				} else if (suffix == plusSign) {
					cp.suffix = '+';
				}
			}
			return cp;
		}

	}

	private final NonTerminalSymbol children;

	public ChildrenProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final SProduction sProduction, final NameProduction nameProduction) {
		super(grammar);
		children = this.grammar.getNonTerminalSymbol("children");
		
		final NonTerminalSymbol choice = this.grammar.getNonTerminalSymbol("choice");
		final NonTerminalSymbol choice_inner = this.grammar.getNonTerminalSymbol("choice-inner");
		final NonTerminalSymbol choice_inner_Plus = this.grammar.getNonTerminalSymbol("choice-inner-Plus");
		final NonTerminalSymbol seq = this.grammar.getNonTerminalSymbol("seq");
		final NonTerminalSymbol seq_inner = this.grammar.getNonTerminalSymbol("seq-inner");
		final NonTerminalSymbol seq_inner_Plus = this.grammar.getNonTerminalSymbol("seq-inner-Plus");
		final NonTerminalSymbol cp = this.grammar.getNonTerminalSymbol("cp");
		
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
		
		final ChildrenChoiceHandler childrenChoiceHandler = new ChildrenChoiceHandler();
		final ChildrenSeqHandler childrenSeqHandler = new ChildrenSeqHandler();
		this.grammar.addProduction(children, childrenChoiceHandler, choice, questionMark);
		this.grammar.addProduction(children, childrenChoiceHandler, choice, asterisk);
		this.grammar.addProduction(children, childrenChoiceHandler, choice, plusSign);
		this.grammar.addProduction(children, childrenChoiceHandler, choice);
		this.grammar.addProduction(children, childrenSeqHandler, seq, questionMark);
		this.grammar.addProduction(children, childrenSeqHandler, seq, asterisk);
		this.grammar.addProduction(children, childrenSeqHandler, seq, plusSign);
		this.grammar.addProduction(children, childrenSeqHandler, seq);
		
		this.grammar.addProduction(choice, nullHandler, leftParenthesis, S, cp, S, choice_inner_Plus, rightParenthesis);
		this.grammar.addProduction(choice, nullHandler, leftParenthesis, cp, S, choice_inner_Plus, rightParenthesis);
		this.grammar.addProduction(choice, nullHandler, leftParenthesis, S, cp, choice_inner_Plus, rightParenthesis);
		this.grammar.addProduction(choice, nullHandler, leftParenthesis, cp, choice_inner_Plus, rightParenthesis);
		
		final AppendListHandler appendListHandler = new AppendListHandler();
		final NewListHandler newListHandler = new NewListHandler();
		this.grammar.addProduction(choice_inner_Plus, appendListHandler, choice_inner, choice_inner_Plus);
		this.grammar.addProduction(choice_inner_Plus, newListHandler, choice_inner);
		
		final SecondHandler secondHandler = new SecondHandler();
		final ThirdHandler thirdHandler = new ThirdHandler();
		this.grammar.addProduction(choice_inner, thirdHandler, verticalBar, S, cp, S);
		this.grammar.addProduction(choice_inner, secondHandler, verticalBar, cp, S);
		this.grammar.addProduction(choice_inner, thirdHandler, verticalBar, S, cp);
		this.grammar.addProduction(choice_inner, secondHandler, verticalBar, cp);
		
		this.grammar.addProduction(seq, nullHandler, leftParenthesis, S, cp, S, seq_inner_Plus, rightParenthesis);
		this.grammar.addProduction(seq, nullHandler, leftParenthesis, cp, S, seq_inner_Plus, rightParenthesis);
		this.grammar.addProduction(seq, nullHandler, leftParenthesis, S, cp, seq_inner_Plus, rightParenthesis);
		this.grammar.addProduction(seq, nullHandler, leftParenthesis, cp, seq_inner_Plus, rightParenthesis);
		this.grammar.addProduction(seq, nullHandler, leftParenthesis, S, cp, S, rightParenthesis);
		this.grammar.addProduction(seq, nullHandler, leftParenthesis, cp, S, rightParenthesis);
		this.grammar.addProduction(seq, nullHandler, leftParenthesis, S, cp, rightParenthesis);
		this.grammar.addProduction(seq, nullHandler, leftParenthesis, cp, rightParenthesis);
		
		this.grammar.addProduction(seq_inner_Plus, appendListHandler, seq_inner, seq_inner_Plus);
		this.grammar.addProduction(seq_inner_Plus, newListHandler, seq_inner);
		
		this.grammar.addProduction(seq_inner, thirdHandler, comma, S, cp, S);
		this.grammar.addProduction(seq_inner, secondHandler, comma, cp, S);
		this.grammar.addProduction(seq_inner, thirdHandler, comma, S, cp);
		this.grammar.addProduction(seq_inner, secondHandler, comma, cp);
		
		final CPNameHandler cpNameHandler = new CPNameHandler();
		final CPChoiceHandler cpChoiceHandler = new CPChoiceHandler();
		final CPSeqHandler cpSeqHandler = new CPSeqHandler();
		this.grammar.addProduction(cp, cpNameHandler, Name, questionMark);
		this.grammar.addProduction(cp, cpNameHandler, Name, asterisk);
		this.grammar.addProduction(cp, cpNameHandler, Name, plusSign);
		this.grammar.addProduction(cp, cpNameHandler, Name);
		this.grammar.addProduction(cp, cpChoiceHandler, choice, questionMark);
		this.grammar.addProduction(cp, cpChoiceHandler, choice, asterisk);
		this.grammar.addProduction(cp, cpChoiceHandler, choice, plusSign);
		this.grammar.addProduction(cp, cpChoiceHandler, choice);
		this.grammar.addProduction(cp, cpSeqHandler, seq, questionMark);
		this.grammar.addProduction(cp, cpSeqHandler, seq, asterisk);
		this.grammar.addProduction(cp, cpSeqHandler, seq, plusSign);
		this.grammar.addProduction(cp, cpSeqHandler, seq);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return children;
	}

}
