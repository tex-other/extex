/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.typesetter.Discartable;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;

/**
 * This node represents a TeX "glue" node.
 * <p>
 * For the document writer it acts like a kern node. The width contains
 * the distance to add.
 * </p>
 * <p>
 * The stretchability is adjusted by the typesetter and the width is adjusted
 * accordingly.
 * </p>
 *
 * @see "TeX -- The Program [149]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class GlueNode extends AbstractNode implements Node, Discartable {

    /**
     * The field <tt>DEVELOP</tt> contains the indicator that the development
     * is not finished.
     */
    private static final boolean DEVELOP = true;

    /**
     * The field <tt>size</tt> contains the glue specification for this node.
     * The natural size of the glue is the initial width of this node.
     */
    private FixedGlue size;

    /**
     * Creates a new object.
     *
     * @param theSize the actual size
     */
    public GlueNode(final FixedGlue theSize) {

        super(theSize.getLength());
        this.size = new Glue(theSize);
    }

    /**
     * @see de.dante.extex.typesetter.Node#addWidthTo(
     *      de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addWidthTo(final Glue glue) {

        glue.add(this.size);
    }

    /**
     * Adjust the width of the flexible node.
     *
     * @param width the desired with
     * @param sum the total sum of the glues
     *
     * @see de.dante.extex.typesetter.Node#spread(FixedDimen, FixedGlueComponent)
     */
    public void spread(final FixedDimen width, final FixedGlueComponent sum) {

        long w = width.getValue();
        FixedGlueComponent s = (w > 0 ? this.size.getStretch() : this.size
                .getShrink());

        int order = s.getOrder();
        long value = sum.getValue();
        if (order < sum.getOrder() || value == 0) {
            return;
        }

        long sValue = s.getValue();
        long adjust = sValue * w / value;
        if (order == 0) {
            if (adjust > sValue) {
                adjust = sValue;
            } else if (adjust < -sValue) {
                adjust = -sValue;
            }
        }
        getWidth().add(adjust);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return " "; //TODO gene: incomplete
    }

    /**
     * @see de.dante.extex.typesetter.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append("\\glue ");
        sb.append(this.size.toString());
        if (DEVELOP && !getWidth().eq(size.getLength())) {
            sb.append(" [");
            sb.append(getWidth().toString());
            sb.append(']');
        }
    }

    /**
     * This method returns the printable representation.
     * This is meant to produce an exhaustive form as it is used in tracing
     * output to the log file.
     *
     * @return the printable representation
     *
     * @see "TeX -- The Program [186]"
     */
    public String toText() {

        return " "; //TODO gene: incomplete
    }

    /**
     * @see de.dante.extex.typesetter.Node#toText(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append(" "); //TODO gene: incomplete
    }

    /**
     * @see de.dante.extex.typesetter.Node#visit(
     *      de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitGlue(this, value);
    }

    /**
     * @see de.dante.extex.typesetter.Node#visit(
     *      de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value,
            final Object value2) throws GeneralException {

        return visitor.visitGlue(value, value2);
    }
}