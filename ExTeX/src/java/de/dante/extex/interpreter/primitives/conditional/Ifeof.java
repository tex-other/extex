/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.primitives.file.AbstractFileCode;
import de.dante.extex.interpreter.type.file.InFile;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\ifeof</code>.
 *
 * <doc name="ifeof">
 * <h3>The Primitive <tt>\ifeof</tt></h3>
 * <p>
 *  This primitive tests for end of file on the given read register. The read
 *  register is specified as a (expanded) number.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;ifeof&rang;
 *      &rarr; <tt>\ifeof</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger()
 *        &lang;number&rang;} &lang;true text&rang; <tt>\fi</tt>
 *      | <tt>\ifeof</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger()
 *        &lang;number&rang;} &lang;true text&rang; <tt>\else</tt> &lang;false text&rang; <tt>\fi</tt> </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \ifeof 3 -E-O-F- \else ready \fi  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Ifeof extends AbstractIf {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Ifeof(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.conditional.AbstractIf#conditional(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    protected boolean conditional(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String key = AbstractFileCode.scanInFileKey(source);
        InFile file = context.getInFile(key);
        return (file == null || file.isEof());
    }
}