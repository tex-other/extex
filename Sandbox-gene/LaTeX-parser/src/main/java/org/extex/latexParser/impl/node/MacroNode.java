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
 * TODO gene: missing JavaDoc.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class MacroNode implements Node {

    /**
     * The field <tt>token</tt> contains the ...
     */
    private Token token;

    private Node opt;

    private Node[] args;

    /**
     * Creates a new object.
     * 
     * @param t the token to add
     * @param opt
     */
    public MacroNode(Token t, Node opt, Node[] args) {

        super();
        token = t;
        this.opt = opt;
        this.args = args;
    }

    /**
     * Getter for token.
     * 
     * @return the token
     */
    public Token getToken() {

        return token;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.latexParser.api.Node#print(java.io.PrintStream)
     */
    public void print(PrintStream stream) {

        stream.print(token.toText());
        if (opt != null) {
            opt.print(stream);
        }
        for (Node n : args) {
            n.print(stream);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(token.toText());
        if (opt != null) {
            sb.append(opt.toString());
        }
        for (Node n : args) {
            sb.append(n.toString());
        }
        return sb.toString();
    }

}