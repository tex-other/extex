/*
 * Copyright (C) 2008-2009 The ExTeX Group and individual authors listed below
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

package org.extex.maven.latex.builder.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.extex.maven.latex.builder.artifact.Artifact;
import org.extex.maven.latex.builder.exception.MakeActionException;
import org.extex.maven.latex.builder.exception.MakeException;

/**
 * This action runs a command on the artifact.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public abstract class Action {

    /**
     * The field <tt>artifact</tt> contains the artifact to run the command on.
     */
    private final Artifact artifact;

    /**
     * The field <tt>name</tt> contains the name.
     */
    private final String name;

    /**
     * Creates a new object.
     * 
     * @param name the name
     * @param artifact the artifact to run LaTeX on
     */
    public Action(String name, Artifact artifact) {

        this.name = name;
        this.artifact = artifact;
    }

    /**
     * Run or simulate the commands to be executed.
     * 
     * @param target the artifact requesting the build; note: this is in general
     *        <i>not</i> the artifact to be build by this action
     * @param context the environment
     * @param logger the logger
     * @param simulate the indicator whether or not to really execute the
     *        commands
     * 
     * @throws MakeException in case of an error
     */
    public void execute(Artifact target, Map<String, String> context,
            Logger logger, boolean simulate) throws MakeException {

        List<String> commandLine = makeCommandLine(context);
        if (logger != null) {
            logger.log(simulate ? Level.INFO : Level.FINE, //
                "--> " + join(commandLine));
        }
        if (simulate) {
            return;
        }

        ProcessBuilder builder = new ProcessBuilder(commandLine);
        builder.directory(makeWorkingDirectory(context));
        builder.redirectErrorStream(true);
        try {
            Process p = builder.start();
            try {
                p.getOutputStream().close();
                StringBuilder buffer = new StringBuilder();
                InputStream in = p.getInputStream();
                for (int c = in.read(); c >= 0; c = in.read()) {
                    buffer.append((char) c);
                }
                if (p.exitValue() != 0) {
                    logger.severe(buffer.toString());
                }
            } finally {
                p.destroy();
            }
        } catch (IOException e) {
            logger.severe(commandLine.get(0) + " can not be run");
            throw new MakeActionException(commandLine.get(0), e);
        }
    }

    /**
     * Get some value from a context map with a fallback value in case that the
     * value is not found in the context.
     * 
     * @param context the context map
     * @param key the key
     * @param fallback the fallback value
     * 
     * @return the value for the key of the fallback value
     */
    protected String get(Map<String, String> context, String key,
            String fallback) {

        String result = context.get(key);
        return result != null ? result : fallback;
    }

    /**
     * Getter for the artifact.
     * 
     * @return the artifact
     */
    protected Artifact getArtifact() {

        return artifact;
    }

    /**
     * Join the elements of a string list and separate them by a space.
     * 
     * @param commandLine the command line
     * 
     * @return the joined elements of the list
     */
    private String join(List<String> commandLine) {

        StringBuilder buffer = new StringBuilder();
        boolean first = true;
        for (String s : commandLine) {
            if (first) {
                first = false;
            } else {
                buffer.append(' ');
            }
            buffer.append(s);
        }
        return buffer.toString();
    }

    /**
     * Make the command line.
     * 
     * @param context the processor context
     * 
     * @return the command line
     */
    protected abstract List<String> makeCommandLine(Map<String, String> context);

    /**
     * Get the working directory.
     * 
     * @param context the context mapping
     * 
     * @return the working directory
     */
    protected abstract File makeWorkingDirectory(Map<String, String> context);

    /**
     * Print the action to a writer.
     * 
     * @param w the writer
     * @param pre the prefix inserted at the beginning of each line
     */
    public void print(PrintWriter w, String pre) {

        w.print(pre);
        w.println(toString());
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return name + " " + artifact.toString();
    }

}
