package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import util.HTTP_Formatter;
import util.HTTP_Parser;

public class Client {
	Socket clientSocket;
	String dirName;
	ClientGUI clientGUI;

	public void setGUI(ClientGUI cg) {
		this.clientGUI = cg;
	}

	public Client(int port, String dirName) throws UnknownHostException, IOException, DirectoryExistsException {
		File dir = new File("./src/client/downloads/" + dirName);
		if (dir.exists()) {
			throw new DirectoryExistsException();
		}
		dir.mkdirs();
		Socket clientSocket = new Socket("127.0.0.1", port);
		System.out.println("Connected successfully to server!");
		this.clientSocket = clientSocket;
		this.dirName = dirName;
	}

	public void sendRequest(String url, String keepAlive) throws IOException {
		DataOutputStream outToServer = new DataOutputStream(this.clientSocket.getOutputStream());
		InputStream in = clientSocket.getInputStream();
		String request = HTTP_Formatter.createRequest(url, "127.0.0.1", keepAlive);
		outToServer.writeBytes(request + '\n');
		this.clientGUI.textPane_req.setText(request);
		// Since no pipelining is to be implemented; therefore need to wait for
		// the response before sending another request
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(in));
		String response = "";
		for (int i = 0; i < 5; i++) {
			response += inFromServer.readLine() + '\n';
		}
		this.clientGUI.textPane_res
				.setText(response.substring(0, (response.substring(0, response.lastIndexOf('\n'))).lastIndexOf('\n')));
		String[] parsedRes = HTTP_Parser.parseResponse(response);
		String status = parsedRes[0];
		if (status.equals("200")) {
			int contentLength = Integer.parseInt(parsedRes[4]);
			OutputStream out = new FileOutputStream("./src/client/downloads/" + this.dirName + "/" + url);
			byte[] bytes = new byte[contentLength];
			int count;
			int total = 0;
			while ((count = in.read(bytes)) > 0) {
				out.write(bytes, 0, count);
				total += count;
				if (total >= contentLength)
					break;
			}
			out.close();
		}
		if (parsedRes[3].equals("close")) {
			this.clientGUI.terminate();
		}
	}

	public class DirectoryExistsException extends Exception {
		private static final long serialVersionUID = 1L;
	}

}
