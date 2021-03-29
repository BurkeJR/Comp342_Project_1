import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/** Author:  John Burke, Sydnee Charles
  * Course:  COMP 342 Data Communications and Networking
  * Date:    12 April 2021
  * Description: 
*/
public class Server {
	
	private File currentDirectory;

	public static void main(String[] args) {
		try {
			ServerSocket srverSocket=new ServerSocket(9001);  
			Socket mySocket =srverSocket.accept();  
			DataInputStream inStream=new DataInputStream(mySocket.getInputStream());
			DataOutputStream outStream=new DataOutputStream(mySocket.getOutputStream());  
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Welcome to GCC FTP service! \n Waiting for client commands...");
			String clientStr="", outStr="";
			while( !clientStr.equals("QUIT")) {
				
				clientStr = inStream.readUTF();
				//System.out.println("clientStr: " + clientStr);
				
				Scanner scan = new Scanner(clientStr);
				String cmd = scan.next();		
				//check which command was entered:
				if(cmd.equals("PWD")) {
					
					outStr = pwd();
					
				}else if(cmd.equals("LIST")) {
					outStr = list();
					
				}else if (cmd.equals("STOR")) {
					//Get arguments
					if(scan.hasNext()) {
						outStr = store(scan.next());
					}else {
						outStr = "Need Arguments!";
					}
					
				}else if (cmd.equals("RETR")) {
					//Get arguments
					if(scan.hasNext()) {
						outStr = retr(scan.next());
					}else {
						outStr = "Need Arguments!";
					}
					
				}else if (!cmd.equals("QUIT")){
					//Invalid command
					outStr = "Invalid Command: " + cmd;
				}
				outStream.writeUTF(outStr);
				//flush the stream to make sure the data has been written 
				outStream.flush();
				System.out.println("\n");
				//close the scanner
				scan.close();
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
		
		return "Testing List";
	}
	
	public static String pwd() {
		return "Testing PWD";
	}
	
	public static String store(String file) {
		return "File stored correctly";
	}
	
	public static String retr(String file) {
		return "Here is the XX file contents!";
	}
	

}
