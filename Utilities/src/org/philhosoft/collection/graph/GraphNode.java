package org.philhosoft.collection.graph;

/**
 * A node (or vertex) in a graph.<br>
 * It has an id (managed by the Graph class for uniqueness, etc.), and can have some (optional) data of the given type ND.
 *
 * @param <ND> the type of the edge data. Often a String (name), an Integer or Float (weight).
 *
 * @author Philippe Lhoste
 */
public class GraphNode<ND>
{
	private final String id;
	private ND data;

	// Only created by Graph
	GraphNode(String id, ND data)
	{
		this.id = id;
		this.data = data;
	}

	public String getId() { return id; }
	public ND getData() { return data; }

	/** Sets the optional data of this node. */
	public void setData(ND data) { this.data = data; }

	@Override
	public String toString()
	{
		return "DefaultGraphNode{" +
				(id == null ? "" : "id: " + id + ", ") +
				(data == null ? "" : "data: " + data + ", ") +
				"}";
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (data == null ? 0 : data.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof GraphNode)) return false;
		@SuppressWarnings("unchecked")
		GraphNode<ND> other = (GraphNode<ND>) obj;
		if (data == null)
		{
			if (other.data != null) return false;
		}
		else if (!data.equals(other.data)) return false;
		return true;
	}
}
