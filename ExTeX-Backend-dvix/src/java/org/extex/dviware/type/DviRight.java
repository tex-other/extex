/*
 * Copyright (C) 2006-2007 The ExTeX Group and individual authors listed below
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

package org.extex.dviware.type;

import java.io.IOException;
import java.io.OutputStream;

import org.extex.dviware.Dvi;

/**
 * This class represents the DVI instruction <tt>right</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class DviRight extends AbstractDviCode {

    /**
     * The field <tt>dist</tt> contains the the distance to move rightwards.
     */
    private int dist;

    /**
     * Creates a new object.
     *
     * @param dist the distance to move rightwards
     */
    public DviRight(final int dist) {

        super("right" + variant(dist));
        this.dist = dist;
    }

    /**
     * Add some value to the move distance.
     *
     * @param x the value to add
     */
    public void add(final int x) {

        dist += x;
    }

    /**
     * Write the code to the output stream.
     *
     * @param stream the target stream
     *
     * @return the number of bytes actually written
     *
     * @throws IOException in case of an error
     *
     * @see org.extex.dviware.type.DviCode#write(java.io.OutputStream)
     */
    public int write(final OutputStream stream) throws IOException {

        return opcodeSigned(Dvi.RIGHT1, (int) dist, stream);
    }

}
