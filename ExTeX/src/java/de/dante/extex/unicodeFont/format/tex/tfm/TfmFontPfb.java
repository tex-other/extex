/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.tex.tfm;

import java.io.InputStream;

import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.format.pfb.PfbParser;
import de.dante.extex.unicodeFont.type.FontPfb;

/**
 * This class is a <code>TfmFont</code> which use a pfb file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */

public class TfmFontPfb extends TfmFont implements FontPfb {

    /**
     * Create a new object.
     */
    public TfmFontPfb() {

        super();
    }

    /**
     * The pdf parser.
     */
    private PfbParser pdfParser;

    /**
     * @see de.dante.extex.unicodeFont.type.FontPfb#getPfb()
     */
    public PfbParser getPfb() {

        return pdfParser;
    }

    /**
     * @see de.dante.extex.unicodeFont.type.FontPfb#setPfb(java.io.InputStream)
     */
    public void setPfb(final InputStream in) throws FontException {

        pdfParser = new PfbParser(in);
    }

}
