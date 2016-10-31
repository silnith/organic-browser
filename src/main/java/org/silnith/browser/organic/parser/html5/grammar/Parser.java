package org.silnith.browser.organic.parser.html5.grammar;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.silnith.browser.organic.network.Download;
import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.mode.AfterAfterBodyInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.AfterAfterFramesetInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.AfterBodyInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.AfterFramesetInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.AfterHeadInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.BeforeHeadInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.BeforeHtmlInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.ForeignContentMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InBodyInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InCaptionInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InCellInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InColumnGroupInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InFramesetInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InHeadInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InHeadNoScriptInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InRowInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InSelectInTableInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InSelectInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InTableBodyInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InTableInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InTableTextInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InTemplateInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InitialInsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.InsertionMode;
import org.silnith.browser.organic.parser.html5.grammar.mode.TextInsertionMode;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;


public class Parser {
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#insertion-mode">
     *      insertion mode</a>
     * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
     */
    public enum Mode {
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#the-initial-insertion-mode">
         *      12.2.5.4.1 The "initial" insertion mode</a>
         */
        INITIAL,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#the-before-html-insertion-mode">
         *      12.2.5.4.2 The "before html" insertion mode</a>
         */
        BEFORE_HTML,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#the-before-head-insertion-mode">
         *      12.2.5.4.3 The "before head" insertion mode</a>
         */
        BEFORE_HEAD,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-inhead">
         *      12.2.5.4.4 The "in head" insertion mode</a>
         */
        IN_HEAD,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-inheadnoscript">
         *      12.2.5.4.5 The "in head noscript" insertion mode</a>
         */
        IN_HEAD_NOSCRIPT,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#the-after-head-insertion-mode">
         *      12.2.5.4.6 The "after head" insertion mode</a>
         */
        AFTER_HEAD,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-inbody">
         *      12.2.5.4.7 The "in body" insertion mode</a>
         */
        IN_BODY,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-incdata">
         *      12.2.5.4.8 The "text" insertion mode</a>
         */
        TEXT,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intable">
         *      12.2.5.4.9 The "in table" insertion mode</a>
         */
        IN_TABLE,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intabletext">
         *      12.2.5.4.10 The "in table text" insertion mode</a>
         */
        IN_TABLE_TEXT,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-incaption">
         *      12.2.5.4.11 The "in caption" insertion mode</a>
         */
        IN_CAPTION,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-incolgroup">
         *      12.2.5.4.12 The "in column group" insertion mode</a>
         */
        IN_COLUMN_GROUP,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intbody">
         *      12.2.5.4.13 The "in table body" insertion mode</a>
         */
        IN_TABLE_BODY,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intr">
         *      12.2.5.4.14 The "in row" insertion mode</a>
         */
        IN_ROW,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intd">
         *      12.2.5.4.15 The "in cell" insertion mode</a>
         */
        IN_CELL,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-inselect">
         *      12.2.5.4.16 The "in select" insertion mode</a>
         */
        IN_SELECT,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-inselectintable">
         *      12.2.5.4.17 The "in select in table" insertion mode</a>
         */
        IN_SELECT_IN_TABLE,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intemplate">
         *      12.2.5.4.18 The "in template" insertion mode</a>
         */
        IN_TEMPLATE,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-afterbody">
         *      12.2.5.4.19 The "after body" insertion mode</a>
         */
        AFTER_BODY,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-inframeset">
         *      12.2.5.4.20 The "in frameset" insertion mode</a>
         */
        IN_FRAMESET,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-afterframeset">
         *      12.2.5.4.21 The "after frameset" insertion mode</a>
         */
        AFTER_FRAMESET,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#the-after-after-body-insertion-mode">
         *      12.2.5.4.22 The "after after body" insertion mode</a>
         */
        AFTER_AFTER_BODY,
        /**
         * @see <a href=
         *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#the-after-after-frameset-insertion-mode">
         *      12.2.5.4.23 The "after after frameset" insertion mode</a>
         */
        AFTER_AFTER_FRAMESET
    }
    
