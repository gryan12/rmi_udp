/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	public RMIServer() throws RemoteException {}
	public void receiveMessage(MessageInfo msg) throws RemoteException {
//		if (msg.totalMessages <= 0) {
//			System.out.println("Receiving 0 messages");
//			throw new RemoteException(); // maybe return rather than throwing an error?
//		}

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (receivedMessages == null || msg.messageNum == 0) {
			receivedMessages = new int[msg.totalMessages];
			totalMessages = msg.totalMessages; 
		}
		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages

		if (msg.messageNum == totalMessages-1) {
			ArrayList<Integer> missingIndeces = new ArrayList<>();
			for (int i= 0; i < receivedMessages.length; i++) {
				if (receivedMessages[i] != 1) {
				    missingIndeces.add(i);
				}
			}

			System.out.println("The (putatitvely) intended number of messages sent was: " + totalMessages); 
			if (missingIndeces.isEmpty()) {
				System.out.println("All expected messages received");
			} else {
			    String output = "Messages numbers: ";
			    for (Integer index: missingIndeces) {
			    	output += index.toString() + ", ";
				}
			    output += " were expected but not receieved.";
				System.out.println(output);
				System.out.println("The total number of messages received was: " + (totalMessages - missingIndeces.size())); 
			}

		}
	//	receivedMessages = null;
	//	totalMessages = -1;
	}


	public static void main(String[] args) {
		RMIServer rmis = null;
		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		// TO-DO: Instantiate the server class
		try {
			rmis = new RMIServer();
			System.out.println("Server (should be) insantatied"); 
			rebindServer("RMIServer",rmis); 
			//LocateRegistry.createRegistry(6969); 
			//Naming.rebind("RMIServer", new RMIServer()); 

		} catch (RemoteException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		// TO-DO: Bind to RMI registry. only perform if successful instantiation
		//rebindServer("RMIServer", rmis);
	}

	protected static void rebindServer(String serverURL, RMIServer server) {
		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind(serverURL, server);
			System.out.println("Rebound registry, should be rdy to go"); 
		} catch (RemoteException e){
			System.out.println("Exception: " + e);
		}


	}
}
