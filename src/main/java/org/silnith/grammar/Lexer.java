package org.silnith.grammar;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.silnith.browser.organic.parser.util.UnicodeCodePoints;


public class Lexer implements Iterable<UnicodeTerminalSymbols> {
    
    private final Reader in;
    
    private int currentCharacter;
    
    public Lexer(final Reader in) {
        super();
        this.in = in;
        this.currentCharacter = 0;
    }
    
    @Override
    public Iterator<UnicodeTerminalSymbols> iterator() {
        return new UnicodeLexerIterator();
    }
    
    private class UnicodeLexerIterator implements Iterator<UnicodeTerminalSymbols> {
        
        private UnicodeLexerIterator() {
            super();
            try {
                final int r = in.read();
                if (r == -1) {
                    return;
                }
                if (Character.isHighSurrogate((char) r)) {
                    final int rr = in.read();
                    if (rr == -1) {
                        currentCharacter = -1;
                        return;
                    }
                    if (Character.isLowSurrogate((char) rr)) {
                        currentCharacter = Character.toCodePoint((char) r, (char) rr);
                    } else {
                        currentCharacter = r;
                    }
                } else {
                    currentCharacter = (char) r;
                }
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public boolean hasNext() {
            return currentCharacter != -1;
        }
        
        @Override
        public UnicodeTerminalSymbols next() {
            final UnicodeTerminalSymbols symbol;
            switch (currentCharacter) {
            case -1: {
                symbol = UnicodeTerminalSymbols.EOF;
            }
                break;
            case UnicodeCodePoints.CHARACTER_TABULATION: {
                symbol = UnicodeTerminalSymbols.tab;
            }
                break;
            case UnicodeCodePoints.LINE_FEED: {
                symbol = UnicodeTerminalSymbols.lineFeed;
            }
                break;
            case UnicodeCodePoints.CARRIAGE_RETURN: {
                symbol = UnicodeTerminalSymbols.carriageReturn;
            }
                break;
            case UnicodeCodePoints.SPACE: {
                symbol = UnicodeTerminalSymbols.space;
            }
                break;
            case UnicodeCodePoints.EXCLAMATION_MARK: {
                symbol = UnicodeTerminalSymbols.exclamationMark;
            }
                break;
            case UnicodeCodePoints.QUOTATION_MARK: {
                symbol = UnicodeTerminalSymbols.quotationMark;
            }
                break;
            case UnicodeCodePoints.NUMBER_SIGN: {
                symbol = UnicodeTerminalSymbols.numberSign;
            }
                break;
            case UnicodeCodePoints.DOLLAR_SIGN: {
                symbol = UnicodeTerminalSymbols.dollarSign;
            }
                break;
            case UnicodeCodePoints.PERCENTAGE_SIGN: {
                symbol = UnicodeTerminalSymbols.percentSign;
            }
                break;
            case UnicodeCodePoints.AMPERSAND: {
                symbol = UnicodeTerminalSymbols.ampersand;
            }
                break;
            case UnicodeCodePoints.APOSTROPHE: {
                symbol = UnicodeTerminalSymbols.apostrophe;
            }
                break;
            case UnicodeCodePoints.LEFT_PARENTHESIS: {
                symbol = UnicodeTerminalSymbols.leftParenthesis;
            }
                break;
            case UnicodeCodePoints.RIGHT_PARENTHESIS: {
                symbol = UnicodeTerminalSymbols.rightParenthesis;
            }
                break;
            case UnicodeCodePoints.ASTERISK: {
                symbol = UnicodeTerminalSymbols.asterisk;
            }
                break;
            case UnicodeCodePoints.PLUS_SIGN: {
                symbol = UnicodeTerminalSymbols.plusSign;
            }
                break;
            case UnicodeCodePoints.COMMA: {
                symbol = UnicodeTerminalSymbols.comma;
            }
                break;
            case UnicodeCodePoints.HYPHEN_MINUS: {
                symbol = UnicodeTerminalSymbols.hyphenMinus;
            }
                break;
            case UnicodeCodePoints.FULL_STOP: {
                symbol = UnicodeTerminalSymbols.fullStop;
            }
                break;
            case UnicodeCodePoints.SOLIDUS: {
                symbol = UnicodeTerminalSymbols.solidus;
            }
                break;
            case UnicodeCodePoints.DIGIT_ZERO: {
                symbol = UnicodeTerminalSymbols.digitZero;
            }
                break;
            case 0x31: {
                symbol = UnicodeTerminalSymbols.digitOne;
            }
                break;
            case 0x32: {
                symbol = UnicodeTerminalSymbols.digitTwo;
            }
                break;
            case 0x33: {
                symbol = UnicodeTerminalSymbols.digitThree;
            }
                break;
            case 0x34: {
                symbol = UnicodeTerminalSymbols.digitFour;
            }
                break;
            case 0x35: {
                symbol = UnicodeTerminalSymbols.digitFive;
            }
                break;
            case 0x36: {
                symbol = UnicodeTerminalSymbols.digitSix;
            }
                break;
            case 0x37: {
                symbol = UnicodeTerminalSymbols.digitSeven;
            }
                break;
            case 0x38: {
                symbol = UnicodeTerminalSymbols.digitEight;
            }
                break;
            case UnicodeCodePoints.DIGIT_NINE: {
                symbol = UnicodeTerminalSymbols.digitNine;
            }
                break;
            case UnicodeCodePoints.COLON: {
                symbol = UnicodeTerminalSymbols.colon;
            }
                break;
            case UnicodeCodePoints.SEMICOLON: {
                symbol = UnicodeTerminalSymbols.semicolon;
            }
                break;
            case UnicodeCodePoints.LESS_THAN_SIGN: {
                symbol = UnicodeTerminalSymbols.lessThanSign;
            }
                break;
            case UnicodeCodePoints.EQUALS_SIGN: {
                symbol = UnicodeTerminalSymbols.equalsSign;
            }
                break;
            case UnicodeCodePoints.GREATER_THAN_SIGN: {
                symbol = UnicodeTerminalSymbols.greaterThanSign;
            }
                break;
            case UnicodeCodePoints.QUESTION_MARK: {
                symbol = UnicodeTerminalSymbols.questionMark;
            }
                break;
            case UnicodeCodePoints.COMMERCIAL_AT: {
                symbol = UnicodeTerminalSymbols.atSymbol;
            }
                break;
            case UnicodeCodePoints.LATIN_CAPITAL_LETTER_A: {
                symbol = UnicodeTerminalSymbols.capitalA;
            }
                break;
            case 0x42: {
                symbol = UnicodeTerminalSymbols.capitalB;
            }
                break;
            case 0x43: {
                symbol = UnicodeTerminalSymbols.capitalC;
            }
                break;
            case 0x44: {
                symbol = UnicodeTerminalSymbols.capitalD;
            }
                break;
            case 0x45: {
                symbol = UnicodeTerminalSymbols.capitalE;
            }
                break;
            case 0x46: {
                symbol = UnicodeTerminalSymbols.capitalF;
            }
                break;
            case 0x47: {
                symbol = UnicodeTerminalSymbols.capitalG;
            }
                break;
            case 0x48: {
                symbol = UnicodeTerminalSymbols.capitalH;
            }
                break;
            case 0x49: {
                symbol = UnicodeTerminalSymbols.capitalI;
            }
                break;
            case 0x4A: {
                symbol = UnicodeTerminalSymbols.capitalJ;
            }
                break;
            case 0x4B: {
                symbol = UnicodeTerminalSymbols.capitalK;
            }
                break;
            case 0x4C: {
                symbol = UnicodeTerminalSymbols.capitalL;
            }
                break;
            case 0x4D: {
                symbol = UnicodeTerminalSymbols.capitalM;
            }
                break;
            case 0x4E: {
                symbol = UnicodeTerminalSymbols.capitalN;
            }
                break;
            case 0x4F: {
                symbol = UnicodeTerminalSymbols.capitalO;
            }
                break;
            case 0x50: {
                symbol = UnicodeTerminalSymbols.capitalP;
            }
                break;
            case 0x51: {
                symbol = UnicodeTerminalSymbols.capitalQ;
            }
                break;
            case 0x52: {
                symbol = UnicodeTerminalSymbols.capitalR;
            }
                break;
            case 0x53: {
                symbol = UnicodeTerminalSymbols.capitalS;
            }
                break;
            case 0x54: {
                symbol = UnicodeTerminalSymbols.capitalT;
            }
                break;
            case 0x55: {
                symbol = UnicodeTerminalSymbols.capitalU;
            }
                break;
            case 0x56: {
                symbol = UnicodeTerminalSymbols.capitalV;
            }
                break;
            case 0x57: {
                symbol = UnicodeTerminalSymbols.capitalW;
            }
                break;
            case 0x58: {
                symbol = UnicodeTerminalSymbols.capitalX;
            }
                break;
            case 0x59: {
                symbol = UnicodeTerminalSymbols.capitalY;
            }
                break;
            case UnicodeCodePoints.LATIN_CAPITAL_LETTER_Z: {
                symbol = UnicodeTerminalSymbols.capitalZ;
            }
                break;
            case UnicodeCodePoints.LEFT_SQUARE_BRACKET: {
                symbol = UnicodeTerminalSymbols.leftBracket;
            }
                break;
            case UnicodeCodePoints.REVERSE_SOLIDUS: {
                symbol = UnicodeTerminalSymbols.reverseSolidus;
            }
                break;
            case UnicodeCodePoints.RIGHT_SQUARE_BRACKET: {
                symbol = UnicodeTerminalSymbols.rightBracket;
            }
                break;
            case UnicodeCodePoints.CIRCUMFLEX_ACCENT: {
                symbol = UnicodeTerminalSymbols.circumflexAccent;
            }
                break;
            case UnicodeCodePoints.LOW_LINE: {
                symbol = UnicodeTerminalSymbols.lowLine;
            }
                break;
            case UnicodeCodePoints.GRAVE_ACCENT: {
                symbol = UnicodeTerminalSymbols.graveAccent;
            }
                break;
            case UnicodeCodePoints.LATIN_SMALL_LETTER_A: {
                symbol = UnicodeTerminalSymbols.smallA;
            }
                break;
            case 0x62: {
                symbol = UnicodeTerminalSymbols.smallB;
            }
                break;
            case 0x63: {
                symbol = UnicodeTerminalSymbols.smallC;
            }
                break;
            case 0x64: {
                symbol = UnicodeTerminalSymbols.smallD;
            }
                break;
            case 0x65: {
                symbol = UnicodeTerminalSymbols.smallE;
            }
                break;
            case 0x66: {
                symbol = UnicodeTerminalSymbols.smallF;
            }
                break;
            case 0x67: {
                symbol = UnicodeTerminalSymbols.smallG;
            }
                break;
            case 0x68: {
                symbol = UnicodeTerminalSymbols.smallH;
            }
                break;
            case 0x69: {
                symbol = UnicodeTerminalSymbols.smallI;
            }
                break;
            case 0x6A: {
                symbol = UnicodeTerminalSymbols.smallJ;
            }
                break;
            case 0x6B: {
                symbol = UnicodeTerminalSymbols.smallK;
            }
                break;
            case 0x6C: {
                symbol = UnicodeTerminalSymbols.smallL;
            }
                break;
            case 0x6D: {
                symbol = UnicodeTerminalSymbols.smallM;
            }
                break;
            case 0x6E: {
                symbol = UnicodeTerminalSymbols.smallN;
            }
                break;
            case 0x6F: {
                symbol = UnicodeTerminalSymbols.smallO;
            }
                break;
            case 0x70: {
                symbol = UnicodeTerminalSymbols.smallP;
            }
                break;
            case 0x71: {
                symbol = UnicodeTerminalSymbols.smallQ;
            }
                break;
            case 0x72: {
                symbol = UnicodeTerminalSymbols.smallR;
            }
                break;
            case 0x73: {
                symbol = UnicodeTerminalSymbols.smallS;
            }
                break;
            case 0x74: {
                symbol = UnicodeTerminalSymbols.smallT;
            }
                break;
            case 0x75: {
                symbol = UnicodeTerminalSymbols.smallU;
            }
                break;
            case 0x76: {
                symbol = UnicodeTerminalSymbols.smallV;
            }
                break;
            case 0x77: {
                symbol = UnicodeTerminalSymbols.smallW;
            }
                break;
            case 0x78: {
                symbol = UnicodeTerminalSymbols.smallX;
            }
                break;
            case 0x79: {
                symbol = UnicodeTerminalSymbols.smallY;
            }
                break;
            case UnicodeCodePoints.LATIN_SMALL_LETTER_Z: {
                symbol = UnicodeTerminalSymbols.smallZ;
            }
                break;
            case UnicodeCodePoints.LEFT_CURLY_BRACKET: {
                symbol = UnicodeTerminalSymbols.leftCurlyBrace;
            }
                break;
            case UnicodeCodePoints.VERTICAL_LINE: {
                symbol = UnicodeTerminalSymbols.verticalBar;
            }
                break;
            case UnicodeCodePoints.RIGHT_CURLY_BRACKET: {
                symbol = UnicodeTerminalSymbols.rightCurlyBrace;
            }
                break;
            case UnicodeCodePoints.TILDE: {
                symbol = UnicodeTerminalSymbols.tilde;
            }
                break;
            case UnicodeCodePoints.DELETE: {
                symbol = UnicodeTerminalSymbols.delete;
            }
                break;
            default: {
                if (currentCharacter >= 0x80 && currentCharacter <= 0xD7FF) {
                    symbol = UnicodeTerminalSymbols.belowSurrogates;
                } else if (currentCharacter >= 0xE000 && currentCharacter <= 0xFFFD) {
                    symbol = UnicodeTerminalSymbols.aboveSurrogates;
                } else if (currentCharacter >= 0x10000 && currentCharacter <= 0x1FFFF) {
                    symbol = UnicodeTerminalSymbols.supplementaryMultilingualPlane;
                } else if (currentCharacter >= 0x20000 && currentCharacter <= 0x2FFFF) {
                    symbol = UnicodeTerminalSymbols.supplementaryIdeographicPlane;
                } else if (currentCharacter >= 0x30000 && currentCharacter <= 0xDFFFF) {
                    symbol = UnicodeTerminalSymbols.supplementaryUnassigned;
                } else if (currentCharacter >= 0xE0000 && currentCharacter <= 0xEFFFF) {
                    symbol = UnicodeTerminalSymbols.supplementarySpecialPurposePlane;
                } else if (currentCharacter >= 0xF0000 && currentCharacter <= 0x10FFFF) {
                    symbol = UnicodeTerminalSymbols.supplementaryPrivateUsePlanes;
                } else {
                    symbol = null;
                }
            }
                break;
            }
            
            try {
                final int r = in.read();
                if (r == -1) {
                    currentCharacter = -1;
                    return symbol;
                }
                final char ch = (char) r;
                final int codePoint;
                if (Character.isHighSurrogate(ch)) {
                    final char chHigh = ch;
                    final int rr = in.read();
                    if (rr == -1) {
                        throw new NoSuchElementException();
                    }
                    final char chLow = (char) rr;
                    codePoint = Character.toCodePoint(chHigh, chLow);
                } else {
                    codePoint = ch;
                }
                currentCharacter = codePoint;
                return symbol;
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
    
}
