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

package org.extex.unit.tex.typesetter;

import org.extex.core.exception.helping.HelpingException;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.type.AbstractCode;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;
import org.extex.typesetter.type.node.SpecialNode;

/**
 * This class provides an implementation for the primitive <code>\special</code>.
 * 
 * <doc name="special">
 * <h3>The Primitive <tt>\special</tt></h3>
 * <p>
 * This primitive sends a string to the back-end driver. The argument is a
 * balanced block of text which is expanded and translated into a string. The
 * string is given in a
 * {@link org.extex.typesetter.type.node.SpecialNode SpecialNode} to the
 * typesetter for passing it down.
 * </p>
 * 
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 * 
 * <pre class="syntax">
 *    &lang;special&rang;
 *        &rarr; <tt>\special</tt> {@linkplain
 *           org.extex.interpreter.TokenSource#scanTokens(Context,boolean,boolean,String)
 *           &lang;general text&rang;}  </pre>
 * 
 * <h4>Examples</h4>
 * 
 * <pre class="TeXSample">
 *    \special{hello world}  </pre>
 *  <pre class="TeXSample">
 *    \special{ps: \abc}  </pre>
 * 
 * <p>
 * For several back-end drivers for <logo>TeX</logo> a quasi-standard has
 * emerged which uses a prefix ended by a colon to indicate the back-end driver
 * the special is targeted at.
 * </p>
 * </doc>
 * 
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4732 $
 */
public class Special extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for
     * serialization.
     */
    protected static final long serialVersionUID = 2007L;

    /**
     * Creates a new object.
     * 
     * @param name the name for tracing and debugging
     */
    public Special(String name) {

        super(name);
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

        String text =
                source.scanUnprotectedTokens(context, true, false, getName())
                    .toText();
        typesetter.add(new SpecialNode(text));
    }

}
