package org.silnith.browser.organic.parser.html5.grammar;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import javax.swing.border.AbstractBorder;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.dom.AfterLastChildInsertionPosition;
import org.silnith.browser.organic.parser.html5.grammar.dom.InsertionPosition;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.DOCTYPEToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndOfFileToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.silnith.browser.organic.parser.util.UnicodeCodePoints;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

public class Parser2 {
    
    private enum InsertionModeType {
        INITIAL,
        BEFORE_HTML,
        BEFORE_HEAD,
        IN_HEAD,
        IN_HEAD_NOSCRIPT,
        AFTER_HEAD,
        IN_BODY,
        TEXT,
        IN_TABLE,
        IN_TABLE_TEXT,
        IN_CAPTION,
        IN_COLUMN_GROUP,
        IN_TABLE_BODY,
        IN_ROW,
        IN_CELL,
        IN_SELECT,
        IN_SELECT_IN_TABLE,
        IN_TEMPLATE,
        AFTER_BODY,
        IN_FRAMESET,
        AFTER_FRAMESET,
        AFTER_AFTER_BODY,
        AFTER_AFTER_FRAMESET
    }

    private static final int EOF = -1;
    
    private Reader inputStream;
    
    private int currentInputCharacter;
    
    private int nextInputCharacter;
    
    private Object insertionPoint;
    
    private InsertionModeType insertionMode;
    
    private InsertionModeType originalInsertionMode;
    
    private Deque<InsertionModeType> stackOfTemplateInsertionModes;
    
    private InsertionModeType getCurrentTemplateInsertionMode() {
        return stackOfTemplateInsertionModes.peekFirst();
    }
    
    private Deque<Element> stackOfOpenElements;
    
    private Element getCurrentNode() {
        return stackOfOpenElements.peekLast();
    }
    
    private List<Element> listOfActiveFormattingElements;
    
    private Element headElementPointer;
    
    private Element formElementPointer;
    
    private boolean scriptingFlag;
    
    private boolean framesetOkFlag;
    
    private boolean abortParserOnError;
    
