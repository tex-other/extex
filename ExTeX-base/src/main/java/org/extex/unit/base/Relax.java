/*
 * Copyright (C) 2003-2007 The ExTeX Group and individual authors listed below
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

package org.extex.unit.base;

import org.extex.core.exception.helping.HelpingException;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.type.AbstractCode;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;

/**
 * This class provides an implementation for the primitive <code>\relax</code>.
 * 
 * <doc name="relax">
 * <h3>The Primitive <tt>\relax</tt></h3>
 * <p>
 * This primitive simply does nothing. It acts as a no-op for the <logo>TeX</logo>
 * macro language. <tt>\relax</tt> is not even expandable. in certain
 * circumstances it might be treated as if it where expandable and the expansion
 * is empty.
 * </p>
 * <p>
 * <tt>\relax</tt> sometimes acts as terminating token. For instance when a
 * number is parsed <tt>\relax</tt> terminates the parsing even if the
 * following token is a digit.
 * </p>
 * 
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 * 
 * <pre class="syntax">
 *    &lang;relax&rang;
 *      &rarr; <tt>\relax</tt>  </pre>
 * 
 * <h4>Examples</h4>
 * 
 * <pre class="TeXSample">
 *    \relax  </pre>
 *  <pre class="TeXSample">
 *    \the\count123\relax456  </pre>
 * 
 * </doc>
 * 
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4732 $
 */
public class Relax extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for
     * serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     * 
     * @param name the name for tracing and debugging
     */
    public Relax(String name) {

        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.interpreter.type.Code#execute(
     *      org.extex.interpreter.Flags, org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    @Override
    public void execute(Flags prefix, Context context, TokenSource source,
            Typesetter typesetter) throws HelpingException, TypesetterException {

        // relax
    }

    /**
     * All instances of this class are treated as equal.
     * This is needed for the comparison (\ifx) of tokens defined via \csname.
     * 
     * {@inheritDoc}
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        return obj instanceof Relax;
    }

    /**
     * All instances of this class are treated as equal.
     * Thus all receive the same hash code.
     * 
     * {@inheritDoc}
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        return 4711;
    }

}
