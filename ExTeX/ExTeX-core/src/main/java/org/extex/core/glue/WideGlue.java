/*
 * Copyright (C) 2005-2011 The ExTeX Group and individual authors listed below
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

package org.extex.core.glue;

import org.extex.core.dimen.Dimen;
import org.extex.core.dimen.FixedDimen;

/**
 * This class provides an implementation for glue.
 * In contrast to {@link Glue Glue} a full vector of all infinities for the
 * stretchable and shrinkable components are stored. {@link Glue Glue} stores
 * only the highest factor. In case that during computations &ndash; addition or
 * subtraction &ndash; this component reduces to zero the next lower infinity
 * order should determine the value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class WideGlue {

    /**
     * The field <tt>SIZE</tt> contains the size of the arrays.
     */
    private static final int SIZE = 5;

    /**
     * The field <tt>length</tt> contains the natural length.
     */
    private Dimen length = new Dimen();

    /**
     * The field <tt>shrink</tt> contains the shrink components.
     */
    private long[] shrink = new long[SIZE];

    /**
     * The field <tt>stretch</tt> contains the stretch components.
     */
    private long[] stretch = new long[SIZE];

    /**
     * Creates a new object.
     *
     */
    public WideGlue() {

    }

    /**
     * Add some length to the natural length.
     *
     * @param dimen the length to add
     */
    public void add(FixedDimen dimen) {

        length.add(dimen.getValue());
    }

    /**
     * Add some more glue to this one.
     *
     * @param glue the glue to add
     */
    public void add(FixedGlue glue) {

        length.add(glue.getLength());
        FixedGlueComponent s = glue.getStretch();
        stretch[s.getOrder()] += s.getValue();

        s = glue.getShrink();
        shrink[s.getOrder()] += s.getValue();
    }

    /**
     * Add some more glue to this one.
     *
     * @param glue the glue to add
     */
    public void add(WideGlue glue) {

        this.length.add(glue.length);

        for (int i = SIZE - 1; i >= 0; i--) {
            stretch[i] += glue.stretch[i];
            shrink[i] += glue.shrink[i];
        }
    }

    /**
     * Add some length to the stretch.
     *
     * @param s the length to add
     */
    public void addStretch(FixedDimen s) {

        shrink[0] += s.getValue();
    }

    /**
     * Get the highest non-zero glue component.
     *
     * @param a the vector to get the component from
     *
     * @return the highest glue component or ZERO
     */
    private FixedGlueComponent getGC(long[] a) {

        for (byte i = SIZE - 1; i >= 0; i--) {
            if (a[i] != 0) {
                return new GlueComponent(a[i], i);
            }
        }

        return GlueComponent.ZERO;
    }

    /**
     * Getter for the natural length.
     *
     * @return the natural length
     */
    public FixedDimen getLength() {

        return length;
    }

    /**
     * Getter for the shrink.
     *
     * @return the shrink as glue component
     */
    public FixedGlueComponent getShrink() {

        for (byte i = SIZE - 1; i >= 0; i--) {
            if (shrink[i] != 0) {
                return new GlueComponent(shrink[i], i);
            }
        }

        return GlueComponent.ZERO;
    }

    /**
     * Getter for the stretch.
     *
     * @return the stretch component
     */
    public FixedGlueComponent getStretch() {

        for (byte i = SIZE - 1; i >= 0; i--) {
            if (stretch[i] != 0) {
                return new GlueComponent(stretch[i], i);
            }
        }

        return GlueComponent.ZERO;
    }

    /**
     * Setter for the glue value in all three components
     *
     * @param glue the glue to copy
     */
    public void set(FixedGlue glue) {

        this.length.set(glue.getLength());

        for (int i = SIZE - 1; i >= 0; i--) {
            stretch[i] = 0;
            shrink[i] = 0;
        }
        stretch[glue.getStretch().getOrder()] = glue.getStretch().getValue();
        shrink[glue.getShrink().getOrder()] = glue.getShrink().getValue();
    }

    /**
     * Setter for the length.
     * The stretch and shrink components are set to zero.
     *
     * @param len the length
     */
    public void set(FixedDimen len) {

        this.length.set(len);

        for (int i = SIZE - 1; i >= 0; i--) {
            stretch[i] = 0;
            shrink[i] = 0;
        }
    }

    /**
     * Setter for the glue value in all three components
     *
     * @param wg the glue to copy
     */
    public void set(WideGlue wg) {

        this.length.set(wg.length);

        for (int i = SIZE - 1; i >= 0; i--) {
            stretch[i] = wg.stretch[i];
            shrink[i] = wg.shrink[i];
        }
    }

    /**
     * Subtract some more glue to this one.
     *
     * @param glue the glue to add
     */
    public void subtract(FixedGlue glue) {

        length.subtract(glue.getLength());
        FixedGlueComponent s = glue.getStretch();
        stretch[s.getOrder()] -= s.getValue();

        s = glue.getShrink();
        shrink[s.getOrder()] -= s.getValue();
    }

    /**
     * Subtract some length from this one.
     *
     * @param dimen the length to subtract
     */
    public void subtract(FixedDimen dimen) {

        length.subtract(dimen);
    }

    /**
     * Subtract some glue from this one.
     *
     * @param glue the glue to subtract
     */
    public void subtract(WideGlue glue) {

        this.length.subtract(glue.length);

        for (int i = SIZE - 1; i >= 0; i--) {
            stretch[i] -= glue.stretch[i];
            shrink[i] -= glue.shrink[i];
        }
    }

    /**
     * Get the Glue representation for this instance.
     * The stretch and shrink components are reduced to the highest order
     * coefficients.
     *
     * @return the Glue representation of this instance
     */
    public Glue toGlue() {

        return new Glue(length, getGC(stretch), getGC(shrink));
    }

    /**
     * Returns a string representation of the object.
     *
     * @return  a string representation of the object.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return toGlue().toString();
    }

}
