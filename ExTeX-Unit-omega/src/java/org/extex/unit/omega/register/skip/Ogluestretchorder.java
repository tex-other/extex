/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package org.extex.unit.omega.register.skip;

import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.exception.InterpreterException;
import org.extex.interpreter.type.AbstractCode;
import org.extex.interpreter.type.Theable;
import org.extex.interpreter.type.count.CountConvertible;
import org.extex.interpreter.type.glue.Glue;
import org.extex.interpreter.type.tokens.Tokens;
import org.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\gluestretchorder</code>.
 *
 * <doc name="gluestretchorder">
 * <h3>The Primitive <tt>\gluestretchorder</tt></h3>
 * <p>
 *  The primitive <tt>\gluestretchorder</tt> determines the order of the glue
 *  stretch component of the following glue specification.
 *  A fixed, non-stretchable glue returns the value 0.
 *  Glue with the order fi gives 1, fil gives 2, fill gives 3, and filll gives 4.
 * </p>
 * <p>
 *  Note that the glue specification of 1&nbsp;fi has been introduced by
 *  <logo>Omega</logo>.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;gluestretchorder&rang;
 *      &rarr; <tt>\gluestretchorder</tt> {@linkplain
 *        org.extex.interpreter.type.glue.Glue#parse(TokenSource,Context,Typesetter)
 *        &lang;glue&rang;} </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   \gluestretchorder\skip1  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:4431 $
 */
public class Ogluestretchorder extends AbstractCode
        implements
            CountConvertible,
            Theable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Ogluestretchorder(final String name) {

        super(name);
    }

    /**
     * @see org.extex.interpreter.type.count.CountConvertible#convertCount(
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource,
     *      org.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Glue glue = Glue.parse(source, context, typesetter);
        return glue.getStretch().getOrder();
    }

    /**
     * @see org.extex.interpreter.type.Theable#the(
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource,
     *      org.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Tokens(context, Long.toString(convertCount(context, source,
                typesetter)));
    }

}
