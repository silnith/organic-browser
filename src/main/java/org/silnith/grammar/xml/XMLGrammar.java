package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.silnith.grammar.GenericSymbol;
import org.silnith.grammar.Grammar;
import org.silnith.grammar.Lexer;
import org.silnith.grammar.NonTerminal;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.Parser;
import org.silnith.grammar.Production;
import org.silnith.grammar.ProductionHandler;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;


/**
 * <ol type="1">
 * <li value="1">{@linkplain #document document} ::= {@linkplain #prolog prolog}
 * {@linkplain #element element} {@linkplain #Misc Misc}*
 * <li value="2">{@linkplain #Char Char} ::= #x9 | #xA | #xD | [#x20-#xD7FF] |
 * [#xE000-#xFFFD] | [#x10000-#x10FFFF]
 * <li value="3">{@linkplain #S S} ::= (#x20 | #x9 | #xD | #xA)+
 * <li value="4">{@linkplain #NameStartChar NameStartChar} ::= ":" | [A-Z] | "_"
 * | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] |
 * [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] |
 * [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
 * <ol type="a">
 * <li value="1">{@linkplain #NameChar NameChar} ::= {@linkplain #NameStartChar
 * NameStartChar} | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
 * </ol>
 * <li value="5">{@linkplain #Name Name} ::= {@linkplain #NameStartChar
 * NameStartChar} ({@linkplain #NameChar NameChar})*
 * <li value="6">{@linkplain #Names Names} ::= {@linkplain #Name Name} (#x20
 * {@linkplain #Name Name})*
 * <li value="7">{@linkplain #Nmtoken Nmtoken} ::= ({@linkplain #NameChar
 * NameChar})+
 * <li value="8">{@linkplain #Nmtokens Nmtokens} ::= {@linkplain #Nmtoken
 * Nmtoken} (#x20 {@linkplain #Nmtoken Nmtoken})*
 * <li value="9">{@linkplain #EntityValue EntityValue} ::= '"' ([^%&amp;"] |
 * {@linkplain #PEReference PEReference} | {@linkplain #Reference Reference})*
 * '"' | "'" ([^%&amp;'] | {@linkplain #PEReference PEReference} |
 * {@linkplain #Reference Reference})* "'"
 * <li value="10">{@linkplain #AttValue AttValue} ::= '"' ([^&lt;&amp;"] |
 * {@linkplain #Reference Reference})* '"' | "'" ([^&lt;&amp;'] |
 * {@linkplain #Reference Reference})* "'"
 * <li value="11">{@linkplain #SystemLiteral SystemLiteral} ::= ('"' [^"]* '
 * "') | ("'" [^']* "'")
 * <li value="12">{@linkplain #PubidLiteral PubidLiteral} ::= '
 * "' {@linkplain #PubidChar PubidChar}* '"' | "'" ({@linkplain #PubidChar
 * PubidChar} - "'")* "'"
 * <li value="13">{@linkplain #PubidChar PubidChar} ::= #x20 | #xD | #xA |
 * [a-zA-Z0-9] | [-'()+,./:=?;!*#@$_%]
 * <li value="14">{@linkplain #CharData CharData} ::= [^&lt;&amp;]* -
 * ([^&lt;&amp;]* ']]&gt;' [^&lt;&amp;]*)
 * <li value="15">{@linkplain #Comment Comment} ::= '&lt;!--' ((
 * {@linkplain #Char Char} - '-') | ('-' ({@linkplain #Char Char} - '-')))*
 * '--&gt;'
 * <li value="16">{@linkplain #PI PI} ::= '&lt;?' {@linkplain #PITarget
 * PITarget} ({@linkplain #S S} ({@linkplain #Char Char}* - ({@linkplain #Char
 * Char}* '?&gt;' {@linkplain #Char Char}*)))? '?&gt;'
 * <li value="17">{@linkplain #PITarget PITarget} ::= {@linkplain #Name Name} -
 * (('X' | 'x') ('M' | 'm') ('L' | 'l'))
 * <li value="18">{@linkplain #CDSect CDSect} ::= {@linkplain #CDStart CDStart}
 * {@linkplain #CData CData} {@linkplain #CDEnd CDEnd}
 * <li value="19">{@linkplain #CDStart CDStart} ::= '&lt;![CDATA['
 * <li value="20">{@linkplain #CData CData} ::= ({@linkplain #Char Char}* - (
 * {@linkplain #Char Char}* ']]&gt;' {@linkplain #Char Char}*))
 * <li value="21">{@linkplain #CDEnd CDEnd} ::= ']]&gt;'
 * <li value="22">{@linkplain #prolog prolog} ::= {@linkplain #XMLDecl XMLDecl}?
 * {@linkplain #Misc Misc}* ({@linkplain #doctypedecl doctypedecl}
 * {@linkplain #Misc Misc}*)?
 * <li value="23">{@linkplain #XMLDecl XMLDecl} ::= '&lt;?xml'
 * {@linkplain #VersionInfo VersionInfo} {@linkplain #EncodingDecl EncodingDecl}
 * ? {@linkplain #SDDecl SDDecl}? {@linkplain #S S}? '?&gt;'
 * <li value="24">{@linkplain #VersionInfo VersionInfo} ::= {@linkplain #S S}
 * 'version' {@linkplain #Eq Eq} ("'" {@linkplain #VersionNum VersionNum} "'" |
 * '"' {@linkplain #VersionNum VersionNum} '"')
 * <li value="25">{@linkplain #Eq Eq} ::= {@linkplain #S S}? '=' {@linkplain #S
 * S}?
 * <li value="26">{@linkplain #VersionNum VersionNum} ::= '1.' [0-9]+
 * <li value="27">{@linkplain #Misc Misc} ::= {@linkplain #Comment Comment} |
 * {@linkplain #PI PI} | {@linkplain #S S}
 * <li value="28">{@linkplain #doctypedecl doctypedecl} ::= '&lt;!DOCTYPE'
 * {@linkplain #S S} {@linkplain #Name Name} ({@linkplain #S S}
 * {@linkplain #ExternalID ExternalID})? {@linkplain #S S}? ('['
 * {@linkplain #intSubset intSubset} ']' {@linkplain #S S}?)? '&gt;'
 * <ol type="a">
 * <li value="1">{@linkplain #DeclSep DeclSep} ::= {@linkplain #PEReference
 * PEReference} | {@linkplain #S S}
 * <li value="2">{@linkplain #intSubset intSubset} ::= ({@linkplain #markupdecl
 * markupdecl} | {@linkplain #DeclSep DeclSep})*
 * </ol>
 * <li value="29">{@linkplain #markupdecl markupdecl} ::=
 * {@linkplain #elementdecl elementdecl} | {@linkplain #AttlistDecl AttlistDecl}
 * | {@linkplain #EntityDecl EntityDecl} | {@linkplain #NotationDecl
 * NotationDecl} | {@linkplain #PI PI} | {@linkplain #Comment Comment}
 * <li value="30">{@linkplain #extSubset extSubset} ::= {@linkplain #TextDecl
 * TextDecl}? {@linkplain #extSubsetDecl extSubsetDecl}
 * <li value="31">{@linkplain #extSubsetDecl extSubsetDecl} ::= (
 * {@linkplain #markupdecl markupdecl} | {@linkplain #conditionalSect
 * conditionalSect} | {@linkplain #DeclSep DeclSep})*
 * <li value="32">{@linkplain #SDDecl SDDecl} ::= {@linkplain #S S} 'standalone'
 * {@linkplain #Eq Eq} (("'" ('yes' | 'no') "'") | ('"' ('yes' | 'no') '"'))
 * <li value="39">{@linkplain #element element} ::= {@linkplain #EmptyElemTag
 * EmptyElemTag} | {@linkplain #STag STag} {@linkplain #content content}
 * {@linkplain #ETag ETag}
 * <li value="40">{@linkplain #STag STag} ::= '&lt;' {@linkplain #Name Name} (
 * {@linkplain #S S} {@linkplain #Attribute Attribute})* {@linkplain #S S}?
 * '&gt;'
 * <li value="41">{@linkplain #Attribute Attribute} ::= {@linkplain #Name Name}
 * {@linkplain #Eq Eq} {@linkplain #AttValue AttValue}
 * <li value="42">{@linkplain #ETag ETag} ::= '&lt;/' {@linkplain #Name Name}
 * {@linkplain #S S}? '&gt;'
 * <li value="43">{@linkplain #content content} ::= {@linkplain #CharData
 * CharData}? (({@linkplain #element element} | {@linkplain #Reference
 * Reference} | {@linkplain #CDSect CDSect} | {@linkplain #PI PI} |
 * {@linkplain #Comment Comment}) {@linkplain #CharData CharData}?)*
 * <li value="44">{@linkplain #EmptyElemTag EmptyElemTag} ::= '&lt;'
 * {@linkplain #Name Name} ({@linkplain #S S} {@linkplain #Attribute Attribute}
 * )* {@linkplain #S S}? '/&gt;'
 * <li value="45">{@linkplain #elementdecl elementdecl} ::= '&lt;!ELEMENT'
 * {@linkplain #S S} {@linkplain #Name Name} {@linkplain #S S}
 * {@linkplain #contentspec contentspec} {@linkplain #S S}? '&gt;'
 * <li value="46">{@linkplain #contentspec contentspec} ::= 'EMPTY' | 'ANY' |
 * {@linkplain #Mixed Mixed} | {@linkplain #children children}
 * <li value="47">{@linkplain #children children} ::= ({@linkplain #choice
 * choice} | {@linkplain #seq seq}) ('?' | '*' | '+')?
 * <li value="48">{@linkplain #cp cp} ::= ({@linkplain #Name Name} |
 * {@linkplain #choice choice} | {@linkplain #seq seq}) ('?' | '*' | '+')?
 * <li value="49">{@linkplain #choice choice} ::= '(' {@linkplain #S S}?
 * {@linkplain #cp cp} ( {@linkplain #S S}? '|' {@linkplain #S S}?
 * {@linkplain #cp cp} )+ {@linkplain #S S}? ')'
 * <li value="50">{@linkplain #seq seq} ::= '(' {@linkplain #S S}?
 * {@linkplain #cp cp} ( {@linkplain #S S}? ',' {@linkplain #S S}?
 * {@linkplain #cp cp} )* {@linkplain #S S}? ')'
 * <li value="51">{@linkplain #Mixed Mixed} ::= '(' {@linkplain #S S}? '#PCDATA'
 * ({@linkplain #S S}? '|' {@linkplain #S S}? {@linkplain #Name Name})*
 * {@linkplain #S S}? ')*' | '(' {@linkplain #S S}? '#PCDATA' {@linkplain #S S}?
 * ')'
 * <li value="52">{@linkplain #AttlistDecl AttlistDecl} ::= '&lt;!ATTLIST'
 * {@linkplain #S S} {@linkplain #Name Name} {@linkplain #AttDef AttDef}*
 * {@linkplain #S S}? '&gt;'
 * <li value="53">{@linkplain #AttDef AttDef} ::= {@linkplain #S S}
 * {@linkplain #Name Name} {@linkplain #S S} {@linkplain #AttType AttType}
 * {@linkplain #S S} {@linkplain #DefaultDecl DefaultDecl}
 * <li value="54">{@linkplain #AttType AttType} ::= {@linkplain #StringType
 * StringType} | {@linkplain #TokenizedType TokenizedType} |
 * {@linkplain #EnumeratedType EnumeratedType}
 * <li value="55">{@linkplain #StringType StringType} ::= 'CDATA'
 * <li value="56">{@linkplain #TokenizedType TokenizedType} ::= 'ID' | 'IDREF' |
 * 'IDREFS' | 'ENTITY' | 'ENTITIES' | 'NMTOKEN' | 'NMTOKENS'
 * <li value="57">{@linkplain #EnumeratedType EnumeratedType} ::=
 * {@linkplain #NotationType NotationType} | {@linkplain #Enumeration
 * Enumeration}
 * <li value="58">{@linkplain #NotationType NotationType} ::= 'NOTATION'
 * {@linkplain #S S} '(' {@linkplain #S S}? {@linkplain #Name Name} (
 * {@linkplain #S S}? '|' {@linkplain #S S}? {@linkplain #Name Name})*
 * {@linkplain #S S}? ')'
 * <li value="59">{@linkplain #Enumeration Enumeration} ::= '(' {@linkplain #S
 * S}? {@linkplain #Nmtoken Nmtoken} ({@linkplain #S S}? '|' {@linkplain #S S}?
 * {@linkplain #Nmtoken Nmtoken})* {@linkplain #S S}? ')'
 * <li value="60">{@linkplain #DefaultDecl DefaultDecl} ::= '#REQUIRED' |
 * '#IMPLIED' | (('#FIXED' {@linkplain #S S})? {@linkplain #AttValue AttValue})
 * <li value="61">{@linkplain #conditionalSect conditionalSect} ::=
 * {@linkplain #includeSect includeSect} | {@linkplain #ignoreSect ignoreSect}
 * <li value="62">{@linkplain #includeSect includeSect} ::= '&lt;!['
 * {@linkplain #S S}? 'INCLUDE' {@linkplain #S S}? '['
 * {@linkplain #extSubsetDecl extSubsetDecl} ']]&gt;'
 * <li value="63">{@linkplain #ignoreSect ignoreSect} ::= '&lt;!['
 * {@linkplain #S S}? 'IGNORE' {@linkplain #S S}? '['
 * {@linkplain #ignoreSectContents ignoreSectContents}* ']]&gt;'
 * <li value="64">{@linkplain #ignoreSectContents ignoreSectContents} ::=
 * {@linkplain #Ignore Ignore} ('&lt;![' {@linkplain #ignoreSectContents
 * ignoreSectContents} ']]&gt;' {@linkplain #Ignore Ignore})*
 * <li value="65">{@linkplain #Ignore Ignore} ::= {@linkplain #Char Char}* - (
 * {@linkplain #Char Char}* ('&lt;![' | ']]&gt;') {@linkplain #Char Char}*)
 * <li value="66">{@linkplain #CharRef CharRef} ::= '&amp;#' [0-9]+ ';' |
 * '&amp;#x' [0-9a-fA-F]+ ';'
 * <li value="67">{@linkplain #Reference Reference} ::= {@linkplain #EntityRef
 * EntityRef} | {@linkplain #CharRef CharRef}
 * <li value="68">{@linkplain #EntityRef EntityRef} ::= '&amp;'
 * {@linkplain #Name Name} ';'
 * <li value="69">{@linkplain #PEReference PEReference} ::= '%'
 * {@linkplain #Name Name} ';'
 * <li value="70">{@linkplain #EntityDecl EntityDecl} ::= {@linkplain #GEDecl
 * GEDecl} | {@linkplain #PEDecl PEDecl}
 * <li value="71">{@linkplain #GEDecl GEDecl} ::= '&lt;!ENTITY' {@linkplain #S
 * S} {@linkplain #Name Name} {@linkplain #S S} {@linkplain #EntityDef
 * EntityDef} {@linkplain #S S}? '&gt;'
 * <li value="72">{@linkplain #PEDecl PEDecl} ::= '&lt;!ENTITY' {@linkplain #S
 * S} '%' {@linkplain #S S} {@linkplain #Name Name} {@linkplain #S S}
 * {@linkplain #PEDecl PEDecl} {@linkplain #S S}? '&gt;'
 * <li value="73">{@linkplain #EntityDef EntityDef} ::= {@linkplain #EntityValue
 * EntityValue} | ({@linkplain #ExternalID ExternalID} {@linkplain #NDataDecl
 * NDataDecl}?)
 * <li value="74">{@linkplain #PEDef PEDef} ::= {@linkplain #EntityValue
 * EntityValue} | {@linkplain #ExternalID ExternalID}
 * <li value="75">{@linkplain #ExternalID ExternalID} ::= 'SYSTEM'
 * {@linkplain #S S} {@linkplain #SystemLiteral SystemLiteral} | 'PUBLIC'
 * {@linkplain #S S} {@linkplain #PubidLiteral PubidLiteral} {@linkplain #S S}
 * {@linkplain #SystemLiteral SystemLiteral}
 * <li value="76">{@linkplain #NDataDecl NDataDecl} ::= {@linkplain #S S}
 * 'NDATA' {@linkplain #S S} {@linkplain #Name Name}
 * <li value="77">{@linkplain #TextDecl TextDecl} ::= '&lt;?xml'
 * {@linkplain #VersionInfo VersionInfo}? {@linkplain #EncodingDecl
 * EncodingDecl} {@linkplain #S S}? '?&gt;'
 * <li value="78">{@linkplain #extParsedEnt extParsedEnt} ::=
 * {@linkplain #TextDecl TextDecl}? {@linkplain #content content}
 * <li value="80">{@linkplain #EncodingDecl EncodingDecl} ::= {@linkplain #S S}
 * 'encoding' {@linkplain #Eq Eq} ('"' {@linkplain #EncName EncName} '"' | "'"
 * {@linkplain #EncName EncName} "'" )
 * <li value="81">{@linkplain #EncName EncName} ::= [A-Za-z] ([A-Za-z0-9._] |
 * '-')*
 * <li value="82">{@linkplain #NotationDecl NotationDecl} ::= '&lt;!NOTATION'
 * {@linkplain #S S} {@linkplain #Name Name} {@linkplain #S S} (
 * {@linkplain #ExternalID ExternalID} | {@linkplain #PublicID PublicID})
 * {@linkplain #S S}? '&gt;'
 * <li value="83">{@linkplain #PublicID PublicID} ::= 'PUBLIC' {@linkplain #S S}
 * {@linkplain #PubidLiteral PubidLiteral}
 * <li value="84">{@linkplain #Letter Letter} ::= {@linkplain #BaseChar
 * BaseChar} | {@linkplain #Ideographic Ideographic}
 * <li value="85">{@linkplain #BaseChar BaseChar} ::= [#x0041-#x005A] |
 * [#x0061-#x007A] | [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x00FF] |
 * [#x0100-#x0131] | [#x0134-#x013E] | [#x0141-#x0148] | [#x014A-#x017E] |
 * [#x0180-#x01C3] | [#x01CD-#x01F0] | [#x01F4-#x01F5] | [#x01FA-#x0217] |
 * [#x0250-#x02A8] | [#x02BB-#x02C1] | #x0386 | [#x0388-#x038A] | #x038C |
 * [#x038E-#x03A1] | [#x03A3-#x03CE] | [#x03D0-#x03D6] | #x03DA | #x03DC |
 * #x03DE | #x03E0 | [#x03E2-#x03F3] | [#x0401-#x040C] | [#x040E-#x044F] |
 * [#x0451-#x045C] | [#x045E-#x0481] | [#x0490-#x04C4] | [#x04C7-#x04C8] |
 * [#x04CB-#x04CC] | [#x04D0-#x04EB] | [#x04EE-#x04F5] | [#x04F8-#x04F9] |
 * [#x0531-#x0556] | #x0559 | [#x0561-#x0586] | [#x05D0-#x05EA] |
 * [#x05F0-#x05F2] | [#x0621-#x063A] | [#x0641-#x064A] | [#x0671-#x06B7] |
 * [#x06BA-#x06BE] | [#x06C0-#x06CE] | [#x06D0-#x06D3] | #x06D5 |
 * [#x06E5-#x06E6] | [#x0905-#x0939] | #x093D | [#x0958-#x0961] |
 * [#x0985-#x098C] | [#x098F-#x0990] | [#x0993-#x09A8] | [#x09AA-#x09B0] |
 * #x09B2 | [#x09B6-#x09B9] | [#x09DC-#x09DD] | [#x09DF-#x09E1] |
 * [#x09F0-#x09F1] | [#x0A05-#x0A0A] | [#x0A0F-#x0A10] | [#x0A13-#x0A28] |
 * [#x0A2A-#x0A30] | [#x0A32-#x0A33] | [#x0A35-#x0A36] | [#x0A38-#x0A39] |
 * [#x0A59-#x0A5C] | #x0A5E | [#x0A72-#x0A74] | [#x0A85-#x0A8B] | #x0A8D |
 * [#x0A8F-#x0A91] | [#x0A93-#x0AA8] | [#x0AAA-#x0AB0] | [#x0AB2-#x0AB3] |
 * [#x0AB5-#x0AB9] | #x0ABD | #x0AE0 | [#x0B05-#x0B0C] | [#x0B0F-#x0B10] |
 * [#x0B13-#x0B28] | [#x0B2A-#x0B30] | [#x0B32-#x0B33] | [#x0B36-#x0B39] |
 * #x0B3D | [#x0B5C-#x0B5D] | [#x0B5F-#x0B61] | [#x0B85-#x0B8A] |
 * [#x0B8E-#x0B90] | [#x0B92-#x0B95] | [#x0B99-#x0B9A] | #x0B9C |
 * [#x0B9E-#x0B9F] | [#x0BA3-#x0BA4] | [#x0BA8-#x0BAA] | [#x0BAE-#x0BB5] |
 * [#x0BB7-#x0BB9] | [#x0C05-#x0C0C] | [#x0C0E-#x0C10] | [#x0C12-#x0C28] |
 * [#x0C2A-#x0C33] | [#x0C35-#x0C39] | [#x0C60-#x0C61] | [#x0C85-#x0C8C] |
 * [#x0C8E-#x0C90] | [#x0C92-#x0CA8] | [#x0CAA-#x0CB3] | [#x0CB5-#x0CB9] |
 * #x0CDE | [#x0CE0-#x0CE1] | [#x0D05-#x0D0C] | [#x0D0E-#x0D10] |
 * [#x0D12-#x0D28] | [#x0D2A-#x0D39] | [#x0D60-#x0D61] | [#x0E01-#x0E2E] |
 * #x0E30 | [#x0E32-#x0E33] | [#x0E40-#x0E45] | [#x0E81-#x0E82] | #x0E84 |
 * [#x0E87-#x0E88] | #x0E8A | #x0E8D | [#x0E94-#x0E97] | [#x0E99-#x0E9F] |
 * [#x0EA1-#x0EA3] | #x0EA5 | #x0EA7 | [#x0EAA-#x0EAB] | [#x0EAD-#x0EAE] |
 * #x0EB0 | [#x0EB2-#x0EB3] | #x0EBD | [#x0EC0-#x0EC4] | [#x0F40-#x0F47] |
 * [#x0F49-#x0F69] | [#x10A0-#x10C5] | [#x10D0-#x10F6] | #x1100 |
 * [#x1102-#x1103] | [#x1105-#x1107] | #x1109 | [#x110B-#x110C] |
 * [#x110E-#x1112] | #x113C | #x113E | #x1140 | #x114C | #x114E | #x1150 |
 * [#x1154-#x1155] | #x1159 | [#x115F-#x1161] | #x1163 | #x1165 | #x1167 |
 * #x1169 | [#x116D-#x116E] | [#x1172-#x1173] | #x1175 | #x119E | #x11A8 |
 * #x11AB | [#x11AE-#x11AF] | [#x11B7-#x11B8] | #x11BA | [#x11BC-#x11C2] |
 * #x11EB | #x11F0 | #x11F9 | [#x1E00-#x1E9B] | [#x1EA0-#x1EF9] |
 * [#x1F00-#x1F15] | [#x1F18-#x1F1D] | [#x1F20-#x1F45] | [#x1F48-#x1F4D] |
 * [#x1F50-#x1F57] | #x1F59 | #x1F5B | #x1F5D | [#x1F5F-#x1F7D] |
 * [#x1F80-#x1FB4] | [#x1FB6-#x1FBC] | #x1FBE | [#x1FC2-#x1FC4] |
 * [#x1FC6-#x1FCC] | [#x1FD0-#x1FD3] | [#x1FD6-#x1FDB] | [#x1FE0-#x1FEC] |
 * [#x1FF2-#x1FF4] | [#x1FF6-#x1FFC] | #x2126 | [#x212A-#x212B] | #x212E |
 * [#x2180-#x2182] | [#x3041-#x3094] | [#x30A1-#x30FA] | [#x3105-#x312C] |
 * [#xAC00-#xD7A3]
 * <li value="86">{@linkplain #Ideographic Ideographic} ::= [#x4E00-#x9FA5] |
 * #x3007 | [#x3021-#x3029]
 * <li value="87">{@linkplain #CombiningChar CombiningChar} ::= [#x0300-#x0345]
 * | [#x0360-#x0361] | [#x0483-#x0486] | [#x0591-#x05A1] | [#x05A3-#x05B9] |
 * [#x05BB-#x05BD] | #x05BF | [#x05C1-#x05C2] | #x05C4 | [#x064B-#x0652] |
 * #x0670 | [#x06D6-#x06DC] | [#x06DD-#x06DF] | [#x06E0-#x06E4] |
 * [#x06E7-#x06E8] | [#x06EA-#x06ED] | [#x0901-#x0903] | #x093C |
 * [#x093E-#x094C] | #x094D | [#x0951-#x0954] | [#x0962-#x0963] |
 * [#x0981-#x0983] | #x09BC | #x09BE | #x09BF | [#x09C0-#x09C4] |
 * [#x09C7-#x09C8] | [#x09CB-#x09CD] | #x09D7 | [#x09E2-#x09E3] | #x0A02 |
 * #x0A3C | #x0A3E | #x0A3F | [#x0A40-#x0A42] | [#x0A47-#x0A48] |
 * [#x0A4B-#x0A4D] | [#x0A70-#x0A71] | [#x0A81-#x0A83] | #x0ABC |
 * [#x0ABE-#x0AC5] | [#x0AC7-#x0AC9] | [#x0ACB-#x0ACD] | [#x0B01-#x0B03] |
 * #x0B3C | [#x0B3E-#x0B43] | [#x0B47-#x0B48] | [#x0B4B-#x0B4D] |
 * [#x0B56-#x0B57] | [#x0B82-#x0B83] | [#x0BBE-#x0BC2] | [#x0BC6-#x0BC8] |
 * [#x0BCA-#x0BCD] | #x0BD7 | [#x0C01-#x0C03] | [#x0C3E-#x0C44] |
 * [#x0C46-#x0C48] | [#x0C4A-#x0C4D] | [#x0C55-#x0C56] | [#x0C82-#x0C83] |
 * [#x0CBE-#x0CC4] | [#x0CC6-#x0CC8] | [#x0CCA-#x0CCD] | [#x0CD5-#x0CD6] |
 * [#x0D02-#x0D03] | [#x0D3E-#x0D43] | [#x0D46-#x0D48] | [#x0D4A-#x0D4D] |
 * #x0D57 | #x0E31 | [#x0E34-#x0E3A] | [#x0E47-#x0E4E] | #x0EB1 |
 * [#x0EB4-#x0EB9] | [#x0EBB-#x0EBC] | [#x0EC8-#x0ECD] | [#x0F18-#x0F19] |
 * #x0F35 | #x0F37 | #x0F39 | #x0F3E | #x0F3F | [#x0F71-#x0F84] |
 * [#x0F86-#x0F8B] | [#x0F90-#x0F95] | #x0F97 | [#x0F99-#x0FAD] |
 * [#x0FB1-#x0FB7] | #x0FB9 | [#x20D0-#x20DC] | #x20E1 | [#x302A-#x302F] |
 * #x3099 | #x309A
 * <li value="88">{@linkplain #Digit Digit} ::= [#x0030-#x0039] |
 * [#x0660-#x0669] | [#x06F0-#x06F9] | [#x0966-#x096F] | [#x09E6-#x09EF] |
 * [#x0A66-#x0A6F] | [#x0AE6-#x0AEF] | [#x0B66-#x0B6F] | [#x0BE7-#x0BEF] |
 * [#x0C66-#x0C6F] | [#x0CE6-#x0CEF] | [#x0D66-#x0D6F] | [#x0E50-#x0E59] |
 * [#x0ED0-#x0ED9] | [#x0F20-#x0F29]
 * <li value="89">{@linkplain #Extender Extender} ::= #x00B7 | #x02D0 | #x02D1 |
 * #x0387 | #x0640 | #x0E46 | #x0EC6 | #x3005 | [#x3031-#x3035] |
 * [#x309D-#x309E] | [#x30FC-#x30FE]
 * </ol>
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/">XML 1.0</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class XMLGrammar extends Grammar<UnicodeTerminalSymbols> {
    
    /*
     * All the productions that are reachable from the originating production
     * "document".
     */
    /**
     * &#x03B5; ::= &empty;
     */
    public static final NonTerminal epsilon = createNonTerminalSymbol("\u03B5");
    
    public static final NonTerminal START = createNonTerminalSymbol("START");
    
    /**
     * [1] document ::= {@linkplain #prolog prolog} {@linkplain #element
     * element} {@linkplain #Misc Misc}*
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-document">
     *      document</a>
     */
    public static final NonTerminal document = createNonTerminalSymbol("document");
    
    /**
     * [22] prolog ::= {@linkplain #XMLDecl XMLDecl}? {@linkplain #Misc Misc}* (
     * {@linkplain #doctypedecl doctypedecl} {@linkplain #Misc Misc}*)?
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-prolog">
     *      prolog</a>
     */
