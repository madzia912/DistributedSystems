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
	public static void main(String[] args) {
		Socket s = null;
		try
		{
			int serverPort = 5679;//to do the list of nodes/ports
			s = new Socket(args[0], serverPort);
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			Scanner scan = new Scanner(System.in);
			while(true)
			{
				String line = scan.nextLine();
				switch(line)
				{
				case "shiftNode":
					break;
				case "getTemp":
					out.writeUTF("getTemp");
					String data = in.readUTF();
					System.out.println("Received: " + data);
					break;
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
			System.out.println("IO: " + e.getMessage());
		}
		finally
		{
			if(s != null)
			try
			{
				s.close();
			}
			catch(IOException e)
			{
				System.out.println("close: " + e.getMessage());
			}
		}
	}

}
