package part2_server_client;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Server implements Runnable
{

    private static CopyOnWriteArrayList<DataInputStream> input_streams;
    private static CopyOnWriteArrayList<DataOutputStream> output_streams;

    private static ServerSocket ss;

    public Server() { }

    public void run()
    {
        try
        {
            while(true)
            {
                System.out.println("About to accept...");
                Socket s = ss.accept();
                System.out.println("New connection Established");
                synchronized(this)
                {
                    output_streams.add(new DataOutputStream(s.getOutputStream()));
                    input_streams.add(new DataInputStream(s.getInputStream()));
                }
                System.out.println("Streams successfully added.");
            }
        }
        catch(IOException iox)
        {
            iox.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
    	try {
	        ss = new ServerSocket(5422);
	        input_streams = new CopyOnWriteArrayList<DataInputStream>();
	        output_streams = new CopyOnWriteArrayList<DataOutputStream>();
	
	        Server server = new Server();
	        (new Thread(server)).start();
	
	        int iter = 0;
	        while(true)
	        {
	            synchronized(server)
	            {
	            	try {
		                for(DataInputStream din : input_streams)
		                {
		                    System.out.println("Server received: " + din.readInt() + " " + din.readInt());
		                }
	            	} catch (IOException e) {
	            		//nothing
	            	}
	            }
	            System.out.println("Server completed reading all streams, now writting");
	            synchronized(server)
	            {
	            	try {
		                for(DataOutputStream dout : output_streams)
		                {
		                    dout.writeInt(0);
		                    dout.writeInt(iter);
		                }
	            	} catch (IOException e) {
	            		//nothing
	            	}
		            Thread.sleep(2000);
	            }
	            ++iter;
	        }
    	} catch (SocketException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    } 

}