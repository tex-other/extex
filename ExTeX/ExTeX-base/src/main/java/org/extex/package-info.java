/*
 * Copyright (C) 2004-2011 The ExTeX Group and individual authors listed below
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
/**
 * This package contains &epsilon;&chi;T<span style=
 * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
 * >e</span>X &ndash; an implementation of a
 * typesetting engine based on the concepts of T<span style=
 * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
 * >e</span>X. The following
 * figure gives a impression of the top-level components of &epsilon;&chi;T<span style=
 * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
 * >e</span>X.
 *
 * <div class="Figure">
 *  <img src="doc-files/components.png" width="90%"/>
 *  <br />
 *  <caption>The top-level components of &epsilon;&chi;T<span style=
 * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
 * >e</span>X</caption>
 * </div>
 *
 * <dl>
 *  <dt><b>Main</b></dt>
 *  <dd>
 *   <p>
 *    The Main component encapsulates everything necessary to run
 *    &epsilon;&chi;T<span style=
 *    "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
 *    >e</span>X from the command line. This includes the evaluation
 *    of the command line arguments as well as handling the interaction with
 *    the user &ndash; e.g. in case of an error. The remaining components
 *    should not know anything about were the input is coming from or where
 *    it is going to.
 *   </p>
 *   <p>
 *    This component is located in the package Main.
 *   </p>
 *  </dd>
 *  <dt><b>ExTeX</b></dt>
 *  <dd>
 *   <p>
 *    The component called <b>ExTeX</b> is in fact just the glue which
 *    ties together all other components. It constitutes the entry
 *    point for any client using the &epsilon;&chi;T<span style=
 *   "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
 *   >e</span>X functionality.
 *   </p>
 *   <p>
 *    This component is located in this package.
 *   </p>
 *  </dd>
 *  <dt><b>Input Subsystem</b></dt>
 *  <dd>
 *   <p>
 *    The component called <b>Input Subsystem</b> provides everything
 *    to process the input from some raw bytes until a scanner has made
 *    tokens out of them. A general purpose pre-processing might be
 *    included into this process.
 *   </p>
 *   <p>
 *    This component is located in the package Interpreter.
 *   </p>
 *  </dd>
 *  <dt><b>Interpreter Subsystem</b></dt>
 *  <dd>
 *   <p>
 *    The component called <b>Interpreter Subsystem</b> provides an
 *    interpreter for the T<span style=
 *    "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
 *    >e</span>X language. This includes an
 *    engine to parse instructions &ndash; based on the tokens passed in
 *    from the input subsystem &ndash; and evaluate them.
 *   </p>
 *   <p>
 *    This component is located in the package Interpreter.
 *   </p>
 *  </dd>
 *  <dt><b>Typesetting Subsystem</b></dt>
 *  <dd>
 *   <p>
 *    The component called <b>Typesetting Subsystem</b> provides the
 *    typesetting functionality. It places objects on the page. These
 *    objects are mainly characters or boxes filled with characters or
 *    boxes.
 *   </p>
 *   <p>
 *    This component is located in the package Typesetter.
 *   </p>
 *  </dd>
 *  <dt><b>Font Subsystem</b></dt>
 *  <dd>
 *   <p>
 *    The component called <b>Font Subsystem</b> provides means to deal
 *    with fonts of several kinds. The abstraction provided by the
 *    component encapsulates the technical details of the fonts from
 *    the rest of the system.
 *   </p>
 *   <p>
 *    This component is located in the package Font.
 *   </p>
 *  </dd>
 *  <dt><b>Backend Subsystem</b></dt>
 *  <dd>
 *   <p>
 *    The component called <b>Backend Subsystem</b> provides means to
 *    translate the page description into an external format. The only
 *    format known by T<span style=
 * "text-transform:uppercase;font-size:90%;vertical-align:-0.4ex;margin-left:-0.2em;margin-right:-0.1em;line-height: 0;"
 * >e</span>X is supported as well as some
 *    others. This translation might involve some post-processing on the
 *    level of pages. For instance the selection or re-arranging of
 *    pages can be accomplished here.
 *   </p>
 *   <p>
 *    This component is located in the package Backend.
 *   </p>
 *  </dd>
 *  <dt><b>Framework</b></dt>
 *  <dd>
 *   <p>
 *    The component called <b>Framework</b> is in fact not a component
 *    of its own. It just provides cross-cutting functionality needed
 *    by the other components.
 *   </p>
 *   <p>
 *    This component is located in the package util/framework.
 *   </p>
 *  </dd>
 * </dl>
 */

package org.extex;