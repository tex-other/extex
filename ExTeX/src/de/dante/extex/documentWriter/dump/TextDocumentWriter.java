/*
 * Copyright (C) 2004 Michael Niedermair
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
package de.dante.extex.documentWriter.dump;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;

/**
 * This is a text dummy implementation of a document writer (very simple).
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class TextDocumentWriter implements DocumentWriter, NodeVisitor {

    /**
     * The field <tt>out</tt> ...
     */
    private OutputStream out = null;

    /**
     * The field <tt>shippedPages</tt> ...
     */
    private int shippedPages = 0;

    /**
     * Creates a new object.
     */
    public TextDocumentWriter() {
        super();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {
        return shippedPages;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {
        return "out";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setOutputStream(java.io.Writer)
     */
    public void setOutputStream(final OutputStream outStream) {
        out = outStream;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(de.dante.extex.typesetter.NodeList)
     */
    public void shipout(final NodeList nodes) throws IOException {
    	processNodes(nodes);
        out.write('\n');
        out.flush();
        shippedPages++;
    }

    private void processNodes(final NodeList nodes) throws IOException {
    	NodeIterator it = nodes.iterator(); 
    	showNode(nodes);  
    	while(it.hasNext()) 
    	{
    		Node n = it.next();
    		if (n instanceof NodeList) processNodes((NodeList)n);
    		else showNode(n);
    	}
    }
    
    private void showNode(Node node) throws IOException { 
    	try {
    		Object o = node.visit(this, node, null);
    		if (o != null) {
    			if (o instanceof String) {
    				out.write(((String)o).getBytes());
    			}
    		}
    	}
    	catch (GeneralException  e) {
    		e.printStackTrace();
    	} 
    }
    
    
    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() {
        // nothing to do
    }
    
	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitAdjust(java.lang.Object, java.lang.Object)
	 */
	public Object visitAdjust(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitAfterMath(java.lang.Object, java.lang.Object)
	 */
	public Object visitAfterMath(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitAlignedLeaders(java.lang.Object, java.lang.Object)
	 */
	public Object visitAlignedLeaders(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitBeforeMath(java.lang.Object, java.lang.Object)
	 */
	public Object visitBeforeMath(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitCenteredLeaders(java.lang.Object, java.lang.Object)
	 */
	public Object visitCenteredLeaders(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitChar(java.lang.Object, java.lang.Object)
	 */
	public Object visitChar(Object value, Object Value2) {
		CharNode node = (CharNode)value;
		return node.toString();
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitDiscretionary(java.lang.Object, java.lang.Object)
	 */
	public Object visitDiscretionary(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitExpandedLeaders(java.lang.Object, java.lang.Object)
	 */
	public Object visitExpandedLeaders(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitGlue(java.lang.Object, java.lang.Object)
	 */
	public Object visitGlue(Object value, Object Value2) {
		return " ";
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitHorizontalList(java.lang.Object, java.lang.Object)
	 */
	public Object visitHorizontalList(Object value, Object Value2) {
		return "\n";
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitInsertion(java.lang.Object, java.lang.Object)
	 */
	public Object visitInsertion(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitKern(java.lang.Object, java.lang.Object)
	 */
	public Object visitKern(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitLigature(java.lang.Object, java.lang.Object)
	 */
	public Object visitLigature(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitMark(java.lang.Object, java.lang.Object)
	 */
	public Object visitMark(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitPenalty(java.lang.Object, java.lang.Object)
	 */
	public Object visitPenalty(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitRule(java.lang.Object, java.lang.Object)
	 */
	public Object visitRule(Object value, Object Value2) {
		return "\n---------------------------------------------------------------------";
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitSpace(java.lang.Object, java.lang.Object)
	 */
	public Object visitSpace(Object value, Object Value2) {
		return " ";
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitVerticalList(java.lang.Object, java.lang.Object)
	 */
	public Object visitVerticalList(Object value, Object Value2) {
		return null;
	}

	/**
	 * @see de.dante.extex.typesetter.NodeVisitor#visitWhatsIt(java.lang.Object, java.lang.Object)
	 */
	public Object visitWhatsIt(Object value, Object Value2) {
		return null;
	}
}
