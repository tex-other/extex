/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.xtf;

import java.io.IOException;

import org.extex.util.XMLWriterConvertible;
import org.extex.util.file.random.RandomAccessR;
import org.extex.util.xml.XMLStreamWriter;


/**
 * The table 'fpgm' (Font Program).
 *
 * <p>
 * This table is similar to the CVT Program, except that it is only run once,
 * when the font is first used. It is used only for FDEFs and IDEFs.
 * Thus the CVT Program need not contain function definitions.
 * However, the CVT Program may redefine existing FDEFs or IDEFs.
 * <p>
 *
 * <table BORDER="1">
 *   <thead>
 *      <tr><td><b>Type</b></td><td><b>Description</b></td></tr>
 *   </thead>
 *   <tr><td>BYTE[ <I>n</I> ]</td><td>Instructions</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class TtfTableFPGM extends AbstractXtfTable
        implements
            XtfTable,
            XMLWriterConvertible {

    /**
     * Create a new object.
     *
     * @param tablemap  the table map
     * @param de        directory entry
     * @param rar       the RandomAccessInput
     * @throws IOException if an error occurred.
     */
    TtfTableFPGM(final XtfTableMap tablemap, final XtfTableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());

        instructions = new byte[de.getLength()];
        for (int i = 0; i < de.getLength(); i++) {
            instructions[i] = (byte) rar.readUnsignedByte();
        }
    }

    /**
     * instructions
     */
    private byte[] instructions;

    /**
     * Returns the instructions
     * @return Returns the instructions
     */
    public byte[] getInstructions() {

        return instructions;
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return XtfReader.FPGM;
    }

    /**
     * @see de.dante.extex.unicodeFont.format.xtf.XtfTable#getShortcur()
     */
    public String getShortcut() {

        return "fpgm";
    }

    /**
     * @see org.extex.util.XMLWriterConvertible#writeXML(
     *      org.extex.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writeStartElement(writer);
        writer.writeByteArray(instructions);
        writer.writeEndElement();
    }
}