    public static class FormattingElement implements Map.Entry<StartTagToken, Element> {
        
        private final StartTagToken startTagToken;
        
        private final Element element;
        
        public FormattingElement(final StartTagToken startTagToken, final Element element) {
            super();
            this.startTagToken = startTagToken;
            this.element = element;
        }
        
        @Override
        public StartTagToken getKey() {
            return startTagToken;
        }
        
        @Override
        public Element getValue() {
            return element;
        }
        
        @Override
        public Element setValue(final Element value) {
            throw new UnsupportedOperationException();
        }
        
    }
    
    private final Tokenizer tokenizer;
    
    private final Map<Mode, InsertionMode> insertionModeMap;
    
    private final InsertionMode foreignContentMode;
    
    private final boolean allowParseErrors;
    
    private final boolean scriptingEnabled;
    
    private boolean fosterParenting;
    
    private boolean framesetOK;
    
    private boolean quirksMode;
    
    private Mode insertionMode;
    
    private Mode originalInsertionMode;
    
    private boolean stop;
    
    private final Document document;
    
    private Element headElementPointer;
    
    private Element formElementPointer;
    
    private final List<Element> stackOfOpenElements;
    
    public final List<FormattingElement> listOfActiveFormattingElements;
    
    private final List<Mode> stackOfTemplateInsertionModes;
    
    public Parser(final Tokenizer tokenizer, final DOMImplementationRegistry registry) {
        super();
        this.tokenizer = tokenizer;
        this.insertionModeMap = new EnumMap<>(Mode.class);
        this.foreignContentMode = new ForeignContentMode(this);
        this.allowParseErrors = true;
        this.scriptingEnabled = false;
        this.fosterParenting = false;
        this.framesetOK = true;
        this.quirksMode = false;
        this.insertionMode = Mode.INITIAL;
        this.originalInsertionMode = null;
        this.stop = false;
        final DOMImplementation domImplementation = registry.getDOMImplementation("Core 2.0");
//        final DOMImplementation domImplementation = registry.getDOMImplementation("Core 2.0 HTML 2.0");
        this.document = domImplementation.createDocument(InsertionMode.HTML_NAMESPACE, "html", null);
        this.headElementPointer = null;
        this.formElementPointer = null;
        this.stackOfOpenElements = new ArrayList<>();
        this.listOfActiveFormattingElements = new ArrayList<>();
        this.stackOfTemplateInsertionModes = new ArrayList<>();
        
        this.insertionModeMap.put(Mode.INITIAL, new InitialInsertionMode(this));
        this.insertionModeMap.put(Mode.BEFORE_HTML, new BeforeHtmlInsertionMode(this));
        this.insertionModeMap.put(Mode.BEFORE_HEAD, new BeforeHeadInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_HEAD, new InHeadInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_HEAD_NOSCRIPT, new InHeadNoScriptInsertionMode(this));
        this.insertionModeMap.put(Mode.AFTER_HEAD, new AfterHeadInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_BODY, new InBodyInsertionMode(this));
        this.insertionModeMap.put(Mode.TEXT, new TextInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_TABLE, new InTableInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_TABLE_TEXT, new InTableTextInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_CAPTION, new InCaptionInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_COLUMN_GROUP, new InColumnGroupInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_TABLE_BODY, new InTableBodyInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_ROW, new InRowInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_CELL, new InCellInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_SELECT, new InSelectInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_SELECT_IN_TABLE, new InSelectInTableInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_TEMPLATE, new InTemplateInsertionMode(this));
        this.insertionModeMap.put(Mode.AFTER_BODY, new AfterBodyInsertionMode(this));
        this.insertionModeMap.put(Mode.IN_FRAMESET, new InFramesetInsertionMode(this));
        this.insertionModeMap.put(Mode.AFTER_FRAMESET, new AfterFramesetInsertionMode(this));
        this.insertionModeMap.put(Mode.AFTER_AFTER_BODY, new AfterAfterBodyInsertionMode(this));
        this.insertionModeMap.put(Mode.AFTER_AFTER_FRAMESET, new AfterAfterFramesetInsertionMode(this));
    }
    
