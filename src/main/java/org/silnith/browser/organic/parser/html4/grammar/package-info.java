/**
 * HTML 4.01 Grammar
 * 
 * <ul>
 *   <li>VALUE := (name | word)
 *              | singlequote (text|whitespace)* singlequote
 *              | doublequote (text|whitespace)* doublequote
 *   <li>SPACE := (whitespace|COMMENT)+
 *   <li>EXTERNALDATA := (namebody|name|number|gt|leftbracket|rightbracket|singlequote|doublequote|amp|semicolon|dash|bang|newline|whitespace|text)*
 *   <li>CDATA := (text|name|namebody|number|lt|slash|leftbracket|singlequote|doublequote|dash|bang|newline|whitespace)*
 *   <li>PCDATA := (text|name|namebody|number|singlequote|doublequote|dash|bang)+
 *   <li>MARKUPDECL := markupdecl leftbracket whitespace* name leftbracket CDATA endblock
 *   <li>COMMENT := markupdecl commentdelim CDATA commentdelim whitespace* gt
 *   <li>DOCUMENT := DOCTYPE? HTML
 *   <li>DOCTYPE := markupdecl "DOCTYPE" name name VALUE VALUE? gt
 *   <li>ATTRIBUTES := ATTRIBUTE*
 *   <li>ATTRIBUTE := whitespace+ name whitespace? ("=" whitespace? VALUE)?
 *   <li>HTML := (lt "HTML" ATTRIBUTES gt)? newline? HEAD BODY newline? (closetag "HTML" gt)?
 *   <li>HEAD := (lt "HEAD" ATTRIBUTES gt)? newline? (TITLE | BASE | SCRIPT | STYLE | META | LINK | OBJECT)+ newline? (closetag "HEAD" gt)?
 *   <li>TITLE := lt "TITLE" ATTRIBUTES gt newline? PCDATA newline? closetag "TITLE" gt
 *   <li>BASE := lt "BASE" ATTRIBUTES gt
 *   <li>META := lt "META" ATTRIBUTES gt
 *   <li>STYLE := lt "STYLE" ATTRIBUTES gt newline? EXTERNALDATA newline? closetag "STYLE" gt
 *   <li>SCRIPT := lt "SCRIPT" ATTRIBUTES gt newline? EXTERNALDATA newline? closetag "SCRIPT" gt
 *   <li>NOSCRIPT := lt "NOSCRIPT" ATTRIBUTES gt newline? BLOCK+ newline? closetag "NOSCRIPT" gt
 *   <li>BODY := (lt "BODY" ATTRIBUTES gt)? newline? (BLOCK | SCRIPT | INS | DEL | SPACE)+ newline? (closetag "BODY" gt)?
 *   <li>BLOCK := P | HEADING | LIST | PREFORMATTED | DL | DIV | NOSCRIPT | BLOCKQUOTE | FORM | HR | TABLE | FIELDSET | ADDRESS
 *   <li>P := lt "P" ATTRIBUTES gt newline? INLINE* newline? (closetag "P" gt)?
 *   <li>INLINE := PCDATA | FONTSTYLE | PHRASE | SPECIAL | FORMCTRL
 *   <li>FONTSTYLE := TT | I | B | BIG | SMALL
 *   <li>TT := lt "TT" ATTRIBUTES gt newline? INLINE* newline? closetag "TT" gt
 *   <li>I := lt "I" ATTRIBUTES gt newline? INLINE* newline? closetag "I" gt
 *   <li>B := lt "B" ATTRIBUTES gt newline? INLINE* newline? closetag "B" gt
 *   <li>BIG := lt "BIG" ATTRIBUTES gt newline? INLINE* newline? closetag "BIG" gt
 *   <li>SMALL := lt "SMALL" ATTRIBUTES gt newline? INLINE* newline? closetag "SMALL" gt
 *   <li>PHRASE := EM | STRONG | DFN | CODE | SAMP | KBD | VAR | CITE | ABBR | ACRONYM
 *   <li>EM := lt "EM" ATTRIBUTES gt newline? INLINE* newline? closetag "EM" gt
 *   <li>STRONG := lt "STRONG" ATTRIBUTES gt newline? INLINE* newline? closetag "STRONG" gt
 *   <li>DFN := lt "DFN" ATTRIBUTES gt newline? INLINE* newline? closetag "DFN" gt
 *   <li>CODE := lt "CODE" ATTRIBUTES gt newline? INLINE* newline? closetag "CODE" gt
 *   <li>SAMP := lt "SAMP" ATTRIBUTES gt newline? INLINE* newline? closetag "SAMP" gt
 *   <li>KBD := lt "KBD" ATTRIBUTES gt newline? INLINE* newline? closetag "KBD" gt
 *   <li>VAR := lt "VAR" ATTRIBUTES gt newline? INLINE* newline? closetag "VAR" gt
 *   <li>CITE := lt "CITE" ATTRIBUTES gt newline? INLINE* newline? closetag "CITE" gt
 *   <li>ABBR := lt "ABBR" ATTRIBUTES gt newline? INLINE* newline? closetag "ABBR" gt
 *   <li>ACRONYM := lt "ACRONYM" ATTRIBUTES gt newline? INLINE* newline? closetag "ACRONYM" gt
 *   <li>SPECIAL := A | IMG | OBJECT | BR | SCRIPT | MAP | Q | SUB | SUP | SPAN | BDO
 *   <li>A := lt "A" ATTRIBUTES gt newline? INLINE* newline? closetag "A" gt
 *   <li>IMG := lt "IMG" ATTRIBUTES gt
 *   <li>OBJECT := lt "OBJECT" ATTRIBUTES gt newline? (PARAM | FLOW)* newline? closetag "OBJECT" gt
 *   <li>PARAM := lt "PARAM" ATTRIBUTES gt
 *   <li>BR := lt "BR" ATTRIBUTES gt
 *   <li>MAP := lt "MAP" ATTRIBUTES gt newline? (BLOCK | AREA)+ newline? closetag "MAP" gt
 *   <li>AREA := lt "AREA" ATTRIBUTES gt
 *   <li>LINK := lt "LINK" ATTRIBUTES gt
 *   <li>Q := lt "Q" ATTRIBUTES gt newline? INLINE* newline? closetag "Q" gt
 *   <li>SUB := lt "SUB" ATTRIBUTES gt newline? INLINE* newline? closetag "SUB" gt
 *   <li>SUP := lt "SUP" ATTRIBUTES gt newline? INLINE* newline? closetag "SUP" gt
 *   <li>SPAN := lt "SPAN" ATTRIBUTES gt newline? INLINE* newline? closetag "SPAN" gt
 *   <li>BDO := lt "BDO" ATTRIBUTES gt newline? INLINE* newline? closetag "BDO" gt
 *   <li>FORMCTRL := INPUT | SELECT | TEXTAREA | LABEL | BUTTON
 *   <li>FLOW := BLOCK | INLINE
 *   <li>HEADING := H1 | H2 | H3 | H4 | H5 | H6
 *   <li>H1 := lt "H1" ATTRIBUTES gt newline? INLINE* newline? closetag "H1" gt
 *   <li>H2 := lt "H2" ATTRIBUTES gt newline? INLINE* newline? closetag "H2" gt
 *   <li>H3 := lt "H3" ATTRIBUTES gt newline? INLINE* newline? closetag "H3" gt
 *   <li>H4 := lt "H4" ATTRIBUTES gt newline? INLINE* newline? closetag "H4" gt
 *   <li>H5 := lt "H5" ATTRIBUTES gt newline? INLINE* newline? closetag "H5" gt
 *   <li>H6 := lt "H6" ATTRIBUTES gt newline? INLINE* newline? closetag "H6" gt
 *   <li>LIST := UL | OL
 *   <li>UL := lt "UL" ATTRIBUTES gt newline? LI+ newline? closetag "UL" gt
 *   <li>OL := lt "OL" ATTRIBUTES gt newline? LI+ newline? closetag "OL" gt
 *   <li>LI := lt "LI" ATTRIBUTES gt newline? FLOW* newline? (closetag "LI" gt)?
 *   <li>PREFORMATTED := PRE
 *   <li>PRE := lt "PRE" ATTRIBUTES gt newline? INLINE* newline? closetag "PRE" gt
 *   <li>DL := lt "DL" ATTRIBUTES gt newline? (DT | DD)+ newline? closetag "DL" gt
 *   <li>DT := lt "DT" ATTRIBUTES gt newline? INLINE* newline? (closetag "DT" gt)?
 *   <li>DD := lt "DD" ATTRIBUTES gt newline? FLOW* newline? (closetag "DD" gt)?
 *   <li>DIV := lt "DIV" ATTRIBUTES gt newline? FLOW* newline? closetag "DIV" gt
 *   <li>BLOCKQUOTE := lt "BLOCKQUOTE" ATTRIBUTES gt newline? (BLOCK | SCRIPT)+ newline? closetag "BLOCKQUOTE" gt
 *   <li>FORM := lt "FORM" ATTRIBUTES gt newline? (BLOCK | SCRIPT)+ newline? closetag "FORM" gt
 *   <li>LABEL := lt "LABEL" ATTRIBUTES gt newline? INLINE* newline? closetag "LABEL" gt
 *   <li>INPUT := lt "INPUT" ATTRIBUTES gt
 *   <LI>SELECT := lt "SELECT" ATTRIBUTES gt newline? (OPTGROUP | OPTION)+ newline? closetag "SELECT" gt
 *   <li>OPTGROUP := lt "OPTGROUP" ATTRIBUTES gt newline? OPTION+ newline? closetag "OPTGROUP" gt
 *   <li>OPTION := lt "OPTION" ATTRIBUTES gt newline? PCDATA newline? (closetag "OPTION" gt)?
 *   <li>TEXTAREA := lt "TEXTAREA" ATTRIBUTES gt newline? PCDATA newline? closetag "TEXTAREA" gt
 *   <li>HR := lt "HR" ATTRIBUTES gt
 *   <li>TABLE := lt "TABLE" ATTRIBUTES gt newline? CAPTION? (COL* | COLGROUP*) THEAD? TFOOT? TBODY+ newline? closetag "TABLE" gt
 *   <li>CAPTION := lt "CAPTION" ATTRIBUTES gt newline? INLINE* newline? closetag "CAPTION" gt
 *   <li>COL := lt "COL" ATTRIBUTES gt
 *   <li>COLGROUP := lt "COLGROUP" ATTRIBUTES gt newline? COL* newline? (closetag "COLGROUP" gt)?
 *   <li>THEAD := lt "THEAD" ATTRIBUTES gt newline? TR+ newline? (closetag "THEAD" gt)?
 *   <li>TFOOT := lt "TFOOT" ATTRIBUTES gt newline? TR+ newline? (closetag "TFOOT" gt)?
 *   <li>TBODY := (lt "TBODY" ATTRIBUTES gt)? newline? TR+ newline? (closetag "TBODY" gt)?
 *   <li>TR := lt "TR" ATTRIBUTES gt newline? (TH | TD)+ newline? (closetag "TR" gt)?
 *   <li>TH := lt "TH" ATTRIBUTES gt newline? FLOW* newline? (closetag "TH" gt)?
 *   <li>TD := lt "TD" ATTRIBUTES gt newline? FLOW* newline? (closetag "TD" gt)?
 *   <li>FIELDSET := lt "FIELDSET" ATTRIBUTES gt newline? PCDATA LEGEND FLOW* closetag "FIELDSET" gt
 *   <li>LEGEND := lt "LEGEND" ATTRIBUTES gt newline? INLINE* newline? closetag "LEGEND" gt
 *   <li>BUTTON := lt "BUTTON" ATTRIBUTES gt newline? FLOW* newline? closetag "BUTTON" gt
 *   <li>ADDRESS := lt "ADDRESS" ATTRIBUTES gt newline? INLINE* newline? closetag "ADDRESS" gt
 *   <li>INS := lt "INS" ATTRIBUTES gt newline? FLOW* newline? closetag "INS" gt
 *   <li>DEL := lt "DEL" ATTRIBUTES gt newline? FLOW* newline? closetag "DEL" gt
 * </ul>
 */
package org.silnith.browser.organic.parser.html4.grammar;
