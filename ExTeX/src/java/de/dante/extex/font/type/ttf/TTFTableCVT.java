/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.ttf;

import java.io.IOException;

import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * The 'cvt ' table is optional.
 * <p>
 * It can be used by fonts that contain instructions.
 * It contains an array of FWords that can be accessed by instructions.
 * The 'cvt ' is used to tie together certain font features when
 * their values are sufficiently close to the table value.
 * The number of control value table entries can be calculated
 * by dividing the length of the 'cvt ' table,
 * as given in the table directory, by 4.
 * </p>
 *
 * <table BORDER="1">
 *   <tbody>
 *     <tr><td><b>Type</b></td><td><b>Description</b></td><tr>
 *   </tbody>
 *   <tr><td>FWORD[ <I>n</I> ]</td><td>List of <I>n</I> values referenceable by
 *           instructions.</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class TTFTableCVT extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * values
     */
    private short[] values;

    /**
     * Create a new object.
     *
     * @param tablemap  the tablemap
     * @param de    directory entry
     * @param rar   the RandomAccessInput
     * @throws IOException if an error occured
     */
    TTFTableCVT(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());

        int len = de.getLength() / 2;
        values = new short[len];
        for (int i = 0; i < len; i++) {
            values[i] = rar.readShort();
        }
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.CVT;
    }

    /**
     * Returns the values
     * @return Returns the values
     */
    public short[] getValues() {

        return values;
    }

    /**
     * Returns the info for this class
     * @return Returns the info for this class
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("Table CVT\n");
        for (int i = 0; i < values.length; i++) {
            buf.append("   [" + i + "] : " + String.valueOf(values[i]) + '\n');
        }
        return buf.toString();
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("table");
        table.setAttribute("name", "cvt");
        table.setAttribute("id", "0x" + Integer.toHexString(getType()));
        for (int i = 0; i < values.length; i++) {
            Element val = new Element("values");
            val.setAttribute("id", String.valueOf(i));
            val.setAttribute("value", String.valueOf(values[i]));
            table.addContent(val);
        }
        return table;
    }
}