    /**
     * Whether to allow parse errors and use error-handling behavior.
     * 
     * @return whether parse errors will be suppressed
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#parse-error">
     *      parse errors</a>
     */
    public boolean isAllowParseErrors() {
        return allowParseErrors;
    }
    
    /**
     * Returns whether this parser is parsing an HTML fragment as opposed to a
     * full document.
     * 
     * @return {@code true} if this parser is parsing a fragment
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/the-end.html#html-fragment-parsing-algorithm">
     *      HTML fragment parsing algorithm</a>
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/the-end.html#fragment-case">
     *      fragment case</a>
     */
    public boolean isHTMLFragmentParsingAlgorithm() {
        return false;
    }
    
    /**
     * Whether scripting is enabled for this parser.
     * 
     * @return whether scripting is enabled.
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#scripting-flag">
     *      scripting flag</a>
     */
    public boolean isScriptingEnabled() {
        return scriptingEnabled;
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#foster-parent">
     *      foster parenting</a>
     */
    public boolean isFosterParentingEnabled() {
        return fosterParenting;
    }
    
    /**
     * @param enabled
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#foster-parent">
     *      foster parenting</a>
     */
    public void setFosterParentingEnabled(final boolean enabled) {
        this.fosterParenting = enabled;
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#frameset-ok-flag">
     *      frameset-ok flag</a>
     */
    public boolean isFramesetOkFlag() {
        return framesetOK;
    }
    
    /**
     * @param isOK
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#frameset-ok-flag">
     *      frameset-ok flag</a>
     */
    public void setFramesetOKFlag(final boolean isOK) {
        this.framesetOK = isOK;
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/infrastructure.html#quirks-mode">
     *      quirks mode</a>
     */
    public boolean isQuirksMode() {
        return quirksMode;
    }
    
    /**
     * @param quirksMode
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/infrastructure.html#quirks-mode">
     *      quirks mode</a>
     */
    public void setQuirksMode(final boolean quirksMode) {
        this.quirksMode = quirksMode;
    }
    
    /**
     * Returns the current insertion mode.
     * 
     * @return the current insertion mode
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#insertion-mode">
     *      insertion mode</a>
     */
    public Mode getInsertionMode() {
        return insertionMode;
    }
    
    /**
     * Sets the current insertion mode. This is the mode that will be used to
     * parse the next token to be emitted.
     * 
     * @param insertionMode the insertion mode for the next token
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#insertion-mode">
     *      insertion mode</a>
     */
    public void setInsertionMode(final Mode insertionMode) {
        if (insertionMode == null) {
            throw new NullPointerException();
        }
        if (insertionMode == Mode.TEXT || insertionMode == Mode.IN_TABLE_TEXT) {
            this.originalInsertionMode = this.insertionMode;
        }
        this.insertionMode = insertionMode;
    }
    
    public Mode getOriginalInsertionMode() {
        return originalInsertionMode;
    }
    
    public void setOriginalInsertionMode(final Mode originalInsertionMode) {
        if (originalInsertionMode == null) {
            throw new NullPointerException();
        }
        this.originalInsertionMode = originalInsertionMode;
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/the-end.html#stop-parsing">
     *      stop parsing</a>
     */
    public void stopParsing() {
        stop = true;
    }
    
    public Document getDocument() {
        return document;
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#head-element-pointer">
     *      <code>head</code> element pointer</a>
     */
    public Element getHeadElementPointer() {
        return headElementPointer;
    }
    
    /**
     * @param element
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#head-element-pointer">
     *      <code>head</code> element pointer</a>
     */
    public void setHeadElementPointer(final Element element) {
        this.headElementPointer = element;
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#form-element-pointer">
     *      <code>form</code> element pointer</a>
     */
    public Element getFormElementPointer() {
        return formElementPointer;
    }
    
    /**
     * @param formElement
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#form-element-pointer">
     *      <code>form</code> element pointer</a>
     */
    public void setFormElementPointer(final Element formElement) {
        this.formElementPointer = formElement;
    }
    
    /**
     * Returns the number of elements on the stack of open elements.
     * 
     * @return the number of elements on the stack of open elements
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    public int getNumOpenElements() {
        return stackOfOpenElements.size();
    }
    
//	/**
//	 * Returns the stack of open elements.
//	 *
//	 * @return the stack of open elements
//	 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">stack of open elements</a>
//	 */
//	@Deprecated
//	public List<Element> getStackOfOpenElements() {
//		return stackOfOpenElements;
//	}
    
    /**
     * Returns an {@link Iterable} over the stack of open elements. The
     * direction of iteration is not guaranteed.
     * 
     * @return an {@link Iterable} over the stack of open elements
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    public Iterable<Element> getOpenElementsIterable() {
        return stackOfOpenElements;
    }
    
    /**
     * Adds the given element to the stack of open elements. This will be the
     * new current open element.
     * 
     * @param element the element to add to the stack of open elements
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#current-node">
     *      current node</a>
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    public void pushOpenElement(final Element element) {
        if (element == null) {
            throw new NullPointerException();
        }
        stackOfOpenElements.add(element);
    }
    
    /**
     * Removes and returns the current open element from the stack of open
     * elements.
     * 
     * @return the current open element
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#current-node">
     *      current node</a>
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    public Element popOpenElement() {
        return stackOfOpenElements.remove(stackOfOpenElements.size() - 1);
    }
    
    /**
     * Retrieves, but does not remove, the current open element.
     * 
     * @return the current open element
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#current-node">
     *      current node</a>
     */
    public Element getCurrentOpenElement() {
        return getOpenElement(getNumOpenElements() - 1);
    }
    
    /**
     * Retrieves, but does not remove, the open element at the given index.
     * Indices start at {@code 0} for the top of the stack, which is the root
     * element (the {@code html} element).
     * 
     * @param index the index of the open element to return
     * @return the open element at the given index
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    public Element getOpenElement(final int index) {
        return stackOfOpenElements.get(index);
    }
    
    public Element setOpenElement(final int index, final Element element) {
        return stackOfOpenElements.set(index, element);
    }
    
    public void insertOpenElement(final int index, final Element element) {
        stackOfOpenElements.add(index, element);
    }
    
    public Element replaceOpenElement(final Element oldElement, final Element newElement) {
        return stackOfOpenElements.set(stackOfOpenElements.indexOf(oldElement), newElement);
    }
    
    /**
     * Returns whether the given element appears in the stack of open elements.
     * 
     * @param element the element to find
     * @return whether the given element appears in the stack of open elements
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    public boolean containsOpenElement(final Element element) {
        if (element == null) {
            throw new NullPointerException();
        }
        return stackOfOpenElements.contains(element);
    }
    
    public int getIndexOfOpenElement(final Element element) {
        if (element == null) {
            throw new NullPointerException();
        }
        return stackOfOpenElements.indexOf(element);
    }
    
    /**
     * Removes the given element from the stack of open elements.
     * 
     * @param element the element to remove
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#stack-of-open-elements">
     *      stack of open elements</a>
     */
    public void removeOpenElement(final Element element) {
        if (element == null) {
            throw new NullPointerException();
        }
        stackOfOpenElements.remove(element);
    }
    
    /**
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#adjusted-current-node">
     *      adjusted current node</a>
     */
    public Element getAdjustedCurrentNode() {
        if (getNumOpenElements() == 1 && isHTMLFragmentParsingAlgorithm()) {
            throw new UnsupportedOperationException();
        } else {
            return getCurrentOpenElement();
        }
    }
    
    public boolean isActiveFormattingElementsContains(final Element element) {
        for (final FormattingElement formattingElement : listOfActiveFormattingElements) {
            if ( !isMarker(formattingElement)) {
                if (formattingElement.getValue() == element) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static class StartTokenComparable {
        
        private final String tagName;
        
        private final String namespace;
        
        private final Map<String, String> attributes;
        
        public StartTokenComparable(final StartTagToken startTagToken, final Element element) {
            super();
            this.tagName = element.getTagName();
            this.namespace = element.getNamespaceURI();
            this.attributes = new HashMap<>();
            for (final TagToken.Attribute attr : startTagToken.getAttributes()) {
                this.attributes.put(attr.getName(), attr.getValue());
            }
        }
        
        @Override
        public boolean equals(final Object object) {
            if (object instanceof StartTokenComparable) {
                final StartTokenComparable other = (StartTokenComparable) object;
                return tagName.equals(other.tagName) && namespace.equals(other.namespace)
                        && attributes.equals(other.attributes);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return tagName.hashCode() ^ namespace.hashCode() ^ attributes.hashCode();
        }
        
    }
    
    public void addMarkerToListOfActiveFormattingElements() {
        listOfActiveFormattingElements.add(getMarker());
    }
    
    /**
     * @param startTagToken
     * @param element
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#push-onto-the-list-of-active-formatting-elements">
     *      push onto the list of active formatting elements</a>
     */
    public void pushOntoListOfActiveFormattingElements(final StartTagToken startTagToken, final Element element) {
        final StartTokenComparable expected = new StartTokenComparable(startTagToken, element);
        int count = 0;
        final ListIterator<FormattingElement> iter =
                listOfActiveFormattingElements.listIterator(listOfActiveFormattingElements.size());
        while (iter.hasPrevious()) {
            final FormattingElement previous = iter.previous();
            if (isMarker(previous)) {
                iter.next();
                break;
            }
            final StartTokenComparable actual = new StartTokenComparable(previous.getKey(), previous.getValue());
            if (expected.equals(actual)) {
                count++ ;
            }
        }
        if (count >= 3) {
            while (iter.hasNext()) {
                final FormattingElement next = iter.next();
                if (isMarker(next)) {
                    continue;
                }
                final StartTokenComparable actual = new StartTokenComparable(next.getKey(), next.getValue());
                if (expected.equals(actual)) {
                    iter.remove();
                    break;
                }
            }
        }
        listOfActiveFormattingElements.add(new FormattingElement(startTagToken, element));
    }
    
    /**
     * Returns whether this element is a marker in the list of active formatting
     * elements.
     * 
     * @param element the element to check
     * @return {@code true} if the element is a marker
     */
    public boolean isMarker(final FormattingElement element) {
        return element == null;
    }
    
    private FormattingElement getMarker() {
        return null;
    }
    
    /**
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#clear-the-list-of-active-formatting-elements-up-to-the-last-marker">
     *      clear the list of active formatting elements up to the last
     *      marker</a>
     */
    public void clearActiveFormattingElements() {
        FormattingElement popped;
        do {
            popped = listOfActiveFormattingElements.remove(listOfActiveFormattingElements.size() - 1);
        } while ( !isMarker(popped) && !listOfActiveFormattingElements.isEmpty());
    }
    
    public void pushTemplateInsertionMode(final Mode mode) {
        if (mode == null) {
            throw new NullPointerException();
        }
        stackOfTemplateInsertionModes.add(mode);
    }
    
    public Mode popTemplateInsertionMode() {
        return stackOfTemplateInsertionModes.remove(getNumTemplateInsertionModes() - 1);
    }
    
    public Mode getCurrentTemplateInsertionMode() {
        return stackOfTemplateInsertionModes.get(getNumTemplateInsertionModes() - 1);
    }
    
    public int getNumTemplateInsertionModes() {
        return stackOfTemplateInsertionModes.size();
    }
    
    public void setTokenizerState(final Tokenizer.State state) {
        if (state == null) {
            throw new NullPointerException();
        }
        tokenizer.setState(state);
    }
    
    public Token getNextToken() throws IOException {
        return tokenizer.getNextToken();
    }
    
    /**
     * @param node
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#mathml-text-integration-point">
     *      MathML text integration point</a>
     */
    public boolean isMathMLTextIntegrationPoint(final Element node) {
        if (node == null) {
            throw new NullPointerException();
        }
        final String namespace = node.getNamespaceURI();
        final String tagName = node.getTagName();
        if (InsertionMode.MATHML_NAMESPACE.equals(namespace)) {
            if ("mi".equals(tagName)) {
                return true;
            }
            if ("mo".equals(tagName)) {
                return true;
            }
            if ("mn".equals(tagName)) {
                return true;
            }
            if ("ms".equals(tagName)) {
                return true;
            }
            if ("mtext".equals(tagName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param node
     * @return
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#html-integration-point">
     *      HTML integration point</a>
     */
    public boolean isHTMLIntegrationPoint(final Element node) {
        if (node == null) {
            throw new NullPointerException();
        }
        final String namespace = node.getNamespaceURI();
        final String tagName = node.getTagName();
        final String encodingAttribute = node.getAttribute("encoding");
        
        if (InsertionMode.MATHML_NAMESPACE.equals(namespace) && "annotation-xml".equals(tagName)
                && "text/html".equalsIgnoreCase(encodingAttribute)) {
            return true;
        }
        if (InsertionMode.MATHML_NAMESPACE.equals(namespace) && "annotation-xml".equals(tagName)
                && "application/xhtml+xml".equalsIgnoreCase(encodingAttribute)) {
            return true;
        }
        if (InsertionMode.SVG_NAMESPACE.equals(namespace) && "foreignObject".equals(tagName)) {
            return true;
        }
        if (InsertionMode.SVG_NAMESPACE.equals(namespace) && "desc".equals(tagName)) {
            return true;
        }
        if (InsertionMode.SVG_NAMESPACE.equals(namespace) && "title".equals(tagName)) {
            return true;
        }
        return false;
    }
    
    /**
     * @param token
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#tree-construction-dispatcher">
     *      tree construction dispatcher</a>
     */
    protected boolean treeConstructionDispatcher(final Token token) {
        if (token == null) {
            throw new NullPointerException();
        }
        final Element adjustedCurrentNode = getAdjustedCurrentNode();
        
        if (adjustedCurrentNode == null) {
            return insertHTML(token);
        }
        
        final String namespace = adjustedCurrentNode.getNamespaceURI();
        
        if (InsertionMode.HTML_NAMESPACE.equals(namespace)) {
            return insertHTML(token);
        }
        
        if (isMathMLTextIntegrationPoint(adjustedCurrentNode) && token.getType() == Token.Type.START_TAG) {
            final StartTagToken startTagToken = (StartTagToken) token;
            final String tagName = startTagToken.getTagName();
            if ( !"mglyph".equals(tagName) && !"malignmark".equals(tagName)) {
                return insertHTML(token);
            }
        }
        
        if (isMathMLTextIntegrationPoint(adjustedCurrentNode) && token.getType() == Token.Type.CHARACTER) {
            return insertHTML(token);
        }
        
        if (InsertionMode.MATHML_NAMESPACE.equals(namespace)
                && "annotation-xml".equals(adjustedCurrentNode.getTagName())
                && token.getType() == Token.Type.START_TAG) {
            final StartTagToken startTagToken = (StartTagToken) token;
            final String tagName = startTagToken.getTagName();
            if ("svg".equals(tagName)) {
                return insertHTML(token);
            }
        }
        
        if (isHTMLIntegrationPoint(adjustedCurrentNode) && token.getType() == Token.Type.START_TAG) {
            return insertHTML(token);
        }
        
        if (isHTMLIntegrationPoint(adjustedCurrentNode) && token.getType() == Token.Type.CHARACTER) {
            return insertHTML(token);
        }
        
        if (token.getType() == Token.Type.EOF) {
            return insertHTML(token);
        }
        
        return insertForeignContent(token);
    }
    
    private boolean insertHTML(final Token token) {
        if (token == null) {
            throw new NullPointerException();
        }
        final InsertionMode modeHandler = insertionModeMap.get(insertionMode);
        return modeHandler.insert(token);
    }
    
    private boolean insertForeignContent(final Token token) {
        if (token == null) {
            throw new NullPointerException();
        }
        return foreignContentMode.insert(token);
    }
    
    public void acknowledgeSelfClosingFlag() {
        tokenizer.acknowledgeSelfClosingFlag();
    }
    
    public void emitToken() throws IOException {
        final Token token = getNextToken();
        System.out.println(token);
        int count = 0;
        boolean accepted;
        do {
//			accepted = treeConstructionDispatcher(token);
            accepted = insertHTML(token);
            if (count++ > 1024) {
                System.out.println(count + " : " + token);
                throw new ParseErrorException("Too many stack frames emitting token in parser.");
            }
        } while ( !accepted);
//		if (count > 1) {
//			System.out.println(count + " : " + token);
//		} else {
//			System.out.println(token);
//		}
    }
    
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
    public boolean processUsingRulesFor(final Mode mode, final Token token) {
        if (mode == null) {
            throw new NullPointerException();
        }
        if (token == null) {
            throw new NullPointerException();
        }
        return this.insertionModeMap.get(mode).insert(token);
    }
    
    public Document parse() {
        try {
            while ( !stop) {
                emitToken();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return document;
    }
    
    public static void main(final String[] args) throws UnsupportedEncodingException, IOException,
            ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
        final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        final URL url;
//        url = new URL("http://slashdot.org/");
        url = new URL("http://fark.com/");
//        url = new URL("http://w3.org/");
//        url = new URL("http://ejohn.org/");
//        url = new URL("http://rgsb.org/");
        final boolean predownload = true;
        final Reader reader;
        if (predownload) {
            final Download download = new Download(url);
            download.connect();
            download.download();
            final String content = download.getContent();
            reader = new StringReader(content);
            System.out.println(content);
        } else {
            final URLConnection connection = url.openConnection();
            final String contentEncoding = connection.getContentEncoding();
            if (contentEncoding == null) {
                reader = new InputStreamReader(connection.getInputStream());
            } else {
                reader = new InputStreamReader(connection.getInputStream(), contentEncoding);
            }
        }
        final Tokenizer tokenizer = new Tokenizer(reader);
        tokenizer.setAllowParseErrors(true);
        final Parser parser = new Parser(tokenizer, registry);
        
        final DOMImplementation impl = registry.getDOMImplementation("+LS 3.0");
        final DOMImplementationLS lsImpl = (DOMImplementationLS) impl.getFeature("+LS", "3.0");
        final LSSerializer serializer = lsImpl.createLSSerializer();
        serializer.getDomConfig().setParameter("format-pretty-print", true);
        final LSOutput output = lsImpl.createLSOutput();
        output.setByteStream(System.out);
        
        while ( !parser.stop) {
            try {
                parser.emitToken();
            } catch (final RuntimeException e) {
                System.out.println();
                System.out.println(
                        "--------------------------------------------------------------------------------------------------------");
                System.out.println();
//                serializer.write(parser.document, output);
                System.out.println();
                System.out.println(
                        "--------------------------------------------------------------------------------------------------------");
                System.out.println();
                e.printStackTrace();
                return;
            }
        }
        System.out.println();
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");
        System.out.println();
        serializer.write(parser.document, output);
        System.out.println();
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");
        System.out.println();
    }
    
    private List<CharacterToken> pendingTableCharacterTokens;

    public void setPendingTableCharacterTokens() {
        this.pendingTableCharacterTokens = new ArrayList<>();
    }
    
    public List<CharacterToken> getPendingTableCharacterTokens() {
        return pendingTableCharacterTokens;
    }
    
    public void appendToPendingTableCharacterTokens(final CharacterToken characterToken) {
        pendingTableCharacterTokens.add(characterToken);
    }
    
}
