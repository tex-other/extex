/*
 * Copyright (C) 2007 The ExTeX Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.extex.exindex.makeindex;

import java.util.ArrayList;
import java.util.List;

import org.extex.exindex.makeindex.pages.PageRange;

/**
 * This class represents an entry in the index.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:7790 $
 */
public class Entry {

    /**
     * The field <tt>HEADING_NUMBER</tt> contains the heading number constant.
     */
    public static final char HEADING_NUMBER = '1';

    /**
     * The field <tt>HEADING_SYMBOL</tt> contains the heading symbol constant.
     */
    public static final char HEADING_SYMBOL = ' ';

    /**
     * The field <tt>value</tt> contains the display value.
     */
    private String display;

    /**
     * The field <tt>key</tt> contains the key.
     */
    private String[] key;

    /**
     * The field <tt>pages</tt> contains the pages contained.
     */
    private List<PageRange> pages = new ArrayList<PageRange>();

    /**
     * The field <tt>heading</tt> contains the heading character.
     */
    private char heading;

    /**
     * Creates a new object.
     * 
     * @param k the key
     * @param display the display string
     * @param page the page specification
     */
    public Entry(String[] k, String display, PageRange page) {

        this.key = k;
        this.display = display;
        this.pages.add(page);

        String s = key[0] == null ? "" : key[0].replaceAll("[{}]", "");
        if (s.equals("")) {
            this.heading = HEADING_SYMBOL;
        } else if (s.matches("[-]?[0-9]+")) {
            this.heading = HEADING_NUMBER;
        } else {
            char c = s.charAt(0);
            this.heading = (Character.isLetter(c)) //
                    ? Character.toLowerCase(c)
                    : HEADING_SYMBOL;
        }
    }

    /**
     * Add the given pages.
     * 
     * @param morePages the pages to add
     */
    public void addPages(List<PageRange> morePages) {

        pages.addAll(morePages);
    }

    /**
     * Add the given pages.
     * 
     * @param morePages the pages to add
     */
    // public void addPages(List<PageReference> morePages) {
    //
    // for (PageReference pr : morePages) {
    // pages.add(new PageRange(pr));
    // }
    // }
    /**
     * Getter for the heading character.
     * 
     * @return the heading character
     */
    public char getHeading() {

        return heading;
    }

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String[] getKey() {

        return key;
    }

    /**
     * Getter for the pages.
     * 
     * @return the pages
     */
    public List<PageRange> getPages() {

        return pages;
    }

    /**
     * Getter for value.
     * 
     * @return the value
     */
    public String getValue() {

        return display;
    }

}
