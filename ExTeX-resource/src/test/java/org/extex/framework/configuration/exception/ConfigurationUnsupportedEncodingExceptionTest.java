/*
 * Copyright (C) 2010 The ExTeX Group and individual authors listed below
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

package org.extex.framework.configuration.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.Test;

/**
 * TODO gene: missing JavaDoc.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 5432 $
 */
public class ConfigurationUnsupportedEncodingExceptionTest {

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationException#getLocalizedMessage()}
     * .
     */
    @Test
    public void testGetLocalizedMessage() {

        ConfigurationUnsupportedEncodingException e =
                new ConfigurationUnsupportedEncodingException("abc", "def");
        Locale.setDefault(Locale.ENGLISH);
        assertEquals("Unsupported encoding abc in def", e.getLocalizedMessage());
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationException#getLocalizer()}
     * .
     */
    @Test
    public void testGetLocalizer() {

        ConfigurationUnsupportedEncodingException e =
                new ConfigurationUnsupportedEncodingException(null, "def");
        Locale.setDefault(Locale.ENGLISH);
        assertNotNull(e.getLocalizer());
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationUnsupportedEncodingException#getMesssage()}
     * .
     */
    @Test
    public void testGetMessage1() {

        ConfigurationUnsupportedEncodingException e =
                new ConfigurationUnsupportedEncodingException(null, "def");
        Locale.setDefault(Locale.ENGLISH);
        assertNull(e.getMessage());
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationUnsupportedEncodingException#getMesssage()}
     * .
     */
    @Test
    public void testGetMessage2() {

        ConfigurationUnsupportedEncodingException e =
                new ConfigurationUnsupportedEncodingException(null, "def");
        Locale.setDefault(Locale.ENGLISH);
        assertEquals(null, e.getMessage());
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationException#getSource()}
     * .
     */
    @Test
    public void testGetSource() {

        ConfigurationUnsupportedEncodingException e =
                new ConfigurationUnsupportedEncodingException(null, "def");
        Locale.setDefault(Locale.ENGLISH);
        assertEquals("def", e.getSource());
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationException#getText()}
     * .
     */
    @Test
    public void testGetText0() {

        ConfigurationUnsupportedEncodingException e =
                new ConfigurationUnsupportedEncodingException(null, "def");
        Locale.setDefault(Locale.ENGLISH);
        assertEquals("Unsupported encoding", e.getText());
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationException#getText()}
     * .
     */
    @Test
    public void testGetText1() {

        ConfigurationUnsupportedEncodingException e =
                new ConfigurationUnsupportedEncodingException("abc", "def");
        Locale.setDefault(Locale.ENGLISH);
        assertEquals("Unsupported encoding", e.getText());
    }

}
