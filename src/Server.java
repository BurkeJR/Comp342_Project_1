import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

/** Author:  John Burke, Sydnee Charles
  * Course:  COMP 342 Data Communications and Networking
  * Date:    12 April 2021
  * Description: 
*/
public class Server {
	
	public final static int BUFFER_SIZE = 500;

	public static void main(String[] args) {
		try {
			ServerSocket srverSocket=new ServerSocket(9001);  
			Socket mySocket =srverSocket.accept();  
			DataInputStream inStream=new DataInputStream(mySocket.getInputStream());
			DataOutputStream outStream=new DataOutputStream(mySocket.getOutputStream());  
			//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Welcome to GCC FTP service! \n Waiting for client commands...");
			String clientStr="", outStr="";
			while( !clientStr.equals("QUIT")) {
				outStr="";
				clientStr = inStream.readUTF();
				
				Scanner scan = new Scanner(clientStr);
				String cmd = scan.next();		
				//check which command was entered:
				if(cmd.equals("PWD")) {
					
					outStr = pwd() + "\n";
					
				}else if(cmd.equals("LIST")) {
					outStr = list();
						
				}else if (cmd.equals("STOR")) {
					//Get arguments
					if(scan.hasNext()) {
						byte[] buffer = new byte[BUFFER_SIZE];
						OutputStream myFile = new FileOutputStream(scan.next());
						inStream.read(buffer);
						myFile.write(buffer);
						
						outStr = "File stored correctly";
						myFile.close();	
						
					}else {
						outStr = "Need Arguments!\n";
					}
				}else if (cmd.equals("RETR")) {
					//Get arguments
					if(scan.hasNext()) {
						//send file to server
						File myFile = new File(scan.next());
						byte[] b = Files.readAllBytes(myFile.toPath());
						
						//Send file to server					
						outStream.write(b, 0, b.length);
						outStr = "Here is the " + myFile.getName() + " file contents!";
					}
					else {
						outStr = "Need Arguments!\n";
					}
					
				}else {
					//Invalid command
				}
				
				outStream.writeUTF(outStr);
				//flush the stream to make sure the data has been written 
				outStream.flush();
				scan.close();
				System.out.println("\n");
			}
			System.out.println("Connection terminated by the client...");
			
			//close everything we opened so far
			inStream.close();  
			outStream.close();
			mySocket.close();  
			srverSocket.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String list() {
		
		return "";
	}
	
	public static String pwd() {
		return "";
	}
	
}
