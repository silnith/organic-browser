package org.silnith.browser.organic.parser.html5.grammar.mode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.grammar.Parser.FormattingElement;
import org.silnith.browser.organic.parser.html5.grammar.Parser.Mode;
import org.silnith.browser.organic.parser.html5.grammar.dom.AfterLastChildInsertionPosition;
import org.silnith.browser.organic.parser.html5.grammar.dom.InsertionPosition;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.css.ElementCSSInlineStyle;


public abstract class InsertionMode {
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/namespaces.html#html-namespace-0">
     *      HTML namespace</a>
     */
    public static final String HTML_NAMESPACE = "http://www.w3.org/1999/xhtml";
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/namespaces.html#mathml-namespace">
     *      MathML namespace</a>
     */
    public static final String MATHML_NAMESPACE = "http://www.w3.org/1998/Math/MathML";
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/namespaces.html#svg-namespace">
     *      SVG namespace</a>
     */
    public static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/namespaces.html#xlink-namespace">
     *      XLink namespace</a>
     */
    public static final String XLINK_NAMESPACE = "http://www.w3.org/1999/xlink";
    
    public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/namespaces.html#xmlns-namespace">
     *      XMLNS namespace</a>
     */
    public static final String XMLNS_NAMESPACE = "http://www.w3.org/2000/xmlns/";
    
    /**
     * A constant for insertion modes to return when the token has been ignored.
     */
    protected static final boolean IGNORE_TOKEN = true;
    
    /**
     * A constant for insertion modes to return when the token has been handled.
     */
    protected static final boolean TOKEN_HANDLED = true;
    
    /**
     * A constant for insertion modes to return when the token needs to be
     * reprocessed by the parser.
     */
    protected static final boolean REPROCESS_TOKEN = false;
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#frameset-ok-flag">
     *      frameset-ok flag</a>
     */
    protected static final boolean NOT_OK = false;
    
    private static final Set<String> SPECIFIC_SCOPE;
    
    private static final Set<String> BUTTON_SCOPE;
    
    private static final Set<String> LIST_ITEM_SCOPE;
    
    private static final Set<String> TABLE_SCOPE;
    
    private static final Set<String> IMPLIED_END_TAGS;
    
    private static final Set<String> SPECIAL_ELEMENTS;
    
    private static final Set<String> FORMATTING_ELEMENTS;
    
