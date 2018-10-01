package part2_server_client;

import java.net.Socket;
import java.net.SocketException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
// Code taken from class moodle page
public class Client 
{

    private static DataInputStream input_stream;
    private static DataOutputStream output_stream;

    public static void main(String[] args) throws IOException, InterruptedException
    {
    	try {
	        Socket s = new Socket("127.0.0.1", 5422);
	        output_stream = new DataOutputStream(s.getOutputStream());
	        input_stream = new DataInputStream(s.getInputStream());
	
	        int id = Integer.parseInt(args[0]);
	
	        int iter = 0;
	        while(true)
	        {
	            // System.out.println("Iterating...");
	            output_stream.writeInt(id);
	            // System.out.println("Wrote first int");
	            output_stream.writeInt(iter);
	            // System.out.println("Wrote second int");
	            ++iter;
	
	            System.out.println("Client received: " + input_stream.readInt() + " " + input_stream.readInt());
	            Thread.sleep(2000);
	        }
    	} catch (SocketException e) {
    		e.printStackTrace();
    	}
    }

}

