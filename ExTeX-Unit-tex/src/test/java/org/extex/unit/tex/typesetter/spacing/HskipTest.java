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

package org.extex.unit.tex.typesetter.spacing;

import org.extex.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\hskip</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4808 $
 */
public class HskipTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(String[] args) {

        junit.textui.TestRunner.run(HskipTest.class);
    }

    /**
     * Constructor for HskipTest.
     *
     * @param arg the name
     */
    public HskipTest(String arg) {

        super(arg, "hskip", "12pt", "A");
    }

    /**
     * <testcase primitive="\hskip">
     *  Test case checking that a lonely <tt>\hskip</tt> is discarded.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\hskip 123pt\\end ",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\hskip">
     *  Test case checking that <tt>\hskip</tt> switches to horizontal mode and
     *  inserts a glue node with the appropriate value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "x\\hskip 123pt x\\end ",
                //--- output channel ---
                "\\vbox(8.0pt+0.0pt)x3000.0pt\n" + //
                ".\\hbox(8.0pt+0.0pt)x3000.0pt\n" + //
                "..x\n" + //
                "..\\glue123.0pt\n" + //
                "..x\n");
    }

    /**
     * <testcase primitive="\hskip">
     *  Test case checking that <tt>\hskip</tt> switches to horizontal mode and
     *  inserts a glue node with the appropriate value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "x\\hskip 123pt plus 1.2fil x\\end ",
                //--- output channel ---
                "\\vbox(8.0pt+0.0pt)x3000.0pt\n" + //
                ".\\hbox(8.0pt+0.0pt)x3000.0pt\n" + //
                "..x\n" + //
                "..\\glue123.0pt plus 1.2fil\n" + //
                "..x\n");
    }

}