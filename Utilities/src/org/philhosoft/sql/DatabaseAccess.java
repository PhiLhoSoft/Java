/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2005/03/18 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLWarning;

//import org.philhosoft.string.Plural;

/**
 * Database Access wrapper for generic 'execute' commands.
 *
 * Currently, this is more a template than a generic class,
 * since the line process code is hardcoded.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/03/18
 */
public class DatabaseAccess
{
	private Connection m_connection;
	private Statement m_statement;
	private ResultSet m_resultSet;
	private ResultSetMetaData m_resultSetMetaData;
	private int m_updateCount;
//	private boolean m_bAsMoreResults;

	public DatabaseAccess()
	{
	}

	public String connect(
		String driver,	// "com.mysql.jdbc.Driver"
		String url,		// "jdbc:mysql://localhost/test?user=monty&password=greatsqldb"
		String login,
		String password
	)
		throws DatabaseAccessException
	{
		try
		{
			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName(driver).newInstance();
		}
		catch (Exception ex)
		{
			throw new DatabaseAccessException(this, "Driver Connection error!", null);
		}
		try
		{
			close();
//~ 			m_connection = DriverManager.getConnection(url +
//~ 					"?user=" + login + "&password=" + password);
			m_connection = DriverManager.getConnection(url,
					login, password);
			return getWarnings(m_connection.getWarnings());
		}
		catch (SQLException ex)
		{
			throw new DatabaseAccessException(this, "Database Connection error!", ex);
		}
	}

	public void close()
			throws DatabaseAccessException
	{
		try
		{
			if (m_statement != null)
			{
				m_statement.close();
				m_statement = null;
			}
			if (m_resultSet != null)
			{
				m_resultSet.close();
				m_resultSet = null;
			}
			if (m_connection != null)
			{
				m_connection.close();
				m_connection = null;
			}
		}
		catch (SQLException ex)
		{
			throw new DatabaseAccessException(this, "Cannot close Database Connection!", ex);
		}
	}

	public String execute(String request)
			throws DatabaseAccessException
	{
		try
		{
 			m_statement = m_connection.createStatement();
			// For a SELECT
//~ 			rs = m_statement.executeQuery(request);
			// For any request
			m_statement.execute(request);
//			m_bAsMoreResults = true;
			m_updateCount = m_statement.getUpdateCount();
			return getWarnings(m_statement.getWarnings());
		}
		catch (SQLException ex)
		{
			m_updateCount = 0;
//			m_bAsMoreResults = false;
			throw new DatabaseAccessException(this, "Request [" + request + "] has failed!", ex);
		}
	}

	/**
	 * @return true if statement was an update (INSERT, UPDATE or DELETE).
	 */
	public boolean isUpdate()
	{
		return m_updateCount >= 0;
	}

	/**
	 * @return true if statement was a request (SELECT).
	 */
	public boolean isRequest()
	{
		return m_updateCount < 0;
	}

	/**
	 * @return true if there are more results to fetch.
	 */
	public boolean hasMoreResults()
			throws DatabaseAccessException
	{
		boolean bIsResultSet = false;
		m_resultSet = null;
		try
		{
			bIsResultSet = m_statement.getMoreResults();
			m_updateCount = m_statement.getUpdateCount();
		}
		catch (SQLException ex)
		{
			throw new DatabaseAccessException(this, "Failed accessing request results!", ex);
		}
		return bIsResultSet || m_updateCount >= 0;
	}

	/*----- Accessors -----*/

	/**
	 * @return connection.
	 */
	public Connection getConnection()
	{
		return m_connection;
	}

	/**
	 * @return udpdateCount (number of rows that were affected).
	 */
	public int getUpdateCount()
	{
		return m_updateCount;
	}

	/**
	 * @return resultSet.
	 */
	public ResultSet getResultSet()
			throws DatabaseAccessException
	{
		if (m_updateCount < 0)
		{
			if (m_resultSet == null)
			{
				try
				{
					// Result is a result set, or there is no more results.
					m_resultSet = m_statement.getResultSet();
				}
				catch (SQLException ex)
				{
					throw new DatabaseAccessException(this, "Failed accessing request results!", ex);
				}
//				m_bAsMoreResults = (m_resultSet != null);
			}
			return m_resultSet;	// null if no more results.
		}
		return null;
	}

	/**
	 * @return resultSetMetaData.
	 */
	public ResultSetMetaData getResultMetaData()
			throws DatabaseAccessException
	{
		if (m_resultSet == null)
		{
			getResultSet();
			if (m_resultSet == null)
			{
				return null;
			}
		}
		try
		{
			m_resultSetMetaData = m_resultSet.getMetaData();
		}
		catch (SQLException ex)
		{
			throw new DatabaseAccessException(this, "Unable to access ResultSet MetaData!", ex);
		}
		return m_resultSetMetaData;
	}

	/*----- Private Part -----*/

	private String getWarnings(SQLWarning w)
	{
		if (w != null)
		{
			StringBuffer warnMsg = new StringBuffer();
			do
			{
				warnMsg.append(w.getSQLState() + " (" + w.getErrorCode() + "): " + w.getMessage() + "\n");
			} while (w.getNextException() != null);
			return warnMsg.toString();
		}
		else
		{
			return null;
		}
	}
}
