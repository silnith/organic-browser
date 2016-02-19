package org.silnith.browser.organic.parser.css3.selector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.TokenStream;
import org.silnith.browser.organic.parser.css3.lexical.token.ColonToken;
import org.silnith.browser.organic.parser.css3.lexical.token.DelimToken;
import org.silnith.browser.organic.parser.css3.lexical.token.HashToken;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LeftSquareBracketToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;

public class SelectorParser {
    
    private final TokenStream tokenStream;
    
    private Token nextToken;
    
    public SelectorParser(final TokenStream tokenStream) {
        super();
        this.tokenStream = tokenStream;
        this.nextToken = null;
    }
    
    public Selector parse() throws IOException {
        nextToken = tokenStream.getNextToken();
        return consumeSelectorsGroup();
    }
    
    private GroupOfSelectors consumeSelectorsGroup() throws IOException {
        final Collection<Selector> selectors = new ArrayList<>();
        selectors.add(consumeSelector());
        while (isComma(nextToken)) {
            nextToken = tokenStream.getNextToken();
            while (isSpace(nextToken)) {
                nextToken = tokenStream.getNextToken();
            }
            selectors.add(consumeSelector());
        }
        return new GroupOfSelectors(selectors);
    }
    
    private Selector consumeSelector() throws IOException {
        final SequenceOfSimpleSelectors simpleSelectorSequence = consumeSimpleSelectorSequence();
        Combinator combinator = null;
        while (isCombinator(nextToken)) {
            combinator = consumeCombinator();
            consumeSimpleSelectorSequence();
        }
        if (combinator == null) {
            return simpleSelectorSequence;
        } else {
            return new SelectorImpl(simpleSelectorSequence, combinator);
        }
    }
    
    private boolean isCombinator(final Token token) {
        if (isPlus(token)) {
            return true;
        }
        if (isGreater(token)) {
            return true;
        }
        if (isTilde(token)) {
            return true;
        }
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case WHITESPACE_TOKEN: {
                return true;
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private Combinator consumeCombinator() throws IOException {
        final Combinator combinator;
        if (isPlus(nextToken)) {
            throw new UnsupportedOperationException();
        } else if (isGreater(nextToken)) {
            combinator = new ChildCombinator();
        } else if (isTilde(nextToken)) {
            throw new UnsupportedOperationException();
        } else if (isSpace(nextToken)) {
            combinator = new DescendantCombinator();
        } else {
            throw new IllegalArgumentException();
        }
        while (isSpace(nextToken)) {
            nextToken = tokenStream.getNextToken();
        }
        return combinator;
    }
    
    private SequenceOfSimpleSelectors consumeSimpleSelectorSequence() throws IOException {
        final SimpleSelector firstSelector;
        switch (nextToken.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) nextToken;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                if (isStar(identToken)) {
                    firstSelector = consumeUniversal();
                } else if (isPeriod(identToken)) {
                    firstSelector = new UniversalSelector();
                } else {
                    firstSelector = consumeTypeSelector();
                }
            } break;
            case HASH_TOKEN: {
//                final HashToken hashToken = (HashToken) lexicalToken;
                firstSelector = new UniversalSelector();
            } break;
            case LEFT_BRACKET_TOKEN: {
//                final LeftSquareBracketToken leftSquareBracketToken = (LeftSquareBracketToken) lexicalToken;
                firstSelector = new UniversalSelector();
            } break;
            case COLON_TOKEN: {
//                final ColonToken colonToken = (ColonToken) lexicalToken;
                firstSelector = new UniversalSelector();
            } break;
            case DELIM_TOKEN: {
                final DelimToken delimToken = (DelimToken) lexicalToken;
                if (delimToken.getChars().equals(".")) {
                    firstSelector = new UniversalSelector();
                } else if (delimToken.getChars().equals("*")) {
                    firstSelector = new UniversalSelector();
                    nextToken = tokenStream.getNextToken();
                } else {
                    throw new IllegalArgumentException();
                }
            } break;
            default: {
                System.out.println(lexicalToken);
                throw new IllegalArgumentException();
            } // break;
            }
        } break;
        default: {
            throw new IllegalArgumentException();
        } // break;
        }
        final List<SimpleSelector> selectors = new ArrayList<>();
        selectors.add(firstSelector);
        while (isSimpleSelectorSequenceElement(nextToken)) {
            selectors.add(consumeSimpleSelectorSequenceElement());
        }
        return new SequenceOfSimpleSelectors(selectors);
    }
    
