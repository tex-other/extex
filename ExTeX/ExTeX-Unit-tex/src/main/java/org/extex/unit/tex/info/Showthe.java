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

package org.extex.unit.tex.info;

import java.util.logging.Logger;

import org.extex.core.exception.helping.CantUseAfterException;
import org.extex.core.exception.helping.HelpingException;
import org.extex.core.exception.helping.NoHelpException;
import org.extex.framework.logger.LogEnabled;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.type.AbstractCode;
import org.extex.interpreter.type.Code;
import org.extex.interpreter.type.Theable;
import org.extex.scanner.api.exception.CatcodeException;
import org.extex.scanner.type.token.CodeToken;
import org.extex.scanner.type.token.Token;
import org.extex.scanner.type.tokens.Tokens;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;

/**
 * This class provides an implementation for the primitive <code>\showthe</code>.
 * 
 * <doc name="showthe">
 * <h3>The Primitive <tt>\showthe</tt></h3>
 * <p>
 * The primitive <tt>\showthe</tt> produces on the terminal and the error log
 * the exact result as the primitive <tt>\the</tt>. the tokens are preceded
 * by a greater sign (&gt;) and followed by a period (.).
 * </p>
 * <p>
 * If the token following the primitive is not applicable to <tt>\the</tt>
 * then an error s raised.
 * </p>
 * <p>
 * Different entities might have different ideas about what the result of this
 * primitive is. In doubt consult the documentation of the primitive.
 * </p>
 * 
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 * 
 * <pre class="syntax">
 *    &lang;showthe&rang;
 *      &rarr; <tt>\showthe</tt> &lang;internal quantity&rang;  </pre>
 * 
 * <h4>Examples</h4>
 * 
 * <pre class="TeXSample">
 *    \toks2={UTF-8}
 *    \showthe\toks2
 *    &rarr; > UTF-(.  </pre>
 * 
 * </doc>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 4770 $
 */
public class Showthe extends AbstractCode implements LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for
     * serialization.
     */
    protected static final long serialVersionUID = 2007L;

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private transient Logger logger = null;

    /**
     * Creates a new object.
     * 
     * @param token the initial token for the primitive
     */
    public Showthe(CodeToken token) {

        super(token);
    }

    /**
     * Setter for the logger.
     * 
     * @param log the logger to use
     * 
     * @see org.extex.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(Logger log) {

        this.logger = log;
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

        Token cs = source.getToken(context);

        if (cs instanceof CodeToken) {

            Code code = context.getCode((CodeToken) cs);

            if (code != null && code instanceof Theable) {
                Tokens toks;
                try {
                    toks = ((Theable) code).the(context, source, typesetter);
                } catch (CatcodeException e) {
                    throw new NoHelpException(e);
                }
                logger.info(getLocalizer().format("TTP.Format", toks.toText()));
                return;
            }
        }

        throw new CantUseAfterException(cs.toString(),
            toText(context));
    }

}