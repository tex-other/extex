/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\iffontchar</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class IffontcharTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IffontcharTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IffontcharTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\iffontchar">
     *  Test case checking that <tt>\iffontchar</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(//--- input code ---
                "\\iffontchar x true\\else false\\fi \\end",
                //--- output channel ---
                "Missing font identifier");
    }

    /**
     * <testcase primitive="\iffontchar">
     *  Test case checking that <tt>\iffontchar</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr2() throws Exception {

        assertFailure(//--- input code ---
                "\\iffontchar \\nullfont x true\\else false\\fi \\end",
                //--- output channel ---
                "Missing number, treated as zero");
    }

    //TODO implement the primitive specific test cases

}