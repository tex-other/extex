/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.ttf;

import java.io.IOException;

import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * The 'hhea' table contains information needed to layout fonts
 * whose characters are written horizontally, that is, either
 * left to right or right to left. This table contains information
 * that is general to the font as a whole.
 *
 * <table BORDER="1">
 * <tbody>
 *   <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 * </tbody>
 * <tr><td>Fixed</td><td>Table version number</td><td>
 *          0x00010000 for version 1.0.</td></tr>
 * <tr><td>FWord</td><td>Ascender</td><td>Typographic ascent.</td></tr>
 * <tr><td>FWord</td><td>Descender</td><td>Typographic descent.</td></tr>
 * <tr>FWord</td><td>LineGap</td><td>
 *          Typographic line gap. Negative LineGap values are
 *          treated as zero <BR>in Windows 3.1, System 6, and <BR>System 7.</td></tr>
 * <tr><td>uFWord</td><td>advanceWidthMax</td><td>
 *          Maximum advance width value in &lsquo;hmtx&rsquo; table.</td></tr>
 * <tr><td>FWord</td><td>minLeftSideBearing</td><td>
 *          Minimum left sidebearing value in &lsquo;hmtx&rsquo; table.</td></tr>
 * <tr><td>FWord</td><td>minRightSideBearing</td><td>
 *          Minimum right sidebearing value; calculated as
 *          Min(aw - lsb - (xMax - xMin)).</td></tr>
 * <tr><td>FWord</td><td>xMaxExtent</td><td>Max(lsb + (xMax - xMin)).</td></tr>
 * <tr><td>SHORT</td><<td>caretSlopeRise</td><td>
 *          Used to calculate the slope of the cursor
 *          (rise/run); 1 for vertical.</td></tr>
 * <tr><td>SHORT</td><td>caretSlopeRun</td><td>0 for vertical.</td></tr>
 * <tr><td>SHORT</td><td>(reserved)</td><td>set to 0</td></tr>
 * <tr><td>SHORT</td><td>(reserved)</td><td>set to 0</td></tr>
 * <tr><td>SHORT</td><td>(reserved)</td><td>set to 0</td></tr>
 * <tr><td>SHORT</td><td>(reserved)</td><td>set to 0</td></tr>
 * <tr><td>SHORT</td><td>(reserved)</td><td>set to 0</td></tr>
 * <tr><td>SHORT</td><td>metricDataFormat</td><td>0 for current format.</td></tr>
 * <tr><td>USHORT</td><td>numberOfHMetrics</td><td>
 *          Number of hMetric entries in  &lsquo;hmtx&rsquo;
 *          table; may be smaller than the total number of glyphs in the
 *          font.</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class TTFTableHHEA implements TTFTable, XMLConvertible {

    /**
     * version
     */
    private int version;

    /**
     * ascender
     */
    private short ascender;

    /**
     * descender
     */
    private short descender;

    /**
     * line gap
     */
    private short lineGap;

    /**
     * advance width max
     */
    private short advanceWidthMax;

    /**
     * min left side bearing
     */
    private short minLeftSideBearing;

    /**
     * min right side bearing
     */
    private short minRightSideBearing;

    /**
     * max extent
     */
    private short xMaxExtent;

    /**
     * caret slope rise
     */
    private short caretSlopeRise;

    /**
     * caret slope run
     */
    private short caretSlopeRun;

    /**
     * metrix data format
     */
    private short metricDataFormat;

    /**
     * number of horizontal metrics
     */
    private short numberOfHMetrics;

    /**
     * Create a new object
     *
     * @param de        entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TTFTableHHEA(final TableDirectory.Entry de, final RandomAccessR rar)
            throws IOException {

        rar.seek(de.getOffset());
        version = rar.readInt();
        ascender = rar.readShort();
        descender = rar.readShort();
        lineGap = rar.readShort();
        advanceWidthMax = rar.readShort();
        minLeftSideBearing = rar.readShort();
        minRightSideBearing = rar.readShort();
        xMaxExtent = rar.readShort();
        caretSlopeRise = rar.readShort();
        caretSlopeRun = rar.readShort();
        for (int i = 0; i < 5; i++) {
            rar.readShort();
        }
        metricDataFormat = rar.readShort();
        numberOfHMetrics = rar.readShort();
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.HHEA;
    }

    /**
     * Returns the advanceWidthMax.
     * @return Returns the advanceWidthMax.
     */
    public short getAdvanceWidthMax() {

        return advanceWidthMax;
    }

    /**
     * Returns the ascender.
     * @return Returns the ascender.
     */
    public short getAscender() {

        return ascender;
    }

    /**
     * Returns the caretSlopeRise.
     * @return Returns the caretSlopeRise.
     */
    public short getCaretSlopeRise() {

        return caretSlopeRise;
    }

    /**
     * Returns the caretSlopeRun.
     * @return Returns the caretSlopeRun.
     */
    public short getCaretSlopeRun() {

        return caretSlopeRun;
    }

    /**
     * Returns the descender.
     * @return Returns the descender.
     */
    public short getDescender() {

        return descender;
    }

    /**
     * Returns the lineGap.
     * @return Returns the lineGap.
     */
    public short getLineGap() {

        return lineGap;
    }

    /**
     * Returns the metricDataFormat.
     * @return Returns the metricDataFormat.
     */
    public short getMetricDataFormat() {

        return metricDataFormat;
    }

    /**
     * Returns the minLeftSideBearing.
     * @return Returns the minLeftSideBearing.
     */
    public short getMinLeftSideBearing() {

        return minLeftSideBearing;
    }

    /**
     * Returns the minRightSideBearing.
     * @return Returns the minRightSideBearing.
     */
    public short getMinRightSideBearing() {

        return minRightSideBearing;
    }

    /**
     * Returns the numberOfHMetrics.
     * @return Returns the numberOfHMetrics.
     */
    public short getNumberOfHMetrics() {

        return numberOfHMetrics;
    }

    /**
     * Returns the version.
     * @return Returns the version.
     */
    public int getVersion() {

        return version;
    }

    /**
     * Returns the xMaxExtent.
     * @return Returns the xMaxExtent.
     */
    public short getXMaxExtent() {

        return xMaxExtent;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("table");
        table.setAttribute("name", "hhea");
        table.setAttribute("id", "0x" + Integer.toHexString(getType()));
        table.setAttribute("version", String.valueOf(version));
        table.setAttribute("ascender", String.valueOf(ascender));
        table.setAttribute("descender", String.valueOf(descender));
        table.setAttribute("linegap", String.valueOf(lineGap));
        table.setAttribute("advancewidthmax", String.valueOf(advanceWidthMax));
        table.setAttribute("minleftsidebearing", String
                .valueOf(minLeftSideBearing));
        table.setAttribute("minrightsidebearing", String
                .valueOf(minRightSideBearing));
        table.setAttribute("xmaxextent", String.valueOf(xMaxExtent));
        table.setAttribute("caretsloperise", String.valueOf(caretSlopeRise));
        table.setAttribute("caretsloperun", String.valueOf(caretSlopeRun));
        table
                .setAttribute("metricdataformat", String
                        .valueOf(metricDataFormat));
        table
                .setAttribute("numberofhmetrics", String
                        .valueOf(numberOfHMetrics));
        return table;
    }
}