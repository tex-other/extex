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
/**
 * This package contains the classes for the built-in functions of
 * the processor.
 * <p>
 *   Built-in functions are implemented as classes which are
 *   characterized as follows:
 * </p>
 * <ul>
 *   <li>They have a method <code>execute()</code> which performs the actions
 *     required.
 *   </li>
 *   <li>They have a constructor which takes a String argument which is the name
 *     under which this function is registered in the processor context.
 *   </li>
 * </ul>
 * <p>
 *   I.e. they implement the interface {@link org.extex.exbib.core.bst.code.Code Code}.
 * </p>
 * <p>
 *   The functions are either integrated in the processor statically like in 
 *   <code>{@link org.extex.exbib.core.bst.BstInterpreter099c BstInterpreter099c}</code>.
 *   Alternatively they can be loaded dynamically according to a configuration
 *   file like in <code>{@link org.extex.exbib.core.bst.BstInterpreter BstInterpreter}</code>.
 * </p>

 * <h2>Dependencies of the functions and implementations</h2>

 * <table style="border-top-width:3px;border-bottom-width:3px;border-top-style:solid;border-bottom-style:solid;font-family:helvetica;font-size:9pt;margin-left:2pt;margin-right:2pt" align="center">
 *   <tr style="border-bottom-width:2px;border-bottom-stlye:solid;">
 *     <th>Function Name</th>
 *     <th>Class</th>
 *     <th>Braces</th>
 *     <th>T<sub>E</sub>X</th>
 *     <th>Western</th>
 *   </tr>
 *   <tr>
 *     <td><tt>add.period$</tt></td>
 *     <td><tt>AddPeriod</tt></td>
 *     <td>ignore right braces</td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>call.type$</tt></td>
 *     <td><tt>CallType</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>change.case$</tt></td>
 *     <td><tt>ChangeCase</tt></td>
 *     <td>Consider brace level</td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>chr.to.int$</tt></td>
 *     <td><tt>ChrToInt</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>cite$</tt></td>
 *     <td><tt>Cite</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>*</tt></td>
 *     <td><tt>Concat</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>duplicate$</tt></td>
 *     <td><tt>Duplicate</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>empty$</tt></td>
 *     <td><tt>Empty</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>=</tt></td>
 *     <td><tt>Eq</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>format.name$</tt></td>
 *     <td><tt>FormatName</tt></td>
 *     <td>Consider brace level</td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>format.name$</tt></td>
 *     <td><tt>FormatName099</tt></td>
 *     <td>Consider brace level</td>
 *     <td></td>
 *     <td>Knows tie</td>
 *   </tr>
 *   <tr>
 *     <td><tt>&gt;</tt></td>
 *     <td><tt>Gt</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>if$</tt></td>
 *     <td><tt>If</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>int.to.chr$</tt></td>
 *     <td><tt>IntToChr</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>int.to.str$</tt></td>
 *     <td><tt>IntToStr</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>&lt;</tt></td>
 *     <td><tt>Lt</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>-</tt></td>
 *     <td><tt>Minus</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>missing$</tt></td>
 *     <td><tt>Missing</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>newline$</tt></td>
 *     <td><tt>Newline</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>num.names$</tt></td>
 *     <td><tt>NumNames</tt></td>
 *     <td>Consider brace level</td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>+</tt></td>
 *     <td><tt>Plus</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>pop$</tt></td>
 *     <td><tt>Pop</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>preamble$</tt></td>
 *     <td><tt>Preamble</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>purify$</tt></td>
 *     <td><tt>Purify</tt></td>
 *     <td>Consider brace level</td>
 *     <td>Knows special characters</td>
 *     <td>*</td>
 *   </tr>
 *   <tr>
 *     <td><tt>quote$</tt></td>
 *     <td><tt>Quote</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>:=</tt></td>
 *     <td><tt>Set</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>skip$</tt></td>
 *     <td><tt>Skip</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>stack$</tt></td>
 *     <td><tt>Stack</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>substring$</tt></td>
 *     <td><tt>Substring</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>swap$</tt></td>
 *     <td><tt>Swap</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>text.length$</tt></td>
 *     <td><tt>TextLength</tt></td>
 *     <td>Ignore braces</td>
 *     <td>Knows special characters</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>text.prefix$</tt></td>
 *     <td><tt>TextPrefix</tt></td>
 *     <td>Consoder brace level</td>
 *     <td>Knows special charcters</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>Top$</tt></td>
 *     <td><tt>Top</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>type$</tt></td>
 *     <td><tt>Type</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>warning$</tt></td>
 *     <td><tt>Warning</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>while$</tt></td>
 *     <td><tt>While</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td><tt>width$</tt></td>
 *     <td><tt>Width</tt></td>
 *     <td>Sometimes ignores braces</td>
 *     <td>Knows special characters</td>
 *     <td>ASCII only</td>
 *   </tr>
 *   <tr>
 *     <td><tt>write$</tt></td>
 *     <td><tt>Write</tt></td>
 *     <td></td>
 *     <td></td>
 *     <td></td>
 *     </tr>
 *   </table>
 *
 */

package org.extex.exbib.core.bst.code.impl;

