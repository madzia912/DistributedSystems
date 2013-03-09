import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class RegularNode {

	/**
	 * @param args
	 */
	
	private static String adminIP;
	private static int adminPort;
	private static String groupIP;
	private static int groupPort;	
	
	private static boolean ifAdmin;
	
	public static double getTemperature()
	{
		return 5;
	}

	public static void main(String[] args) {
		// arguments:
		// regNode groupIP groupPort adminIP adminPort
		// adminNode groupIP groupPort 0 0
		
		if(args[2] == "0" && args[3] == "0")
		{
			ifAdmin = true;
			adminMain(args);
		}
		else
		{
			ifAdmin = false;
			regMain(args);
		}
	}
	
	private static void adminMain(String[] args) {
		
		groupIP = args[0];
		groupPort = Integer.parseInt(args[1]);
		
	}

	static void regMain(String[] args)
	{
		adminIP = args[2];
		adminPort = Integer.parseInt(args[3]);
		
		groupIP = args[0];
		groupPort = Integer.parseInt(args[1]);
		
		
		MulticastSocket s = null;
		try
		{
			// join the multicast group
			s = new MulticastSocket(groupPort);
			InetAddress group = InetAddress.getByName(groupIP);
			s.joinGroup(group);
			
			Socket regSocket = null;
			while(true)
			{
				// create the TCP connection to admin
				try
				{
					regSocket = new Socket(adminIP, adminPort);
					DataInputStream in = new DataInputStream(regSocket.getInputStream());
					DataOutputStream out = new DataOutputStream(regSocket.getOutputStream());
					out.writeUTF(Double.toString(getTemperature()));
					String data = in.readUTF();
					System.out.println("Received: " + data);
					
					byte[] buffer = new byte[1000];
					DatagramPacket messIn = new DatagramPacket(buffer,buffer.length);
					s.setSoTimeout(5000);
					try
					{
						s.receive(messIn);
						String shift = new String(messIn.getData());
						String[] shiftInWords = shift.split(" ");
						adminIP = shiftInWords[0];
						adminPort = Integer.parseInt(shiftInWords[1]);
					}
					catch(SocketTimeoutException e)
					{
						
					}
				}
				catch(UnknownHostException e)
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
				finally
				{
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
		catch(SocketException e)
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
