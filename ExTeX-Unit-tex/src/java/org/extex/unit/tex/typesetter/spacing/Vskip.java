/*
 * Copyright (C) 2004-2007 The ExTeX Group and individual authors listed below
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

package org.extex.unit.tex.typesetter.spacing;

import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.exception.InterpreterException;
import org.extex.interpreter.type.glue.FixedGlue;
import org.extex.interpreter.type.glue.Glue;
import org.extex.typesetter.Typesetter;
import org.extex.unit.tex.typesetter.AbstractVerticalCode;

/**
 * This class provides an implementation for the primitive <code>\vskip</code>.
 *
 * <doc name="vskip">
 * <h3>The Primitive <tt>\vskip</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 *
 * <pre class="syntax">
 *     &lang;vskip&rang;
 *         &rarr; <tt>\vskip</tt> &lang;Glue&rang;
 * </pre>
 *
 * <h4>Examples</h4>
 *
 * <pre class="TeXSample">
 *     \vskip 1em plus 1pt minus 1pt
 * </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:4431 $
 */
public class Vskip extends AbstractVerticalCode implements VerticalSkip {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Vskip(final String name) {

        super(name);
    }

    /**
     * @see org.extex.interpreter.type.Code#execute(
     *      org.extex.interpreter.Flags,
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource,
     *      org.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        ensureVerticalMode(typesetter);
        typesetter.add(Glue.parse(source, context, typesetter));
    }

    /**
     * @see org.extex.unit.tex.typesetter.spacing.VerticalSkip#getGlue(
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource,
     *      org.extex.typesetter.Typesetter)
     */
    public FixedGlue getGlue(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return Glue.parse(source, context, typesetter);
    }

}
