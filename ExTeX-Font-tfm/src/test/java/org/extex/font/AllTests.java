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

package org.extex.font;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test for org.extex.font.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class AllTests {

    public static Test suite() {

        TestSuite suite = new TestSuite("Test for org.extex.font");
        // $JUnit-BEGIN$
        suite.addTestSuite(FontFactoryImplGenCmr10Scaled1440Test.class);
        suite.addTestSuite(FontFactoryImplGenCmr10Test.class);
        suite.addTestSuite(FontFactoryImplGenCmvtti10Test.class);
        suite.addTestSuite(FontFactoryImplGenCmtt10Test.class);
        suite.addTestSuite(FontFactoryImplGenLasy5Test.class);
        suite.addTestSuite(FontFactoryImplCmmanTest.class);
        suite.addTestSuite(FontFactoryImplGenCmr10At50PtTest.class);
        suite.addTestSuite(FontFactoryImplCmr10Test.class);
        suite.addTestSuite(FontFactoryImplGenCmr10At5PtTest.class);
        suite.addTestSuite(FontFactoryImplGenLcirclew10Test.class);
        suite.addTestSuite(FontFactoryImplGenLogosl9Test.class);
        suite.addTestSuite(FontFactoryImplTest.class);
        suite.addTestSuite(FontFactoryImplCmr10At12Test.class);
        suite.addTestSuite(FontFactoryImplCmex10UndefTest.class);
        // $JUnit-END$
        return suite;
    }

}