//	public static final NonTerminal prolog = createNonTerminalSymbol("prolog");
    /**
     * [39] element ::= {@linkplain #EmptyElemTag EmptyElemTag} |
     * {@linkplain #STag STag} {@linkplain #content content} {@linkplain #ETag
     * ETag}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-element">
     *      element</a>
     */
    public static final NonTerminal element = createNonTerminalSymbol("element");
    
    public static final NonTerminal element_container = createNonTerminalSymbol("element-container");
    
    /**
     * [23] XMLDecl ::= '&lt;?xml' {@linkplain #VersionInfo VersionInfo}
     * {@linkplain #EncodingDecl EncodingDecl}? {@linkplain #SDDecl SDDecl}?
     * {@linkplain #S S}? '?&gt;'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-XMLDecl">
     *      XMLDecl</a>
     */
    public static final NonTerminal XMLDecl = createNonTerminalSymbol("XMLDecl");
    
    /**
     * [28] doctypedecl ::= '&lt;!DOCTYPE' {@linkplain #S S} {@linkplain #Name
     * Name} ({@linkplain #S S} {@linkplain #ExternalID ExternalID})?
     * {@linkplain #S S}? ('[' {@linkplain #intSubset intSubset} ']'
     * {@linkplain #S S}?)? '&gt;'
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-doctypedecl">
     *      doctypedecl</a>
     */
    public static final NonTerminal doctypedecl = createNonTerminalSymbol("doctypedecl");
    
    public static final NonTerminal doctypedecl_container = createNonTerminalSymbol("doctypedecl-container");
    
    /**
     * [24] VersionInfo ::= {@linkplain #S S} 'version' {@linkplain #Eq Eq} ("'"
     * {@linkplain #VersionNum VersionNum} "'" | '
     * "' {@linkplain #VersionNum VersionNum} '"')
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-VersionInfo">
     *      VersionInfo</a>
     */
    public static final NonTerminal VersionInfo = createNonTerminalSymbol("VersionInfo");
    
    /**
     * [80] EncodingDecl ::= {@linkplain #S S} 'encoding' {@linkplain #Eq Eq} ('
     * "' {@linkplain #EncName EncName} '"' | "'" {@linkplain #EncName EncName}
     * "'" )
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EncodingDecl">
     *      EncodingDecl</a>
     */
    public static final NonTerminal EncodingDecl = createNonTerminalSymbol("EncodingDecl");
    
    /**
     * [32] SDDecl ::= {@linkplain #S S} 'standalone' {@linkplain #Eq Eq} (("'"
     * ('yes' | 'no') "'") | ('"' ('yes' | 'no') '"'))
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-SDDecl">
     *      SDDecl</a>
     */
    public static final NonTerminal SDDecl = createNonTerminalSymbol("SDDecl");
    
    /**
     * [3] S ::= (#x20 | #x9 | #xD | #xA)+
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-S">S</a>
     */
    public static final NonTerminal S = createNonTerminalSymbol("S");
    
    /**
     * [25] Eq ::= {@linkplain #S S}? '=' {@linkplain #S S}?
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Eq">Eq</a>
     */
    public static final NonTerminal Eq = createNonTerminalSymbol("Eq");
    
    /**
     * [26] VersionNum ::= '1.' [0-9]+
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-VersionNum">
     *      VersionNum</a>
     */
    public static final NonTerminal VersionNum = createNonTerminalSymbol("VersionNum");
    
    /**
     * WHITE_SPACE ::= #x20 | #x9 | #xD | #xA
     */
    public static final NonTerminal WHITE_SPACE = createNonTerminalSymbol("WHITE-SPACE");
    
    /**
     * DECIMAL_DIGIT ::= [0-9]
     */
    public static final NonTerminal DECIMAL_DIGIT = createNonTerminalSymbol("DECIMAL-DIGIT");
    
    public static final NonTerminal DECIMAL_DIGIT_Plus = createNonTerminalSymbol("DECIMAL-DIGIT-Plus");
    
    /**
     * [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EncName">
     *      EncName</a>
     */
    public static final NonTerminal EncName = createNonTerminalSymbol("EncName");
    
    /**
     * EncName_StartChar ::= [A-Za-z]
     */
    public static final NonTerminal EncName_StartChar = createNonTerminalSymbol("EncName-StartChar");
    
    /**
     * EncName_Char ::= [A-Za-z0-9._-]
     */
    public static final NonTerminal EncName_Char = createNonTerminalSymbol("EncName-Char");
    
    public static final NonTerminal EncName_Char_Plus = createNonTerminalSymbol("EncName-Char-Plus");
    
    /**
     * [27] Misc ::= {@linkplain #Comment Comment} | {@linkplain #PI PI} |
     * {@linkplain #S S}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Misc">Misc
     *      </a>
     */
    public static final NonTerminal Misc = createNonTerminalSymbol("Misc");
    
    public static final NonTerminal Misc_Plus = createNonTerminalSymbol("Misc-Plus");
    
    /**
     * [15] Comment ::= '&lt;!--' (({@linkplain #Char Char} - '-') | ('-' (
     * {@linkplain #Char Char} - '-')))* '--&gt;'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Comment">
     *      Comment</a>
     */
    public static final NonTerminal Comment = createNonTerminalSymbol("Comment");
    
    public static final NonTerminal Comment_span_Plus = createNonTerminalSymbol("Comment-span-Plus");
    
    /**
     * Comment_Char ::= (Char - '-') | ('-' (Char - '-'))
     */
    public static final NonTerminal Comment_Char = createNonTerminalSymbol("Comment-Char");
    
    public static final NonTerminal Comment_Char_Plus = createNonTerminalSymbol("Comment-Char-Plus");
    
    /**
     * [16] PI ::= '&lt;?' {@linkplain #PITarget PITarget} ({@linkplain #S S} (
     * {@linkplain #Char Char}* - ({@linkplain #Char Char}* '?&gt;'
     * {@linkplain #Char Char}*)))? '?&gt;'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PI">PI</a>
     */
    public static final NonTerminal PI = createNonTerminalSymbol("PI");
    
    public static final NonTerminal PI_inner = createNonTerminalSymbol("PI-inner");
    
    /**
     * PI_Char ::= [^?>#x9#xA#xD#x20]
     */
    public static final NonTerminal PI_Char = createNonTerminalSymbol("PI-Char");
    
    public static final NonTerminal PI_Char_Plus = createNonTerminalSymbol("PI-Char-Plus");
    
    /**
     * [2] Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] |
     * [#x10000-#x10FFFF]
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Char">Char
     *      </a>
     */
    public static final NonTerminal Char = createNonTerminalSymbol("Char");
    
    /**
     * [17] PITarget ::= {@linkplain #Name Name} - (('X' | 'x') ('M' | 'm') ('L'
     * | 'l'))
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PITarget">
     *      PITarget</a>
     */
    public static final NonTerminal PITarget = createNonTerminalSymbol("PITarget");
    
    /**
     * [5] Name ::= {@linkplain #NameStartChar NameStartChar} (
     * {@linkplain #NameChar NameChar})*
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Name">Name
     *      </a>
     */
    public static final NonTerminal Name = createNonTerminalSymbol("Name");
    
    /**
     * [4] NameStartChar ::= ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] |
     * [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] |
     * [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] |
     * [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-NameStartChar">
     *      NameStartChar</a>
     */
    public static final NonTerminal NameStartChar = createNonTerminalSymbol("NameStartChar");
    
    public static final NonTerminal NonXNameStartChar = createNonTerminalSymbol("non-X-NameStartChar");
    
    /**
     * [4a] NameChar ::= {@linkplain #NameStartChar NameStartChar} | "-" | "." |
     * [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-NameChar">
     *      NameChar</a>
     */
    public static final NonTerminal NameChar = createNonTerminalSymbol("NameChar");
    
    public static final NonTerminal NonMNameChar = createNonTerminalSymbol("non-M-NameChar");
    
    public static final NonTerminal NonLNameChar = createNonTerminalSymbol("non-L-NameChar");
    
    public static final NonTerminal NameChar_Plus = createNonTerminalSymbol("NameChar-Plus");
    
    /**
     * [75] ExternalID ::= 'SYSTEM' {@linkplain #S S} {@linkplain #SystemLiteral
     * SystemLiteral} | 'PUBLIC' {@linkplain #S S} {@linkplain #PubidLiteral
     * PubidLiteral} {@linkplain #S S} {@linkplain #SystemLiteral SystemLiteral}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-ExternalID">
     *      ExternalID</a>
     */
    public static final NonTerminal ExternalID = createNonTerminalSymbol("ExternalID");
    
    /**
     * [28b] intSubset ::= ({@linkplain #markupdecl markupdecl} |
     * {@linkplain #DeclSep DeclSep})*
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-intSubset">
     *      intSubset</a>
     */
    public static final NonTerminal intSubset = createNonTerminalSymbol("intSubset");
    
    public static final NonTerminal intSubset_inner = createNonTerminalSymbol("intSubset-inner");
    
    public static final NonTerminal intSubset_inner_Plus = createNonTerminalSymbol("intSubset-inner-Plus");
    
    /**
     * [12] PubidLiteral ::= '"' {@linkplain #PubidChar PubidChar}* '"' | "'" (
     * {@linkplain #PubidChar PubidChar} - "'")* "'"
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PubidLiteral">
     *      PubidLiteral</a>
     */
    public static final NonTerminal PubidLiteral = createNonTerminalSymbol("PubidLiteral");
    
    /**
     * [11] SystemLiteral ::= ('"' [^"]* '"') | ("'" [^']* "'")
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-SystemLiteral">
     *      SystemLiteral</a>
     */
    public static final NonTerminal SystemLiteral = createNonTerminalSymbol("SystemLiteral");
    
    public static final NonTerminal SystemLiteral_inner_Plus = createNonTerminalSymbol("SystemLiteral-inner-Plus");
    
    /**
     * SystemLiteral_Char ::= [^"']
     */
    public static final NonTerminal SystemLiteral_Char = createNonTerminalSymbol("SystemLiteral-Char");
    
    /**
     * [13] PubidChar ::= #x20 | #xD | #xA | [a-zA-Z0-9] | [-()+,./:=?;!*#@$_%]
     * <p>
     * This production has been altered from the specification to remove the
     * apostrophe.
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PubidChar">
     *      PubidChar</a>
     */
    public static final NonTerminal PubidChar = createNonTerminalSymbol("PubidChar");
    
    public static final NonTerminal PubidChar_Plus = createNonTerminalSymbol("PubidChar-Plus");
    
    public static final NonTerminal QuotedPubidChar_Plus = createNonTerminalSymbol("QuotedPubidChar-Plus");
    
    /**
     * [29] markupdecl ::= {@linkplain #elementdecl elementdecl} |
     * {@linkplain #AttlistDecl AttlistDecl} | {@linkplain #EntityDecl
     * EntityDecl} | {@linkplain #NotationDecl NotationDecl} | {@linkplain #PI
     * PI} | {@linkplain #Comment Comment}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-markupdecl">
     *      markupdecl</a>
     */
    public static final NonTerminal markupdecl = createNonTerminalSymbol("markupdecl");
    
    /**
     * [28a] DeclSep ::= {@linkplain #PEReference PEReference} | {@linkplain #S
     * S}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-DeclSep">
     *      DeclSep</a>
     */
    public static final NonTerminal DeclSep = createNonTerminalSymbol("DeclSep");
    
    /**
     * [45] elementdecl ::= '&lt;!ELEMENT' {@linkplain #S S} {@linkplain #Name
     * Name} {@linkplain #S S} {@linkplain #contentspec contentspec}
     * {@linkplain #S S}? '&gt;'
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-elementdecl">
     *      elementdecl</a>
     */
    public static final NonTerminal elementdecl = createNonTerminalSymbol("elementdecl");
    
    /**
     * [52] AttlistDecl ::= '&lt;!ATTLIST' {@linkplain #S S} {@linkplain #Name
     * Name} {@linkplain #AttDef AttDef}* {@linkplain #S S}? '&gt;'
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-AttlistDecl">
     *      AttlistDecl</a>
     */
    public static final NonTerminal AttlistDecl = createNonTerminalSymbol("AttlistDecl");
    
    /**
     * [70] EntityDecl ::= {@linkplain #GEDecl GEDecl} | {@linkplain #PEDecl
     * PEDecl}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EntityDecl">
     *      EntityDecl</a>
     */
    public static final NonTerminal EntityDecl = createNonTerminalSymbol("EntityDecl");
    
    /**
     * [82] NotationDecl ::= '&lt;!NOTATION' {@linkplain #S S} {@linkplain #Name
     * Name} {@linkplain #S S} ({@linkplain #ExternalID ExternalID} |
     * {@linkplain #PublicID PublicID}) {@linkplain #S S}? '&gt;'
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-NotationDecl">
     *      NotationDecl</a>
     */
    public static final NonTerminal NotationDecl = createNonTerminalSymbol("NotationDecl");
    
    /**
     * [46] contentspec ::= 'EMPTY' | 'ANY' | {@linkplain #Mixed Mixed} |
     * {@linkplain #children children}
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-contentspec">
     *      contentspec</a>
     */
    public static final NonTerminal contentspec = createNonTerminalSymbol("contentspec");
    
    /**
     * [51] Mixed ::= '(' {@linkplain #S S}? '#PCDATA' ({@linkplain #S S}? '|'
     * {@linkplain #S S}? {@linkplain #Name Name})* {@linkplain #S S}? ')*' |
     * '(' {@linkplain #S S}? '#PCDATA' {@linkplain #S S}? ')'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Mixed">Mixed
     *      </a>
     */
    public static final NonTerminal Mixed = createNonTerminalSymbol("Mixed");
    
    public static final NonTerminal Mixed_inner = createNonTerminalSymbol("Mixed-inner");
    
    public static final NonTerminal Mixed_inner_Plus = createNonTerminalSymbol("Mixed-inner-Plus");
    
    /**
     * [47] children ::= ({@linkplain #choice choice} | {@linkplain #seq seq})
     * ('?' | '*' | '+')?
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-children">
     *      children</a>
     */
    public static final NonTerminal children = createNonTerminalSymbol("children");
    
    /**
     * [49] choice ::= '(' {@linkplain #S S}? {@linkplain #cp cp} (
     * {@linkplain #S S}? '|' {@linkplain #S S}? {@linkplain #cp cp} )+
     * {@linkplain #S S}? ')'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-choice">
     *      choice</a>
     */
    public static final NonTerminal choice = createNonTerminalSymbol("choice");
    
    public static final NonTerminal choice_inner = createNonTerminalSymbol("choice-inner");
    
    public static final NonTerminal choice_inner_Plus = createNonTerminalSymbol("choice-inner-Plus");
    
    /**
     * [50] seq ::= '(' {@linkplain #S S}? {@linkplain #cp cp} ( {@linkplain #S
     * S}? ',' {@linkplain #S S}? {@linkplain #cp cp} )* {@linkplain #S S}? ')'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-seq">seq</a>
     */
    public static final NonTerminal seq = createNonTerminalSymbol("seq");
    
    public static final NonTerminal seq_inner = createNonTerminalSymbol("seq-inner");
    
    public static final NonTerminal seq_inner_Plus = createNonTerminalSymbol("seq-inner-Plus");
    
    /**
     * [48] cp ::= ({@linkplain #Name Name} | {@linkplain #choice choice} |
     * {@linkplain #seq seq}) ('?' | '*' | '+')?
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-cp">cp</a>
     */
    public static final NonTerminal cp = createNonTerminalSymbol("cp");
    
    /**
     * [53] AttDef ::= {@linkplain #S S} {@linkplain #Name Name} {@linkplain #S
     * S} {@linkplain #AttType AttType} {@linkplain #S S}
     * {@linkplain #DefaultDecl DefaultDecl}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-AttDef">
     *      AttDef</a>
     */
    public static final NonTerminal AttDef = createNonTerminalSymbol("AttDef");
    
    public static final NonTerminal AttDef_Plus = createNonTerminalSymbol("AttDef-Plus");
    
    /**
     * [54] AttType ::= {@linkplain #StringType StringType} |
     * {@linkplain #TokenizedType TokenizedType} | {@linkplain #EnumeratedType
     * EnumeratedType}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-AttType">
     *      AttType</a>
     */
    public static final NonTerminal AttType = createNonTerminalSymbol("AttType");
    
    /**
     * [60] DefaultDecl ::= '#REQUIRED' | '#IMPLIED' | (('#FIXED' {@linkplain #S
     * S})? {@linkplain #AttValue AttValue})
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-DefaultDecl">
     *      DefaultDecl</a>
     */
    public static final NonTerminal DefaultDecl = createNonTerminalSymbol("DefaultDecl");
    
    /**
     * [55] StringType ::= 'CDATA'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-StringType">
     *      StringType</a>
     */
    public static final NonTerminal StringType = createNonTerminalSymbol("StringType");
    
    /**
     * TokenizedType ::= 'ID' | 'IDREF' | 'IDREFS' | 'ENTITY' | 'ENTITIES' |
     * 'NMTOKEN' | 'NMTOKENS'
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-TokenizedType">
     *      TokenizedType</a>
     */
    public static final NonTerminal TokenizedType = createNonTerminalSymbol("TokenizedType");
    
    /**
     * [57] EnumeratedType ::= {@linkplain #NotationType NotationType} |
     * {@linkplain #Enumeration Enumeration}
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EnumeratedType">
     *      EnumeratedType</a>
     */
    public static final NonTerminal EnumeratedType = createNonTerminalSymbol("EnumeratedType");
    
    /**
     * [58] NotationType ::= 'NOTATION' {@linkplain #S S} '(' {@linkplain #S S}?
     * {@linkplain #Name Name} ({@linkplain #S S}? '|' {@linkplain #S S}?
     * {@linkplain #Name Name})* {@linkplain #S S}? ')'
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-NotationType">
     *      NotationType</a>
     */
    public static final NonTerminal NotationType = createNonTerminalSymbol("NotationType");
    
    public static final NonTerminal NotationType_inner = createNonTerminalSymbol("NotationType-inner");
    
    public static final NonTerminal NotationType_inner_Plus = createNonTerminalSymbol("NotationType-inner-Plus");
    
    /**
     * [59] Enumeration ::= '(' {@linkplain #S S}? {@linkplain #Nmtoken Nmtoken}
     * ({@linkplain #S S}? '|' {@linkplain #S S}? {@linkplain #Nmtoken Nmtoken}
     * )* {@linkplain #S S}? ')'
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Enumeration">
     *      Enumeration</a>
     */
    public static final NonTerminal Enumeration = createNonTerminalSymbol("Enumeration");
    
    public static final NonTerminal Enumeration_inner = createNonTerminalSymbol("Enumeration-inner");
    
    public static final NonTerminal Enumeration_inner_Plus = createNonTerminalSymbol("Enumeration-inner-Plus");
    
    /**
     * [7] Nmtoken ::= ({@linkplain #NameChar NameChar})+
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Nmtoken">
     *      Nmtoken</a>
     */
    public static final NonTerminal Nmtoken = createNonTerminalSymbol("Nmtoken");
    
    /**
     * [10] AttValue ::= '"' ([^&lt;&amp;"] | {@linkplain #Reference Reference}
     * )* '"' | "'" ([^&lt;&amp;'] | {@linkplain #Reference Reference})* "'"
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-AttValue">
     *      AttValue</a>
     */
    public static final NonTerminal AttValue = createNonTerminalSymbol("AttValue");
    
    public static final NonTerminal AttValue_inner = createNonTerminalSymbol("AttValue-inner");
    
    public static final NonTerminal AttValue_inner_Plus = createNonTerminalSymbol("AttValue-inner-Plus");
    
    /**
     * AttValue_Char ::= [^<&'"]
     */
    public static final NonTerminal AttValue_Char = createNonTerminalSymbol("AttValue-Char");
    
    /**
     * [67] Reference ::= {@linkplain #EntityRef EntityRef} |
     * {@linkplain #CharRef CharRef}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Reference">
     *      Reference</a>
     */
    public static final NonTerminal Reference = createNonTerminalSymbol("Reference");
    
    /**
     * [68] EntityRef ::= '&amp;' {@linkplain #Name Name} ';'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EntityRef">
     *      EntityRef</a>
     */
    public static final NonTerminal EntityRef = createNonTerminalSymbol("EntityRef");
    
    /**
     * [66] CharRef ::= '&amp;#' [0-9]+ ';' | '&amp;#x' [0-9a-fA-F]+ ';'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CharRef">
     *      CharRef</a>
     */
    public static final NonTerminal CharRef = createNonTerminalSymbol("CharRef");
    
    /**
     * HEX_DIGIT ::= [0-9A-Fa-f]
     */
    public static final NonTerminal HEX_DIGIT = createNonTerminalSymbol("HEX-DIGIT");
    
    public static final NonTerminal HEX_DIGIT_Plus = createNonTerminalSymbol("HEX-DIGIT-Plus");
    
    /**
     * [71] GEDecl ::= '&lt;!ENTITY' {@linkplain #S S} {@linkplain #Name Name}
     * {@linkplain #S S} {@linkplain #EntityDef EntityDef} {@linkplain #S S}?
     * '&gt;'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-GEDecl">
     *      GEDecl</a>
     */
    public static final NonTerminal GEDecl = createNonTerminalSymbol("GEDecl");
    
    /**
     * [72] PEDecl ::= '&lt;!ENTITY' {@linkplain #S S} '%' {@linkplain #S S}
     * {@linkplain #Name Name} {@linkplain #S S} {@linkplain #PEDef PEDef}
     * {@linkplain #S S}? '&gt;'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PEDecl">
     *      PEDecl</a>
     */
    public static final NonTerminal PEDecl = createNonTerminalSymbol("PEDecl");
    
    /**
     * [73] EntityDef ::= {@linkplain #EntityValue EntityValue} | (
     * {@linkplain #ExternalID ExternalID} {@linkplain #NDataDecl NDataDecl}?)
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EntityDef">
     *      EntityDef</a>
     */
    public static final NonTerminal EntityDef = createNonTerminalSymbol("EntityDef");
    
    /**
     * [9] EntityValue ::= '"' ([^%&amp;"] | {@linkplain #PEReference
     * PEReference} | {@linkplain #Reference Reference})* '"' | "'" ([^%&amp;']
     * | {@linkplain #PEReference PEReference} | {@linkplain #Reference
     * Reference})* "'"
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EntityValue">
     *      EntityValue</a>
     */
    public static final NonTerminal EntityValue = createNonTerminalSymbol("EntityValue");
    
    public static final NonTerminal EntityValue_inner = createNonTerminalSymbol("EntityValue-inner");
    
    public static final NonTerminal EntityValue_inner_Plus = createNonTerminalSymbol("EntityValue-inner-Plus");
    
    /**
     * EntityValue_Char ::= [^%&'"]
     */
    public static final NonTerminal EntityValue_Char = createNonTerminalSymbol("EntityValue-Char");
    
    /**
     * [76] NDataDecl ::= {@linkplain #S S} 'NDATA' {@linkplain #S S}
     * {@linkplain #Name Name}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-NDataDecl">
     *      NDataDecl</a>
     */
    public static final NonTerminal NDataDecl = createNonTerminalSymbol("NDataDecl");
    
    /**
     * [69] PEReference ::= '%' {@linkplain #Name Name} ';'
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PEReference">
     *      PEReference</a>
     */
    public static final NonTerminal PEReference = createNonTerminalSymbol("PEReference");
    
    /**
     * [74] PEDef ::= {@linkplain #EntityValue EntityValue} |
     * {@linkplain #ExternalID ExternalID}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PEDef">PEDef
     *      </a>
     */
    public static final NonTerminal PEDef = createNonTerminalSymbol("PEDef");
    
    /**
     * [83] PublicID ::= 'PUBLIC' {@linkplain #S S} {@linkplain #PubidLiteral
     * PubidLiteral}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-PublicID">
     *      PublicID</a>
     */
    public static final NonTerminal PublicID = createNonTerminalSymbol("PublicID");
    
    /**
     * [44] EmptyElemTag ::= '&lt;' {@linkplain #Name Name} ({@linkplain #S S}
     * {@linkplain #Attribute Attribute})* {@linkplain #S S}? '/&gt;'
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-EmptyElemTag">
     *      EmptyElemTag</a>
     */
    public static final NonTerminal EmptyElemTag = createNonTerminalSymbol("EmptyElemTag");
    
    /**
     * [40] STag ::= '&lt;' {@linkplain #Name Name} ({@linkplain #S S}
     * {@linkplain #Attribute Attribute})* {@linkplain #S S}? '&gt;'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-STag">STag
     *      </a>
     */
    public static final NonTerminal STag = createNonTerminalSymbol("STag");
    
    /**
     * [43] content ::= {@linkplain #CharData CharData}? (({@linkplain #element
     * element} | {@linkplain #Reference Reference} | {@linkplain #CDSect
     * CDSect} | {@linkplain #PI PI} | {@linkplain #Comment Comment})
     * {@linkplain #CharData CharData}?)*
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-content">
     *      content</a>
     */
    public static final NonTerminal content = createNonTerminalSymbol("content");
    
    public static final NonTerminal content_and_ETag = createNonTerminalSymbol("content-and-ETag");
    
    /**
     * [42] ETag ::= '&lt;/' {@linkplain #Name Name} {@linkplain #S S}? '&gt;'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-ETag">ETag
     *      </a>
     */
    public static final NonTerminal ETag = createNonTerminalSymbol("ETag");
    
    /**
     * [41] Attribute ::= {@linkplain #Name Name} {@linkplain #Eq Eq}
     * {@linkplain #AttValue AttValue}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Attribute">
     *      Attribute</a>
     */
    public static final NonTerminal Attribute = createNonTerminalSymbol("Attribute");
    
    public static final NonTerminal AttributeList_Plus = createNonTerminalSymbol("AttributeList-Plus");
    
    /**
     * [14] CharData ::= [^&lt;&amp;]* - ([^&lt;&amp;]* ']]&gt;' [^&lt;&amp;]*)
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CharData">
     *      CharData</a>
     */
    public static final NonTerminal CharData = createNonTerminalSymbol("CharData");
    
    public static final NonTerminal CharData_Plus = createNonTerminalSymbol("CharData-Plus");
    
    /**
     * CharData_Single ::= [^&lt;&amp;\]&gt;]
     */
    public static final NonTerminal CharData_Single = createNonTerminalSymbol("CharData-Single");
    
    /**
     * [18] CDSect ::= {@linkplain #CDStart CDStart} {@linkplain #CData CData}
     * {@linkplain #CDEnd CDEnd}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CDSect">
     *      CDSect</a>
     */
    public static final NonTerminal CDSect = createNonTerminalSymbol("CDSect");
    
    /**
     * [19] CDStart ::= '&lt;![CDATA['
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CDStart">
     *      CDStart</a>
     */
    public static final NonTerminal CDStart = createNonTerminalSymbol("CDStart");
    
    /**
     * [20] CData ::= ({@linkplain #Char Char}* - ({@linkplain #Char Char}*
     * ']]&gt;' {@linkplain #Char Char}*))
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CData">CData
     *      </a>
     */
    public static final NonTerminal CData = createNonTerminalSymbol("CData");
    
    public static final NonTerminal CData_body = createNonTerminalSymbol("CData-body");
    
    public static final NonTerminal CData_body_suffix_Plus = createNonTerminalSymbol("CData-body-suffix-Plus");
    
    public static final NonTerminal CData_body_inner = createNonTerminalSymbol("CData-body-inner");
    
    public static final NonTerminal CData_body_inner_Plus = createNonTerminalSymbol("CData-body-inner-Plus");
    
    /**
     * CData_Char ::= (Char* - (Char* ']]>' Char*))
     */
    public static final NonTerminal CData_Char = createNonTerminalSymbol("CData-Char");
    
    public static final NonTerminal CData_Char_Plus = createNonTerminalSymbol("CData-Char-Plus");
    
    /**
     * [21] CDEnd ::= ']]&gt;'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CDEnd">CDEnd
     *      </a>
     */
    public static final NonTerminal CDEnd = createNonTerminalSymbol("CDEnd");
    
