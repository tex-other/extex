/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.hyphen;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive
 * <code>\righthyphenmin</code>.
 *
 * <doc name="righthyphenmin">
 * <h3>The Primitive <tt>\righthyphenmin</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  <pre class="syntax">
 *    &lang;righthyphenmin&rang;
 *     &rarr; <tt>\righthyphenmin</tt> = ... </pre>
 *
 * <h4>Example:</h4>
 *  <pre class="TeXSample">
 *   \righthyphenmin=3 </pre>
 *
 * </doc>
 *
 *
 * The value is stored in the <code>HyphernationTable</code>.
 * Each <code>HyphernationTable</code> is based on <code>\language</code>
 * and has its own <code>\righthyphenmin</code> value (other than the original
 * <logo>TeX</logo>).
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class RightHyphenmin extends AbstractHyphenationCode
        implements
            CountConvertible,
            Theable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public RightHyphenmin(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return getHyphenationTable(context).getRightHyphenmin();
    }

    /**
     * Scan for righthyphenmin value and store it in the
     * <code>HyphernationTable</code> with the language number as index.
     *
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Language table = getHyphenationTable(context);
        source.getOptionalEquals(context);
        long righthyphenmin = source.scanInteger(context, typesetter);

        try {
            table.setRightHyphenmin(righthyphenmin);
        } catch (HyphenationException e) {
            if (e.getCause() instanceof ConfigurationException) {
                throw new InterpreterException(e.getCause());
            }
            throw new InterpreterException(e);
        }
    }

    /**
     * Return the <code>Tokens</code> to show the content with <code>\the</code>.
     *
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Language table = getHyphenationTable(context);
        try {
            return new Tokens(context, String
                    .valueOf(table.getRightHyphenmin()));
        } catch (HyphenationException e) {
            if (e.getCause() instanceof ConfigurationException) {
                throw new InterpreterException(e.getCause());
            }
            throw new InterpreterException(e);
        }
    }
}