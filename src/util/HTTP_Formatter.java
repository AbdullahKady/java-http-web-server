package util;

public class HTTP_Formatter {
	public static String createRequest(String url, String host, boolean keepAlive) {
		String extension = url.substring(url.lastIndexOf('.') + 1);
		return "GET " + url + " HTTP/1.1\nHost " + host + "\nAccepted format " + extension + "\nConnection "
				+ (keepAlive ? "keep-alive" : "close");
	}

	public static String create404Response(boolean keepAlive) {
		return "Status 404 HTTP/1.1\nTimestamp " + new java.util.Date() + "\nFormat NONE\nConnection "
				+ (keepAlive ? "keep-alive" : "close");
	}

	// Payload represents the file stream to be sent
	public static String createResponse(String format, boolean keepAlive, String payload) {
		return "Status 200 HTTP/1.1\nTimestamp " + new java.util.Date() + "\nFormat " + format + "\nConnection "
				+ (keepAlive ? "keep-alive\n" : "close\n") + payload;
	}

	public static void main(String[] args) {
		String response = create404Response(false);
		String[] res = HTTP_Parser.parseResponse(response);
		for (String el : res) {
			System.out.println(el);
		}
	}
}
