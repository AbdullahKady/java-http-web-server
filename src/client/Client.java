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
import java.util.Scanner;

import util.HTTP_Formatter;
import util.HTTP_Parser;

public class Client {
	Socket clientSocket;
	String dirName;

	public Client(int port, String dirName) throws UnknownHostException, IOException {

		Socket clientSocket = new Socket("127.0.0.1", port);
		System.out.println("Connected successfully to server!");
		this.clientSocket = clientSocket;
		this.dirName = dirName;

	}

	private void sendRequest(String url, String keepAlive) throws IOException {
		DataOutputStream outToServer = new DataOutputStream(this.clientSocket.getOutputStream());
		InputStream in = clientSocket.getInputStream();
		outToServer.writeBytes(HTTP_Formatter.createRequest(url, "whateverthe.com", keepAlive) + '\n');
		// Since no pipelining is to be implemented; therefore need to wait for
		// the response before sending another request
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(in));
		System.out.println("REQUEST SENT FOR: " + url);
		String request = "";
		for (int i = 0; i < 5; i++) {
			request += inFromServer.readLine() + '\n';
		}
		String[] parsedReq = HTTP_Parser.parseResponse(request);
		String status = parsedReq[0];
		System.out.println("RESPONSE RECEIVED: " + status);
		if (status.equals("200")) {
			int contentLength = Integer.parseInt(parsedReq[4]);
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
			if (parsedReq[3].equals("close"))
				out.close();
		} else {
			System.out.println("File was not found");
		}
		System.out.println("DONE WITH THE REQUEST");
	}

	public static void main(String[] args) throws Exception {
		System.out.println(
				"Welcome, please type in a name to create a directory inside the downloads folder to receive your files in");
		Scanner sc = new Scanner(System.in);
		String tempDir;
		while (true) {
			tempDir = sc.nextLine();
			File dir = new File("./src/client/downloads/" + tempDir);
			if (!dir.exists()) {
				dir.mkdirs();
				System.out.println("Folder created successfully, you can now find your downloads in the '" + tempDir
						+ "' Directory");
				break;
			}
			System.out.println("Folder already exists, please enter a new a name.");
		}
		Client c = new Client(4444, tempDir);
		String line;
		String[] input;
		while (true) {
			line = sc.nextLine();
			input = line.split(",");
			c.sendRequest(input[0], input[1]);
			if (input[1].equals("close"))
				break;
		}
		sc.close();
	}

}
