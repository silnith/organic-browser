//package org.silnith.grammar.copy;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.ListIterator;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Future;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.silnith.grammar.Edge;
//import org.silnith.grammar.LookaheadItem;
//import org.silnith.grammar.ItemSet;
//import org.silnith.grammar.NonTerminalSymbol;
//import org.silnith.grammar.Parser;
//import org.silnith.grammar.Production;
//import org.silnith.grammar.Symbol;
//import org.silnith.grammar.SymbolSequence;
//import org.silnith.grammar.TerminalSymbol;
//
///**
// * @param <T> the concrete type of terminal symbols
// * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
// */
//public class Grammar<T extends TerminalSymbol> {
//
//	public interface SetFactory<V> {
//
//		Set<V> getNewSet();
//
//	}
//
//	public interface MapFactory<K, V> {
//
//		Map<K, V> getNewMap();
//
//	}
//
//	protected static class DefaultSetFactory<V> implements SetFactory<V> {
//
//		@Override
//		public Set<V> getNewSet() {
//			return new HashSet<>();
//		}
//
//	}
//
//	protected static class DefaultMapFactory<K, V> implements MapFactory<K, V> {
//
//		@Override
//		public Map<K, V> getNewMap() {
//			return new HashMap<>();
//		}
//
//	}
//
//	private static final NonTerminalSymbol START = new NonTerminalSymbol() {
//
//		@Override
//		public String toString() {
//			return "START";
//		}
//
//		@Override
//		public Type getType() {
//			return Type.NON_TERMINAL;
//		}
//
//	};
//
//	private final SetFactory<T> terminalSetFactory;
//
//	private final MapFactory<NonTerminalSymbol, Set<Production>> nonTerminalMapFactory;
//
//	private final SetFactory<NonTerminalSymbol> nonTerminalSetFactory;
//
//	private final Set<T> lexicon;
//
//	private final Map<NonTerminalSymbol, Set<Production>> productions;
//
//	private final Set<NonTerminalSymbol> nullable;
//
//	private final Map<Symbol, Set<T>> first;
//
//	private final Map<Symbol, Set<T>> follow;
//
//	public Grammar() {
//		this(new DefaultSetFactory<T>(),
//				new DefaultMapFactory<NonTerminalSymbol, Set<Production>>(),
//				new DefaultSetFactory<NonTerminalSymbol>());
////		super();
////		this.lexicon = defaultTerminalSymbolSetFactory.getNewSet();
////		this.productions = defaultNonTerminalSymbolMapFactory.getNewMap();
////		this.nullable = defaultNonTerminalSymbolSetFactory.getNewSet();
////		this.first = defaultSymbolMapFactory.getNewMap();
////		this.follow = defaultSymbolMapFactory.getNewMap();
//	}
//
//	public Grammar(final SetFactory<T> terminalSetFactory,
//			final MapFactory<NonTerminalSymbol, Set<Production>> nonTerminalMapFactory,
//			final SetFactory<NonTerminalSymbol> nonTerminalSetFactory) {
//		super();
//		this.terminalSetFactory = terminalSetFactory;
//		this.nonTerminalMapFactory = nonTerminalMapFactory;
//		this.nonTerminalSetFactory = nonTerminalSetFactory;
//
//		this.lexicon = this.terminalSetFactory.getNewSet();
//		this.productions = this.nonTerminalMapFactory.getNewMap();
//		this.nullable = this.nonTerminalSetFactory.getNewSet();
//		this.first = new HashMap<>();
//		this.follow = new HashMap<>();
//	}
//
//	public Set<T> getLexicon() {
//		return lexicon;
//	}
//
//	public Set<NonTerminalSymbol> getNullableSet() {
//		return nullable;
//	}
//
//	public Map<Symbol, Set<T>> getFirstSet() {
//		return first;
//	}
//
//	public Map<Symbol, Set<T>> getFollowSet() {
//		return follow;
//	}
//
//	public void clear() {
//		lexicon.clear();
//		productions.clear();
//		nullable.clear();
//		first.clear();
//		follow.clear();
//	}
//
//	@SafeVarargs
//	public final void addTerminalSymbols(final T... terminals) {
//		lexicon.addAll(Arrays.asList(terminals));
//	}
//
//	public void addProduction(final NonTerminalSymbol leftHandSide, final Symbol... rightHandSide) {
//		final Set<Production> productionSet = getProductionSet(leftHandSide);
//		productionSet.add(new Production(rightHandSide));
//		addTerminalSymbolsInternal(rightHandSide);
//	}
//
//	protected void addTerminalSymbolsInternal(final Symbol... symbols) {
//		for (final Symbol symbol : symbols) {
//			if (symbol.getType() == Symbol.Type.TERMINAL) {
//				@SuppressWarnings("unchecked")
//				final T terminalSymbol = (T) symbol;
//				addTerminalSymbols(terminalSymbol);
//			} else if (symbol.getType() == Symbol.Type.SYMBOL_SEQUENCE) {
//				final SymbolSequence sequence = (SymbolSequence) symbol;
//				addTerminalSymbolsInternal(sequence.getSequence());
//			}
//		}
//	}
//
//	protected Set<Production> getProductionSet(final NonTerminalSymbol symbol) {
//		if (productions.containsKey(symbol)) {
//			return productions.get(symbol);
//		} else {
//			final Set<Production> newSet = new HashSet<>();
//			productions.put(symbol, newSet);
//			return newSet;
//		}
//	}
//
//	protected boolean isNullable(final Symbol symbol) {
//		// No need to do an instanceof check, thanks to type erasure.
//		return nullable.contains(symbol);
//	}
//
//	protected boolean isNullable(final NonTerminalSymbol nonTerminalSymbol) {
//		return nullable.contains(nonTerminalSymbol);
//	}
//
//	protected boolean setNullable(final NonTerminalSymbol nonTerminalSymbol) {
//		return nullable.add(nonTerminalSymbol);
//	}
//
//	protected Set<T> getFirstSet(final Symbol symbol) {
////		final Set<T> initialTry = first.get(symbol);
////		if (initialTry == null) {
////			final Set<T> newSet = Collections.synchronizedSet(terminalSetFactory.getNewSet());
////			final Set<T> secondTry = first.putIfAbsent(symbol, newSet);
////			if (secondTry == null) {
////				return newSet;
////			} else {
////				return secondTry;
////			}
////		} else {
////			return initialTry;
////		}
//		if (first.containsKey(symbol)) {
//			return first.get(symbol);
//		} else {
//			final Set<T> newSet = terminalSetFactory.getNewSet();
//			first.put(symbol, newSet);
//			return newSet;
//		}
//	}
//
//	protected Set<T> getFollowSet(final Symbol symbol) {
//		if (follow.containsKey(symbol)) {
//			return follow.get(symbol);
//		} else {
//			final Set<T> newSet = terminalSetFactory.getNewSet();
//			follow.put(symbol, newSet);
//			return newSet;
//		}
//	}
//
//	protected LookaheadItem createInitialProduction(final NonTerminalSymbol symbol, final T endOfFileSymbol) {
//		getFirstSet(endOfFileSymbol).add(endOfFileSymbol);
//		return new LookaheadItem(START, new Production(symbol, endOfFileSymbol), 0, endOfFileSymbol);
//	}
//
//	protected List<Symbol> getProductionRemainder(final LookaheadItem item) {
//		final List<Symbol> symbols = item.getRightHandSide().getSymbols();
//		final int secondSymbolIndex = item.getParserPosition() + 1;
//		if (secondSymbolIndex < symbols.size()) {
//			final List<Symbol> remainder = new ArrayList<>(symbols.subList(secondSymbolIndex, symbols.size()));
//			remainder.add(item.getLookahead());
//			return remainder;
//		} else {
//			return Collections.<Symbol>singletonList(item.getLookahead());
//		}
//	}
//
//	protected Set<T> calculateFirstSet(final List<Symbol> tempProduction) {
//		final Set<T> set = terminalSetFactory.getNewSet();
//		for (final Symbol symbol : tempProduction) {
//			set.addAll(getFirstSet(symbol));
//			if ( !isNullable(symbol)) {
//				break;
//			}
//		}
//		return set;
//	}
//
//	protected ItemSet calculateClosure(final Collection<LookaheadItem> items) {
//		final Set<LookaheadItem> itemSet = new HashSet<>(items);
//		boolean changed;
//		do {
//			changed = false;
//			// Copy the item set because we are going to modify it in the loop.
//			// Without the copy this throws ConcurrentModificationExceptions.
//			for (final LookaheadItem item : new ArrayList<>(itemSet)) {
//				if (item.isComplete()) {
//					continue;
//				} else {
//					final Symbol nextSymbol = item.getNextSymbol();
//					final Set<T> firstSetOfRemainder = calculateFirstSet(getProductionRemainder(item));
//					if (nextSymbol.getType() == Symbol.Type.NON_TERMINAL) {
//						final NonTerminalSymbol nextNonTerminalSymbol = (NonTerminalSymbol) nextSymbol;
//						final Set<Production> productionsForNextSymbol = getProductionSet(nextNonTerminalSymbol);
//						for (final Production production : productionsForNextSymbol) {
//							for (final T lookahead : firstSetOfRemainder) {
//								final LookaheadItem newItem = new LookaheadItem(nextNonTerminalSymbol, production, 0, lookahead);
//								changed = itemSet.add(newItem) || changed;
//							}
//						}
//					}
//				}
//			}
//		} while (changed);
//		return new ItemSet(itemSet);
//	}
//
//	protected ItemSet calculateGoto(final Collection<LookaheadItem> itemSet, final Symbol symbol) {
//		final Set<LookaheadItem> jset = new HashSet<>(itemSet.size());
//		for (final LookaheadItem item : itemSet) {
//			if (item.isComplete()) {
//				continue;
//			}
//			final Symbol nextSymbol = item.getNextSymbol();
//			if (symbol.equals(nextSymbol)) {
//				final LookaheadItem newItem = new LookaheadItem(item.getLeftHandSide(), item.getRightHandSide(), item.getParserPosition() + 1, item.getLookahead());
//				jset.add(newItem);
//			}
//		}
//		return calculateClosure(jset);
//	}
//
//	public Parser<T> createParser(final NonTerminalSymbol startSymbol, final T endOfFileSymbol) {
//		final long startTime = System.currentTimeMillis();
//
//		final Set<ItemSet> parserStates = new HashSet<>();
//		final Set<Edge> edges = new HashSet<>();
//		final LookaheadItem initialProduction = createInitialProduction(startSymbol, endOfFileSymbol);
//		final ItemSet startState = calculateClosure(Collections.singleton(initialProduction));
//		parserStates.add(startState);
//		boolean changed;
//		do {
//			changed = false;
//			// Copy the item set because we are going to modify it in the loop.
//			// Without the copy this throws ConcurrentModificationExceptions.
//			for (final ItemSet parserState : new ArrayList<>(parserStates)) {
//				final Set<LookaheadItem> stateItems = parserState.getItems();
//				for (final LookaheadItem item : stateItems) {
////					System.out.print('.');
//					if (item.isComplete()) {
//						continue;
//					}
//					final Symbol nextSymbolInProduction = item.getNextSymbol();
//					if (endOfFileSymbol.equals(nextSymbolInProduction)) {
//						continue;
//					}
//					final ItemSet newParserState = calculateGoto(stateItems, nextSymbolInProduction);
//					final Edge newEdge = new Edge(parserState, nextSymbolInProduction, newParserState);
//					changed = parserStates.add(newParserState) || changed;
//					changed = edges.add(newEdge) || changed;
//				}
////				System.out.println();
//			}
//			System.out.print("Number of states: ");
//			System.out.println(parserStates.size());
//			System.out.print("Number of edges: ");
//			System.out.println(edges.size());
//			// single: 1169592
//			// batch: 1439974
//		} while (changed);
//
//		final long endTime = System.currentTimeMillis();
//
//		System.out.print("Duration: ");
//		System.out.println(endTime - startTime);
//
//		final Parser<T> parser = new Parser<>(parserStates, edges, startState, endOfFileSymbol);
//		return parser;
//	}
//
//	private class Worker implements Callable<Boolean> {
//
//		private final ItemSet itemSet;
//
//		final Symbol endOfFileSymbol;
//
//		public Worker(final ItemSet itemSet, final Symbol endOfFileSymbol) {
//			super();
//			this.itemSet = itemSet;
//			this.endOfFileSymbol = endOfFileSymbol;
//		}
//
//		@Override
//		public Boolean call() throws Exception {
//			boolean changed = false;
//			final Set<LookaheadItem> stateItems = itemSet.getItems();
//			for (final LookaheadItem item : stateItems) {
//				if (item.isComplete()) {
//					continue;
//				}
//				final Symbol nextSymbolInProduction = item.getNextSymbol();
//				if (endOfFileSymbol.equals(nextSymbolInProduction)) {
//					continue;
//				}
//				final ItemSet newParserState = calculateGoto(stateItems, nextSymbolInProduction);
//				final Edge newEdge = new Edge(itemSet, nextSymbolInProduction, newParserState);
////				changed = parserStates.add(newParserState) || changed;
////				changed = edges.add(newEdge) || changed;
//
//				final Integer previousName = parserStates.putIfAbsent(newParserState, stateCounter.incrementAndGet());
//				if (previousName == null) {
//					changed = true;
//				}
//				final Boolean previousEdge = edges.putIfAbsent(newEdge, true);
//				if (previousEdge == null) {
//					changed = true;
//				}
//			}
//			return changed;
//		}
//
//	}
//
//	private final ConcurrentMap<ItemSet, Integer> parserStates = new ConcurrentHashMap<>();
//
//	private final AtomicInteger stateCounter = new AtomicInteger();
//
//	private final ConcurrentMap<Edge, Boolean> edges = new ConcurrentHashMap<>();
//
//	public Parser<T> threadedCreateParser(final NonTerminalSymbol startSymbol, final T endOfFileSymbol, final ExecutorService executorService) throws InterruptedException, ExecutionException {
//		final long startTime = System.currentTimeMillis();
//
////		final ConcurrentMap<ItemSet, Integer> parserStates = new ConcurrentHashMap<>();
////		final AtomicInteger stateCounter = new AtomicInteger();
////		final Set<Edge> edges = new HashSet<>();
//		final LookaheadItem initialProduction = createInitialProduction(startSymbol, endOfFileSymbol);
//		final ItemSet startState = calculateClosure(Collections.singleton(initialProduction));
//		parserStates.put(startState, stateCounter.get());
//		final ArrayList<Worker> workers = new ArrayList<>();
//		boolean changed;
//		do {
//			changed = false;
//			workers.clear();
//			for (final ItemSet parserState : parserStates.keySet()) {
//				final Worker worker = new Worker(parserState, endOfFileSymbol);
//				workers.add(worker);
//			}
//			final List<Future<Boolean>> results = executorService.invokeAll(workers);
//			for (final Future<Boolean> result : results) {
//				changed = result.get() || changed;
//			}
//			System.out.print("Number of states: ");
//			System.out.println(parserStates.size());
//			System.out.print("Number of edges: ");
//			System.out.println(edges.size());
//			// single: 1169592
//			// batch: 1439974
//		} while (changed);
//
//		final long endTime = System.currentTimeMillis();
//
//		System.out.print("Duration: ");
//		System.out.println(endTime - startTime);
//
//		final Parser<T> parser = new Parser<>(parserStates.keySet(), edges.keySet(), startState, endOfFileSymbol);
//		return parser;
//	}
//
//	public void validate() {
////		System.out.println(productions.keySet());
//		for (final Map.Entry<NonTerminalSymbol, Set<Production>> entry : productions.entrySet()) {
////			final NonTerminalSymbol nonTerminal = entry.getKey();
//			final Set<Production> productionsForSymbol = entry.getValue();
//
//			for (final Production production : productionsForSymbol) {
//				for (final Symbol symbol : production.getSymbols()) {
//					if (symbol.getType() == Symbol.Type.TERMINAL) {
////						System.out.println("Terminal symbol found: " + symbol.getName());
//					} else {
//						assert symbol.getType() == Symbol.Type.NON_TERMINAL;
//
//						if (productions.containsKey(symbol)) {
////							System.out.println("Valid non-terminal symbol: " + symbol.getName());
//						} else {
////							System.out.println("Undefined symbol: " + symbol.getName());
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public void computeNullable() {
//		int size;
//		do {
//			size = nullable.size();
//			for (final Map.Entry<NonTerminalSymbol, Set<Production>> entry : productions.entrySet()) {
//				final NonTerminalSymbol nonTerminal = entry.getKey();
//				if (isNullable(nonTerminal)) {
//					continue;
//				}
//				final Set<Production> productionsForSymbol = entry.getValue();
//
//				for (final Production production : productionsForSymbol) {
//					boolean n = true;
//					for (final Symbol symbol : production.getSymbols()) {
//						n = n && isNullable(symbol);
//					}
//					if (n) {
//						setNullable(nonTerminal);
//					}
//				}
//			}
//		} while (size < nullable.size());
//	}
//
//	public void computeFirst() {
//		for (final T terminalSymbol : lexicon) {
//			getFirstSet(terminalSymbol).add(terminalSymbol);
//		}
//
//		boolean changed;
//		do {
//			changed = false;
//			for (final Map.Entry<NonTerminalSymbol, Set<Production>> entry : productions.entrySet()) {
//				final NonTerminalSymbol nonTerminal = entry.getKey();
//				final Set<T> firstSetForLeftHandSide = getFirstSet(nonTerminal);
//				final Set<Production> productionsForSymbol = entry.getValue();
//
//				for (final Production production : productionsForSymbol) {
//					for (final Symbol symbol : production.getSymbols()) {
//						final Set<T> firstSetForSymbolInProduction = getFirstSet(symbol);
//						changed = firstSetForLeftHandSide.addAll(firstSetForSymbolInProduction) || changed;
//
//						if ( !isNullable(symbol)) {
//							break;
//						}
//					}
//				}
//			}
//		} while (changed);
//	}
//
//	public void computeFollow() {
//		boolean changed;
//		do {
//			changed = false;
//			for (final Map.Entry<NonTerminalSymbol, Set<Production>> entry : productions.entrySet()) {
//				final NonTerminalSymbol nonTerminal = entry.getKey();
//				final Set<T> followSetForLeftHandSide = getFollowSet(nonTerminal);
//				final Set<Production> productionsForSymbol = entry.getValue();
//
//				for (final Production production : productionsForSymbol) {
//					final List<Symbol> productionSymbols = production.getSymbols();
//					final ListIterator<Symbol> revIter = productionSymbols.listIterator(productionSymbols.size());
//					while (revIter.hasPrevious()) {
//						final Symbol symbol = revIter.previous();
//						final Set<T> followSetForSymbolInProduction = getFollowSet(symbol);
//						changed = followSetForSymbolInProduction.addAll(followSetForLeftHandSide) || changed;
//						if ( !isNullable(symbol)) {
//							break;
//						}
//					}
//
//					final ListIterator<Symbol> forwardIter = productionSymbols.listIterator();
//					while (forwardIter.hasNext()) {
//						final int startIndex = forwardIter.nextIndex();
//						final Symbol startSymbol = forwardIter.next();
//						final Set<T> followSetForRangeStart = getFollowSet(startSymbol);
//						final ListIterator<Symbol> innerIter = productionSymbols.listIterator(startIndex + 1);
//						while (innerIter.hasNext()) {
//							final Symbol endSymbol = innerIter.next();
//							final Set<T> firstSetForRangeEnd = getFirstSet(endSymbol);
//							followSetForRangeStart.addAll(firstSetForRangeEnd);
//							if ( !isNullable(endSymbol)) {
//								break;
//							}
//						}
//					}
//				}
//			}
//		} while (changed);
//	}
//
//	public void compute() {
//		computeNullable();
//		computeFirst();
//		computeFollow();
//	}
//
//}
