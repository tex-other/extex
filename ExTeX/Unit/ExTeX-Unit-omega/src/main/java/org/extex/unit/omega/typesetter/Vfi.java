/*
 * Copyright (C) 2004-2011 The ExTeX Group and individual authors listed below
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

package org.extex.unit.omega.typesetter;

import org.extex.core.dimen.Dimen;
import org.extex.core.exception.helping.HelpingException;
import org.extex.core.glue.FixedGlue;
import org.extex.core.glue.Glue;
import org.extex.core.glue.GlueComponent;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.scanner.type.token.CodeToken;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;
import org.extex.unit.tex.typesetter.AbstractVerticalCode;
import org.extex.unit.tex.typesetter.spacing.VerticalSkip;

/**
 * This class provides an implementation for the primitive <code>\vfi</code>.
 * 
 * <doc name="vfi"> <h3>The Primitive <tt>\vfi</tt></h3>
 * <p>
 * The primitive <tt>\vfi</tt> inserts vertical glue into the current list. It
 * switches to vertical mode if necessary. The amount of glue inserted has the
 * natural height of 0pt and a stretchability of 1fi.
 * </p>
 * 
 * <h4>Syntax</h4> The formal description of this primitive is the following:
 * 
 * <pre class="syntax">
 *    &lang;vfi&rang;
 *        &rarr; <tt>\vfi</tt>  </pre>
 * 
 * <h4>Examples</h4>
 * 
 * <pre class="TeXSample">
 *    \vfi  </pre>
 * 
 * </doc>
 * 
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:4431 $
 */
public class Vfi extends AbstractVerticalCode implements VerticalSkip {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2007L;

    /**
     * The field <tt>FIL</tt> contains the amount of 1 fil.
     */
    private static final Glue FI = new Glue(Dimen.ZERO, GlueComponent.ONE_FI,
        GlueComponent.ZERO);

    /**
     * Creates a new object.
     * 
     * @param token the initial token for the primitive
     */
    public Vfi(CodeToken token) {

        super(token);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.interpreter.type.AbstractCode#execute(org.extex.interpreter.Flags,
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    @Override
    public void execute(Flags prefix, Context context, TokenSource source,
            Typesetter typesetter) throws HelpingException, TypesetterException {

        ensureVerticalMode(typesetter);
        typesetter.add(FI);
    }

    /**
     * This method acquires a vertical glue.
     * 
     * {@inheritDoc}
     * 
     * @see org.extex.unit.tex.typesetter.spacing.VerticalSkip#getGlue(org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    @Override
    public FixedGlue getGlue(Context context, TokenSource source,
            Typesetter typesetter) throws HelpingException, TypesetterException {

        return FI;
    }

}
