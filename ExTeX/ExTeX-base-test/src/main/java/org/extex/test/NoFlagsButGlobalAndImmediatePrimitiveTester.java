/*
 * Copyright (C) 2005-2011 The ExTeX Group and individual authors listed below
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

package org.extex.test;

import org.junit.Test;

/**
 * This abstract base class for tests contains some tests which check that all
 * flag primitives but <tt>\global</tt> and <tt>\immediate</tt> lead to an
 * error.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public abstract class NoFlagsButGlobalAndImmediatePrimitiveTester
        extends
            ExTeXLauncher {

    /**
     * The field <tt>primitive</tt> contains the name of the primitive.
     */
    private String primitive;

    /**
     * The field <tt>args</tt> contains the additional arguments for the flag
     * test.
     */
    private String args;

    /**
     * Creates a new object.
     * 
     * @param primitive the name of the primitive
     * @param args additional arguments for the flag test
     */
    public NoFlagsButGlobalAndImmediatePrimitiveTester(String primitive,
            String args) {

        this.primitive = primitive;
        this.args = args;
    }

    /**
     * <testcase> Test case checking that the <tt>\long</tt> flag leads to an
     * error. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testNoLongFlag() throws Exception {

        assertFailure(// --- input code ---
            DEFINE_CATCODES + "\\long\\" + primitive + args,
            // --- log message ---
            "You can\'t use the prefix `\\long\' with the control sequence \\"
                    + primitive);
    }

    /**
     * <testcase> Test case checking that the <tt>\outer</tt> flag leads to an
     * error. </testcase>
     * 
     * @throws Exception in case of an error
     */
    @Test
    public void testNoOuterFlag() throws Exception {

        assertFailure(// --- input code ---
            DEFINE_CATCODES + "\\outer\\" + primitive + args,
            // --- log message ---
            "You can\'t use the prefix `\\outer\' with the control sequence \\"
                    + primitive);
    }

    /**
     * <testcase> Test case checking that the <tt>\protected</tt> flag leads
     * to an error. </testcase>
     * 
     * @throws Exception in case of an error
     */
    // @Test
    // public void testNoProtectedFlag() throws Exception {
    //
    // assertFailure(//--- input code ---
    // DEFINE_CATCODES + "\\protected\\" + primitive + args,
    // //--- log message ---
    // "You can\'t use the prefix `\\protected\' with the control sequence \\"
    // + primitive);
    // }
}
