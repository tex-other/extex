/*
 * Copyright (C) 2004 Gerd Neugebauer
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
import de.dante.extex.interpreter.Boxable;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Box;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\vbox</code>.
 * <p>
 * Examples
 * </p>
 * <pre>
 *  \vbox{abc}
 *  \vbox to 120pt {abc}
 *  \vbox spread 12pt {abc}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Vbox extends AbstractCode implements Boxable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Vbox(final String name) {
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

        Box b = getBox(context, source, typesetter);
        typesetter.add(b.getNodes());

        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.Boxable#getBox(de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        Tokens toks = context.getToks("everyvbox");
        if (toks != null) {
            source.push(toks);
        }

        Box b;
        if (source.scanKeyword("to")) {
            Dimen d = new Dimen(context, source);
            b = new Box(context, source, typesetter, false);
            b.setWidth(d);
        } else if (source.scanKeyword("spread")) {
            Dimen d = new Dimen(context, source);
            b = new Box(context, source, typesetter, false);
            b.getWidth().add(d);
        } else {
            b = new Box(context, source, typesetter, false);
        }
        return b;
    }

}
