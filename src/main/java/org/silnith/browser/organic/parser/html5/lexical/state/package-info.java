/**
 * Implementations must act as if they used the following state machine to
 * tokenize HTML. The state machine must start in the
 * {@link org.silnith.browser.organic.parser.html5.lexical.state.DataState data state}.
 * Most states consume a single character, which may have various side-effects,
 * and either switches the state machine to a new state to <dfn>reconsume</dfn>
 * the same character, or switches it to a new state to consume the next
 * character, or stays in the same state to consume the next character. Some
 * states have more complicated behavior and can consume several characters
 * before switching to another state. In some cases, the tokenizer state is also
 * changed by the tree construction stage.
 * <p>
 * The exact behavior of certain states depends on the insertion mode and the
 * stack of open elements. Certain states also use a <var>temporary buffer</var>
 * to track progress.
 * <p>
 * The output of the tokenization step is a series of zero or more of the
 * following tokens:
 * {@link org.silnith.browser.organic.parser.html5.lexical.token.DOCTYPEToken DOCTYPE},
 * {@link org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken start tag},
 * {@link org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken end tag},
 * {@link org.silnith.browser.organic.parser.html5.lexical.token.CommentToken comment},
 * {@link org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken character},
 * {@link org.silnith.browser.organic.parser.html5.lexical.token.EndOfFileToken end-of-file}.
 * DOCTYPE tokens have a name, a public identifier, a system identifier, and a
 * <dfn>force-quirks flag</dfn>. When a DOCTYPE token is created, its name,
 * public identifier, and system identifier must be marked as missing (which is
 * a distinct state from the empty string), and the <var>force-quirks flag</var>
 * must be set to <var>off</var> (its other state is <var>on</var>). Start and
 * end tag tokens have a tag name, a <var>self-closing flag</var>, and a list of
 * attributes, each of which has a name and a value. When a start or end tag
 * token is created, its <var>self-closing flag</var> must be unset (its other
 * state is that it be set), and its attributes list must be empty. Comment and
 * character tokens have data.
 * <p>
 * When a token is emitted, it must immediately be handled by the tree
 * construction stage. The tree construction stage can affect the state of the
 * tokenization stage, and can insert additional characters into the stream.
 * (For example, the script element can result in scripts executing and using
 * the dynamic markup insertion APIs to insert characters into the stream being
 * tokenized.)
 * <p>
 * <p><strong>Note:</strong> Creating a token and emitting it are distinct
 * actions. It is possible for a token to be created but implicitly abandoned
 * (never emitted), e.g. if the file ends unexpectedly while processing the
 * characters that are being parsed into a start tag token.
 * <p>
 * When a start tag token is emitted with its <var>self-closing flag</var> set,
 * if the flag is not <dfn>acknowledged</dfn> when it is processed by the tree
 * construction stage, that is a parse error.
 * <p>
 * When an end tag token is emitted with attributes, that is a parse error.
 * <p>
 * When an end tag token is emitted with its <var>self-closing flag</var> set,
 * that is a parse error.
 * <p>
 * <p>An <dfn>appropriate end tag token</dfn> is an end tag token whose tag name
 * matches the tag name of the last start tag to have been emitted from this
 * tokenizer, if any. If no start tag has been emitted from this tokenizer, then
 * no end tag token is appropriate.
 * <p>
 * Before each step of the tokenizer, the user agent must first check the
 * <var>parser pause flag</var>. If it is true, then the tokenizer must abort
 * the processing of any nested invocations of the tokenizer, yielding control
 * back to the caller.
 * 
 * @see <a href="https://www.w3.org/TR/html5/syntax.html#tokenization">8.2.4 Tokenization</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
package org.silnith.browser.organic.parser.html5.lexical.state;
