/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.main.errorHandler.editHandler;

import org.extex.type.Locator;

import de.dante.util.framework.i18n.Localizer;

/**
 * This interface describes the possibility to edit a file at a certain
 * location. It the implementing class is able, it should open an editor and
 * place the cursor on the line indicated.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:4445 $
 */
public interface EditHandler {

    /**
     * Edit a file at a given location.
     *
     * @param localizer the localizer to acquire texts from
     * @param locator the locator for the place to edit
     *
     * @return <code>true</code> iff the job can be continued
     */
    boolean edit(Localizer localizer, Locator locator);

}
