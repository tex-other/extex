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

package org.extex.font.format.ofm;

import java.io.FileInputStream;

import junit.framework.TestCase;

/**
 * Test for the {@link OfmReader}.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class OfmReaderTest extends TestCase {

    /**
     * The reader.
     */
    private OfmReader reader;

    /**
     * Creates a new object.
     * 
     * @throws Exception if an error occurred.
     */
    public OfmReaderTest() throws Exception {

        if (reader == null) {
            reader =
                    new OfmReader(new FileInputStream(
                        "../ExTeX-Font-tfm/src/font/omlgc.ofm"), "omlgc");
        }
    }

    /**
     * Test, if the font exists.
     * 
     * @throws Exception if an error occurred.
     */
    public void testExist01() throws Exception {

        assertNotNull(reader);
    }

    /**
     * Test, if the font exists.
     * 
     * @throws Exception if an error occurred.
     */
    public void testHeader01() throws Exception {

        assertNotNull(reader);
        assertEquals(0, reader.getOfmLevel());

        OfmHeaderLengths l = reader.getLengths();
        assertNotNull(l);

        assertEquals(0x21, l.getBc());
    }

}
