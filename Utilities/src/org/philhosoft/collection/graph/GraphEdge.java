package org.philhosoft.collection.graph;

/**
 * An edge (or arc or link) between two nodes in a graph.<br>
 * It goes from a source node to a target node (order isn't important if graph is undirected).<br>
 * It has an id (managed by the Graph class for uniqueness, etc.), and can have some optional data of the given type ED.
 *
 * @param <ED> the type of the edge data. Often a String (name), an Integer or Float (weight).
 * @param <ND> the type of the node data.
 *
 * @author Philippe Lhoste
 */
public class GraphEdge<ED, ND>
{
	private final String id;
	private ED data;
	private final GraphNode<ND> sourceNode;
	private final GraphNode<ND> targetNode;

	public GraphEdge(GraphNode<ND> sourceNode, GraphNode<ND> targetNode)
	{
		this(null, sourceNode, targetNode);
	}
	public GraphEdge(String id, GraphNode<ND> sourceNode, GraphNode<ND> targetNode)
	{
		this.id = id;
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
	}

	/** Gets the id of this edge. Can be null. */
	public String getId() { return id; }
	/** Gets the data of this edge. Can be null. */
	public ED getData() { return data; }
	/** Gets the source node. */
	public GraphNode<ND> getSourceNode() { return sourceNode; }
	/** Gets the target node. */
	public GraphNode<ND> getTargetNode() { return targetNode; }

	/** Sets the optional data of this edge. */
	public void setData(ED data) { this.data = data; }

	@Override
	public String toString()
	{
		return "DefaultGraphEdge{" +
				(id == null ? "" : "id: " + id + ", ") +
				(data == null ? "" : "data: " + data + ", ") +
				"from: " + sourceNode +
				", to: " + targetNode +
				"}";
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (data == null ? 0 : data.hashCode());
		result = prime * result + (sourceNode == null ? 0 : sourceNode.hashCode());
		result = prime * result + (targetNode == null ? 0 : targetNode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof GraphEdge)) return false;
		@SuppressWarnings("unchecked")
		GraphEdge<ED, ND> other = (GraphEdge<ED, ND>) obj;
		if (data == null)
		{
			if (other.data != null) return false;
		}
		else if (!data.equals(other.data)) return false;
		if (sourceNode == null)
		{
			if (other.sourceNode != null) return false;
		}
		else if (!sourceNode.equals(other.sourceNode)) return false;
		if (targetNode == null)
		{
			if (other.targetNode != null) return false;
		}
		else if (!targetNode.equals(other.targetNode)) return false;
		return true;
	}

}
