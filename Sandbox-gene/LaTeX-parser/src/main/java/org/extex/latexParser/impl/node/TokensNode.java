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

import org.extex.latexParser.api.Node;
import org.extex.scanner.type.token.Token;

/**
 * This class represents a list of tokens.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class TokensNode extends AbstractNode implements Node {

    /**
     * The field <tt>buffer</tt> contains the buffer for the characters
     * contained.
     */
    private StringBuilder buffer = new StringBuilder();

    /**
     * Creates a new object.
     */
    public TokensNode() {

        super(null, 0);
    }

    /**
     * Creates a new object.
     * 
     * @param t the token to add
     * @param source the source
     * @param lineNumber the line number
     */
    public TokensNode(Token t, String source, int lineNumber) {

        super(source, lineNumber);
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

        buffer.append(t.toText());
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.latexParser.api.Node#print(java.io.PrintStream)
     */
    public void print(PrintStream stream) {

        stream.print(buffer);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return sb.toString();
    }

}
