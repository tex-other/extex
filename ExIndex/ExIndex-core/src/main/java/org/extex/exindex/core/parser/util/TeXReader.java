/*
 * Copyright (C) 2008 The ExTeX Group and individual authors listed below
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

package org.extex.exindex.core.parser.util;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Reader which translates ^^-Notation on the fly.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class TeXReader extends FilterReader {

    /**
     * The field <tt>save</tt> contains the saved character or -1 for none.
     */
    private int save = -1;

    /**
     * Creates a new object.
     * 
     * @param reader the reader to use for input
     */
    public TeXReader(Reader reader) {

        super(reader);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.io.FilterReader#read()
     */
    @Override
    public int read() throws IOException {

        int c;
        if (save < 0) {
            c = super.read();
        } else {
            c = save;
            save = -1;
        }
        if (c != '^') {
            return c;
        }
        c = super.read();
        if (c != '^') {
            save = c;
            return '^';
        }
        int ret = 0;
        c = super.read();
        if ('0' <= c && c <= '9') {
            ret = c - '0';
        } else if ('a' <= c && c <= 'f') {
            ret = c + 10 - 'a';
        } else {
            return (c < 64 ? c + 64 : c - 64);
        }
        c = super.read();
        if ('0' <= c && c <= '9') {
            ret = (ret << 4) + c - '0';
        } else if ('a' <= c && c <= 'f') {
            ret = (ret << 4) + c - 'a';
        } else {
            save = c;
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.io.Reader#read(char[], int, int)
     */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {

        int i = 0;
        for (; i < len; i++) {
            int c = read();
            if (c < 0) {
                break;
            }
            cbuf[off + i] = (char) c;
        }
        return i;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.io.FilterReader#skip(long)
     */
    @Override
    public long skip(long n) throws IOException {

        for (long i = 0; i < n; i++) {
            if (read() < 0) {
                return i;
            }
        }
        return n;
    }

}