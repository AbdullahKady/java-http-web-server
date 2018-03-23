package util;

public class HTTP_Parser {
	// Returns a string array:
	// 1st element: the file name (URL)
	// 2nd element: the file format requested
	// 3rd element: connection (close or keep-alive)
	public static String[] parseRequest(String http_request) {
		String[] lines = http_request.split("\n");
		String file = lines[0].substring(4, lines[0].length() - 9);
		String format = lines[2].substring(16);
		String connection = lines[3].substring(11);
		return new String[] { file, format, connection };
	}

	// Returns a string array:
	// 1st element: status
	// 2nd element: timestamp
	// 3rd element: file format
	// 4th element: connection (keep-alive/close)
	// 5th element: content length of the body sent
	public static String[] parseResponse(String http_response) {
		String[] lines = http_response.split("\n");
		String status = lines[0].substring(7, 10);
		String stamp = lines[1].substring(10);
		String format = lines[2].substring(7);
		String connection = lines[3].substring(11);
		String length = lines[4].substring(15);

		return new String[] { status, stamp, format, connection, length };
	}
}
