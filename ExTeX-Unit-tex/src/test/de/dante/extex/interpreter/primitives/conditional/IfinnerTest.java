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

import org.extex.test.ExTeXLauncher;

import de.dante.extex.interpreter.primitives.math.AbstractMathTester;

/**
 * This is a test suite for the primitive <tt>\ifinner</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class IfinnerTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IfinnerTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IfinnerTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\ifinner">
     *  Test case checking that <tt>\ifinner</tt> is false in vertical mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifinner a\\else b\\fi\\end",
                //--- output channel ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\ifinner">
     *  Test case checking that <tt>\ifinner</tt> is false in horizontal mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifinner a\\else b\\fi\\end",
                //--- output channel ---
                "xb" + TERM);
    }

    /**
     * <testcase primitive="\ifinner">
     *  Test case checking that <tt>\ifinner</tt> is false in displaymath mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertSuccess(//--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
                + "$$\\ifinner a\\else b\\fi$$\\end",
                //--- output channel ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\ifinner">
     *  Test case checking that <tt>\ifinner</tt> is true in inner vertical mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES  + "\\vbox{\\ifinner a\\else b\\fi}\\end",
                //--- output channel ---
                "a\n\n" + TERM);
    }

    /**
     * <testcase primitive="\ifinner">
     *  Test case checking that <tt>\ifinner</tt> is true in restricted
     *  horizontal mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test5() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES  + "\\hbox{\\ifinner a\\else b\\fi}\\end",
                //--- output channel ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\ifinner">
     *  Test case checking that <tt>\ifinner</tt> is true in math mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test6() throws Exception {

        assertSuccess(//--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\ifinner a\\else b\\fi$\\end",
                //--- output channel ---
                "a" + TERM);
    }

}
