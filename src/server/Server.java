package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	public static ConcurrentHashMap<Integer, ServerThread> clientsList = new ConcurrentHashMap<Integer, ServerThread>();

	public static void main(String[] args) throws IOException {

		@SuppressWarnings("resource")
		ServerSocket welcomeSocket = new ServerSocket(4444);
		Dispatcher dispatcher = new Dispatcher();
		dispatcher.start();
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			Integer currID = Server.clientsList.size() + 1;
			ServerThread serverThread = new ServerThread(connectionSocket, currID);
			System.out.println("--Internal-- New client connected, ID-> " + currID);
			serverThread.start();
		}

	}
}