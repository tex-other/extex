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

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.extex.framework.configuration.Configuration;
import org.extex.framework.configuration.ConfigurationLoader;
import org.junit.Test;

/**
 * TODO gene: missing JavaDoc.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 5432 $
 */
public class ConfigurationInvalidConstructorExceptionTest {

    /**
     * The field <tt>CONF</tt> contains the configuration for testing purposes.
     */
    private static final Configuration CONF = new Configuration() {

        @Override
        public Configuration findConfiguration(String key)
                throws ConfigurationInvalidResourceException,
                    ConfigurationNotFoundException,
                    ConfigurationSyntaxException,
                    ConfigurationIOException {

            return null;
        }

        @Override
        public Configuration findConfiguration(String key, String attribute)
                throws ConfigurationException {

            return null;
        }

        @Override
        public String getAttribute(String name) {

            return null;
        }

        @Override
        public Configuration getConfiguration(String key)
                throws ConfigurationException {

            return null;
        }

        @Override
        public Configuration getConfiguration(String key, String attribute)
                throws ConfigurationException {

            return null;
        }

        @Override
        public String getValue() throws ConfigurationException {

            return null;
        }

        @Override
        public String getValue(String key) throws ConfigurationException {

            return null;
        }

        @Override
        public int getValueAsInteger(String key, int defaultValue)
                throws ConfigurationException {

            return 0;
        }

        @Override
        public void getValues(List<String> list, String key) {

        }

        @Override
        public List<String> getValues(String key) {

            // TODO gene: getValues unimplemented
            return null;
        }

        @Override
        public Iterator<Configuration> iterator() throws ConfigurationException {

            return null;
        }

        @Override
        public Iterator<Configuration> iterator(String key)
                throws ConfigurationException {

            return null;
        }

        @Override
        public void setConfigurationLoader(ConfigurationLoader loader) {

        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {

            return "THE CONFIG";
        }

    };

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationException#getLocalizedMessage()}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void test1() {

        new ConfigurationInvalidConstructorException("abc", null);
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationException#getLocalizedMessage()}
     * .
     */
    @Test
    public void testGetLocalizedMessage1() {

        ConfigurationInvalidConstructorException e =
                new ConfigurationInvalidConstructorException("abc", CONF);
        Locale.setDefault(Locale.ENGLISH);
        assertEquals(
            "The class `abc' does not provide a proper constructor in THE CONFIG",
            e.getLocalizedMessage());
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationException#getLocalizer()}
     * .
     */
    @Test
    public void testGetLocalizer() {

        ConfigurationInvalidConstructorException e =
                new ConfigurationInvalidConstructorException("abc", CONF);
        assertNotNull(e.getLocalizer());
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationException#getSource()}
     * .
     */
    @Test
    public void testGetSource() {

        ConfigurationInvalidConstructorException e =
                new ConfigurationInvalidConstructorException("abc", CONF);
        assertEquals("THE CONFIG", e.getSource());
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationInvalidConstructorException#getText()}
     * .
     */
    @Test
    public void testGetText1() {

        ConfigurationInvalidConstructorException e =
                new ConfigurationInvalidConstructorException("abc", CONF);
        Locale.setDefault(Locale.ENGLISH);
        assertEquals("The class `abc' does not provide a proper constructor",
            e.getText());
    }

    /**
     * Test method for
     * {@link org.extex.framework.configuration.exception.ConfigurationInvalidConstructorException#getText()}
     * .
     */
    @Test
    public void testGetText2() {

        ConfigurationInvalidConstructorException e =
                new ConfigurationInvalidConstructorException(null, CONF);
        Locale.setDefault(Locale.ENGLISH);
        assertEquals("The class `' does not provide a proper constructor",
            e.getText());
    }

}