    private enum TokenizerState {
        DATA,
        CHARACTER_REFERENCE_IN_DATA,
        RCDATA,
        CHARACTER_REFERENCE_IN_RCDATA,
        RAWTEXT,
        SCRIPT_DATA,
        PLAINTEXT,
        TAG_OPEN,
        END_TAG_OPEN,
        TAG_NAME,
        RCDATA_LESS_THAN_SIGN,
        RCDATA_END_TAG_OPEN,
        RCDATA_END_TAG_NAME,
        RAWTEXT_LESS_THAN_SIGN,
        RAWTEXT_END_TAG_OPEN,
        RAWTEXT_END_TAG_NAME,
        SCRIPT_DATA_LESS_THAN_SIGN,
        SCRIPT_DATA_END_TAG_OPEN,
        SCRIPT_DATA_END_TAG_NAME,
        SCRIPT_DATA_ESCAPE_START,
        SCRIPT_DATA_ESCAPE_START_DASH,
        SCRIPT_DATA_ESCAPED,
        SCRIPT_DATA_ESCAPED_DASH,
        SCRIPT_DATA_ESCAPED_DASH_DASH,
        SCRIPT_DATA_ESCAPED_LESS_THAN_SIGN,
        SCRIPT_DATA_ESCAPED_END_TAG_OPEN,
        SCRIPT_DATA_ESCAPED_END_TAG_NAME,
        SCRIPT_DATA_DOUBLE_ESCAPE_START,
        SCRIPT_DATA_DOUBLE_ESCAPED,
        SCRIPT_DATA_DOUBLE_ESCAPED_DASH,
        SCRIPT_DATA_DOUBLE_ESCAPED_DASH_DASH,
        SCRIPT_DATA_DOUBLE_ESCAPED_LESS_THAN_SIGN,
        SCRIPT_DATA_DOUBLE_ESCAPED_END,
        BEFORE_ATTRIBUTE_NAME,
        ATTRIBUTE_NAME,
        AFTER_ATTRIBUTE_NAME,
        BEFORE_ATTRIBUTE_VALUE,
        ATTRIBUTE_VALUE_DOUBLE_QUOTED,
        ATTRIBUTE_VALUE_SINGLE_QUOTED,
        ATTRIBUTE_VALUE_UNQUOTED,
        CHARACTER_REFERENCE_IN_ATTRIBUTE_VALUE,
        AFTER_ATTRIBUTE_VALUE_QUOTED,
        SELF_CLOSING_START_TAG,
        BOGUS_COMMENT,
        MARKUP_DECLARATION_OPEN,
        COMMENT_START,
        COMMENT_START_DASH,
        COMMENT,
        COMMENT_END_DASH,
        COMMENT_END,
        COMMENT_END_BANG,
        DOCTYPE,
        BEFORE_DOCTYPE_NAME,
        DOCTYPE_NAME,
        AFTER_DOCTYPE_NAME,
        AFTER_DOCTYPE_PUBLIC_KEYWORD,
        BEFORE_DOCTYPE_PUBLIC_IDENTIFIER,
        DOCTYPE_PUBLIC_IDENTIFIER_DOUBLE_QUOTED,
        DOCTYPE_PUBLIC_IDENTIFIER_SINGLE_QUOTED,
        AFTER_DOCTYPE_PUBLIC_IDENTIFIER,
        BETWEEN_DOCTYPE_PUBLIC_AND_SYSTEM_IDENTIFIERS,
        AFTER_DOCTYPE_SYSTEM_KEYWORD,
        BEFORE_DOCTYPE_SYSTEM_IDENTIFIER,
        DOCTYPE_SYSTEM_IDENTIFIER_DOUBLE_QUOTED,
        DOCTYPE_SYSTEM_IDENTIFIER_SINGLE_QUOTED,
        AFTER_DOCTYPE_SYSTEM_IDENTIFIER,
        BOGUS_DOCTYPE,
        CDATA_SECTION
    }
    
    private TokenizerState tokenizerState = TokenizerState.DATA;
    
    private boolean fosterParenting;
    
    private Document document;

    private DOMImplementationRegistry registry;
    
    private int scriptNestingLevel = 0;
    
    private boolean parserPauseFlag = false;
    
    private void consumeNextInputCharacter() throws IOException {
        currentInputCharacter = nextInputCharacter;
        nextInputCharacter = inputStream.read();
    }
    
    private void tokenize() throws IOException {
        switch (tokenizerState) {
        case DATA: {
            tokenizeDataState();
        } break;
        case CHARACTER_REFERENCE_IN_DATA: {
            tokenizeCharacterReferenceInData();
        } break;
        default: break;
        }
    }

    private void tokenizeCharacterReferenceInData() {
        tokenizerState = TokenizerState.DATA;
        consumeCharacterReference();
    }
    
    private int[] consumeCharacterReference() {
        return new int[0];
    }

    private void tokenizeDataState() throws IOException {
        consumeNextInputCharacter();
        switch (currentInputCharacter) {
        case UnicodeCodePoints.AMPERSAND: {
            tokenizerState = TokenizerState.CHARACTER_REFERENCE_IN_DATA;
        } break;
        case UnicodeCodePoints.LESS_THAN_SIGN: {
            tokenizerState = TokenizerState.TAG_OPEN;
        } break;
        case UnicodeCodePoints.NULL: {
            if (abortParserOnError) {
                abortParser();
            }
            emit(new CharacterToken((char) currentInputCharacter));
        } break;
        case EOF: {
            emit(new EndOfFileToken());
        } break;
        default: {
            emit(new CharacterToken((char) currentInputCharacter));
        } break;
        }
    }
    
    private void emit(Token token) {
        switch (insertionMode) {
        case INITIAL: {
            initialInsertionMode(token);
        } break;
        default: throw new IllegalStateException();
        }
    }
    
