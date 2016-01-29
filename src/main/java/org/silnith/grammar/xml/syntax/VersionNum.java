package org.silnith.grammar.xml.syntax;

public class VersionNum {

	public int major;

	public int minor;

	public VersionNum() {
	}

	@Override
	public String toString() {
		return String.valueOf(major) + '.' + minor;
	}

}
