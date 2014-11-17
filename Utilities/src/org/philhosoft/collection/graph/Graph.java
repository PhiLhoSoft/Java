package org.philhosoft.collection.graph;

import java.util.HashMap;
import java.util.Map;

import org.philhosoft.collection.IdCreator;

/**
 * A graph object, made of {@link GraphNode}s (or vertices) and {@link GraphEdge}s (or arcs or links).
 * <p>
 * Closely follow cpettitt's graphlib (JS graph library) API https://github.com/cpettitt/graphlib/wiki/API-Reference
 * Firstly because it is well done. Secondly because it is intended to be used in a GWT wrapper for this library.
 * This first version doesn't support compound graphs.
 *
 * @author Philippe Lhoste
 */
public class Graph<ED, ND>
{
	public enum Option { DIRECTED, UNDIRECTED, SIMPLE, MULTIGRAPH }; //, COMPOUND

	private String name;
	private boolean directed = true;
	private boolean multigraph;
//	private boolean compound;

	private Map<String, GraphNode<ND>> nodes = new HashMap<String, GraphNode<ND>>();
	private Map<String, GraphEdge<ED, ND>> edges = new HashMap<String, GraphEdge<ED, ND>>();

	private ND defaultNodeData;
	private ED defaultEdgeData;

	private final IdCreator idCreator = new IdCreator();

	public Graph(String name, Option... options)
	{
		this.name = name;
		for (Option o : options)
		{
			switch (o)
			{
			case DIRECTED:
				directed = true;
				break;
			case UNDIRECTED:
				directed = false;
				break;
			case SIMPLE:
				multigraph = false;
				break;
			case MULTIGRAPH:
				multigraph = true;
				break;
//			case COMPOUND:
//				compound = true;
//				break;
			default:
				throw new IllegalArgumentException("Unknown option! Source to be updated...");
			}
		}
	}
	public Graph(Option... options)
	{
		this(null, options);
	}

	/**
	 * Returns the name of the graph (null if unnamed).
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * Tells if the graph is directed or undirected.
	 * Defaults to directed.
	 */
	public boolean isDirected()
	{
		return directed;
	}
	/**
	 * Tells if the graph is multigraph or not.
	 * Ie. if there can be multiple edges between the same pair of nodes.
	 * Defaults to not multigraph.
	 */
	public boolean isMultigraph()
	{
		return multigraph;
	}
	/**
	 * Tells if the graph is compound (made of sub-graphs) or not.
	 * Defaults to not compound.
	 */
//	public boolean isCompound()
//	{
//		return compound;
//	}

	/**
	 * Sets the default node data, eg. default weight if not specified.
	 */
	public void setDefaultNodeData(ND data)
	{
		this.defaultNodeData = data;
	}
	/**
	 * Sets the default edge data, eg. default weight if not specified.
	 */
	public void setDefaultEdgeData(ED data)
	{
		this.defaultEdgeData = data;
	}

	/**
	 * Creates or updates a node with the given id and no data, or default node data, if it has been defined.
	 *
	 * @param id  unique id. If there is already a node with this id in this graph, it is updated.
	 *            If the id is null, a random id is generated.
	 * @return the updated or created node
	 */
	public GraphNode<ND> putNode(String id)
	{
		return putNode(id, defaultNodeData);
	}
	/**
	 * Creates or updates a node with the given id and the data.
	 *
	 * @param id  unique id. If there is already a node with this id in this graph, it is updated.
	 *            If the id is null, a random id is generated.
	 * @param data  data to associate with the node. If null, will erase the data of the found node, if any,
	 *              otherwise the default node data, if it has been defined, will be used.
	 * @return the updated or created node
	 */
	public GraphNode<ND> putNode(String id, ND data)
	{
		if (id == null)
		{
			do
			{
				id = idCreator.create();
			} while (nodes.get(id) != null); // Ensure no collision, probably no more than 2 loops...
		}
		else
		{
			GraphNode<ND> node = nodes.get(id);
			if (node != null)
			{
				node.setData(data);
				return node;
			}
		}
		GraphNode<ND> node = new GraphNode<ND>(id, data == null ? defaultNodeData : data);
		return node;
	}
}
