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

package org.extex.font.format.vf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import junit.framework.TestCase;

import org.extex.font.format.tfm.TfmReader;
import org.extex.util.xml.XMLStreamWriter;

/**
 * Test for the {@link VfFont}.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class VfFont01Test extends TestCase {

    /**
     * Font exists.
     * 
     * @throws Exception if an error occurred.
     */
    public void testFontExists01() throws Exception {

        File vf = new File("../ExTeX-Font-tfm/src/font/aer12.vf");
        File tfm = new File("../ExTeX-Font-tfm/src/font/aer12.tfm");

        assertTrue(vf.canRead());
        assertTrue(tfm.canRead());
    }

    /**
     * Font can be read.
     * 
     * @throws Exception if an error occurred.
     */
    public void testFontRead01() throws Exception {

        File vf = new File("../ExTeX-Font-tfm/src/font/aer12.vf");
        File tfm = new File("../ExTeX-Font-tfm/src/font/aer12.tfm");

        assertTrue(vf.canRead());
        assertTrue(tfm.canRead());

        TfmReader tfmReader = new TfmReader(new FileInputStream(tfm), "aer12");
        assertNotNull(tfmReader);

        VfFont vfFont = new VfFont("aer12", new FileInputStream(vf), tfmReader);
        assertNotNull(vfFont);

    }

    /**
     * Font parameters.
     * 
     * @throws Exception if an error occurred.
     */
    public void testFontArgs01() throws Exception {

        try {
            new VfFont("aer12", null, null);
            assertTrue(false);

            new VfFont(null, null, null);
            assertTrue(false);

        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Font values.
     * 
     * @throws Exception if an error occurred.
     */
    public void testFontValues01() throws Exception {

        File vf = new File("../ExTeX-Font-tfm/src/font/aer12.vf");
        File tfm = new File("../ExTeX-Font-tfm/src/font/aer12.tfm");

        assertTrue(vf.canRead());
        assertTrue(tfm.canRead());

        TfmReader tfmReader = new TfmReader(new FileInputStream(tfm), "aer12");
        assertNotNull(tfmReader);

        VfFont vfFont = new VfFont("aer12", new FileInputStream(vf), tfmReader);
        assertNotNull(vfFont);

        assertEquals("aer12", vfFont.getFontname());
        assertNotNull(vfFont.getCmds());

    }

    /**
     * Font xml export.
     * 
     * @throws Exception if an error occurred.
     */
    public void testFontXml01() throws Exception {

        File vf = new File("../ExTeX-Font-tfm/src/font/aer12.vf");
        File tfm = new File("../ExTeX-Font-tfm/src/font/aer12.tfm");

        assertTrue(vf.canRead());
        assertTrue(tfm.canRead());

        TfmReader tfmReader = new TfmReader(new FileInputStream(tfm), "aer12");
        assertNotNull(tfmReader);

        VfFont vfFont = new VfFont("aer12", new FileInputStream(vf), tfmReader);
        assertNotNull(vfFont);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO8859-1");
        writer.setBeauty(true);

        vfFont.writeXML(writer);

        writer.close();
        byte[] b = out.toByteArray();
        assertNotNull(b);

        //System.out.println(new String(b, "ISO8859-1"));
    }

}
