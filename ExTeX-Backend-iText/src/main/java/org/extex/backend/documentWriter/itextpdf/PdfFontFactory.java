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

package org.extex.backend.documentWriter.itextpdf;

import java.io.IOException;
import java.util.WeakHashMap;
import java.util.logging.Logger;

import org.extex.font.BackendFont;
import org.extex.font.FontKey;
import org.extex.framework.i18n.Localizer;
import org.extex.framework.i18n.LocalizerFactory;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

/**
 * Factory for fonts to use it with iText.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class PdfFontFactory {

    /**
     * The map for the fonts.
     */
    private static WeakHashMap<FontKey, BaseFont> fonts;

    /**
     * The field <tt>localizer</tt> contains the localizer. It is initiated
     * with a localizer for the name of this class.
     */
    private static Localizer localizer =
            LocalizerFactory.getLocalizer(PdfFontFactory.class);

    /**
     * The logger.
     */
    private static Logger logger;

    /**
     * Returns the font for the pdf backend.
     * 
     * @param backendfont The backend font.
     * @return Returns the font for the pdf backend.
     * @throws DocumentException if a document error occurred.
     * @throws IOException if a io-error occurred.
     */
    public static BaseFont getFont(BackendFont backendfont)
            throws DocumentException,
                IOException {

        if (backendfont == null) {
            throw new IllegalArgumentException("backendfont");
        }

        if (fonts == null) {
            fonts = new WeakHashMap<FontKey, BaseFont>();
        }
        FontKey key = backendfont.getActualFontKey();
        BaseFont font = fonts.get(key);

        if (font != null) {
            return font;
        }

        if (backendfont.isXtf()) {
            byte[] data = backendfont.getXtf();
            // TODO mgn: check encoding
            font =
                    BaseFont
                        .createFont(
                            key.getName() + ".ttf" /* name */,
                            BaseFont.IDENTITY_H /* BaseFont.CP1252 *//* encoding */,
                            true /* embedded */, true /* cached */,
                            data /* ttf,afm */, null /* pfb */);
            logInfo(localizer.format("PdfFontFactory.useFont", "XTF", key
                .getName()));
        }
        if (backendfont.isType1()) {
            byte[] afmdata = backendfont.getAfm();
            byte[] pfbdata = backendfont.getPfb();

            if (afmdata != null && pfbdata != null) {
                // TODO mgn: check encoding
                font =
                        BaseFont
                            .createFont(
                                key.getName() + ".afm" /* name */,
                                "" /* BaseFont.CP1252 *//* encoding */,
                                true /* embedded */, true /* cached */,
                                afmdata /* ttf,afm */, pfbdata /* pfb */);
                logInfo(localizer.format("PdfFontFactory.useFont", "Type1", key
                    .getName()));
            } else {
                logSevere(localizer.format("PdfFontFactory.missingAfmPfb", key
                    .getName()));
            }
        }
        if (font == null) {
            font =
                    BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252,
                        BaseFont.EMBEDDED);
            logSevere(localizer.format("PdfFontFactory.defaultFont", key
                .getName()));
        }
        // store it
        fonts.put(key, font);

        return font;
    }

    /**
     * Log a info message.
     * 
     * @param msg The message.
     */
    private static void logInfo(String msg) {

        if (logger != null) {
            logger.info(msg);
        }
    }

    /**
     * Log a severe message.
     * 
     * @param msg The message.
     */
    private static void logSevere(String msg) {

        if (logger != null) {
            logger.severe(msg);
        }
    }

    /**
     * Setter for logger.
     * 
     * @param logger the logger to set
     */
    public static void setLogger(Logger logger) {

        PdfFontFactory.logger = logger;
    }

}
