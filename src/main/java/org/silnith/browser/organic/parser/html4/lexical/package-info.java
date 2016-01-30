/**
 * Lexical analysis for HTML 4.01.
 * <ul>
 * <li>namebody := [a-zA-Z0-9_.-:]
 * <li>name := [a-zA-Z](namebody)+
 * <li>lt := <
 * <li>gt := >
 * <li>slash := /
 * <li>leftbracket := [
 * <li>rightbracket := ]
 * <li>singlequote := '
 * <li>doublequote := "
 * <li>amp := &
 * <li>semicolon := ;
 * <li>dash := -
 * <li>bang := !
 * <li>newline := \n
 * <li>whitespace := [ \r\t]+
 * <li>text :=
 * [^(lt)(gt)(slash)(leftbracket)(rightbracket)(singlequote)(doublequote)(amp)(
 * semicolon)(dash)(bang)(whitespace)]
 * </ul>
 * <ul>
 * <li>name := [a-zA-Z][a-zA-Z0-9_.-:]*
 * <li>word := [a-zA-Z0-9_.-:]+
 * <li>markupdecl := <!
 * <li>processinginstr := <?
 * <li>closetag := </<li>endblock := ]]>
 * <li>commentdelim := --
 * <li>opentag := <
 * </ul>
 */
package org.silnith.browser.organic.parser.html4.lexical;
