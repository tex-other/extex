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

package org.extex.latexParser.impl.node;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.extex.latexParser.api.Node;
import org.extex.scanner.type.token.OtherToken;

/**
 * TODO gene: missing JavaDoc.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class OptGroupNode implements Node {

    /**
     * The field <tt>list</tt> contains the main nodes contained.
     */
    private List<Node> list = new ArrayList<Node>();

    /**
     * The field <tt>openToken</tt> contains the open token.
     */
    private OtherToken openToken;

    /**
     * The field <tt>closeToken</tt> contains the close token.
     */
    private OtherToken closeToken;

    /**
     * Creates a new object.
     * 
     * @param t the token to add
     */
    public OptGroupNode(OtherToken t) {

        super();
        openToken = t;
    }

    /**
     * Add a token to the list.
     * 
     * @param n the node to add
     * 
     * @return <code>true</code>
     * 
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(Node n) {

        return list.add(n);
    }

    /**
     * TODO gene: missing JavaDoc
     * 
     * @param t
     */
    public void close(OtherToken t) {

        closeToken = t;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.latexParser.api.Node#print(java.io.PrintStream)
     */
    public void print(PrintStream stream) {

        stream.print(openToken.toText());
        for (Node n : list) {
            n.print(stream);
        }
        stream.print(closeToken.toText());
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(openToken.toText());
        for (Node n : list) {
            sb.append(n.toString());
        }
        sb.append(closeToken.toText());
        return super.toString();
    }

}
