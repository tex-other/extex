/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.noad;

import java.util.logging.Logger;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.math.MathDelimiter;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This noad represents mathematical material under a radical sign.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class RadicalNoad extends AbstractNucleusNoad {

    /**
     * The field <tt>leftDelimiter</tt> contains the delimiter for the left
     * side. This should normally be the radical sign.
     *
     * @see "TTP [683]"
     */
    private MathDelimiter leftDelimiter;

    /**
     * Creates a new object.
     *
     * @param leftDelimiter the left delimiter; normally something like the
     *  square root symbol
     * @param nucleus the nucleus under the radical
     * @param tc the typesetting context for the color
     */
    public RadicalNoad(final MathDelimiter leftDelimiter, final Noad nucleus,
            final TypesettingContext tc) {

        super(nucleus, tc);
        this.leftDelimiter = leftDelimiter;
    }

    /**
     * @see "TTP [696]"
     * @see de.dante.extex.typesetter.type.noad.AbstractNoad#toStringAdd(
     *      java.lang.StringBuffer,
     *      int)
     */
    protected void toStringAdd(final StringBuffer sb, final int depth) {

        sb.append("radical");
        leftDelimiter.toString(sb);
    }

    /**
     * @see "TTP [737]"
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public int typeset(final NoadList noads, final int index,
            final NodeList list, final MathContext mathContext,
            final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        //TODO gene: typeset() unimplemented
        throw new RuntimeException("unimplemented");
        //return index + 1;
    }

}