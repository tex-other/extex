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
 * The 'prep' table stores the instructions that make up the control
 * value program, a set of TrueType instructions that will be executed
 * once when the font is first accessed and again whenever the font,
 * point size or transformation matrix change. It consists of an ordered
 * list of instructions opcodes. Each opcode is a byte.
 * The tag 'prep', referring to the preProgram, is anachronistic
 * but some people still call the control value program the preProgram.
 *
 * <table BORDER="1">
 *   <tbody>
 *     <tr><td><b>Type</b></td><td><b>Description</b></td></tr>
 *   </tbody>
 *   <tr><td>BYTE[ ]</td><td>
 *          Set of instructions executed whenever point size or
 *          font or transformation change</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class TTFTablePREP extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * Create a new object.
     *
     * @param tablemap  the tablemap
     * @param de        directory entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TTFTablePREP(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());
        instructions = new short[de.getLength()];
        for (int i = 0; i < de.getLength(); i++) {
            instructions[i] = (short) rar.readUnsignedByte();
        }
    }

    /**
     * instructions
     */
    private short[] instructions;

    /**
     * Returns the instructions
     * @return Returns the instructions
     */
    public short[] getInstructions() {

        return instructions;
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.PREP;
    }

    /**
     * Returns the info for this class
     * @return Returns the info for this class
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("Table Prep\n");
        for (int i = 0; i < instructions.length; i++) {
            buf.append("   ").append(i).append('\n');
        }
        return buf.toString();
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("table");
        table.setAttribute("name", "PREP");
        table.setAttribute("id", "0x" + Integer.toHexString(getType()));
        for (int i = 0; i < instructions.length; i++) {
            Element ins = new Element("instructions");
            ins.setAttribute("id", String.valueOf(i));
            ins.setAttribute("value", String.valueOf(instructions[i]));
            table.addContent(ins);
        }
        return table;
    }
}