package server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import util.HTTP_Formatter;
import util.HTTP_Parser;

public class Dispatcher extends Thread {
	public static ConcurrentLinkedQueue<Object[]> Q = new ConcurrentLinkedQueue<>();

	public static void addClient(Object[] pair) {
		Q.add(pair);
	}

	@Override
	public void run() {
		while (true) {
			Object[] pair = Q.poll();
			if (pair != null) {
				try {
					ServerThread currentThread = ((ServerThread) pair[0]);
					DataOutputStream outToClient = new DataOutputStream(
							currentThread.getClientSocket().getOutputStream());
					String[] requestParams = HTTP_Parser.parseRequest((String) pair[1]);
					String URL = requestParams[0];
					String format = requestParams[1];
					String connectionState = requestParams[2];
					// Search for the requested URL
					File file = new File("./src/server/docroot/" + URL);
					if (file.exists() && !file.isDirectory()) {
						int fileLength = (int) file.length(); // Max of ~2GB
						outToClient
								.writeBytes(HTTP_Formatter.createResponse(format, connectionState, fileLength) + "\n");
						sendFile(file, fileLength, currentThread.getClientSocket());
					} else {
						outToClient.writeBytes(HTTP_Formatter.create404Response(connectionState) + '\n');
					}
					currentThread.getClientSocket().getOutputStream().flush();
					// Generate a response based on the request, send it
					// if connection close, terminate thread
					if (connectionState.equals("close")) {
						currentThread.terminateThread();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void sendFile(File file, int fileLength, Socket clientSocket) {
		try {
			InputStream in = new FileInputStream(file);
			byte[] bytes = new byte[fileLength];
			OutputStream out = clientSocket.getOutputStream();
			int count;
			while ((count = in.read(bytes)) > 0) {
				out.write(bytes, 0, count);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
