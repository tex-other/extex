/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.max;

import java.util.Properties;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\namespace</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class MaxTest extends ExTeXLauncher {

    /**
     * Constructor for TabMarkTest.
     *
     * @param arg the name
     */
    public MaxTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that a sole tab mark leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testTabMark() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.config", "extex");

        runCode(properties,
                "\\catcode`&=4\\relax"
                + "&"
                + "\\end ", "Misplaced alignment tab character &", "\n");
    }

    /**
     * Test case checking that a sole sub mark leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testSupMark() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.config", "extex");

        runCode(properties,
                "\\catcode`^=7\\relax"
                + "^"
                + "\\end ", "Missing $ inserted", "\n");
    }

    /**
     * Test case checking that a sole super mark leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testSubMark() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.config", "extex");

        runCode(properties,
                "\\catcode`_=8\\relax"
                + "_"
                + "\\end ", "Missing $ inserted", "\n");
    }

    /**
     * Test case checking that a sole macro parameter leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testMacroParam() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.config", "extex");

        runCode(properties,
                "\\catcode`#=6\\relax"
                + "#"
                + "\\end ",
                "You can't use `macro parameter character #' in vertical mode",
                "\n");
    }

    /**
     * Test case checking that an undefined active character leads to an
     * error message.
     *
     * @throws Exception in case of an error
     */
    public void testUndefinedAcive() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.config", "extex");

        runCode(properties,
                "\\catcode`~=13\\relax"
                + "~"
                + "\\end ",
                "Undefined control sequence",
                "\n");
    }

    /**
     * Test case checking that an undefined control sequence leads to an
     * error message.
     *
     * @throws Exception in case of an error
     */
    public void testUndefinedCs() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.config", "extex");

        runCode(properties,
                "\\UNDEF"
                + "\\end ",
                "Undefined control sequence",
                "\n");
    }

}