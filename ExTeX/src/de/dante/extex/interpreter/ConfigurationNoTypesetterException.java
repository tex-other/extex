/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.interpreter;

import de.dante.util.configuration.ConfigurationException;
import de.dante.extex.i18n.Messages;

/**
 * This Exception is thrown when a configuration is requested with the path
 * <code>null</code>> or the empty string.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class ConfigurationNoTypesetterException
    extends ConfigurationException {
    /** The message of this exception */
    private String message;

    /**
     * Create a new object.
     *
     * @param messageText the message string
     */
    public ConfigurationNoTypesetterException(final String messageText) {
        super(messageText, (String) null);
        this.message = messageText;
    }

    /**
     * Getter for the text prefix of this ConfigException.
     *
     * @return the text
     */
    public String getMessage() {
        return Messages.format("ConfigurationNoTypesetterException.Text",
                               message);
    }
}
