package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.greaterThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.lessThanSign;
import static org.silnith.grammar.UnicodeTerminalSymbols.solidus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.Attribute;
import org.silnith.grammar.xml.syntax.Content;
import org.silnith.grammar.xml.syntax.ETag;
import org.silnith.grammar.xml.syntax.Element;
import org.silnith.grammar.xml.syntax.EmptyElemTag;
import org.silnith.grammar.xml.syntax.STag;

/**
 * [39]   	element	   ::=   	{@linkplain ElementProduction EmptyElemTag}
 * 			| {@linkplain ElementProduction STag} {@linkplain ElementProduction content} {@linkplain ElementProduction ETag} 
 * [40]   	STag	   ::=   	'&lt;' {@linkplain NameProduction Name} ({@linkplain SProduction S} {@linkplain AttributeProduction Attribute})* {@linkplain SProduction S}? '&gt;'
 * [42]   	ETag	   ::=   	'&lt;/' {@linkplain NameProduction Name} {@linkplain SProduction S}? '&gt;'
 * [43]   	content	   ::=   	{@linkplain CharDataProduction CharData}? (({@linkplain ElementProduction element} | {@linkplain ReferenceProduction Reference} | {@linkplain CDSectProduction CDSect} | {@linkplain PIProduction PI} | {@linkplain CommentProduction Comment}) {@linkplain CharDataProduction CharData}?)*
 * [44]   	EmptyElemTag	   ::=   	'&lt;' {@linkplain NameProduction Name} ({@linkplain SProduction S} {@linkplain AttributeProduction Attribute})* {@linkplain SProduction S}? '/&gt;'
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-element">element</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-STag">STag</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-ETag">ETag</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-content">content</a>
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EmptyElemTag">EmptyElemTag</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ElementProduction extends XMLProduction {

	private static class EmptyElementHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final Element element = new Element();
			element.emptyElemTag = (EmptyElemTag) rightHandSide.get(0);
			return element;
		}

	}

	private static class ElementHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final Element element = new Element();
			element.sTag = (STag) rightHandSide.get(0);
			final List<Object> contentAndETag = (List<Object>) rightHandSide.get(1);
			final Content content = new Content();
			element.content = content;
			content.contents = new ArrayList<>(contentAndETag.subList(0, contentAndETag.size() - 1));
			element.eTag = (ETag) contentAndETag.get(contentAndETag.size() - 1);
			return element;
		}

	}

	private static class EmptyElemTagHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final EmptyElemTag emptyElemTag = new EmptyElemTag();
			emptyElemTag.name = (String) rightHandSide.get(1);
			emptyElemTag.attributeList = new ArrayList<>();
			return emptyElemTag;
		}

	}

	private static class EmptyElemTagAttributesHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final EmptyElemTag emptyElemTag = new EmptyElemTag();
			emptyElemTag.name = (String) rightHandSide.get(1);
			emptyElemTag.attributeList = (List<Attribute>) rightHandSide.get(2);
			return emptyElemTag;
		}

	}

	private static class STagHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final STag emptyElemTag = new STag();
			emptyElemTag.name = (String) rightHandSide.get(1);
			emptyElemTag.attributeList = new ArrayList<>();
			return emptyElemTag;
		}

	}

	private static class STagAttributesHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final STag emptyElemTag = new STag();
			emptyElemTag.name = (String) rightHandSide.get(1);
			emptyElemTag.attributeList = (List<Attribute>) rightHandSide.get(2);
			return emptyElemTag;
		}

	}

	private static class NewContentAndETagHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final List<Object> list = new LinkedList<>();
			list.addAll(rightHandSide);
			return list;
		}

	}

	private static class AppendContentAndETagHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final List<Object> list = (List<Object>) rightHandSide.get(2);
			list.add(0, rightHandSide.get(1));
			list.add(0, rightHandSide.get(0));
			return list;
		}

	}

	private static class ContentHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			return rightHandSide.get(0);
		}

	}

	private static class ETagHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final ETag emptyElemTag = new ETag();
			emptyElemTag.name = (String) rightHandSide.get(2);
			return emptyElemTag;
		}

	}

	private final NonTerminalSymbol element;

	private final NonTerminalSymbol content;

	public ElementProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final NameProduction nameProduction, final SProduction sProduction,
			final AttributeProduction attributeProduction,
			final CharDataProduction charDataProduction,
			final ReferenceProduction referenceProduction,
			final CDSectProduction cdSectProduction,
			final PIProduction piProduction,
			final CommentProduction commentProduction) {
		super(grammar);
		element = this.grammar.getNonTerminalSymbol("element");
		content = this.grammar.getNonTerminalSymbol("content");
		
		final NonTerminalSymbol EmptyElemTag = this.grammar.getNonTerminalSymbol("EmptyElemTag");
		final NonTerminalSymbol STag = this.grammar.getNonTerminalSymbol("STag");
		final NonTerminalSymbol content_and_ETag = this.grammar.getNonTerminalSymbol("content-and-ETag");
		final NonTerminalSymbol ETag = this.grammar.getNonTerminalSymbol("ETag");
		
		final NonTerminalSymbol Name = nameProduction.getNonTerminalSymbol();
		final NonTerminalSymbol AttributeList_Plus = attributeProduction.getPlus();
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol CharData = charDataProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Reference = referenceProduction.getNonTerminalSymbol();
		final NonTerminalSymbol CDSect = cdSectProduction.getNonTerminalSymbol();
		final NonTerminalSymbol PI = piProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Comment = commentProduction.getNonTerminalSymbol();
		
		this.grammar.addProduction(element, new EmptyElementHandler(), EmptyElemTag);
		this.grammar.addProduction(element, new ElementHandler(), STag, content_and_ETag);
		
		final EmptyElemTagHandler emptyElemTagHandler = new EmptyElemTagHandler();
		final EmptyElemTagAttributesHandler emptyElemTagAttributesHandler = new EmptyElemTagAttributesHandler();
		this.grammar.addProduction(EmptyElemTag, emptyElemTagAttributesHandler, lessThanSign, Name, AttributeList_Plus, S, solidus, greaterThanSign);
		this.grammar.addProduction(EmptyElemTag, emptyElemTagAttributesHandler, lessThanSign, Name, AttributeList_Plus, solidus, greaterThanSign);
		this.grammar.addProduction(EmptyElemTag, emptyElemTagHandler, lessThanSign, Name, S, solidus, greaterThanSign);
		this.grammar.addProduction(EmptyElemTag, emptyElemTagHandler, lessThanSign, Name, solidus, greaterThanSign);
		
		final STagHandler sTagHandler = new STagHandler();
		final STagAttributesHandler sTagAttributesHandler = new STagAttributesHandler();
		this.grammar.addProduction(STag, sTagAttributesHandler, lessThanSign, Name, AttributeList_Plus, S, greaterThanSign);
		this.grammar.addProduction(STag, sTagAttributesHandler, lessThanSign, Name, AttributeList_Plus, greaterThanSign);
		this.grammar.addProduction(STag, sTagHandler, lessThanSign, Name, S, greaterThanSign);
		this.grammar.addProduction(STag, sTagHandler, lessThanSign, Name, greaterThanSign);
		
		this.grammar.addProduction(content_and_ETag, new NewContentAndETagHandler(), CharData, ETag);
		this.grammar.addProduction(content_and_ETag, new AppendContentAndETagHandler(), CharData, content, content_and_ETag);
		
		final ContentHandler contentHandler = new ContentHandler();
		this.grammar.addProduction(content, contentHandler, element);
		this.grammar.addProduction(content, contentHandler, Reference);
		this.grammar.addProduction(content, contentHandler, CDSect);
		this.grammar.addProduction(content, contentHandler, PI);
		this.grammar.addProduction(content, contentHandler, Comment);
		
		final ETagHandler eTagHandler = new ETagHandler();
		this.grammar.addProduction(ETag, eTagHandler, lessThanSign, solidus, Name, S, greaterThanSign);
		this.grammar.addProduction(ETag, eTagHandler, lessThanSign, solidus, Name, greaterThanSign);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return element;
	}

	public NonTerminalSymbol getContentNonTerminalSymbol() {
		return content;
	}

}
