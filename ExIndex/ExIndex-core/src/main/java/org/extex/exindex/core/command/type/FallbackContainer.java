/*
 * Copyright (C) 2008 The ExTeX Group and individual authors listed below
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

package org.extex.exindex.core.command.type;

/**
 * This interface describes the handling of several fallback scenarios.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public interface FallbackContainer {

    /**
     * Apply the fallback merge rules.
     * 
     * @param text the text to transform
     * 
     * @return the transformed text
     */
    String applyMergeRuleFallback(String text);

    /**
     * Apply the fallback sort rules.
     * 
     * @param text the text to transform
     * @param level the current level
     * 
     * @return the transformed text
     */
    String applySortRuleFallback(String text, int level);

    /**
     * Getter for the fallback markup.
     * 
     * @param name the name
     * 
     * @return the markup
     */
    Markup getFallbackMarkup(String name);

}
