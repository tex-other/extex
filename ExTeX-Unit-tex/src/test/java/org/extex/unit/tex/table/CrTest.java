/*
 * Copyright (C) 2004-2007 The ExTeX Group and individual authors listed below
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

package org.extex.unit.tex.table;

import org.extex.test.NoFlagsPrimitiveTester;
import org.junit.Test;

/**
 * This is a test suite for the primitive <tt>\cr</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 4808 $
 */
public class CrTest extends NoFlagsPrimitiveTester {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public CrTest(String arg) {

        super(arg, "cr", "}", DEFINE_HASH + "\\halign{#\\cr");
    }

    /**
     * <testcase primitive="\cr">
     *  Test case checking that <tt>\cr</tt> outside of an alignment context
     *  produces an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    @Test
    public void testLonelyCr() throws Exception {

        assertFailure(//--- input code ---
                "\\cr" + "\\end ",
                //--- log message ---
                "Misplaced \\cr");
    }

}
