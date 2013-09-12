package dk.kingu.shootingscoredisplay.sius;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import dk.kingu.shootingscoredisplay.datasource.DataSourceInterface;
import dk.kingu.shootingscoredisplay.event.ScoreEvent;
import dk.kingu.shootingscoredisplay.event.ScoreEventHandler;

public class SiusDataSource extends Thread implements DataSourceInterface {

	private Socket connection;
	private final String host;
	private final int port;
	private List<ScoreEventHandler> eventHandlers;
	
	public SiusDataSource(String host, String port) {
		this.host = host;
		this.port = Integer.parseInt(port);
		eventHandlers = new ArrayList<ScoreEventHandler>();
	}

	public void connect() throws UnknownHostException, IOException {
		if(connection == null) {
			connection = new Socket(host, port);
		} else {
			if(!connection.isConnected()) {
				connection = new Socket(host, port);
			}
		}
		connection.setKeepAlive(true);
		
	}
	
	public void close() {
		interrupt();
	}

	private void disconnect() throws IOException {
		if(connection != null) {
			connection.close();
		}
	}

	public void registerEventHandler(ScoreEventHandler handler) {
		if(!eventHandlers.contains(handler)) {
			eventHandlers.add(handler);
		}
	}

	public void run() {
		String message;
		BufferedReader reader;
		ScoreEvent event;
		try {
			connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while(!interrupted()) {
				if(connection.isConnected()) {
					message = reader.readLine();
					event = SiusMessageParser.parseMessage(message);
					for(ScoreEventHandler handler : eventHandlers) {
						handler.handleEvent(event);
					}
				} else {
					// TODO handle reconnect...
				}
				
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	
}
