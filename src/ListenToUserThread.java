
import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ListenToUserThread extends Thread
{
	private static HashMap<Integer, Double> tempMap = new HashMap<Integer, Double>();
	
	public ListenToUserThread(Object obj)
	{
		tempMap = (HashMap<Integer, Double>) obj;
		this.start();
	}
	
	public void run()
	{	
		int serverPort = 5678;
		while(true){
			try
			{
				
				serverPort ++;
				ServerSocket userSocket;
				userSocket = new ServerSocket(serverPort);
				
				Socket uSocket = userSocket.accept();
				UserThread c = new UserThread(uSocket, tempMap);
				RegularNode.setAdmin(true);
			}
			catch(IOException e)
			{
				System.out.println("Listen :" + e.getMessage());
			}
		}
	}
}