    private void reprocessToken(Token token) {
        emit(token);
    }
    
    private void initialInsertionMode(Token token) {
        switch (token.getType()) {
        case CHARACTER: {
            final CharacterToken characterToken = (CharacterToken) token;
            switch (characterToken.getCharacter()) {
            case UnicodeCodePoints.CHARACTER_TABULATION: // fall through
            case UnicodeCodePoints.LINE_FEED: // fall through
            case UnicodeCodePoints.FORM_FEED: // fall through
            case UnicodeCodePoints.CARRIAGE_RETURN: // fall through
            case UnicodeCodePoints.SPACE: {
                /*
                 * Ignore the token.
                 */
                return;
            } // break;
            default: break;
            }
        } break;
        case COMMENT: {
            // insert a comment
            final CommentToken commentToken = (CommentToken) token;
            insertComment(commentToken);
            return;
        } // break;
        case DOCTYPE: {
            final DOCTYPEToken doctypeToken = (DOCTYPEToken) token;
            final String name = doctypeToken.getName();
            final String publicIdentifier = doctypeToken.getPublicIdentifier();
            final String systemIdentifier = doctypeToken.getSystemIdentifier();
            
            boolean nameIsHTML = name.equals("html");
            boolean publicIdentifierMissing = publicIdentifier == null;
            boolean systemIdentifierMissing = systemIdentifier == null;
            boolean systemIdentifierIsLegacyCompat = systemIdentifier.equals("about:legacy-compat");
            boolean condition1 = nameIsHTML
                    && publicIdentifier.equals("-//W3C//DTD HTML 4.0//EN")
                    && (systemIdentifier == null || systemIdentifier.equals("http://www.w3.org/TR/REC-html40/strict.dtd"));
            boolean condition2 = nameIsHTML
                    && publicIdentifier.equals("-//W3C//DTD HTML 4.01//EN")
                    && (systemIdentifier == null || systemIdentifier.equals("http://www.w3.org/TR/html4/strict.dtd"));
            boolean condition3 = nameIsHTML
                    && publicIdentifier.equals("-//W3C//DTD XHTML 1.0 Strict//EN")
                    && systemIdentifier.equals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
            boolean condition4 = nameIsHTML
                    && publicIdentifier.equals("-//W3C//DTD XHTML 1.1//EN")
                    && (systemIdentifier == null || systemIdentifier.equals("http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"));
            boolean parseError = false;
            if (!nameIsHTML) {
                parseError = true;
            }
            if (!publicIdentifierMissing) {
                parseError = true;
            }
            if (!systemIdentifierMissing && !systemIdentifierIsLegacyCompat) {
                parseError = true;
            }
            if (!condition1 && !condition2 && !condition3 && !condition4) {
                parseError = true;
            }
            if (nameIsHTML && publicIdentifierMissing && (systemIdentifierMissing || systemIdentifierIsLegacyCompat)) {
                parseError = false;
            }
            if (condition1 || condition2 || condition3 || condition4) {
                parseError = false;
            }
            if (parseError) {
                if (abortParserOnError) {
                    abortParser();
                }
            }
            
            final DOMImplementation domImplementation = registry.getDOMImplementation("Core HTML");
            final DocumentType documentType = domImplementation.createDocumentType(name,
                    publicIdentifier == null ? "" : publicIdentifier,
                            systemIdentifier == null ? "" : systemIdentifier);
            document = domImplementation.createDocument(HTML_NAMESPACE, "html", documentType);
            
            insertionMode = InsertionModeType.BEFORE_HTML;
        } // break;
        default: break;
        }
        // anything else
        if (!isIframeSrcdocDocument(document)) {
            if (abortParserOnError) {
                throw new ParseErrorException();
            }
            // set document to quirks mode
        }
        
        insertionMode = InsertionModeType.BEFORE_HTML;
        reprocessToken(token);
    }
    
