/*
 * Copyright (C) 2009 The ExTeX Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.extex.maven.latex.builder.artifact.latex.macro;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.extex.maven.latex.builder.DependencyNet;
import org.extex.maven.latex.builder.artifact.Artifact;
import org.extex.maven.latex.builder.artifact.latex.LatexReader;
import org.extex.maven.latex.builder.artifact.latex.MacroWithArgs;

/**
 * This class implements a handler for <code>\documentclass</code>.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 7699 $
 */
public final class DocumentClass extends MacroWithArgs {

    /**
     * The field <tt>CLASSES</tt> contains the ...
     */
    public static final String[] CLASSES =
            new String[]{"book", "report", "article", "letter", "slides",
                    "scrbook", "scrrprt", "scrartcl"};

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.maven.latex.builder.artifact.latex.MacroWithArgs#expand(org.extex.maven.latex.builder.artifact.latex.LatexReader,
     *      org.extex.maven.latex.builder.DependencyNet, java.io.File,
     *      java.lang.String, java.lang.String)
     */
    @Override
    protected void expand(LatexReader reader, DependencyNet net, File base,
            String opt, String arg) throws IOException {

        Logger logger = net.getLogger();
        logger.fine(base.getName() + ": \\documentclass");

        File f = net.searchFile(arg, new String[]{".cls"}, base);
        if (f == null) {
            for (String s : CLASSES) {
                if (arg.equals(s)) {
                    return;
                }
            }
            logger
                .info("documentclass " + arg + " not found; analysis skipped");
            return;
        }
        Artifact artifact = new Artifact(f);
        net.addArtifact(artifact);
        net.getTarget().dependsOn(artifact);

        net.setAtLetter(true);
        net.analyzeLaTeX(artifact);
        net.setAtLetter(false);
    }
}