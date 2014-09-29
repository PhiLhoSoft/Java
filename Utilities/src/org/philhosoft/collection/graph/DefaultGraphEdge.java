package org.philhosoft.collection.graph;

/**
 * Default implementation of a graph edge.
 *
 * @author Philippe Lhoste
 */
public class DefaultGraphEdge<ED, VD> implements GraphEdge<ED, VD>
{
	private String id;
	private ED data;
	private GraphVertex<VD> sourceVertex;
	private GraphVertex<VD> targetVertex;

	public DefaultGraphEdge()
	{
		this(null, null);
	}
	public DefaultGraphEdge(String id, ED data)
	{
		this.id = id;
		this.data = data;
	}
	public DefaultGraphEdge(String id)
	{
		this(id, null);
	}
	public DefaultGraphEdge(ED data)
	{
		this(null, data);
	}

	@Override public String getId() { return id; }
	@Override public ED getData() { return data; }
	@Override public GraphVertex<VD> getSourceVertex() { return sourceVertex; }
	@Override public GraphVertex<VD> getTargetVertex() { return targetVertex; }

	public void setId(String id) { this.id = id; }
	public void setData(ED data) { this.data = data; }
	public void setSourceVertex(GraphVertex<VD> sourceVertex) { this.sourceVertex = sourceVertex; }
	public void setTargetVertex(GraphVertex<VD> targetVertex) { this.targetVertex = targetVertex; }
}
