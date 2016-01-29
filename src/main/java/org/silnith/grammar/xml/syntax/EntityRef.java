package org.silnith.grammar.xml.syntax;

public class EntityRef {

	public String name;

	public EntityRef() {
	}

	@Override
	public String toString() {
		return "&" + name + ';';
	}

}
