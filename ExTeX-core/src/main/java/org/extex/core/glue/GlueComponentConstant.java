/*
 * Copyright (C) 2003-2007 The ExTeX Group and individual authors listed below
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

import java.io.Serializable;

import org.extex.framework.i18n.LocalizerFactory;

/**
 * This class provides a means to store floating numbers with an order.
 * 
 * <p>
 * Examples
 * </p>
 * 
 * <pre>
 * 123 pt
 * -123 pt
 * 123.456 pt
 * 123.pt
 * .465 pt
 * -.456pt
 * +456pt
 * </pre>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision:4399 $
 */
public class GlueComponentConstant implements Serializable, FixedGlueComponent {

    /**
     * The constant <tt>ONE</tt> contains the internal representation for 1pt.
     * 
     * @see "<logo>TeX</logo> &ndash; The Program [101]"
     */
    public static final long ONE = 1 << 16;

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for
     * serialization.
     */
    protected static final long serialVersionUID = 2007L;

    /**
     * The field <tt>order</tt> contains the order of infinity. In case of an
     * order 0 the value holds the absolute value; otherwise value holds the
     * factor of the order.
     */
    protected byte order;

    /**
     * The field <tt>value</tt> contains the integer representation of the
     * dimen register in sp if the order is 0. If the order is not 0 then the
     * value holds the factor to the order in units of 2<sup>16</sup>.
     */
    protected long value;

    /**
     * Creates a new object.
     */
    public GlueComponentConstant() {

        super();
        this.value = 0;
        this.order = 0;
    }

    /**
     * Creates a new object with a fixed width.
     * 
     * @param component the fixed value
     */
    public GlueComponentConstant(FixedGlueComponent component) {

        super();
        this.value = component.getValue();
        this.order = component.getOrder();
    }

    /**
     * Creates a new object with a fixed width.
     * 
     * @param theValue the fixed value
     */
    public GlueComponentConstant(long theValue) {

        super();
        this.value = theValue;
        this.order = 0;
    }

    /**
     * Creates a new object with a width with a possibly higher order.
     * 
     * @param theValue the fixed width or the factor
     * @param theOrder the order
     */
    public GlueComponentConstant(long theValue, byte theOrder) {

        super();
        this.value = theValue;
        this.order = theOrder;
    }

    /**
     * Create a copy of this instance with the same order and value.
     * 
     * @return a new copy of this instance
     */
    public FixedGlueComponent copy() {

        return new GlueComponentConstant(value, order);
    }

    /**
     * Compares the current instance with another GlueComponent for equality.
     * 
     * @param d the other GlueComponent to compare to. If this parameter is
     *        <code>null</code> then the comparison fails.
     * 
     * @return <code>true</code> iff <i>|this| == |d| and ord(this) == ord(d)</i>
     */
    public boolean eq(FixedGlueComponent d) {

        return (d != null && //
                value == d.getValue() && order == d.getOrder());
    }

    /**
     * Compares the current instance with another GlueComponent.
     * 
     * @param d the other GlueComponent to compare to
     * 
     * @return <code>true</code> iff this is greater or equal to d
     */
    public boolean ge(FixedGlueComponent d) {

        return (!lt(d));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.extex.core.glue.FixedGlueComponent#getOrder()
     */
    public byte getOrder() {

        return order;
    }

    /**
     * Getter for the value in scaled points (sp).
     * 
     * @return the value in internal units of scaled points (sp)
     */
    public long getValue() {

        return this.value;
    }

    /**
     * Compares the current instance with another GlueComponent.
     * 
     * @param d the other GlueComponent to compare to
     * 
     * @return <code>true</code> iff <i>ord(this) == ord(d) && |this| &gt; |d|</i>
     *         or <i>ord(this) &gt; ord(d)</i>
     */
    public boolean gt(FixedGlueComponent d) {

        return ((order == d.getOrder() && value > d.getValue()) || //
        order > d.getOrder());
    }

    /**
     * Compares the current instance with another GlueComponent.
     * 
     * @param d the other GlueComponent to compare to
     * 
     * @return <code>true</code> iff this is less or equal to d
     */
    public boolean le(FixedGlueComponent d) {

        return (!gt(d));
    }

    /**
     * Compares the current instance with another GlueComponent.
     * 
     * @param d the other GlueComponent to compare to
     * 
     * @return <code>true</code> iff <i>ord(this) == ord(d) && |this| &lt; |d|</i>
     *         or <i>ord(this) &lt; ord(d)</i>
     */
    public boolean lt(FixedGlueComponent d) {

        return ((order == d.getOrder() && value < d.getValue()) || //
        order < d.getOrder());
    }

    /**
     * Compares the current instance with another GlueComponent for equality.
     * 
     * @param d the other GlueComponent to compare to. If this parameter is
     *        <code>null</code> then the comparison fails.
     * 
     * @return <code>false</code> iff <i>|this| == |d| and ord(this) == ord(d)</i>
     */
    public boolean ne(FixedGlueComponent d) {

        return (d != null && (value != d.getValue() || order != d.getOrder()));
    }

    /**
     * Determine the printable representation of the object.
     * 
     * @return the printable representation
     * 
     * @see #toString(StringBuffer)
     */
    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * Determine the printable representation of the object and append it to the
     * given StringBuffer.
     * 
     * @param sb the output string buffer
     * 
     * @see #toString()
     */
    public void toString(StringBuffer sb) {

        toString(sb, 'p', 't');
    }

    /**
     * Determine the printable representation of the object and append it to the
     * given StringBuffer.
     * 
     * @param sb the output string buffer
     * @param c1 the first character for the length of order 0
     * @param c2 the second character for the length of order 0
     * 
     * @see #toString(StringBuffer)
     */
    public void toString(StringBuffer sb, char c1, char c2) {

        long val = getValue();

        if (val < 0) {
            sb.append('-');
            val = -val;
        }

        long v = val / ONE;
        if (v == 0) {
            sb.append('0');
        } else {
            long m = 1;
            while (m <= v) {
                m *= 10;
            }
            m /= 10;
            while (m > 0) {
                sb.append((char) ('0' + (v / m)));
                v = v % m;
                m /= 10;
            }
        }

        sb.append('.');

        val = 10 * (val % ONE) + 5;
        long delta = 10;
        do {
            if (delta > ONE) {
                val = val + 0100000 - 50000; // round the last digit
            }
            int i = (int) (val / ONE);
            sb.append((char) ('0' + i));
            val = 10 * (val % ONE);
            delta *= 10;
        } while (val > delta);

        if (order == 0) {
            sb.append(c1);
            sb.append(c2);
        } else if (order > 0) {
            sb.append('f');
            sb.append('i');
            for (int i = order; i > 1; i--) {
                sb.append('l');
            }
        } else {
            throw new RuntimeException(LocalizerFactory.getLocalizer(
                GlueComponentConstant.class).format("Illegal.Order",
                Integer.toString(order)));
        }
    }

}