/*
 * Copyright (C) 2006-2007 The ExTeX Group and individual authors listed below
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

package org.extex.unit.omega.math.symbol;

import org.extex.core.exception.helping.HelpingException;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;
import org.extex.typesetter.listMaker.math.NoadConsumer;
import org.extex.typesetter.type.math.MathCode;
import org.extex.unit.omega.math.AbstractOmegaMathCode;
import org.extex.unit.tex.math.util.MathCodeConvertible;

/**
 * This class provides an implementation for the primitive
 * <code>\omathchar</code>.
 * 
 * <doc name="omathchar">
 * <h3>The Math Primitive <tt>\omathchar</tt></h3>
 * <p>
 * The primitive <tt>\omathchar</tt> inserts a mathematical character
 * consisting of a math class and a character code inti the current math list.
 * This is supposed to work in math mode only.
 * </p>
 * <p>
 * TODO missing documentation
 * </p>
 * 
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 * 
 * <pre class="syntax">
 *    &lang;omathchar&rang;
 *       &rarr; <tt>\omathchar ...</tt>  </pre>
 * 
 * <h4>Examples</h4>
 * 
 * <pre class="TeXSample">
 *    \omathchar"041  </pre>
 *  <pre class="TeXSample">
 *    \omathchar{ordinary 0 `A}  </pre>
 * 
 * </doc>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:4431 $
 */
public class Omathchar extends AbstractOmegaMathCode
        implements
            MathCodeConvertible {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for
     * serialization.
     */
    protected static final long serialVersionUID = 2006L;

    /**
     * Creates a new object.
     * 
     * @param name the name for debugging
     */
    public Omathchar(String name) {

        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.unit.tex.math.util.MathCodeConvertible#convertMathCode(
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    public MathCode convertMathCode(Context context, TokenSource source,
            Typesetter typesetter) throws HelpingException, TypesetterException {

        return parseMathCode(context, source, typesetter,
            printableControlSequence(context));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.interpreter.type.AbstractCode#execute(
     *      org.extex.interpreter.Flags, org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    @Override
    public void execute(Flags prefix, Context context, TokenSource source,
            Typesetter typesetter) throws HelpingException, TypesetterException {

        NoadConsumer nc = getListMaker(context, typesetter);
        MathCode mc =
                parseMathCode(context, source, typesetter,
                    printableControlSequence(context));

        nc.add(mc, context.getTypesettingContext());
    }

}
