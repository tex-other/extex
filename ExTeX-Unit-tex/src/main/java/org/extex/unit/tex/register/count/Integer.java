/*
 * Copyright (C) 2003-2007 The ExTeX Group and individual authors listed below
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

package org.extex.unit.tex.register.count;

import org.extex.core.exception.helping.HelpingException;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.type.AbstractAssignment;
import org.extex.scanner.CountParser;
import org.extex.scanner.type.token.CodeToken;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;
import org.extex.unit.base.register.count.util.IntegerCode;

/**
 * This class provides an implementation for the primitive
 * <code>\integer</code>.
 *
 * <doc name="scaled">
 * <h3>The Primitive <tt>\integer</tt></h3>
 * <p>
 *  The primitive <tt>\integer</tt> can be used to define a control sequence as
 *  alias for an integer register. The control sequence can be used wherever a
 *  count is expected afterwards.
 * </p>
 * <p>
 *  The primitive <tt>\integer</tt>  is an assignment. Thus the settings of
 *  <tt>\afterassignment</tt> and <tt>\globaldefs</tt> are applied.
 * </p>
 * <p>
 *  The prefix <tt>\global</tt> can be used to make the assignment to the new
 *  control sequence global instead of the group-local assignment which is the
 *  default.
 * </p>
 *
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;integer&rang;
 *      &rarr; &lang;optional prefix&rang; <tt>\integer</tt> {@linkplain
 *        org.extex.interpreter.TokenSource#getControlSequence(Context, Typesetter)
 *        &lang;control sequence&rang;} {@linkplain
 *        org.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        org.extex.scanner.CountParser#scanNumber(Context,TokenSource,Typesetter)
 *        &lang;number&rang;}
 *
 *    &lang;optional prefix&rang;
 *      &rarr;
 *       |  <tt>\global</tt> &lang;optional prefix&rang;  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \integer\abc=45  </pre>
 *  <pre class="TeXSample">
 *    \integer\abc 33  </pre>
 *  <pre class="TeXSample">
 *    \integer\abc=-12  </pre>
 *
 * </doc>
 *
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4770 $
 */
public class Integer extends AbstractAssignment {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060607L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Integer(String name) {

        super(name);
    }

    /**
     * The method <tt>assign</tt> is the core of the functionality of
     * {@link #execute(Flags, Context, TokenSource, Typesetter) execute()}.
     * This method is preferable to <tt>execute()</tt> since the
     * <tt>execute()</tt> method provided in this class takes care of
     * <tt>\afterassignment</tt> and <tt>\globaldefs</tt> as well.
     *
     * @param prefix the prefix controlling the execution
     * @param context the interpreter context
     * @param source the token source
     * @param typesetter the typesetter
     * @see org.extex.interpreter.type.AbstractAssignment#assign(
     *      org.extex.interpreter.Flags,
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource,
     *      org.extex.typesetter.Typesetter)
     */
    public void assign(Flags prefix, Context context,
            TokenSource source, Typesetter typesetter)
            throws HelpingException, TypesetterException {

        CodeToken cs = source.getControlSequence(context, typesetter);
        source.getOptionalEquals(context);
        long value = CountParser.scanInteger(context, source, typesetter);
        context.setCode(cs, new IntegerCode(getName(), value), prefix
                .clearGlobal());
    }

}