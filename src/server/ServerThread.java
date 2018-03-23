package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread {

	private Socket clientSocket;
	protected Integer currentID;
	public boolean terminateThread;

	public Socket getClientSocket() {
		return clientSocket;
	}

	// To be set after a response is sent to a "close connection" request
	protected void terminateThread() {
		this.terminateThread = true;
	}

	public ServerThread(Socket connectionSocket, Integer id) {
		this.clientSocket = connectionSocket;
		this.terminateThread = false;
		this.currentID = id;
		Server.clientsList.put(currentID, this);
	}

	// Reading requests, and appending them to the queue
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (true) {
				String req = "";
				String method = inFromClient.readLine();
				req = method + '\n';
				System.out.println(method);
				req += inFromClient.readLine() + '\n';
				req += inFromClient.readLine() + '\n';
				String connection = inFromClient.readLine();
				req += connection + '\n';
				// Add request to the dispatcher's Q if there is one.
				if (!req.equals(""))
					Dispatcher.addClient(new Object[] { this, req });
				// Ensures no more requests can be received if "closed"
				if (connection.substring(11).equals("close"))
					break;
			}

			// Ensures that the dispatcher will first finish then set the flag
			// to true (sorry not sorry)
			while (!terminateThread) {
				sleep(10);
			}
			
			// Thread termination (connection close) from the dispatcher
			Server.clientsList.remove(this.currentID);
			System.out.println("--Internal-- Removed ID-> " + this.currentID);
			clientSocket.close();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
