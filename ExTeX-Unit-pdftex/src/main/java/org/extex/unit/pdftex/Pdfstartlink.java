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

import org.extex.core.dimen.Dimen;
import org.extex.core.exception.helping.HelpingException;
import org.extex.interpreter.Flags;
import org.extex.interpreter.TokenSource;
import org.extex.interpreter.context.Context;
import org.extex.scanner.DimenParser;
import org.extex.typesetter.Typesetter;
import org.extex.typesetter.exception.TypesetterException;
import org.extex.typesetter.type.node.RuleNode;
import org.extex.typesetter.type.node.pdftex.PdfStartLink;
import org.extex.unit.pdftex.util.action.ActionSpec;

/**
 * This class provides an implementation for the primitive
 * <code>\pdfstartlink</code>.
 * 
 * <doc name="pdfstartlink">
 * <h3>The Primitive <tt>\pdfstartlink</tt></h3>
 * <p>
 * TODO missing documentation
 * </p>
 * 
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 * 
 * <pre class="syntax">
 *    &lang;pdfstartlink&rang;
 *       &rarr; <tt>\pdfstartlink</tt> ... </pre>
 * 
 * <h4>Examples</h4>
 * 
 * <pre class="TeXSample">
 *    \pdfstartlink {abc.png}  </pre>
 * 
 * </doc>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4791 $
 */
public class Pdfstartlink extends AbstractPdftexCode {

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
    public Pdfstartlink(String name) {

        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.interpreter.type.AbstractCode#execute(org.extex.interpreter.Flags,
     *      org.extex.interpreter.context.Context,
     *      org.extex.interpreter.TokenSource, org.extex.typesetter.Typesetter)
     */
    public void execute(Flags prefix, Context context, TokenSource source,
            Typesetter typesetter)
            throws TypesetterException,
                HelpingException {

        ensurePdftex(context, typesetter);
        Dimen width = Dimen.ONE_PT; // TODO gene:provide correct default
        Dimen height = Dimen.ONE_PT; // TODO gene:provide correct default
        Dimen depth = Dimen.ONE_PT; // TODO gene:provide correct default
        String attr = null;

        for (;;) {
            if (source.getKeyword(context, "width")) {
                width = DimenParser.parse(context, source, typesetter);
            } else if (source.getKeyword(context, "height")) {
                height = DimenParser.parse(context, source, typesetter);
            } else if (source.getKeyword(context, "depth")) {
                depth = DimenParser.parse(context, source, typesetter);
            } else if (source.getKeyword(context, "attr")) {
                attr = source.scanTokensAsString(context, getName());
            } else {
                break;
            }
        }

        ActionSpec action =
                ActionSpec.parseActionSpec(context, source, typesetter,
                    getName());

        typesetter.add(new PdfStartLink(new RuleNode(width, height, depth,
            context.getTypesettingContext(), true), attr, action));
    }

}