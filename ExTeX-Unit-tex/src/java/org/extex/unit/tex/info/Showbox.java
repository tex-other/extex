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

package org.extex.unit.tex.info;

import java.util.logging.Logger;

import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.exception.InterpreterException;
import org.extex.interpreter.type.AbstractCode;
import org.extex.interpreter.type.box.Box;
import org.extex.typesetter.Typesetter;
import org.extex.unit.tex.register.box.Setbox;
import org.extex.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\showbox</code>.
 *
 * <doc name="showbox">
 * <h3>The Primitive <tt>\showbox</tt></h3>
 * <p>
 *  The primitive <tt>\showbox</tt> produces a listing of the box register
 *  given as parameter. The listing is restricted in breadth and depth by the
 *  count registers <tt>\showboxbreadth</tt> and <tt>\showboxdepth</tt>
 *  respectively.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;showbox&rang;
 *      &rarr; <tt>\showbox</tt> {@linkplain
 *        org.extex.unit.tex.register.box.Setbox#getKey(Context,TokenSource,Typesetter,String)
 *        &lang;box&nbsp;register&nbsp;name&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \showbox 1  </pre>
 *
 * </doc>
 *
 *
 * <doc name="showboxbreadth" type="register">
 * <h3>The Count Parameter <tt>\showboxbreadth</tt></h3>
 * <p>
 *  The count register <tt>\showboxbreadth</tt> contains the breadth to which
 *  the box produced by <tt>\showbox</tt> should be presented.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;showboxbreadth&rang;
 *      &rarr; <tt>\showboxbreadth</tt> {@linkplain
 *        org.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        org.extex.interpreter.type.count.Count#scanNumber(Context,TokenSource,Typesetter)
 *        &lang;number&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \showboxbreadth=16  </pre>
 *
 * </doc>
 *
 *
 * <doc name="showboxdepth" type="register">
 * <h3>The Count Parameter <tt>\showboxdepth</tt></h3>
 * <p>
 *  The count register <tt>\showboxdepth</tt> contains the depth to which
 *  the box produced by <tt>\showbox</tt> should be presented.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;showboxdepth&rang;
 *      &rarr; <tt>\showboxdepth</tt> {@linkplain
 *        org.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        org.extex.interpreter.type.count.Count#scanNumber(Context,TokenSource,Typesetter)
 *        &lang;number&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \showboxdepth=16  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4732 $
 */
public class Showbox extends AbstractCode implements LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private transient Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Showbox(final String name) {

        super(name);
    }

    /**
     * Setter for the logger.
     *
     * @param log the logger to use
     *
     * @see org.extex.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger log) {

        this.logger = log;
    }

    /**
     * This method takes the first token and executes it. The result is placed
     * on the stack. This operation might have side effects. To execute a token
     * it might be necessary to consume further tokens.
     *
     * @param prefix the prefix controlling the execution
     * @param context the interpreter context
     * @param source the token source
     * @param typesetter the typesetter
     *
     * @throws InterpreterException in case of an error
     *
     * @see org.extex.interpreter.type.Code#execute(
     *      org.extex.interpreter.Flags,
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource,
     *      org.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        String key = Setbox.getKey(context, source, typesetter, getName());
        Box b = context.getBox(key);

        if (b == null) {
            logger.info(getLocalizer().format("TTP.Show.void", //
                context.esc("box" + key)));
        } else {
            long depth = context.getCount("showboxdepth").getValue();
            long width = context.getCount("showboxbreadth").getValue();
            StringBuffer sb = new StringBuffer();
            sb.append(context.esc("box" + key));
            sb.append("=\n");
            b.getNodes().toString(sb, "", (int) depth, (int) width);
            sb.append("\n");
            logger.info(sb.toString());
        }
        logger.info(getLocalizer().format("TTP.Show.OK"));
    }

}
