/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.count;

/**
 * This is a test suite for the primitive <tt>\countdef</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class CountdefTest extends AbstractCountRegisterTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(CountdefTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public CountdefTest(final String arg) {

        super(arg, "x", "", "0", "\\countdef\\x=42 ");
    }

    /**
     * <testcase primitive="\countdef">
     *  Test case checking that <tt>\countdef</tt> creates a count assignable
     *  control sequence which is equivalent to the <tt>\count</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(
                //--- input code ---
                "\\countdef\\x=42 " + "\\count42=123 " + "\\the\\count42 \\end",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase primitive="\countdef">
     *  Test case checking that <tt>\countdef</tt> respects a group.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal1() throws Exception {

        assertFailure(//--- input code ---
                "\\begingroup\\countdef\\x=42 \\endgroup" + "\\the\\x \\end",
                //--- error channel ---
                "You can't use `the control sequence \\x' after \\the");
    }

    /**
     * <testcase primitive="\countdef">
     *  Test case checking that <tt>\countdef</tt> respects a group.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal2() throws Exception {

        assertSuccess(//--- input code ---
                "\\begingroup\\global\\countdef\\x=42 \\endgroup"
                        + "\\the\\x \\end",
                //--- output channel ---
                "0" + TERM);
    }

}
