/*
 * Copyright (C) 2004-2007 The ExTeX Group and individual authors listed below
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

package org.extex.font.format.tfm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.extex.font.exception.FontException;
import org.extex.interpreter.type.dimen.Dimen;
import org.extex.interpreter.type.dimen.FixedDimen;
import org.extex.util.EFMWriterConvertible;
import org.extex.util.file.random.RandomAccessInputStream;
import org.extex.util.file.random.RandomAccessR;
import org.extex.util.framework.configuration.exception.ConfigurationException;
import org.extex.util.xml.XMLStreamWriter;

import de.dante.extex.unicodeFont.format.pfb.PfbParser;
import de.dante.extex.unicodeFont.format.pl.PlFormat;
import de.dante.extex.unicodeFont.format.pl.PlWriter;
import de.dante.extex.unicodeFont.format.tex.psfontmap.PsFontEncoding;
import de.dante.extex.unicodeFont.format.tex.psfontmap.PsFontsMapReader;
import de.dante.extex.unicodeFont.format.tex.psfontmap.enc.EncFactory;

/**
 * This class read a TFM-file.
 *
 * @see <a href="package-summary.html#TFMformat">TFM-Format</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class TfmReader implements PlFormat, EFMWriterConvertible, Serializable {

    /**
     * The field <tt>serialVersionUID</tt> ...
     */
    private static final long serialVersionUID = 1L;

    /**
     * The char info.
     */
    private TfmCharInfoArray charinfo;

    /**
     * The depth.
     */
    private TfmDepthArray depth;

    /**
     * Encoderfactory.
     */
    private EncFactory encfactory;

    /**
     * encodingtable.
     */
    private String[] enctable;

    /**
     * the exten
     */
    private TfmExtenArray exten;

    /**
     * The font name.
     */
    private String fontname;

    /**
     * The header.
     */
    private TfmHeaderArray header;

    /**
     * The height.
     */
    private TfmHeightArray height;

    /**
     * The italic.
     */
    private TfmItalicArray italic;

    /**
     * The kern.
     */
    private TfmKernArray kern;

    /**
     * The lengths in the file.
     */
    private TfmHeaderLengths lengths;

    /**
     * The lig/kern array.
     */
    private TfmLigKernArray ligkern;

    /**
     * The param.
     */
    private TfmParamArray param;

    /**
     * pfb filename.
     */
    private String pfbfilename;

    /**
     * the pfb parser.
     */
    private PfbParser pfbparser;

    /**
     * psfontencoding.
     */
    private PsFontEncoding psfenc;

    //    /**
    //     * @see de.dante.extex.font.type.FontMetric#getFontMetric()
    //     */
    //    public Element getFontMetric() {
    //
    //        // create efm-file
    //        Element root = new Element("fontgroup");
    //        root.setAttribute("name", getFontFamily());
    //        root.setAttribute("id", getFontFamily());
    //        root.setAttribute("default-size", String.valueOf(getDesignSize()));
    //        root.setAttribute("empr", "100");
    //        root.setAttribute("type", "tfm");
    //
    //        Element fontdimen = new Element("fontdimen");
    //        root.addContent(fontdimen);
    //
    //        Element font = new Element("font");
    //        root.addContent(font);
    //
    //        font.setAttribute("font-name", getFontFamily());
    //        font.setAttribute("font-family", getFontFamily());
    //        root.setAttribute("units-per-em", "1000");
    //        font.setAttribute("checksum", String.valueOf(getChecksum()));
    //        font.setAttribute("type", getFontType().toTFMString());
    //        param.addParam(fontdimen);
    //
    //        // filename
    //        if (pfbfilename != null) {
    //            font.setAttribute("filename", pfbfilename);
    //        }
    //        // charinfo.addGlyphs(font);
    //
    //        return root;
    //    }
    //
    /**
     * psfontmap.
     */
    private PsFontsMapReader psfontmap;

    /**
     * The width.
     */
    private TfmWidthArray width;

    /**
     * Create e new object.
     *
     * @param in       The input.
     * @param afontname The font name.
     * @throws IOException if a IO-error occurred.
     */
    public TfmReader(final InputStream in, final String afontname)
            throws IOException {

        this(new RandomAccessInputStream(in), afontname);
    }

    /**
     * Create e new object.
     *
     * @param rar       The input.
     * @param afontname The font name.
     * @throws IOException if a IO-error occurred.
     */
    public TfmReader(final RandomAccessR rar, final String afontname)
            throws IOException {

        fontname = afontname;

        // read the input
        lengths = new TfmHeaderLengths(rar);
        header = new TfmHeaderArray(rar, lengths.getLh());
        charinfo = new TfmCharInfoArray(rar, lengths.getCc());
        width = new TfmWidthArray(rar, lengths.getNw());
        height = new TfmHeightArray(rar, lengths.getNh());
        depth = new TfmDepthArray(rar, lengths.getNd());
        italic = new TfmItalicArray(rar, lengths.getNi());
        ligkern = new TfmLigKernArray(rar, lengths.getNl());
        kern = new TfmKernArray(rar, lengths.getNk());
        exten = new TfmExtenArray(rar, lengths.getNe());
        param = new TfmParamArray(rar, lengths.getNp(), header.getFontType());

        // close input
        rar.close();

        // calculate lig/kern
        ligkern.calculate(charinfo, kern, lengths.getBc());

        // create chartable
        charinfo.createCharTable(width, height, depth, italic, exten, lengths
                .getBc(), ligkern.getLigKernTable());

        // create the lig/kern map for each character
        charinfo.createLigKernMap();

    }

    /**
     * remove the path, if exists.
     * @param  file the filename
     * @return  the filename without the path
     */
    private String filenameWithoutPath(final String file) {

        String rt = file;
        int i = rt.lastIndexOf(File.separator);
        if (i > 0) {
            rt = rt.substring(i + 1);
        }
        return rt;
    }

    /**
     * Returns the charinfo.
     * @return Returns the charinfo.
     */
    public TfmCharInfoArray getCharinfo() {

        return charinfo;
    }

    /**
     * Returns the checksum.
     * @return Returns the checksum.
     */
    public int getChecksum() {

        return header.getChecksum();
    }

    /**
     * Returns the coding scheme of the font.
     *
     * @return the coding scheme of the font.
     * @see org.extex.font.format.tfm.TfmHeaderArray#getCodingscheme()
     */
    public String getCodingscheme() {

        return header.getCodingscheme();
    }

    /**
     * Returns the depth.
     * @return Returns the depth.
     */
    public TfmDepthArray getDepth() {

        return depth;
    }

    /**
     * Returns the depth of a char.
     *
     * @param pos   the position
     * @return the depth of a char, or <code>null</code>, if it does not exist.
     */
    public TfmFixWord getDepth(final int pos) {

        TfmCharInfoWord ci = charinfo.getCharInfoWord(pos);
        if (ci != null) {
            return ci.getDepth();
        }
        return null;
    }

    /**
     * Returns the design size.
     * @return Returns the design size.
     */
    public FixedDimen getDesignSize() {

        TfmFixWord ds = header.getDesignsize();

        return new Dimen(ds.getValue() >> 4);
    }

    /**
     * Returns the design size as fix word.
     * @return Returns the design size as fix word.
     */
    public TfmFixWord getDesignSizeAsFixWord() {

        return header.getDesignsize();
    }

    /**
     * Returns the encfactory.
     * @return Returns the encfactory.
     */
    public EncFactory getEncfactory() {

        return encfactory;
    }

    /**
     * Returns the enctable.
     * @return Returns the enctable.
     */
    public String[] getEnctable() {

        return enctable;
    }

    /**
     * Returns the exten.
     * @return Returns the exten.
     */
    public TfmExtenArray getExten() {

        return exten;
    }

    /**
     * Returns the face of the font.
     * @return Returns the face of the font.
     */
    public int getFace() {

        return header.getFace();
    }

    /**
     * Returns the font family.
     * @return Returns the font family.
     */
    public String getFontFamily() {

        return header.getFontfamily();
    }

    /**
     * Returns the fontname.
     * @return Returns the fontname.
     */
    public String getFontname() {

        return fontname;
    }

    /**
     * Returns the font type.
     * @return Returns the font type.
     */
    public TfmFontType getFontType() {

        return header.getFontType();
    }

    /**
     * Returns the header.
     * @return Returns the header.
     */
    public TfmHeaderArray getHeader() {

        return header;
    }

    /**
     * Returns the height.
     * @return Returns the height.
     */
    public TfmHeightArray getHeight() {

        return height;
    }

    /**
     * Returns the height of a char.
     *
     * @param pos   the position
     * @return the height of a char, or <code>null</code>, if it does not exist.
     */
    public TfmFixWord getHeight(final int pos) {

        TfmCharInfoWord ci = charinfo.getCharInfoWord(pos);
        if (ci != null) {
            return ci.getHeight();
        }
        return null;
    }

    /**
     * Returns the italic.
     * @return Returns the italic.
     */
    public TfmItalicArray getItalic() {

        return italic;
    }

    /**
     * Returns the italic correction of a char.
     *
     * @param pos   the position
     * @return the italic correction of a char, or <code>null</code>, if it does not exist.
     */
    public TfmFixWord getItalicCorrection(final int pos) {

        TfmCharInfoWord ci = charinfo.getCharInfoWord(pos);
        if (ci != null) {
            return ci.getItalic();
        }
        return null;
    }

    /**
     * Returns the kern.
     * @return Returns the kern.
     */
    public TfmKernArray getKern() {

        return kern;
    }

    /**
     * @see org.extex.font.format.tfm.TfmLigKernArray#getKerning(int, int)
     */
    public TfmFixWord getKerning(final int cp1, final int cp2) {

        return ligkern.getKerning(cp1, cp2);
    }

    /**
     * Returns the lengths.
     * @return Returns the lengths.
     */
    public TfmHeaderLengths getLengths() {

        return lengths;
    }

    /**
     * @see org.extex.font.format.tfm.TfmLigKernArray#getLigature(int, int)
     */
    public int getLigature(final int cp1, final int cp2) {

        return ligkern.getLigature(cp1, cp2);
    }

    /**
     * Returns the ligkern.
     * @return Returns the ligkern.
     */
    public TfmLigKernArray getLigkern() {

        return ligkern;
    }

    /**
     * Returns the param.
     * @return Returns the param.
     */
    public TfmParamArray getParam() {

        return param;
    }

    /**
     * Returns the value of a font parameter.
     *
     * @param name the name of the parameter.
     * @return the value of a font parameter.
     */
    public FixedDimen getParam(final String name) {

        return param.getParam(name).toDimen(getDesignSize());
    }

    /**
     * Returns the value of a font parameter as a fix word.
     *
     * @param name the name of the parameter.
     * @return the value of a font parameter as a fix word.
     */
    public TfmFixWord getParamAsFixWord(final String name) {

        return param.getParam(name);
    }

    /**
     * Returns the pfbfilename.
     * @return Returns the pfbfilename.
     */
    public String getPfbfilename() {

        return pfbfilename;
    }

    /**
     * Returns the pfbparser.
     * @return Returns the pfbparser.
     */
    public PfbParser getPfbParser() {

        return pfbparser;
    }

    /**
     * Returns the psfenc.
     * @return Returns the psfenc.
     */
    public PsFontEncoding getPsfenc() {

        return psfenc;
    }

    /**
     * Returns the psfontmap.
     * @return Returns the psfontmap.
     */
    public PsFontsMapReader getPsfontmap() {

        return psfontmap;
    }

    /**
     * Returns the width.
     * @return Returns the width.
     */
    public TfmWidthArray getWidth() {

        return width;
    }

    /**
     * Returns the width of a char.
     *
     * @param pos   the position
     * @return the width of a char, or <code>null</code>, if it does not exist.
     */
    public TfmFixWord getWidth(final int pos) {

        TfmCharInfoWord ci = charinfo.getCharInfoWord(pos);
        if (ci != null) {
            return ci.getWidth();
        }
        return null;
    }

    /**
     * Set the fontmap reader and the encoding factory.
     * @param apsfontmap    the psfonts.map reader
     * @param encf          the encoding factory
     * @throws FontException if a font-erorr occured
     * @throws ConfigurationException from the resourcefinder
     */
    public void setFontMapEncoding(final PsFontsMapReader apsfontmap,
            final EncFactory encf) throws FontException, ConfigurationException {

        psfontmap = apsfontmap;
        encfactory = encf;

        // read psfonts.map
        if (psfontmap != null) {
            psfenc = psfontmap.getPSFontEncoding(fontname);

            // encoding
            if (psfenc != null) {
                if (!"".equals(psfenc.getEncfile())) {
                    enctable = encfactory.getEncodingTable(psfenc.getEncfile());
                }
                // filename
                if (psfenc.getFontfile() != null) {
                    pfbfilename = filenameWithoutPath(psfenc.getFontfile());
                }
                // glyphname
                if (enctable != null) {
                    charinfo.setEncodingTable(enctable);
                }
            }
        }
    }

    /**
     * The pfbparser to set.
     * @param parser The pfbparser to set.
     */
    public void setPfbParser(final PfbParser parser) {

        pfbparser = parser;
        if (enctable == null && parser != null) {

            // no encoding table -> set the glyphname
            String enc[] = parser.getEncoding();
            // glyphname
            charinfo.setEncodingTable(enc);
        }
    }

    /**
     * @see org.extex.font.type.PlFormat#toPL(org.extex.font.type.PlWriter)
     */
    public void toPL(final PlWriter out) throws IOException {

        header.toPL(out);
        param.toPL(out);
        ligkern.toPL(out);
        charinfo.toPL(out);
    }

    /**
     * Visit for the {@link TfmVisitor}. 
     *
     * @param visitor The visitor.
     * @throws IOException if a io error occurred.
     */
    public void visit(final TfmVisitor visitor) throws IOException {

        visitor.visitTfmReader(this);
        visitor.visitTfmHeaderLengths(lengths);
        visitor.visitTfmHeaderArray(header);
        visitor.visitTfmCharInfoArray(charinfo);
        visitor.visitTfmWidthArray(width);
        visitor.visitTfmHeightArray(height);
        visitor.visitTfmDepthArray(depth);
        visitor.visitTfmItalicArray(italic);
        visitor.visitTfmLigKernArray(ligkern);
        visitor.visitTfmKernArray(kern);
        visitor.visitTfmExtenArray(exten);
        visitor.visitTfmParamArray(param);
    }

    /**
     * @see org.extex.util.EFMWriterConvertible#writeEFM(org.extex.util.xml.XMLStreamWriter)
     */
    public void writeEFM(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("font");
        writer.writeAttribute("id", getFontFamily());
        writer.writeAttribute("font-name", getFontFamily());
        writer.writeAttribute("font-family", getFontFamily());
        writer.writeAttribute("default-size", String.valueOf(getDesignSize()));
        writer.writeAttribute("type", "tfm");
        writer.writeAttribute("units-per-em", "1000");
        writer.writeAttribute("checksum", String.valueOf(getChecksum()));
        writer.writeAttribute("subtype", getFontType().toTFMString());
        if (pfbfilename != null) {
            writer.writeAttribute("filename", pfbfilename);
        }
        param.writeEFM(writer);
        charinfo.writeEFM(writer);
        writer.writeEndElement();
    }

}
