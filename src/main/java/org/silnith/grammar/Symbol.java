package org.silnith.grammar;

public interface Symbol {
    
    public enum Type {
        /**
         * @see {@link TerminalSymbol}
         */
        TERMINAL,
        /**
         * @see {@link NonTerminalSymbol}
         */
        NON_TERMINAL,
        /**
         * @see {@link SymbolSequence}
         */
        SYMBOL_SEQUENCE
    }
    
    Type getType();
    
}
