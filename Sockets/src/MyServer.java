import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

	int port;
	ServerSocket serverSocket ;

	
	public MyServer(int port) throws IOException {
		this.port = port;
		
		serverSocket = new ServerSocket(port);
	}
	
	public String getMessageFromClient() throws IOException {
		
		//System.out.println("Listening For Incomming Connections...");
		
		Socket clientSocket = serverSocket.accept();
		
		DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
		
		String string = (String)inputStream.readUTF();
		
		close();
		
		return string;
		
	}
	
	public void close() throws IOException {
		serverSocket.close();
	}
	
	
	
}
