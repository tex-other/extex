/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.primitives.file;

import java.io.File;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\openout</code>.
 *
 * <doc name="openout">
 * <h3>The Primitive <tt>\openout</tt></h3>
 * <p>
 *  ...
 * </p>
 * </doc>
 *
 * Example
 * <pre>
 * \openout3= abc.def
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Openout extends AbstractFileCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Openout(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        long no = source.scanInteger();
        String key = Long.toString(no);
        if (no < 0 || no > OutFile.MAX_FILE_NO) {
            throw new GeneralHelpingException("TTP.BadFileNum", key, "0",
                    Integer.toString(OutFile.MAX_FILE_NO));
        }

        source.getOptionalEquals();
        String name = scanFileName(source, context);

        OutFile file = new OutFile(new File(name));

        if (prefix.isImmediate()) {
            file.open();
        }
        context.setOutFile(key, file, prefix.isGlobal());

        return true;
    }

}
