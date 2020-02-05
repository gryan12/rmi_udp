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

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		} 

		try {
			// TO-DO: Bind to RMIServer
			iRMIServer = (RMIServerI)Naming.lookup(urlServer);
			System.out.println("server should be ready to receieve"); 

		} catch (RemoteException | MalformedURLException | NotBoundException e ) {
			System.out.println("Exception: " + e);
			e.printStackTrace(); 
		}
		// TO-DO: Attempt to send messages the specified number of times

		for (int i = 0; i < numMessages; i++) {
		    try {
					MessageInfo message = new MessageInfo(numMessages, i);
					iRMIServer.receiveMessage(message);
				} catch (RemoteException e) {
					System.out.println("Exception:" + e + " at message number: " + i);
				}
		}

	}
}

