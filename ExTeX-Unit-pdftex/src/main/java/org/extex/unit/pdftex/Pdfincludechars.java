/*
 * Copyright (C) 2005-2007 The ExTeX Group and individual authors listed below
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

package org.extex.unit.pdftex;

import org.extex.backend.documentWriter.PdftexSupport;
import org.extex.core.exception.helping.HelpingException;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;
import org.extex.typesetter.tc.font.Font;

/**
 * This class provides an implementation for the primitive
 * <code>\pdfincludechars</code>.
 * 
 * <doc name="pdfincludechars">
 * <h3>The PDF Primitive <tt>\pdfincludechars</tt></h3>
 * <p>
 * This primitive tells the PDF back-end to include certain characters from a
 * font into the generated output. This should overwrite any partial font
 * downloading in effect.
 * </p>
 * 
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 * 
 * <pre class="syntax">
 *    &lang;pdfincludechars&rang;
 *       &rarr; <tt>\pdfincludechars</tt> {@linkplain
 *          org.extex.interpreter.TokenSource#getFont(org.extex.interpreter.context.Context,String)
 *          &lang;font&rang;} {@linkplain
 *          org.extex.interpreter.TokenSource#scanTokens(Context,boolean,boolean,String)
 *          &lang;general text&rang;} </pre>
 * 
 * <h4>Examples</h4>
 * 
 * <pre class="TeXSample">
 *    \font\f cmr12
 *    \pdfincludechars \f {abc} </pre>
 * 
 * </doc>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4791 $
 */
public class Pdfincludechars extends AbstractPdftexCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for
     * serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     * 
     * @param name the name for tracing and debugging
     */
    public Pdfincludechars(String name) {

        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.interpreter.type.AbstractCode#execute(
     *      org.extex.interpreter.Flags, org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    public void execute(Flags prefix, Context context, TokenSource source,
            Typesetter typesetter) throws TypesetterException, HelpingException {

        PdftexSupport writer = ensurePdftex(context, typesetter);

        Font font = source.getFont(context, getName());
        String text =
                source.scanTokens(context, false, false, getName()).toText();

        writer.pdfincludechars(font, text);
    }

}