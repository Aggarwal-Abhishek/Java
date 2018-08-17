package fxgui;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.Values;
import webserver.Initialize;
import webserver.WebServer;

public class Controller implements Initializable{

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	
	
	ListView<String> iplistview;
	
	@FXML Button toggleserverbtn;
	@FXML Label serverstatuslabel;
	@FXML Button toggleiplistviewbtn;
	@FXML Button copyipbtn;
	@FXML AnchorPane serveranchorpane;
	
	@FXML TextField changeporttxtfield;
	@FXML Label changeportlabel;
	@FXML Button changeportbtn;
	
	
	@FXML public void toggleServer() {
		
		try {
			if(Values.serverRunning) {
				Values.serverRunning = false;
				Values.server.stop();
				
				toggleserverbtn.setText("Start Server");
				serverstatuslabel.setText("");
			}else {
				Values.serverRunning = true;
				Values.server = new WebServer(Values.port);
				Initialize.Do(Values.server);
				
				toggleserverbtn.setText("Stop Server");
				serverstatuslabel.setText("Server started at : http://[ip of this machine]:port/");
			}
		}
		catch (Exception e) {serverstatuslabel.setText("Operation Failed : "+e);e.printStackTrace();}
	}
	
	@FXML public void toggleIpListView() {
		
		if(iplistview == null) {
			iplistview = new ListView<>();
			iplistview.setVisible(false);
			
		}
		
		if(iplistview.isVisible()) {
			iplistview.setVisible(false);
			copyipbtn.setVisible(false);
		}else {
			iplistview.getItems().clear();
			iplistview.getItems().addAll(getUrlsList());
			iplistview.setVisible(true);
			copyipbtn.setVisible(true);
			

			serveranchorpane.getChildren().clear();
			serveranchorpane.getChildren().add(iplistview);
		}
	}
	
	@FXML public void copyIpToClipboard() {
		copyToClipboard(iplistview.getSelectionModel().getSelectedItem());
	}
	
	@FXML public void loadChangePort() {
		serveranchorpane.getChildren().clear();
		copyipbtn.setVisible(false);
		
		try {
			serveranchorpane.getChildren().add(FXMLLoader.load(getClass().getResource("ChangePort.fxml")));
			
		} catch (IOException e) {e.printStackTrace();}
	}
	
	@FXML public void checkPort() {
		String txt = changeporttxtfield.getText();
		
		String msg;
		
		if(txt == null || txt.length()<1) {
			msg = "Enter a valid port...";
		}
		else {
			msg = "1";
			
			try {
				int p = Integer.parseInt(txt);
				
				if(p>=80 && p<=65535) {}
				else msg = "Enter between 80 and 65535...";
			}catch(Exception e) {msg = "Enter a valid port number...";}
			
		}
		if(msg.equals("1")) {
			changeportbtn.setVisible(true);
			changeportlabel.setText("Good to Go...   :-) ");
			changeportbtn.setVisible(true);
			
		}
		else {
			changeportbtn.setVisible(false);
			changeportlabel.setText(msg);
			changeportbtn.setVisible(false);
		}
	}
	
	@FXML public void changePort() {
		try {
			int port = Integer.parseInt(changeporttxtfield.getText());
			Values.port = port;
			
			if(Values.serverRunning) {
				
				Values.server.stop();
				Values.server = new WebServer(port);
				Initialize.Do(Values.server);
				
			}
			changeportlabel.setText("Updated...");
		} catch (Exception e) {
			e.printStackTrace();
			changeportlabel.setText("Failed Update Port : "+e);
		}
	}
	
	
	
	public void copyToClipboard(String myString) {
		StringSelection stringSelection = new StringSelection(myString);
		java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
	

    public static List<String> getUrlsList(){
        List<String> urls = new ArrayList<>() ;

        try{
            for(Enumeration<NetworkInterface> enInterface = NetworkInterface.getNetworkInterfaces();
                enInterface.hasMoreElements();){
                NetworkInterface ni = enInterface.nextElement();

                for(Enumeration<InetAddress> enAddress = ni.getInetAddresses();
                    enAddress.hasMoreElements();){
                    InetAddress addr = enAddress.nextElement() ;

                    if(addr instanceof Inet4Address){

                        urls.add("http://"+addr.getHostAddress()+"/");

                    }else if(addr instanceof Inet6Address){
                        String tmp = addr.getHostAddress();
                        int i = tmp.lastIndexOf("%");
                        if(i!=-1)tmp = tmp.substring(0,i);

                        urls.add("http://["+tmp+"]/") ;

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return urls;
    }

	
}