//	/*
//	 * All the terminal symbols reachable from the original production
//	 * "document".
//	 */
//	/**
//	 * U+0009 CHARACTER TABULATION (tab)
//	 */
//	public static final Terminal tab = new CharacterLiteral("#x9", '\t');
//	/**
//	 * U+000A LINE FEED (LF)
//	 */
//	public static final Terminal lineFeed = new CharacterLiteral("#xA", '\n');
//	/**
//	 * U+000D CARRIAGE RETURN (CR)
//	 */
//	public static final Terminal carriageReturn = new CharacterLiteral("#xD", '\r');
//	/**
//	 * U+0020 SPACE
//	 */
//	public static final Terminal space = new CharacterLiteral("#x20", ' ');
//	/**
//	 * U+0021 EXCLAMATION MARK (!)
//	 */
//	public static final Terminal exclamationMark = toLiteral('!'); // 21
//	/**
//	 * U+0022 QUOTATION MARK (")
//	 */
//	public static final Terminal quotationMark = toLiteral('"'); // 22
//	/**
//	 * U+0023 NUMBER SIGN (#)
//	 */
//	public static final Terminal numberSign = toLiteral('#'); // 23
//	/**
//	 * U+0024 DOLLAR SIGN ($)
//	 */
//	public static final Terminal dollarSign = toLiteral('$'); // 24
//	/**
//	 * U+0025 PERCENTAGE SIGN (%)
//	 */
//	public static final Terminal percentSign = toLiteral('%'); // 25
//	/**
//	 * U+0026 AMPERSAND (&)
//	 */
//	public static final Terminal ampersand = toLiteral('&'); // 26
//	/**
//	 * U+0027 APOSTROPHE (')
//	 */
//	public static final Terminal apostrophe = toLiteral('\''); // 27
//	/**
//	 * U+0028 LEFT PARENTHESIS (()
//	 */
//	public static final Terminal leftParenthesis = toLiteral('('); // 28
//	/**
//	 * U+0029 RIGHT PARENTHESIS ())
//	 */
//	public static final Terminal rightParenthesis = toLiteral(')'); // 29
//	/**
//	 * U+002A ASTERISK (*)
//	 */
//	public static final Terminal asterisk = toLiteral('*'); // 2A
//	/**
//	 * U+002B PLUS SIGN (+)
//	 */
//	public static final Terminal plusSign = toLiteral('+'); // 2B
//	/**
//	 * U+002C COMMA (,)
//	 */
//	public static final Terminal comma = toLiteral(','); // 2C
//	/**
//	 * U+002D HYPHEN-MINUS (-)
//	 */
//	public static final Terminal hyphenMinus = toLiteral('-'); // 2D
//	/**
//	 * U+002E FULL STOP (.)
//	 */
//	public static final Terminal fullStop = toLiteral('.'); // 2E
//	/**
//	 * U+002F SOLIDUS (/)
//	 */
//	public static final Terminal solidus = toLiteral('/'); // 2F
//	/**
//	 * U+0030 DIGIT ZERO (0)
//	 */
//	public static final Terminal digitZero = toLiteral('0');
//	/**
//	 * U+0031 DIGIT ONE (1)
//	 */
//	public static final Terminal digitOne = toLiteral('1');
//	/**
//	 * U+0032 DIGIT TWO (2)
//	 */
//	public static final Terminal digitTwo = toLiteral('2');
//	/**
//	 * U+0033 DIGIT THREE (3)
//	 */
//	public static final Terminal digitThree = toLiteral('3');
//	/**
//	 * U+0034 DIGIT FOUR (4)
//	 */
//	public static final Terminal digitFour = toLiteral('4');
//	/**
//	 * U+0035 DIGIT FIVE (5)
//	 */
//	public static final Terminal digitFive = toLiteral('5');
//	/**
//	 * U+0036 DIGIT SIX (6)
//	 */
//	public static final Terminal digitSix = toLiteral('6');
//	/**
//	 * U+0037 DIGIT SEVEN (7)
//	 */
//	public static final Terminal digitSeven = toLiteral('7');
//	/**
//	 * U+0038 DIGIT EIGHT (8)
//	 */
//	public static final Terminal digitEight = toLiteral('8');
//	/**
//	 * U+0039 DIGIT NINE (9)
//	 */
//	public static final Terminal digitNine = toLiteral('9');
//	/**
//	 * U+003A COLON (:)
//	 */
//	public static final Terminal colon = toLiteral(':'); // 3A
//	/**
//	 * U+003B SEMICOLON (;)
//	 */
//	public static final Terminal semicolon = toLiteral(';'); // 3B
//	/**
//	 * U+003C LESS-THAN SIGN (<)
//	 */
//	public static final Terminal lessThanSign = toLiteral('<'); // 3C
//	/**
//	 * U+003D EQUALS SIGN (=)
//	 */
//	public static final Terminal equalsSign = toLiteral('='); // 3D
//	/**
//	 * U+003E GREATER-THAN SIGN (>)
//	 */
//	public static final Terminal greaterThanSign = toLiteral('>'); // 3E
//	/**
//	 * U+003F QUESTION MARK (?)
//	 */
//	public static final Terminal questionMark = toLiteral('?'); // 3F
//	/**
//	 * U+0040 COMMERCIAL AT (@)
//	 */
//	public static final Terminal atSymbol = toLiteral('@'); // 40
//	/**
//	 * U+0041 LATIN CAPITAL LETTER A (A)
//	 * &ndash;
//	 * U+0046 LATIN CAPITAL LETTER F (F)
//	 */
//	public static final Terminal uppercaseHexDigit = toLiteral('A', 'F'); // 41 - 46
//	/**
//	 * &ndash;
//	 * U+005A LATIN CAPITAL LETTER Z (Z)
//	 */
//	public static final Terminal uppercaseASCII = toLiteral('G', 'Z'); // 47 - 5A
//	/**
//	 * U+005B LEFT SQUARE BRACKET ([)
//	 */
//	public static final Terminal leftBracket = toLiteral('['); // 5B
//	/**
//	 * U+005C REVERSE SOLIDUS (\)
//	 */
//	public static final Terminal reverseSolidus = toLiteral('\\'); // 5C = reverse solidus
//	/**
//	 * U+005D RIGHT SQUARE BRACKET (])
//	 */
//	public static final Terminal rightBracket = toLiteral(']'); // 5D
//	/**
//	 * U+005E CIRCUMFLEX ACCENT (^)
//	 */
//	public static final Terminal circumflexAccent = toLiteral('^'); // 5E = circumflex accent
//	/**
//	 * U+005F LOW LINE (_)
//	 */
//	public static final Terminal lowLine = toLiteral('_'); // 5F
//	/**
//	 * U+0060 GRAVE ACCENT (`)
//	 */
//	public static final Terminal graveAccent = toLiteral('`'); // 60 = grave accent
//	/**
//	 * U+0061 LATIN SMALL LETTER A (a)
//	 * &ndash;
//	 * U+0066 LATIN SMALL LETTER F (f)
//	 */
//	public static final Terminal lowercaseHexDigit = toLiteral('a', 'f'); // 61 - 66
//	/**
//	 * &ndash;
//	 * U+007A LATIN SMALL LETTER Z (z)
//	 */
//	public static final Terminal lowercaseASCII = toLiteral('g', 'z'); // 67 - 7A
//	/**
//	 * U+007B LEFT CURLY BRACKET ({)
//	 */
//	public static final Terminal leftCurlyBrace = toLiteral('{'); // 7B = left curly bracket
//	/**
//	 * U+007C VERTICAL LINE (|)
//	 */
//	public static final Terminal verticalBar = toLiteral('|'); // 7C
//	/**
//	 * U+007D RIGHT CURLY BRACKET (})
//	 */
//	public static final Terminal rightCurlyBrace = toLiteral('}'); // 7D = right curly bracket
//	/**
//	 * U+007E TILDE (~)
//	 */
//	public static final Terminal tilde = toLiteral('~'); // 7E = tilde
//	/**
//	 * U+007F DELETE
//	 */
//	public static final Terminal delete = toLiteral('\u007F'); // 7F = delete
//	// 80 = control
//	/**
//	 * U+0080
//	 * &ndash;
//	 * U+D7FF
//	 */
//	public static final Terminal belowSurrogates = toLiteral('\u0080', '\uD7FF');
//	/**
//	 * U+E000
//	 * &ndash;
//	 * U+FFFD
//	 */
//	public static final Terminal aboveSurrogates = toLiteral('\uE000', '\uFFFD');
//	// TODO: [#x10000-#x10FFFF]

