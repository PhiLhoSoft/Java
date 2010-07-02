import java.util.*;
import java.io.*;
import javax.xml.stream.*;

import java.lang.reflect.InvocationHandler;

// Based on StAX pretty printer <http://www.ewernli.com/web/guest/47> article
// http://www.java2s.com/Open-Source/Java-Document/XML/stax-utils/javanet/staxutils/IndentingXMLEventWriter.java.htm
//

class PrettyPrintXMLHandler extends InvocationHandler
{
//~ 	private static Logger LOGGER = Logger.getLogger(PrettyPrintHandler.class.getName());

	private XMLStreamWriter m_target;

	private String m_indentBase = " ";
	private int m_depth = 0;
	private Map<Integer, Boolean> m_hasChildElement = new HashMap<Integer, Boolean>();

	private static final String LINEFEED_CHAR = "\n";

	public PrettyPrintHandler(XMLStreamWriter target)
	{
		m_target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		String m = method.getName();

//~ 		if (LOGGER.isDebugEnabled())
//~ 		{
//~ 			LOGGER.debug( "XML event: "+m );
//~ 		}

		// Needs to be BEFORE the actual event, so that for instance the
		// sequence writeStartElem, writeAttr, writeStartElem, writeEndElem, writeEndElem
		// is correctly handled

		if (m.equals("writeStartElement"))
		{
			// update state of parent node
			if (m_depth > 0)
			{
				hasChildElement.put(m_depth-1, true);
			}

			// reset state of current node
			hasChildElement.put(m_depth, false);

			// indent for current depth
			m_target.writeCharacters(LINEFEED_CHAR);
			m_target.writeCharacters( repeat( m_depth, INDENT_CHAR ));

			m_depth++;
		}

		if (m.equals("writeEmptyElement"))
		{
			// update state of parent node
			if (m_depth > 0)
			{
				hasChildElement.put(m_depth-1, true);
			}

			// indent for current depth
			target.writeCharacters(LINEFEED_CHAR);
			target.writeCharacters( repeat( m_depth, INDENT_CHAR ));
		}

		if (m.equals("writeEndElement"))
		{
			m_depth--;

			if (hasChildElement.get(m_depth))
			{
				target.writeCharacters(LINEFEED_CHAR);
				target.writeCharacters( repeat( m_depth, INDENT_CHAR ));
			}
		}

		// Transmit the call
		method.invoke(target, args);

		return null;
	}

	private String repeat( int d, String s )
	{
		String _s = "";
		while( d-- > 0 )
		{
			_s += s;
		}
		return _s;
	}
}
