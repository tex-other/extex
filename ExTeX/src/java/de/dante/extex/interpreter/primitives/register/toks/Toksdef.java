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

package de.dante.extex.interpreter.primitives.register.toks;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\toksdef</code>.
 *
 * <doc>
 * <h3>The Primitive <tt>\toksdef</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\toksdef</tt> {@linkplain
 *    de.dante.extex.interpreter.TokenSource#getControlSequence()
 *    &lang;control sequence&rang;} {@linkplain
 *    de.dante.extex.interpreter.TokenSource#getOptionalEquals()
 *    &lang;equals&rang;} &lang;8-bit number&rang;</pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \toksdef\abc=45  </pre>
 *  <pre class="TeXSample">
 *    \toksdef\abc 33  </pre>
 * </p>
 * </doc>
 *
 * <p>Example</p>
 * <pre>
 * \toksdef\abc=45
 * \toksdef\abc 54
 * </pre>
 *
 *
 * <h3>Possible Extension</h3>
 * Allow an expandable expression instead of the number to define real named
 * counters.
 *
 * <p>Example</p>
 * <pre>
 * \toksdef\abc={xyz\the\count0}
 * \toksdef\abc {def}
 * </pre>
 * To protect the buildin registers one might consider to use the key
 * "#<i>name</i>" or "toks#<i>name</i>".
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Toksdef extends AbstractToks {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Toksdef(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.AbstractAssignment#assign(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token cs = source.getControlSequence();
        source.getOptionalEquals();
        String key = getKey(source);
        context.setCode(cs, new NamedToks(key), prefix.isGlobal());
    }

}