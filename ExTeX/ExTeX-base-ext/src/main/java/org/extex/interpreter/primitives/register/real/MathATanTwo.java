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

package org.extex.interpreter.primitives.register.real;

import org.extex.core.exception.helping.HelpingException;
import org.extex.framework.configuration.exception.ConfigurationException;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.parser.CountConvertible;
import org.extex.interpreter.type.Theable;
import org.extex.scanner.type.token.CodeToken;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;

/**
 * Math. Converts rectangular coordinates (x, y) to polar (r, theta). This
 * computes the phase theta by computing an arc tangent of y/x in the range of
 * -pi to pi.
 * 
 * <p>
 * Example
 * </p>
 * 
 * <pre>
 * \the\mathatantwo 0.234 0.34
 * \real7=\mathatantwo 0.56 0.34
 * \real8=\mathatantwo\real7\real8
 * \count99=\mathatantwo 1.34 0.34
 * </pre>
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class MathATanTwo extends AbstractMath
        implements
            Theable,
            RealConvertible,
            CountConvertible {

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    private static final long serialVersionUID = 2007L;

    /**
     * Creates a new object.
     * 
     * @param token the initial token for the primitive
     */
    public MathATanTwo(CodeToken token) {

        super(token);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.interpreter.primitives.register.real.AbstractMath#calculate(
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    @Override
    protected Real calculate(Context context, TokenSource source,
            Typesetter typesetter)
            throws ConfigurationException,
                HelpingException,
                TypesetterException {

        Real real1 = new Real(context, source, typesetter);
        Real real2 = new Real(context, source, typesetter);
        return new Real(Math.atan2(real1.getValue(), real2.getValue()));
    }
}
