/*
 * Copyright (C) 2009 The ExTeX Group and individual authors listed below
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

package org.extex.exindex.makeindex.main;

import java.util.Comparator;
import java.util.regex.Pattern;

import org.extex.exindex.makeindex.Entry;

public class MakeindexComparator implements Comparator<Entry> {

    /**
     * The field <tt>NUMBER_PATTERN</tt> contains the pattern for numbers.
     */
    private static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");

    /**
     * The field <tt>comparisons</tt> contains the number of comparisons
     * performed.
     */
    private long comparisons;

    /**
     * Compare two entries.
     * 
     * @param o1 the first entry
     * @param o2 the second entry
     * 
     * @return the result
     */
    public int compare(Entry o1, Entry o2) {

        comparisons++;
        char c1 = o1.getHeading();
        char c2 = o2.getHeading();
        if (c1 < c2) {
            return -1;
        } else if (c1 > c2) {
            return 1;
        }
        String[] ka1 = o1.getKey();
        String[] ka2 = o2.getKey();
        int len = Math.max(ka1.length, ka2.length);
        String[] va1 = o1.getValue();
        String[] va2 = o2.getValue();

        for (int i = 0; i < len; i++) {
            String v1 = i < ka1.length ? ka1[i] : null;
            String v2 = i < ka2.length ? ka2[i] : null;
            if (v1 == null) {
                if (v2 != null) {
                    return -1;
                }
            } else if (v2 == null) {
                return 1;
            } else {
                int cmp = compare1(v1, v2);
                if (cmp != 0) {
                    return cmp;
                }
            }
            v1 = i < va1.length ? va1[i] : null;
            v2 = i < va2.length ? va2[i] : null;
            if (v1 == null) {
                if (v2 != null) {
                    return -1;
                }
            } else if (v2 == null) {
                return 1;
            } else {
                int cmp = compare1(v1, v2);
                if (cmp != 0) {
                    return cmp;
                }
            }
        }
        if (ka1.length < ka2.length) {
            return -1;
        } else if (ka1.length > ka2.length) {
            return 1;
        }

        return 0;
    }

    /**
     * Compare two strings for ordering.
     * <ul>
     * <li>Empty strings come before anything else.</li>
     * <li>Numbers consisting of digits only come next. They are ordered
     * according to the numerical value.</li>
     * <li>If the first character is a special character then they come after
     * the numbers and before the alphabetic segment.</li>
     * <li>Finally the alphabetic segment is ordered according to the lowercased
     * equivalent. If the numbers are compared equal when ignoring case they are
     * compared case insensitive.</li>
     * </ul>
     * 
     * @param x the first string
     * @param y the second string
     * 
     * @return 0 if they are equal, -1 if the first one is before the second one
     *         and 1 if the second one is before the first one
     */
    private int compare1(String x, String y) {

        if (x.equals("")) {
            return y.equals("") ? 0 : -1;
        } else if (y.equals("")) {
            return 1;
        }

        if (NUMBER_PATTERN.matcher(x).matches()) {
            return NUMBER_PATTERN.matcher(y).matches() //
                    ? Integer.parseInt(x) - Integer.parseInt(y)
                    : -1;
        } else if (NUMBER_PATTERN.matcher(y).matches()) {
            return 1;
        }
        char cx = x.charAt(0);
        char cy = y.charAt(0);
        if (issymbol(cx)) {
            if (issymbol(cy)) {
                if (Character.isDigit(cx)) {
                    if (!Character.isDigit(cy)) {
                        return -1;
                    }
                } else if (Character.isDigit(cy)) {
                    return 1;
                }
                return x.compareTo(y);
            }
            return 1;
        } else if (issymbol(cy)) {
            return -1;
        }
        int cmp = x.compareToIgnoreCase(y);
        if (cmp != 0) {
            return cmp;
        }
        return x.compareTo(y);
    }

    /**
     * Getter for comparisons.
     * 
     * @return the comparisons
     */
    public long getComparisons() {

        return comparisons;
    }

    /**
     * Check whether the parameter is a symbol.
     * 
     * @param c the character
     * 
     * @return <code>true</code> iff the character is a symbol
     */
    private boolean issymbol(char c) {

        return ('!' <= c && c <= '@') || //
                ('[' <= c && c <= '`') || //
                ('{' <= c && c <= '~');
    }

}
