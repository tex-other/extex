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

package org.extex.latexParser.impl.macro.latex;

import java.io.IOException;

import org.extex.latexParser.api.Node;
import org.extex.latexParser.impl.Macro;
import org.extex.latexParser.impl.Parser;
import org.extex.latexParser.impl.exception.SyntaxError;
import org.extex.latexParser.impl.exception.SystemException;
import org.extex.latexParser.impl.node.EnvironmentNode;
import org.extex.latexParser.impl.node.GroupNode;
import org.extex.latexParser.impl.node.MacroNode;
import org.extex.latexParser.impl.node.TokensNode;
import org.extex.scanner.api.exception.ScannerException;
import org.extex.scanner.type.token.Token;

/**
 * This class represents a \begin instruction.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Begin implements Macro {

    /**
     * Creates a new object.
     * 
     * @param s the initial name
     */
    public Begin(String s) {

        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.latexParser.impl.Macro#parse(
     *      org.extex.scanner.type.token.Token,
     *      org.extex.latexParser.impl.Parser)
     */
    public Node parse(Token token, Parser parser)
            throws ScannerException,
                IOException {

        GroupNode group = parser.parseGroup();
        if (group.size() != 1) {
            throw new SyntaxError(parser, "environment expected");
        }
        Node node = group.get(0);
        if (!(node instanceof TokensNode)) {
            throw new SyntaxError(parser, "environment expected");
        }
        String name = node.toString();
        Macro macro = parser.getDefinition("begin{" + name + "}");
        if (macro == null) {
            throw new SyntaxError(parser, "undefined environment {0}", name);
        }

        String source = parser.getSource();
        int lineno = parser.getLineno();

        Node mac = macro.parse(null, parser);
        if (!(mac instanceof MacroNode)) {
            throw new SystemException("type mismatch", null);
        }
        MacroNode m = (MacroNode) mac;

        EnvironmentNode environmentNode =
                new EnvironmentNode(name, m.getOpt(), m.getArgs(), source,
                    lineno);
        parser.push(environmentNode);

        for (;;) {
            Node n = parser.parseNode(null);
            if (n == null) {
                throw new SyntaxError(parser,
                    "unexpected end of file in environment {0}", name);
            } else if (n instanceof End) {
                break;
            }
            environmentNode.add(n);
        }
        return environmentNode;
    }
}
