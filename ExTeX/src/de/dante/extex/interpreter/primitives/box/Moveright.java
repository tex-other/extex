/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.box;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\moveright</code>.
 *
 * <doc name="moveright">
 * <h3>The Primitive <tt>\moveright</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;moveright&rang;
 *        := <tt>\moveright</tt> &lang;dimen&rang; {@linkplain
 *    de.dante.extex.interpreter.TokenSource#getBox() &lang;box&rang;}  </pre>
 * </p>
 * <p>
 *  The color from the typographic context is taken as foregroud color for the
 *  rule. The default color is black.
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \moveright 2em \hbox{abc}  </pre>
 *  <pre class="TeXSample">
 *    \moveright -1pt \hbox to 120pt {abc}  </pre>
 *  <pre class="TeXSample">
 *    \moveright 2mm \hbox spread 12pt {abc}  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Moveright extends AbstractCode implements Boxable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Moveright(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Box box = getBox(context, source, typesetter);
        typesetter.add(box.getNodes());
        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.type.box.Boxable#getBox(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        Dimen move = new Dimen(context, source);
        Box box = source.getBox(typesetter);
        move.add(box.getMove());
        box.setMove(move);
        return box;
    }

}