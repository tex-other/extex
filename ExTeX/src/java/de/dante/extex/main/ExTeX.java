/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.main;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dante.extex.main.exception.MainException;
import de.dante.extex.main.exception.MainMissingArgumentException;
import de.dante.extex.main.exception.MainUnknownOptionException;
import de.dante.extex.main.logging.LogFormatter;
import de.dante.extex.main.queryFile.QueryFileHandler;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationFactory;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This is the command line interface to ExTeX. It does all the horrible things
 * necessary to interact with the user of the command line in nearly the same
 * way as TeX does.
 * <p>
 * The command line interface provides the following features:
 * </p>
 * <ul>
 * <li>Specifying format, input file and TeX code on the command line.</li>
 * <li>Interacting with the user to get a input file.</li>
 * <li>Interacting with the user in case on an error</li>
 * </ul>
 *
 * <h3>ExTeX: Command Line Usage</h3>
 *
 * <p>
 *  This program is normally used through a wrapper which performs all
 *  necessary initializations and hides the implementation language from the
 *  casual user. Since this is the default case it is described here first.
 *  Details about the direct usage without the wrapper can be found in section
 *  <a href="#invocation">Direct Java Invocation</a>.
 * </p>
 * <p>
 *  This program &ndash; called <tt>extex</tt> here &ndash; has in its normal
 *  form of invocation one parameter. This parameter is the name of the file to
 *  process:
 * </p>
 * <pre class="CLISample">
 *   extex file.tex </pre>
 * <p>
 *  The input file is sought in the current directory and other locations.
 *  Details about searching can be found in <a href="#fileSearch">Searching TeX
 *  Files</a>.
 * </p>
 * <p>
 *  In general the syntax of invocation is as follows:
 * </p>
 * <pre class="CLIsyntax">
 *   extex &lang;options&rang; &lang;file&rang; &lang;code&rang; </pre>
 * <p>
 *  Command line parameters are the way of setting options with
 *  highest priority. The command line parameters overrule all
 *  settings in other parameter files. The command line options are
 *  contained in the table below.
 * </p>
 *
 * <dl>
 *   <dt><tt>&lang;code&rang;</tt></dt>
 *   <dd>
 *    This parameter contains ExTeX code to be executed directly. The
 *    execution is performed after any code specified in an input file.
 *    on the command line the code has to start with a backslash. This
 *    restriction does not hold for the property settings.
 *   </dd>
 *   <dd>Property: <tt><a href="#extex.code">extex.code</a></tt></dd>
 *
 *   <dt><tt>&lang;file&rang;</tt></dt>
 *   <dd>
 *    This parameter contains the file to read from. A file name may
 *    not start with a backslash or a ampercent. It has no default.
 *   </dd>
 *   <dd>Property:
 *    <a href="#extex.file"><tt>extex.file</tt></a></dd>
 *
 *   <dt><a name="-configuration"><tt>-configuration &lang;resource&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the name of the configuration resource to use.
 *    This configuration resource is sought on the class path.
 *   </dd>
 *   <dd>Property: <tt><a href="#extex.config">extex.config</a></tt></dd>
 *
 *   <dt><a name="-copyright"><tt>-copyright</tt></a></dt>
 *   <dd>
 *    This command line option produces a copyright notice on the
 *    standard output stream and terminates the program afterwards.
 *   </dd>
 *
 *   <dt><tt>&amp;&lang;format&rang;</tt></dt>
 *   <dt><a name="-fmt"><tt>-fmt &lang;format&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the name of the format to read. An empty
 *    string denotes that no format should be read. This is the default.
 *   </dd>
 *   <dd>Property: <tt><a href="#extex.fmt">extex.fmt</a></tt></dd>
 *
 *   <dt><a name="-debug"><tt>-debug &lang;spec&rang;</tt></a></dt>
 *   <dd>
 *    This command line parameter can be used to instruct the program to produce
 *    debugging output of several kinds. The specification &lang;spec&rang; is
 *    interpreted left to right. Each character is interpreted according to the
 *    following table:
 *    <table>
 *     <th>
 *      <td>Spec</td>Description<td></td><td>See</td>
 *     </th>
 *     <tr>
 *      <td><tt>F</tt></td>
 *      <td>
 *       This specifier contains the indicator whether or not to trace the
 *       searching for input files.
 *      </td>
 *      <td><tt><a href="#extex.trace.input.files">extex.trace.input.files</a></tt></td>
 *     </tr>
 *     <tr>
 *      <td><tt>f</tt></td>
 *      <td>
 *       This specifier contains the indicator whether or not to trace the
 *       searching for font files.
 *      </td>
 *      <td><tt><a href="#extex.trace.font.files">extex.trace.font.files</a></tt></td>
 *     </tr>
 *     <tr>
 *      <td><tt>M</tt></td>
 *      <td>
 *       This specifier contains the indicator whether or not to trace the
 *       execution of macros.
 *      </td>
 *      <td><tt><a href="#extex.trace.macros">extex.trace.macros</a></tt></td>
 *     </tr>
 *     <tr>
 *      <td><tt>T</tt></td>
 *      <td>
 *       This specifier contains the indicator whether or not to trace the
 *       work of the tokenizer.
 *      </td>
 *      <td><tt><a href="#extex.trace.tokenizer">extex.trace.tokenizer</a></tt></td>
 *     </tr>
 *    </table>
 *   </dd>
 *
 *   <dt><a name="-halt"><tt>-halt-on-error</tt></a></dt>
 *   <dd>
 *    This parameter contains the indicator whether the processing should
 *    halt after the first error has been encountered.
 *   </dd>
 *   <dd>Property:
 *     <tt><a href="#extex.halt.on.error">extex.halt.on.error</a></tt></dd>
 *
 *   <dt><a name="-help"><tt>-help</tt></a></dt>
 *   <dd>
 *    This command line option produces a short usage description on the
 *    standard output stream and terminates the program afterwards.
 *   </dd>
 *
 *   <dt><a name="-ini"><tt>-ini</tt></a></dt>
 *   <dd>
 *    If set to <code>true</code> then act as initex. This co�mand line
 *    option is defined for compatibility to TeX only. In ExTeX it has no
 *    effect at all.
 *   </dd>
 *   <dd>Property: <tt><a href="#extex.ini">extex.ini</a></tt> </dd>
 *
 *   <dt><a name="-interaction"><tt>-interaction &lang;mode&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the interaction mode. Possible values are
 *    the numbers 0..3 and the symbolic names batchmode (0), nonstopmode (1),
 *    scrollmode (2), and errorstopmode (3).
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.interaction">extex.interaction</a></tt></dd>
 *
 *   <dt><a name="-job"><tt>-job-name &lang;name&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the name of the job. It is overwritten
 *    if a file is given to read from. In this case the base name of
 *    the input file is used instead.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.jobname">extex.jobname</a></tt></dd>
 *
 *   <dt><a name="-language"><tt>-language &lang;language&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the name of the locale to be used for the
 *    messages.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.lang">extex.lang</a></tt> </dd>
 *
 *   <dt><a name="-output"><tt>-output &lang;format&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the output format. This logical name is resolved
 *    via the configuration.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.output">extex.output</a></tt></dd>
 *
 *   <dt><a name="-progname"/><tt>-progname &lang;name&rang;</tt></dt>
 *   <dd>
 *    This parameter can be used to overrule the name of the program shown in
 *    the banner and the version information.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.progname">extex.progname</a></tt></dd>
 *
 *   <dt><a name="-texinputs"/><tt>-texinputs &lang;path&rang;</tt></dt>
 *   <dd>
 *    This parameter contains the additional directories for searching TeX
 *    input files.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.texinputs">extex.texinputs</a></tt> </dd>
 *
 *   <dt><a name="-texmfoutputs"><tt>-texmfoutputs &lang;dir&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the name of the
 *    property for the fallback if the output directory fails to be writable.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.outputdir.fallback">extex.outputdir.fallback</a></tt>
 *   </dd>
 *
 *   <dt><a name="-texoutputs"><tt>-texoutputs &lang;dir&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contain the directory where output files should be
 *    created.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.outputdir">extex.outputdir</a></tt></dd>
 *
 *   <dt><a name="-version"/><tt>-version</tt></dt>
 *   <dd>
 *    This command line parameter forces that the version information
 *    is written to standard output and the program is terminated.
 *   </dd>
 * </dl>
 *
 * <p>
 *  Command line parameters can be abbreviated up to a unique prefix
 *  &ndash; and sometimes even more. Thus the following invocations
 *  are equivalent:
 * <pre class="CLIsyntax">
 *   extex -v
 *   extex -ve
 *   extex -ver
 *   extex -vers
 *   extex -versi
 *   extex -versio
 *   extex -version  </pre>
 * </p>
 *
 *
 * <a name="fileSearch"/><h3>Searching Files</h3>
 *
 * TODO gene: doc incomplete
 *
 *
 *
 * <a name="settings"/><h3>Settings and Command Line Parameters</h3>
 *
 * <p>
 *  Settings can be stored in properties files. Those settings are the
 *  fallbacks if no corresponding command line arguments are found.
 * </p>
 * <p>
 *  The properties are stored in a file named <tt>.extex</tt>. It is
 *  sought in the users home directory. This determined by the system
 *  property <tt>user.home</tt>. Afterwards it is sought in the
 *  current directory. The local settings of a directory overwrite the
 *  user's setting. The user's setting overwrite the compiled in defaults
 * </p>
 * <p>
 * The following properties are recognized:
 * </p>
 * <dl>
 *   <dt><a name="extex.code"/><tt>extex.code</tt></a></dt>
 *   <dd>
 *    This parameter contains ExTeX code to be executed directly. The
 *    execution is performed after any code specified in an input file.
 *    on the command line the code has to start with a backslash. This
 *    restriction does not hold for the property settings.
 *   </dd>
 *   <dd>Command line: <tt>&lang;code&rang;</tt></dd>
 *
 *   <dt><a name="extex.config"/><tt>extex.config</tt></dt>
 *   <dd>
 *    This parameter contains the name of the configuration resource to use.
 *    This configuration resource is sought on the class path.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-configuration"><tt>-configuration &lang;resource&rang;</tt></a></dd>
 *   <dd>Default: <tt>extex.xml</tt></dd>
 *
 *   <dt><a name="extex.encoding"/><tt>extex.encoding</tt></dt>
 *   <dd>
 *    This parameter contains the name of the property for
 *    the standard encoding to use.
 *   </dd>
 *   <dd>Default: <tt>ISO-8859-1</tt></dd>
 *
 *   <dt><a name="extex.error.handler"/><tt>extex.error.handler</tt></dt>
 *   <dd>
 *    This parameter contains the logical name of the error handler.
 *   </dd>
 *
 *   <dt><a name="extex.error.handler"/><tt>extex.error.handler</tt></dt>
 *   <dd>
 *    This parameter contains the logical name of the error handler.
 *   </dd>
 *
 *   <dt><a name="extex.fonts"/><tt>extex.fonts</tt></dt>
 *   <dd>
 *    This parameter contains the name of the property indicating where to
 *    find font files. The value is a path similar to extex.texinputs.
 *   </dd>
 *
 *   <dt><a name="extex.halt.on.error"/><tt>extex.halt.on.error</tt></dt>
 *   <dd>
 *    This parameter contains the name of the property indicating whether the
 *    processing should stop after the first error.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-halt"><tt>-halt-on-error</tt></a> </dd>
 *
 *   <dt><a name="extex.file"><tt>extex.file</tt></a></dt>
 *   <dd>
 *    This parameter contains the file to read from. It has no default
 *   </dd>
 *   <dd>Command line:
 *    <a href="&lang"><tt>&lang;file&rang;</tt></a> </dd>
 *
 *   <dt><a name="extex.fmt"/><tt>extex.fmt</tt></dt>
 *   <dd>
 *    This parameter contains the name of the format to read. An empty
 *    string denotes that no format should be read. This is the default.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-fmt"><tt>-fmt &lang;format&rang;</tt></a></dd>
 *
 *   <dt><a name="extex.ini"/><tt>extex.ini</tt></dt>
 *   <dd>
 *    If set to <code>true</code> then act as initex. This command line
 *    option is defined for compatibility to TeX only. In ExTeX it has no
 *    effect at all.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-ini"><tt>-ini</tt></a> </dd>
 *
 *   <dt><a name="extex.interaction"/><tt>extex.interaction</tt></dt>
 *   <dd>
 *    This parameter contains the interaction mode. possible values are
 *    the numbers 0..3 and the symbolic names batchmode (0), nonstopmode (1),
 *    scrollmode (2), and errorstopmode (3).
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-interaction"><tt>-interaction &lang;mode&rang;</tt></a></dd>
 *   <dd>Default: <tt>3</tt></dd>
 *
 *   <dt><a name="extex.jobname"/><tt>extex.jobname</tt></dt>
 *   <dd>
 *    This parameter contains the name of the job. It is overwritten
 *    if a file is given to read from. In this case the basename of
 *    the input file is used instead.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-job"><tt>-job-name &lang;name&rang;</tt></a></dd>
 *   <dd>Default: <tt>texput</tt></dd>
 *
 *   <dt><a name="extex.jobnameMaster"/><tt>extex.jobnameMaster</tt></dt>
 *   <dd>
 *    This parameter contains the name of the job to be used with high
 *    priority.
 *   </dd>
 *   <dd>Default: <tt>texput</tt></dd>
 *
 *   <dt><a name="extex.lang"/><tt>extex.lang</tt></dt>
 *   <dd>
 *    This parameter contains the name of the locale to be used for the
 *    messages.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-language"><tt>-language &lang;language&rang;</tt></a> </dd>
 *
 *   <dt><a name="extex.nobanner"/><tt>extex.nobanner</tt></dt>
 *   <dd>
 *    This parameter contains a boolean indicating that the banner should be
 *    suppressed.
 *   </dd>
 *
 *   <dt><a name="extex.output"/><tt>extex.output</tt></dt>
 *   <dd>
 *    This parameter contains the output format. This logical name is resolved
 *    via the configuration.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-output"><tt>-output &lang;format&rang;</tt></a></dd>
 *   <dd>Default: <tt>pdf</tt></dd>
 *
 *   <dt><a name="extex.outputdir"/><tt>extex.outputdir</tt></dt>
 *   <dd>
 *    This parameter contain the directory where output files should be
 *    created.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-texoutputs"><tt>-texoutputs &lang;dir&rang;</tt></a></dd>
 *   <dd>Default: <tt>.</tt></dd>
 *
 *   <dt><a name="extex.outputdir.fallback"/><tt>extex.outputdir.fallback</tt></dt>
 *   <dd>
 *    This parameter contains the name of the
 *    property for the fallback if the output directory fails to be writable.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-texmfoutputs"><tt>-texmfoutputs &lang;dir&rang;</tt></a> </dd>
 *
 *   <dt><a name="extex.progname"/><tt>extex.progname</tt></dt>
 *   <dd>
 *    This parameter can be used to overrule the name of the program shown in
 *    the banner and the version information.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-progname"><tt>-progname</tt></a></dd>
 *   <dd>Default: <tt>ExTeX</tt></dd>
 *
 *   <dt><a name="extex.texinputs"/><tt>extex.texinputs</tt></dt>
 *   <dd>
 *    This parameter contains the additional directories for searching TeX
 *    input files.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-texinputs"><tt>-texinputs &lang;path&rang;</tt></a> </dd>
 *
 *   <dt><a name="extex.token.stream"/><tt>extex.token.stream</tt></dt>
 *   <dd>
 *    This string parameter contains the logical name of the configuration to
 *    use for the token stream.
 *   </dd>
 *
 *   <dt><a name="extex.trace.input.files"/><tt>extex.trace.input.files</tt></dt>
 *   <dd>
 *    This boolean parameter contains the indicator whether or not to trace the
 *    search for input files.
 *   </dd>
 *
 *   <dt><a name="extex.trace.font.files"/><tt>extex.trace.font.filess</tt></dt>
 *   <dd>
 *    This boolean parameter contains the indicator whether or not to trace the
 *    search for font files.
 *   </dd>
 *
 *   <dt><a name="extex.trace.macros"/><tt>extex.trace.macros</tt></dt>
 *   <dd>
 *    This boolean parameter contains the indicator whether or not to trace the
 *    execution of macros.
 *   </dd>
 *
 *   <dt><a name="extex.trace.tokenizer"/><tt>extex.trace.tokenizer</tt></dt>
 *   <dd>
 *    This boolean parameter contains the indicator whether or not to trace the
 *    work of the tokenizer.
 *   </dd>
 *
 *   <dt><a name="extex.typesetter"/><tt>extex.typesetter</tt></dt>
 *   <dd>
 *    This parameter contains the name of the typesetter to use. If it is
 *    not set then the default from the configuration file is used.
 *   </dd>
 * </dl>
 *
 * <p>
 *  There is another level of properties which is considered between the
 *  compiled in defaults and the user's  Those are the system
 *  properties of the Java system. There system wide settings can be stored.
 *  Nevertheless, you should use this feature sparsely.
 * </p>
 *
 *
 * <a name="configuration"/><h3>Configuration Resources</h3>
 *
 * <p>
 *  The configuration of ExTeX is controlled by several configuration resources.
 *  The fallback for those configuration resources are contained in the ExTeX
 *  jar file. In this section we will describe how to overwrite the settings in
 *  the default configuration resource.
 * </p>
 *
 * TODO gene: doc incomplete
 *
 *
 *
 * <a name="invocation"/><h3>Direct Java Invocation</h3>
 *
 * <p>
 *  The direct invocation of the Java needs some settings to be preset.
 *  These settings are needed for ExTeX to run properly. The following
 *  premises are needed:
 * </p>
 * <ul>
 *  <li>Java needs to be installed (see section
 *   <a href="#installation">Installation</a>. The program <tt>java</tt> is
 *   assumed to be on the path of executables.
 *  </li>
 *  <li>Java must be configured to find the jar files from the ExTeX
 *   distribution. This can be accomplished by setting the environment variable
 *   <tt>CLASSPATH</tt> or <tt>JAVA_HOME</tt>. See the documentation of your
 *   Java system for details.
 *  </li>
 * </ul>
 * <p>
 *  Now ExTeX can be invoked with the same parameters described above:
 * </p>
 * <pre class="CLIsyntax">
 *   java de.dante.extex.ExTeX &lang;options&rang; &lang;file&rang; </pre>
 * <p>
 *  The result should be the same as the invocation of the wrapper.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 *
 * @version $Revision$
 */
public class ExTeX extends de.dante.extex.ExTeX {

    /**
     * The constant <tt>COPYRIGHT_YEAR</tt> contains the starting year of
     * development for the copyright message. This number is fixed to be the
     * year 2003.
     */
    private static final int COPYRIGHT_YEAR = 2003;

    /**
     * The field <tt>DOT_EXTEX</tt> contains the name of the user properties
     * file. This file contains property settings which are read when ExTeX is
     * started.
     */
    private static final String DOT_EXTEX = ".extex";

    /**
     * The constant <tt>EXIT_INTERNAL_ERROR</tt> contains the exit code for
     * internal errors.
     */
    private static final int EXIT_INTERNAL_ERROR = -666;

    /**
     * The constant <tt>EXIT_OK</tt> contains the exit code of the program for
     * the success case.
     */
    private static final int EXIT_OK = 0;

    /**
     * The constant <tt>VERSION</tt> contains the manually incremented version
     * string.
     */
    private static final String EXTEX_VERSION = new Version().toString();

    /**
     * The field <tt>PROP_CODE</tt> contains the name of the
     * property for the TeX code to be inserted at the beginning of the job.
     */
    private static final String PROP_CODE = "extex.code";

    /**
     * The field <tt>PROP_CONFIG</tt> contains the name of the
     * property for the configuration resource to use.
     */
    private static final String PROP_CONFIG = "extex.config";

    /**
     * The field <tt>PROP_FILE</tt> contains the name of the property for the
     * input file to read.
     */
    private static final String PROP_FILE = "extex.file";

    /**
     * The field <tt>PROP_FMT</tt> contains the name of the property for the
     * name of the format file to use.
     */
    private static final String PROP_FMT = "extex.fmt";

    /**
     * The field <tt>PROP_HALT_ON_ERROR</tt> contains the name of the property
     * indicating whether the processing should stop at the first error.
     */
    private static final String PROP_HALT_ON_ERROR = "extex.halt.on.error";

    /**
     * The field <tt>PROP_INI</tt> contains the name of the property for the
     * boolean value indicating that some kind of emulations for iniTeX should
     * be provided. Currently this has no effect in ExTeX.
     */
    private static final String PROP_INI = "extex.ini";

    /**
     * The field <tt>PROP_INTERACTION</tt> contains the name of the
     * property for the interaction mode.
     */
    private static final String PROP_INTERACTION = "extex.interaction";

    /**
     * The field <tt>PROP_JOBNAME</tt> contains the name of the
     * property for the job name. The value can be overruled by the property
     * named in <tt>PROP_JOBNAME_MASTER</tt>.
     */
    private static final String PROP_JOBNAME = "extex.jobname";

    /**
     * The field <tt>PROP_JOBNAME_MASTER</tt> contains the name of the
     * property for the jobname to be used with high priority.
     */
    private static final String PROP_JOBNAME_MASTER = "extex.jobnameMaster";

    /**
     * The field <tt>PROP_LANG</tt> contains the name of the property for the
     * language to use for messages.
     */
    private static final String PROP_LANG = "extex.lang";

    /**
     * The field <tt>PROP_OUTPUT_TYPE</tt> contains the name of the property for
     * the output driver. This value is resolved by the
     * {@link de.dante.extex.documentWriter.DocumentWriterFactory DocumentWriterFactory}
     * to find the appropriate class.
     */
    private static final String PROP_OUTPUT_TYPE = "extex.output";

    /**
     * The field <tt>PROP_OUTPUTDIR</tt> contains the name of the
     * property for the output directory.
     */
    private static final String PROP_OUTPUTDIR = "extex.outputdir";

    /**
     * The field <tt>PROP_PROGNAME</tt> contains the name of the
     * property for the program name used in usage messages.
     */
    private static final String PROP_PROGNAME = "extex.progname";

    /**
     * The field <tt>PROP_TEXINPUTS</tt> contains the name of the
     * property for the additional texinputs specification of directories.
     */
    private static final String PROP_TEXINPUTS = "extex.texinputs";

    /**
     * The field <tt>PROP_TRACE_FONT_FILES</tt> contains the name of the
     * property for the boolean determining whether or not the searching for
     * font files should produce tracing output.
     */
    private static final String PROP_TRACE_FONT_FILES = "extex.trace.font.files";

    /**
     * The field <tt>PROP_TRACE_INPUT_FILES</tt> contains the name of the
     * property for the boolean determining whether or not the searching for
     * input files should produce tracing output.
     */
    private static final String PROP_TRACE_INPUT_FILES = "extex.trace.input.files";

    /**
     * The field <tt>PROP_TRACE_MACROS</tt> contains the name of the
     * property for the boolean determining whether or not the execution of
     * macros should produce tracing output.
     */
    private static final String PROP_TRACE_MACROS = "extex.trace.macros";

    /**
     * The field <tt>PROP_TRACE_TOKENIZER</tt> contains the name of the
     * property for the boolean determining whether or not the tokenizer
     * should produce tracing output.
     */
    private static final String PROP_TRACE_TOKENIZER = "extex.trace.tokenizer";

    /**
     * The constant <tt>TRACE_MAP</tt> contains the mapping from single
     * characters to tracing property names.
     */
    private static final Map TRACE_MAP = new HashMap();

    static {
        TRACE_MAP.put("F", PROP_TRACE_INPUT_FILES);
        TRACE_MAP.put("f", PROP_TRACE_FONT_FILES);
        TRACE_MAP.put("M", PROP_TRACE_MACROS);
        TRACE_MAP.put("T", PROP_TRACE_TOKENIZER);
    }

    /**
     * Log a Throwable including its stack trace to the logger.
     *
     * @param logger the target logger
     * @param text the prefix text to log
     * @param e the Throwable to log
     */
    private static void logException(final Logger logger, final String text,
            final Throwable e) {

        logger.severe(text == null ? "" : text);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(os);
        e.printStackTrace(writer);
        writer.flush();
        logger.fine(os.toString());
    }

    /**
     * This is the main method which is invoked to run the whole engine from
     * the command line. It creates a new ExTeX object and invokes
     * <tt>{@link #run(java.lang.String[]) run()}</tt> on it.
     * <p>
     * The return value is used as the exit status.
     * </p>
     * <p>
     * The properties to be used are taken from the
     * <tt>{@link java.lang.System#getProperties() System.properties}</tt> and
     * the user's properties in the file <tt>.extex</tt>. The user properties
     * are loaded both from the users home directory and the current directory.
     * Finally the properties can be overwritten on the command line.
     * </p>
     *
     * @param args the list of command line arguments
     *
     * @see #ExTeX(java.util.Properties,java.lang.String)
     */
    public static void main(final String[] args) {

        int status;

        try {
            ExTeX extex = new ExTeX(System.getProperties(), DOT_EXTEX);
            status = extex.run(args);
        } catch (Throwable e) {
            Logger logger = Logger.getLogger(ExTeX.class.getName());
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);

            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new LogFormatter());
            consoleHandler.setLevel(Level.WARNING);
            logger.addHandler(consoleHandler);

            Localizer localizer = LocalizerFactory.getLocalizer(ExTeX.class
                    .getName());
            logException(logger, //
                    localizer.format("ExTeX.SevereError", e.toString()), e);
            status = EXIT_INTERNAL_ERROR;
        }

        System.exit(status);
    }

    /**
     * Creates a new object and initializes the properties from given
     * properties and possibly from a user's properties in the file
     * <tt>.extex</tt>.
     * The user properties are loaded from the users home directory and the
     * current directory.
     *
     * @param theProperties the properties to consider
     * @param dotFile the name of the local configuration file. In the case
     *            that this value is <code>null</code> no user properties
     *            will be considered.
     *
     * @throws MainException in case of an IO Error during the reading of the
     *             properties file
     *
     * @see de.dante.extex.ExTeX#ExTeX(java.util.Properties, java.lang.String)
     */
    public ExTeX(final Properties theProperties, final String dotFile)
            throws MainException {

        super(theProperties, dotFile);
    }

    /**
     * This class provides access to the whole functionality of ExTeX on the
     * command line. The exception is that this method does not call
     * <code>{@link System#exit(int) System.exit()}</code>
     * but returns the exit status as result.
     *
     * @param args the list of command line arguments
     *
     * @return the exit status
     */
    public int run(final String[] args) {

        boolean onceMore = true;
        int returnCode = EXIT_OK;

        try {

            for (int i = 0; onceMore && i < args.length; i++) {
                String arg = args[i];

                if (arg.startsWith("-")) {
                    if ("-".equals(arg)) {
                        runWithFile(args, i + 1);
                        onceMore = false;
                    } else if ("-configuration".startsWith(arg)) {
                        useArg(PROP_CONFIG, args, ++i);
                    } else if ("-copyright".startsWith(arg)) {
                        int year = Calendar.getInstance().get(Calendar.YEAR);
                        getLogger()
                                .info(
                                        getLocalizer()
                                                .format(
                                                        "ExTeX.Copyright",
                                                        (year <= COPYRIGHT_YEAR
                                                                ? Integer
                                                                        .toString(COPYRIGHT_YEAR)
                                                                : Integer
                                                                        .toString(COPYRIGHT_YEAR)
                                                                        + "-"
                                                                        + Integer
                                                                                .toString(year))));
                        onceMore = false;
                    } else if ("-help".startsWith(arg)) {
                        getLogger().info(
                                getLocalizer().format("ExTeX.Usage", "extex"));
                        onceMore = false;
                    } else if ("-fmt".startsWith(arg)) {
                        useArg(PROP_FMT, args, ++i);
                        i++;
                    } else if (arg.startsWith("-fmt=")) {
                        setProperty(PROP_FMT, arg.substring("-fmt=".length()));
                    } else if ("-halt-on-error".startsWith(arg)) {
                        setProperty(PROP_HALT_ON_ERROR, "true");
                    } else if ("-interaction".startsWith(arg)) {
                        useArg(PROP_INTERACTION, args, ++i);
                        applyInteraction();
                    } else if ("-ini".startsWith(arg)) {
                        setProperty(PROP_INI, "true");
                    } else if (arg.startsWith("-interaction=")) {
                        setProperty(PROP_INTERACTION, arg
                                .substring("-interaction=".length()));
                        applyInteraction();
                    } else if ("-job-name".startsWith(arg)) {
                        useArg(PROP_JOBNAME_MASTER, args, ++i);
                    } else if (arg.startsWith("-job-name=")) {
                        setProperty(PROP_JOBNAME_MASTER, arg
                                .substring("-job-name=".length()));
                    } else if ("-language".startsWith(arg)) {
                        useArg(PROP_LANG, args, ++i);
                        applyLanguage();
                    } else if ("-progname".startsWith(arg)) {
                        useArg(PROP_PROGNAME, args, ++i);
                    } else if (arg.startsWith("-progname=")) {
                        setProperty(PROP_PROGNAME, arg.substring("-progname="
                                .length()));
                    } else if ("-version".startsWith(arg)) {
                        getLogger().info(
                                getLocalizer().format("ExTeX.Version",
                                        getProperty(PROP_PROGNAME),
                                        EXTEX_VERSION,
                                        getProperty("java.version")));
                        onceMore = false;
                    } else if ("-output".startsWith(arg)) {
                        useArg(PROP_OUTPUT_TYPE, args, ++i);
                    } else if ("-texinputs".startsWith(arg)) {
                        useArg(PROP_TEXINPUTS, args, ++i);
                    } else if ("-texoutputs".startsWith(arg)) {
                        useArg(PROP_OUTPUTDIR, args, ++i);
                    } else if ("-texmfoutputs".startsWith(arg)) {
                        useArg("extex.fallbackOutputdir", args, ++i);
                    } else if ("-debug".startsWith(arg)) {
                        useTrace(args, ++i);
                    } else if ("--".equals(arg)) {
                        useArg(PROP_CONFIG, args, ++i);
                    } else if (!loadArgumentFile(arg.substring(1))) {
                        throw new MainUnknownOptionException(arg);
                    }
                } else if (arg.startsWith("&")) {
                    setProperty(PROP_FMT, arg.substring(1));
                    runWithFile(args, i + 1);
                    onceMore = false;
                } else if (arg.startsWith("\\")) {
                    runWithArgs(args, i);
                    onceMore = false;
                } else if (!arg.equals("")) {
                    runWithFile(args, i);
                    onceMore = false;
                }
            }

            if (onceMore) {
                runWithoutFile();
            }
        } catch (MainException e) {
            try {
                showBanner(null, Level.INFO);
            } catch (MainException e1) {
                logException(getLogger(), e1.getLocalizedMessage(), e1);
            }
            logException(getLogger(), e.getLocalizedMessage(), e);
            returnCode = e.getCode();
        } catch (Throwable e) {
            try {
                showBanner(null, Level.INFO);
            } catch (MainException e1) {
                logException(getLogger(), e1.getLocalizedMessage(), e1);
            }
            logInternalError(e);
            getLogger().info(
                    getLocalizer().format("ExTeX.Logfile",
                            getProperty(PROP_JOBNAME)));

            returnCode = EXIT_INTERNAL_ERROR;
        }

        return returnCode;
    }

    /**
     * The command line is processed starting at an argument which starts with
     * a backslash. This argument and any following argument are taken as input
     * to the tokenizer.
     *
     * @param arguments the list of arguments to process
     * @param position starting index
     *
     * @throws MainException in case of an error in {@link #run() run()}
     */
    private void runWithArgs(final String[] arguments, final int position)
            throws MainException {

        if (position < arguments.length) {
            StringBuffer in = new StringBuffer();

            for (int i = position; i < arguments.length; i++) {
                in.append(" ");
                in.append(arguments[i]);
            }

            setProperty(PROP_CODE, in.toString());
        }

        run();
    }

    /**
     * Process the command line arguments when the i<sup>th</sup> argument
     * is a file name. The file is prepared to be read from. The remaining
     * arguments are used as input to the processor.
     *
     * @param arguments the list of arguments to process
     * @param position starting index
     *
     * @throws MainException in case of an error
     */
    private void runWithFile(final String[] arguments, final int position)
            throws MainException {

        if (position >= arguments.length) {
            runWithoutFile();
        } else {
            setInputFileName(arguments[position]);
            runWithArgs(arguments, position + 1);
        }
    }

    /**
     * Ask the query file handler to provide a file name and use it.
     *
     * @throws MainException in case of an error
     */
    private void runWithoutFile() throws MainException {

        try {
            showBanner(new ConfigurationFactory()
                    .newInstance(getProperty(PROP_CONFIG)), Level.INFO);
        } catch (ConfigurationException e) {
            // ignored on purpose. It will be checked again later
        }

        QueryFileHandler queryFileHandler = getQueryFileHandler();
        setInputFileName((queryFileHandler != null ? queryFileHandler
                .query(getLogger()) : null));
        run();
    }

    /**
     * Setter for the input file name.
     *
     * @param name the name of the input file. If it is <code>null</code> then
     *  the values are reset to the initial state
     */
    private void setInputFileName(final String name) {

        if (name != null) {
            setProperty(PROP_JOBNAME, //
                    (name.matches(".*\\.[a-zA-Z0-9_]*") //
                            ? name.substring(0, name.lastIndexOf(".")) : name));
            setProperty(PROP_FILE, name);
        } else {
            setProperty(PROP_JOBNAME, "texput");
            setProperty(PROP_FILE, "");
        }
    }

    /**
     * Acquire the next argument from the command line and set a property
     * accordingly. If none is found then an exception is thrown.
     *
     * @param name the name of the argument
     * @param arguments the list of arguments
     * @param position the starting index
     *
     * @throws MainMissingArgumentException in case of an error
     */
    protected void useArg(final String name, final String[] arguments,
            final int position) throws MainMissingArgumentException {

        if (position >= arguments.length) {
            throw new MainMissingArgumentException(name);
        }

        setProperty(name, arguments[position]);
    }

    /**
     * Acquire the next argument from the command line and use it as a
     * specification to control the tracing features. The appropriate properties
     * are set accordingly.
     *
     * @param arguments the list of arguments
     * @param position the starting index
     *
     * @throws MainMissingArgumentException in case that no key letters follow
     * @throws MainUnknownOptionException in case that the specified option
     *  letter has no assigned property to set
     */
    protected void useTrace(final String[] arguments, final int position)
            throws MainUnknownOptionException,
                MainMissingArgumentException {

        getLogger().setLevel(Level.FINE);
        if (position >= arguments.length) {
            throw new MainMissingArgumentException("debug");
        }
        String s = arguments[position];
        for (int i = 0; i < s.length(); i++) {
            String prop = (String) TRACE_MAP.get(s.substring(i, i + 1));
            if (prop != null) {
                setProperty(prop, "true");
            } else {
                throw new MainUnknownOptionException(s.substring(i, i + 1));
            }
        }
    }

}