/*O
 * Copyright (C) 2004 Michael Niedermair
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
package de.dante.extex.typesetter.impl;

import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.GlueComponent;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.typesetter.LineBreaker;
import de.dante.extex.typesetter.NodeList;

/**
 * Implementation for a <code>LineBreaker</code>.
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class LineBreakerXXXImpl implements LineBreaker {

    /**
     * Creates a new Obejct 
     */
    public LineBreakerXXXImpl() {
        super();
    }

    /**
     * @see de.dante.extex.typesetter.LineBreaker#breakLines(de.dante.extex.interpreter.type.node.HorizontalListNode, de.dante.extex.interpreter.context.Context)
     */
    // TODO incomplete (break no line, only for test)
    public NodeList breakLines(HorizontalListNode nodes, Manager manager) {

        VerticalListNode vlnode = new VerticalListNode();

        vlnode.addSkip(new Glue(GlueComponent.ONE * 12));
        //gene: trashed because of merge
        //        nodes.setLineBreak(true);
        vlnode.add(nodes);
        return vlnode;
    }
}
