package org.silnith.grammar.xml;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.silnith.grammar.xml.syntax.DocTypeDecl;
import org.silnith.grammar.xml.syntax.Document;
import org.silnith.grammar.xml.syntax.Element;
import org.silnith.grammar.xml.syntax.Misc;
import org.silnith.grammar.xml.syntax.Prolog;
import org.silnith.grammar.xml.syntax.S;
import org.silnith.grammar.xml.syntax.XMLDecl;

/**
 * [1]   	document	   ::=   	{@linkplain PrologProduction prolog} {@linkplain ElementProduction element} {@linkplain MiscProduction Misc}*
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-document">document</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DocumentProduction extends XMLProduction {

	private static List<Misc> getMiscList(final List<Object> list) {
		final List<Misc> miscList = new ArrayList<>();
		for (final Object obj : list) {
			if (obj instanceof List<?>) {
				List<Misc> o = (List<Misc>) obj;
				miscList.addAll(o);
			} else if (obj instanceof S) {
				Misc misc = new Misc();
				misc.s = (S) obj;
				miscList.add(misc);
			}
		}
		return miscList;
	}

	private static class FullDeclHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final Document document = new Document();
			final XMLDecl xmlDecl = (XMLDecl) rightHandSide.get(0);
			final List<Object> docTypeDeclContainer = (List<Object>) rightHandSide.get(1);
			final List<Object> elementContainer = (List<Object>) rightHandSide.get(2);
			final List<Object> trailingMiscList = rightHandSide.subList(3, rightHandSide.size());
			
			final List<Object> firstMiscList = docTypeDeclContainer.subList(0, docTypeDeclContainer.size() - 1);
			final DocTypeDecl docTypeDecl = (DocTypeDecl) docTypeDeclContainer.get(docTypeDeclContainer.size() - 1);
			
			final List<Object> secondMiscList = elementContainer.subList(0, elementContainer.size() - 1);
			final Element docElement = (Element) elementContainer.get(elementContainer.size() - 1);
			
			document.prolog = new Prolog();
			document.prolog.xmlDecl = xmlDecl;
			document.prolog.firstMiscList = getMiscList(firstMiscList);
			document.prolog.doctypedecl = docTypeDecl;
			document.prolog.secondMiscList = getMiscList(secondMiscList);
			document.element = docElement;
			document.miscList = getMiscList(trailingMiscList);
			return document;
		}

	}

	private static class XMLDeclHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final Document document = new Document();
			final XMLDecl xmlDecl = (XMLDecl) rightHandSide.get(0);
			final List<Object> elementContainer = (List<Object>) rightHandSide.get(1);
			final List<Object> trailingMiscList = rightHandSide.subList(2, rightHandSide.size());
			
			final List<Object> secondMiscList = elementContainer.subList(0, elementContainer.size() - 1);
			final Element docElement = (Element) elementContainer.get(elementContainer.size() - 1);
			
			document.prolog = new Prolog();
			document.prolog.xmlDecl = xmlDecl;
			document.prolog.secondMiscList = getMiscList(secondMiscList);
			document.element = docElement;
			document.miscList = getMiscList(trailingMiscList);
			return document;
		}

	}

	private static class DocTypeDeclHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final Document document = new Document();
			final List<Object> docTypeDeclContainer = (List<Object>) rightHandSide.get(0);
			final List<Object> elementContainer = (List<Object>) rightHandSide.get(1);
			final List<Object> trailingMiscList = rightHandSide.subList(2, rightHandSide.size());
			
			final List<Object> firstMiscList = docTypeDeclContainer.subList(0, docTypeDeclContainer.size() - 1);
			final DocTypeDecl docTypeDecl = (DocTypeDecl) docTypeDeclContainer.get(docTypeDeclContainer.size() - 1);
			
			final List<Object> secondMiscList = elementContainer.subList(0, elementContainer.size() - 1);
			final Element docElement = (Element) elementContainer.get(elementContainer.size() - 1);
			
			document.prolog = new Prolog();
			document.prolog.firstMiscList = getMiscList(firstMiscList);
			document.prolog.doctypedecl = docTypeDecl;
			document.prolog.secondMiscList = getMiscList(secondMiscList);
			document.element = docElement;
			document.miscList = getMiscList(trailingMiscList);
			return document;
		}

	}

	private static class ElementHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final Document document = new Document();
			final List<Object> elementContainer = (List<Object>) rightHandSide.get(0);
			final List<Object> trailingMiscList = rightHandSide.subList(1, rightHandSide.size());
			
			final List<Object> secondMiscList = elementContainer.subList(0, elementContainer.size() - 1);
			final Element docElement = (Element) elementContainer.get(elementContainer.size() - 1);
			
			document.prolog = new Prolog();
			document.prolog.secondMiscList = getMiscList(secondMiscList);
			document.element = docElement;
			document.miscList = getMiscList(trailingMiscList);
			return document;
		}

	}

	private static class NewContainerHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final List<Object> list = new LinkedList<>();
			list.addAll(rightHandSide);
			return list;
		}

	}

	private static class AppendContainerHandler implements ProductionHandler {

		@Override
		public Object handleReduction(final List<Object> rightHandSide) {
			final List<Object> list = (List<Object>) rightHandSide.get(rightHandSide.size() - 1);
			list.addAll(0, rightHandSide.subList(0, rightHandSide.size() - 1));
			return list;
		}

	}

	private final NonTerminalSymbol document;

	public DocumentProduction(final Grammar<UnicodeTerminalSymbols> grammar,
			final XMLDeclProduction xmlDeclProduction,
			final MiscProduction miscProduction,
			final SProduction sProduction,
			final DoctypedeclProduction doctypedeclProduction,
			final ElementProduction elementProduction) {
		super(grammar);
		document = this.grammar.getNonTerminalSymbol("document");
		
		final NonTerminalSymbol doctypedecl_container = this.grammar.getNonTerminalSymbol("doctypedecl_container");
		final NonTerminalSymbol element_container = this.grammar.getNonTerminalSymbol("element_container");
		
		final NonTerminalSymbol XMLDecl = xmlDeclProduction.getNonTerminalSymbol();
		final NonTerminalSymbol Misc_Plus = miscProduction.getPlus();
		final NonTerminalSymbol Misc = miscProduction.getNonTerminalSymbol();
		final NonTerminalSymbol S = sProduction.getNonTerminalSymbol();
		final NonTerminalSymbol doctypedecl = doctypedeclProduction.getNonTerminalSymbol();
		final NonTerminalSymbol element = elementProduction.getNonTerminalSymbol();
		
		final FullDeclHandler fullDeclHandler = new FullDeclHandler();
		this.grammar.addProduction(document, fullDeclHandler, XMLDecl, doctypedecl_container, element_container, Misc_Plus, S);
		this.grammar.addProduction(document, fullDeclHandler, XMLDecl, doctypedecl_container, element_container, Misc_Plus);
		this.grammar.addProduction(document, fullDeclHandler, XMLDecl, doctypedecl_container, element_container, S);
		this.grammar.addProduction(document, fullDeclHandler, XMLDecl, doctypedecl_container, element_container);
		final XMLDeclHandler xmlDeclHandler = new XMLDeclHandler();
		this.grammar.addProduction(document, xmlDeclHandler, XMLDecl, element_container, Misc_Plus, S);
		this.grammar.addProduction(document, xmlDeclHandler, XMLDecl, element_container, Misc_Plus);
		this.grammar.addProduction(document, xmlDeclHandler, XMLDecl, element_container, S);
		this.grammar.addProduction(document, xmlDeclHandler, XMLDecl, element_container);
		final DocTypeDeclHandler docTypeDeclHandler = new DocTypeDeclHandler();
		this.grammar.addProduction(document, docTypeDeclHandler, doctypedecl_container, element_container, Misc_Plus, S);
		this.grammar.addProduction(document, docTypeDeclHandler, doctypedecl_container, element_container, Misc_Plus);
		this.grammar.addProduction(document, docTypeDeclHandler, doctypedecl_container, element_container, S);
		this.grammar.addProduction(document, docTypeDeclHandler, doctypedecl_container, element_container);
		final ElementHandler elementHandler = new ElementHandler();
		this.grammar.addProduction(document, elementHandler, element_container, Misc_Plus, S);
		this.grammar.addProduction(document, elementHandler, element_container, Misc_Plus);
		this.grammar.addProduction(document, elementHandler, element_container, S);
		this.grammar.addProduction(document, elementHandler, element_container);
		
		final NewContainerHandler newContainerHandler = new NewContainerHandler();
		final AppendContainerHandler appendContainerHandler = new AppendContainerHandler();
		this.grammar.addProduction(doctypedecl_container, appendContainerHandler, S, Misc, doctypedecl_container);
		this.grammar.addProduction(doctypedecl_container, appendContainerHandler, Misc, doctypedecl_container);
		this.grammar.addProduction(doctypedecl_container, newContainerHandler, S, doctypedecl);
		this.grammar.addProduction(doctypedecl_container, newContainerHandler, doctypedecl);
		
		this.grammar.addProduction(element_container, appendContainerHandler, S, Misc, element_container);
		this.grammar.addProduction(element_container, appendContainerHandler, Misc, element_container);
		this.grammar.addProduction(element_container, newContainerHandler, S, element);
		this.grammar.addProduction(element_container, newContainerHandler, element);
	}

	@Override
	public NonTerminalSymbol getNonTerminalSymbol() {
		return document;
	}

}
