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

package de.dante.extex.interpreter.primitives.pdftex;

import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.interpreter.exception.InterpreterException;
import org.extex.util.framework.configuration.exception.ConfigurationException;

import de.dante.extex.backend.documentWriter.PdftexSupport;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.node.pdftex.PdfObject;

/**
 * This class provides an implementation for the primitive <code>\pdfobj</code>.
 *
 * <doc name="pdfobj">
 * <h3>The Primitive <tt>\pdfobj</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;pdfobj&rang;
 *       &rarr; <tt>\pdfobj</tt> &lang;optional attr&rang; &lang;optional stream or file&rang; {@linkplain
 *          org.extex.interpreter.TokenSource#scanTokens(Context, String)
 *          &lang;general text&rang;}
 *
 *    &lang;optional attr&rang;
 *       &rarr; <tt>attr</tt> {@linkplain
 *          org.extex.interpreter.TokenSource#scanTokens(Context, String)
 *          &lang;general text&rang;}
 *       |
 *
 *    &lang;optional stream or file&rang;
 *       &rarr; <tt>file</tt>
 *       |  <tt>stream</tt>
 *       |
 *    </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \pdfobj {abc.png}  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Pdfobj extends AbstractPdftexCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Pdfobj(final String name) {

        super(name);
    }

    /**
     * @see org.extex.interpreter.type.Code#execute(
     *      org.extex.interpreter.Flags,
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource,
     *      org.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        PdftexSupport writer = ensurePdftex(context, typesetter);
        String attr = null;
        boolean isStream = false;

        if (source.getKeyword(context, "attr")) {
            attr = source.scanTokensAsString(context, getName());
        }
        if (source.getKeyword(context, "stream")) {
            isStream = true;
        } else if (source.getKeyword(context, "file")) {
            isStream = false;
        }

        String text = source.scanTokensAsString(context, getName());

        PdfObject a = writer.getObject(attr, isStream, text);

        try {
            typesetter.add(a);
        } catch (TypesetterException e) {
            throw new InterpreterException(e);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }

        prefix.clearImmediate();
    }

}