//	public static final SymbolSequence lowercaseXML = toLiteral("xml");
//	public static final SymbolSequence lowercaseVersion = toLiteral("version");
//	public static final SymbolSequence lowercaseEncoding = toLiteral("encoding");
//	public static final SymbolSequence lowercaseStandalone = toLiteral("standalone");
//	public static final SymbolSequence lowercaseYes = toLiteral("yes");
//	public static final SymbolSequence lowercaseNo = toLiteral("no");
//	public static final SymbolSequence uppercaseDoctype = toLiteral("DOCTYPE");
//	public static final SymbolSequence uppercasePublic = toLiteral("PUBLIC");
//	public static final SymbolSequence uppercaseSystem = toLiteral("SYSTEM");
//	public static final SymbolSequence uppercaseElement = toLiteral("ELEMENT");
//	public static final SymbolSequence uppercaseEmpty = toLiteral("EMPTY");
//	public static final SymbolSequence uppercaseAny = toLiteral("ANY");
//	public static final SymbolSequence uppercasePCData = toLiteral("PCDATA");
//	public static final SymbolSequence uppercaseAttList = toLiteral("ATTLIST");
//	public static final SymbolSequence uppercaseCData = toLiteral("CDATA");
//	public static final SymbolSequence uppercaseID = toLiteral("ID");
//	public static final SymbolSequence uppercaseIDRef = toLiteral("IDREF");
//	public static final SymbolSequence uppercaseIDRefs = toLiteral("IDREFS");
//	public static final SymbolSequence uppercaseEntity = toLiteral("ENTITY");
//	public static final SymbolSequence uppercaseEntities = toLiteral("ENTITIES");
//	public static final SymbolSequence uppercaseNMToken = toLiteral("NMTOKEN");
//	public static final SymbolSequence uppercaseNMTokens = toLiteral("NMTOKENS");
//	public static final SymbolSequence uppercaseNotation = toLiteral("NOTATION");
//	public static final SymbolSequence uppercaseRequired = toLiteral("REQUIRED");
//	public static final SymbolSequence uppercaseImplied = toLiteral("IMPLIED");
//	public static final SymbolSequence uppercaseFixed = toLiteral("FIXED");
//	public static final SymbolSequence uppercaseNData = toLiteral("NDATA");
    
    /*
     * All the productions included in the XML specification that are not
     * reachable from the root production "document". These are used during
     * validation and well-formedness checks.
     */
    /**
     * [6] Names ::= {@linkplain #Name Name} (#x20 {@linkplain #Name Name})*
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Names">Names
     *      </a>
     */
    public static final NonTerminal Names = createNonTerminalSymbol("Names");
    
    public static final NonTerminal Names_inner_Plus = createNonTerminalSymbol("Names-inner-Plus");
    
    /**
     * [8] Nmtokens ::= {@linkplain #Nmtoken Nmtoken} (#x20 {@linkplain #Nmtoken
     * Nmtoken})*
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Nmtokens">
     *      Nmtokens</a>
     */
    public static final NonTerminal Nmtokens = createNonTerminalSymbol("Nmtokens");
    
    public static final NonTerminal Nmtokens_inner_Kleene = createNonTerminalSymbol("Nmtokens-inner-Kleene");
    
    /**
     * [30] extSubset ::= {@linkplain #TextDecl TextDecl}?
     * {@linkplain #extSubsetDecl extSubsetDecl}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-extSubset">
     *      extSubset</a>
     */
    public static final NonTerminal extSubset = createNonTerminalSymbol("extSubset");
    
    /**
     * [77] TextDecl ::= '&lt;?xml' {@linkplain #VersionInfo VersionInfo}?
     * {@linkplain #EncodingDecl EncodingDecl} {@linkplain #S S}? '?&gt;'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-TextDecl">
     *      TextDecl</a>
     */
    public static final NonTerminal TextDecl = createNonTerminalSymbol("TextDecl");
    
    /**
     * [31] extSubsetDecl ::= ( {@linkplain #markupdecl markupdecl} |
     * {@linkplain #conditionalSect conditionalSect} | {@linkplain #DeclSep
     * DeclSep})*
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-extSubsetDecl">
     *      extSubsetDecl</a>
     */
    public static final NonTerminal extSubsetDecl = createNonTerminalSymbol("extSubsetDecl");
    
    public static final NonTerminal extSubsetDecl_inner = createNonTerminalSymbol("extSubsetDecl-inner");
    
    public static final NonTerminal extSubsetDecl_inner_Plus = createNonTerminalSymbol("extSubsetDecl-inner-Plus");
    
    /**
     * [61] conditionalSect ::= {@linkplain #includeSect includeSect} |
     * {@linkplain #ignoreSect ignoreSect}
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-conditionalSect">
     *      conditionalSect</a>
     */
    public static final NonTerminal conditionalSect = createNonTerminalSymbol("conditionalSect");
    
    /**
     * [62] includeSect ::= '&lt;![' {@linkplain #S S}? 'INCLUDE' {@linkplain #S
     * S}? '[' {@linkplain #extSubsetDecl extSubsetDecl} ']]&gt;'
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-includeSect">
     *      includeSect</a>
     */
    public static final NonTerminal includeSect = createNonTerminalSymbol("includeSect");
    
    /**
     * [63] ignoreSect ::= '&lt;![' {@linkplain #S S}? 'IGNORE' {@linkplain #S
     * S}? '[' {@linkplain #ignoreSectContents ignoreSectContents}* ']]&gt;'
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-ignoreSect">
     *      ignoreSect</a>
     */
    public static final NonTerminal ignoreSect = createNonTerminalSymbol("ignoreSect");
    
    /**
     * [64] ignoreSectContents ::= {@linkplain #Ignore Ignore} ('&lt;!['
     * {@linkplain #ignoreSectContents ignoreSectContents} ']]&gt;'
     * {@linkplain #Ignore Ignore})*
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-ignoreSectContents">
     *      ignoreSectContents</a>
     */
    public static final NonTerminal ignoreSectContents = createNonTerminalSymbol("ignoreSectContents");
    
    public static final NonTerminal ignoreSectContents_Plus = createNonTerminalSymbol("ignoreSectContents-Plus");
    
    public static final NonTerminal ignoreSectContents_inner_Plus =
            createNonTerminalSymbol("ignoreSectContents-inner-Plus");
            
    /**
     * [65] Ignore ::= {@linkplain #Char Char}* - ({@linkplain #Char Char}*
     * ('&lt;![' | ']]&gt;') {@linkplain #Char Char}*)
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Ignore">
     *      Ignore</a>
     */
    public static final NonTerminal Ignore = createNonTerminalSymbol("Ignore");
    
    /**
     * Ignore_Char ::= Char* - (Char* ('<![' | ']]>') Char*)
     */
    public static final NonTerminal Ignore_Char = createNonTerminalSymbol("Ignore-Char");
    
    public static final NonTerminal Ignore_Char_Plus = createNonTerminalSymbol("Ignore-Char-Plus");
    
    /**
     * [78] extParsedEnt ::= {@linkplain #TextDecl TextDecl}?
     * {@linkplain #content content}
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-extParsedEnt">
     *      extParsedEnt</a>
     */
    public static final NonTerminal extParsedEnt = createNonTerminalSymbol("extParsedEnt");
    
    /**
     * [84] Letter ::= {@linkplain #BaseChar BaseChar} |
     * {@linkplain #Ideographic Ideographic}
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Letter">
     *      Letter</a>
     */
    public static final NonTerminal Letter = createNonTerminalSymbol("Letter");
    
    /**
     * [85] BaseChar ::= [#x0041-#x005A] | [#x0061-#x007A] | [#x00C0-#x00D6] |
     * [#x00D8-#x00F6] | [#x00F8-#x00FF] | [#x0100-#x0131] | [#x0134-#x013E] |
     * [#x0141-#x0148] | [#x014A-#x017E] | [#x0180-#x01C3] | [#x01CD-#x01F0] |
     * [#x01F4-#x01F5] | [#x01FA-#x0217] | [#x0250-#x02A8] | [#x02BB-#x02C1] |
     * #x0386 | [#x0388-#x038A] | #x038C | [#x038E-#x03A1] | [#x03A3-#x03CE] |
     * [#x03D0-#x03D6] | #x03DA | #x03DC | #x03DE | #x03E0 | [#x03E2-#x03F3] |
     * [#x0401-#x040C] | [#x040E-#x044F] | [#x0451-#x045C] | [#x045E-#x0481] |
     * [#x0490-#x04C4] | [#x04C7-#x04C8] | [#x04CB-#x04CC] | [#x04D0-#x04EB] |
     * [#x04EE-#x04F5] | [#x04F8-#x04F9] | [#x0531-#x0556] | #x0559 |
     * [#x0561-#x0586] | [#x05D0-#x05EA] | [#x05F0-#x05F2] | [#x0621-#x063A] |
     * [#x0641-#x064A] | [#x0671-#x06B7] | [#x06BA-#x06BE] | [#x06C0-#x06CE] |
     * [#x06D0-#x06D3] | #x06D5 | [#x06E5-#x06E6] | [#x0905-#x0939] | #x093D |
     * [#x0958-#x0961] | [#x0985-#x098C] | [#x098F-#x0990] | [#x0993-#x09A8] |
     * [#x09AA-#x09B0] | #x09B2 | [#x09B6-#x09B9] | [#x09DC-#x09DD] |
     * [#x09DF-#x09E1] | [#x09F0-#x09F1] | [#x0A05-#x0A0A] | [#x0A0F-#x0A10] |
     * [#x0A13-#x0A28] | [#x0A2A-#x0A30] | [#x0A32-#x0A33] | [#x0A35-#x0A36] |
     * [#x0A38-#x0A39] | [#x0A59-#x0A5C] | #x0A5E | [#x0A72-#x0A74] |
     * [#x0A85-#x0A8B] | #x0A8D | [#x0A8F-#x0A91] | [#x0A93-#x0AA8] |
     * [#x0AAA-#x0AB0] | [#x0AB2-#x0AB3] | [#x0AB5-#x0AB9] | #x0ABD | #x0AE0 |
     * [#x0B05-#x0B0C] | [#x0B0F-#x0B10] | [#x0B13-#x0B28] | [#x0B2A-#x0B30] |
     * [#x0B32-#x0B33] | [#x0B36-#x0B39] | #x0B3D | [#x0B5C-#x0B5D] |
     * [#x0B5F-#x0B61] | [#x0B85-#x0B8A] | [#x0B8E-#x0B90] | [#x0B92-#x0B95] |
     * [#x0B99-#x0B9A] | #x0B9C | [#x0B9E-#x0B9F] | [#x0BA3-#x0BA4] |
     * [#x0BA8-#x0BAA] | [#x0BAE-#x0BB5] | [#x0BB7-#x0BB9] | [#x0C05-#x0C0C] |
     * [#x0C0E-#x0C10] | [#x0C12-#x0C28] | [#x0C2A-#x0C33] | [#x0C35-#x0C39] |
     * [#x0C60-#x0C61] | [#x0C85-#x0C8C] | [#x0C8E-#x0C90] | [#x0C92-#x0CA8] |
     * [#x0CAA-#x0CB3] | [#x0CB5-#x0CB9] | #x0CDE | [#x0CE0-#x0CE1] |
     * [#x0D05-#x0D0C] | [#x0D0E-#x0D10] | [#x0D12-#x0D28] | [#x0D2A-#x0D39] |
     * [#x0D60-#x0D61] | [#x0E01-#x0E2E] | #x0E30 | [#x0E32-#x0E33] |
     * [#x0E40-#x0E45] | [#x0E81-#x0E82] | #x0E84 | [#x0E87-#x0E88] | #x0E8A |
     * #x0E8D | [#x0E94-#x0E97] | [#x0E99-#x0E9F] | [#x0EA1-#x0EA3] | #x0EA5 |
     * #x0EA7 | [#x0EAA-#x0EAB] | [#x0EAD-#x0EAE] | #x0EB0 | [#x0EB2-#x0EB3] |
     * #x0EBD | [#x0EC0-#x0EC4] | [#x0F40-#x0F47] | [#x0F49-#x0F69] |
     * [#x10A0-#x10C5] | [#x10D0-#x10F6] | #x1100 | [#x1102-#x1103] |
     * [#x1105-#x1107] | #x1109 | [#x110B-#x110C] | [#x110E-#x1112] | #x113C |
     * #x113E | #x1140 | #x114C | #x114E | #x1150 | [#x1154-#x1155] | #x1159 |
     * [#x115F-#x1161] | #x1163 | #x1165 | #x1167 | #x1169 | [#x116D-#x116E] |
     * [#x1172-#x1173] | #x1175 | #x119E | #x11A8 | #x11AB | [#x11AE-#x11AF] |
     * [#x11B7-#x11B8] | #x11BA | [#x11BC-#x11C2] | #x11EB | #x11F0 | #x11F9 |
     * [#x1E00-#x1E9B] | [#x1EA0-#x1EF9] | [#x1F00-#x1F15] | [#x1F18-#x1F1D] |
     * [#x1F20-#x1F45] | [#x1F48-#x1F4D] | [#x1F50-#x1F57] | #x1F59 | #x1F5B |
     * #x1F5D | [#x1F5F-#x1F7D] | [#x1F80-#x1FB4] | [#x1FB6-#x1FBC] | #x1FBE |
     * [#x1FC2-#x1FC4] | [#x1FC6-#x1FCC] | [#x1FD0-#x1FD3] | [#x1FD6-#x1FDB] |
     * [#x1FE0-#x1FEC] | [#x1FF2-#x1FF4] | [#x1FF6-#x1FFC] | #x2126 |
     * [#x212A-#x212B] | #x212E | [#x2180-#x2182] | [#x3041-#x3094] |
     * [#x30A1-#x30FA] | [#x3105-#x312C] | [#xAC00-#xD7A3]
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-BaseChar">
     *      BaseChar</a>
     */
    public static final NonTerminal BaseChar = createNonTerminalSymbol("BaseChar");
    
    /**
     * [86] Ideographic ::= [#x4E00-#x9FA5] | #x3007 | [#x3021-#x3029]
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Ideographic">
     *      Ideographic</a>
     */
    public static final NonTerminal Ideographic = createNonTerminalSymbol("Ideographic");
    
    /**
     * [87] CombiningChar ::= [#x0300-#x0345] | [#x0360-#x0361] |
     * [#x0483-#x0486] | [#x0591-#x05A1] | [#x05A3-#x05B9] | [#x05BB-#x05BD] |
     * #x05BF | [#x05C1-#x05C2] | #x05C4 | [#x064B-#x0652] | #x0670 |
     * [#x06D6-#x06DC] | [#x06DD-#x06DF] | [#x06E0-#x06E4] | [#x06E7-#x06E8] |
     * [#x06EA-#x06ED] | [#x0901-#x0903] | #x093C | [#x093E-#x094C] | #x094D |
     * [#x0951-#x0954] | [#x0962-#x0963] | [#x0981-#x0983] | #x09BC | #x09BE |
     * #x09BF | [#x09C0-#x09C4] | [#x09C7-#x09C8] | [#x09CB-#x09CD] | #x09D7 |
     * [#x09E2-#x09E3] | #x0A02 | #x0A3C | #x0A3E | #x0A3F | [#x0A40-#x0A42] |
     * [#x0A47-#x0A48] | [#x0A4B-#x0A4D] | [#x0A70-#x0A71] | [#x0A81-#x0A83] |
     * #x0ABC | [#x0ABE-#x0AC5] | [#x0AC7-#x0AC9] | [#x0ACB-#x0ACD] |
     * [#x0B01-#x0B03] | #x0B3C | [#x0B3E-#x0B43] | [#x0B47-#x0B48] |
     * [#x0B4B-#x0B4D] | [#x0B56-#x0B57] | [#x0B82-#x0B83] | [#x0BBE-#x0BC2] |
     * [#x0BC6-#x0BC8] | [#x0BCA-#x0BCD] | #x0BD7 | [#x0C01-#x0C03] |
     * [#x0C3E-#x0C44] | [#x0C46-#x0C48] | [#x0C4A-#x0C4D] | [#x0C55-#x0C56] |
     * [#x0C82-#x0C83] | [#x0CBE-#x0CC4] | [#x0CC6-#x0CC8] | [#x0CCA-#x0CCD] |
     * [#x0CD5-#x0CD6] | [#x0D02-#x0D03] | [#x0D3E-#x0D43] | [#x0D46-#x0D48] |
     * [#x0D4A-#x0D4D] | #x0D57 | #x0E31 | [#x0E34-#x0E3A] | [#x0E47-#x0E4E] |
     * #x0EB1 | [#x0EB4-#x0EB9] | [#x0EBB-#x0EBC] | [#x0EC8-#x0ECD] |
     * [#x0F18-#x0F19] | #x0F35 | #x0F37 | #x0F39 | #x0F3E | #x0F3F |
     * [#x0F71-#x0F84] | [#x0F86-#x0F8B] | [#x0F90-#x0F95] | #x0F97 |
     * [#x0F99-#x0FAD] | [#x0FB1-#x0FB7] | #x0FB9 | [#x20D0-#x20DC] | #x20E1 |
     * [#x302A-#x302F] | #x3099 | #x309A
     * 
     * @see <a href=
     *      "http://www.w3.org/TR/2008/REC-xml-20081126/#NT-CombiningChar">
     *      CombiningChar</a>
     */
    public static final NonTerminal CombiningChar = createNonTerminalSymbol("CombiningChar");
    
    /**
     * [88] Digit ::= [#x0030-#x0039] | [#x0660-#x0669] | [#x06F0-#x06F9] |
     * [#x0966-#x096F] | [#x09E6-#x09EF] | [#x0A66-#x0A6F] | [#x0AE6-#x0AEF] |
     * [#x0B66-#x0B6F] | [#x0BE7-#x0BEF] | [#x0C66-#x0C6F] | [#x0CE6-#x0CEF] |
     * [#x0D66-#x0D6F] | [#x0E50-#x0E59] | [#x0ED0-#x0ED9] | [#x0F20-#x0F29]
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Digit">Digit
     *      </a>
     */
    public static final NonTerminal Digit = createNonTerminalSymbol("Digit");
    
    /**
     * [89] Extender ::= #x00B7 | #x02D0 | #x02D1 | #x0387 | #x0640 | #x0E46 |
     * #x0EC6 | #x3005 | [#x3031-#x3035] | [#x309D-#x309E] | [#x30FC-#x30FE]
     * 
     * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-Extender">
     *      Extender</a>
     */
    public static final NonTerminal Extender = createNonTerminalSymbol("Extender");
    
