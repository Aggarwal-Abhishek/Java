package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.samskivert.mustache.Mustache;

import main.Utility;
import main.Values;
import webserver.WebServer.Response;

public class Initialize {

	
	public static void Do(WebServer server) {
		
		int n = Values.password.length() + 1 ;
		
		server.addTask("/" + Values.password + "/assets/", new WebServer.Task() {
			@Override
			public Response run(String uri, Properties header) {
				uri = uri.substring(n);
				return server.serverFile(new File("").getAbsolutePath() + uri, header) ;
			}
		});
		
		server.addTask("/" + Values.password + "/files/", new WebServer.Task() {
			
			@Override
			public Response run(String uri, Properties header) {
				uri = uri.substring(n + "/files/".length());
				File f = new File(uri);
				if(f.exists()) {
					
					if(f.isDirectory()) {
						return server.send(Utility.ServeDirectory(uri), WebServer.MIME_HTML);
					}else {
						return server.serverFile(uri, header);
					}
					
				}else {
					return server.send404();
				}
				
			}
		});
		
		server.addTask("/" + Values.password + "/receive/", new WebServer.Task() {
			
			@Override
			public Response run(String uri, Properties header) {
				return server.send(Utility.ServeDirectory(Values.sendFiles), WebServer.MIME_HTML);
			}
		});
				
		server.addTask("/" + Values.password + "/upload/", new WebServer.Task() {
			
			@Override
			public Response run(String uri, Properties header) {
				
				return server.serverFile(new File("").getAbsolutePath() + "/assets/html/upload.html", header);
			}
		});
		
		server.addTask("/" + Values.password + "/", new WebServer.Task() {
			
			@Override
			public Response run(String uri, Properties header) {
				
				FileInputStream fis;
				try {
					fis = new FileInputStream(new File(new File("").getAbsolutePath() + "/assets/html/index.html"));
					String text = new String(fis.readAllBytes());
					
					text = Mustache.compiler().compile(text).execute(new Object() {
						@SuppressWarnings("unused")
						String password = Values.password ;
					});
					fis.close();
					
					return server.send(text, WebServer.MIME_HTML);
					
				} catch (Exception e) {}
				return server.send404();
			}
		});
		
		server.addTask("/", new WebServer.Task() {
			
			@Override
			public Response run(String uri, Properties header) {
				return server.serverFile(new File("").getAbsolutePath() + "/assets/html/password.html", header);
			}
		});
		
	}
	
}
