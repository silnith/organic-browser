package org.silnith.grammar.xml.syntax;

public class VersionInfo {
    
    public VersionNum versionNum;
    
    public VersionInfo() {
    }
    
    @Override
    public String toString() {
        return " version=\"" + versionNum + '"';
    }
    
}
