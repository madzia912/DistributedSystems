
import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ListenToUserThread extends Thread
{
	private static HashMap<Integer, Double> tempMap = new HashMap<Integer, Double>();
	private static Boolean ifAdmin;
	public ListenToUserThread(Object obj, Boolean obj2)
	{
		tempMap = (HashMap<Integer, Double>) obj;
		ifAdmin = obj2;
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
				ifAdmin = true;
			}
			catch(IOException e)
			{
				System.out.println("Listen :" + e.getMessage());
			}
		}
	}
}
