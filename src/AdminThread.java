
import java.io.*;
import java.net.*;
import java.util.HashMap;

public class AdminThread extends Thread{
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	double temp;
	HashMap<Integer, Double> hm = null;
	int ourKey;
	public AdminThread(Socket aClientSocket, Object obj)
	{
		try
		{
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			hm = (HashMap) obj;
			ourKey = hm.size() + 1;
			this.start();
		}
		catch(IOException e)
		{
			System.out.println("EchoThread: " + e.getMessage());
		}
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				
				String data = in.readUTF();
				temp = Double.parseDouble(data);
				hm.put((int) getId(), temp);
				System.out.println("Key: " + getId() + " value: " + data);
				//out.writeUTF(data);
			}
		}
		catch(EOFException e)
		{
			System.out.println("EOF: " + e.getMessage());
		}
		catch(IOException e)
		{
			
			hm.remove((int) getId());
			//System.out.println("We delete the key");
			System.out.println("IO: " + e.getMessage());
		}
//		finally
//		{
//			try
//			{
//				clientSocket.close();
//			}
//			catch(IOException e)
//			{
//				/* close failed */
//			}
//			
//		}
	}
}