    static {
        SPECIFIC_SCOPE = Collections.unmodifiableSet(new HashSet<>(
                Arrays.asList("applet", "caption", "html", "table", "td", "th", "marquee", "object", "template")));
        final Set<String> buttonScope = new HashSet<>(SPECIFIC_SCOPE);
        buttonScope.add("button");
        BUTTON_SCOPE = Collections.unmodifiableSet(buttonScope);
        final Set<String> listItemScope = new HashSet<>(SPECIFIC_SCOPE);
        listItemScope.add("ol");
        listItemScope.add("ul");
        LIST_ITEM_SCOPE = Collections.unmodifiableSet(listItemScope);
        TABLE_SCOPE = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("html", "table", "template")));
        IMPLIED_END_TAGS = Collections.unmodifiableSet(
                new HashSet<>(Arrays.asList("dd", "dt", "li", "option", "optgroup", "p", "rp", "rt")));
        SPECIAL_ELEMENTS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("address", "applet", "area",
                "article", "aside", "base", "basefont", "bgsound", "blockquote", "body", "br", "button", "caption",
                "center", "col", "colgroup", "dd", "details", "dir", "div", "dl", "dt", "embed", "fieldset",
                "figcaption", "figure", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6",
                "head", "header", "hgroup", "hr", "html", "iframe", "img", "input", "isindex", "li", "link", "listing",
                "main", "marquee", "menu", "menuitem", "meta", "nav", "noembed", "noframes", "noscript", "object", "ol",
                "p", "param", "plaintext", "pre", "script", "section", "select", "source", "style", "summary", "table",
                "tbody", "td", "template", "textarea", "tfoot", "th", "thead", "title", "tr", "track", "ul", "wbr",
                "xmp")));
        FORMATTING_ELEMENTS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("a", "b", "big", "code", "em",
                "font", "i", "nobr", "s", "small", "strike", "strong", "tt", "u")));
    }
    
    /**
     * All state should be saved in the parser. State needs to be shared across
     * many insertion modes.
     */
    protected final Parser parser;
    
    public InsertionMode(final Parser parser) {
        super();
        this.parser = parser;
    }
    
    /**
     * Insert the given token into the document.
     * 
     * @param token the token to insert into the document
     * @return whether the token was handled. {@code false} means the token has
     *         not been handled and needs to be passed to the next insertion
     *         mode.
     */
    public abstract boolean insert(final Token token);
    
    /**
     * Process the token using the rules for the given insertion mode.
     * 
     * @param mode the mode to use to process the token
     * @param token the token to process
     * @return whether the token was handled. {@code false} means the token
     *         needs to be passed to the next insertion mode.
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#using-the-rules-for">
     *      using the rules for</a>
     */
    protected boolean processUsingRulesFor(final Mode mode, final Token token) {
        return parser.processUsingRulesFor(mode, token);
    }
    
    /**
     * Whether to allow parse errors and use error-handling behavior.
     * 
     * @return whether parse errors will be suppressed
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#parse-error">
     *      parse errors</a>
     */
    protected boolean isAllowParseErrors() {
        return parser.isAllowParseErrors();
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/the-end.html#html-fragment-parsing-algorithm">
     *      HTML fragment parsing algorithm</a>
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/the-end.html#fragment-case">
     *      fragment case</a>
     */
    protected boolean isHTMLFragmentParsingAlgorithm() {
        return parser.isHTMLFragmentParsingAlgorithm();
    }
    
    /**
     * Whether scripting is enabled for this parser.
     * 
     * @return whether scripting is enabled.
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#scripting-flag">
     *      scripting flag</a>
     */
    protected boolean isScriptingEnabled() {
        return parser.isScriptingEnabled();
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/infrastructure.html#quirks-mode">
     *      quirks mode</a>
     */
    protected boolean isQuirksMode() {
        return parser.isQuirksMode();
    }
    
    protected Document getDocument() {
        return parser.getDocument();
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#foster-parent">
     *      foster parenting</a>
     */
    protected boolean isFosterParentingEnabled() {
        return parser.isFosterParentingEnabled();
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#foster-parent">
     *      foster parenting</a>
     */
    protected void enableFosterParenting() {
        parser.setFosterParentingEnabled(true);
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#foster-parent">
     *      foster parenting</a>
     */
    protected void disableFosterParenting() {
        parser.setFosterParentingEnabled(false);
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#frameset-ok-flag">
     *      frameset-ok flag</a>
     */
    protected boolean isFramesetOkFlag() {
        return parser.isFramesetOkFlag();
    }
    
    /**
     * @param isOK
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#frameset-ok-flag">
     *      frameset-ok flag</a>
     */
    protected void setFramesetOKFlag(final boolean isOK) {
        parser.setFramesetOKFlag(isOK);
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#head-element-pointer">
     *      <code>head</code> element pointer</a>
     */
    protected Element getHeadElementPointer() {
        return parser.getHeadElementPointer();
    }
    
    /**
     * @param element
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#head-element-pointer">
     *      <code>head</code> element pointer</a>
     */
    protected void setHeadElementPointer(final Element element) {
        parser.setHeadElementPointer(element);
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#form-element-pointer">
     *      <code>form</code> element pointer</a>
     */
    protected Element getFormElementPointer() {
        return parser.getFormElementPointer();
    }
    
    /**
     * @param formElement
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#form-element-pointer">
     *      <code>form</code> element pointer</a>
     */
    protected void setFormElementPointer(final Element formElement) {
        parser.setFormElementPointer(formElement);
    }
    
    /**
     * Returns the current insertion mode.
     * 
     * @return the current insertion mode
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#insertion-mode">
     *      insertion mode</a>
     */
    protected Mode getInsertionMode() {
        return parser.getInsertionMode();
    }
    
    /**
     * @param insertionMode
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#insertion-mode">
     *      insertion mode</a>
     */
    protected void setInsertionMode(final Mode insertionMode) {
        parser.setInsertionMode(insertionMode);
    }
    
    protected Mode getOriginalInsertionMode() {
        return parser.getOriginalInsertionMode();
    }
    
    protected void setOriginalInsertionMode(final Mode originalInsertionMode) {
        parser.setOriginalInsertionMode(originalInsertionMode);
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#reconstruct-the-active-formatting-elements">
     *      reconstruct the active formatting elements</a>
     */
    protected void reconstructActiveFormattingElements() {
        if (parser.listOfActiveFormattingElements.isEmpty()) {
            return;
        }
        if (parser.isMarker(
                parser.listOfActiveFormattingElements.get(parser.listOfActiveFormattingElements.size() - 1))) {
            return;
        }
        if (parser.containsOpenElement(parser.listOfActiveFormattingElements.get(
                parser.listOfActiveFormattingElements.size() - 1).getValue())) {
            return;
        }
        
        final ListIterator<FormattingElement> entryIter =
                parser.listOfActiveFormattingElements.listIterator(parser.listOfActiveFormattingElements.size());
        assert entryIter.hasPrevious();
        FormattingElement entry = null;
        while (entryIter.hasPrevious()) {
            entry = entryIter.previous();
            if (parser.isMarker(entry) || parser.containsOpenElement(entry.getValue())) {
                entry = entryIter.next();
                break;
            }
        }
        // entry is the first element to be re-opened
        do {
            final StartTagToken startTagToken = entry.getKey();
            final Element newElement = insertHTMLElement(startTagToken);
            entryIter.set(new FormattingElement(startTagToken, newElement));
            if ( !entryIter.hasNext()) {
                return;
            }
            entry = entryIter.next();
        } while (true);
    }
    
    /**
     * Resets the insertion mode appropriately based on the stack of open
     * elements.
     * 
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#reset-the-insertion-mode-appropriately">
     *      reset the insertion mode appropriately</a>
     */
    protected void resetInsertionModeAppropriately() {
        for (int index = getStackOfOpenElementsSize() - 1; index >= 0; index-- ) {
            final Element node = getOpenElement(index);
            final boolean last = (index == 0);
            // TODO: fragment -> context
            if (isElementA(node, "select")) {
                if (last) {
                    setInsertionMode(Mode.IN_SELECT);
                    return;
                }
                for (int ancestorIndex = index; ancestorIndex >= 0; ancestorIndex-- ) {
                    final Element ancestor = getOpenElement(ancestorIndex);
                    if (isElementA(ancestor, "template")) {
                        break;
                    }
                    if (isElementA(ancestor, "table")) {
                        setInsertionMode(Parser.Mode.IN_SELECT_IN_TABLE);
                        return;
                    }
                }
                setInsertionMode(Parser.Mode.IN_SELECT);
                return;
            }
            if (isElementA(node, "td", "th") && !last) {
                setInsertionMode(Mode.IN_CELL);
                return;
            }
            if (isElementA(node, "tr")) {
                setInsertionMode(Mode.IN_ROW);
                return;
            }
            if (isElementA(node, "tbody", "thead", "tfoot")) {
                setInsertionMode(Mode.IN_TABLE_BODY);
                return;
            }
            if (isElementA(node, "caption")) {
                setInsertionMode(Mode.IN_CAPTION);
                return;
            }
            if (isElementA(node, "colgroup")) {
                setInsertionMode(Mode.IN_COLUMN_GROUP);
                return;
            }
            if (isElementA(node, "table")) {
                setInsertionMode(Mode.IN_TABLE);
                return;
            }
            if (isElementA(node, "template")) {
                setInsertionMode(getCurrentTemplateInsertionMode());
                return;
            }
            if (isElementA(node, "head") && !last) {
                setInsertionMode(Mode.IN_HEAD);
                return;
            }
            if (isElementA(node, "body")) {
                setInsertionMode(Mode.IN_BODY);
                return;
            }
            if (isElementA(node, "frameset")) {
                setInsertionMode(Mode.IN_FRAMESET);
                return;
            }
            if (isElementA(node, "html")) {
                if (getHeadElementPointer() == null) {
                    setInsertionMode(Mode.BEFORE_HEAD);
                    return;
                } else {
                    setInsertionMode(Mode.AFTER_HEAD);
                    return;
                }
            }
            if (last) {
                setInsertionMode(Mode.IN_BODY);
                return;
            }
        }
        assert false;
        throw new IllegalStateException("Should have exited loop in the last check of the loop above.");
    }
    
    protected void setTokenizerState(final Tokenizer.State state) {
        parser.setTokenizerState(state);
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#adjusted-current-node">
     *      adjusted current node</a>
     */
    protected Element getAdjustedCurrentNode() {
        if (getStackOfOpenElementsSize() == 1 && isHTMLFragmentParsingAlgorithm()) {
            throw new UnsupportedOperationException();
        } else {
            return getCurrentNode();
        }
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#current-node">
     *      current node</a>
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected Element getCurrentNode() {
        return parser.getCurrentOpenElement();
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#current-node">
     *      current node</a>
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected Element popCurrentNode() {
        return parser.popOpenElement();
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected int getStackOfOpenElementsSize() {
        return parser.getNumOpenElements();
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected Element getFirstElementInStackOfOpenElements() {
        return getOpenElement(0);
    }
    
    /**
     * @param tagName
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected boolean isStackOfOpenElementsContains(final String tagName) {
        for (final Element element : parser.getOpenElementsIterable()) {
            if (isElementA(element, tagName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected Element getNodeImmediatelyBeforeCurrentNode() {
        return getOpenElement(getStackOfOpenElementsSize() - 2);
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected boolean isStackOfOpenElementsHasOnlyOneNode() {
        return getStackOfOpenElementsSize() == 1;
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected Element getSecondElementOfStackOfOpenElements() {
        return getOpenElement(1);
    }
    
    /**
     * @param element
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected void removeNodeFromStackOfOpenElements(final Element element) {
        parser.removeOpenElement(element);
    }
    
    /**
     * @param tagNames
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected boolean isStackOfOpenElementsContainsOtherThan(final Collection<String> tagNames) {
        for (final Element element : parser.getOpenElementsIterable()) {
            if ( !isElementA(element, tagNames)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param index
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected Element getOpenElement(final int index) {
        return parser.getOpenElement(index);
    }
    
    /**
     * @param element
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    protected void addToStackOfOpenElements(final Element element) {
        parser.pushOpenElement(element);
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#clear-the-stack-back-to-a-table-context">
     *      clear the stack back to a table context</a>
     */
    protected void clearStackBackToTableContext() {
        while (isElementA(getCurrentNode(), "table", "template", "html")) {
            popCurrentNode();
        }
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#clear-the-stack-back-to-a-table-body-context">
     *      clear the stack back to a table body context</a>
     */
    protected void clearStackBackToTableBodyContext() {
        while (isElementA(getCurrentNode(), "tbody", "tfoot", "thead", "template", "html")) {
            popCurrentNode();
        }
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#clear-the-stack-back-to-a-table-row-context">
     *      clear the stack back to a table row context</a>
     */
    protected void clearStackBackToTableRowContext() {
        while (isElementA(getCurrentNode(), "tr", "template", "html")) {
            popCurrentNode();
        }
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-template-insertion-modes">
     *      stack of template insertion modes</a>
     */
    protected boolean isStackOfTemplateInsertionModesEmpty() {
        return parser.getNumTemplateInsertionModes() == 0;
    }
    
    /**
     * @param mode
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-template-insertion-modes">
     *      stack of template insertion modes</a>
     */
    protected void pushOntoStackOfTemplateInsertionModes(final Parser.Mode mode) {
        parser.pushTemplateInsertionMode(mode);
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#current-template-insertion-mode">
     *      current template insertion mode</a>
     */
    protected Parser.Mode popCurrentTemplateInsertionMode() {
        return parser.popTemplateInsertionMode();
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#current-template-insertion-mode">
     *      current template insertion mode</a>
     */
    protected Parser.Mode getCurrentTemplateInsertionMode() {
        return parser.getCurrentTemplateInsertionMode();
    }
    
    protected boolean isListOfActiveFormattingElementsContainsAfterLastMarker(final String... tagNames) {
        return isListOfActiveFormattingElementsContainsAfterLastMarker(Arrays.asList(tagNames));
    }
    
    protected boolean isListOfActiveFormattingElementsContainsAfterLastMarker(final Collection<String> tagNames) {
        return false;
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#list-of-active-formatting-elements">
     *      list of active formatting elements</a>
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#push-onto-the-list-of-active-formatting-elements">
     *      push onto the list of active formatting elements</a>
     */
    protected void pushOntoListOfActiveFormattingElements(final StartTagToken startTagToken, final Element element) {
        if (startTagToken == null) {
            throw new NullPointerException();
        }
        if (element == null) {
            throw new NullPointerException();
        }
        parser.pushOntoListOfActiveFormattingElements(startTagToken, element);
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#list-of-active-formatting-elements">
     *      list of active formatting elements</a>
     */
    protected void insertMarkerAtEndOfListOfActiveFormattingElements() {
        parser.addMarkerToListOfActiveFormattingElements();
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#clear-the-list-of-active-formatting-elements-up-to-the-last-marker">
     *      clear the list of active formatting elements up to the last
     *      marker</a>
     */
    protected void clearListOfActiveFormattingElementsUpToLastMarker() {
        parser.clearActiveFormattingElements();
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#pending-table-character-tokens">
     *      pending table character tokens</a>
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/common-microsyntaxes.html#space-character">
     *      space characters</a>
     */
    protected boolean isPendingTableCharacterTokensListContainsCharactersThatAreNotSpaceCharacters() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    protected List<CharacterToken> getPendingTableCharacterTokens() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    protected void setPendingTableCharacterTokens() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    /**
     * @param characterToken
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#pending-table-character-tokens">
     *      pending table character tokens</a>
     */
    protected void appendToPendingTableCharacterTokens(final CharacterToken characterToken) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/the-end.html#stop-parsing">
     *      stop parsing</a>
     */
    protected void stopParsing() {
        parser.stopParsing();
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#appropriate-place-for-inserting-a-node">
     *      appropriate place for inserting a node</a>
     */
    protected InsertionPosition getAppropriatePlaceForInsertingNode() {
        return getAppropriatePlaceForInsertingNode(getCurrentNode());
    }
    
    /**
     * @param overrideTarget
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#appropriate-place-for-inserting-a-node">
     *      appropriate place for inserting a node</a>
     */
    protected InsertionPosition getAppropriatePlaceForInsertingNode(final Node overrideTarget) {
        final Node target = overrideTarget;
        
        final InsertionPosition adjustedInsertionLocation;
        
        final Set<String> tableContainers = new HashSet<>(Arrays.asList("table", "tbody", "tfoot", "thead", "tr"));
        if (isFosterParentingEnabled() && target.getNodeType() == Node.ELEMENT_NODE
                && tableContainers.contains(target.getNodeName())) {
            // find last template in stack of open elements
            // find last table in stack of open elements
            throw new UnsupportedOperationException();
        } else {
            adjustedInsertionLocation = new AfterLastChildInsertionPosition(target);
        }
        
        if (adjustedInsertionLocation.getContainingNode().getNodeType() == Node.ELEMENT_NODE
                && adjustedInsertionLocation.getContainingNode().getNodeName().equals("template")) {
            // return position inside "template contents", after last child
            throw new UnsupportedOperationException();
        } else {
            return adjustedInsertionLocation;
        }
    }
    
    /**
     * @param characterToken
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#insert-a-character">
     *      insert a character</a>
     */
    protected void insertCharacter(final CharacterToken characterToken) {
        insertCharacter(characterToken.getCharacter());
    }
    
    /**
     * @param character
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#insert-a-character">
     *      insert a character</a>
     */
    protected void insertCharacter(final char character) {
        final String data = String.valueOf(character);
        final InsertionPosition adjustedInsertionLocation = getAppropriatePlaceForInsertingNode();
        if (adjustedInsertionLocation.getContainingNode().getNodeType() == Node.DOCUMENT_NODE) {
            // abort
            return;
        }
        final Node nodeImmediatelyBefore = adjustedInsertionLocation.getNodeImmediatelyBefore();
        if (nodeImmediatelyBefore != null && nodeImmediatelyBefore.getNodeType() == Node.TEXT_NODE) {
            final Text textNode = (Text) nodeImmediatelyBefore;
            textNode.appendData(data);
        } else {
            final Text textNode = adjustedInsertionLocation.getContainingNode().getOwnerDocument().createTextNode(data);
            adjustedInsertionLocation.insert(textNode);
        }
    }
    
    /**
     * @param commentToken
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#insert-a-comment">
     *      insert a comment</a>
     */
    protected void insertComment(final CommentToken commentToken) {
        insertComment(commentToken, getAppropriatePlaceForInsertingNode());
    }
    
    /**
     * @param commentToken
     * @param position
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#insert-a-comment">
     *      insert a comment</a>
     */
    protected void insertComment(final CommentToken commentToken, final InsertionPosition position) {
        final String data = commentToken.getContent();
        final InsertionPosition adjustedInsertionLocation = position;
        final Node containingNode = adjustedInsertionLocation.getContainingNode();
        final Document ownerDocument;
        if (containingNode.getNodeType() == Node.DOCUMENT_NODE) {
            /*
             * This is against the "spec", but it appears the "owner document"
             * of the document itself is null, so special-casing this for the
             * case where websites put comments before any markup.
             */
            ownerDocument = (Document) containingNode;
        } else {
            ownerDocument = containingNode.getOwnerDocument();
        }
        final Comment commentNode = ownerDocument.createComment(data);
        adjustedInsertionLocation.insert(commentNode);
        
        // do not fire DOM mutation events
        // do fire mutation observers
    }
    
    /**
     * @param startTagToken
     * @param givenNamespace
     * @param intendedParent
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#create-an-element-for-the-token">
     *      create an element for a token</a>
     */
    protected Element createElementForToken(final StartTagToken startTagToken, final String givenNamespace,
            final Node intendedParent) {
        final Document document = intendedParent.getOwnerDocument();
        final Element element = document.createElementNS(givenNamespace, startTagToken.getTagName());
        for (final TagToken.Attribute attribute : startTagToken.getAttributes()) {
            final String name = attribute.getName();
            final String value = attribute.getValue();
            element.setAttribute(name, value);
        }
        // check for xmlns attribute
        // check for xmlns:xlink attribute
        if (isResettable(element)) {
            invokeResetAlgorithm(element);
        }
        // check if form-associated element
        if (isFormAssociatedElement(element) && getFormElementPointer() != null
                && !isStackOfOpenElementsContains("template")) {
            // check not reassociateable or no form attribute
            // check intended parent in same home subtree as form pointer
            // associate with form
        }
        return element;
    }
    
    /**
     * @param startTagToken
     * @param givenNamespace
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#insert-a-foreign-element">
     *      insert a foreign element</a>
     */
    protected Element insertForeignElement(final StartTagToken startTagToken, final String givenNamespace) {
        final InsertionPosition adjustedInsertionLocation = getAppropriatePlaceForInsertingNode();
        final Element element =
                createElementForToken(startTagToken, givenNamespace, adjustedInsertionLocation.getContainingNode());
        // if Document, drop on floor instead
        adjustedInsertionLocation.insert(element);
        addToStackOfOpenElements(element);
        assert getCurrentNode() == element;
        return element;
    }
    
    /**
     * @param startTagToken
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#insert-an-html-element">
     *      insert an HTML element</a>
     */
    protected Element insertHTMLElement(final StartTagToken startTagToken) {
        final Element element = insertForeignElement(startTagToken, HTML_NAMESPACE);
        if (element.hasAttributeNS(null, "style") && element.isSupported("CSS", "2.0")) {
            final ElementCSSInlineStyle elementCSSInlineStyle = (ElementCSSInlineStyle) element;
            elementCSSInlineStyle.getStyle().setCssText(element.getAttributeNS(null, "style"));
        }
        return element;
    }
    
    /**
     * @param tagName
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#insert-an-html-element">
     *      insert an HTML element</a>
     */
    protected Element insertHTMLElement(final String tagName) {
        final StartTagToken startTagToken = new StartTagToken();
        for (final char character : tagName.toCharArray()) {
            startTagToken.appendToTagName(character);
        }
        return insertHTMLElement(startTagToken);
//		return insertForeignElement(startTagToken, HTML_NAMESPACE);
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#generate-implied-end-tags">
     *      generate implied end tags</a>
     */
    protected void generateImpliedEndTagsExcept(final Collection<String> excludedTags) {
        final Set<String> implied = new HashSet<>(IMPLIED_END_TAGS);
        implied.removeAll(excludedTags);
        while (implied.contains(getCurrentNode().getTagName())) {
            popCurrentNode();
        }
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#generate-implied-end-tags">
     *      generate implied end tags</a>
     */
    protected void generateImpliedEndTagsExcept(final String... excludedTags) {
        generateImpliedEndTagsExcept(Arrays.asList(excludedTags));
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#generate-implied-end-tags">
     *      generate implied end tags</a>
     */
    protected void generateImpliedEndTags() {
        while (IMPLIED_END_TAGS.contains(getCurrentNode().getTagName())) {
            popCurrentNode();
        }
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#close-the-cell">
     *      close the cell</a>
     */
    protected void closeCell() {
        generateImpliedEndTags();
        if ( !isElementA(getCurrentNode(), "td", "th")) {
            if (isAllowParseErrors()) {
                // do nothing?
            } else {
                throw new ParseErrorException(
                        "Expected current node to be a table cell, instead it was: " + getCurrentNode().getTagName());
            }
        }
        // pop stack until popped "td" or "th"
        Element popped;
        do {
            popped = popCurrentNode();
        } while ( !isElementA(popped, "td", "th"));
        clearListOfActiveFormattingElementsUpToLastMarker();
        setInsertionMode(Parser.Mode.IN_ROW);
    }
    
    /**
     * @param startTagToken
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#generic-raw-text-element-parsing-algorithm">
     *      generic raw text element parsing algorithm</a>
     */
    protected void genericRawTextElementParsingAlgorithm(final StartTagToken startTagToken) {
        insertHTMLElement(startTagToken);
        setTokenizerState(Tokenizer.State.RAWTEXT);
        setOriginalInsertionMode(getInsertionMode());
        setInsertionMode(Parser.Mode.TEXT);
    }
    
    /**
     * @param startTagToken
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#generic-rcdata-element-parsing-algorithm">
     *      generic RCDATA element parsing algorithm</a>
     */
    protected void genericRCDATAElementParsingAlgorithm(final StartTagToken startTagToken) {
        insertHTMLElement(startTagToken);
        setTokenizerState(Tokenizer.State.RCDATA);
        setOriginalInsertionMode(getInsertionMode());
        setInsertionMode(Parser.Mode.TEXT);
    }
    
    /**
     * @param startTagToken
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#adjust-mathml-attributes">
     *      adjust MathML attributes</a>
     */
    protected void adjustMathMLAttributes(final StartTagToken startTagToken) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    /**
     * @param startTagToken
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#adjust-foreign-attributes">
     *      adjust foreign attributes</a>
     */
    protected void adjustForeignAttributes(final StartTagToken startTagToken) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    /**
     * @param startTagToken
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#adjust-svg-attributes">
     *      adjust SVG attributes</a>
     */
    protected void adjustSVGAttributes(final StartTagToken startTagToken) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    /**
     * @param target
     * @param elementTypes
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#has-an-element-in-the-specific-scope">
     *      have an element <var>target node</var> in a specific scope</a>
     */
    protected boolean hasElementInScope(final String target, final Collection<String> elementTypes) {
        for (int index = getStackOfOpenElementsSize() - 1; index >= 0; index-- ) {
            final Element node = getOpenElement(index);
            if (isElementA(node, target)) {
                return true;
            }
            if (isElementA(node, elementTypes)) {
                return false;
            }
        }
        throw new ParseErrorException("Should have found an html element in the stack of open elements.");
    }
    
    /**
     * @param target
     * @param elementTypes
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#has-an-element-in-the-specific-scope">
     *      have an element <var>target node</var> in a specific scope</a>
     */
    protected boolean hasElementInScope(final String target, final String... elementTypes) {
        return hasElementInScope(target, Arrays.asList(elementTypes));
    }
    
    /**
     * @param target
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#has-an-element-in-scope">
     *      have a particular element in scope</a>
     */
    protected boolean hasParticularElementInScope(final String target) {
        return hasElementInScope(target, SPECIFIC_SCOPE);
    }
    
    protected boolean hasParticularElementInButtonScope(final String target) {
        return hasElementInScope(target, BUTTON_SCOPE);
    }
    
    protected boolean hasParticularElementInListItemScope(final String target) {
        return hasElementInScope(target, LIST_ITEM_SCOPE);
    }
    
    protected boolean hasParticularElementInTableScope(final String target) {
        return hasElementInScope(target, TABLE_SCOPE);
    }
    
    /**
     * @param target
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#has-an-element-in-select-scope">
     *      have a particular element in select scope</a>
     */
    protected boolean hasParticularElementInSelectScope(final String target) {
        for (int index = getStackOfOpenElementsSize() - 1; index >= 0; index-- ) {
            final Element node = getOpenElement(index);
            if (isElementA(node, target)) {
                return true;
            }
            if ( !isElementA(node, "option", "optgroup")) {
                return false;
            }
        }
        throw new ParseErrorException("Should have found an html element in the stack of open elements.");
    }
    
    protected void acknowledgeTokenSelfClosingFlag(final StartTagToken startTagToken) {
        if (startTagToken.isSelfClosing()) {
            parser.acknowledgeSelfClosingFlag();
        }
    }
    
    protected boolean isElementA(final Element element, final Collection<String> tagNames) {
        return tagNames.contains(element.getTagName());
    }
    
    protected boolean isElementA(final Element element, final String... tagNames) {
        return isElementA(element, Arrays.asList(tagNames));
    }
    
    protected boolean isSpecialCategory(final Element element) {
        return isElementA(element, SPECIAL_ELEMENTS);
    }
    
    protected boolean isSpecialCategory(final String tagName) {
        return SPECIAL_ELEMENTS.contains(tagName);
    }
    
    protected boolean isSpecialCategoryExcept(final String tagName, final Collection<String> excluded) {
        final Set<String> set = new HashSet<>(SPECIAL_ELEMENTS);
        set.removeAll(excluded);
        return set.contains(tagName);
    }
    
    protected boolean isSpecialCategoryExcept(final String tagName, final String... excluded) {
        return isSpecialCategoryExcept(tagName, Arrays.asList(excluded));
    }
    
    protected boolean isFormattingCategory(final String tagName) {
        return FORMATTING_ELEMENTS.contains(tagName);
    }
    
    /**
     * @param element
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/forms.html#category-reset">
     *      resettable elements</a>
     */
    protected boolean isResettable(final Element element) {
        // TODO
        return false;
    }
    
    /**
     * @param element
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/association-of-controls-and-forms.html#concept-form-reset-control">
     *      reset algorithm</a>
     */
    protected void invokeResetAlgorithm(final Element element) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    /**
     * @param element
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/forms.html#form-associated-element">
     *      form-associated elements</a>
     */
    protected boolean isFormAssociatedElement(final Element element) {
        // TODO
        return false;
    }
    
    protected TagToken.Attribute getAttributeNamed(final StartTagToken startTagToken, final String attributeName) {
        for (final TagToken.Attribute attribute : startTagToken.getAttributes()) {
            final String name = attribute.getName();
            if (name.equals(attributeName)) {
                return attribute;
            }
        }
        return null;
    }
    
}
