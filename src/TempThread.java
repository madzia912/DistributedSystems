import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class TempThread extends Thread{
	
	HashMap<Integer, Double> hm;
	int ourKey = 0;
	double temp;
	private static Random fRandom = new Random();
	MulticastSocket s;
	int serverPort;
	public TempThread(Object obj, MulticastSocket b, int port)
	{
		hm = (HashMap<Integer, Double>) obj;
		s = b;
		serverPort = port;
		this.start();
	}
	
	public static double getTemperature()
	{
		return (25 + fRandom.nextGaussian() * 5);
	}

	public void run()
	{
		while(true)
		{
			temp = getTemperature();
			hm.put((int) getId(), temp);
			System.out.println("Key: " + getId() + " value: " + temp);
			InetAddress addr;
			try 
			{
				addr = InetAddress.getLocalHost();
				String msg = addr.getHostAddress();
				msg += " " + serverPort;
				byte[] m = msg.getBytes();
				DatagramPacket messageOut = new DatagramPacket(m, m.length, InetAddress.getByName("228.5.6.7"), 6789);
				s.send(messageOut);
			} 
			catch (UnknownHostException e) 
			{
				System.out.println("Host: " + e.getMessage());
			}
			catch (IOException e) 
			{
				System.out.println("IO: " + e.getMessage());
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