    private boolean hasElementInASpecificScope(Element targetNode, List<ElementType> elementTypes) {
        final Iterator<Element> descendingIterator = stackOfOpenElements.descendingIterator();
        while (descendingIterator.hasNext()) {
            final Element node = descendingIterator.next();
            if (node == targetNode) {
                return true;
            }
            for (final ElementType type : elementTypes) {
                if (type.matches(node)) {
                    return false;
                }
            }
        }
        throw new IllegalStateException();
    }
    
    private boolean hasParticularElementInScope(Element element) {
        final List<ElementType> types = new ArrayList<>();
        types.add(new ElementType(HTML_NAMESPACE, "applet"));
        types.add(new ElementType(HTML_NAMESPACE, "caption"));
        types.add(new ElementType(HTML_NAMESPACE, "html"));
        types.add(new ElementType(HTML_NAMESPACE, "table"));
        types.add(new ElementType(HTML_NAMESPACE, "td"));
        types.add(new ElementType(HTML_NAMESPACE, "th"));
        types.add(new ElementType(HTML_NAMESPACE, "marquee"));
        types.add(new ElementType(HTML_NAMESPACE, "object"));
        types.add(new ElementType(HTML_NAMESPACE, "template"));
        types.add(new ElementType(MATHML_NAMESPACE, "mi"));
        types.add(new ElementType(MATHML_NAMESPACE, "mo"));
        types.add(new ElementType(MATHML_NAMESPACE, "mn"));
        types.add(new ElementType(MATHML_NAMESPACE, "ms"));
        types.add(new ElementType(MATHML_NAMESPACE, "mtext"));
        types.add(new ElementType(MATHML_NAMESPACE, "annotation-xml"));
        types.add(new ElementType(SVG_NAMESPACE, "foreignObject"));
        types.add(new ElementType(SVG_NAMESPACE, "desc"));
        types.add(new ElementType(SVG_NAMESPACE, "title"));
        return hasElementInASpecificScope(element, types);
    }
    
    private boolean hasParticularElementInListItemScope(Element element) {
        final List<ElementType> types = new ArrayList<>();
        types.add(new ElementType(HTML_NAMESPACE, "applet"));
        types.add(new ElementType(HTML_NAMESPACE, "caption"));
        types.add(new ElementType(HTML_NAMESPACE, "html"));
        types.add(new ElementType(HTML_NAMESPACE, "table"));
        types.add(new ElementType(HTML_NAMESPACE, "td"));
        types.add(new ElementType(HTML_NAMESPACE, "th"));
        types.add(new ElementType(HTML_NAMESPACE, "marquee"));
        types.add(new ElementType(HTML_NAMESPACE, "object"));
        types.add(new ElementType(HTML_NAMESPACE, "template"));
        types.add(new ElementType(MATHML_NAMESPACE, "mi"));
        types.add(new ElementType(MATHML_NAMESPACE, "mo"));
        types.add(new ElementType(MATHML_NAMESPACE, "mn"));
        types.add(new ElementType(MATHML_NAMESPACE, "ms"));
        types.add(new ElementType(MATHML_NAMESPACE, "mtext"));
        types.add(new ElementType(MATHML_NAMESPACE, "annotation-xml"));
        types.add(new ElementType(SVG_NAMESPACE, "foreignObject"));
        types.add(new ElementType(SVG_NAMESPACE, "desc"));
        types.add(new ElementType(SVG_NAMESPACE, "title"));
        types.add(new ElementType(HTML_NAMESPACE, "ol"));
        types.add(new ElementType(HTML_NAMESPACE, "ul"));
        return hasElementInASpecificScope(element, types);
    }
    
