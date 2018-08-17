import java.io.IOException;
import java.util.Scanner;

public class MySockets {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Act as Server/Client...(1/2)..?");
		int op = scanner.nextInt();
		
		
		if(op == 1) {
			
			System.out.println("Enter Port : ");
			int port = scanner.nextInt();
			
			System.out.println("Listening For Incomming Connections....");
			
			
			while(true) {
			
				try {
					System.out.println(new MyServer(port).getMessageFromClient());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
		else if(op == 2) {
			System.out.println("Enter Host and Port...");
			String host = scanner.next().toString();
			int port = scanner.nextInt();
			
			System.out.println("Enter Message to Send..");
			
			String message;
			while(true) {
				
				message = scanner.next().toString();
				
				try {
					new MyClient(host, port).sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		scanner.close();
		
		
		
	}
	
	
}
