/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package org.extex.font;

import org.extex.font.type.efm.EfmReader;

import junit.framework.TestCase;

/**
 * Test the EfmReader class.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */

public class EfmReaderTest extends TestCase {

    /**
     * 01
     */
    public void test01() {

        assertEquals(reader.getFontname(), "CMR");
        assertEquals(reader.getId(), "CMR");
        assertEquals(reader.getDefaultsize(), 12.0f, 0.0f);
        assertEquals(reader.getType(), "tfm");
        assertEquals(reader.getUnitsperem(), 1000);
    }

    /**
     * the efm reader
     */
    private EfmReader reader;

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {

        reader = new EfmReader("../ExTeX-Font/src/font/efm-cmr12.efm");
    }

    /**
     * main
     * @param args  teh commandline
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(EfmReaderTest.class);
    }

}
