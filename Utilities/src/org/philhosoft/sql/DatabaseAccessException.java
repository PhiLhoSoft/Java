/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 *
 * Copyright notice: See the PhiLhoSoftLicence.txt file for details.
 * This file is distributed under the zlib/libpng license.
 * Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
 */
/* File history:
 *  1.00.000 -- 2005/03/22 (PL) -- Creation
 */
package org.philhosoft.sql;

import java.sql.SQLException;

/**
 * Generic exception for Connexion's Database access.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/03/22
 */
public class DatabaseAccessException extends SQLException
{
	private static final long serialVersionUID = 1L;

	DatabaseAccess m_dba;
	String m_message;


	/**
	 *
	 */
	public DatabaseAccessException(DatabaseAccess dba, String message, SQLException ex)
	{
		m_dba = dba;
		StringBuffer errorMsg = new StringBuffer(message + "\n");
		if (ex != null)
		{
			do
			{
				errorMsg.append(ex.getSQLState() + " (" + ex.getErrorCode() + "): " + ex.getMessage() + "\n");
			} while (ex.getNextException() != null);
		}

		m_message = errorMsg.toString();
	}
	public String toString()
	{
		return "### [" + m_dba + "]: Exception! " + m_message;
	}
}
