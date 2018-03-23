package util;

public class HTTP_Formatter {
	public static String createRequest(String url, String host, String keepAlive) {
		String extension = url.substring(url.lastIndexOf('.') + 1);
		return "GET " + url + " HTTP/1.1\nHost " + host + "\nAccepted format " + extension + "\nConnection "
				+ keepAlive;
	}

	public static String create404Response(String keepAlive) {
		return "Status 404 HTTP/1.1\nTimestamp " + new java.util.Date() + "\nFormat NONE\nConnection " + keepAlive
				+ "\nContent-Length 0";
	}

	// Length is the content length of the file to be sent (the data)
	public static String createResponse(String format, String keepAlive, int length) {
		return "Status 200 HTTP/1.1\nTimestamp " + new java.util.Date() + "\nFormat " + format + "\nConnection "
				+ keepAlive + "\nContent-Length " + length;
	}
}
