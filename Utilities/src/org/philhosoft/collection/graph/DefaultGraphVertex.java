package org.philhosoft.collection.graph;

/**
 * Default implementation of a graph vertex.
 *
 * @author Philippe Lhoste
 */
public class DefaultGraphVertex<T> implements GraphVertex<T>
{
	private String id;
	private T data;

	public DefaultGraphVertex()
	{
		this(null, null);
	}
	public DefaultGraphVertex(String id, T data)
	{
		this.id = id;
		this.data = data;
	}
	public DefaultGraphVertex(String id)
	{
		this(id, null);
	}
	public DefaultGraphVertex(T data)
	{
		this(null, data);
	}

	@Override public String getId() { return id; }
	@Override public T getData() { return data; }

	public void setId(String id) { this.id = id; }
	public void setData(T data) { this.data = data; }
}
