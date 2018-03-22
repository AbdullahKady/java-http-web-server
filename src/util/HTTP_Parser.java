package util;

public class HTTP_Parser {
	// Returns a string array, first element being the file name (URL), and the
	// 2nd element is the connection (close or keep-alive)
	public static String[] parseRequest(String http_request) {
		String[] lines = http_request.split("\n");
		String file = lines[0].substring(4, lines[0].length() - 9);
		String connection = lines[3].substring(11);
		return new String[] { file, connection };
	}

	// Returns a string array:
	// 1st element: status
	// 2nd element: timestamp
	// 3rd element: file format
	// 4th element: connection (keep-alive/close)
	// 5th element: payload (file stream) in case the status was 200, else it
	// returns the 5th element as null
	public static String[] parseResponse(String http_response) {
		String[] lines = http_response.split("\n");
		String status = lines[0].substring(7,10);
		String stamp = lines[1].substring(10);
		String format = lines[2].substring(7);
		String connection = lines[3].substring(11);
		// In case no payload attached (file was not found), a null would be
		// returned for the 5th element
		String stream = null;
		if (status.equals("200"))
			stream = lines[4];
		return new String[] { status, stamp, format, connection, stream };
	}
}