    private boolean hasParticularElementInButtonScope(Element element) {
        final List<ElementType> types = new ArrayList<>();
        types.add(new ElementType(HTML_NAMESPACE, "applet"));
        types.add(new ElementType(HTML_NAMESPACE, "caption"));
        types.add(new ElementType(HTML_NAMESPACE, "html"));
        types.add(new ElementType(HTML_NAMESPACE, "table"));
        types.add(new ElementType(HTML_NAMESPACE, "td"));
        types.add(new ElementType(HTML_NAMESPACE, "th"));
        types.add(new ElementType(HTML_NAMESPACE, "marquee"));
        types.add(new ElementType(HTML_NAMESPACE, "object"));
        types.add(new ElementType(HTML_NAMESPACE, "template"));
        types.add(new ElementType(MATHML_NAMESPACE, "mi"));
        types.add(new ElementType(MATHML_NAMESPACE, "mo"));
        types.add(new ElementType(MATHML_NAMESPACE, "mn"));
        types.add(new ElementType(MATHML_NAMESPACE, "ms"));
        types.add(new ElementType(MATHML_NAMESPACE, "mtext"));
        types.add(new ElementType(MATHML_NAMESPACE, "annotation-xml"));
        types.add(new ElementType(SVG_NAMESPACE, "foreignObject"));
        types.add(new ElementType(SVG_NAMESPACE, "desc"));
        types.add(new ElementType(SVG_NAMESPACE, "title"));
        types.add(new ElementType(HTML_NAMESPACE, "button"));
        return hasElementInASpecificScope(element, types);
    }
    
    private boolean hasParticularElementInTableScope(Element element) {
        final List<ElementType> types = new ArrayList<>();
        types.add(new ElementType(HTML_NAMESPACE, "html"));
        types.add(new ElementType(HTML_NAMESPACE, "table"));
        types.add(new ElementType(HTML_NAMESPACE, "template"));
        return hasElementInASpecificScope(element, types);
    }
    
    private boolean hasParticularElementInSelectScope(Element element) {
        final List<ElementType> types = new ArrayList<>();
        types.add(new ElementType(HTML_NAMESPACE, "optgroup"));
        types.add(new ElementType(HTML_NAMESPACE, "option"));
        return hasElementInASpecificScope(element, types);
    }
    
    private static final String HTML_NAMESPACE = "http://www.w3.org/1999/xhtml";
    private static final String MATHML_NAMESPACE = "http://www.w3.org/1998/Math/MathML";
    private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    private static final String XLINK_NAMESPACE = "http://www.w3.org/1999/xlink";
    private static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
    private static final String XMLNS_NAMESPACE = "http://www.w3.org/2000/xmlns/";
    
    /**
     * @see <a href="https://www.w3.org/TR/html5/syntax.html#abort-a-parser">abort a parser</a>
     */
    private void abortParser() {
        throw new IllegalStateException();
    }
    
    private boolean isIframeSrcdocDocument(Document document) {
        return false;
    }
    
    private void insertComment(CommentToken commentToken) {
        insertComment(commentToken, getAppropriatePlaceForInsertingANode());
    }
    
    private void insertComment(CommentToken commentToken, InsertionLocation adjustedInsertionLocation) {
        final String data = commentToken.getContent();
        
        final Document ownerDocument = adjustedInsertionLocation.getOwnerDocument();
        final Comment comment = ownerDocument.createComment(data);
        
        adjustedInsertionLocation.insert(comment);
    }
    
    private InsertionLocation getAppropriatePlaceForInsertingANode(Element overrideTarget) {
        Element target = overrideTarget;
        
        InsertionLocation adjustedInsertionLocation;
        if (fosterParenting
                && (target.getTagName().equals("table")
                        || target.getTagName().equals("tbody")
                        || target.getTagName().equals("tfoot")
                        || target.getTagName().equals("thead")
                        || target.getTagName().equals("tr"))) {
            throw new UnsupportedOperationException();
        } else {
            adjustedInsertionLocation = new InsideAfterLastChild(target);
        }
        
        if (adjustedInsertionLocation.isInsideTemplateElement()) {
            throw new UnsupportedOperationException();
        }
        
        return adjustedInsertionLocation;
    }
    
    private InsertionLocation getAppropriatePlaceForInsertingANode() {
        return getAppropriatePlaceForInsertingANode(getCurrentNode());
    }

}
