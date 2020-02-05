/*
 * Created on 01-Mar-2016
 */
package udp;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.jdi.IntegerType;
import common.MessageInfo;

public class UDPServer {
	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	MessageInfo msg = null;
	private boolean close;

	private void run() throws SocketTimeoutException {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
			while (!close) {
			    pacSize = 4000;
			    pacData = new byte[pacSize];
			    pac = new DatagramPacket(pacData, pacSize);
			    try {
			    	recvSoc.setSoTimeout(50000);//high to give me more time to move between pcs..
			    	recvSoc.receive(pac);

				} catch (IOException e) {
					System.out.println("Exception: " + e);
				       outputIfLost(msg); 
					System.exit(-9);
				}
			    processMessage(new String(pac.getData())); 
			}
	}

	public void processMessage(String data) {


		// TO-DO: Use the data to construct a new MessageInfo object
		try {
			msg = new MessageInfo(data.trim());
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}

		// TO-DO: On receipt of first message, initialise the receive buffer
        if (receivedMessages == null || msg.totalMessages != totalMessages) {
        	totalMessages = msg.totalMessages;
        	receivedMessages = new int[totalMessages];
		}

		// TO-DO: Log receipt of the message
        receivedMessages[msg.messageNum] = 1;
	//System.out.println(msg.messageNum + " received"); 

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (msg.messageNum + 1 == totalMessages) { //gna 1-index hte messages
			ArrayList<Integer> missingIDs = new ArrayList<>();

			for (int i = 0; i < receivedMessages.length; i++) {
				if (receivedMessages[i] == 0) {
				    missingIDs.add(i);
				}
			}

			if (missingIDs.size() == 0) {
				System.out.println("No messages were lost!");
			} else {
				System.out.println(missingIDs.size() + " messages were lost.");
				String output = "Missing IDs are: ";
				for (int ID: missingIDs) {
				    output += ID + ", ";
				}
				System.out.println(output);
			}

			int total = totalMessages - missingIDs.size(); 
			System.out.println("The total number of messages received is: " + Integer.toString(total)); 
		//	totalMessages = -1; 
		//	receivedMessages = null; 
		}

	}

	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
        try {
        	recvSoc = new DatagramSocket(rp);
		System.out.println("should have init dgram socket"); 
		} catch (SocketException e) {
			System.out.println("Exception: " + e);
		}

		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public void outputIfLost(MessageInfo msg) {
		ArrayList<Integer> missingIDs = new ArrayList<>();
		for (int i = 0; i < receivedMessages.length; i++) {
			if (receivedMessages[i] == 0) {
			    missingIDs.add(i);
			}
		}

		if (missingIDs.size() == 0) {
			System.out.println("No messages were lost!");
		} else {
			System.out.println(missingIDs.size() + " messages were lost.");
			String output = "Missing IDs are: ";
			for (int ID: missingIDs) {
			    output += ID + ", ";
			}
			System.out.println(output);
		}
		int total = totalMessages - missingIDs.size(); 
		System.out.println("The total number of messages received is: " + Integer.toString(total)); 
	}

	public static void main(String args[]) {
		int	recvPort;
		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
	       UDPServer server = new UDPServer(recvPort);
	       try {
		       server.run();
	       } catch(SocketTimeoutException e) {
		       System.out.println("Exception: " + e); 
	       }
	}

}