//	public static final SymbolSequence uppercaseInclude = toLiteral("INCLUDE");
//	public static final SymbolSequence uppercaseIgnore= toLiteral("IGNORE");
    
    private static class EnumSetFactory implements SetFactory<UnicodeTerminalSymbols> {
        
        @Override
        public Set<UnicodeTerminalSymbols> getNewSet() {
            return EnumSet.noneOf(UnicodeTerminalSymbols.class);
        }
        
        @Override
        public Set<UnicodeTerminalSymbols> getNewSet(final Collection<UnicodeTerminalSymbols> c) {
            return EnumSet.copyOf(c);
        }
        
    }
    
    private static class IgnoreHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            return "ignore";
        }
        
    }
    
    private static final ProductionHandler IGNORE_HANDLER = new IgnoreHandler();
    
    private static class PrintProductionHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            System.out.println(rightHandSide);
            return rightHandSide;
        }
        
    }
    
    private static final ProductionHandler HANDLER = new PrintProductionHandler();
    
    private static final class CharacterHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final UnicodeTerminalSymbols symbol = (UnicodeTerminalSymbols) rightHandSide.get(0);
            return symbol.getCharacter();
        }
        
    }
    
    private static final ProductionHandler CHAR_HANDLER = new CharacterHandler();
    
    private static class StringConcatenationHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final StringBuilder builder = new StringBuilder();
            for (final Object object : rightHandSide) {
                builder.append(object);
            }
            return builder.toString();
        }
        
    }
    
    private static final ProductionHandler STRING_CONCAT = new StringConcatenationHandler();
    
    private static class StartTagHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            // get(0) == '<'
            final String name = rightHandSide.get(1).toString();
            // get(size()) == '>'
            return '<' + name + '>';
        }
        
    }
    
    private static final ProductionHandler STAG_HANDLER = new StartTagHandler();
    
    private static class StartTagWithAttributesHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            // get(0) == '<'
            final String name = rightHandSide.get(1).toString();
            // get(size()) == '>'
            return '<' + name + ' ' + rightHandSide.get(2) + '>';
        }
        
    }
    
    private static final ProductionHandler STAG_ATT_HANDLER = new StartTagWithAttributesHandler();
    
    private static class EndTagHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            // get(0) == '<'
            // get(1) == '/'
            final String name = rightHandSide.get(2).toString();
            // get(size()) == '>'
            return name;
        }
        
    }
    
    private static final ProductionHandler ETAG_HANDLER = new EndTagHandler();
    
    private static class AttributeHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            return rightHandSide.get(0) + "=" + rightHandSide.get(2);
        }
        
    }
    
    private static final ProductionHandler ATTRIBUTE_HANDLER = new AttributeHandler();
    
    private static class AttributeListHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            if (rightHandSide.size() == 2) {
                final List<Object> list = new ArrayList<>();
                list.add(rightHandSide.get(1));
                return list;
            } else {
                final List<Object> list = (List<Object>) rightHandSide.get(0);
                list.add(rightHandSide.get(2));
                return list;
            }
        }
        
    }
    
    private static final ProductionHandler ATT_LIST_HANDLER = new AttributeListHandler();
    
    private final class SystemLiteralHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            systemID = rightHandSide.get(1).toString();
            return rightHandSide.get(1);
        }
        
    }
    
    private class ConstantHandler implements ProductionHandler {
        
        private final Object object;
        
        public ConstantHandler(final Object object) {
            super();
            this.object = object;
        }
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            return object;
        }
        
    }
    
    private static final DOMImplementationRegistry registry;
    
    static {
        try {
            registry = DOMImplementationRegistry.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    private class DoctypeDeclHandler implements ProductionHandler {
        
        @Override
        public Object handleReduction(final List<Object> rightHandSide) {
            final DOMImplementation impl = registry.getDOMImplementation("Core");
            final DocumentType domDocType = impl.createDocumentType(null, publicID, systemID);
            domDocument = impl.createDocument(null, "html", domDocType);
            return null;
        }
        
    }
    
    protected Document domDocument = null;
    
    protected String publicID = null;
    
    protected String systemID = null;
    
    public XMLGrammar() {
        super(new EnumSetFactory(), new DefaultMapFactory<NonTerminalSymbol, Set<Production>>(),
                new DefaultSetFactory<NonTerminalSymbol>());
                
        /*
         * Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] |
         * [#x10000-#x10FFFF] CharData_Single ::= [^<&]* - ([^<&]* ']]>' [^<&]*)
         * Comment_Char ::= (Char - '-') | ('-' (Char - '-')) PI_Char ::= (Char*
         * - (Char* '?>' Char*)) CData_Char ::= (Char* - (Char* ']]>' Char*))
         * WHITE_SPACE ::= #x20 | #x9 | #xD | #xA NameStartChar ::= ":" | [A-Z]
         * | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] |
         * [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] |
         * [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD]
         * | [#x10000-#xEFFFF] NameChar ::= NameStartChar | "-" | "." | [0-9] |
         * #xB7 | [#x0300-#x036F] | [#x203F-#x2040] PubidChar ::= #x20 | #xD |
         * #xA | [a-zA-Z0-9] | [-'()+,./:=?;!*#@$_%] Ignore_Char ::= Char* -
         * (Char* ('<![' | ']]>') Char*) EncName_StartChar ::= [A-Za-z]
         * EncName_Char ::= [A-Za-z0-9._] | '-' DECIMAL_DIGIT ::= [0-9]
         * HEX_DIGIT ::= [0-9A-Fa-f] EntityValue_Char ::= [^%&'"] AttValue_Char
         * ::= [^<&'"] SystemLiteral_Char ::= [^"']
         */
        this.addProduction(epsilon, HANDLER, new GenericSymbol[0]);
        // #x9
        this.addProduction(Char, CHAR_HANDLER, tab);
        this.addProduction(CharData_Single, CHAR_HANDLER, tab);
        this.addProduction(Comment_Char, CHAR_HANDLER, tab);
        this.addProduction(CData_Char, CHAR_HANDLER, tab);
        this.addProduction(WHITE_SPACE, CHAR_HANDLER, tab);
        this.addProduction(Ignore_Char, CHAR_HANDLER, tab);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, tab);
        this.addProduction(AttValue_Char, CHAR_HANDLER, tab);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, tab);
        // #xA
        this.addProduction(Char, CHAR_HANDLER, lineFeed);
        this.addProduction(CharData_Single, CHAR_HANDLER, lineFeed);
        this.addProduction(Comment_Char, CHAR_HANDLER, lineFeed);
        this.addProduction(CData_Char, CHAR_HANDLER, lineFeed);
        this.addProduction(WHITE_SPACE, CHAR_HANDLER, lineFeed);
        this.addProduction(PubidChar, CHAR_HANDLER, lineFeed);
        this.addProduction(Ignore_Char, CHAR_HANDLER, lineFeed);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, lineFeed);
        this.addProduction(AttValue_Char, CHAR_HANDLER, lineFeed);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, lineFeed);
        // #xD
        this.addProduction(Char, CHAR_HANDLER, carriageReturn);
        this.addProduction(CharData_Single, CHAR_HANDLER, carriageReturn);
        this.addProduction(Comment_Char, CHAR_HANDLER, carriageReturn);
        this.addProduction(CData_Char, CHAR_HANDLER, carriageReturn);
        this.addProduction(WHITE_SPACE, CHAR_HANDLER, carriageReturn);
        this.addProduction(PubidChar, CHAR_HANDLER, carriageReturn);
        this.addProduction(Ignore_Char, CHAR_HANDLER, carriageReturn);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, carriageReturn);
        this.addProduction(AttValue_Char, CHAR_HANDLER, carriageReturn);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, carriageReturn);
        // #x20
        this.addProduction(Char, CHAR_HANDLER, space);
        this.addProduction(CharData_Single, CHAR_HANDLER, space);
        this.addProduction(Comment_Char, CHAR_HANDLER, space);
        this.addProduction(CData_Char, CHAR_HANDLER, space);
        this.addProduction(WHITE_SPACE, CHAR_HANDLER, space);
        this.addProduction(PubidChar, CHAR_HANDLER, space);
        this.addProduction(Ignore_Char, CHAR_HANDLER, space);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, space);
        this.addProduction(AttValue_Char, CHAR_HANDLER, space);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, space);
        // #x21
        this.addProduction(Char, CHAR_HANDLER, exclamationMark);
        this.addProduction(CharData_Single, CHAR_HANDLER, exclamationMark);
        this.addProduction(Comment_Char, CHAR_HANDLER, exclamationMark);
        this.addProduction(PI_Char, CHAR_HANDLER, exclamationMark);
        this.addProduction(CData_Char, CHAR_HANDLER, exclamationMark);
        this.addProduction(PubidChar, CHAR_HANDLER, exclamationMark);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, exclamationMark);
        this.addProduction(AttValue_Char, CHAR_HANDLER, exclamationMark);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, exclamationMark);
        // #x22
        this.addProduction(Char, CHAR_HANDLER, quotationMark);
        this.addProduction(CharData_Single, CHAR_HANDLER, quotationMark);
        this.addProduction(Comment_Char, CHAR_HANDLER, quotationMark);
        this.addProduction(PI_Char, CHAR_HANDLER, quotationMark);
        this.addProduction(CData_Char, CHAR_HANDLER, quotationMark);
        this.addProduction(Ignore_Char, CHAR_HANDLER, quotationMark);
        // #x23
        this.addProduction(Char, CHAR_HANDLER, numberSign);
        this.addProduction(CharData_Single, CHAR_HANDLER, numberSign);
        this.addProduction(Comment_Char, CHAR_HANDLER, numberSign);
        this.addProduction(PI_Char, CHAR_HANDLER, numberSign);
        this.addProduction(CData_Char, CHAR_HANDLER, numberSign);
        this.addProduction(PubidChar, CHAR_HANDLER, numberSign);
        this.addProduction(Ignore_Char, CHAR_HANDLER, numberSign);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, numberSign);
        this.addProduction(AttValue_Char, CHAR_HANDLER, numberSign);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, numberSign);
        // #x24
        this.addProduction(Char, CHAR_HANDLER, dollarSign);
        this.addProduction(CharData_Single, CHAR_HANDLER, dollarSign);
        this.addProduction(Comment_Char, CHAR_HANDLER, dollarSign);
        this.addProduction(PI_Char, CHAR_HANDLER, dollarSign);
        this.addProduction(CData_Char, CHAR_HANDLER, dollarSign);
        this.addProduction(PubidChar, CHAR_HANDLER, dollarSign);
        this.addProduction(Ignore_Char, CHAR_HANDLER, dollarSign);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, dollarSign);
        this.addProduction(AttValue_Char, CHAR_HANDLER, dollarSign);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, dollarSign);
        // #x25
        this.addProduction(Char, CHAR_HANDLER, percentSign);
        this.addProduction(CharData_Single, CHAR_HANDLER, percentSign);
        this.addProduction(Comment_Char, CHAR_HANDLER, percentSign);
        this.addProduction(PI_Char, CHAR_HANDLER, percentSign);
        this.addProduction(CData_Char, CHAR_HANDLER, percentSign);
        this.addProduction(PubidChar, CHAR_HANDLER, percentSign);
        this.addProduction(Ignore_Char, CHAR_HANDLER, percentSign);
        this.addProduction(AttValue_Char, CHAR_HANDLER, percentSign);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, percentSign);
        // #x26
        this.addProduction(Char, CHAR_HANDLER, ampersand);
        this.addProduction(Comment_Char, CHAR_HANDLER, ampersand);
        this.addProduction(PI_Char, CHAR_HANDLER, ampersand);
        this.addProduction(CData_Char, CHAR_HANDLER, ampersand);
        this.addProduction(Ignore_Char, CHAR_HANDLER, ampersand);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, ampersand);
        // #x27
        this.addProduction(Char, CHAR_HANDLER, apostrophe);
        this.addProduction(CharData_Single, CHAR_HANDLER, apostrophe);
        this.addProduction(Comment_Char, CHAR_HANDLER, apostrophe);
        this.addProduction(PI_Char, CHAR_HANDLER, apostrophe);
        this.addProduction(CData_Char, CHAR_HANDLER, apostrophe);
        this.addProduction(Ignore_Char, CHAR_HANDLER, apostrophe);
        // #x28
        this.addProduction(Char, CHAR_HANDLER, leftParenthesis);
        this.addProduction(CharData_Single, CHAR_HANDLER, leftParenthesis);
        this.addProduction(Comment_Char, CHAR_HANDLER, leftParenthesis);
        this.addProduction(PI_Char, CHAR_HANDLER, leftParenthesis);
        this.addProduction(CData_Char, CHAR_HANDLER, leftParenthesis);
        this.addProduction(PubidChar, CHAR_HANDLER, leftParenthesis);
        this.addProduction(Ignore_Char, CHAR_HANDLER, leftParenthesis);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, leftParenthesis);
        this.addProduction(AttValue_Char, CHAR_HANDLER, leftParenthesis);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, leftParenthesis);
        // #x29
        this.addProduction(Char, CHAR_HANDLER, rightParenthesis);
        this.addProduction(CharData_Single, CHAR_HANDLER, rightParenthesis);
        this.addProduction(Comment_Char, CHAR_HANDLER, rightParenthesis);
        this.addProduction(PI_Char, CHAR_HANDLER, rightParenthesis);
        this.addProduction(CData_Char, CHAR_HANDLER, rightParenthesis);
        this.addProduction(PubidChar, CHAR_HANDLER, rightParenthesis);
        this.addProduction(Ignore_Char, CHAR_HANDLER, rightParenthesis);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, rightParenthesis);
        this.addProduction(AttValue_Char, CHAR_HANDLER, rightParenthesis);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, rightParenthesis);
        // #x2A
        this.addProduction(Char, CHAR_HANDLER, asterisk);
        this.addProduction(CharData_Single, CHAR_HANDLER, asterisk);
        this.addProduction(Comment_Char, CHAR_HANDLER, asterisk);
        this.addProduction(PI_Char, CHAR_HANDLER, asterisk);
        this.addProduction(CData_Char, CHAR_HANDLER, asterisk);
        this.addProduction(PubidChar, CHAR_HANDLER, asterisk);
        this.addProduction(Ignore_Char, CHAR_HANDLER, asterisk);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, asterisk);
        this.addProduction(AttValue_Char, CHAR_HANDLER, asterisk);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, asterisk);
        // #x2B
        this.addProduction(Char, CHAR_HANDLER, plusSign);
        this.addProduction(CharData_Single, CHAR_HANDLER, plusSign);
        this.addProduction(Comment_Char, CHAR_HANDLER, plusSign);
        this.addProduction(PI_Char, CHAR_HANDLER, plusSign);
        this.addProduction(CData_Char, CHAR_HANDLER, plusSign);
        this.addProduction(PubidChar, CHAR_HANDLER, plusSign);
        this.addProduction(Ignore_Char, CHAR_HANDLER, plusSign);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, plusSign);
        this.addProduction(AttValue_Char, CHAR_HANDLER, plusSign);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, plusSign);
        // #x2C
        this.addProduction(Char, CHAR_HANDLER, comma);
        this.addProduction(CharData_Single, CHAR_HANDLER, comma);
        this.addProduction(Comment_Char, CHAR_HANDLER, comma);
        this.addProduction(PI_Char, CHAR_HANDLER, comma);
        this.addProduction(CData_Char, CHAR_HANDLER, comma);
        this.addProduction(PubidChar, CHAR_HANDLER, comma);
        this.addProduction(Ignore_Char, CHAR_HANDLER, comma);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, comma);
        this.addProduction(AttValue_Char, CHAR_HANDLER, comma);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, comma);
        // #x2D
        this.addProduction(Char, CHAR_HANDLER, hyphenMinus);
        this.addProduction(CharData_Single, CHAR_HANDLER, hyphenMinus);
        this.addProduction(PI_Char, CHAR_HANDLER, hyphenMinus);
        this.addProduction(CData_Char, CHAR_HANDLER, hyphenMinus);
        this.addProduction(NameChar, CHAR_HANDLER, hyphenMinus);
        this.addProduction(NonMNameChar, CHAR_HANDLER, hyphenMinus);
        this.addProduction(NonLNameChar, CHAR_HANDLER, hyphenMinus);
        this.addProduction(PubidChar, CHAR_HANDLER, hyphenMinus);
        this.addProduction(Ignore_Char, CHAR_HANDLER, hyphenMinus);
        this.addProduction(EncName_Char, CHAR_HANDLER, hyphenMinus);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, hyphenMinus);
        this.addProduction(AttValue_Char, CHAR_HANDLER, hyphenMinus);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, hyphenMinus);
        // #x2E
        this.addProduction(Char, CHAR_HANDLER, fullStop);
        this.addProduction(CharData_Single, CHAR_HANDLER, fullStop);
        this.addProduction(Comment_Char, CHAR_HANDLER, fullStop);
        this.addProduction(PI_Char, CHAR_HANDLER, fullStop);
        this.addProduction(CData_Char, CHAR_HANDLER, fullStop);
        this.addProduction(NameChar, CHAR_HANDLER, fullStop);
        this.addProduction(NonMNameChar, CHAR_HANDLER, fullStop);
        this.addProduction(NonLNameChar, CHAR_HANDLER, fullStop);
        this.addProduction(PubidChar, CHAR_HANDLER, fullStop);
        this.addProduction(Ignore_Char, CHAR_HANDLER, fullStop);
        this.addProduction(EncName_Char, CHAR_HANDLER, fullStop);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, fullStop);
        this.addProduction(AttValue_Char, CHAR_HANDLER, fullStop);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, fullStop);
        // #x2F
        this.addProduction(Char, CHAR_HANDLER, solidus);
        this.addProduction(CharData_Single, CHAR_HANDLER, solidus);
        this.addProduction(Comment_Char, CHAR_HANDLER, solidus);
        this.addProduction(PI_Char, CHAR_HANDLER, solidus);
        this.addProduction(CData_Char, CHAR_HANDLER, solidus);
        this.addProduction(PubidChar, CHAR_HANDLER, solidus);
        this.addProduction(Ignore_Char, CHAR_HANDLER, solidus);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, solidus);
        this.addProduction(AttValue_Char, CHAR_HANDLER, solidus);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, solidus);
        // #x30 - #x39
        for (final UnicodeTerminalSymbols s : Arrays.asList(digitZero, digitOne, digitTwo, digitThree, digitFour,
                digitFive, digitSix, digitSeven, digitEight, digitNine)) {
            this.addProduction(Char, CHAR_HANDLER, s);
            this.addProduction(CharData_Single, CHAR_HANDLER, s);
            this.addProduction(Comment_Char, CHAR_HANDLER, s);
            this.addProduction(PI_Char, CHAR_HANDLER, s);
            this.addProduction(CData_Char, CHAR_HANDLER, s);
            this.addProduction(NameChar, CHAR_HANDLER, s);
            this.addProduction(NonMNameChar, CHAR_HANDLER, s);
            this.addProduction(NonLNameChar, CHAR_HANDLER, s);
            this.addProduction(PubidChar, CHAR_HANDLER, s);
            this.addProduction(Ignore_Char, CHAR_HANDLER, s);
            this.addProduction(EncName_Char, CHAR_HANDLER, s);
            this.addProduction(DECIMAL_DIGIT, CHAR_HANDLER, s);
            this.addProduction(HEX_DIGIT, CHAR_HANDLER, s);
            this.addProduction(EntityValue_Char, CHAR_HANDLER, s);
            this.addProduction(AttValue_Char, CHAR_HANDLER, s);
            this.addProduction(SystemLiteral_Char, CHAR_HANDLER, s);
        }
        // #x3A
        this.addProduction(Char, CHAR_HANDLER, colon);
        this.addProduction(CharData_Single, CHAR_HANDLER, colon);
        this.addProduction(Comment_Char, CHAR_HANDLER, colon);
        this.addProduction(PI_Char, CHAR_HANDLER, colon);
        this.addProduction(CData_Char, CHAR_HANDLER, colon);
        this.addProduction(NameStartChar, CHAR_HANDLER, colon);
        this.addProduction(NonXNameStartChar, CHAR_HANDLER, colon);
        this.addProduction(NameChar, CHAR_HANDLER, colon);
        this.addProduction(NonMNameChar, CHAR_HANDLER, colon);
        this.addProduction(NonLNameChar, CHAR_HANDLER, colon);
        this.addProduction(PubidChar, CHAR_HANDLER, colon);
        this.addProduction(Ignore_Char, CHAR_HANDLER, colon);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, colon);
        this.addProduction(AttValue_Char, CHAR_HANDLER, colon);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, colon);
        // #x3B
        this.addProduction(Char, CHAR_HANDLER, semicolon);
        this.addProduction(CharData_Single, CHAR_HANDLER, semicolon);
        this.addProduction(Comment_Char, CHAR_HANDLER, semicolon);
        this.addProduction(PI_Char, CHAR_HANDLER, semicolon);
        this.addProduction(CData_Char, CHAR_HANDLER, semicolon);
        this.addProduction(PubidChar, CHAR_HANDLER, semicolon);
        this.addProduction(Ignore_Char, CHAR_HANDLER, semicolon);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, semicolon);
        this.addProduction(AttValue_Char, CHAR_HANDLER, semicolon);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, semicolon);
        // #x3C
        this.addProduction(Char, CHAR_HANDLER, lessThanSign);
        this.addProduction(Comment_Char, CHAR_HANDLER, lessThanSign);
        this.addProduction(PI_Char, CHAR_HANDLER, lessThanSign);
        this.addProduction(CData_Char, CHAR_HANDLER, lessThanSign);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, lessThanSign);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, lessThanSign);
        // #x3D
        this.addProduction(Char, CHAR_HANDLER, equalsSign);
        this.addProduction(CharData_Single, CHAR_HANDLER, equalsSign);
        this.addProduction(Comment_Char, CHAR_HANDLER, equalsSign);
        this.addProduction(PI_Char, CHAR_HANDLER, equalsSign);
        this.addProduction(CData_Char, CHAR_HANDLER, equalsSign);
        this.addProduction(PubidChar, CHAR_HANDLER, equalsSign);
        this.addProduction(Ignore_Char, CHAR_HANDLER, equalsSign);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, equalsSign);
        this.addProduction(AttValue_Char, CHAR_HANDLER, equalsSign);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, equalsSign);
        // #x3E
        this.addProduction(Char, CHAR_HANDLER, greaterThanSign);
        this.addProduction(Comment_Char, CHAR_HANDLER, greaterThanSign);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, greaterThanSign);
        this.addProduction(AttValue_Char, CHAR_HANDLER, greaterThanSign);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, greaterThanSign);
        // #x3F
        this.addProduction(Char, CHAR_HANDLER, questionMark);
        this.addProduction(CharData_Single, CHAR_HANDLER, questionMark);
        this.addProduction(Comment_Char, CHAR_HANDLER, questionMark);
        this.addProduction(CData_Char, CHAR_HANDLER, questionMark);
        this.addProduction(PubidChar, CHAR_HANDLER, questionMark);
        this.addProduction(Ignore_Char, CHAR_HANDLER, questionMark);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, questionMark);
        this.addProduction(AttValue_Char, CHAR_HANDLER, questionMark);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, questionMark);
        // #x40
        this.addProduction(Char, CHAR_HANDLER, atSymbol);
        this.addProduction(CharData_Single, CHAR_HANDLER, atSymbol);
        this.addProduction(Comment_Char, CHAR_HANDLER, atSymbol);
        this.addProduction(PI_Char, CHAR_HANDLER, atSymbol);
        this.addProduction(CData_Char, CHAR_HANDLER, atSymbol);
        this.addProduction(PubidChar, CHAR_HANDLER, atSymbol);
        this.addProduction(Ignore_Char, CHAR_HANDLER, atSymbol);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, atSymbol);
        this.addProduction(AttValue_Char, CHAR_HANDLER, atSymbol);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, atSymbol);
        // #x41 - #x46
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalA, capitalB, capitalC, capitalD,
                capitalE, capitalF }) {
            this.addProduction(Char, CHAR_HANDLER, s);
            this.addProduction(CharData_Single, CHAR_HANDLER, s);
            this.addProduction(Comment_Char, CHAR_HANDLER, s);
            this.addProduction(PI_Char, CHAR_HANDLER, s);
            this.addProduction(CData_Char, CHAR_HANDLER, s);
            this.addProduction(NameStartChar, CHAR_HANDLER, s);
            this.addProduction(NonXNameStartChar, CHAR_HANDLER, s);
            this.addProduction(NameChar, CHAR_HANDLER, s);
            this.addProduction(NonMNameChar, CHAR_HANDLER, s);
            this.addProduction(NonLNameChar, CHAR_HANDLER, s);
            this.addProduction(PubidChar, CHAR_HANDLER, s);
            this.addProduction(Ignore_Char, CHAR_HANDLER, s);
            this.addProduction(EncName_StartChar, CHAR_HANDLER, s);
            this.addProduction(EncName_Char, CHAR_HANDLER, s);
            this.addProduction(HEX_DIGIT, CHAR_HANDLER, s);
            this.addProduction(EntityValue_Char, CHAR_HANDLER, s);
            this.addProduction(AttValue_Char, CHAR_HANDLER, s);
            this.addProduction(SystemLiteral_Char, CHAR_HANDLER, s);
        }
        // #x47 - #x5A
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { capitalG, capitalH, capitalI, capitalJ,
                capitalK, capitalL, capitalM, capitalN, capitalO, capitalP, capitalQ, capitalR, capitalS, capitalT,
                capitalU, capitalV, capitalW, capitalX, capitalY, capitalZ }) {
            this.addProduction(Char, CHAR_HANDLER, s);
            this.addProduction(CharData_Single, CHAR_HANDLER, s);
            this.addProduction(Comment_Char, CHAR_HANDLER, s);
            this.addProduction(PI_Char, CHAR_HANDLER, s);
            this.addProduction(CData_Char, CHAR_HANDLER, s);
            this.addProduction(NameStartChar, CHAR_HANDLER, s);
            if (s != capitalX) {
                this.addProduction(NonXNameStartChar, CHAR_HANDLER, s);
            }
            this.addProduction(NameChar, CHAR_HANDLER, s);
            if (s != capitalM) {
                this.addProduction(NonMNameChar, CHAR_HANDLER, s);
            }
            if (s != capitalL) {
                this.addProduction(NonLNameChar, CHAR_HANDLER, s);
            }
            this.addProduction(PubidChar, CHAR_HANDLER, s);
            this.addProduction(Ignore_Char, CHAR_HANDLER, s);
            this.addProduction(EncName_StartChar, CHAR_HANDLER, s);
            this.addProduction(EncName_Char, CHAR_HANDLER, s);
            this.addProduction(EntityValue_Char, CHAR_HANDLER, s);
            this.addProduction(AttValue_Char, CHAR_HANDLER, s);
            this.addProduction(SystemLiteral_Char, CHAR_HANDLER, s);
        }
        // #x5B
        this.addProduction(Char, CHAR_HANDLER, leftBracket);
        this.addProduction(CharData_Single, CHAR_HANDLER, leftBracket);
        this.addProduction(Comment_Char, CHAR_HANDLER, leftBracket);
        this.addProduction(PI_Char, CHAR_HANDLER, leftBracket);
        this.addProduction(CData_Char, CHAR_HANDLER, leftBracket);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, leftBracket);
        this.addProduction(AttValue_Char, CHAR_HANDLER, leftBracket);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, leftBracket);
        // #x5C
        this.addProduction(Char, CHAR_HANDLER, reverseSolidus);
        this.addProduction(CharData_Single, CHAR_HANDLER, reverseSolidus);
        this.addProduction(Comment_Char, CHAR_HANDLER, reverseSolidus);
        this.addProduction(PI_Char, CHAR_HANDLER, reverseSolidus);
        this.addProduction(CData_Char, CHAR_HANDLER, reverseSolidus);
        this.addProduction(Ignore_Char, CHAR_HANDLER, reverseSolidus);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, reverseSolidus);
        this.addProduction(AttValue_Char, CHAR_HANDLER, reverseSolidus);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, reverseSolidus);
        // #x5D
        this.addProduction(Char, CHAR_HANDLER, rightBracket);
        this.addProduction(Comment_Char, CHAR_HANDLER, rightBracket);
        this.addProduction(PI_Char, CHAR_HANDLER, rightBracket);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, rightBracket);
        this.addProduction(AttValue_Char, CHAR_HANDLER, rightBracket);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, rightBracket);
        // #x5E
        this.addProduction(Char, CHAR_HANDLER, circumflexAccent);
        this.addProduction(CharData_Single, CHAR_HANDLER, circumflexAccent);
        this.addProduction(Comment_Char, CHAR_HANDLER, circumflexAccent);
        this.addProduction(PI_Char, CHAR_HANDLER, circumflexAccent);
        this.addProduction(CData_Char, CHAR_HANDLER, circumflexAccent);
        this.addProduction(Ignore_Char, CHAR_HANDLER, circumflexAccent);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, circumflexAccent);
        this.addProduction(AttValue_Char, CHAR_HANDLER, circumflexAccent);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, circumflexAccent);
        // #x5F
        this.addProduction(Char, CHAR_HANDLER, lowLine);
        this.addProduction(CharData_Single, CHAR_HANDLER, lowLine);
        this.addProduction(Comment_Char, CHAR_HANDLER, lowLine);
        this.addProduction(PI_Char, CHAR_HANDLER, lowLine);
        this.addProduction(CData_Char, CHAR_HANDLER, lowLine);
        this.addProduction(NameStartChar, CHAR_HANDLER, lowLine);
        this.addProduction(NonXNameStartChar, CHAR_HANDLER, lowLine);
        this.addProduction(NameChar, CHAR_HANDLER, lowLine);
        this.addProduction(NonMNameChar, CHAR_HANDLER, lowLine);
        this.addProduction(NonLNameChar, CHAR_HANDLER, lowLine);
        this.addProduction(PubidChar, CHAR_HANDLER, lowLine);
        this.addProduction(Ignore_Char, CHAR_HANDLER, lowLine);
        this.addProduction(EncName_Char, CHAR_HANDLER, lowLine);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, lowLine);
        this.addProduction(AttValue_Char, CHAR_HANDLER, lowLine);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, lowLine);
        // #x60
        this.addProduction(Char, CHAR_HANDLER, graveAccent);
        this.addProduction(CharData_Single, CHAR_HANDLER, graveAccent);
        this.addProduction(Comment_Char, CHAR_HANDLER, graveAccent);
        this.addProduction(PI_Char, CHAR_HANDLER, graveAccent);
        this.addProduction(CData_Char, CHAR_HANDLER, graveAccent);
        this.addProduction(Ignore_Char, CHAR_HANDLER, graveAccent);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, graveAccent);
        this.addProduction(AttValue_Char, CHAR_HANDLER, graveAccent);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, graveAccent);
        // #x61 - #x66
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallA, smallB, smallC, smallD, smallE,
                smallF }) {
            this.addProduction(Char, CHAR_HANDLER, s);
            this.addProduction(CharData_Single, CHAR_HANDLER, s);
            this.addProduction(Comment_Char, CHAR_HANDLER, s);
            this.addProduction(PI_Char, CHAR_HANDLER, s);
            this.addProduction(CData_Char, CHAR_HANDLER, s);
            this.addProduction(NameStartChar, CHAR_HANDLER, s);
            this.addProduction(NonXNameStartChar, CHAR_HANDLER, s);
            this.addProduction(NameChar, CHAR_HANDLER, s);
            this.addProduction(NonMNameChar, CHAR_HANDLER, s);
            this.addProduction(NonLNameChar, CHAR_HANDLER, s);
            this.addProduction(PubidChar, CHAR_HANDLER, s);
            this.addProduction(Ignore_Char, CHAR_HANDLER, s);
            this.addProduction(EncName_StartChar, CHAR_HANDLER, s);
            this.addProduction(EncName_Char, CHAR_HANDLER, s);
            this.addProduction(HEX_DIGIT, CHAR_HANDLER, s);
            this.addProduction(EntityValue_Char, CHAR_HANDLER, s);
            this.addProduction(AttValue_Char, CHAR_HANDLER, s);
            this.addProduction(SystemLiteral_Char, CHAR_HANDLER, s);
        }
        // #x67 - #x7A
        for (final UnicodeTerminalSymbols s : new UnicodeTerminalSymbols[] { smallG, smallH, smallI, smallJ, smallK,
                smallL, smallM, smallN, smallO, smallP, smallQ, smallR, smallS, smallT, smallU, smallV, smallW, smallX,
                smallY, smallZ }) {
            this.addProduction(Char, CHAR_HANDLER, s);
            this.addProduction(CharData_Single, CHAR_HANDLER, s);
            this.addProduction(Comment_Char, CHAR_HANDLER, s);
            this.addProduction(PI_Char, CHAR_HANDLER, s);
            this.addProduction(CData_Char, CHAR_HANDLER, s);
            this.addProduction(NameStartChar, CHAR_HANDLER, s);
            if (s != smallX) {
                this.addProduction(NonXNameStartChar, CHAR_HANDLER, s);
            }
            this.addProduction(NameChar, CHAR_HANDLER, s);
            if (s != smallM) {
                this.addProduction(NonMNameChar, CHAR_HANDLER, s);
            }
            if (s != smallL) {
                this.addProduction(NonLNameChar, CHAR_HANDLER, s);
            }
            this.addProduction(PubidChar, CHAR_HANDLER, s);
            this.addProduction(Ignore_Char, CHAR_HANDLER, s);
            this.addProduction(EncName_StartChar, CHAR_HANDLER, s);
            this.addProduction(EncName_Char, CHAR_HANDLER, s);
            this.addProduction(EntityValue_Char, CHAR_HANDLER, s);
            this.addProduction(AttValue_Char, CHAR_HANDLER, s);
            this.addProduction(SystemLiteral_Char, CHAR_HANDLER, s);
        }
        // #x7B
        this.addProduction(Char, CHAR_HANDLER, leftCurlyBrace);
        this.addProduction(CharData_Single, CHAR_HANDLER, leftCurlyBrace);
        this.addProduction(Comment_Char, CHAR_HANDLER, leftCurlyBrace);
        this.addProduction(PI_Char, CHAR_HANDLER, leftCurlyBrace);
        this.addProduction(CData_Char, CHAR_HANDLER, leftCurlyBrace);
        this.addProduction(Ignore_Char, CHAR_HANDLER, leftCurlyBrace);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, leftCurlyBrace);
        this.addProduction(AttValue_Char, CHAR_HANDLER, leftCurlyBrace);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, leftCurlyBrace);
        // #x7C
        this.addProduction(Char, CHAR_HANDLER, verticalBar);
        this.addProduction(CharData_Single, CHAR_HANDLER, verticalBar);
        this.addProduction(Comment_Char, CHAR_HANDLER, verticalBar);
        this.addProduction(PI_Char, CHAR_HANDLER, verticalBar);
        this.addProduction(CData_Char, CHAR_HANDLER, verticalBar);
        this.addProduction(Ignore_Char, CHAR_HANDLER, verticalBar);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, verticalBar);
        this.addProduction(AttValue_Char, CHAR_HANDLER, verticalBar);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, verticalBar);
        // #x7D
        this.addProduction(Char, CHAR_HANDLER, rightCurlyBrace);
        this.addProduction(CharData_Single, CHAR_HANDLER, rightCurlyBrace);
        this.addProduction(Comment_Char, CHAR_HANDLER, rightCurlyBrace);
        this.addProduction(PI_Char, CHAR_HANDLER, rightCurlyBrace);
        this.addProduction(CData_Char, CHAR_HANDLER, rightCurlyBrace);
        this.addProduction(Ignore_Char, CHAR_HANDLER, rightCurlyBrace);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, rightCurlyBrace);
        this.addProduction(AttValue_Char, CHAR_HANDLER, rightCurlyBrace);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, rightCurlyBrace);
        // #x7E
        this.addProduction(Char, CHAR_HANDLER, tilde);
        this.addProduction(CharData_Single, CHAR_HANDLER, tilde);
        this.addProduction(Comment_Char, CHAR_HANDLER, tilde);
        this.addProduction(PI_Char, CHAR_HANDLER, tilde);
        this.addProduction(CData_Char, CHAR_HANDLER, tilde);
        this.addProduction(Ignore_Char, CHAR_HANDLER, tilde);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, tilde);
        this.addProduction(AttValue_Char, CHAR_HANDLER, tilde);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, tilde);
        // #x7F
        this.addProduction(Char, CHAR_HANDLER, delete);
        this.addProduction(CharData_Single, CHAR_HANDLER, delete);
        this.addProduction(Comment_Char, CHAR_HANDLER, delete);
        this.addProduction(PI_Char, CHAR_HANDLER, delete);
        this.addProduction(CData_Char, CHAR_HANDLER, delete);
        this.addProduction(Ignore_Char, CHAR_HANDLER, delete);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, delete);
        this.addProduction(AttValue_Char, CHAR_HANDLER, delete);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, delete);
        // #x80 - #xD7FF
        this.addProduction(Char, CHAR_HANDLER, belowSurrogates);
        this.addProduction(CharData_Single, CHAR_HANDLER, belowSurrogates);
        this.addProduction(Comment_Char, CHAR_HANDLER, belowSurrogates);
        this.addProduction(PI_Char, CHAR_HANDLER, belowSurrogates);
        this.addProduction(CData_Char, CHAR_HANDLER, belowSurrogates);
        this.addProduction(Ignore_Char, CHAR_HANDLER, belowSurrogates);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, belowSurrogates);
        this.addProduction(AttValue_Char, CHAR_HANDLER, belowSurrogates);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, belowSurrogates);
        // #xE000 - #xFFFD
        this.addProduction(Char, CHAR_HANDLER, aboveSurrogates);
        this.addProduction(CharData_Single, CHAR_HANDLER, aboveSurrogates);
        this.addProduction(Comment_Char, CHAR_HANDLER, aboveSurrogates);
        this.addProduction(PI_Char, CHAR_HANDLER, aboveSurrogates);
        this.addProduction(CData_Char, CHAR_HANDLER, aboveSurrogates);
        this.addProduction(Ignore_Char, CHAR_HANDLER, aboveSurrogates);
        this.addProduction(EntityValue_Char, CHAR_HANDLER, aboveSurrogates);
        this.addProduction(AttValue_Char, CHAR_HANDLER, aboveSurrogates);
        this.addProduction(SystemLiteral_Char, CHAR_HANDLER, aboveSurrogates);
        // NameStartChar ::= [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] |
        // [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] |
        // [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD]
        // | [#x10000-#xEFFFF]
        // NameChar ::= #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
        // TODO: surrogate pairs
        this.addProduction(Char, CHAR_HANDLER, supplementaryMultilingualPlane);
        this.addProduction(Char, CHAR_HANDLER, supplementaryIdeographicPlane);
        this.addProduction(Char, CHAR_HANDLER, supplementaryUnassigned);
        this.addProduction(Char, CHAR_HANDLER, supplementarySpecialPurposePlane);
        this.addProduction(Char, CHAR_HANDLER, supplementaryPrivateUsePlanes);
        
        // document ::= prolog element Misc*
        // prolog ::= XMLDecl? Misc* (doctypedecl Misc*)?
        // document ::= XMLDecl? Misc* doctypedecl? Misc* element Misc*
        this.addProduction(document, HANDLER, XMLDecl, doctypedecl_container, element_container, Misc_Plus, S);
        this.addProduction(document, HANDLER, XMLDecl, doctypedecl_container, element_container, Misc_Plus);
        this.addProduction(document, HANDLER, XMLDecl, doctypedecl_container, element_container, S);
        this.addProduction(document, HANDLER, XMLDecl, doctypedecl_container, element_container);
        this.addProduction(document, HANDLER, XMLDecl, element_container, Misc_Plus, S);
        this.addProduction(document, HANDLER, XMLDecl, element_container, Misc_Plus);
        this.addProduction(document, HANDLER, XMLDecl, element_container, S);
        this.addProduction(document, HANDLER, XMLDecl, element_container);
        this.addProduction(document, HANDLER, doctypedecl_container, element_container, Misc_Plus, S);
        this.addProduction(document, HANDLER, doctypedecl_container, element_container, Misc_Plus);
        this.addProduction(document, HANDLER, doctypedecl_container, element_container, S);
        this.addProduction(document, HANDLER, doctypedecl_container, element_container);
        this.addProduction(document, HANDLER, element_container, Misc_Plus, S);
        this.addProduction(document, HANDLER, element_container, Misc_Plus);
        this.addProduction(document, HANDLER, element_container, S);
        this.addProduction(document, HANDLER, element_container);
        // XMLDecl ::= '<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'
