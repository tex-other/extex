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

/**
 * TODO gene: missing JavaDoc.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class EnvironmentNode implements Node {

    /**
     * The field <tt>list</tt> contains the ...
     */
    private List<Node> list = new ArrayList<Node>();

    /**
     * The field <tt>args</tt> contains the ...
     */
    private Node args;

    private String name;

    private String source;

    private int line;

    /**
     * Creates a new object.
     * 
     * @param t the token to add
     */
    public EnvironmentNode(Node args, String name, String source, int line) {

        super();
        this.name = name;
        this.source = source;
        this.line = line;
        this.args = args;
    }

    /**
     * Creates a new object.
     */
    public EnvironmentNode(String name) {

        super();
        this.name = name;
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
     * @param index
     * @return
     * @see java.util.List#get(int)
     */
    public Node get(int index) {

        return list.get(index);
    }

    /**
     * Getter for line.
     * 
     * @return the line
     */
    public int getLine() {

        return line;
    }

    /**
     * Getter for list.
     * 
     * @return the list
     */
    public List<Node> getList() {

        return list;
    }

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * Getter for source.
     * 
     * @return the source
     */
    public String getSource() {

        return source;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.latexParser.api.Node#print(java.io.PrintStream)
     */
    public void print(PrintStream stream) {

        stream.print("\\begin{");
        stream.print(name);
        stream.print("}");
        if (args != null) {
            stream.print('[');
            args.print(stream);
            stream.print('}');
        }
        for (Node n : list) {
            n.print(stream);
        }
        stream.print("\\end{");
        stream.print(name);
        stream.print("}");
    }

    /**
     * @return
     * @see java.util.List#size()
     */
    public int size() {

        return list.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("\\begin{");
        sb.append(name);
        sb.append("}");
        if (args != null) {
            sb.append('[');
            sb.append(args.toString());
            sb.append('}');
        }
        for (Node n : list) {
            sb.append(n.toString());
        }
        sb.append("\\end{");
        sb.append(name);
        sb.append("}");
        return sb.toString();
    }

}
