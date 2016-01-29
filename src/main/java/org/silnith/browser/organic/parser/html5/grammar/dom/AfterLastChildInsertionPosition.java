package org.silnith.browser.organic.parser.html5.grammar.dom;

import org.w3c.dom.Node;

public class AfterLastChildInsertionPosition implements InsertionPosition {

	private final Node parentNode;

	public AfterLastChildInsertionPosition(final Node parentNode) {
		super();
		this.parentNode = parentNode;
	}

	@Override
	public Node getContainingNode() {
		return parentNode;
	}

	@Override
	public Node getNodeImmediatelyBefore() {
		return parentNode.getLastChild();
	}

	@Override
	public void insert(final Node node) {
		parentNode.appendChild(node);
	}

}
