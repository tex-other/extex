/*
 * Copyright (C) 2004-2007 The ExTeX Group and individual authors listed below
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

package org.extex.font.format.xtf.cff;

import java.io.IOException;
import java.util.List;

import org.extex.font.format.xtf.cff.T2TDOCharStrings.CharString;
import org.extex.util.xml.XMLStreamWriter;

/**
 * hmoveto |- dx1 hmoveto (22) |
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class T2HMoveTo extends T2PathConstruction {

    /**
     * dx.
     */
    private T2Number dx;

    /**
     * Create a new object.
     * 
     * @param ch The char string.
     * @param stack The stack.
     */
    public T2HMoveTo(List<T2CharString> stack, CharString ch)
            throws IOException {

        super(stack, new short[]{T2HMOVETO}, ch);

        int n = stack.size();

        if (n > 1) {
            checkWidth(stack, ch);
        }
        n = stack.size();
        if (n != 1) {
            throw new T2MissingNumberException();
        }

        dx = (T2Number) stack.get(0);

    }

    /**
     * Getter for dx.
     * 
     * @return the dx
     */
    public T2Number getDx() {

        return dx;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.font.format.xtf.cff.T2Operator#getID()
     */
    @Override
    public int getID() {

        return TYPE_HMOVETO;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.font.format.xtf.cff.T2Operator#getName()
     */
    @Override
    public String getName() {

        return "hmoveto";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.font.format.xtf.cff.T2Operator#getValue()
     */
    @Override
    public Object getValue() {

        T2Number[] arr = new T2Number[1];
        arr[0] = dx;
        return arr;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.util.xml.XMLWriterConvertible#writeXML(
     *      org.extex.util.xml.XMLStreamWriter)
     */
    public void writeXML(XMLStreamWriter writer) throws IOException {

        writer.writeStartElement(getName());
        writer.writeAttribute("dx", dx);
        writer.writeEndElement();
    }

}