import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class UserNode {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{	
		int serverPort = 5679;//to do the list of nodes/ports
		while(true)
		{
			Socket s = null;
			try
			{
				
				s = new Socket(args[0], serverPort);
				AdminReaderThread reader = new AdminReaderThread(s);
				DataInputStream in = new DataInputStream(s.getInputStream());
				DataOutputStream out = new DataOutputStream(s.getOutputStream());
				while(true)
				{
					out.writeUTF("poll");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
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
				if(serverPort > 5779)
					serverPort = 5679;
				else
					serverPort++;

				System.out.println("IO: " + e.getMessage());
			}
//		finally
//		{
//			if(s != null)
//			try
//			{
//				s.close();
//			}
//			catch(IOException e)
//			{
//				System.out.println("close: " + e.getMessage());
//			}
//		}
		}
	}

}
