package org.silnith.browser.organic.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.silnith.browser.organic.CSSPseudoElementRule;
import org.silnith.browser.organic.CSSRule;
import org.silnith.browser.organic.network.Download;
import org.silnith.css.model.data.PropertyName;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.helpers.ParserFactory;

import com.steadystate.css.parser.CSSOMParser;

public class StyleParser {

	public StyleParser() {
		super();
	}

	public void parseStyleRules(final Download download) throws Exception {
		final String content = download.getContent();
		
		Class.forName(CSSOMParser.class.getName());
		new CSSOMParser();
		
		final ParserFactory factory = new ParserFactory();
		
		final Parser parser = factory.makeParser();
		
		final SelectorList selectorList = parser.parseSelectors(new InputSource(new StringReader(content)));
		
		;
	}

	public Collection<CSSRule> parseStyleRules(final Object documentText) {
		final Collection<CSSRule> defaultHTMLRules = defaultHTMLRules();
		final List<CSSRule> rules = new ArrayList<>(defaultHTMLRules);
		rules.addAll(createStyleList());
		return rules;
	}

	public Collection<CSSPseudoElementRule> parsePseudoElementStyleRules(final Object documentText) {
		return createGeneratedContentRules();
	}

	private static Collection<CSSRule> createStyleList() {
		final Collection<CSSRule> cssRules = new ArrayList<>();
		cssRules.add(new CSSRule("html", PropertyName.COLOR, "black"));
		cssRules.add(new CSSRule("head", PropertyName.FONT_SIZE, "x-small"));
		cssRules.add(new CSSRule("body", PropertyName.FONT_SIZE, "12pt"));
		cssRules.add(new CSSRule("body", PropertyName.BACKGROUND_COLOR, "silver"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_TOP_STYLE, "solid"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_TOP_WIDTH, "15px"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_TOP_COLOR, "blue"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_RIGHT_STYLE, "solid"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_RIGHT_COLOR, "blue"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_BOTTOM_COLOR, "blue"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_LEFT_STYLE, "solid"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_LEFT_WIDTH, "15px"));
		cssRules.add(new CSSRule("body", PropertyName.BORDER_LEFT_COLOR, "blue"));
		cssRules.add(new CSSRule("p", PropertyName.FONT_SIZE, "16pt"));
		cssRules.add(new CSSRule("p", PropertyName.MARGIN_TOP, "3px"));
		cssRules.add(new CSSRule("p", PropertyName.MARGIN_RIGHT, "3px"));
		cssRules.add(new CSSRule("p", PropertyName.MARGIN_BOTTOM, "3px"));
		cssRules.add(new CSSRule("p", PropertyName.MARGIN_LEFT, "3px"));
		cssRules.add(new CSSRule("p", PropertyName.BACKGROUND_COLOR, "white"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_TOP_STYLE, "solid"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_TOP_WIDTH, "15px"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_TOP_COLOR, "lime"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_RIGHT_STYLE, "solid"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_RIGHT_COLOR, "lime"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_BOTTOM_COLOR, "lime"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_LEFT_STYLE, "solid"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_LEFT_WIDTH, "15px"));
		cssRules.add(new CSSRule("p", PropertyName.BORDER_LEFT_COLOR, "lime"));
		cssRules.add(new CSSRule("p", PropertyName.PADDING_TOP, "3px"));
		cssRules.add(new CSSRule("p", PropertyName.PADDING_RIGHT, "3px"));
		cssRules.add(new CSSRule("p", PropertyName.PADDING_BOTTOM, "3px"));
		cssRules.add(new CSSRule("p", PropertyName.PADDING_LEFT, "3px"));
		cssRules.add(new CSSRule("em", PropertyName.FONT_STYLE, "italic"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_TOP_STYLE, "solid"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_TOP_WIDTH, "5px"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_TOP_COLOR, "fuchsia"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_RIGHT_STYLE, "solid"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_RIGHT_COLOR, "fuchsia"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_BOTTOM_COLOR, "fuchsia"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_LEFT_STYLE, "solid"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_LEFT_WIDTH, "5px"));
		cssRules.add(new CSSRule("em", PropertyName.BORDER_LEFT_COLOR, "fuchsia"));
		cssRules.add(new CSSRule("strong", PropertyName.FONT_WEIGHT, "bold"));
		cssRules.add(new CSSRule("strong", PropertyName.BACKGROUND_COLOR, "red"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_TOP_STYLE, "solid"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_TOP_WIDTH, "8px"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_TOP_COLOR, "gray"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_RIGHT_STYLE, "solid"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_RIGHT_WIDTH, "8px"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_RIGHT_COLOR, "gray"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_BOTTOM_WIDTH, "8px"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_BOTTOM_COLOR, "gray"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_LEFT_STYLE, "solid"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_LEFT_WIDTH, "8px"));
		cssRules.add(new CSSRule("strong", PropertyName.BORDER_LEFT_COLOR, "gray"));
		cssRules.add(new CSSRule("div", PropertyName.MARGIN_TOP, "2em"));
		cssRules.add(new CSSRule("div", PropertyName.MARGIN_RIGHT, "2em"));
		cssRules.add(new CSSRule("div", PropertyName.MARGIN_BOTTOM, "2em"));
		cssRules.add(new CSSRule("div", PropertyName.MARGIN_LEFT, "2em"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_TOP_STYLE, "solid"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_TOP_WIDTH, "15px"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_TOP_COLOR, "red"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_RIGHT_STYLE, "solid"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_RIGHT_COLOR, "red"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_BOTTOM_COLOR, "red"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_LEFT_STYLE, "solid"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_LEFT_WIDTH, "15px"));
		cssRules.add(new CSSRule("div", PropertyName.BORDER_LEFT_COLOR, "red"));
		cssRules.add(new CSSRule("div", PropertyName.PADDING_TOP, "6px"));
		cssRules.add(new CSSRule("div", PropertyName.PADDING_RIGHT, "6px"));
		cssRules.add(new CSSRule("div", PropertyName.PADDING_BOTTOM, "6px"));
		cssRules.add(new CSSRule("div", PropertyName.PADDING_LEFT, "6px"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_TOP_STYLE, "solid"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_TOP_WIDTH, "3px"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_TOP_COLOR, "aqua"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_RIGHT_STYLE, "solid"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_RIGHT_WIDTH, "3px"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_RIGHT_COLOR, "aqua"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_BOTTOM_WIDTH, "3px"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_BOTTOM_COLOR, "aqua"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_LEFT_STYLE, "solid"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_LEFT_WIDTH, "3px"));
		cssRules.add(new CSSRule("a", PropertyName.BORDER_LEFT_COLOR, "aqua"));
		cssRules.add(new CSSRule("a", PropertyName.PADDING_TOP, "1px"));
		cssRules.add(new CSSRule("a", PropertyName.PADDING_RIGHT, "1px"));
		cssRules.add(new CSSRule("a", PropertyName.PADDING_BOTTOM, "1px"));
		cssRules.add(new CSSRule("a", PropertyName.PADDING_LEFT, "1px"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_TOP_STYLE, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_TOP_WIDTH, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_TOP_COLOR, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_RIGHT_STYLE, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_RIGHT_WIDTH, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_RIGHT_COLOR, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_BOTTOM_STYLE, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_BOTTOM_WIDTH, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_BOTTOM_COLOR, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_LEFT_STYLE, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_LEFT_WIDTH, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.BORDER_LEFT_COLOR, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.PADDING_TOP, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.PADDING_RIGHT, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.PADDING_BOTTOM, "inherit"));
		cssRules.add(new CSSRule("abbr", PropertyName.PADDING_LEFT, "inherit"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_TOP_STYLE, "solid"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_TOP_WIDTH, "5px"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_TOP_COLOR, "black"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_RIGHT_STYLE, "solid"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_RIGHT_COLOR, "black"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_BOTTOM_COLOR, "black"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_LEFT_STYLE, "solid"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_LEFT_WIDTH, "5px"));
		cssRules.add(new CSSRule("ol", PropertyName.BORDER_LEFT_COLOR, "black"));
		cssRules.add(new CSSRule("ol", PropertyName.PADDING_TOP, "5px"));
		cssRules.add(new CSSRule("ol", PropertyName.PADDING_RIGHT, "5px"));
		cssRules.add(new CSSRule("ol", PropertyName.PADDING_BOTTOM, "5px"));
		cssRules.add(new CSSRule("ol", PropertyName.PADDING_LEFT, "5px"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_TOP_STYLE, "solid"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_TOP_WIDTH, "5px"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_TOP_COLOR, "fuchsia"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_RIGHT_STYLE, "solid"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_RIGHT_COLOR, "fuchsia"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_BOTTOM_COLOR, "fuchsia"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_LEFT_STYLE, "solid"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_LEFT_WIDTH, "5px"));
		cssRules.add(new CSSRule("ul", PropertyName.BORDER_LEFT_COLOR, "fuchsia"));
		cssRules.add(new CSSRule("ul", PropertyName.PADDING_TOP, "5px"));
		cssRules.add(new CSSRule("ul", PropertyName.PADDING_RIGHT, "5px"));
		cssRules.add(new CSSRule("ul", PropertyName.PADDING_BOTTOM, "5px"));
		cssRules.add(new CSSRule("ul", PropertyName.PADDING_LEFT, "5px"));
		cssRules.add(new CSSRule("ul", PropertyName.LIST_STYLE_POSITION, "inside"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_TOP_STYLE, "solid"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_TOP_WIDTH, "5px"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_TOP_COLOR, "orange"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_RIGHT_STYLE, "solid"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_RIGHT_COLOR, "orange"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_BOTTOM_COLOR, "orange"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_LEFT_STYLE, "solid"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_LEFT_WIDTH, "5px"));
		cssRules.add(new CSSRule("li", PropertyName.BORDER_LEFT_COLOR, "orange"));
		cssRules.add(new CSSRule("li", PropertyName.PADDING_TOP, "5px"));
		cssRules.add(new CSSRule("li", PropertyName.PADDING_RIGHT, "5px"));
		cssRules.add(new CSSRule("li", PropertyName.PADDING_BOTTOM, "5px"));
		cssRules.add(new CSSRule("li", PropertyName.PADDING_LEFT, "5px"));
		
		return cssRules;
	}

