/*
 * Copyright (C) 2009 The ExTeX Group and individual authors listed below
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

package org.extex.ant.latex.command;

import java.io.File;

import org.extex.ant.latex.Settings;

public class Makeindex implements Command {

    /**
     * The field <tt>settings</tt> contains the task.
     */
    private Settings settings;

    /**
     * Creates a new object.
     * 
     * @param settings
     */
    public Makeindex(Settings settings) {

        this.settings = settings;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.ant.latex.command.Command#execute(java.io.File)
     */
    public boolean execute(File artifact) {

        settings.log(toString() + " " + artifact.getName() + "\n");

        // TODO gene: execute unimplemented
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.ant.latex.command.Command#simulate(java.io.File)
     */
    public boolean simulate(File artifact) {

        settings.log(toString() + " " + artifact.getName() + "\n");
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "makeindex";
    }

}