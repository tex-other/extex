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

package org.extex.unit.tex.file.nodes;

import org.extex.core.exception.GeneralException;
import org.extex.scanner.type.file.OutFile;
import org.extex.typesetter.PageContext;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.type.Node;
import org.extex.typesetter.type.NodeVisitor;
import org.extex.typesetter.type.node.WhatsItNode;

/**
 * This WhatsIt node which opens an out file at shipping.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class WhatsItOpenNode extends WhatsItNode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2007L;

    /**
     * The field <tt>file</tt> contains the output file.
     */
    private OutFile file;

    /**
     * The field <tt>key</tt> contains the reference key.
     */
    private String key;

    /**
     * Creates a new object.
     *
     * @param theKey the key of the file to open
     * @param outFile the out file to open
     */
    public WhatsItOpenNode(String theKey, OutFile outFile) {

        super();
        this.key = theKey;
        this.file = outFile;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.extex.typesetter.type.Node#atShipping(
     *      org.extex.interpreter.context.Context,
     *      org.extex.typesetter.Typesetter,
     *      org.extex.typesetter.type.NodeVisitor,
     *      boolean)
     */
    @SuppressWarnings("unchecked")
    public Node atShipping(PageContext context, Typesetter typesetter,
            NodeVisitor visitor, boolean inHMode)
            throws GeneralException {

        file.open();
        context.setOutFile(key, file, true);

        return null;
    }

}
