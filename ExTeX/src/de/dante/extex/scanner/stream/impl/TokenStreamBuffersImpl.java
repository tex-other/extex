/*
 * Copyright (C) 2004 Gerd Neugebauer
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
package de.dante.extex.scanner.stream.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import de.dante.extex.scanner.stream.TokenStream;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de"> Gerd Neugebauer </a>
 *
 * @version $Revision$
 */
public class TokenStreamBuffersImpl extends TokenStreamBufferImpl implements
        TokenStream {

    /**
     * Creates a new object.
     */
    protected TokenStreamBuffersImpl() {
        super();
    }

    /**
     * Creates a new object.
     *
     * @param line
     * @param encoding
     * @throws CharacterCodingException
     */
    public TokenStreamBuffersImpl(String line, String encoding)
            throws CharacterCodingException {
        super(line, encoding);
    }

    /**
     * The field <tt>nextLine</tt> ...
     */
    private int nextLine = 1;

    /**
     * The field <tt>lines</tt> ...
     */
    private String[] lines = null;

    /**
     * The field <tt>encoding</tt> contains the ...
     */
    private String encoding;

    /**
     * Creates a new object.
     *
     * @param lines the array of lines to consider
     * @throws CharacterCodingException in cas of an error
     */
    public TokenStreamBuffersImpl(final String[] lines, final String encoding)
            throws CharacterCodingException {
        super(lines[0], encoding);
        this.lines = lines;
        this.encoding = encoding;
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl#refill()
     */
    protected boolean refill() throws IOException {
        if (lines == null || nextLine >= lines.length) {
            return false;
        }
        setBuffer(Charset.forName(encoding).newDecoder().decode(
                ByteBuffer.wrap(lines[nextLine++].getBytes())));
        return true;
    }

}
