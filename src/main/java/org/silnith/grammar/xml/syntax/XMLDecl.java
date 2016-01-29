package org.silnith.grammar.xml.syntax;

public class XMLDecl {

	public VersionInfo versionInfo;

	public EncodingDecl encodingDecl;

	public SDDecl sdDecl;

	public XMLDecl() {
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml");
		if (versionInfo != null) {
			stringBuilder.append(versionInfo);
		}
		if (encodingDecl != null) {
			stringBuilder.append(encodingDecl);
		}
		if (sdDecl != null) {
			stringBuilder.append(sdDecl);
		}
		stringBuilder.append("?>");
		return stringBuilder.toString();
	}

}
