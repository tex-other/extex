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

package de.dante.extex.interpreter.primitives.hyphen;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\-</code>.
 *
 * <doc name="-">
 * <h3>The Primitive <tt>\-</tt></h3>
 * <p>
 *  The primitive <tt>\-</tt> inserts a soft hyphenation into the current list.
 *  The effect is that the current position is considered as point to insert a
 *  hyphenation mark and break the line here.
 * </p>
 * <p>
 *  <logo>TeX</logo> has another mechanism for describing conditional text
 *  insertions when line breaking appears at a certain place. Those are
 *  associated with the primitive
 *  {@link de.dante.extex.interpreter.primitives.hyphen.Discretionary \discretionary}.
 *  In this context the primitive <tt>\-</tt> is an abbreviation for
 *  <tt>\discretionary{-}{}{}</tt>.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;hyphen&rang;
 *       &rarr; <tt>\-</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    abc\-def  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Hyphen extends AbstractCode {

    /**
     * The field <tt>HYPHEN</tt> contains the Unicode character for the hyphen.
     */
    private static final UnicodeChar HYPHEN = UnicodeChar.get('-');

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Hyphen(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        TypesettingContext tc = context.getTypesettingContext();
        NodeList hyphen = new HorizontalListNode(new CharNode(tc, HYPHEN));

        try {
            typesetter.add(new DiscretionaryNode(hyphen, null, null));
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

}
