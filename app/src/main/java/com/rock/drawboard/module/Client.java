package com.rock.drawboard.module;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	public Client(Socket socket) {
		setSocket(socket);
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	private void setSocket(Socket socket) {
		this.socket = socket;
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public boolean isClosed() {
		return socket.isClosed();
	}
	
	public void close() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Object receive() throws ClassNotFoundException, IOException {
		return input.readObject();
	}
	
	public void send(Object obj) throws IOException {
		output.writeObject(obj);
		output.flush();
	}
}