	private static Collection<CSSPseudoElementRule> createGeneratedContentRules() {
		final ArrayList<CSSPseudoElementRule> pseudoRules = new ArrayList<>();
		final ArrayList<CSSRule> beforeRules = new ArrayList<>();
		final ArrayList<CSSRule> afterRules = new ArrayList<>();
		
		pseudoRules.add(new CSSPseudoElementRule("div", "Div: ", null));
		beforeRules.clear();
		afterRules.clear();
		afterRules.add(new CSSRule(":after", PropertyName.DISPLAY, "none"));
		afterRules.add(new CSSRule(":after", PropertyName.BACKGROUND_COLOR, "aqua"));
		afterRules.add(new CSSRule(":after", PropertyName.BORDER_TOP_STYLE, "solid"));
		afterRules.add(new CSSRule(":after", PropertyName.COLOR, "red"));
		pseudoRules.add(new CSSPseudoElementRule("p", null, "yomama", beforeRules, afterRules));
		
		return pseudoRules;
	}

	private static Collection<CSSRule> defaultHTMLRules() {
		final List<CSSRule> rules = new ArrayList<>();
		
		rules.add(new CSSRule("html", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("address", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("blockquote", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("body", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("dd", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("div", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("dl", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("dt", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("fieldset", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("form", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("frame", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("frameset", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("h1", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("h2", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("h3", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("h4", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("h5", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("h6", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("noframes", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("ol", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("p", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("ul", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("center", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("dir", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("hr", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("menu", PropertyName.DISPLAY, "block"));
		rules.add(new CSSRule("pre", PropertyName.DISPLAY, "block"));
		
		rules.add(new CSSRule("html", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("address", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("blockquote", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("body", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("dd", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("div", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("dl", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("dt", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("fieldset", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("form", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("frame", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("frameset", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("h1", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("h2", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("h3", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("h4", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("h5", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("h6", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("noframes", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("ol", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("p", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("ul", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("center", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("dir", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("hr", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("menu", PropertyName.UNICODE_BIDI, "embed"));
		rules.add(new CSSRule("pre", PropertyName.UNICODE_BIDI, "embed"));
		
		rules.add(new CSSRule("li", PropertyName.DISPLAY, "list-item"));
		rules.add(new CSSRule("head", PropertyName.DISPLAY, "none"));
		rules.add(new CSSRule("table", PropertyName.DISPLAY, "table"));
		rules.add(new CSSRule("tr", PropertyName.DISPLAY, "table-row"));
		rules.add(new CSSRule("thead", PropertyName.DISPLAY, "table-header-group"));
		rules.add(new CSSRule("tbody", PropertyName.DISPLAY, "table-row-group"));
		rules.add(new CSSRule("tfoot", PropertyName.DISPLAY, "table-footer-group"));
		rules.add(new CSSRule("col", PropertyName.DISPLAY, "table-column"));
		rules.add(new CSSRule("colgroup", PropertyName.DISPLAY, "table-column-group"));
		rules.add(new CSSRule("td", PropertyName.DISPLAY, "table-cell"));
		rules.add(new CSSRule("th", PropertyName.DISPLAY, "table-cell"));
		rules.add(new CSSRule("caption", PropertyName.DISPLAY, "table-caption"));
		rules.add(new CSSRule("th", PropertyName.FONT_WEIGHT, "bolder"));
		rules.add(new CSSRule("th", PropertyName.TEXT_ALIGN, "center"));
		rules.add(new CSSRule("caption", PropertyName.TEXT_ALIGN, "center"));
		
		rules.add(new CSSRule("body", PropertyName.MARGIN_TOP, "8px"));
		rules.add(new CSSRule("body", PropertyName.MARGIN_RIGHT, "8px"));
		rules.add(new CSSRule("body", PropertyName.MARGIN_BOTTOM, "8px"));
		rules.add(new CSSRule("body", PropertyName.MARGIN_LEFT, "8px"));
		
		rules.add(new CSSRule("h1", PropertyName.FONT_SIZE, "2em"));
		rules.add(new CSSRule("h1", PropertyName.MARGIN_TOP, ".67em"));
		rules.add(new CSSRule("h1", PropertyName.MARGIN_BOTTOM, ".67em"));
		
		rules.add(new CSSRule("h2", PropertyName.FONT_SIZE, "1.5em"));
		rules.add(new CSSRule("h2", PropertyName.MARGIN_TOP, ".75em"));
		rules.add(new CSSRule("h2", PropertyName.MARGIN_BOTTOM, ".75em"));
		
		rules.add(new CSSRule("h3", PropertyName.FONT_SIZE, "1.17em"));
		rules.add(new CSSRule("h3", PropertyName.MARGIN_TOP, ".83em"));
		rules.add(new CSSRule("h3", PropertyName.MARGIN_BOTTOM, ".83em"));
		
		rules.add(new CSSRule("h4", PropertyName.MARGIN_TOP, "1.12em"));
		rules.add(new CSSRule("h4", PropertyName.MARGIN_BOTTOM, "1.12em"));
		
		rules.add(new CSSRule("p", PropertyName.MARGIN_TOP, "1.12em"));
		rules.add(new CSSRule("p", PropertyName.MARGIN_BOTTOM, "1.12em"));
		rules.add(new CSSRule("blockquote", PropertyName.MARGIN_TOP, "1.12em"));
		rules.add(new CSSRule("blockquote", PropertyName.MARGIN_BOTTOM, "1.12em"));
		rules.add(new CSSRule("ul", PropertyName.MARGIN_TOP, "1.12em"));
		rules.add(new CSSRule("ul", PropertyName.MARGIN_BOTTOM, "1.12em"));
		rules.add(new CSSRule("fieldset", PropertyName.MARGIN_TOP, "1.12em"));
		rules.add(new CSSRule("fieldset", PropertyName.MARGIN_BOTTOM, "1.12em"));
		rules.add(new CSSRule("form", PropertyName.MARGIN_TOP, "1.12em"));
		rules.add(new CSSRule("form", PropertyName.MARGIN_BOTTOM, "1.12em"));
		rules.add(new CSSRule("ol", PropertyName.MARGIN_TOP, "1.12em"));
		rules.add(new CSSRule("ol", PropertyName.MARGIN_BOTTOM, "1.12em"));
		rules.add(new CSSRule("dl", PropertyName.MARGIN_TOP, "1.12em"));
		rules.add(new CSSRule("dl", PropertyName.MARGIN_BOTTOM, "1.12em"));
		rules.add(new CSSRule("dir", PropertyName.MARGIN_TOP, "1.12em"));
		rules.add(new CSSRule("dir", PropertyName.MARGIN_BOTTOM, "1.12em"));
		rules.add(new CSSRule("menu", PropertyName.MARGIN_TOP, "1.12em"));
		rules.add(new CSSRule("menu", PropertyName.MARGIN_BOTTOM, "1.12em"));
		
		rules.add(new CSSRule("h5", PropertyName.FONT_SIZE, ".83em"));
		rules.add(new CSSRule("h5", PropertyName.MARGIN_TOP, "1.5em"));
		rules.add(new CSSRule("h5", PropertyName.MARGIN_BOTTOM, "1.5em"));
		
		rules.add(new CSSRule("h6", PropertyName.FONT_SIZE, ".75em"));
		rules.add(new CSSRule("h6", PropertyName.MARGIN_TOP, "1.67em"));
		rules.add(new CSSRule("h6", PropertyName.MARGIN_BOTTOM, "1.67em"));
		
		rules.add(new CSSRule("h1", PropertyName.FONT_WEIGHT, "bolder"));
		rules.add(new CSSRule("h2", PropertyName.FONT_WEIGHT, "bolder"));
		rules.add(new CSSRule("h3", PropertyName.FONT_WEIGHT, "bolder"));
		rules.add(new CSSRule("h4", PropertyName.FONT_WEIGHT, "bolder"));
		rules.add(new CSSRule("h5", PropertyName.FONT_WEIGHT, "bolder"));
		rules.add(new CSSRule("h6", PropertyName.FONT_WEIGHT, "bolder"));
		rules.add(new CSSRule("b", PropertyName.FONT_WEIGHT, "bolder"));
		rules.add(new CSSRule("strong", PropertyName.FONT_WEIGHT, "bolder"));
		
		rules.add(new CSSRule("blockquote", PropertyName.MARGIN_RIGHT, "40px"));
		rules.add(new CSSRule("blockquote", PropertyName.MARGIN_LEFT, "40px"));
		
		rules.add(new CSSRule("i", PropertyName.FONT_STYLE, "italic"));
		rules.add(new CSSRule("cite", PropertyName.FONT_STYLE, "italic"));
		rules.add(new CSSRule("em", PropertyName.FONT_STYLE, "italic"));
		rules.add(new CSSRule("var", PropertyName.FONT_STYLE, "italic"));
		rules.add(new CSSRule("address", PropertyName.FONT_STYLE, "italic"));
		
		rules.add(new CSSRule("pre", PropertyName.FONT_FAMILY, "monospace"));
		rules.add(new CSSRule("tt", PropertyName.FONT_FAMILY, "monospace"));
		rules.add(new CSSRule("code", PropertyName.FONT_FAMILY, "monospace"));
		rules.add(new CSSRule("kbd", PropertyName.FONT_FAMILY, "monospace"));
		rules.add(new CSSRule("samp", PropertyName.FONT_FAMILY, "monospace"));
		
		rules.add(new CSSRule("pre", PropertyName.WHITE_SPACE, "pre"));
		
		rules.add(new CSSRule("button", PropertyName.DISPLAY, "inline-block"));
		rules.add(new CSSRule("textarea", PropertyName.DISPLAY, "inline-block"));
		rules.add(new CSSRule("input", PropertyName.DISPLAY, "inline-block"));
		rules.add(new CSSRule("select", PropertyName.DISPLAY, "inline-block"));
		
		rules.add(new CSSRule("big", PropertyName.FONT_SIZE, "1.17em"));
		rules.add(new CSSRule("small", PropertyName.FONT_SIZE, ".83em"));
		rules.add(new CSSRule("sub", PropertyName.FONT_SIZE, ".83em"));
		rules.add(new CSSRule("sup", PropertyName.FONT_SIZE, ".83em"));
		rules.add(new CSSRule("sub", PropertyName.VERTICAL_ALIGN, "sub"));
		rules.add(new CSSRule("sup", PropertyName.VERTICAL_ALIGN, "super"));
		
		rules.add(new CSSRule("table", PropertyName.BORDER_SPACING, "2px"));
		rules.add(new CSSRule("thead", PropertyName.VERTICAL_ALIGN, "middle"));
		rules.add(new CSSRule("tbody", PropertyName.VERTICAL_ALIGN, "middle"));
		rules.add(new CSSRule("tfoot", PropertyName.VERTICAL_ALIGN, "middle"));
		rules.add(new CSSRule("td", PropertyName.VERTICAL_ALIGN, "inherit"));
		rules.add(new CSSRule("th", PropertyName.VERTICAL_ALIGN, "inherit"));
		rules.add(new CSSRule("tr", PropertyName.VERTICAL_ALIGN, "inherit"));
		
		rules.add(new CSSRule("s", PropertyName.TEXT_DECORATION, "line-through"));
		rules.add(new CSSRule("strike", PropertyName.TEXT_DECORATION, "line-through"));
		rules.add(new CSSRule("del", PropertyName.TEXT_DECORATION, "line-through"));
		
		rules.add(new CSSRule("hr", PropertyName.BORDER_TOP_WIDTH, "1px"));
		rules.add(new CSSRule("hr", PropertyName.BORDER_RIGHT_WIDTH, "1px"));
		rules.add(new CSSRule("hr", PropertyName.BORDER_BOTTOM_WIDTH, "1px"));
		rules.add(new CSSRule("hr", PropertyName.BORDER_LEFT_WIDTH, "1px"));
		rules.add(new CSSRule("hr", PropertyName.BORDER_TOP_STYLE, "inset"));
		rules.add(new CSSRule("hr", PropertyName.BORDER_RIGHT_STYLE, "inset"));
		rules.add(new CSSRule("hr", PropertyName.BORDER_BOTTOM_STYLE, "inset"));
		rules.add(new CSSRule("hr", PropertyName.BORDER_LEFT_STYLE, "inset"));

//		rules.add(new CSSRule("ol", PropertyName.MARGIN_LEFT, "40px"));
//		rules.add(new CSSRule("ul", PropertyName.MARGIN_LEFT, "40px"));
		rules.add(new CSSRule("dir", PropertyName.MARGIN_LEFT, "40px"));
		rules.add(new CSSRule("menu", PropertyName.MARGIN_LEFT, "40px"));
		rules.add(new CSSRule("dd", PropertyName.MARGIN_LEFT, "40px"));
		
		rules.add(new CSSRule("ol", PropertyName.LIST_STYLE_TYPE, "decimal"));
		
		rules.add(new CSSRule("u", PropertyName.TEXT_DECORATION, "underline"));
		rules.add(new CSSRule("ins", PropertyName.TEXT_DECORATION, "underline"));
		
		rules.add(new CSSRule("br:before", PropertyName.CONTENT, "\\A"));
		rules.add(new CSSRule("br:before", PropertyName.WHITE_SPACE, "pre-line"));
		
		rules.add(new CSSRule("center", PropertyName.TEXT_ALIGN, "center"));
		
		rules.add(new CSSRule(":link", PropertyName.TEXT_DECORATION, "underline"));
		rules.add(new CSSRule(":visited", PropertyName.TEXT_DECORATION, "underline"));
		
		rules.add(new CSSRule(":focus", PropertyName.BORDER_TOP_WIDTH, "thin"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_RIGHT_WIDTH, "thin"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_BOTTOM_WIDTH, "thin"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_LEFT_WIDTH, "thin"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_TOP_STYLE, "dotted"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_RIGHT_STYLE, "dotted"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_BOTTOM_STYLE, "dotted"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_LEFT_STYLE, "dotted"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_TOP_COLOR, "invert"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_RIGHT_COLOR, "invert"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_BOTTOM_COLOR, "invert"));
		rules.add(new CSSRule(":focus", PropertyName.BORDER_LEFT_COLOR, "invert"));
		
		return rules;
	}

}
