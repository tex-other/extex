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

package org.extex.exindex.core.parser.raw;

/**
 * This class is a container for the raw index entries.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision:6617 $
 */
public class RawIndexentry {

    /**
     * The field <tt>mainKey</tt> contains the main key as given by the user.
     */
    private String[] mainKey;

    /**
     * The field <tt>mergeKey</tt> contains the merge key.
     */
    private String[] mergeKey = null;

    /**
     * The field <tt>printKey</tt> contains the print key.
     */
    private String[] printKey;

    /**
     * The field <tt>sortKey</tt> contains the sort key.
     */
    private String[] sortKey = null;

    /**
     * The field <tt>attribute</tt> contains the attribute.
     */
    private String attribute;

    /**
     * The field <tt>ref</tt> contains the page reference.
     */
    private RefSpec ref;

    /**
     * Creates a new object.
     * 
     * @param key the main key; It can not be <code>null</code>
     * @param print the print key; It can not be <code>null</code>
     * @param attribute the attribute or <code>null</code>
     * @param ref the reference
     */
    public RawIndexentry(String[] key, String[] print, String attribute,
            RefSpec ref) {

        super();
        if (key == null || print == null || ref == null) {
            throw new IllegalArgumentException();
        }
        this.mainKey = key;
        this.printKey = print;
        this.attribute = attribute;
        this.ref = ref;
    }

    /**
     * Getter for the attribute.
     * 
     * @return the attribute
     */
    public String getAttr() {

        return attribute;
    }

    /**
     * Getter for main key.
     * 
     * @return the main key
     */
    public String[] getMainKey() {

        return mainKey;
    }

    /**
     * Getter for merge key.
     * 
     * @return the merge key
     */
    public String[] getMergeKey() {

        return mergeKey;
    }

    /**
     * Getter for print key.
     * 
     * @return the print key
     */
    public String[] getPrintKey() {

        return printKey;
    }

    /**
     * Getter for ref.
     * 
     * @return the ref
     */
    public RefSpec getRef() {

        return ref;
    }

    /**
     * Getter for sort key.
     * 
     * @return the sort key
     */
    public String[] getSortKey() {

        return sortKey;
    }

    /**
     * Setter for the merge key.
     * 
     * @param mergeKey the merge key
     */
    public void setMergeKey(String[] mergeKey) {

        this.mergeKey = mergeKey;
    }

    /**
     * Setter for the sort key.
     * 
     * @param sortKey the sort key
     */
    public void setSortKey(String[] sortKey) {

        this.sortKey = sortKey;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("(indexentry");
        if (printKey == null) {
            toStringAppendList(sb, " :key ", mainKey);
        } else if (mainKey == printKey) {
            toStringAppendList(sb, " :key ", mainKey);
        } else {
            toStringAppendList(sb, " :key ", mainKey);
            toStringAppendList(sb, " :print ", printKey);
        }
        if (attribute != null) {
            sb.append(" :attr ");
            StringUtils.putPrintable(sb, attribute);
        }
        if (ref instanceof XRef) {
            sb.append(" :xref ");
            sb.append(ref.toString());
        } else if (ref instanceof OpenLocRef) {
            sb.append(" :locref ");
            sb.append(ref.toString());
            sb.append(" :open-range");
        } else if (ref instanceof CloseLocRef) {
            sb.append(" :locref ");
            sb.append(ref.toString());
            sb.append(" :close-range");
        } else {
            sb.append(" :locref ");
            sb.append(ref.toString());
        }
        sb.append(")\n");
        return sb.toString();
    }

    /**
     * Append a list of strings to a string builder.
     * 
     * @param sb the buffer
     * @param tag the initial tag
     * @param a the array of values
     */
    private void toStringAppendList(StringBuilder sb, String tag, String[] a) {

        if (a == null) {
            return;
        }
        sb.append(tag);
        sb.append("(");
        boolean first = true;
        for (String s : a) {
            if (first) {
                first = false;
            } else {
                sb.append(" ");
            }
            StringUtils.putPrintable(sb, s);
        }
        sb.append(")");
    }
}