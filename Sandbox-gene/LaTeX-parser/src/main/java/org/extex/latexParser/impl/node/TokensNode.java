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
import org.extex.scanner.type.token.Token;

/**
 * TODO gene: missing JavaDoc.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class TokensNode implements Node {

    /**
     * The field <tt>list</tt> contains the ...
     */
    private List<Token> list = new ArrayList<Token>();

    /**
     * Creates a new object.
     */
    public TokensNode() {

        super();
    }

    /**
     * Creates a new object.
     * 
     * @param t the token to add
     */
    public TokensNode(Token t) {

        super();
        add(t);
    }

    /**
     * Add a token to the list.
     * 
     * @param t the token to add
     * 
     * @return <code>true</code>
     * 
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(Token t) {

        return list.add(t);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.latexParser.api.Node#print(java.io.PrintStream)
     */
    public void print(PrintStream stream) {

        for (Token t : list) {
            stream.print(t.toText());
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (Token t : list) {
            sb.append(t.toText());
        }
        return sb.toString();
    }

}
