/*
 * Copyright (C) 2003-2008 The ExTeX Group and individual authors listed below
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

package org.extex.exbib.core.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.extex.framework.configuration.Configurable;
import org.extex.framework.configuration.Configuration;
import org.extex.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a writer with a target in an outputStream.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class StreamWriter implements Writer, Configurable {

    /**
     * The field <tt>writer</tt> contains the output writer.
     */
    private OutputStreamWriter writer = null;

    /**
     * Creates a new object.
     * 
     * @param stream the output stream
     * @param encoding the encoding to use for writing
     * 
     * @throws UnsupportedEncodingException in case that the given encoding is
     *         undefined
     */
    public StreamWriter(PrintStream stream, String encoding)
            throws UnsupportedEncodingException {

        super();

        if (encoding == null) {
            writer = new OutputStreamWriter(stream);
        } else {
            writer = new OutputStreamWriter(stream, encoding);
        }
    }

    /**
     * Creates a new object.
     * 
     * @param file the name of the file to write to
     * @param encoding the encoding to use for writing
     * 
     * @throws FileNotFoundException in case that the file could not be opened
     *         for writing
     * @throws UnsupportedEncodingException in case that the given encoding is
     *         undefined
     */
    public StreamWriter(String file, String encoding)
            throws FileNotFoundException,
                UnsupportedEncodingException {

        super();

        FileOutputStream stream = new FileOutputStream(file);

        if (encoding == null) {
            writer = new OutputStreamWriter(stream);
        } else {
            writer = new OutputStreamWriter(stream, encoding);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.io.Writer#close()
     */
    public void close() throws IOException {

        writer.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.bst.code.AbstractCode#configure(
     *      org.extex.framework.configuration.Configuration)
     */
    public void configure(Configuration cfg) throws ConfigurationException {

        //
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.io.Writer#flush()
     */
    public void flush() throws IOException {

        writer.flush();
    }

    /**
     * Getter for the print stream.
     * 
     * @return the print stream
     */
    public PrintWriter getPrintWriter() {

        return new PrintWriter(writer, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.io.Writer#print(java.lang.String)
     */
    public void print(String s) throws IOException {

        writer.write(s);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.io.Writer#print(java.lang.String,
     *      java.lang.String)
     */
    public void print(String s1, String s2) throws IOException {

        writer.write(s1);
        writer.write(s2);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.io.Writer#print(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void print(String s1, String s2, String s3) throws IOException {

        writer.write(s1);
        writer.write(s2);
        writer.write(s3);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.io.Writer#println()
     */
    public void println() throws IOException {

        writer.write("\n");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.io.Writer#println(java.lang.String)
     */
    public void println(String s) throws IOException {

        writer.write(s);
        writer.write("\n");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.io.Writer#println(java.lang.String,
     *      java.lang.String)
     */
    public void println(String s1, String s2) throws IOException {

        writer.write(s1);
        writer.write(s2);
        writer.write("\n");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.io.Writer#println(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void println(String s1, String s2, String s3) throws IOException {

        writer.write(s1);
        writer.write(s2);
        writer.write(s3);
        writer.write("\n");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.exbib.core.io.Writer#write(int)
     */
    public void write(int c) throws IOException {

        writer.write(c);
    }
}
