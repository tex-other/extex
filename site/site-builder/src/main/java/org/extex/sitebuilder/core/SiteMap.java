/*
 * Copyright (C) 2011 The ExTeX Group and individual authors listed below
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

package org.extex.sitebuilder.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * This class is a creator for a site map.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision$
 */
public class SiteMap extends TemplatingEngine {

    /**
     * The field <tt>template</tt> contains the ...
     */
    private String template = "org/extex/sitebuilder/sitemap.vm";

    /**
     * The field <tt>output</tt> contains the ...
     */
    private File output = null;

    /**
     * The field <tt>target</tt> contains the ...
     */
    private File target = null;

    /**
     * Create the site map.
     * 
     * @throws Exception in case of an error
     * 
     */
    public void generate() throws Exception {

        if (target == null) {
            throw new IllegalArgumentException("Missing target");
        }
        if (template == null) {
            throw new IllegalArgumentException("Missing template");
        }

        VelocityEngine engine = makeEngine();
        VelocityContext context = makeContext(engine);

        context.put("headContent", "");
        context.put("bodyContent", "");
        context.put("relativePath", ".");
        context.put("targetDirectory", new FileWrapper(target, "."));
        File dir = output.getParentFile();
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("directory creation failed: " + dir);
        }
        Writer writer = new BufferedWriter(new FileWriter(output));
        try {
            Template t = engine.getTemplate(template);
            t.merge(context, writer);
        } finally {
            writer.close();
            context.put("targetDirectory", null);
        }
    }

    /**
     * Setter for output.
     * 
     * @param output the output to set
     */
    public void setOutput(File output) {

        this.output = output;
    }

    /**
     * Setter for target.
     * 
     * @param target the target to set
     */
    public void setTarget(File target) {

        this.target = target;
    }

    /**
     * Setter for template.
     * 
     * @param template the template to set
     */
    public void setTemplate(String template) {

        this.template = template;
    }

}
