/*
 * Copyright (C) 2006-2007 The ExTeX Group and individual authors listed below
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

package org.extex.unit.tex.typesetter.undo;

import org.extex.interpreter.primitives.register.count.AbstractReadonlyCountRegisterTester;

/**
 * This is a test suite for the primitive <tt>\lastpenalty</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4808 $
 */
public class LastpenaltyTest extends AbstractReadonlyCountRegisterTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(String[] args) {

        junit.textui.TestRunner.run(LastpenaltyTest.class);
    }

    /**
     * Constructor for LastboxTest.
     *
     * @param arg the name
     */
    public LastpenaltyTest(String arg) {

        super(arg, "lastpenalty", "", "0");
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertOutput(
        //--- input code ---
                "a\\penalty123 \\showthe\\lastpenalty\\end",
                //--- output channel ---
                "> 123.\n",
                //
                null);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertOutput(
        //--- input code ---
                "a\\penalty123\\relax\\showthe\\lastpenalty\\end",
                //--- output channel ---
                "> 123.\n",
                //
                null);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertOutput(
        //--- input code ---
                "a\\showthe\\lastpenalty\\end",
                //--- output channel ---
                "> 0.\n",
                //
                null);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test10() throws Exception {

        assertOutput(
        //--- input code ---
                "a\\penalty123 \\count0=\\lastpenalty x\\showthe\\count0\\end",
                //--- output channel ---
                "> 123.\n",
                //
                null);
    }

}