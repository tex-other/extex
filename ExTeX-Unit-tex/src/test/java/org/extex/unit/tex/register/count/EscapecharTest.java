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

package org.extex.unit.tex.register.count;

import org.extex.test.count.AbstractCountRegisterTester;

/**
 * This is a test suite for the primitive <tt>\escapechar</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4808 $
 */
public class EscapecharTest extends AbstractCountRegisterTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(String[] args) {

        junit.textui.TestRunner.run(EscapecharTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public EscapecharTest(String arg) {

        super(arg, "escapechar", "", "92");
    }

    /**
     * <testcase primitive="\escapechar">
     *  Test case checking that <tt>\escapechar</tt> works with
     *  <tt>\meaning</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
            "\\escapechar=`\\:\\meaning\\relax" + "\\end",
            //--- output channel ---
            ":relax=:relax" + TERM);
    }

    /**
     * <testcase primitive="\escapechar">
     *  Test case checking that <tt>\escapechar</tt> works with
     *  <tt>\string</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
            "\\escapechar=`\\:\\string\\relax" + "\\end",
            //--- output channel ---
            ":relax" + TERM);
    }

    //TODO implement the primitive specific test cases

}
