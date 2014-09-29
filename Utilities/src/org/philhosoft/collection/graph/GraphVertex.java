package org.philhosoft.collection.graph;

/**
 * A vertex (or node) in a graph.<br>
 * It can have an id (to be managed by the Graph class for uniqueness, etc.), and some data of the given type T.
 *
 * @author Philippe Lhoste
 */
public interface GraphVertex<VD>
{
	String getId();
	VD getData();
}