//		this.addProduction(XMLDecl, lessThanSign, questionMark, lowercaseXML, VersionInfo, EncodingDecl, SDDecl, S, questionMark, greaterThanSign);
        this.addProduction(XMLDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo,
                EncodingDecl, SDDecl, S, questionMark, greaterThanSign);
        this.addProduction(XMLDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, SDDecl, S,
                questionMark, greaterThanSign);
        this.addProduction(XMLDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo,
                EncodingDecl, S, questionMark, greaterThanSign);
        this.addProduction(XMLDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, S,
                questionMark, greaterThanSign);
        this.addProduction(XMLDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo,
                EncodingDecl, SDDecl, questionMark, greaterThanSign);
        this.addProduction(XMLDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, SDDecl,
                questionMark, greaterThanSign);
        this.addProduction(XMLDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo,
                EncodingDecl, questionMark, greaterThanSign);
        this.addProduction(XMLDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo,
                questionMark, greaterThanSign);
        // VersionInfo ::= S 'version' Eq ("'" VersionNum "'" | '"' VersionNum
        // '"')
        this.addProduction(VersionInfo, HANDLER, S, smallV, smallE, smallR, smallS, smallI, smallO, smallN, Eq,
                apostrophe, VersionNum, apostrophe);
        this.addProduction(VersionInfo, HANDLER, S, smallV, smallE, smallR, smallS, smallI, smallO, smallN, Eq,
                quotationMark, VersionNum, quotationMark);
        // S ::= (#x20 | #x9 | #xD | #xA)+
        this.addProduction(S, STRING_CONCAT, WHITE_SPACE);
        this.addProduction(S, STRING_CONCAT, S, WHITE_SPACE);
        // Eq ::= S? '=' S?
        this.addProduction(Eq, STRING_CONCAT, S, equalsSign, S);
        this.addProduction(Eq, STRING_CONCAT, S, equalsSign);
        this.addProduction(Eq, STRING_CONCAT, equalsSign, S);
        this.addProduction(Eq, STRING_CONCAT, equalsSign);
        // VersionNum ::= '1.' [0-9]+
        this.addProduction(VersionNum, STRING_CONCAT, digitOne, fullStop, DECIMAL_DIGIT_Plus);
        this.addProduction(DECIMAL_DIGIT_Plus, STRING_CONCAT, DECIMAL_DIGIT_Plus, DECIMAL_DIGIT);
        this.addProduction(DECIMAL_DIGIT_Plus, STRING_CONCAT, DECIMAL_DIGIT);
        // EncodingDecl ::= S 'encoding' Eq ('"' EncName '"' | "'" EncName "'" )
        this.addProduction(EncodingDecl, HANDLER, S, smallE, smallN, smallC, smallO, smallD, smallI, smallN, smallG, Eq,
                quotationMark, EncName, quotationMark);
        this.addProduction(EncodingDecl, HANDLER, S, smallE, smallN, smallC, smallO, smallD, smallI, smallN, smallG, Eq,
                apostrophe, EncName, apostrophe);
        // EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*
        this.addProduction(EncName, STRING_CONCAT, EncName_StartChar, EncName_Char_Plus);
        this.addProduction(EncName, STRING_CONCAT, EncName_StartChar);
        this.addProduction(EncName_Char_Plus, STRING_CONCAT, EncName_Char_Plus, EncName_Char);
        this.addProduction(EncName_Char_Plus, STRING_CONCAT, EncName_Char);
        // SDDecl ::= S 'standalone' Eq (("'" ('yes' | 'no') "'") | ('"' ('yes'
        // | 'no') '"'))
        this.addProduction(SDDecl, HANDLER, S, smallS, smallT, smallA, smallN, smallD, smallA, smallL, smallO, smallN,
                smallE, Eq, apostrophe, smallY, smallE, smallS, apostrophe);
        this.addProduction(SDDecl, HANDLER, S, smallS, smallT, smallA, smallN, smallD, smallA, smallL, smallO, smallN,
                smallE, Eq, apostrophe, smallN, smallO, apostrophe);
        this.addProduction(SDDecl, HANDLER, S, smallS, smallT, smallA, smallN, smallD, smallA, smallL, smallO, smallN,
                smallE, Eq, quotationMark, smallY, smallE, smallS, quotationMark);
        this.addProduction(SDDecl, HANDLER, S, smallS, smallT, smallA, smallN, smallD, smallA, smallL, smallO, smallN,
                smallE, Eq, quotationMark, smallN, smallO, quotationMark);
        // Misc ::= Comment | PI | S
        this.addProduction(Misc, HANDLER, Comment);
        this.addProduction(Misc, HANDLER, PI);
        this.addProduction(Misc_Plus, HANDLER, Misc_Plus, S, Misc);
        this.addProduction(Misc_Plus, HANDLER, Misc_Plus, Misc);
        this.addProduction(Misc_Plus, HANDLER, S, Misc);
        this.addProduction(Misc_Plus, HANDLER, Misc);
        // Comment ::= '<!--' ((Char - '-') | ('-' (Char - '-')))* '-->'
        this.addProduction(Comment, HANDLER, lessThanSign, exclamationMark, hyphenMinus, hyphenMinus, Comment_span_Plus,
                hyphenMinus, greaterThanSign);
        this.addProduction(Comment, HANDLER, lessThanSign, exclamationMark, hyphenMinus, hyphenMinus, hyphenMinus,
                hyphenMinus, greaterThanSign);
        this.addProduction(Comment_span_Plus, STRING_CONCAT, Comment_Char_Plus, hyphenMinus, Comment_span_Plus);
        this.addProduction(Comment_span_Plus, STRING_CONCAT, Comment_Char_Plus, hyphenMinus);
        this.addProduction(Comment_Char_Plus, STRING_CONCAT, Comment_Char_Plus, Comment_Char);
        this.addProduction(Comment_Char_Plus, STRING_CONCAT, Comment_Char);
        // PI ::= '<?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'
        this.addProduction(PI, HANDLER, lessThanSign, questionMark, PITarget, S, PI_inner, questionMark,
                greaterThanSign);
        this.addProduction(PI, HANDLER, lessThanSign, questionMark, PITarget, S, questionMark, greaterThanSign);
        this.addProduction(PI, HANDLER, lessThanSign, questionMark, PITarget, questionMark, greaterThanSign);
        this.addProduction(PI_inner, HANDLER, PI_Char_Plus, S, PI_inner);
        this.addProduction(PI_inner, HANDLER, PI_Char_Plus);
        // PI_Char_Plus will never end with a question mark except when it ends
        // completely
        // it may end with a greater than sign
        this.addProduction(PI_Char_Plus, HANDLER, PI_Char_Plus, PI_Char);
//		this.addProduction(PI_Char_Plus, PI_Char_Plus, literalCharacterQuestionMark, PI_Char);
        this.addProduction(PI_Char_Plus, HANDLER, PI_Char_Plus, greaterThanSign);
        this.addProduction(PI_Char_Plus, HANDLER, PI_Char);
