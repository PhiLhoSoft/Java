import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GetGnatsData
{
	private static final String GET_REQUEST_PART1 =
			"cmd=view%20audit-trail&database=mycom-gnats-db&pr="; // Add PR number here
	private static final String AUTH = // Here: plhoste:philippe
			/* Authorization: */ "Basic cGxob3N0ZTpwaGlsaXBwZQ==";

	// Not used...
	private static final String GET_REQUEST_PART2 =
			"&return_url=http%3A%2F%2Fwww.mycom-int.fr%2Fcgi-bin%2Fdevdoc%2Fgnatsweb.pl%3Fdebug%3D%3Bdatabase%3Dmycom-gnats-db%3Btext%3D%3Bmultitext%3D%3BArrival-Date_after%3D%3BArrival-Date_before%3D%3BClosed-Date_after%3D%3BClosed-Date_before%3D%3BLast-Modified_after%3D%3BLast-Modified_before%3D%3BNumber%3D%3BCategory%3D%3BSynopsis%3D%3BSeverity%3D%3BResponsible%3D%3BResponsible%3Dplhoste%3BState%3D%3Bignoreclosed%3DIgnore%2520Closed%3BKeywords%3D%3BClass%3D%3Bignorechangerequest%3DIgnore%2520Change-Request%3BSubmitter-Id%3D%3BProduct%3D%3BInterface%3D%3BRelease%3D%3BCustomer-Pack%3D%3BCustomer-Pack-Release%3D%3BArrival-Date%3D%3BClosed-Date%3D%3BLast-Modified%3D%3BOriginator%3D%3BDescription%3D%3BFix%3D%3BRelease-Note%3D%3BAudit-Trail%3D%3BUnformatted%3D%3Bcolumns%3DCategory%3Bcolumns%3DSynopsis%3Bcolumns%3DSeverity%3Bcolumns%3DResponsible%3Bcolumns%3DState%3Bcolumns%3DRelease%3Bsortby%3DNumber%3Bdisplaydate%3DCurrent%2520Date%3Bcmd%3Dsubmit%2520query HTTP/1.1";
	private static final String COOKIE =
			/* Cookie: */ "gnatsweb-query-MyPrOptima=database%3Dmycom-gnats-db%3BArrival-Arrival-Closed-Closed-Last-Last-Category%3Dnims-myproptima%3Bignoreclosed%3DIgnore%2520Closed%3BL3-Submitter-Customer-Customer-Pack-Fixed-Fixed-Arrival-Closed-Last-Dev-Release-Audit-columns%3DCategory%3Bcolumns%3DSynopsis%3Bcolumns%3DSeverity%3Bcolumns%3DState%3Bcolumns%3DClass%3Bcolumns%3DSubmitter-Id%3Bcolumns%3DProduct%3Bcolumns%3DRelease%3Bcolumns%3DArrival-Date%3Bcolumns%3DOriginator%3Bsortby%3DNumber%3Bdisplaydate%3DCurrent%2520Date%3Bqueryname%3DMyPrOptima%3Breturn_url%3Dhttp%253A%252F%252Fwww.mycom-int.fr%252Fcgi-bin%252Fdevdoc%252Fgnatsweb.pl%253Fdebug%253D%253Bdatabase%253Dmycom-gnats-db%253Btext%253D%253Bmultitext%253D%253BArrival-Date_after%253D%253BArrival-Date_before%253D%253BClosed-Date_after%253D%253BClosed-Date_before%253D%253BLast-Modified_after%253D%253BLast-Modified_before%253D%253BNumber%253D%253BCategory%253D%253BCategory%253Dnims-myproptima%253BSynopsis%253D%253BSeverity%253D%253BResponsible%253D%253BState%253D%253Bignoreclosed%253DIgnore%252520Closed%253BKeywords%253D%253BClass%253D%253BL3-Comment%253D%253BSubmitter-Id%253D%253BProduct%253D%253BRelease%253D%253BCustomer-Pack%253D%253BCustomer-Pack-Release%253D%253BFixed-Product%253D%253BFixed-Release%253D%253BArrival-Date%253D%253BClosed-Date%253D%253BLast-Modified%253D%253BOriginator%253D%253BDescription%253D%253BFix%253D%253BDev-Comment%253D%253BRelease-Note%253D%253BAudit-Trail%253D%253BUnformatted%253D%253Bcolumns%253DCategory%253Bcolumns%253DSynopsis%253Bcolumns%253DSeverity%253Bcolumns%253DState%253Bcolumns%253DClass%253Bcolumns%253DSubmitter-Id%253Bcolumns%253DProduct%253Bcolumns%253DRelease%253Bcolumns%253DArrival-Date%253Bcolumns%253DOriginator%253Bsortby%253DNumber%253Bdisplaydate%253DCurrent%252520Date%253Bcmd%253Dsubmit%252520query; gnatsweb-query-PR03-PL=database%3Dmycom-gnats-db%3BCategory%3Dany%3BSeverity%3Dany%3BResponsible%3Dplhoste%3BState%3Dany%3Bignoreclosed%3DIgnore%2520Closed%3BClass%3Dany%3BSubmitter-Id%3Dany%3BProduct%3DNIMS-PROPTIMA_03.00%3BInterface%3Dany%3BCustomer-Pack%3Dany%3Bcolumns%3DCategory%3Bcolumns%3DSynopsis%3Bcolumns%3DSeverity%3Bcolumns%3DResponsible%3Bcolumns%3DState%3Bcolumns%3DKeywords%3Bcolumns%3DProduct%3Bcolumns%3DCustomer-Pack%3Bcolumns%3DLast-Modified%3Bsortby%3DState%3Bqueryname%3DPR03-PL%3Breturn_url%3Dhttp%253A%252F%252Fwww.mycom-int.fr%252Fcgi-bin%252Fdevdoc%252Fgnatsweb.pl%253Fdatabase%253Dmycom-gnats-db%253Bcmd%253Dsubmit%252520query%253BCategory%253Dany%253BSeverity%253Dany%253BResponsible%253Dplhoste%253BState%253Dany%253Bignoreclosed%253DIgnore%252520Closed%253BClass%253Dany%253BSubmitter-Id%253Dany%253BProduct%253DNIMS-PROPTIMA_03.00%253BInterface%253Dany%253BCustomer-Pack%253Dany%253Bcolumns%253DCategory%253Bcolumns%253DSynopsis%253Bcolumns%253DSeverity%253Bcolumns%253DResponsible%253Bcolumns%253DState%253Bcolumns%253DKeywords%253Bcolumns%253DProduct%253Bcolumns%253DCustomer-Pack%253Bcolumns%253DLast-Modified%253Bsortby%253DState; gnatsweb-query-OpenPR-PL=database%3Dmycom-gnats-db%3BArrival-Arrival-Closed-Closed-Last-Last-Responsible%3Dplhoste%3BState%3Dopen%3BState%3Danalyzed%3Bignoreclosed%3DIgnore%2520Closed%3Bignorechangerequest%3DIgnore%2520Change-Request%3BSubmitter-Customer-Customer-Pack-Arrival-Closed-Last-Dev-Release-Audit-columns%3DCategory%3Bcolumns%3DSynopsis%3Bcolumns%3DSeverity%3Bcolumns%3DResponsible%3Bcolumns%3DState%3Bcolumns%3DKeywords%3Bcolumns%3DClass%3Bcolumns%3DSubmitter-Id%3Bcolumns%3DProduct%3Bcolumns%3DInterface%3Bcolumns%3DRelease%3Bcolumns%3DCustomer-Pack%3Bcolumns%3DCustomer-Pack-Release%3Bcolumns%3DArrival-Date%3Bcolumns%3DClosed-Date%3Bcolumns%3DLast-Modified%3Bcolumns%3DOriginator%3Bsortby%3DNumber%3Bdisplaydate%3DCurrent%2520Date%3Bqueryname%3DOpenPR-PL%3Breturn_url%3Dhttp%253A%252F%252Fwww.mycom-int.fr%252Fcgi-bin%252Fdevdoc%252Fgnatsweb.pl%253Fdebug%253D%253Bdatabase%253Dmycom-gnats-db%253Btext%253D%253Bmultitext%253D%253BArrival-Date_after%253D%253BArrival-Date_before%253D%253BClosed-Date_after%253D%253BClosed-Date_before%253D%253BLast-Modified_after%253D%253BLast-Modified_before%253D%253BNumber%253D%253BCategory%253D%253BSynopsis%253D%253BSeverity%253D%253BResponsible%253D%253BResponsible%253Dplhoste%253BState%253D%253BState%253Dopen%253BState%253Danalyzed%253Bignoreclosed%253DIgnore%252520Closed%253BKeywords%253D%253BClass%253D%253Bignorechangerequest%253DIgnore%252520Change-Request%253BSubmitter-Id%253D%253BProduct%253D%253BInterface%253D%253BRelease%253D%253BCustomer-Pack%253D%253BCustomer-Pack-Release%253D%253BArrival-Date%253D%253BClosed-Date%253D%253BLast-Modified%253D%253BOriginator%253D%253BDescription%253D%253BFix%253D%253BDev-Comment%253D%253BRelease-Note%253D%253BAudit-Trail%253D%253BUnformatted%253D%253Bcolumns%253DCategory%253Bcolumns%253DSynopsis%253Bcolumns%253DSeverity%253Bcolumns%253DResponsible%253Bcolumns%253DState%253Bcolumns%253DKeywords%253Bcolumns%253DClass%253Bcolumns%253DSubmitter-Id%253Bcolumns%253DProduct%253Bcolumns%253DInterface%253Bcolumns%253DRelease%253Bcolumns%253DCustomer-Pack%253Bcolumns%253DCustomer-Pack-Release%253Bcolumns%253DArrival-Date%253Bcolumns%253DClosed-Date%253Bcolumns%253DLast-Modified%253Bcolumns%253DOriginator%253Bsortby%253DNumber%253Bdisplaydate%253DCurrent%252520Date%253Bcmd%253Dsubmit%252520query; gnatsweb-global=email&Philippe.Lhoste%40Mycom-int.com&database&mycom-gnats-db&Submitter-Id&development&columns&Category%20Synopsis%20Severity%20Responsible%20State%20Release; PHPSESSID=d96e2ae6c43fa2c0b43555df9d613472";

	/** URL path to the server. */
	private static final String BASE_URL = "http://www.mycom-int.fr/cgi-bin/devdoc";
	/** Server script name. */
	private static final String SCRIPT_NAME = "gnatsweb.pl";
	/** Made of the URL and the server script name. Can add parameters too. */
	private String requestURL;

	/** The connection to the server. */
	private HttpURLConnection connection;
	/** The output stream to write the binary data. */
	private DataOutputStream output;

	/** The regular expression to get the synopsis data. */
	private static final String SYNOPSIS =
			"<tr><td nowrap><b>Synopsis:</b></td>[\r\n]*" +
			"<td><tt>([^<]*)</tt></td></tr>";

	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Usage: GetGnatsData <PR id>");
			return;
		}
		GetGnatsData ggd = new GetGnatsData(args[0]);
		boolean bOK = false;
		// Send the request
		bOK = ggd.SendRequest();
		if (!bOK)
		{
			// Report problem
		}

		// Get the answer of the server
		int rc = ggd.GetResponseCode();
		String feedback = ggd.GetServerFeedback();
		System.out.println("----- Answer: " + rc + " -----\n" + feedback.length() + " bytes");

		// Extract the information from the server feedback
		Pattern p = Pattern.compile(SYNOPSIS);
		Matcher m = p.matcher(feedback);
		if (m.find())
		{
//			int count = m.groupCount() + 1;
			String synopsis = m.group(1);
			System.out.println("\n=> " + synopsis + "\n");
		}
	}

	public GetGnatsData(String prID)
	{
		// We can add optional parameters, eg. a string given by the user, parameters used, etc.
		requestURL = BASE_URL + "/" + SCRIPT_NAME + "?" + GET_REQUEST_PART1 + prID;
	}

	/** Send the GET request. */
	public boolean SendRequest()
	{
		try
		{
			URL url = new URL(requestURL); // throws MalformedURLException
			connection = (HttpURLConnection) url.openConnection();  // throws IOException
			// connection is probably of HttpURLConnection  type now

			connection.setDoOutput(true); // We output stuff (?)
			connection.setRequestMethod("GET");  // With GET method
			connection.setDoInput(true);  // We want feedback!
			connection.setUseCaches(false); // No cache

			// Set request headers
			connection.setRequestProperty("Authorization", AUTH);
			// throws IllegalStateException, NullPointerException
		}
		catch (Exception e) // Indistinctly catch all kinds of exceptions this code can throw at us
		{
			// Can display some feedback to user there, or just ignore the issue
			e.printStackTrace();
			return false; // Problem
		}

		return true;
	}

	/** Get the HTTP code returned by the server. */
	public int GetResponseCode()
	{
		int responseCode = -1;
		if (connection == null)
		{
			// ERROR: Can't get server response without first sending request!
			return -1;
		}
		// Note that 200 means OK
		try
		{
			responseCode = connection.getResponseCode();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		return responseCode;
	}

	/** Reads output from server. */
	public String GetServerFeedback()
	{
		if (connection == null)
		{
			// ERROR: Can't get server feedback without first sending request!
			return null;
		}
		BufferedReader input = null;
		StringBuilder answer = new StringBuilder();
		try
		{
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String answerLine = null;
			do
			{
				answerLine = input.readLine();
				if (answerLine != null)
				{
					answer.append(answerLine + "\n");
				}
			} while (answerLine != null);
		}
		catch (Exception e)
		{
			// Can display some feedback to user there, or just ignore the issue
			e.printStackTrace();
			return null;  // Problem
		}
		finally
		{
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		}

		return answer.toString();
	}
}
