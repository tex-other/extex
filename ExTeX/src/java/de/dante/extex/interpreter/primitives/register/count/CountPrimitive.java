/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.arithmetic.Advanceable;
import de.dante.extex.interpreter.type.arithmetic.Divideable;
import de.dante.extex.interpreter.type.arithmetic.Multiplyable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\count</code>.
 * It sets the named count register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <doc name="count">
 * <h3>The Primitive <tt>\count</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\count</tt> {@linkplain
 *      de.dante.extex.interpreter.TokenSource#scanNumber()
 *      &lang;8-bit&nbsp;number&rang;} {@linkplain
 *      de.dante.extex.interpreter.TokenSource#getOptionalEquals()
 *      &lang;equals&rang;} {@linkplain
 *      de.dante.extex.interpreter.TokenSource#scanInteger()
 *      &lang;number&rang;}</pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \count23=-456  </pre>
 * </p>
 * </doc>
 *
 * @see de.dante.extex.interpreter.type.arithmetic.Advanceable
 * @see de.dante.extex.interpreter.type.arithmetic.Divideable
 * @see de.dante.extex.interpreter.type.arithmetic.Multiplyable
 * @see de.dante.extex.interpreter.type.Theable
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class CountPrimitive extends AbstractCount implements ExpandableCode,
        Advanceable, Multiplyable, Divideable, Theable, CountConvertible {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public CountPrimitive(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.arithmetic.Advanceable#advance(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void advance(final Flags prefix, final Context context,
            final TokenSource source) throws GeneralException {

        String key = getKey(source, context.getNamespace());
        source.getKeyword("by");

        long value = Count.scanCount(context, source);
        value += context.getCount(key).getValue();

        context.setCount(key, value, prefix.isGlobal());
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String key = getKey(source, context.getNamespace());
        source.getOptionalEquals();

        long value = Count.scanCount(context, source);
        context.setCount(key, value, prefix.isGlobal());
    }

    /**
     * @see de.dante.extex.interpreter.type.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String key = getKey(source, context.getNamespace());
        source.push(context.getCount(key).toToks(context));
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public long convertCount(final Context context, final TokenSource source)
            throws GeneralException {

        String key = getKey(source, context.getNamespace());
        Count c = context.getCount(key);
        return (c != null ? c.getValue() : 0);
    }

    /**
     * @see de.dante.extex.interpreter.type.arithmetic.Divideable#divide(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void divide(final Flags prefix, final Context context,
            final TokenSource source) throws GeneralException {

        String key = getKey(source, context.getNamespace());
        source.getKeyword("by");

        long value = Count.scanCount(context, source);

        if (value == 0) {
            throw new HelpingException("TTP.ArithOverflow");
        }

        value = context.getCount(key).getValue() / value;
        context.setCount(key, value, prefix.isGlobal());
    }

    /**
     * @see de.dante.extex.interpreter.type.arithmetic.Multiplyable#multiply(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void multiply(final Flags prefix, final Context context,
            final TokenSource source) throws GeneralException {

        String key = getKey(source, context.getNamespace());
        source.getKeyword("by");

        long value = Count.scanCount(context, source);
        value *= context.getCount(key).getValue();
        context.setCount(key, value, prefix.isGlobal());
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Tokens the(final Context context, final TokenSource source)
            throws GeneralException {

        String key = getKey(source, context.getNamespace());
        String s = context.getCount(key).toString();
        Tokens toks = new Tokens(context, s);
        return toks;
    }

}