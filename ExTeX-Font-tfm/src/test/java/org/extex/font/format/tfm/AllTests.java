/*
 * Copyright (C) 2007 The ExTeX Group and individual authors listed below
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

package org.extex.font.format.tfm;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for the fonts.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 5483 $
 */
public class AllTests {

    public static Test suite() {

        TestSuite suite = new TestSuite("Test for org.extex.font.format.tfm");
        // $JUnit-BEGIN$
        suite.addTestSuite(TfmFixWordTest.class);
        suite.addTestSuite(U2tFactoryTest.class);
        suite.addTestSuite(TfmReaderTest.class);
        // $JUnit-END$
        return suite;
    }

}