package org.silnith.grammar.xml.syntax;

import java.util.List;

public class Prolog {

	public XMLDecl xmlDecl;

	public List<Misc> firstMiscList;

	public DocTypeDecl doctypedecl;

	public List<Misc> secondMiscList;

	public Prolog() {
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		if (xmlDecl != null) {
			stringBuilder.append(xmlDecl);
			stringBuilder.append('\n');
		}
		if (firstMiscList != null) {
			for (final Misc misc : firstMiscList) {
				stringBuilder.append(misc);
			}
		}
		if (doctypedecl != null) {
			stringBuilder.append(doctypedecl);
			stringBuilder.append('\n');
		}
		if (secondMiscList != null) {
			for (final Misc misc : secondMiscList) {
				stringBuilder.append(misc);
			}
		}
		return stringBuilder.toString();
	}

}
