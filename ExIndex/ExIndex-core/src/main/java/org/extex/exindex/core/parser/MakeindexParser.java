/*
 * Copyright (C) 2007 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package org.extex.exindex.core.parser;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.extex.exindex.core.Index;
import org.extex.exindex.core.Parameters;
import org.extex.exindex.core.exception.EofException;
import org.extex.exindex.core.exception.MissingException;
import org.extex.exindex.core.normalizer.Collator;
import org.extex.exindex.core.type.Entry;
import org.extex.exindex.core.type.LowerPage;
import org.extex.exindex.core.type.LowerRomanPage;
import org.extex.exindex.core.type.NumericPage;
import org.extex.exindex.core.type.PageReference;
import org.extex.exindex.core.type.SomePage;
import org.extex.exindex.core.type.UpperPage;
import org.extex.exindex.core.type.UpperRomanPage;

/**
 * TODO gene: missing JavaDoc.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class MakeindexParser implements Parser {

    /**
     * Creates a new object.
     */
    public MakeindexParser() {

        super();
    }

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param r the reader
     * @param ec the expected character
     * 
     * @throws IOException
     */
    private void expect(Reader r, char ec) throws IOException {

        int c = r.read();
        if (c != ec) {
            throw new MissingException((char) c, ec);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exindex.core.parser.Parser#load(java.io.Reader,
     *      java.lang.String, org.extex.exindex.core.Index, Collator)
     */
    public int[] load(Reader reader, String resource, Index index,
            Collator collator) throws IOException {

        int[] count = new int[2];
        Parameters params = index.getParams();
        Reader r = new LineNumberReader(reader);
        final String keyword = params.getString("keyword");
        final char argOpen = params.getChar("arg_open");
        final char argClose = params.getChar("arg_close");
        final char escape = params.getChar("escape");
        final char quote = params.getChar("quote");
        final char encap = params.getChar("encap");
        final char level = params.getChar("level");
        final char actual = params.getChar("actual");
        char k0 = keyword.charAt(0);
        try {

            for (int c = r.read(); c >= 0; c = r.read()) {
                if (c == k0 && scanKeyword(r, keyword)) {
                    String arg =
                            scanArgument(r, argOpen, argClose, escape, quote,
                                index);
                    String p =
                            scanArgument(r, argOpen, argClose, escape, quote,
                                index);
                    String enc = null;
                    int x = arg.lastIndexOf(encap);
                    if (x >= 0) {
                        enc = arg.substring(x + 1);
                        arg = arg.substring(0, x);
                    }
                    x = arg.indexOf(actual);
                    String a;
                    if (x >= 0) {
                        a = arg.substring(x + 1);
                        arg = arg.substring(0, x);
                    } else {
                        a = arg;
                    }
                    if (collator != null) {
                        a = collator.collate(a);
                    }
                    store(index, arg, p, enc, a, level);
                    count[0]++;
                }
            }
        } finally {
            r.close();
        }
        return count;
    }

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param r the reader
     * @param argOpen
     * @param argClose
     * @param quote
     * @param escape
     * @param index the index
     * 
     * @return the argument found
     * 
     * @throws IOException in case of an error
     */
    private String scanArgument(Reader r, char argOpen, char argClose,
            char escape, char quote, Index index) throws IOException {

        expect(r, argOpen);
        StringBuilder sb = new StringBuilder();

        for (int level = 1;;) {
            int c = r.read();
            if (c < 0) {
                throw new EofException();
            } else if (c == argOpen) {
                level++;
            } else if (c == argClose) {
                level--;
                if (level <= 0) {
                    break;
                }
            } else if (c == quote) {
                int l = sb.length();
                if (l <= 0 || sb.charAt(l - 1) != escape) {
                    c = r.read();
                    if (c < 0) {
                        throw new EofException();
                    }
                }
            }
            sb.append((char) c);
        }

        return sb.toString();
    }

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param r the reader
     * @param keyword the keyword to read
     * 
     * @return <code>true</code> iff the keyword has been found
     * 
     * @throws IOException in case of an I/O error
     */
    private boolean scanKeyword(Reader r, String keyword) throws IOException {

        int length = keyword.length();
        for (int i = 1; i < length; i++) {
            int c = r.read();
            if (c != keyword.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param index the index
     * @param a1
     * @param p the page
     * @param encap the page encapsulator or <code>null</code> for none
     * @param display the display representation
     * @param level the level separating character
     */
    private void store(Index index, String a1, String p, String encap,
            String display, char level) {

        String key = a1;
        List<String> list = new ArrayList<String>();
        int a = 0;
        for (int i = key.indexOf(level, a); i >= 0; i = key.indexOf(level, a)) {
            list.add(key.substring(a, i));
            a = i + 1;
        }
        list.add(key);
        String[] k = new String[list.size()];
        int i = 0;
        for (String s : list) {
            k[i] = s;
        }

        PageReference page;
        if (p.matches("^[0-9]+$")) {
            page = new NumericPage(encap, p);
        } else if (p.matches("^[IVXLCM]+$")) {
            page = new UpperRomanPage(encap, p);
        } else if (p.matches("^[ivxlcm]+$")) {
            page = new LowerRomanPage(encap, p);
        } else if (p.matches("^[A-Z]+$")) {
            page = new UpperPage(encap, p);
        } else if (p.matches("^[a-z]+$")) {
            page = new LowerPage(encap, p);
        } else {
            page = new SomePage(encap, p);
        }

        Entry entry = new Entry(k, display, page);
        index.add(entry);
    }

}