//		this.addProduction(PI_Char_Plus, literalCharacterQuestionMark, PI_Char);
        this.addProduction(PI_Char_Plus, HANDLER, greaterThanSign);
        // PITarget ::= Name - (('X' | 'x') ('M' | 'm') ('L' | 'l'))
        // PITarget ~= xm*
        this.addProduction(PITarget, HANDLER, capitalX, capitalM, NonLNameChar, NameChar_Plus);
        this.addProduction(PITarget, HANDLER, capitalX, smallM, NonLNameChar, NameChar_Plus);
        this.addProduction(PITarget, HANDLER, smallX, capitalM, NonLNameChar, NameChar_Plus);
        this.addProduction(PITarget, HANDLER, smallX, smallM, NonLNameChar, NameChar_Plus);
        this.addProduction(PITarget, HANDLER, capitalX, capitalM, NonLNameChar);
        this.addProduction(PITarget, HANDLER, capitalX, smallM, NonLNameChar);
        this.addProduction(PITarget, HANDLER, smallX, capitalM, NonLNameChar);
        this.addProduction(PITarget, HANDLER, smallX, smallM, NonLNameChar);
        // PITarget ~= x*
        this.addProduction(PITarget, HANDLER, capitalX, NonMNameChar, NameChar_Plus);
        this.addProduction(PITarget, HANDLER, smallX, NonMNameChar, NameChar_Plus);
        this.addProduction(PITarget, HANDLER, capitalX, NonMNameChar);
        this.addProduction(PITarget, HANDLER, smallX, NonMNameChar);
        // PITarget ~= [^x].*
        this.addProduction(PITarget, HANDLER, NonXNameStartChar);
        this.addProduction(PITarget, HANDLER, NonXNameStartChar, NameChar_Plus);
        // Name ::= NameStartChar (NameChar)*
        this.addProduction(Name, STRING_CONCAT, NameStartChar, NameChar_Plus);
        this.addProduction(Name, STRING_CONCAT, NameStartChar);
        // NameChar* ::= epsilon | NameChar NameChar*
        this.addProduction(NameChar_Plus, STRING_CONCAT, NameChar_Plus, NameChar);
        this.addProduction(NameChar_Plus, STRING_CONCAT, NameChar);
        // doctypedecl ::= '<!DOCTYPE' S Name (S ExternalID)? S? ('[' intSubset
        // ']' S?)? '>'
        this.addProduction(doctypedecl_container, HANDLER, S, Misc, doctypedecl_container);
        this.addProduction(doctypedecl_container, HANDLER, Misc, doctypedecl_container);
        this.addProduction(doctypedecl_container, HANDLER, S, doctypedecl);
        this.addProduction(doctypedecl_container, HANDLER, doctypedecl);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, S, ExternalID, S, leftBracket, intSubset, rightBracket, S,
                greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, S, ExternalID, S, leftBracket, intSubset, rightBracket,
                greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, S, ExternalID, S, greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, S, ExternalID, leftBracket, intSubset, rightBracket, S,
                greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, S, ExternalID, leftBracket, intSubset, rightBracket,
                greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, S, ExternalID, greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, S, leftBracket, intSubset, rightBracket, S, greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, S, leftBracket, intSubset, rightBracket, greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, S, greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, leftBracket, intSubset, rightBracket, S, greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, leftBracket, intSubset, rightBracket, greaterThanSign);
        this.addProduction(doctypedecl, HANDLER, lessThanSign, exclamationMark, capitalD, capitalO, capitalC, capitalT,
                capitalY, capitalP, capitalE, S, Name, greaterThanSign);
        // ExternalID ::= 'SYSTEM' S SystemLiteral | 'PUBLIC' S PubidLiteral S
        // SystemLiteral
        this.addProduction(ExternalID, new ProductionHandler() {
            
            @Override
            public Object handleReduction(final List<Object> rightHandSide) {
                publicID = rightHandSide.get(0).toString();
                return publicID;
            }
        }, PublicID, S, SystemLiteral);
        this.addProduction(ExternalID, HANDLER, capitalS, capitalY, capitalS, capitalT, capitalE, capitalM, S,
                SystemLiteral);
        // PubidLiteral ::= '"' PubidChar* '"' | "'" (PubidChar - "'")* "'"
        this.addProduction(PubidLiteral, HANDLER, quotationMark, QuotedPubidChar_Plus, quotationMark);
        this.addProduction(PubidLiteral, HANDLER, quotationMark, quotationMark);
        this.addProduction(PubidLiteral, HANDLER, apostrophe, PubidChar_Plus, apostrophe);
        this.addProduction(PubidLiteral, HANDLER, apostrophe, apostrophe);
        this.addProduction(QuotedPubidChar_Plus, STRING_CONCAT, PubidChar);
        this.addProduction(QuotedPubidChar_Plus, STRING_CONCAT, apostrophe);
        this.addProduction(QuotedPubidChar_Plus, STRING_CONCAT, QuotedPubidChar_Plus, PubidChar);
        this.addProduction(QuotedPubidChar_Plus, STRING_CONCAT, QuotedPubidChar_Plus, apostrophe);
        this.addProduction(PubidChar_Plus, STRING_CONCAT, PubidChar);
        this.addProduction(PubidChar_Plus, STRING_CONCAT, PubidChar_Plus, PubidChar);
        // SystemLiteral ::= ('"' [^"]* '"') | ("'" [^']* "'")
        this.addProduction(SystemLiteral, new SystemLiteralHandler(), quotationMark, SystemLiteral_inner_Plus,
                quotationMark);
        this.addProduction(SystemLiteral, HANDLER, quotationMark, quotationMark);
        this.addProduction(SystemLiteral, new SystemLiteralHandler(), apostrophe, SystemLiteral_inner_Plus, apostrophe);
        this.addProduction(SystemLiteral, HANDLER, apostrophe, apostrophe);
        this.addProduction(SystemLiteral_inner_Plus, STRING_CONCAT, SystemLiteral_inner_Plus, SystemLiteral_Char);
        this.addProduction(SystemLiteral_inner_Plus, STRING_CONCAT, SystemLiteral_Char);
        // intSubset ::= (markupdecl | DeclSep)*
        // markupdecl ::= elementdecl | AttlistDecl | EntityDecl | NotationDecl
        // | PI | Comment
        // DeclSep ::= PEReference | S
        // intSubset ::= (elementdecl | AttlistDecl | EntityDecl | NotationDecl
        // | PI | Comment | PEReference | S)*
        this.addProduction(intSubset, HANDLER, S, intSubset_inner_Plus);
        this.addProduction(intSubset, HANDLER, intSubset_inner_Plus);
        this.addProduction(intSubset, HANDLER, S);
        this.addProduction(intSubset, HANDLER, epsilon);
        this.addProduction(intSubset_inner_Plus, HANDLER, intSubset_inner, intSubset_inner_Plus);
        this.addProduction(intSubset_inner_Plus, HANDLER, intSubset_inner);
        this.addProduction(intSubset_inner, HANDLER, markupdecl, S);
        this.addProduction(intSubset_inner, HANDLER, markupdecl);
        this.addProduction(intSubset_inner, HANDLER, DeclSep, S);
        this.addProduction(intSubset_inner, HANDLER, DeclSep);
        // markupdecl ::= elementdecl | AttlistDecl | EntityDecl | NotationDecl
        // | PI | Comment
        this.addProduction(markupdecl, HANDLER, elementdecl);
        this.addProduction(markupdecl, HANDLER, AttlistDecl);
        this.addProduction(markupdecl, HANDLER, EntityDecl);
        this.addProduction(markupdecl, HANDLER, NotationDecl);
        this.addProduction(markupdecl, HANDLER, PI);
        this.addProduction(markupdecl, HANDLER, Comment);
        // elementdecl ::= '<!ELEMENT' S Name S contentspec S? '>'
        this.addProduction(elementdecl, HANDLER, lessThanSign, exclamationMark, capitalE, capitalL, capitalE, capitalM,
                capitalE, capitalN, capitalT, S, Name, S, contentspec, S, greaterThanSign);
        this.addProduction(elementdecl, HANDLER, lessThanSign, exclamationMark, capitalE, capitalL, capitalE, capitalM,
                capitalE, capitalN, capitalT, S, Name, S, contentspec, greaterThanSign);
        // contentspec ::= 'EMPTY' | 'ANY' | Mixed | children
        this.addProduction(contentspec, HANDLER, capitalE, capitalM, capitalP, capitalT, capitalY);
        this.addProduction(contentspec, HANDLER, capitalA, capitalN, capitalY);
        this.addProduction(contentspec, HANDLER, Mixed);
        this.addProduction(contentspec, HANDLER, children);
        // Mixed ::= '(' S? '#PCDATA' S? ('|' S? Name S?)* ')*'
        // Mixed ::= '(' S? '#PCDATA' S? ')'
        this.addProduction(Mixed, HANDLER, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, S, Mixed_inner_Plus, rightParenthesis, asterisk);
        this.addProduction(Mixed, HANDLER, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, S, rightParenthesis, asterisk);
        this.addProduction(Mixed, HANDLER, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, Mixed_inner_Plus, rightParenthesis, asterisk);
        this.addProduction(Mixed, HANDLER, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, rightParenthesis, asterisk);
        this.addProduction(Mixed, HANDLER, leftParenthesis, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, S, Mixed_inner_Plus, rightParenthesis, asterisk);
        this.addProduction(Mixed, HANDLER, leftParenthesis, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, S, rightParenthesis, asterisk);
        this.addProduction(Mixed, HANDLER, leftParenthesis, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, Mixed_inner_Plus, rightParenthesis, asterisk);
        this.addProduction(Mixed, HANDLER, leftParenthesis, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, rightParenthesis, asterisk);
        this.addProduction(Mixed, HANDLER, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, S, rightParenthesis);
        this.addProduction(Mixed, HANDLER, leftParenthesis, S, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, rightParenthesis);
        this.addProduction(Mixed, HANDLER, leftParenthesis, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, S, rightParenthesis);
        this.addProduction(Mixed, HANDLER, leftParenthesis, numberSign, capitalP, capitalC, capitalD, capitalA,
                capitalT, capitalA, rightParenthesis);
        this.addProduction(Mixed_inner_Plus, HANDLER, Mixed_inner, Mixed_inner_Plus);
        this.addProduction(Mixed_inner_Plus, HANDLER, Mixed_inner);
        this.addProduction(Mixed_inner, HANDLER, verticalBar, S, Name, S);
        this.addProduction(Mixed_inner, HANDLER, verticalBar, Name, S);
        this.addProduction(Mixed_inner, HANDLER, verticalBar, S, Name);
        this.addProduction(Mixed_inner, HANDLER, verticalBar, Name);
        // children ::= (choice | seq) ('?' | '*' | '+')?
        this.addProduction(children, HANDLER, choice, questionMark);
        this.addProduction(children, HANDLER, choice, asterisk);
        this.addProduction(children, HANDLER, choice, plusSign);
        this.addProduction(children, HANDLER, choice);
        this.addProduction(children, HANDLER, seq, questionMark);
        this.addProduction(children, HANDLER, seq, asterisk);
        this.addProduction(children, HANDLER, seq, plusSign);
        this.addProduction(children, HANDLER, seq);
        // choice ::= '(' S? cp S? ('|' S? cp S?)+ ')'
        this.addProduction(choice, HANDLER, leftParenthesis, S, cp, S, choice_inner_Plus, rightParenthesis);
        this.addProduction(choice, HANDLER, leftParenthesis, cp, S, choice_inner_Plus, rightParenthesis);
        this.addProduction(choice, HANDLER, leftParenthesis, S, cp, choice_inner_Plus, rightParenthesis);
        this.addProduction(choice, HANDLER, leftParenthesis, cp, choice_inner_Plus, rightParenthesis);
        this.addProduction(choice_inner_Plus, HANDLER, choice_inner, choice_inner_Plus);
        this.addProduction(choice_inner_Plus, HANDLER, choice_inner);
        this.addProduction(choice_inner, HANDLER, verticalBar, S, cp, S);
        this.addProduction(choice_inner, HANDLER, verticalBar, cp, S);
        this.addProduction(choice_inner, HANDLER, verticalBar, S, cp);
        this.addProduction(choice_inner, HANDLER, verticalBar, cp);
        // cp ::= (Name | choice | seq) ('?' | '*' | '+')?
        this.addProduction(cp, HANDLER, Name, questionMark);
        this.addProduction(cp, HANDLER, Name, asterisk);
        this.addProduction(cp, HANDLER, Name, plusSign);
        this.addProduction(cp, HANDLER, Name);
        this.addProduction(cp, HANDLER, choice, questionMark);
        this.addProduction(cp, HANDLER, choice, asterisk);
        this.addProduction(cp, HANDLER, choice, plusSign);
        this.addProduction(cp, HANDLER, choice);
        this.addProduction(cp, HANDLER, seq, questionMark);
        this.addProduction(cp, HANDLER, seq, asterisk);
        this.addProduction(cp, HANDLER, seq, plusSign);
        this.addProduction(cp, HANDLER, seq);
        // seq ::= '(' S? cp S? (',' S? cp S?)* ')'
        this.addProduction(seq, HANDLER, leftParenthesis, S, cp, S, seq_inner_Plus, rightParenthesis);
        this.addProduction(seq, HANDLER, leftParenthesis, cp, S, seq_inner_Plus, rightParenthesis);
        this.addProduction(seq, HANDLER, leftParenthesis, S, cp, seq_inner_Plus, rightParenthesis);
        this.addProduction(seq, HANDLER, leftParenthesis, cp, seq_inner_Plus, rightParenthesis);
        this.addProduction(seq, HANDLER, leftParenthesis, S, cp, S, rightParenthesis);
        this.addProduction(seq, HANDLER, leftParenthesis, cp, S, rightParenthesis);
        this.addProduction(seq, HANDLER, leftParenthesis, S, cp, rightParenthesis);
        this.addProduction(seq, HANDLER, leftParenthesis, cp, rightParenthesis);
        this.addProduction(seq_inner_Plus, HANDLER, seq_inner, seq_inner_Plus);
        this.addProduction(seq_inner_Plus, HANDLER, seq_inner);
        this.addProduction(seq_inner, HANDLER, comma, S, cp, S);
        this.addProduction(seq_inner, HANDLER, comma, cp, S);
        this.addProduction(seq_inner, HANDLER, comma, S, cp);
        this.addProduction(seq_inner, HANDLER, comma, cp);
        // AttlistDecl ::= '<!ATTLIST' S Name AttDef* S? '>'
        this.addProduction(AttlistDecl, HANDLER, lessThanSign, exclamationMark, capitalA, capitalT, capitalT, capitalL,
                capitalI, capitalS, capitalT, S, Name, S, AttDef_Plus, greaterThanSign);
        this.addProduction(AttlistDecl, HANDLER, lessThanSign, exclamationMark, capitalA, capitalT, capitalT, capitalL,
                capitalI, capitalS, capitalT, S, Name, S, greaterThanSign);
        this.addProduction(AttlistDecl, HANDLER, lessThanSign, exclamationMark, capitalA, capitalT, capitalT, capitalL,
                capitalI, capitalS, capitalT, S, Name, greaterThanSign);
        this.addProduction(AttDef_Plus, HANDLER, AttDef, S, AttDef_Plus);
        this.addProduction(AttDef_Plus, HANDLER, AttDef, S);
        this.addProduction(AttDef_Plus, HANDLER, AttDef);
        // AttDef ::= S Name S AttType S DefaultDecl
        this.addProduction(AttDef, HANDLER, Name, S, AttType, S, DefaultDecl);
        // AttType ::= StringType | TokenizedType | EnumeratedType
        this.addProduction(AttType, HANDLER, StringType);
        this.addProduction(AttType, HANDLER, TokenizedType);
        this.addProduction(AttType, HANDLER, EnumeratedType);
        // StringType ::= 'CDATA'
        this.addProduction(StringType, HANDLER, capitalC, capitalD, capitalA, capitalT, capitalA);
        // TokenizedType ::= 'ID' | 'IDREF' | 'IDREFS' | 'ENTITY' | 'ENTITIES' |
        // 'NMTOKEN' | 'NMTOKENS'
        this.addProduction(TokenizedType, HANDLER, capitalI, capitalD);
        this.addProduction(TokenizedType, HANDLER, capitalI, capitalD, capitalR, capitalE, capitalF);
        this.addProduction(TokenizedType, HANDLER, capitalI, capitalD, capitalR, capitalE, capitalF, capitalS);
        this.addProduction(TokenizedType, HANDLER, capitalE, capitalN, capitalT, capitalI, capitalT, capitalY);
        this.addProduction(TokenizedType, HANDLER, capitalE, capitalN, capitalT, capitalI, capitalT, capitalI, capitalE,
                capitalS);
        this.addProduction(TokenizedType, HANDLER, capitalN, capitalM, capitalT, capitalO, capitalK, capitalE,
                capitalN);
        this.addProduction(TokenizedType, HANDLER, capitalN, capitalM, capitalT, capitalO, capitalK, capitalE, capitalN,
                capitalS);
        // EnumeratedType ::= NotationType | Enumeration
        this.addProduction(EnumeratedType, HANDLER, NotationType);
        this.addProduction(EnumeratedType, HANDLER, Enumeration);
        // NotationType ::= 'NOTATION' S '(' S? Name S? ('|' S? Name S?)* ')'
        this.addProduction(NotationType, HANDLER, capitalN, capitalO, capitalT, capitalA, capitalT, capitalI, capitalO,
                capitalN, S, leftParenthesis, S, Name, S, NotationType_inner_Plus, rightParenthesis);
        this.addProduction(NotationType, HANDLER, capitalN, capitalO, capitalT, capitalA, capitalT, capitalI, capitalO,
                capitalN, S, leftParenthesis, Name, S, NotationType_inner_Plus, rightParenthesis);
        this.addProduction(NotationType, HANDLER, capitalN, capitalO, capitalT, capitalA, capitalT, capitalI, capitalO,
                capitalN, S, leftParenthesis, S, Name, NotationType_inner_Plus, rightParenthesis);
        this.addProduction(NotationType, HANDLER, capitalN, capitalO, capitalT, capitalA, capitalT, capitalI, capitalO,
                capitalN, S, leftParenthesis, Name, NotationType_inner_Plus, rightParenthesis);
        this.addProduction(NotationType, HANDLER, capitalN, capitalO, capitalT, capitalA, capitalT, capitalI, capitalO,
                capitalN, S, leftParenthesis, S, Name, S, rightParenthesis);
        this.addProduction(NotationType, HANDLER, capitalN, capitalO, capitalT, capitalA, capitalT, capitalI, capitalO,
                capitalN, S, leftParenthesis, Name, S, rightParenthesis);
        this.addProduction(NotationType, HANDLER, capitalN, capitalO, capitalT, capitalA, capitalT, capitalI, capitalO,
                capitalN, S, leftParenthesis, S, Name, rightParenthesis);
        this.addProduction(NotationType, HANDLER, capitalN, capitalO, capitalT, capitalA, capitalT, capitalI, capitalO,
                capitalN, S, leftParenthesis, Name, rightParenthesis);
        this.addProduction(NotationType_inner_Plus, HANDLER, NotationType_inner, NotationType_inner_Plus);
        this.addProduction(NotationType_inner_Plus, HANDLER, NotationType_inner);
        this.addProduction(NotationType_inner, HANDLER, verticalBar, S, Name, S);
        this.addProduction(NotationType_inner, HANDLER, verticalBar, Name, S);
        this.addProduction(NotationType_inner, HANDLER, verticalBar, S, Name);
        this.addProduction(NotationType_inner, HANDLER, verticalBar, Name);
        // Enumeration ::= '(' S? Nmtoken (S? '|' S? Nmtoken)* S? ')'
        // Enumeration ::= '(' S? Nmtoken S? ('|' S? Nmtoken S?)* ')'
        this.addProduction(Enumeration, HANDLER, leftParenthesis, S, Nmtoken, S, Enumeration_inner_Plus,
                rightParenthesis);
        this.addProduction(Enumeration, HANDLER, leftParenthesis, Nmtoken, S, Enumeration_inner_Plus, rightParenthesis);
        this.addProduction(Enumeration, HANDLER, leftParenthesis, S, Nmtoken, Enumeration_inner_Plus, rightParenthesis);
        this.addProduction(Enumeration, HANDLER, leftParenthesis, Nmtoken, Enumeration_inner_Plus, rightParenthesis);
        this.addProduction(Enumeration, HANDLER, leftParenthesis, S, Nmtoken, S, rightParenthesis);
        this.addProduction(Enumeration, HANDLER, leftParenthesis, Nmtoken, S, rightParenthesis);
        this.addProduction(Enumeration, HANDLER, leftParenthesis, S, Nmtoken, rightParenthesis);
        this.addProduction(Enumeration, HANDLER, leftParenthesis, Nmtoken, rightParenthesis);
        this.addProduction(Enumeration_inner_Plus, HANDLER, Enumeration_inner, Enumeration_inner_Plus);
        this.addProduction(Enumeration_inner_Plus, HANDLER, Enumeration_inner);
        this.addProduction(Enumeration_inner, HANDLER, verticalBar, S, Nmtoken, S);
        this.addProduction(Enumeration_inner, HANDLER, verticalBar, Nmtoken, S);
        this.addProduction(Enumeration_inner, HANDLER, verticalBar, S, Nmtoken);
        this.addProduction(Enumeration_inner, HANDLER, verticalBar, Nmtoken);
        // Nmtoken ::= (NameChar)+
        this.addProduction(Nmtoken, HANDLER, NameChar_Plus);
        // DefaultDecl ::= '#REQUIRED' | '#IMPLIED' | (('#FIXED' S)? AttValue)
        this.addProduction(DefaultDecl, HANDLER, numberSign, capitalR, capitalE, capitalQ, capitalU, capitalI, capitalR,
                capitalE, capitalD);
        this.addProduction(DefaultDecl, HANDLER, numberSign, capitalI, capitalM, capitalP, capitalL, capitalI, capitalE,
                capitalD);
        this.addProduction(DefaultDecl, HANDLER, numberSign, capitalF, capitalI, capitalX, capitalE, capitalD, S,
                AttValue);
        this.addProduction(DefaultDecl, HANDLER, AttValue);
        // AttValue ::= '"' ([^<&"] | Reference)* '"' | "'" ([^<&'] |
        // Reference)* "'"
        this.addProduction(AttValue, HANDLER, quotationMark, AttValue_inner_Plus, quotationMark);
        this.addProduction(AttValue, HANDLER, quotationMark, quotationMark);
        this.addProduction(AttValue, HANDLER, apostrophe, AttValue_inner_Plus, apostrophe);
        this.addProduction(AttValue, HANDLER, apostrophe, apostrophe);
        this.addProduction(AttValue_inner_Plus, STRING_CONCAT, AttValue_inner, AttValue_inner_Plus);
        this.addProduction(AttValue_inner_Plus, STRING_CONCAT, AttValue_inner);
        this.addProduction(AttValue_inner, STRING_CONCAT, AttValue_Char);
        this.addProduction(AttValue_inner, STRING_CONCAT, Reference);
        // Reference ::= EntityRef | CharRef
        this.addProduction(Reference, HANDLER, EntityRef);
        this.addProduction(Reference, HANDLER, CharRef);
        // EntityRef ::= '&' Name ';'
        this.addProduction(EntityRef, STRING_CONCAT, ampersand, Name, semicolon);
        // CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'
        this.addProduction(CharRef, STRING_CONCAT, ampersand, numberSign, DECIMAL_DIGIT_Plus, semicolon);
        this.addProduction(CharRef, STRING_CONCAT, ampersand, numberSign, smallX, HEX_DIGIT_Plus, semicolon);
        this.addProduction(HEX_DIGIT_Plus, STRING_CONCAT, HEX_DIGIT, HEX_DIGIT_Plus);
        this.addProduction(HEX_DIGIT_Plus, STRING_CONCAT, HEX_DIGIT);
        // EntityDecl ::= GEDecl | PEDecl
        this.addProduction(EntityDecl, HANDLER, GEDecl);
        this.addProduction(EntityDecl, HANDLER, PEDecl);
        // GEDecl ::= '<!ENTITY' S Name S EntityDef S? '>'
        this.addProduction(GEDecl, HANDLER, lessThanSign, exclamationMark, capitalE, capitalN, capitalT, capitalI,
                capitalT, capitalY, S, Name, S, EntityDef, greaterThanSign);
        // EntityDef ::= EntityValue | (ExternalID NDataDecl?)
        this.addProduction(EntityDef, HANDLER, EntityValue, S);
        this.addProduction(EntityDef, HANDLER, EntityValue);
        this.addProduction(EntityDef, HANDLER, ExternalID, S, NDataDecl, S);
        this.addProduction(EntityDef, HANDLER, ExternalID, S, NDataDecl);
        this.addProduction(EntityDef, HANDLER, ExternalID, S);
        this.addProduction(EntityDef, HANDLER, ExternalID);
        // EntityValue ::= '"' ([^%&"] | PEReference | Reference)* '"' | "'"
        // ([^%&'] | PEReference | Reference)* "'"
        this.addProduction(EntityValue, HANDLER, quotationMark, EntityValue_inner_Plus, quotationMark);
        this.addProduction(EntityValue, HANDLER, apostrophe, EntityValue_inner_Plus, apostrophe);
        this.addProduction(EntityValue_inner_Plus, HANDLER, EntityValue_inner, EntityValue_inner_Plus);
        this.addProduction(EntityValue_inner_Plus, HANDLER, EntityValue_inner);
        this.addProduction(EntityValue_inner, HANDLER, EntityValue_Char);
        this.addProduction(EntityValue_inner, HANDLER, PEReference);
        this.addProduction(EntityValue_inner, HANDLER, Reference);
        // PEReference ::= '%' Name ';'
        this.addProduction(PEReference, HANDLER, percentSign, Name, semicolon);
        // NDataDecl ::= S 'NDATA' S Name
        this.addProduction(NDataDecl, HANDLER, capitalN, capitalD, capitalA, capitalT, capitalA, S, Name);
        // PEDecl ::= '<!ENTITY' S '%' S Name S PEDef S? '>'
        this.addProduction(PEDecl, HANDLER, lessThanSign, exclamationMark, capitalE, capitalN, capitalT, capitalI,
                capitalT, capitalY, S, percentSign, S, Name, S, PEDef, S, greaterThanSign);
        this.addProduction(PEDecl, HANDLER, lessThanSign, exclamationMark, capitalE, capitalN, capitalT, capitalI,
                capitalT, capitalY, S, percentSign, S, Name, S, PEDef, greaterThanSign);
        // PEDef ::= EntityValue | ExternalID
        this.addProduction(PEDef, HANDLER, EntityValue);
        this.addProduction(PEDef, HANDLER, ExternalID);
        // NotationDecl ::= '<!NOTATION' S Name S (ExternalID | PublicID) S? '>'
        this.addProduction(NotationDecl, HANDLER, lessThanSign, exclamationMark, capitalN, capitalO, capitalT, capitalA,
                capitalT, capitalI, capitalO, capitalN, S, Name, S, ExternalID, S, greaterThanSign);
        this.addProduction(NotationDecl, HANDLER, lessThanSign, exclamationMark, capitalN, capitalO, capitalT, capitalA,
                capitalT, capitalI, capitalO, capitalN, S, Name, S, ExternalID, greaterThanSign);
        this.addProduction(NotationDecl, HANDLER, lessThanSign, exclamationMark, capitalN, capitalO, capitalT, capitalA,
                capitalT, capitalI, capitalO, capitalN, S, Name, S, PublicID, S, greaterThanSign);
        this.addProduction(NotationDecl, HANDLER, lessThanSign, exclamationMark, capitalN, capitalO, capitalT, capitalA,
                capitalT, capitalI, capitalO, capitalN, S, Name, S, PublicID, greaterThanSign);
        // PublicID ::= 'PUBLIC' S PubidLiteral
        this.addProduction(PublicID, new ProductionHandler() {
            
            @Override
            public Object handleReduction(final List<Object> rightHandSide) {
                return rightHandSide.get(7);
            }
        }, capitalP, capitalU, capitalB, capitalL, capitalI, capitalC, S, PubidLiteral);
        // DeclSep ::= PEReference | S
        this.addProduction(DeclSep, HANDLER, PEReference);
        // element ::= EmptyElemTag | STag content ETag
        this.addProduction(element_container, HANDLER, S, Misc, element_container);
        this.addProduction(element_container, HANDLER, Misc, element_container);
        this.addProduction(element_container, HANDLER, S, element);
        this.addProduction(element_container, HANDLER, element);
        this.addProduction(element, HANDLER, EmptyElemTag);
        this.addProduction(element, HANDLER, STag, content_and_ETag);
        // EmptyElemTag ::= '<' Name (S Attribute)* S? '/>'
        this.addProduction(EmptyElemTag, STAG_ATT_HANDLER, lessThanSign, Name, AttributeList_Plus, S, solidus,
                greaterThanSign);
        this.addProduction(EmptyElemTag, STAG_ATT_HANDLER, lessThanSign, Name, AttributeList_Plus, solidus,
                greaterThanSign);
        this.addProduction(EmptyElemTag, STAG_HANDLER, lessThanSign, Name, S, solidus, greaterThanSign);
        this.addProduction(EmptyElemTag, STAG_HANDLER, lessThanSign, Name, solidus, greaterThanSign);
        this.addProduction(AttributeList_Plus, ATT_LIST_HANDLER, AttributeList_Plus, S, Attribute);
        this.addProduction(AttributeList_Plus, ATT_LIST_HANDLER, S, Attribute);
        // Attribute ::= Name Eq AttValue
        this.addProduction(Attribute, ATTRIBUTE_HANDLER, Name, Eq, AttValue);
        // STag ::= '<' Name (S Attribute)* S? '>'
        this.addProduction(STag, STAG_ATT_HANDLER, lessThanSign, Name, AttributeList_Plus, S, greaterThanSign);
        this.addProduction(STag, STAG_ATT_HANDLER, lessThanSign, Name, AttributeList_Plus, greaterThanSign);
        this.addProduction(STag, STAG_HANDLER, lessThanSign, Name, S, greaterThanSign);
        this.addProduction(STag, STAG_HANDLER, lessThanSign, Name, greaterThanSign);
        // content ::= CharData? ((element | Reference | CDSect | PI | Comment)
        // CharData?)*
        this.addProduction(content_and_ETag, HANDLER, CharData, ETag);
        this.addProduction(content_and_ETag, HANDLER, CharData, content, content_and_ETag);
        this.addProduction(content, HANDLER, element);
        this.addProduction(content, HANDLER, Reference);
        this.addProduction(content, HANDLER, CDSect);
        this.addProduction(content, HANDLER, PI);
        this.addProduction(content, HANDLER, Comment);
        // CharData ::= [^<&]* - ([^<&]* ']]>' [^<&]*)
        this.addProduction(CharData, STRING_CONCAT, CharData_Plus);
        this.addProduction(CharData, STRING_CONCAT, epsilon);
        this.addProduction(CharData_Plus, STRING_CONCAT, CharData_Plus, CharData_Single);
        this.addProduction(CharData_Plus, STRING_CONCAT, CharData_Single);
        // TODO: filter out ']]>'
        
        // CDSect ::= CDStart CData CDEnd
//		this.addProduction(CDSect, CDStart, CData, CDEnd);
        this.addProduction(CDSect, HANDLER, CDStart, CData_body, greaterThanSign);
        // CDStart ::= '<![CDATA['
        this.addProduction(CDStart, HANDLER, lessThanSign, exclamationMark, leftBracket, capitalC, capitalD, capitalA,
                capitalT, capitalA, leftBracket);
        // CData ::= (Char* - (Char* ']]>' Char*))
        // CData-Char ::= (char | '>')
        // CDSect-body-inner ::= CData-Char* ']' (CData-Char+ ']')*
        // CDSect-body ::= CDSect-body-inner ']' (char CDSect-body-inner ']')*
        // CDSect ::= '<![[' CDSect-body '>'
        this.addProduction(CData_body, STRING_CONCAT, CData_body_inner, rightBracket);
        this.addProduction(CData_body, STRING_CONCAT, CData_body_inner, rightBracket, CData_body_suffix_Plus);
        this.addProduction(CData_body_suffix_Plus, STRING_CONCAT, CData_body_suffix_Plus, CData_Char, CData_body_inner,
                rightBracket);
        this.addProduction(CData_body_suffix_Plus, STRING_CONCAT, CData_Char, CData_body_inner, rightBracket);
        this.addProduction(CData_body_inner, STRING_CONCAT, CData_Char_Plus, rightBracket, CData_body_inner_Plus);
        this.addProduction(CData_body_inner, STRING_CONCAT, CData_Char_Plus, rightBracket);
        this.addProduction(CData_body_inner, STRING_CONCAT, rightBracket);
        this.addProduction(CData_body_inner_Plus, STRING_CONCAT, CData_body_inner_Plus, CData_Char_Plus, rightBracket);
        this.addProduction(CData_body_inner_Plus, STRING_CONCAT, CData_Char_Plus, rightBracket);
        this.addProduction(CData_Char_Plus, STRING_CONCAT, CData_Char_Plus, CData_Char);
        this.addProduction(CData_Char_Plus, STRING_CONCAT, CData_Char_Plus, greaterThanSign);
        this.addProduction(CData_Char_Plus, STRING_CONCAT, CData_Char);
        this.addProduction(CData_Char_Plus, STRING_CONCAT, greaterThanSign);
        // CDEnd ::= ']]>'
