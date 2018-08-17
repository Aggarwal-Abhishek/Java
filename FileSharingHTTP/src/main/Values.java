
package main;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import webserver.Initialize;
import webserver.WebServer;

public class Values {
	
	public static String password = "a" ;
	public static int port = 80;
	public static ArrayList<String> sendFiles = new ArrayList<String>();
	public static ArrayList<String>clipboard = new ArrayList<String>();
	public static JList<String>list = new JList<String>(new DefaultListModel<String>());
	public static boolean serverRunning = false;
	
	public static WebServer server;
	
	
	static {
		
		if(clipboard.size() == 0) {
			addClip("Enter Text...");
		}
		
		
		list.setSelectedIndex(0);
	}
	
	
	
	
	public static void addFile(String s) {
		if(!sendFiles.contains(s))sendFiles.add(s);
	}
	
	public static void addClip(String s) {
		clipboard.add(s);
		((DefaultListModel<String>)list.getModel()).addElement(s);
	}
}
