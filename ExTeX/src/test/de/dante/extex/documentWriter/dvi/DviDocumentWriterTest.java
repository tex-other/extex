/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

// created: 2004-09-31
package de.dante.extex.documentWriter.dvi;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.NoOutputStreamException;
import de.dante.extex.i18n.PanicException;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.node.InsertionNode;
import de.dante.extex.interpreter.type.node.MarkNode;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;

/**
 * JUnit tests for class <code>DviDocumentWriter</code>.
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision$
 */

public class DviDocumentWriterTest extends TestCase {
    private DocumentWriter documentWriter = null;
    private NodeList nodeList = null;
    private DocumentWriterOptions documentWriterOptions = null;
    private Configuration configuration = null;

    /**
     * The if DviDocumentWriter throws the exception, if the node is
     * added to the Nodelist.
     *
     * @param node a <code>Node</code> value
     * @param exception a <code>Class</code> value
     * @exception Exception if an error occurs
     */
    private void checkException(final Node node, final Class exception) throws Exception {
        boolean gotException = false;

        nodeList.add(node);

        try {
            documentWriter.shipout(nodeList);
        } catch (Exception e) {
            if (exception.isInstance(e)) {
                gotException = true;
            } else {
                throw e;
            }
        }
        assertTrue(gotException);
    }


    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(DviDocumentWriterTest.class);
    }

    /**
     * Creates a new <code>DviDocumentWriterTest</code> instance.
     *
     */
    public DviDocumentWriterTest() {
        super();
    }

    /**
     * Setup the fixtures.  All class variables get threr value here.
     * This is not done in the constructor so the variables get new
     * values for each test.
     *
     * @exception Exception if an error occurs
     */
    public void setUp() throws Exception {
        // TODO: do not use null for configuration (TE)

        documentWriterOptions = new MockDocumentWriterOptions();
        documentWriter = new DviDocumentWriter(configuration,
                                               documentWriterOptions);
        nodeList = new VerticalListNode();
        documentWriter.setOutputStream(new ByteArrayOutputStream());
    }


    /**
     * Test if {@link
     *   de.dante.extex.documentWriter.dvi.DviDocumentWriter
     *   DviDocumentWriter }
     * throws a {@link
     *   de.dante.extex.documentWriter.NoOutputStreamException
     * NoOutputStreamException},
     * if there is no OutputStream set before {@link
     *   de.dante.extex.documentWriter.DocumentWriter#shipout(
     *   de.dante.extex.typesetter.NodeList) shipout()}.
     *
     * @exception Exception if an error occurs
     */
    public void testNoOutputStream() throws Exception {
        boolean noOutputStream = false;

        documentWriter = new DviDocumentWriter(configuration,
                                               documentWriterOptions);
        try {
            documentWriter.shipout(nodeList);
        } catch (GeneralException e) {
            if (e instanceof NoOutputStreamException) {
                noOutputStream = true;
            }
        }

        assertTrue(noOutputStream);
    }

    /**
     * Test if the DviDocumentWriter throws new Exception if the
     * nodelist is empty.
     *
     * @exception Exception if an error occurs
     */
    public void testEmptyList() throws Exception {
        documentWriter.shipout(nodeList);
    }


    /**
     * Test if a marknode throws a panic Exception.
     *
     * @exception Exception if an error occurs
     */
    public void testMarkNode() throws Exception {
        checkException(new MarkNode(Tokens.EMPTY), PanicException.class);
    }


    /**
     * Test if a insertionnode throws a panic Exception.
     *
     * @exception Exception if an error occurs
     */
    public void testInsertionNode() throws Exception {
        checkException(new InsertionNode(), PanicException.class);
    }


    private class MockFixedCount implements FixedCount {
        private long value;

        public MockFixedCount(final long theValue) {
            value = theValue;
        }

        public long getValue() {
            return value;
        }

        public void toString(final StringBuffer buffer) {
            buffer.append(toString());
        }

        public Tokens toToks(final Context context) throws GeneralException {
            return new Tokens(context, value);
        }
    }

    private class MockDocumentWriterOptions implements DocumentWriterOptions {
        public MockDocumentWriterOptions() {
        }

        public FixedCount getCountOption(final String count) {
            return new MockFixedCount(0);
        }

        public FixedDimen getDimenOption(final String dimen) {
            return null;
        }

        public long getMagnification() {
            return 1000;
        }
    }
}
