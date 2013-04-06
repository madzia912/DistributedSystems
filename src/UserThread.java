
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserThread extends Thread{
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	HashMap<Integer, Double> hm = null;
	public UserThread(Socket aClientSocket, Object obj)
	{
		try
		{
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			hm = (HashMap) obj;
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
				switch(data)
				{
				case "getTemp":
					int size = hm.size();
					Iterator it = hm.entrySet().iterator();
					double sum = 0; 
				    while (it.hasNext()) {
				    	Map.Entry pairs = (Map.Entry)it.next();
				    	sum = sum + (double) pairs.getValue();
				        //it.remove(); // avoids a ConcurrentModificationException
				    }
					double avg = sum / size;
					out.writeUTF(Double.toString(avg));
					break;
				}
				//
			}
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
			try
			{
				clientSocket.close();
			}
			catch(IOException e)
			{
				/* close failed */
			}
			
		}
	}
}
