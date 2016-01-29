package org.silnith.grammar.xml.syntax;

public class CDSect {

	public String cData;

	public CDSect() {
	}

	@Override
	public String toString() {
		return "<![CDATA[" + cData + "]]>";
	}

}
