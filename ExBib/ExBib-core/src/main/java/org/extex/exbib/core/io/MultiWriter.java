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

package org.extex.exbib.core.io;

import java.io.IOException;
import java.io.PrintWriter;

import org.extex.framework.configuration.Configuration;
import org.extex.framework.configuration.exception.ConfigurationException;

/**
 * This writer is a means to print something to two writers.
 * 
 * @author gene
 * @version $Revision: 1.3 $
 */
public class MultiWriter implements Writer {

    /** the first writer */
    private Writer w1;

    /** the second writer */
    private Writer w2;

    /**
     * Creates a new object.
     * 
     * @param a the first writer
     * @param b the second writer
     */
    public MultiWriter(Writer a, Writer b) {

        super();
        w1 = a;
        w2 = b;
    }

    /**
     * @see org.extex.exbib.core.io.Writer#close()
     */
    public void close() throws IOException {

        w1.close();
        w2.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.bst.code.AbstractCode#configure(
     *      org.extex.framework.configuration.Configuration)
     */
    public void configure(Configuration config) throws ConfigurationException {

        w1.configure(config);
        w2.configure(config);
    }

    /**
     * @see org.extex.exbib.core.io.Writer#flush()
     */
    public void flush() throws IOException {

        w1.flush();
        w2.flush();
    }

    /**
     * @see org.extex.exbib.core.io.Writer#getPrintWriter()
     */
    public PrintWriter getPrintWriter() {

        return null;
    }

    /**
     * @see org.extex.exbib.core.io.Writer#print(java.lang.String)
     */
    public void print(String s) throws IOException {

        w1.print(s);
        w2.print(s);
    }

    /**
     * @see org.extex.exbib.core.io.Writer#print(java.lang.String,
     *      java.lang.String)
     */
    public void print(String s1, String s2) throws IOException {

        w1.print(s1, s2);
        w2.print(s1, s2);
    }

    /**
     * @see org.extex.exbib.core.io.Writer#print(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void print(String s1, String s2, String s3) throws IOException {

        w1.print(s1, s2, s3);
        w2.print(s1, s2, s3);
    }

    /**
     * @see org.extex.exbib.core.io.Writer#println()
     */
    public void println() throws IOException {

        w1.println();
        w2.println();
    }

    /**
     * @see org.extex.exbib.core.io.Writer#println(java.lang.String)
     */
    public void println(String s) throws IOException {

        w1.println(s);
        w2.println(s);
    }

    /**
     * @see org.extex.exbib.core.io.Writer#println(java.lang.String,
     *      java.lang.String)
     */
    public void println(String s1, String s2) throws IOException {

        w1.println(s1, s2);
        w2.println(s1, s2);
    }

    /**
     * @see org.extex.exbib.core.io.Writer#println(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void println(String s1, String s2, String s3) throws IOException {

        w1.println(s1, s2, s3);
        w2.println(s1, s2, s3);
    }

    /**
     * @see org.extex.exbib.core.io.Writer#write(int)
     */
    public void write(int c) throws IOException {

        w1.write(c);
        w2.write(c);
    }
}