//		this.addProduction(CDEnd, rightBracket, rightBracket, greaterThanSign);
        // ETag ::= '</' Name S? '>'
        this.addProduction(ETag, ETAG_HANDLER, lessThanSign, solidus, Name, S, greaterThanSign);
        this.addProduction(ETag, ETAG_HANDLER, lessThanSign, solidus, Name, greaterThanSign);
        
        /*
         * Validation productions.
         */
        
        // [6] Names ::= Name (#x20 Name)*
        this.addProduction(Names, HANDLER, Name, Names_inner_Plus);
        this.addProduction(Names, HANDLER, Name);
        this.addProduction(Names_inner_Plus, HANDLER, space, Name, Names_inner_Plus);
        this.addProduction(Names_inner_Plus, HANDLER, space, Name);
        // [8] Nmtokens ::= Nmtoken (#x20 Nmtoken)*
        this.addProduction(Nmtokens, HANDLER, Nmtoken, Nmtokens_inner_Kleene);
        this.addProduction(Nmtokens_inner_Kleene, HANDLER, space, Nmtoken, Nmtokens_inner_Kleene);
        // [30] extSubset ::= TextDecl? extSubsetDecl
        this.addProduction(extSubset, HANDLER, TextDecl, extSubsetDecl);
        this.addProduction(extSubset, HANDLER, extSubsetDecl);
        // [77] TextDecl ::= '<?xml' VersionInfo? EncodingDecl S? '?>'
        this.addProduction(TextDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo,
                EncodingDecl, S, questionMark, greaterThanSign);
        this.addProduction(TextDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, EncodingDecl, S,
                questionMark, greaterThanSign);
        this.addProduction(TextDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo, S,
                questionMark, greaterThanSign);
        this.addProduction(TextDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, S, questionMark,
                greaterThanSign);
        this.addProduction(TextDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo,
                EncodingDecl, questionMark, greaterThanSign);
        this.addProduction(TextDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, EncodingDecl,
                questionMark, greaterThanSign);
        this.addProduction(TextDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, VersionInfo,
                questionMark, greaterThanSign);
        this.addProduction(TextDecl, HANDLER, lessThanSign, questionMark, smallX, smallM, smallL, questionMark,
                greaterThanSign);
        // [31] extSubsetDecl ::= ( markupdecl | conditionalSect | DeclSep)*
        this.addProduction(extSubsetDecl, HANDLER, extSubsetDecl_inner_Plus);
        this.addProduction(extSubsetDecl, HANDLER, epsilon);
        this.addProduction(extSubsetDecl_inner, HANDLER, markupdecl);
        this.addProduction(extSubsetDecl_inner, HANDLER, conditionalSect);
        this.addProduction(extSubsetDecl_inner, HANDLER, DeclSep);
        this.addProduction(extSubsetDecl_inner_Plus, HANDLER, extSubsetDecl_inner, extSubsetDecl_inner_Plus);
        this.addProduction(extSubsetDecl_inner_Plus, HANDLER, extSubsetDecl_inner);
        // [61] conditionalSect ::= includeSect | ignoreSect
        this.addProduction(conditionalSect, HANDLER, includeSect);
        this.addProduction(conditionalSect, HANDLER, ignoreSect);
        // [62] includeSect ::= '<![' S? 'INCLUDE' S? '[' extSubsetDecl ']]>'
        this.addProduction(includeSect, HANDLER, lessThanSign, exclamationMark, leftBracket, S, capitalI, capitalN,
                capitalC, capitalL, capitalU, capitalD, capitalE, S, leftBracket, extSubsetDecl, rightBracket,
                rightBracket, greaterThanSign);
        this.addProduction(includeSect, HANDLER, lessThanSign, exclamationMark, leftBracket, capitalI, capitalN,
                capitalC, capitalL, capitalU, capitalD, capitalE, S, leftBracket, extSubsetDecl, rightBracket,
                rightBracket, greaterThanSign);
        this.addProduction(includeSect, HANDLER, lessThanSign, exclamationMark, leftBracket, S, capitalI, capitalN,
                capitalC, capitalL, capitalU, capitalD, capitalE, leftBracket, extSubsetDecl, rightBracket,
                rightBracket, greaterThanSign);
        this.addProduction(includeSect, HANDLER, lessThanSign, exclamationMark, leftBracket, capitalI, capitalN,
                capitalC, capitalL, capitalU, capitalD, capitalE, leftBracket, extSubsetDecl, rightBracket,
                rightBracket, greaterThanSign);
        // [63] ignoreSect ::= '<![' S? 'IGNORE' S? '[' ignoreSectContents*
        // ']]>'
        this.addProduction(ignoreSect, HANDLER, lessThanSign, exclamationMark, leftBracket, S, capitalI, capitalG,
                capitalN, capitalO, capitalR, capitalE, S, leftBracket, ignoreSectContents_Plus, rightBracket,
                rightBracket, greaterThanSign);
        this.addProduction(ignoreSect, HANDLER, lessThanSign, exclamationMark, leftBracket, S, capitalI, capitalG,
                capitalN, capitalO, capitalR, capitalE, S, leftBracket, rightBracket, rightBracket, greaterThanSign);
        this.addProduction(ignoreSect, HANDLER, lessThanSign, exclamationMark, leftBracket, capitalI, capitalG,
                capitalN, capitalO, capitalR, capitalE, S, leftBracket, ignoreSectContents_Plus, rightBracket,
                rightBracket, greaterThanSign);
        this.addProduction(ignoreSect, HANDLER, lessThanSign, exclamationMark, leftBracket, capitalI, capitalG,
                capitalN, capitalO, capitalR, capitalE, S, leftBracket, rightBracket, rightBracket, greaterThanSign);
        this.addProduction(ignoreSect, HANDLER, lessThanSign, exclamationMark, leftBracket, S, capitalI, capitalG,
                capitalN, capitalO, capitalR, capitalE, leftBracket, ignoreSectContents_Plus, rightBracket,
                rightBracket, greaterThanSign);
        this.addProduction(ignoreSect, HANDLER, lessThanSign, exclamationMark, leftBracket, S, capitalI, capitalG,
                capitalN, capitalO, capitalR, capitalE, leftBracket, rightBracket, rightBracket, greaterThanSign);
        this.addProduction(ignoreSect, HANDLER, lessThanSign, exclamationMark, leftBracket, capitalI, capitalG,
                capitalN, capitalO, capitalR, capitalE, leftBracket, ignoreSectContents_Plus, rightBracket,
                rightBracket, greaterThanSign);
        this.addProduction(ignoreSect, HANDLER, lessThanSign, exclamationMark, leftBracket, capitalI, capitalG,
                capitalN, capitalO, capitalR, capitalE, leftBracket, rightBracket, rightBracket, greaterThanSign);
        // [64] ignoreSectContents ::= Ignore ('<![' ignoreSectContents ']]>'
        // Ignore)*
        this.addProduction(ignoreSectContents_Plus, HANDLER, ignoreSectContents, ignoreSectContents_Plus);
        this.addProduction(ignoreSectContents_Plus, HANDLER, ignoreSectContents);
        this.addProduction(ignoreSectContents, HANDLER, Ignore, ignoreSectContents_inner_Plus);
        this.addProduction(ignoreSectContents, HANDLER, Ignore);
        this.addProduction(ignoreSectContents_inner_Plus, HANDLER, lessThanSign, exclamationMark, leftBracket,
                ignoreSectContents, rightBracket, rightBracket, greaterThanSign, Ignore, ignoreSectContents_inner_Plus);
        this.addProduction(ignoreSectContents_inner_Plus, HANDLER, lessThanSign, exclamationMark, leftBracket,
                ignoreSectContents, rightBracket, rightBracket, greaterThanSign, Ignore);
        // [65] Ignore ::= Char* - (Char* ('<![' | ']]>') Char*)
        this.addProduction(Ignore, HANDLER, Ignore_Char_Plus);
        this.addProduction(Ignore, HANDLER, epsilon);
        this.addProduction(Ignore_Char_Plus, HANDLER, Ignore_Char, Ignore_Char_Plus);
        this.addProduction(Ignore_Char_Plus, HANDLER, Ignore_Char);
        // TODO: exclude processing instructions
        // [78] extParsedEnt ::= TextDecl? content
        this.addProduction(extParsedEnt, HANDLER, TextDecl, content);
        this.addProduction(extParsedEnt, HANDLER, content);
        
        // [84] Letter ::= BaseChar | Ideographic
        this.addProduction(Letter, HANDLER, BaseChar);
        this.addProduction(Letter, HANDLER, Ideographic);
        
        // [85] BaseChar ::= [#x0041-#x005A] | [#x0061-#x007A] | [#x00C0-#x00D6]
        // | [#x00D8-#x00F6] | [#x00F8-#x00FF] | [#x0100-#x0131] |
        // [#x0134-#x013E] | [#x0141-#x0148] | [#x014A-#x017E] | [#x0180-#x01C3]
        // | [#x01CD-#x01F0] | [#x01F4-#x01F5] | [#x01FA-#x0217] |
        // [#x0250-#x02A8] | [#x02BB-#x02C1] | #x0386 | [#x0388-#x038A] | #x038C
        // | [#x038E-#x03A1] | [#x03A3-#x03CE] | [#x03D0-#x03D6] | #x03DA |
        // #x03DC | #x03DE | #x03E0 | [#x03E2-#x03F3] | [#x0401-#x040C] |
        // [#x040E-#x044F] | [#x0451-#x045C] | [#x045E-#x0481] | [#x0490-#x04C4]
        // | [#x04C7-#x04C8] | [#x04CB-#x04CC] | [#x04D0-#x04EB] |
        // [#x04EE-#x04F5] | [#x04F8-#x04F9] | [#x0531-#x0556] | #x0559 |
        // [#x0561-#x0586] | [#x05D0-#x05EA] | [#x05F0-#x05F2] | [#x0621-#x063A]
        // | [#x0641-#x064A] | [#x0671-#x06B7] | [#x06BA-#x06BE] |
        // [#x06C0-#x06CE] | [#x06D0-#x06D3] | #x06D5 | [#x06E5-#x06E6] |
        // [#x0905-#x0939] | #x093D | [#x0958-#x0961] | [#x0985-#x098C] |
        // [#x098F-#x0990] | [#x0993-#x09A8] | [#x09AA-#x09B0] | #x09B2 |
        // [#x09B6-#x09B9] | [#x09DC-#x09DD] | [#x09DF-#x09E1] | [#x09F0-#x09F1]
        // | [#x0A05-#x0A0A] | [#x0A0F-#x0A10] | [#x0A13-#x0A28] |
        // [#x0A2A-#x0A30] | [#x0A32-#x0A33] | [#x0A35-#x0A36] | [#x0A38-#x0A39]
        // | [#x0A59-#x0A5C] | #x0A5E | [#x0A72-#x0A74] | [#x0A85-#x0A8B] |
        // #x0A8D | [#x0A8F-#x0A91] | [#x0A93-#x0AA8] | [#x0AAA-#x0AB0] |
        // [#x0AB2-#x0AB3] | [#x0AB5-#x0AB9] | #x0ABD | #x0AE0 | [#x0B05-#x0B0C]
        // | [#x0B0F-#x0B10] | [#x0B13-#x0B28] | [#x0B2A-#x0B30] |
        // [#x0B32-#x0B33] | [#x0B36-#x0B39] | #x0B3D | [#x0B5C-#x0B5D] |
        // [#x0B5F-#x0B61] | [#x0B85-#x0B8A] | [#x0B8E-#x0B90] | [#x0B92-#x0B95]
        // | [#x0B99-#x0B9A] | #x0B9C | [#x0B9E-#x0B9F] | [#x0BA3-#x0BA4] |
        // [#x0BA8-#x0BAA] | [#x0BAE-#x0BB5] | [#x0BB7-#x0BB9] | [#x0C05-#x0C0C]
        // | [#x0C0E-#x0C10] | [#x0C12-#x0C28] | [#x0C2A-#x0C33] |
        // [#x0C35-#x0C39] | [#x0C60-#x0C61] | [#x0C85-#x0C8C] | [#x0C8E-#x0C90]
        // | [#x0C92-#x0CA8] | [#x0CAA-#x0CB3] | [#x0CB5-#x0CB9] | #x0CDE |
        // [#x0CE0-#x0CE1] | [#x0D05-#x0D0C] | [#x0D0E-#x0D10] | [#x0D12-#x0D28]
        // | [#x0D2A-#x0D39] | [#x0D60-#x0D61] | [#x0E01-#x0E2E] | #x0E30 |
        // [#x0E32-#x0E33] | [#x0E40-#x0E45] | [#x0E81-#x0E82] | #x0E84 |
        // [#x0E87-#x0E88] | #x0E8A | #x0E8D | [#x0E94-#x0E97] | [#x0E99-#x0E9F]
        // | [#x0EA1-#x0EA3] | #x0EA5 | #x0EA7 | [#x0EAA-#x0EAB] |
        // [#x0EAD-#x0EAE] | #x0EB0 | [#x0EB2-#x0EB3] | #x0EBD | [#x0EC0-#x0EC4]
        // | [#x0F40-#x0F47] | [#x0F49-#x0F69] | [#x10A0-#x10C5] |
        // [#x10D0-#x10F6] | #x1100 | [#x1102-#x1103] | [#x1105-#x1107] | #x1109
        // | [#x110B-#x110C] | [#x110E-#x1112] | #x113C | #x113E | #x1140 |
        // #x114C | #x114E | #x1150 | [#x1154-#x1155] | #x1159 | [#x115F-#x1161]
        // | #x1163 | #x1165 | #x1167 | #x1169 | [#x116D-#x116E] |
        // [#x1172-#x1173] | #x1175 | #x119E | #x11A8 | #x11AB | [#x11AE-#x11AF]
        // | [#x11B7-#x11B8] | #x11BA | [#x11BC-#x11C2] | #x11EB | #x11F0 |
        // #x11F9 | [#x1E00-#x1E9B] | [#x1EA0-#x1EF9] | [#x1F00-#x1F15] |
        // [#x1F18-#x1F1D] | [#x1F20-#x1F45] | [#x1F48-#x1F4D] | [#x1F50-#x1F57]
        // | #x1F59 | #x1F5B | #x1F5D | [#x1F5F-#x1F7D] | [#x1F80-#x1FB4] |
        // [#x1FB6-#x1FBC] | #x1FBE | [#x1FC2-#x1FC4] | [#x1FC6-#x1FCC] |
        // [#x1FD0-#x1FD3] | [#x1FD6-#x1FDB] | [#x1FE0-#x1FEC] | [#x1FF2-#x1FF4]
        // | [#x1FF6-#x1FFC] | #x2126 | [#x212A-#x212B] | #x212E |
        // [#x2180-#x2182] | [#x3041-#x3094] | [#x30A1-#x30FA] | [#x3105-#x312C]
        // | [#xAC00-#xD7A3]
//		this.addProduction(BaseChar, toLiteral('\u0041', '\u005A'));
        // TODO: finish
        // [86] Ideographic ::= [#x4E00-#x9FA5] | #x3007 | [#x3021-#x3029]
//		this.addProduction(Ideographic, toLiteral('\u4E00', '\u9FA5'));
//		this.addProduction(Ideographic, toLiteral("#x3007", '\u3007'));
//		this.addProduction(Ideographic, toLiteral('\u3021', '\u3029'));
        // [87] CombiningChar ::= [#x0300-#x0345] | [#x0360-#x0361] |
        // [#x0483-#x0486] | [#x0591-#x05A1] | [#x05A3-#x05B9] | [#x05BB-#x05BD]
        // | #x05BF | [#x05C1-#x05C2] | #x05C4 | [#x064B-#x0652] | #x0670 |
        // [#x06D6-#x06DC] | [#x06DD-#x06DF] | [#x06E0-#x06E4] | [#x06E7-#x06E8]
        // | [#x06EA-#x06ED] | [#x0901-#x0903] | #x093C | [#x093E-#x094C] |
        // #x094D | [#x0951-#x0954] | [#x0962-#x0963] | [#x0981-#x0983] | #x09BC
        // | #x09BE | #x09BF | [#x09C0-#x09C4] | [#x09C7-#x09C8] |
        // [#x09CB-#x09CD] | #x09D7 | [#x09E2-#x09E3] | #x0A02 | #x0A3C | #x0A3E
        // | #x0A3F | [#x0A40-#x0A42] | [#x0A47-#x0A48] | [#x0A4B-#x0A4D] |
        // [#x0A70-#x0A71] | [#x0A81-#x0A83] | #x0ABC | [#x0ABE-#x0AC5] |
        // [#x0AC7-#x0AC9] | [#x0ACB-#x0ACD] | [#x0B01-#x0B03] | #x0B3C |
        // [#x0B3E-#x0B43] | [#x0B47-#x0B48] | [#x0B4B-#x0B4D] | [#x0B56-#x0B57]
        // | [#x0B82-#x0B83] | [#x0BBE-#x0BC2] | [#x0BC6-#x0BC8] |
        // [#x0BCA-#x0BCD] | #x0BD7 | [#x0C01-#x0C03] | [#x0C3E-#x0C44] |
        // [#x0C46-#x0C48] | [#x0C4A-#x0C4D] | [#x0C55-#x0C56] | [#x0C82-#x0C83]
        // | [#x0CBE-#x0CC4] | [#x0CC6-#x0CC8] | [#x0CCA-#x0CCD] |
        // [#x0CD5-#x0CD6] | [#x0D02-#x0D03] | [#x0D3E-#x0D43] | [#x0D46-#x0D48]
        // | [#x0D4A-#x0D4D] | #x0D57 | #x0E31 | [#x0E34-#x0E3A] |
        // [#x0E47-#x0E4E] | #x0EB1 | [#x0EB4-#x0EB9] | [#x0EBB-#x0EBC] |
        // [#x0EC8-#x0ECD] | [#x0F18-#x0F19] | #x0F35 | #x0F37 | #x0F39 | #x0F3E
        // | #x0F3F | [#x0F71-#x0F84] | [#x0F86-#x0F8B] | [#x0F90-#x0F95] |
        // #x0F97 | [#x0F99-#x0FAD] | [#x0FB1-#x0FB7] | #x0FB9 | [#x20D0-#x20DC]
        // | #x20E1 | [#x302A-#x302F] | #x3099 | #x309A
//		this.addProduction(CombiningChar, toLiteral('\u0300', '\u0345'));
        // TODO: finish
        // [88] Digit ::= [#x0030-#x0039] | [#x0660-#x0669] | [#x06F0-#x06F9] |
        // [#x0966-#x096F] | [#x09E6-#x09EF] | [#x0A66-#x0A6F] | [#x0AE6-#x0AEF]
        // | [#x0B66-#x0B6F] | [#x0BE7-#x0BEF] | [#x0C66-#x0C6F] |
        // [#x0CE6-#x0CEF] | [#x0D66-#x0D6F] | [#x0E50-#x0E59] | [#x0ED0-#x0ED9]
        // | [#x0F20-#x0F29]
//		this.addProduction(Digit, toLiteral('\u0030', '\u0039'));
        // TODO: finish
        // [89] Extender ::= #x00B7 | #x02D0 | #x02D1 | #x0387 | #x0640 | #x0E46
        // | #x0EC6 | #x3005 | [#x3031-#x3035] | [#x309D-#x309E] |
        // [#x30FC-#x30FE]
//		this.addProduction(Extender, toLiteral("#x00B7", '\u00B7'));
//		this.addProduction(Extender, toLiteral("#x02D0", '\u02D0'));
//		this.addProduction(Extender, toLiteral("#x02D1", '\u02D1'));
//		this.addProduction(Extender, toLiteral("#x0387", '\u0387'));
//		this.addProduction(Extender, toLiteral("#x0640", '\u0640'));
//		this.addProduction(Extender, toLiteral("#x0E46", '\u0E46'));
//		this.addProduction(Extender, toLiteral("#x0EC6", '\u0EC6'));
//		this.addProduction(Extender, toLiteral("#x3005", '\u3005'));
//		this.addProduction(Extender, toLiteral('\u3031', '\u3035'));
//		this.addProduction(Extender, toLiteral('\u309D', '\u309E'));
//		this.addProduction(Extender, toLiteral('\u30FC', '\u30FE'));
        
        this.compute();
    }
    
    public static NonTerminal createNonTerminalSymbol(final String name) {
        return new NonTerminal(name);
    }
    
    public static void main(final String[] args) throws InterruptedException, ExecutionException, IOException {
        final XMLGrammar grammar = new XMLGrammar();
        
        grammar.validate();
        
        System.out.println(grammar.getNullableSet());
        
        final Collection<NonTerminal> symbols = new TreeSet<>(new Comparator<NonTerminal>() {
            
            @Override
            public int compare(final NonTerminal o1, final NonTerminal o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
//		symbols.addAll(Arrays.asList(document, XMLDecl, Misc_list, doctypedecl, element, PubidLiteral, PublicID));
//		symbols.addAll(Arrays.asList(epsilon, document, prolog, element,
//				XMLDecl, doctypedecl, VersionInfo, EncodingDecl, SDDecl, S, Eq,
//				VersionNum, EncName, Misc, Comment, PI, PITarget, Name,
//				ExternalID, intSubset, PubidLiteral, SystemLiteral, PubidChar,
//				markupdecl, DeclSep, elementdecl, AttlistDecl, EntityDecl,
//				NotationDecl, contentspec, Mixed, children, choice, seq, cp,
//				AttDef, AttType, DefaultDecl, StringType, TokenizedType,
//				EnumeratedType, NotationType, Enumeration, Nmtoken, AttValue,
//				Reference, EntityRef, CharRef, GEDecl, PEDecl, EntityDef,
//				EntityValue, NDataDecl, PEReference, PEDef, PublicID,
//				EmptyElemTag, STag, content, ETag, Attribute, CharData, CDSect,
//				CDStart, CData, CDEnd));
//		symbols.addAll(Arrays.asList(Misc_Plus, WHITE_SPACE, DECIMAL_DIGIT,
//				DECIMAL_DIGIT_Plus, EncName_StartChar, EncName_Char, EncName_Char_Plus,
//				Comment_Char, Comment_Char_Plus, PI_inner, PI_Char, PI_Char_Plus,
//				NameChar_Plus, intSubset_inner, intSubset_inner_Plus,
//				SystemLiteral_inner_Plus, SystemLiteral_Char, PubidChar_Plus,
//				Mixed_inner, Mixed_inner_Plus, choice_inner, choice_inner_Plus,
//				seq_inner, seq_inner_Plus, AttDef_Plus, NotationType_inner,
//				NotationType_inner_Plus, Enumeration_inner, Enumeration_inner_Plus,
//				AttValue_inner, AttValue_inner_Plus, AttValue_Char, HEX_DIGIT,
//				HEX_DIGIT_Plus, EntityValue_inner, EntityValue_inner_Plus,
//				EntityValue_Char, content_inner_Plus, AttributeList_Plus,
//				CharData_Plus, CharData_Single, CData_Char, CData_Char_Plus));
//		symbols.addAll(Arrays.asList(Names, Names_inner, Names_inner_Plus,
//				Nmtokens, Nmtokens_inner_Kleene, extSubset, TextDecl, extSubsetDecl,
//				extSubsetDecl_inner, extSubsetDecl_inner_Plus, conditionalSect,
//				includeSect, ignoreSect, ignoreSectContents, ignoreSectContents_Plus,
//				ignoreSectContents_inner_Plus, Ignore, Ignore_Char, Ignore_Char_Plus,
//				extParsedEnt, Letter, BaseChar, Ideographic, CombiningChar, Digit,
//				Extender));
        
        for (final NonTerminal symbol : symbols) {
            System.out.println(symbol);
            System.out.print("HANDLERable: ");
            System.out.println(grammar.isNullable(symbol));
            System.out.print("First Set: ");
            System.out.println(grammar.getFirstSet(symbol));
            System.out.print("Follow Set: ");
            System.out.println(grammar.getFollowSet(symbol));
        }
        
        final ExecutorService executorService = Executors.newFixedThreadPool(8);
        
        final Parser<UnicodeTerminalSymbols> parser;
        try {
            parser = grammar.threadedCreateParser(document, EOF, executorService);
        } finally {
            executorService.shutdown();
        }
        
        System.out.println("Done!");
        
//		final List<UnicodeTerminalSymbols> tokenStream = Arrays.asList(
//				lessThanSign, smallA, greaterThanSign,
//				smallF, smallO, smallO,
//				lessThanSign, solidus, smallA, greaterThanSign);
        
        /*
         * <a>foo</a> stateStack: 1 symbolStack: nextSymbol: '<' shift 2
         * stateStack: 1, 2 symbolStack: '<' nextSymbol: 'a' shift 3 stateStack:
         * 1, 2, 3 symbolStack: '<', 'a' nextSymbol: '>' reduce 4
         */
        
//		parser.parse(tokenStream.iterator());
        
        final URL url;
        url = new URL("http://w3.org/");
//		url = XMLGrammar.class.getResource("strict.dtd");
//		url = XMLGrammar.class.getResource("w3c-inlined.html");
//		url = XMLGrammar.class.getResource("note_in_dtd.xml");
        final URLConnection conn = url.openConnection();
        final String contentEncoding = conn.getContentEncoding();
        final InputStream inputStream = conn.getInputStream();
        final Reader reader;
        if (contentEncoding == null) {
            reader = new InputStreamReader(inputStream, "UTF-8");
        } else {
            reader = new InputStreamReader(inputStream, contentEncoding);
        }
        final Lexer lexer = new Lexer(reader);
        final long startTime = System.currentTimeMillis();
        parser.parse(lexer.iterator());
        final long endTime = System.currentTimeMillis();
        System.out.print("Parsing time: ");
        System.out.println(endTime - startTime);
    }
    
}
