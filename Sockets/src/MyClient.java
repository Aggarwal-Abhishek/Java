import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient {

	int port;
	String host;
	
	Socket clientSocket;
	
	
	public MyClient(String host,int port) throws UnknownHostException, IOException {
		this.host = host;
		this.port = port;
		
		clientSocket = new Socket(host , port );
	}
	
	void sendMessage(String message) throws IOException {
		DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
		
		outputStream.writeUTF(message);
		
		outputStream.flush();
		outputStream.close();
		
		clientSocket.close();
	}
	
	void close() throws IOException {
		clientSocket.close();
	}
	
	
}
