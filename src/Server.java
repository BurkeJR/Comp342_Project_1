import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

	public static void main(String[] args) {
		try {
			ServerSocket srverSocket=new ServerSocket(9001);  
			Socket mySocket =srverSocket.accept();  
			DataInputStream inStream=new DataInputStream(mySocket.getInputStream());
			DataOutputStream outStream=new DataOutputStream(mySocket.getOutputStream());  
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Welcome to GCC FTP service! \n Waiting for client commands...");
			String clientStr="", outStr="Got it!";
			while( !clientStr.equals("QUIT")) {
				
				clientStr = inStream.readUTF();
				
				Scanner scan = new Scanner(clientStr);
				String cmd = scan.next();
								
				//check which command was entered:
				if(cmd == "PWD") {
					
					outStr = pwd();
					
				}else if(cmd == "LIST") {
					outStr = list();
					
				}else if (cmd == "STOR") {
					//Get arguments
					if(scan.hasNext()) {
						outStr = store(scan.next());
					}else {
						outStr = "Need Arguments!";
					}
					
				}else if (cmd =="RETR") {
					//Get arguments
					if(scan.hasNext()) {
						outStr = retr(scan.next());
					}else {
						outStr = "Need Arguments!";
					}
					
				}else {
					//Invalid command
				}
				outStream.writeUTF(outStr);
				//flush the stream to make sure the data has been written 
				outStream.flush();
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
	
	public static String store(String file) {
		return "File stored correctly";
	}
	
	public static String retr(String file) {
		return "Here is the XX file contents!";
	}
	

}
