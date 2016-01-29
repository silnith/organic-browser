package org.silnith.grammar.xml.syntax;

public class Comment {

	public String content;

	public Comment() {
	}

	@Override
	public String toString() {
		return "<!--" + content + "-->";
	}

}
