import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Random;
import java.lang.*;

public class RegularNode {

	/**
	 * @param args
	 */
	
	private static String adminIP;
	private static int adminPort;
	private static String groupIP;
	private static int groupPort;	
	private static Random fRandom = new Random();
	private static boolean ifAdmin = false;
	private static MulticastSocket s = null;
	private static HashMap<Integer, Double> tempMap = new HashMap<Integer, Double>();
	private static int serverPort = 6789;
	public static double getTemperature()
	{
		return (25 + fRandom.nextGaussian() * 5);
	}

	public static void setAdmin(boolean b)
	{
		ifAdmin = b;
	}
	public static void main(String[] args) {
		// arguments:
		// regNode groupIP groupPort adminIP adminPort
		// adminNode groupIP groupPort 0 0 //ourport todo later
		
		// part for both reg and admin node
		
		while(true)
		{
			try
			{
				groupIP = "228.5.6.7";
				groupPort = 6789;
				
				// join the multicast group
				s = new MulticastSocket(groupPort);
				InetAddress group = InetAddress.getByName(groupIP);
				s.joinGroup(group);
				
				ListenToUserThread c = new ListenToUserThread(tempMap);
				
				regMain();
			}
			catch(SocketException e) // exceptions to multicast group
			{
				System.out.println("Socket: " + e.getMessage());
			}
			catch(IOException e)
			{
				System.out.println("IO: " + e.getMessage());
			}
			finally
			{
				if(s != null)
				{
					s.close();
				}
			}
		}
		

	}
	
	private static void adminMain() 
	{
		ServerSocket adminSocket = null;
		TempThread t = new TempThread(tempMap, s, serverPort); //thread to measure the admin temp and sending the broadcast info
		
		while(true)
		{
			// create the TCP connection to regnode
			try
			{
				adminSocket = new ServerSocket(serverPort);
				while(true)
				{
					Socket clientSocket = adminSocket.accept();
					AdminThread c = new AdminThread(clientSocket, tempMap);
				}
			}
			catch(IOException e)
			{
				System.out.println("Listen :" + e.getMessage());
			}
		}
	}

	static void regMain()
	{	
		Socket regSocket = null;
		DataOutputStream out = null;
		while(true)
		{
			byte[] buffer = new byte[1000];
			DatagramPacket messIn = new DatagramPacket(buffer,buffer.length);
			try 
			{
				s.setSoTimeout(5000);
				try
				{
					s.receive(messIn); // informing a reg node that the admin has changed
					String info = new String(messIn.getData());
					String[] infoInWords = info.split(" ");
					adminIP = infoInWords[0];
					adminPort = 6789;//Integer.parseInt(infoInWords[1]);
					break;
				}
				catch(SocketTimeoutException e) // timer for messsage from multicast group
				{
					
				}
			} 
			catch (SocketException e) 
			{
				System.out.println("Sock: " + e.getMessage());
			}
			catch(IOException e)
			{
				System.out.println("IO: " + e.getMessage());
			}
			
			if(ifAdmin == true)
				adminMain();
		}
		while(true)
		{
			// create the TCP connection to admin
			try
			{	if(ifAdmin == true)
					adminMain();
				regSocket = new Socket(adminIP, adminPort);
				out = new DataOutputStream(regSocket.getOutputStream());
				
				while(true)
				{
					if(ifAdmin == true)
						adminMain();
					out.writeUTF(Double.toString(getTemperature()));
					byte[] buffer = new byte[1000];
					DatagramPacket messIn = new DatagramPacket(buffer,buffer.length);
					s.setSoTimeout(5000);
					try
					{
						s.receive(messIn); // informing a reg node that the admin has changed
						String shift = new String(messIn.getData());
						String[] shiftInWords = shift.split(" ");
						if(adminIP.compareTo(shiftInWords[0]) != 0 || adminPort != 6789)//Integer.parseInt(shiftInWords[1]))
						{
							adminIP = shiftInWords[0];
							adminPort = 6789;//Integer.parseInt(shiftInWords[1]);
							regSocket.close();
							break;
						}
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					catch(SocketTimeoutException e) // timer for messsage from multicast group
					{
						
					}
				}
			}
			catch(UnknownHostException e) // exceptions for regSocket connection establishment
			{
				System.out.println("Sock: " + e.getMessage());
			}
			catch(EOFException e)
			{
				System.out.println("EOF: " + e.getMessage());
			}
			catch(IOException e)
			{
				System.out.println("IO: " + e.getMessage());
			}
			if(regSocket != null)
			try
			{
				regSocket.close();
			}
			catch(IOException e)
			{
				System.out.println("close: " + e.getMessage());
			}
		}
	}

}
