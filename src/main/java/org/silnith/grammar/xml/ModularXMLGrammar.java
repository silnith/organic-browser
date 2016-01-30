package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.EOF;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.Lexer;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.Parser;
import org.silnith.grammar.Production;
import org.silnith.grammar.UnicodeTerminalSymbols;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;


/**
 * <ol type="1">
 * <li value="1">{@linkplain DocumentProduction document} ::=
 * {@linkplain PrologProduction prolog} {@linkplain ElementProduction element}
 * {@linkplain MiscProduction Misc}*
 * <li value="2">{@linkplain CharProduction Char} ::= #x9 | #xA | #xD |
 * [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
 * <li value="3">{@linkplain SProduction S} ::= (#x20 | #x9 | #xD | #xA)+
 * <li value="4">{@linkplain NameStartCharProduction NameStartChar} ::= ":" |
 * [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] |
 * [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] |
 * [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] |
 * [#x10000-#xEFFFF]
 * <ol type="a">
 * <li value="1">{@linkplain NameCharProduction NameChar} ::=
 * {@linkplain NameStartCharProduction NameStartChar} | "-" | "." | [0-9] | #xB7
 * | [#x0300-#x036F] | [#x203F-#x2040]
 * </ol>
 * <li value="5">{@linkplain NameProduction Name} ::=
 * {@linkplain NameStartCharProduction NameStartChar} (
 * {@linkplain NameCharProduction NameChar})*
 * <li value="6">{@linkplain NamesProduction Names} ::=
 * {@linkplain NameProduction Name} (#x20 {@linkplain NameProduction Name})*
 * <li value="7">{@linkplain NmtokenProduction Nmtoken} ::= (
 * {@linkplain NameCharProduction NameChar})+
 * <li value="8">{@linkplain NmtokensProduction Nmtokens} ::=
 * {@linkplain NmtokenProduction Nmtoken} (#x20 {@linkplain NmtokenProduction
 * Nmtoken})*
 * <li value="9">{@linkplain EntityValueProduction EntityValue} ::= '
 * "' ([^%&amp;"] | {@linkplain PEReferenceProduction PEReference} |
 * {@linkplain ReferenceProduction Reference})* '"' | "'" ([^%&amp;'] |
 * {@linkplain PEReferenceProduction PEReference} |
 * {@linkplain ReferenceProduction Reference})* "'"
 * <li value="10">{@linkplain AttValueProduction AttValue} ::= '"' ([^&lt;&amp;"
 * ] | {@linkplain ReferenceProduction Reference})* '"' | "'" ([^&lt;&amp;'] |
 * {@linkplain ReferenceProduction Reference})* "'"
 * <li value="11">{@linkplain SystemLiteralProduction SystemLiteral} ::= ('
 * "' [^"]* '"') | ("'" [^']* "'")
 * <li value="12">{@linkplain PubidLiteralProduction PubidLiteral} ::= '
 * "' {@linkplain PubidCharProduction PubidChar}* '"' | "'" (
 * {@linkplain PubidCharProduction PubidChar} - "'")* "'"
 * <li value="13">{@linkplain PubidCharProduction PubidChar} ::= #x20 | #xD |
 * #xA | [a-zA-Z0-9] | [-'()+,./:=?;!*#@$_%]
 * <li value="14">{@linkplain CharDataProduction CharData} ::= [^&lt;&amp;]* -
 * ([^&lt;&amp;]* ']]&gt;' [^&lt;&amp;]*)
 * <li value="15">{@linkplain CommentProduction Comment} ::= '&lt;!--' ((
 * {@linkplain CharProduction Char} - '-') | ('-' ({@linkplain CharProduction
 * Char} - '-')))* '--&gt;'
 * <li value="16">{@linkplain PIProduction PI} ::= '&lt;?'
 * {@linkplain PITargetProduction PITarget} ({@linkplain SProduction S} (
 * {@linkplain CharProduction Char}* - ({@linkplain CharProduction Char}*
 * '?&gt;' {@linkplain CharProduction Char}*)))? '?&gt;'
 * <li value="17">{@linkplain PITargetProduction PITarget} ::=
 * {@linkplain NameProduction Name} - (('X' | 'x') ('M' | 'm') ('L' | 'l'))
 * <li value="18">{@linkplain CDSectProduction CDSect} ::=
 * {@linkplain CDSectProduction CDStart} {@linkplain CDSectProduction CData}
 * {@linkplain CDSectProduction CDEnd}
 * <li value="19">{@linkplain CDSectProduction CDStart} ::= '&lt;![CDATA['
 * <li value="20">{@linkplain CDSectProduction CData} ::= (
 * {@linkplain CharProduction Char}* - ({@linkplain CharProduction Char}*
 * ']]&gt;' {@linkplain CharProduction Char}*))
 * <li value="21">{@linkplain CDSectProduction CDEnd} ::= ']]&gt;'
 * <li value="22">{@linkplain PrologProduction prolog} ::=
 * {@linkplain XMLDeclProduction XMLDecl}? {@linkplain MiscProduction Misc}* (
 * {@linkplain DoctypedeclProduction doctypedecl} {@linkplain MiscProduction
 * Misc}*)?
 * <li value="23">{@linkplain XMLDeclProduction XMLDecl} ::= '&lt;?xml'
 * {@linkplain VersionInfoProduction VersionInfo}
 * {@linkplain EncodingDeclProduction EncodingDecl}?
 * {@linkplain SDDeclProduction SDDecl}? {@linkplain SProduction S}? '?&gt;'
 * <li value="24">{@linkplain VersionInfoProduction VersionInfo} ::=
 * {@linkplain SProduction S} 'version' {@linkplain EqProduction Eq} ("'"
 * {@linkplain VersionNumProduction VersionNum} "'" | '
 * "' {@linkplain VersionNumProduction VersionNum} '"')
 * <li value="25">{@linkplain EqProduction Eq} ::= {@linkplain SProduction S}?
 * '=' {@linkplain SProduction S}?
 * <li value="26">{@linkplain VersionNumProduction VersionNum} ::= '1.' [0-9]+
 * <li value="27">{@linkplain MiscProduction Misc} ::=
 * {@linkplain CommentProduction Comment} | {@linkplain PIProduction PI} |
 * {@linkplain SProduction S}
 * <li value="28">{@linkplain DoctypedeclProduction doctypedecl} ::=
 * '&lt;!DOCTYPE' {@linkplain SProduction S} {@linkplain NameProduction Name} (
 * {@linkplain SProduction S} {@linkplain ExternalIDProduction ExternalID})?
 * {@linkplain SProduction S}? ('[' {@linkplain IntSubsetProduction intSubset}
 * ']' {@linkplain SProduction S}?)? '&gt;'
 * <ol type="a">
 * <li value="1">{@linkplain DeclSepProduction DeclSep} ::=
 * {@linkplain PEReferenceProduction PEReference} | {@linkplain SProduction S}
 * <li value="2">{@linkplain IntSubsetProduction intSubset} ::= (
 * {@linkplain MarkupDeclProduction markupdecl} | {@linkplain DeclSepProduction
 * DeclSep})*
 * </ol>
 * <li value="29">{@linkplain MarkupDeclProduction markupdecl} ::=
 * {@linkplain ElementDeclProduction elementdecl} |
 * {@linkplain AttlistDeclProduction AttlistDecl} |
 * {@linkplain EntityDeclProduction EntityDecl} |
 * {@linkplain NotationDeclProduction NotationDecl} | {@linkplain PIProduction
 * PI} | {@linkplain CommentProduction Comment}
 * <li value="30">{@linkplain ExtSubsetProduction extSubset} ::=
 * {@linkplain TextDeclProduction TextDecl}? {@linkplain ExtSubsetDeclProduction
 * extSubsetDecl}
 * <li value="31">{@linkplain ExtSubsetDeclProduction extSubsetDecl} ::= (
 * {@linkplain MarkupDeclProduction markupdecl} |
 * {@linkplain ConditionalSectProduction conditionalSect} |
 * {@linkplain DeclSepProduction DeclSep})*
 * <li value="32">{@linkplain SDDeclProduction SDDecl} ::=
 * {@linkplain SProduction S} 'standalone' {@linkplain EqProduction Eq} (("'"
 * ('yes' | 'no') "'") | ('"' ('yes' | 'no') '"'))
 * <li value="39">{@linkplain ElementProduction element} ::=
 * {@linkplain ElementProduction EmptyElemTag} | {@linkplain ElementProduction
 * STag} {@linkplain ElementProduction content} {@linkplain ElementProduction
 * ETag}
 * <li value="40">{@linkplain ElementProduction STag} ::= '&lt;'
 * {@linkplain NameProduction Name} ({@linkplain SProduction S}
 * {@linkplain AttributeProduction Attribute})* {@linkplain SProduction S}?
 * '&gt;'
 * <li value="41">{@linkplain AttributeProduction Attribute} ::=
 * {@linkplain NameProduction Name} {@linkplain EqProduction Eq}
 * {@linkplain AttValueProduction AttValue}
 * <li value="42">{@linkplain ElementProduction ETag} ::= '&lt;/'
 * {@linkplain NameProduction Name} {@linkplain SProduction S}? '&gt;'
 * <li value="43">{@linkplain ElementProduction content} ::=
 * {@linkplain CharDataProduction CharData}? (({@linkplain ElementProduction
 * element} | {@linkplain ReferenceProduction Reference} |
 * {@linkplain CDSectProduction CDSect} | {@linkplain PIProduction PI} |
 * {@linkplain CommentProduction Comment}) {@linkplain CharDataProduction
 * CharData}?)*
 * <li value="44">{@linkplain ElementProduction EmptyElemTag} ::= '&lt;'
 * {@linkplain NameProduction Name} ({@linkplain SProduction S}
 * {@linkplain AttributeProduction Attribute})* {@linkplain SProduction S}?
 * '/&gt;'
 * <li value="45">{@linkplain ElementDeclProduction elementdecl} ::=
 * '&lt;!ELEMENT' {@linkplain SProduction S} {@linkplain NameProduction Name}
 * {@linkplain SProduction S} {@linkplain ContentSpecProduction contentspec}
 * {@linkplain SProduction S}? '&gt;'
 * <li value="46">{@linkplain ContentSpecProduction contentspec} ::= 'EMPTY' |
 * 'ANY' | {@linkplain MixedProduction Mixed} | {@linkplain ChildrenProduction
 * children}
 * <li value="47">{@linkplain ChildrenProduction children} ::= (
 * {@linkplain ChildrenProduction choice} | {@linkplain ChildrenProduction seq})
 * ('?' | '*' | '+')?
 * <li value="48">{@linkplain ChildrenProduction cp} ::= (
 * {@linkplain NameProduction Name} | {@linkplain ChildrenProduction choice} |
 * {@linkplain ChildrenProduction seq}) ('?' | '*' | '+')?
 * <li value="49">{@linkplain ChildrenProduction choice} ::= '('
 * {@linkplain SProduction S}? {@linkplain ChildrenProduction cp} (
 * {@linkplain SProduction S}? '|' {@linkplain SProduction S}?
 * {@linkplain ChildrenProduction cp} )+ {@linkplain SProduction S}? ')'
 * <li value="50">{@linkplain ChildrenProduction seq} ::= '('
 * {@linkplain SProduction S}? {@linkplain ChildrenProduction cp} (
 * {@linkplain SProduction S}? ',' {@linkplain SProduction S}?
 * {@linkplain ChildrenProduction cp} )* {@linkplain SProduction S}? ')'
 * <li value="51">{@linkplain MixedProduction Mixed} ::= '('
 * {@linkplain SProduction S}? '#PCDATA' ({@linkplain SProduction S}? '|'
 * {@linkplain SProduction S}? {@linkplain NameProduction Name})*
 * {@linkplain SProduction S}? ')*' | '(' {@linkplain SProduction S}? '#PCDATA'
 * {@linkplain SProduction S}? ')'
 * <li value="52">{@linkplain AttlistDeclProduction AttlistDecl} ::=
 * '&lt;!ATTLIST' {@linkplain SProduction S} {@linkplain NameProduction Name}
 * {@linkplain AttDefProduction AttDef}* {@linkplain SProduction S}? '&gt;'
 * <li value="53">{@linkplain AttDefProduction AttDef} ::=
 * {@linkplain SProduction S} {@linkplain NameProduction Name}
 * {@linkplain SProduction S} {@linkplain AttTypeProduction AttType}
 * {@linkplain SProduction S} {@linkplain DefaultDeclProduction DefaultDecl}
 * <li value="54">{@linkplain AttTypeProduction AttType} ::=
 * {@linkplain StringTypeProduction StringType} |
 * {@linkplain TokenizedTypeProduction TokenizedType} |
 * {@linkplain EnumeratedTypeProduction EnumeratedType}
 * <li value="55">{@linkplain StringTypeProduction StringType} ::= 'CDATA'
 * <li value="56">{@linkplain TokenizedTypeProduction TokenizedType} ::= 'ID' |
 * 'IDREF' | 'IDREFS' | 'ENTITY' | 'ENTITIES' | 'NMTOKEN' | 'NMTOKENS'
 * <li value="57">{@linkplain EnumeratedTypeProduction EnumeratedType} ::=
 * {@linkplain NotationTypeProduction NotationType} |
 * {@linkplain EnumerationProduction Enumeration}
 * <li value="58">{@linkplain NotationTypeProduction NotationType} ::=
 * 'NOTATION' {@linkplain SProduction S} '(' {@linkplain SProduction S}?
 * {@linkplain NameProduction Name} ({@linkplain SProduction S}? '|'
 * {@linkplain SProduction S}? {@linkplain NameProduction Name})*
 * {@linkplain SProduction S}? ')'
 * <li value="59">{@linkplain EnumerationProduction Enumeration} ::= '('
 * {@linkplain SProduction S}? {@linkplain NmtokenProduction Nmtoken} (
 * {@linkplain SProduction S}? '|' {@linkplain SProduction S}?
 * {@linkplain NmtokenProduction Nmtoken})* {@linkplain SProduction S}? ')'
 * <li value="60">{@linkplain DefaultDeclProduction DefaultDecl} ::= '#REQUIRED'
 * | '#IMPLIED' | (('#FIXED' {@linkplain SProduction S})?
 * {@linkplain AttValueProduction AttValue})
 * <li value="61">{@linkplain ConditionalSectProduction conditionalSect} ::=
 * {@linkplain IncludeSectProduction includeSect} |
 * {@linkplain IgnoreSectProduction ignoreSect}
 * <li value="62">{@linkplain IncludeSectProduction includeSect} ::= '&lt;!['
 * {@linkplain SProduction S}? 'INCLUDE' {@linkplain SProduction S}? '['
 * {@linkplain ExtSubsetDeclProduction extSubsetDecl} ']]&gt;'
 * <li value="63">{@linkplain IgnoreSectProduction ignoreSect} ::= '&lt;!['
 * {@linkplain SProduction S}? 'IGNORE' {@linkplain SProduction S}? '['
 * {@linkplain IgnoreSectContentsProduction ignoreSectContents}* ']]&gt;'
 * <li value="64">{@linkplain IgnoreSectContentsProduction ignoreSectContents}
 * ::= {@linkplain IgnoreSectContentsProduction Ignore} ('&lt;!['
 * {@linkplain IgnoreSectContentsProduction ignoreSectContents} ']]&gt;'
 * {@linkplain IgnoreSectContentsProduction Ignore})*
 * <li value="65">{@linkplain IgnoreSectContentsProduction Ignore} ::=
 * {@linkplain CharProduction Char}* - ({@linkplain CharProduction Char}*
 * ('&lt;![' | ']]&gt;') {@linkplain CharProduction Char}*)
 * <li value="66">{@linkplain CharRefProduction CharRef} ::= '&amp;#' [0-9]+ ';'
 * | '&amp;#x' [0-9a-fA-F]+ ';'
 * <li value="67">{@linkplain ReferenceProduction Reference} ::=
 * {@linkplain EntityRefProduction EntityRef} | {@linkplain CharRefProduction
 * CharRef}
 * <li value="68">{@linkplain EntityRefProduction EntityRef} ::= '&amp;'
 * {@linkplain NameProduction Name} ';'
 * <li value="69">{@linkplain PEReferenceProduction PEReference} ::= '%'
 * {@linkplain NameProduction Name} ';'
 * <li value="70">{@linkplain EntityDeclProduction EntityDecl} ::=
 * {@linkplain GEDeclProduction GEDecl} | {@linkplain PEDeclProduction PEDecl}
 * <li value="71">{@linkplain GEDeclProduction GEDecl} ::= '&lt;!ENTITY'
 * {@linkplain SProduction S} {@linkplain NameProduction Name}
 * {@linkplain SProduction S} {@linkplain EntityDefProduction EntityDef}
 * {@linkplain SProduction S}? '&gt;'
 * <li value="72">{@linkplain PEDeclProduction PEDecl} ::= '&lt;!ENTITY'
 * {@linkplain SProduction S} '%' {@linkplain SProduction S}
 * {@linkplain NameProduction Name} {@linkplain SProduction S}
 * {@linkplain PEDeclProduction PEDecl} {@linkplain SProduction S}? '&gt;'
 * <li value="73">{@linkplain EntityDefProduction EntityDef} ::=
 * {@linkplain EntityValueProduction EntityValue} | (
 * {@linkplain ExternalIDProduction ExternalID} {@linkplain NDataDeclProduction
 * NDataDecl}?)
 * <li value="74">{@linkplain PEDefProduction PEDef} ::=
 * {@linkplain EntityValueProduction EntityValue} |
 * {@linkplain ExternalIDProduction ExternalID}
 * <li value="75">{@linkplain ExternalIDProduction ExternalID} ::= 'SYSTEM'
 * {@linkplain SProduction S} {@linkplain SystemLiteralProduction SystemLiteral}
 * | 'PUBLIC' {@linkplain SProduction S} {@linkplain PubidLiteralProduction
 * PubidLiteral} {@linkplain SProduction S} {@linkplain SystemLiteralProduction
 * SystemLiteral}
 * <li value="76">{@linkplain NDataDeclProduction NDataDecl} ::=
 * {@linkplain SProduction S} 'NDATA' {@linkplain SProduction S}
 * {@linkplain NameProduction Name}
 * <li value="77">{@linkplain TextDeclProduction TextDecl} ::= '&lt;?xml'
 * {@linkplain VersionInfoProduction VersionInfo}?
 * {@linkplain EncodingDeclProduction EncodingDecl} {@linkplain SProduction S}?
 * '?&gt;'
 * <li value="78">{@linkplain ExtParsedEntProduction extParsedEnt} ::=
 * {@linkplain TextDeclProduction TextDecl}? {@linkplain ElementProduction
 * content}
 * <li value="80">{@linkplain EncodingDeclProduction EncodingDecl} ::=
 * {@linkplain SProduction S} 'encoding' {@linkplain EqProduction Eq} ('
 * "' {@linkplain EncNameProduction EncName} '"' | "'"
 * {@linkplain EncNameProduction EncName} "'" )
 * <li value="81">{@linkplain EncNameProduction EncName} ::= [A-Za-z]
 * ([A-Za-z0-9._] | '-')*
 * <li value="82">{@linkplain NotationDeclProduction NotationDecl} ::=
 * '&lt;!NOTATION' {@linkplain SProduction S} {@linkplain NameProduction Name}
 * {@linkplain SProduction S} ({@linkplain ExternalIDProduction ExternalID} |
 * {@linkplain PublicIDProduction PublicID}) {@linkplain SProduction S}? '&gt;'
 * <li value="83">{@linkplain PublicIDProduction PublicID} ::= 'PUBLIC'
 * {@linkplain SProduction S} {@linkplain PubidLiteralProduction PubidLiteral}
 * <li value="84">{@linkplain LetterProduction Letter} ::=
 * {@linkplain BaseCharProduction BaseChar} | {@linkplain IdeographicProduction
 * Ideographic}
 * <li value="85">{@linkplain BaseCharProduction BaseChar} ::= [#x0041-#x005A] |
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
 * <li value="86">{@linkplain IdeographicProduction Ideographic} ::=
 * [#x4E00-#x9FA5] | #x3007 | [#x3021-#x3029]
 * <li value="87">{@linkplain CombiningCharProduction CombiningChar} ::=
 * [#x0300-#x0345] | [#x0360-#x0361] | [#x0483-#x0486] | [#x0591-#x05A1] |
 * [#x05A3-#x05B9] | [#x05BB-#x05BD] | #x05BF | [#x05C1-#x05C2] | #x05C4 |
 * [#x064B-#x0652] | #x0670 | [#x06D6-#x06DC] | [#x06DD-#x06DF] |
 * [#x06E0-#x06E4] | [#x06E7-#x06E8] | [#x06EA-#x06ED] | [#x0901-#x0903] |
 * #x093C | [#x093E-#x094C] | #x094D | [#x0951-#x0954] | [#x0962-#x0963] |
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
 * <li value="88">{@linkplain DigitProduction Digit} ::= [#x0030-#x0039] |
 * [#x0660-#x0669] | [#x06F0-#x06F9] | [#x0966-#x096F] | [#x09E6-#x09EF] |
 * [#x0A66-#x0A6F] | [#x0AE6-#x0AEF] | [#x0B66-#x0B6F] | [#x0BE7-#x0BEF] |
 * [#x0C66-#x0C6F] | [#x0CE6-#x0CEF] | [#x0D66-#x0D6F] | [#x0E50-#x0E59] |
 * [#x0ED0-#x0ED9] | [#x0F20-#x0F29]
 * <li value="89">{@linkplain ExtenderProduction Extender} ::= #x00B7 | #x02D0 |
 * #x02D1 | #x0387 | #x0640 | #x0E46 | #x0EC6 | #x3005 | [#x3031-#x3035] |
 * [#x309D-#x309E] | [#x30FC-#x30FE]
 * </ol>
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/">XML 1.0</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ModularXMLGrammar extends Grammar<UnicodeTerminalSymbols> {
    
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
    
    private final NonTerminalSymbol document;
    
    public ModularXMLGrammar() {
        super(new EnumSetFactory(), new DefaultMapFactory<NonTerminalSymbol, Set<Production>>(),
                new DefaultSetFactory<NonTerminalSymbol>());
        final SProduction sProduction = new SProduction(this);
        final EqProduction eqProduction = new EqProduction(this, sProduction);
        final DecimalNumberLiteralProduction decimalNumberLiteralProduction = new DecimalNumberLiteralProduction(this);
        final VersionNumProduction versionNumProduction =
                new VersionNumProduction(this, decimalNumberLiteralProduction);
        final VersionInfoProduction versionInfoProduction =
                new VersionInfoProduction(this, sProduction, eqProduction, versionNumProduction);
        final EncNameProduction encNameProduction = new EncNameProduction(this);
        final EncodingDeclProduction encodingDeclProduction =
                new EncodingDeclProduction(this, sProduction, eqProduction, encNameProduction);
        final SDDeclProduction sdDeclProduction = new SDDeclProduction(this, sProduction, eqProduction);
        final XMLDeclProduction xmlDeclProduction = new XMLDeclProduction(this, versionInfoProduction,
                encodingDeclProduction, sdDeclProduction, sProduction);
        final CommentProduction commentProduction = new CommentProduction(this);
        final NameCharProduction nameCharProduction = new NameCharProduction(this);
        final PITargetProduction piTargetProduction = new PITargetProduction(this, nameCharProduction);
        final PIProduction piProduction = new PIProduction(this, piTargetProduction, sProduction);
        final MiscProduction miscProduction = new MiscProduction(this, sProduction, commentProduction, piProduction);
        final NameStartCharProduction nameStartCharProduction = new NameStartCharProduction(this);
        final NameProduction nameProduction = new NameProduction(this, nameStartCharProduction, nameCharProduction);
        final PubidCharProduction pubidCharProduction = new PubidCharProduction(this);
        final PubidLiteralProduction pubidLiteralProduction = new PubidLiteralProduction(this, pubidCharProduction);
        final PublicIDProduction publicIDProduction = new PublicIDProduction(this, sProduction, pubidLiteralProduction);
        final SystemLiteralProduction systemLiteralProduction = new SystemLiteralProduction(this);
        final ExternalIDProduction externalIDProduction =
                new ExternalIDProduction(this, publicIDProduction, sProduction, systemLiteralProduction);
        final MixedProduction mixedProduction = new MixedProduction(this, sProduction, nameProduction);
        final ChildrenProduction childrenProduction = new ChildrenProduction(this, sProduction, nameProduction);
        final ContentSpecProduction contentSpecProduction =
                new ContentSpecProduction(this, mixedProduction, childrenProduction);
        final ElementDeclProduction elementDeclProduction =
                new ElementDeclProduction(this, sProduction, nameProduction, contentSpecProduction);
        final StringTypeProduction stringTypeProduction = new StringTypeProduction(this);
        final TokenizedTypeProduction tokenizedTypeProduction = new TokenizedTypeProduction(this);
        final NotationTypeProduction notationTypeProduction =
                new NotationTypeProduction(this, sProduction, nameProduction);
        final NmtokenProduction nmtokenProduction = new NmtokenProduction(this, nameCharProduction);
        final EnumerationProduction enumerationProduction =
                new EnumerationProduction(this, sProduction, nmtokenProduction);
        final EnumeratedTypeProduction enumeratedTypeProduction =
                new EnumeratedTypeProduction(this, notationTypeProduction, enumerationProduction);
        final AttTypeProduction attTypeProduction =
                new AttTypeProduction(this, stringTypeProduction, tokenizedTypeProduction, enumeratedTypeProduction);
        final EntityRefProduction entityRefProduction = new EntityRefProduction(this, nameProduction);
        final CharRefProduction charRefProduction = new CharRefProduction(this, decimalNumberLiteralProduction);
        final ReferenceProduction referenceProduction =
                new ReferenceProduction(this, entityRefProduction, charRefProduction);
        final AttValueProduction attValueProduction = new AttValueProduction(this, referenceProduction);
        final DefaultDeclProduction defaultDeclProduction =
                new DefaultDeclProduction(this, sProduction, attValueProduction);
        final AttDefProduction attDefProduction =
                new AttDefProduction(this, sProduction, nameProduction, attTypeProduction, defaultDeclProduction);
        final AttlistDeclProduction attlistDeclProduction =
                new AttlistDeclProduction(this, sProduction, nameProduction, attDefProduction);
        final PEReferenceProduction peReferenceProduction = new PEReferenceProduction(this, nameProduction);
        final EntityValueProduction entityValueProduction =
                new EntityValueProduction(this, peReferenceProduction, referenceProduction);
        final NDataDeclProduction nDataDeclProduction = new NDataDeclProduction(this, sProduction, nameProduction);
        final EntityDefProduction entityDefProduction = new EntityDefProduction(this, entityValueProduction,
                externalIDProduction, sProduction, nDataDeclProduction);
        final GEDeclProduction geDeclProduction =
                new GEDeclProduction(this, sProduction, nameProduction, entityDefProduction);
        final PEDefProduction peDefProduction = new PEDefProduction(this, entityValueProduction, externalIDProduction);
        final PEDeclProduction peDeclProduction =
                new PEDeclProduction(this, sProduction, nameProduction, peDefProduction);
        final EntityDeclProduction entityDeclProduction =
                new EntityDeclProduction(this, geDeclProduction, peDeclProduction);
        final NotationDeclProduction notationDeclProduction =
                new NotationDeclProduction(this, sProduction, nameProduction, externalIDProduction, publicIDProduction);
        final MarkupDeclProduction markupDeclProduction = new MarkupDeclProduction(this, elementDeclProduction,
                attlistDeclProduction, entityDeclProduction, notationDeclProduction, piProduction, commentProduction);
        final DeclSepProduction declSepProduction = new DeclSepProduction(this, peReferenceProduction);
        final IntSubsetProduction intSubsetProduction =
                new IntSubsetProduction(this, sProduction, markupDeclProduction, declSepProduction);
        final DoctypedeclProduction doctypedeclProduction =
                new DoctypedeclProduction(this, sProduction, nameProduction, externalIDProduction, intSubsetProduction);
        final AttributeProduction attributeProduction =
                new AttributeProduction(this, nameProduction, eqProduction, attValueProduction, sProduction);
        final CharDataProduction charDataProduction = new CharDataProduction(this);
        final CDSectProduction cdSectProduction = new CDSectProduction(this);
        final ElementProduction elementProduction =
                new ElementProduction(this, nameProduction, sProduction, attributeProduction, charDataProduction,
                        referenceProduction, cdSectProduction, piProduction, commentProduction);
        final DocumentProduction documentProduction = new DocumentProduction(this, xmlDeclProduction, miscProduction,
                sProduction, doctypedeclProduction, elementProduction);
                
        this.document = documentProduction.getNonTerminalSymbol();
        this.compute();
    }
    
    public NonTerminalSymbol getDocumentNonTerminalSymbol() {
        return document;
    }
    
    public static void main(final String[] args) throws IOException, InterruptedException, ExecutionException,
            ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
        final ModularXMLGrammar grammar = new ModularXMLGrammar();
        
        final ExecutorService executorService = Executors.newFixedThreadPool(8);
        
        final Parser<UnicodeTerminalSymbols> parser;
        try {
            parser = grammar.threadedCreateParser(grammar.getDocumentNonTerminalSymbol(), EOF, executorService);
        } finally {
            executorService.shutdown();
        }
        
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
        final Document domDocument = parser.parse(lexer.iterator());
        final long endTime = System.currentTimeMillis();
        System.out.print("Parsing time: ");
        System.out.println(endTime - startTime);
        
        final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        final DOMImplementation domImplementation = registry.getDOMImplementation("+LS 3.0");
        final DOMImplementationLS feature = (DOMImplementationLS) domImplementation.getFeature("+LS", "3.0");
        final LSSerializer serializer = feature.createLSSerializer();
//		serializer.getDomConfig().setParameter("format-pretty-print", true);
//		serializer.getDomConfig().setParameter("cdata-sections", false);
//		serializer.getDomConfig().setParameter("comments", false);
//		serializer.getDomConfig().setParameter("element-content-whitespace", false);
//		serializer.getDomConfig().setParameter("entities", false);
//		serializer.getDomConfig().setParameter("well-formed", false);
        final String docString = serializer.writeToString(domDocument);
        System.out.println(docString);
    }
    
}
