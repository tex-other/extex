/*
 * Copyright (C) 2005-2007 The ExTeX Group and individual authors listed below
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

package org.extex.typesetter.type.node.pdftex;

import org.extex.typesetter.type.node.WhatsItNode;
import org.extex.unit.pdftex.util.destination.DestType;
import org.extex.unit.pdftex.util.id.IdSpec;

/**
 * This node marks the target for a link.
 * This node type represents the extension node from the perspective of
 * <logo>TeX</logo>.
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4791 $
 */
public class PdfDest extends WhatsItNode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2007L;

    /**
     * The field <tt>id</tt> contains the id.
     */
    private IdSpec id;

    /**
     * The field <tt>type</tt> contains the destination type.
     */
    private DestType type;

    /**
     * Creates a new object.
     *
     * @param id the id
     * @param type the destination
     */
    public PdfDest(IdSpec id, DestType type) {

        super();
        this.type = type;
        this.id = id;
    }

    /**
     * Getter for id.
     *
     * @return the id
     */
    public IdSpec getId() {

        return this.id;
    }

    /**
     * Getter for type.
     *
     * @return the type
     */
    public DestType getType() {

        return this.type;
    }

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a exhaustive form as it is used in tracing
     * output to the log file.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     * @param breadth the breadth of the nodes to display
     * @param depth the depth of the nodes to display
     *
     * @see org.extex.typesetter.type.Node#toString(
     *      java.lang.StringBuffer,
     *      java.lang.String,
     *      int,
     *      int)
     */
    @Override
    public void toString(StringBuffer sb, String prefix,
            int breadth, int depth) {

        sb.append("(pdfdest ");
        if (id != null) {
            sb.append(id.toString());
            sb.append(" ");
        }
        sb.append(type.toString());
        sb.append(")");
    }

}