//    private SimpleSelector consumeSimpleSelectorSequenceFirstElement() throws IOException {
//        switch (nextToken.getType()) {
//        case LEXICAL_TOKEN: {
//            final LexicalToken lexicalToken = (LexicalToken) nextToken;
//            switch (lexicalToken.getLexicalType()) {
//            case IDENT_TOKEN: {
//                final IdentToken identToken = (IdentToken) lexicalToken;
//                if (isStar(identToken)) {
//                    return consumeUniversal();
//                } else {
//                    return consumeTypeSelector();
//                }
//            } // break;
//            default: {} break;
//            }
//        } break;
//        default: {} break;
//        }
//        throw new IllegalArgumentException();
//    }
    
    private SimpleSelector consumeSimpleSelectorSequenceElement() throws IOException {
        switch (nextToken.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) nextToken;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                if (isPeriod(identToken)) {
                    nextToken = tokenStream.getNextToken();
                    return new ClassSelector(identToken.getStringValue());
                }
            } break;
            case HASH_TOKEN: {
                final HashToken hashToken = (HashToken) lexicalToken;
                nextToken = tokenStream.getNextToken();
                return new IDSelector(hashToken.getStringValue());
            } // break;
            case LEFT_BRACKET_TOKEN: {
                final LeftSquareBracketToken leftSquareBracketToken = (LeftSquareBracketToken) lexicalToken;
                consumeAttrib();
                // return attrib selector
                throw new UnsupportedOperationException();
            } // break;
            case COLON_TOKEN: {
                final ColonToken colonToken = (ColonToken) lexicalToken;
                consumePseudo();
                // consumeNegation();
                // return pseudo selector
                throw new UnsupportedOperationException();
            } // break;
            case DELIM_TOKEN: {
                final DelimToken delimToken = (DelimToken) lexicalToken;
                if (delimToken.getChars().equals(".")) {
                    nextToken = tokenStream.getNextToken();
                    return new ClassSelector(consumeElementName());
                }
            } break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        throw new IllegalArgumentException();
    }
    
    private boolean isSimpleSelectorSequenceElement(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                if (isPeriod(identToken)) {
                    return true;
                }
            } break;
            case HASH_TOKEN: {
//                final HashToken hashToken = (HashToken) lexicalToken;
                return true;
            } // break;
            case LEFT_BRACKET_TOKEN: {
//                final LeftSquareBracketToken leftSquareBracketToken = (LeftSquareBracketToken) lexicalToken;
                return true;
            } // break;
            case COLON_TOKEN: {
//                final ColonToken colonToken = (ColonToken) lexicalToken;
                return true;
            } // break;
            case DELIM_TOKEN: {
                final DelimToken delimToken = (DelimToken) lexicalToken;
                if (delimToken.getChars().equals(".")) {
                    return true;
                }
            } break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private TypeSelector consumeTypeSelector() throws IOException {
        // namespace_prefix ?
        final String elementName = consumeElementName();
        return new TypeSelector(elementName);
    }
    
    private void consumeNamespacePrefix() {
        ;
    }
    
    private String consumeElementName() throws IOException {
        switch (nextToken.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) nextToken;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                final String elementName = identToken.getStringValue();
                nextToken = tokenStream.getNextToken();
                return elementName;
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        throw new IllegalArgumentException();
    }
    
    private UniversalSelector consumeUniversal() throws IOException {
        // namespace_prefix ?
        if (isStar(nextToken)) {
            nextToken = tokenStream.getNextToken();
            return new UniversalSelector();
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    private void consumeClass() throws IOException {
        if (!isPeriod(nextToken)) {
            throw new IllegalArgumentException();
        }
        nextToken = tokenStream.getNextToken();
        switch (nextToken.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) nextToken;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                ;
            } break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        throw new IllegalArgumentException();
    }
    
    private void consumeAttrib() {
        ;
    }
    
    private void consumePseudo() {
        ;
    }
    
    private void consumeFunctionalPseudo() {
        ;
    }
    
    private void consumeExpression() {
        ;
    }
    
    private void consumeNegation() {
        ;
    }
    
    private void consumeNegationArg() {
        ;
    }
    
    private boolean isComma(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case COMMA_TOKEN: {
                return true;
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isPlus(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                if (identToken.getStringValue().equals("+")) {
                    return true;
                }
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isGreater(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                if (identToken.getStringValue().equals(">")) {
                    return true;
                }
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isTilde(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                if (identToken.getStringValue().equals("~")) {
                    return true;
                }
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isSpace(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case WHITESPACE_TOKEN: {
                return true;
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isLeftBracket(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case LEFT_BRACKET_TOKEN: {
                return true;
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isRightBracket(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case RIGHT_BRACKET_TOKEN: {
                return true;
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isStar(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                if (identToken.getStringValue().equals("*")) {
                    return true;
                }
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isPeriod(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                if (identToken.getStringValue().equals(".")) {
                    return true;
                }
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
}
