/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.expression.term;

import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.exception.InterpreterException;
import org.extex.interpreter.type.Code;
import org.extex.typesetter.Typesetter;

import de.dante.extex.interpreter.expression.EType;
import de.dante.extex.interpreter.expression.ETypeParser;
import de.dante.extex.interpreter.expression.Evaluator;

/**
 * This class implements the supporting functions for the date type
 * {@linkplain de.dante.extex.interpreter.expression.term.TGlue TGlue}
 * for the expression evaluator.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public final class TGlueParser implements ETypeParser {

    /**
     * Creates a new object.
     */
    public TGlueParser() {

        super();
    }

    /**
     * @see de.dante.extex.interpreter.expression.ETypeParser#convert(
     *      org.extex.interpreter.type.Code,
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource,
     *      org.extex.typesetter.Typesetter)
     */
    public EType convert(final Code code, final Context context,
            final TokenSource source, final Typesetter typesetter) {

        // TODO gene: convert unimplemented
        return null;
    }

    /**
     * @see org.extex.interpreter.expression.TerminalParser#parse(
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource,
     *      org.extex.typesetter.Typesetter)
     */
    public EType parse(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return null;
    }

    /**
     * @see org.extex.interpreter.expression.TerminalParser#registered(
     *      de.dante.extex.interpreter.expression.Evaluator)
     */
    public void registered(final Evaluator evaluator) {

    }

}
