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

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		try {
			// TO-DO: Initialise Security Manager
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}
			// TO-DO: Bind to RMIServer
			iRMIServer = (RMIServer)Naming.lookup(urlServer);

		} catch (RemoteException | MalformedURLException | NotBoundException e ) {
			System.out.println("Exception: " + e);
			System.exit(1);
		}

		// TO-DO: Attempt to send messages the specified number of times

        //mmmm maybe move the try block? so each message is attempted.
        for (int i = 0; i < numMessages; i++) {
            try {
				MessageInfo message = new MessageInfo(numMessages, i);
				iRMIServer.receiveMessage(message);
			} catch (RemoteException e) {
				System.out.println("Exception:" + e + " at message number: " + i);
			}
        }
		System.exit(0);
	}
}

