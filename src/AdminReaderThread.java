
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Scanner;

public class AdminReaderThread extends Thread
{	
	public static Socket soc = null;
	public AdminReaderThread(Socket s)
	{
		soc = s;
		this.start();
	}
	
	public void run()
	{	
		try 
		{
			DataInputStream in = new DataInputStream(soc.getInputStream());
			DataOutputStream out = new DataOutputStream(soc.getOutputStream());
			Scanner scan = new Scanner(System.in);
			
			while(true)
			{
				String line = scan.nextLine();
				switch(line)
				{
				case "shift":
					out.writeUTF("shift");
					//String data = in.readUTF();
					//System.out.println("Received: " + data);
					break;
				case "getTemp":
					out.writeUTF("getTemp");
					String data = in.readUTF();
					System.out.println("Received: " + data);
					break;
				}
			}
		} 
		catch (IOException e) 
		{	
			InputStream command;
			try {
				System.out.println("IO: fake msg getTemp " + e.getMessage());
				command = new ByteArrayInputStream( "getTemp".getBytes("UTF-8") );
				System.setIn(command);
				
			} catch (UnsupportedEncodingException e1) {
					
			}
			
		}
		

	}
}
