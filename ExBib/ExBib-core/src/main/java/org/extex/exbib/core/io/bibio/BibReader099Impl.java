/*
 * This file is part of ExBib a BibTeX compatible database.
 * Copyright (C) 2003-2008 Gerd Neugebauer
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package org.extex.exbib.core.io.bibio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.extex.exbib.core.bst.Processor;
import org.extex.exbib.core.bst.exception.ExBibMissingEntryTypeException;
import org.extex.exbib.core.db.DB;
import org.extex.exbib.core.db.Entry;
import org.extex.exbib.core.db.KeyValue;
import org.extex.exbib.core.db.VBlock;
import org.extex.exbib.core.db.VMacro;
import org.extex.exbib.core.db.VNumber;
import org.extex.exbib.core.db.VString;
import org.extex.exbib.core.db.Value;
import org.extex.exbib.core.exceptions.ExBibEofException;
import org.extex.exbib.core.exceptions.ExBibEofInBlockException;
import org.extex.exbib.core.exceptions.ExBibEofInStringException;
import org.extex.exbib.core.exceptions.ExBibException;
import org.extex.exbib.core.exceptions.ExBibMissingAttributeNameException;
import org.extex.exbib.core.exceptions.ExBibMissingKeyException;
import org.extex.exbib.core.exceptions.ExBibSyntaxException;
import org.extex.exbib.core.exceptions.ExBibUnexpectedException;
import org.extex.exbib.core.exceptions.ExBibUnexpectedOfException;
import org.extex.exbib.core.io.AbstractFileReader;
import org.extex.exbib.core.io.Locator;
import org.extex.framework.configuration.exception.ConfigurationException;

/**
 * The BibReader099Impl is a class to implement a reader for BibT<sub>E</sub>X
 * files. This reader is compatible with BibT<sub>E</sub>X 0.99c.
 * <p>
 * This incarnation is characterized as follows:
 * </p>
 * <ul>
 * <li>The special tag <code>@string</code> can be used to define a macro.</li>
 *         <li>The special tag <code>@preamble</code> can be used to define the preamble.</li>
 *           <li>The special tag <code>@comment</code> is silently ignored.</li>
 *          <li>Any characters between entries are considered to be comments
 *          and ignored silently.</li>
 * <li>There is no means to escape the @ in comments.</li>
 *          <li>Either {} or () can be used as delimiters</li>
 *          </ul>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class BibReader099Impl extends AbstractFileReader implements BibReader {

    /**
     * The constant <tt>LHS_PATTERN</tt> contains the pattern for a left hand
     * side of an assignment.
     */
    private static final Pattern LHS_PATTERN =
            Pattern.compile("[^= \t\n\r\b\f]*");

    /**
     * The constant <tt>RECORD_PATTERN</tt> contains the pattern for the name
     * of a record.
     */
    private static final Pattern RECORD_PATTERN =
            Pattern.compile("[a-zA-Z_.:0-9-]*");

    /**
     * The constant <tt>KEY_PATTERN</tt> contains the pattern for the key of
     * an entry.
     */
    private static final Pattern KEY_PATTERN =
            Pattern.compile("[^ \t\n\r\f\b,(){}]*");

    /**
     * Creates a new object.
     * 
     * @throws FileNotFoundException is thrown if the file could not be opened
     *         for reading
     * @throws ConfigurationException in case that the configuration is invalid
     */
    public BibReader099Impl()
            throws FileNotFoundException,
                ConfigurationException {

        super();
    }

    /**
     * Test whether the given character is allowed for a macro name constituent.
     * Any character is allowed except whitespace and one of the characters '{',
     * '}', '#', '(', ')', ',', and '"'.
     * 
     * @param c the character to categorize
     * 
     * @return <code>true</code> iff the character is allowed
     */
    private boolean allowed(char c) {

        switch (c) {
            case '{':
            case '}':
            case '#':
            case '(':
            case ')':
            case ',':
            case '"':
                return false;
        }

        return !Character.isWhitespace(c);
    }

    /**
     * Expect one of several characters from the argument string as next
     * non-space character to be read. If the expected character can not be
     * acquired then an ExBibSyntaxException is thrown.
     * 
     * @param chars the String containing the acceptable characters
     * 
     * @return the character actually found
     * 
     * @throws ExBibEofException in case of an unexpected end of file
     * @throws ExBibSyntaxException in case of an syntax error
     */
    protected char expect(String chars)
            throws ExBibEofException,
                ExBibSyntaxException {

        char c = parseNextNonSpace(false);

        if (c == '\0') {
            throw new ExBibUnexpectedOfException(chars, null, getLocator());
        }

        if (chars.indexOf(c) >= 0) {
            return c;
        }

        throw new ExBibUnexpectedOfException(chars, Character.toString(c),
            getLocator());
    }

    /**
     * This is the extension mechanism to define handlers for special items in
     * the file.
     * <p>
     * Any item name is passed to this handler to be processed.
     * </p>
     * <p>
     * This method is meant to be overwritten in derived classes to implement
     * additional special items. As an example consider the following code:
     * </p>
     * 
     * <pre>
     *   protected boolean handle(String tag, DB db, Processor processor,
     *                            String brace) throws ExBibException {
     *     if (super.handle(tag,db,processor,brace)) {
     *     } else if (tag.equals("new_tag")) {
     *       . . .
     *     }
     *   }
     * </pre
     *
     * @param tag the name of the item encountered. This String has been
     *        converted to lower case already.
     * @param db the database to store the information in
     * @param processor the processor context
     * @param brace the String expected as terminating brace, i.e. ')' or '}'
     *        depending on the opening brace
     * @param locator the locator
     * 
     * @return <code>true</code> iff the item is special and has been handled
     *         successfully.
     *
     * @throws ExBibException in case of an error
     * @throws ExBibEofException in case of an unexpected end of file
     * @throws ExBibSyntaxException in case of an syntax error
     */
    protected boolean handle(String tag, DB db, Processor processor,
            String brace, Locator locator) throws ExBibException {

        if (tag.equals("string")) {
            KeyValue pair = parseAssign();
            expect(brace);
            db.storeString(pair.getKey(), pair.getValue());

            return true;
        } else if (tag.equals("preamble")) {
            Value pre = parseRHS();
            expect(brace);
            db.storePreamble(pre);

            return true;
        }

        return false;
    }

    /**
     * Parse the given file and store the result into the database.
     * 
     * @param db the database to store the records in
     * @param processor the processor context
     * 
     * @throws ExBibException in case of an error
     * @throws ExBibSyntaxException in case that something goes wrong
     */
    public void load(DB db, Processor processor) throws ExBibException {

        StringBuffer comment = new StringBuffer();
        String tag;

        while ((tag = skipToAtTag(comment)) != null) {
            if (tag.equals("")) {
                throw new ExBibMissingEntryTypeException(null, getLocator());
            }

            String s = tag.toLowerCase();

            if (s.equals("comment")) {
                comment.append(tag);
            } else {
                db.storeComment(comment.toString());
                comment.delete(0, comment.length());

                String brace = (expect("({") == '(' ? ")" : "}");

                if (!handle(s, db, processor, brace, getLocator())) {
                    String key = parseKey();

                    if (key.equals("")) {
                        throw new ExBibMissingKeyException(null, getLocator());
                    }

                    Entry entry = db.makeEntry(s, key, getLocator());
                    char c;

                    for (c = parseNextNonSpace(false); c == ','; c =
                            parseNextNonSpace(false)) {
                        c = parseNextNonSpace(true);

                        if (c == '}' || c == ')') {
                            break;
                        }

                        KeyValue pair = parseAssign();
                        entry.set(pair.getKey(), pair.getValue());
                    }

                    if (c != brace.charAt(0)) {
                        if (c < 0) {
                            throw new ExBibUnexpectedOfException(brace, null,
                                getLocator());
                        }

                        throw new ExBibUnexpectedOfException(brace, Character
                            .toString(c), getLocator());
                    }
                }
            }
        }

        try {
            close();
        } catch (IOException e) {
            // shit happens
        }
    }

    /**
     * @see org.extex.exbib.core.io.bibio.BibReader#open(java.lang.String)
     */
    public void open(String file)
            throws ConfigurationException,
                FileNotFoundException {

        open(file, "bib");
    }

    /**
     * Parses an assignment, i.e. a name followed by an equality sign and a
     * right hand side.
     * 
     * @return the pair of name and value
     * 
     * @throws ExBibEofException in case of an unexpected end of file
     * @throws ExBibSyntaxException in case of a syntax error
     */
    protected KeyValue parseAssign()
            throws ExBibEofException,
                ExBibSyntaxException {

        String lhs = parseToken(LHS_PATTERN);

        if (lhs.equals("")) {
            throw new ExBibMissingAttributeNameException(getLocator());
        }

        expect("=");

        return new KeyValue(lhs, parseRHS());
    }

    /**
     * Parses a key of an entry.
     * 
     * @return the key found or <code>null</code>
     */
    protected String parseKey() {

        return parseToken(KEY_PATTERN);
    }

    /**
     * Reads ahead until a non-space character is found.
     * 
     * @param lookahead if <code>true</code> then the character found is left
     *        in the input for the next read
     * 
     * @return the next character or '\0' if none can be acquired
     */
    protected char parseNextNonSpace(boolean lookahead) {

        StringBuffer buffer = getBuffer();

        for (;;) {
            int i = 0;

            while (i < buffer.length()) {
                char c = buffer.charAt(i);

                if (!Character.isWhitespace(c)) {
                    buffer.delete(0, lookahead ? i : i + 1);
                    return c;
                }

                i++;
            }

            if (read() == null) {
                return '\0';
            }
        }
    }

    /**
     * Parse the right hand side of an assignment. The right hand side can be a
     * list of strings, blocks, and numbers separated by a hash mark (#).
     * 
     * @return the value found as the right hand side
     * 
     * @throws ExBibEofException in case of an unexpected end of file
     * @throws ExBibSyntaxException in case of a syntax error
     */
    protected Value parseRHS() throws ExBibEofException, ExBibSyntaxException {

        Value ret = new Value();

        for (;;) {
            char c = parseNextNonSpace(true);

            if (c == '\0') {
                throw new ExBibEofException("", getLocator());
            }

            StringBuffer buffer = getBuffer();

            if (c == '{') {
                int i = 1;

                for (int depth = 1; depth > 0;) {
                    if (i >= buffer.length()) {
                        if (read() == null) {
                            throw new ExBibEofInBlockException(getLocator());
                        }
                    }

                    c = buffer.charAt(i++);

                    if (c == '{') {
                        depth++;
                    } else if (c == '}') {
                        depth--;
                    }
                }

                ret.add(new VBlock(buffer.substring(1, i - 1)));
                buffer.delete(0, i);
            } else if (c == '"') {
                int i = 1;
                int depth = 0;

                do {
                    if (i > buffer.length()) {
                        if (read() == null) {
                            throw new ExBibEofInStringException(getLocator());
                        }
                    }

                    c = buffer.charAt(i++);

                    if (c == '{') {
                        depth++;
                    } else if (c == '}') {
                        depth--;
                    }
                } while (c != '"' || depth != 0);

                ret.add(new VString(buffer.substring(1, i - 1)));
                buffer.delete(0, i);
            } else if (Character.isDigit(c)) {
                int i = 1;

                while (i < buffer.length()
                        && Character.isDigit(buffer.charAt(i))) {
                    i++;
                }

                ret.add(new VNumber(buffer.substring(0, i)));
                buffer.delete(0, i);
            } else if (c == '=' || c == '#' || c == '}' || c == '(' || c == ')') {
                throw new ExBibUnexpectedException(Character.toString(c), null,
                    getLocator());
            } else {
                int i = 1;

                while (i < buffer.length() && allowed(buffer.charAt(i))) {
                    i++;
                }

                ret.add(new VMacro(buffer.substring(0, i)));
                buffer.delete(0, i);
            }

            c = parseNextNonSpace(true);

            if (c == '\0' || c != '#') {
                break;
            }

            parseNextNonSpace(false);
        }

        return ret;
    }

    /**
     * Parses a token according to a given pattern.
     * 
     * @param pattern the pattern for the token
     * 
     * @return the token found
     */
    protected String parseToken(Pattern pattern) {

        StringBuffer buffer = getBuffer();

        if (buffer == null) {
            return null;
        }

        int i = 0;

        for (;;) {
            while (i < buffer.length()
                    && Character.isWhitespace(buffer.charAt(i))) {
                i++;
            }

            if (i < buffer.length()) {
                buffer.delete(0, i);
                break;
            }

            buffer = read();

            if (buffer == null) {
                setBuffer(null);

                return null;
            }
        }

        Matcher matcher = pattern.matcher(buffer);

        if (matcher.lookingAt()) {
            String val = buffer.substring(0, matcher.end());
            buffer.delete(0, matcher.end());
            return val;
        }

        return null;
    }

    /**
     * Collect all characters up to the next at character (@) and the tag
     * following. The characters before the @ are stored in the StringBuffer
     * given as argument.
     * 
     * @param comment the comment to add the ignored characters to
     * 
     * @return the tag found or <code>null</code>
     */
    private String skipToAtTag(StringBuffer comment) {

        StringBuffer buffer = getBuffer();
        int i;

        if (buffer == null) {
            return null;
        }

        while ((i = buffer.indexOf("@")) < 0) {
            comment.append(buffer);

            if (read() == null) {
                return null;
            }
        }

        comment.append(buffer.substring(0, i));
        buffer.delete(0, i + 1);
        return parseToken(RECORD_PATTERN);
    }
}