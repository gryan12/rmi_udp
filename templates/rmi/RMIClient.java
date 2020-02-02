/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;
		System.out.println("RMIClient init"); 

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		boolean safe_to_deref_msg = false; 
		try {
			System.out.println("Attempting to init security manager"); 
			// TO-DO: Initialise Security Manager
			if (System.getSecurityManager() == null) {
				System.out.println("No security manager found, creating..."); 
				System.setSecurityManager(new SecurityManager());
			} else {
				System.out.println("Security manager already present"); 
			}
			// TO-DO: Bind to RMIServer
			System.out.println("Attempting to bind to RMIServer"); 
			iRMIServer = (RMIServerI)Naming.lookup(urlServer);
			safe_to_deref_msg = true; 

		} catch (RemoteException | MalformedURLException | NotBoundException e ) {
			System.out.println("Exception: " + e);
		}

		// TO-DO: Attempt to send messages the specified number of times

	if (safe_to_deref_msg) {

		for (int i = 0; i < numMessages; i++) {
		    try {
					MessageInfo message = new MessageInfo(numMessages, i);
					iRMIServer.receiveMessage(message);
				} catch (RemoteException e) {
					System.out.println("Exception:" + e + " at message number: " + i);
				}
		}

	}

		System.exit(0);
	}
}

