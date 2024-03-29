/*
 * Copyright (C) 2003-2010 The ExTeX Group and individual authors listed below
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

package org.extex.exbib.core.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.extex.exbib.core.bst.exception.ExBibMissingEntryException;
import org.extex.exbib.core.exceptions.ExBibException;
import org.extex.exbib.core.io.Locator;

/**
 * This is the basic object stored in a <logo>&epsilon;&chi;Bib</logo> database.
 * 
 * <p>
 * Each {@link Entry} has a type and a key. The type classifies the
 * {@link Entry}. The key names it.
 * </p>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class Entry implements Iterable<String> {

    /**
     * The field <tt>locator</tt> contains the locator for error messages.
     */
    private Locator locator;

    /**
     * The field <tt>values</tt> contains the hash of "normal" values.
     */
    private Map<String, Value> values = new HashMap<String, Value>();

    /**
     * The field <tt>local</tt> contains the hash of local values.
     */
    private Map<String, ValueItem> local = new HashMap<String, ValueItem>();

    /**
     * The field <tt>key</tt> contains the "name" of the Entry.
     */
    private String key = null;

    /**
     * The field <tt>type</tt> contains the "class" of the Entry.
     */
    private String type = null;

    /**
     * The field <tt>keys</tt> contains the list of keys is maintained for
     * performance reasons.
     */
    private List<String> keys = new ArrayList<String>();

    /**
     * Creates a new object.
     * 
     * @param locator the locator for the object
     */
    public Entry(Locator locator) {

        this.locator = locator;
    }

    /**
     * Getter for the Value stored under a given key in the Entry.
     * 
     * @param name the key of the value
     * 
     * @return the Value or <code>null</code> if none is found
     */
    public Value get(String name) {

        return values.get(name);
    }

    /**
     * Getter for the Value stored under a given key in the Entry. If the value
     * is not found locally and the field crossref is present then the value is
     * requested from the entry stored under the crossref key in the database.
     * 
     * @param entryKey the key of the value
     * @param db database context
     * 
     * @return the Value or <code>null</code> if none is found
     * 
     * @throws ExBibException in case of an error
     */
    public Value get(String entryKey, DB db) throws ExBibException {

        Value val = values.get(entryKey);

        if (val == null) {
            val = get("crossref");

            if (val != null) {
                String crossKey = val.expand(db);
                Entry entry = db.getEntry(crossKey);

                if (entry == null) {
                    throw new ExBibMissingEntryException(crossKey, getLocator());
                }

                val = entry.get(entryKey);
            }
        }

        return val;
    }

    /**
     * This method searches for a normal value and concatenates all expanded
     * constituents. Macros are looked up in the database given and their values
     * are inserted.
     * 
     * @param name the key of the "normal" value to expand
     * @param db the database context
     * 
     * @return the expanded version of the value or <code>null</code> if it does
     *         not exist
     * 
     * @throws ExBibException in case of an error
     */
    public String getExpanded(String name, DB db) throws ExBibException {

        Value val = get(name, db);
        if (val == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (ValueItem item : val) {
            item.expand(sb, db);
        }

        return sb.toString();
    }

    /**
     * Getter for the reference key for this Entry.
     * 
     * @return the reference key
     */
    public String getKey() {

        return key;
    }

    /**
     * Getter for the list of keys for "normal" values. Note that those keys may
     * lead to null values.
     * 
     * @return the list of keys
     */
    public List<String> getKeys() {

        return keys;
    }

    /**
     * Getter for a local value. The local values are stored independently from
     * the normal values. This means that they have a name-space of their own.
     * 
     * @param name the key of the local value
     * 
     * @return the local value for key or <code>null</code> if it does not
     *         exist.
     */
    public ValueItem getLocal(String name) {

        return local.get(name);
    }

    /**
     * Getter for a local value. The local values are stored independently from
     * the normal values. This means that they have a name-space of their own.
     * 
     * @param name the name of the local value
     * 
     * @return the local value for key or <code>null</code> if it does not
     *         exist.
     * 
     * @throws NullPointerException in case that the local variable isn't
     *         defined
     * @throws ClassCastException in case that the local variable isn't a number
     */
    public int getLocalInt(String name) {

        ValueItem value = local.get(name);
        return ((VNumber) value).getValue();
    }

    /**
     * Getter for a local value. The local values are stored independently from
     * the normal values. This means that they have a name-space of their own.
     * 
     * @param name the name of the local value
     * 
     * @return the local value for key or <code>null</code> if it does not
     *         exist.
     */
    public String getLocalString(String name) {

        return local.get(name).getContent();
    }

    /**
     * Getter for the locator for this Entry. The locator allows you to
     * determine where this Entry is coming from.
     * 
     * @return the locator
     */
    public Locator getLocator() {

        return locator;
    }

    /**
     * Getter for the sort key of this Entry. The sort key is stored as local
     * value under the key <tt>sort.key$</tt>.
     * 
     * @return the sort key or <code>null</code> if none is set
     */
    public String getSortKey() {

        ValueItem val = (local.get("sort.key$"));
        return val == null ? null : val.getContent();
    }

    /**
     * Getter for the type of this Entry.
     * 
     * @return the type
     */
    public String getType() {

        return type;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<String> iterator() {

        return keys.iterator();
    }

    /**
     * Setter for the Value stored under a given key in the Entry. The String
     * valued parameter is wrapped into a {@link Value Value} before it is
     * stored.
     * 
     * @param name the key of the value
     * @param value the new value
     */
    public void set(String name, String value) {

        Value val = new Value();
        val.add(new VString(value));
        values.put(name, val);
    }

    /**
     * Setter for the Value stored under a given key in the Entry. An already
     * existing value is overwritten silently.
     * 
     * @param name the key of the value
     * @param value the value
     */
    public void set(String name, Value value) {

        values.put(name, value);
        keys.add(name);
    }

    /**
     * Setter for the reference key for this Entry.
     * 
     * @param string the new key
     */
    public void setKey(String string) {

        key = string;
    }

    /**
     * Setter for a local value. The local values are stored independently from
     * the normal values. This means that they have a name space of their own.
     * <p>
     * This method wraps its integer argument into a VNumber before it is
     * stored.
     * </p>
     * 
     * @param name the name of the local value
     * @param value the contents to be stored
     */
    public void setLocal(String name, int value) {

        local.put(name, new VNumber(value, Integer.toString(value)));
    }

    /**
     * Setter for a local value. The local values are stored independently from
     * the normal values. This means that they have a name space of their own.
     * <p>
     * This method wraps its String argument into a VString before it is stored.
     * </p>
     * 
     * @param name the name of the local value
     * @param value the contents to be stored
     */
    public void setLocal(String name, String value) {

        local.put(name, new VString(value));
    }

    /**
     * Setter for a local value. The local values are stored independently from
     * the normal values. This means that they have a name-space of their own.
     * 
     * @param name the name of the local value
     * @param value the contents to be stored
     */
    public void setLocal(String name, ValueItem value) {

        local.put(name, value);
    }

    /**
     * Setter for the sort key of this Entry. The sort key is stored as local
     * value under the key <tt>sort.key$</tt>.
     * 
     * @param sortKey the sort key
     */
    public void setSortKey(String sortKey) {

        local.put("sort.key$", new VString(sortKey));
    }

    /**
     * Setter for the type of this Entry.
     * 
     * @param t the type
     */
    public void setType(String t) {

        type = t.toLowerCase(Locale.ENGLISH);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "@" + type + "{" + key + ",...}";
    }

}
