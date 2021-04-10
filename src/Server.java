import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/** Author:  John Burke, Sydnee Charles
  * Course:  COMP 342 Data Communications and Networking
  * Date:    12 April 2021
  * Description: Project 1: FTP Server and Client
*/
public class Server {
	
	private static File currentDirectory = new File("./");

	public static void main(String[] args) {
		try {
			ServerSocket srverSocket=new ServerSocket(9001);  
			Socket mySocket =srverSocket.accept();  
			DataInputStream inStream=new DataInputStream(mySocket.getInputStream());
			DataOutputStream outStream=new DataOutputStream(mySocket.getOutputStream());  
			//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Welcome to GCC FTP service! \n Waiting for client commands...");
			
			String clientCmd="", outStr="";
			while( !clientCmd.equals("QUIT")) {
				
				outStr="";		
				clientCmd = inStream.readUTF();	//Client command
				
				//check which command was entered:
				if(clientCmd.equals("PWD")) {
					
					outStr = pwd();
					
				}
				
				else if(clientCmd.equals("LIST")) {
					outStr = list();
						
				}
				
				else if (clientCmd.equals("STOR")) {
					//Get file name and content
					String arg = inStream.readUTF();
					String fileContent = inStream.readUTF();
					
					outStr = Store(arg, fileContent);
					
				}
				
				else if (clientCmd.equals("RETR")) {
					String arg = inStream.readUTF();
					if(!arg.isEmpty()) {
						//Get file to send
						File myFile = new File("S" + arg); //Server copy of file
						//Read file content
						Scanner readFile = new Scanner(myFile);
						String fileContent = "";
						readFile.useDelimiter(""); 
						//Read file by character
						while(readFile.hasNext()) {
							fileContent += readFile.next();
						}
						//Send file content to client
						outStream.writeUTF(fileContent);
						//Output string
						outStr = "Here is the " + arg.substring(0, arg.length() - arg.indexOf(".")) 
								+ " file contents!"; //+ fileContent;
						//Close scanner
						readFile.close();
					}else {
						outStr = "Need Arguments!\n";
					}
					
				}
				
				else if (!clientCmd.equals("QUIT")){
					//Invalid command
					outStr = "Invalid Command: " + clientCmd;
				}
				
				outStream.writeUTF(outStr);
				//flush the stream to make sure the data has been written 
				outStream.flush();
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
		String l = "";
		//add the name of each file in the current directory to a String
		for(File entry: currentDirectory.listFiles()) {
			l += entry.getName() + "\n";
		}
		//return the String that has all of the file names
		return l;
	}
	
	public static String pwd() {
		//returns the path of the current directory
		return currentDirectory.getAbsolutePath();
	}
	
	public static String Store(String filename, String content) throws FileNotFoundException {
		if(!filename.isEmpty()) {
			//Create a server copy of file
			PrintWriter myFile = new PrintWriter("S" + filename);
			myFile.print(content);//Print content to file
			//Closer PrintWriter
			myFile.close();	
			//Output string
			return "File stored correctly";
			
		}else {
			return "Need Arguments!\n";
		}
	}
	
}
