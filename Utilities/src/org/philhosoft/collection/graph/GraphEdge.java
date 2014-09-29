package org.philhosoft.collection.graph;

/**
 * An edge (or arc) between two nodes in a graph.<br>
 * It can have an id (to be managed by the Graph class for uniqueness, etc.), and some data of the given type T.
 *
 * @author Philippe Lhoste
 */
public interface GraphEdge<ED, VD>
{
	String getId();
	ED getData();
	GraphVertex<VD> getSourceVertex();
	GraphVertex<VD> getTargetVertex();
}
