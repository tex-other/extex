/*
 * Copyright (C) 2004 Gerd Neugebauer
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
package de.dante.util.observer;

import de.dante.util.*;
import de.dante.util.GeneralException;


/**
 * This class provides an observer which set a
 * {@link de.dante.util.Switch Switch} when an event is received.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class SwitchObserver implements Observer {

    /**
     * The field <tt>target</tt> contains the target Switch to set upon the 
     * event.
     */
    private Switch target;

    /**
     * The field <tt>value</tt> contains the new value for the switch upon the
     * event.
     */
    private boolean value;

    /**
     * Creates a new object.
     * 
     * @param target the target switch
     * @param value the new value
     */
    public SwitchObserver(final Switch target, final boolean value) {

        super();
        this.target = target;
        this.value = value;
    }

    /**
     * @see de.dante.util.observer.Observer#update(de.dante.util.Observable,
     *      java.lang.Object)
     */
    public void update(final Observable observable, final Object item)
            throws GeneralException {

        target.setOn(value);
    }
}
