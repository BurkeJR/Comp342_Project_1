import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/** Author:  John Burke, Sydnee Charles
  * Course:  COMP 342 Data Communications and Networking
  * Date:    12 April 2021
  * Description: Project 1: FTP Server and Client
*/
public class Client {

	public static void main(String[] args) {
		try {
			//create socket
			Socket mySocket = new Socket("localhost",9001);
			
			//create input and output stream for the socket
			DataInputStream inStream = new DataInputStream(mySocket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(mySocket.getOutputStream());
			
			//create console scanner
			Scanner console = new Scanner(System.in);
			
			System.out.println("Welcome to the GCC FTP Client!");
			
			String toServer = "";
			
			while(!toServer.equals("QUIT")) {
				//get the command from the console
				System.out.print("Command: ");
				toServer = console.next();
				
				if(toServer.equals("STOR")) {	
					//Send command request to server
					outStream.writeUTF(toServer);
					
					if(console.hasNext()) {
						//send file to server
						String file = console.next();
						outStream.writeUTF(file);
						//Get file to send
						File myFile = new File(file);
						Scanner readFile = new Scanner(myFile);
						//Get file content by char
						String fileContent = "";
						readFile.useDelimiter("");
						while(readFile.hasNext()) {
							fileContent += readFile.next();
						}

						//Send file content to server
						outStream.writeUTF(fileContent);
						//Close scanner
						readFile.close();
					}			
					
				}
				else if(toServer.equals("RETR")) {
					//Send command request to server
					outStream.writeUTF(toServer);
					if(console.hasNext()) {
						//Send requested file name to Server
						String file = console.next();
						outStream.writeUTF(file);
						//Create requested file
						PrintWriter myFile = new PrintWriter(file);
						myFile.print(inStream.readUTF());
						//Close PrintWriter
						myFile.close();
					}
					
				}
				
				else {
					//send the command to the server
					outStream.writeUTF(toServer);
				}				
				
				outStream.flush();
				
				//receive the server's response
				System.out.println(inStream.readUTF());
				
				System.out.println("");
			}
			
			//close everything
			inStream.close();
			outStream.close();
			mySocket.close();
			console.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
