package org.silnith.grammar.xml;

import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;

public abstract class XMLProduction {

	private static class NullProductionHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			return rightHandSide;
		}

	}

	private static class CharacterProductionHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final UnicodeTerminalSymbols terminal = (UnicodeTerminalSymbols) rightHandSide.get(0);
			return terminal.getCharacter();
		}

	}

	private static class StringProductionHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final StringBuilder builder = new StringBuilder();
			for (final Object object : rightHandSide) {
				builder.append(object);
			}
			return builder.toString();
		}

	}

	protected ProductionHandler nullHandler = new NullProductionHandler();

	protected ProductionHandler characterHandler = new CharacterProductionHandler();

	protected ProductionHandler stringHandler = new StringProductionHandler();

	protected final Grammar<UnicodeTerminalSymbols> grammar;

	public XMLProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
		super();
		this.grammar = grammar;
	}

	public abstract NonTerminalSymbol getNonTerminalSymbol();

}
