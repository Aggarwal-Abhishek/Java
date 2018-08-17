package gui;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import main.Values;
import webserver.Initialize;
import webserver.WebServer;
public class Index extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private WebServer server;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Index frame = new Index();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	/**
	 * Create the frame.
	 */
	public Index() {
		setResizable(false);
		setTitle("My Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 651, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel label = new JLabel("");
		label.setBounds(10, 71, 614, 46);
		contentPane.add(label);
		
		
		JButton btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Values.serverRunning) {
					
					server.stop();
					((JButton)arg0.getSource()).setText("Start Server");
					Values.serverRunning = false;
					label.setText("");
					
				}else {
					
					try {
						server = new WebServer(Values.port);
						Initialize.Do(server);
						
						((JButton)arg0.getSource()).setText("Stop Server");
						Values.serverRunning = true;
						label.setForeground(Color.GREEN);
						label.setFont(new Font("Times New Roman", Font.PLAIN, 25) );
						label.setText("Server started at : [ip of this system]:[port]/[password]");
						
					} catch (IOException e) {
						
						label.setForeground(Color.RED);
						label.setText("Error : " + e.getMessage());
						
						((JButton)arg0.getSource()).setText("Failed Starting Server");
						e.printStackTrace();
					}
					
				}
				
			}
		});
		btnStartServer.setBounds(10, 21, 614, 38);
		contentPane.add(btnStartServer);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 128, 614, 2);
		contentPane.add(separator);
		
		JButton btnChangePassword = new JButton("Change Password");
		btnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EnterPassword.main(new String[] {""});;
			}
		});
		btnChangePassword.setBounds(10, 163, 143, 23);
		contentPane.add(btnChangePassword);
		
		JButton btnViewPassword = new JButton("Current Password");
		btnViewPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JOptionPane.showMessageDialog(contentPane,
						"Current Password : " + Values.password,
						"Password", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		btnViewPassword.setBounds(481, 163, 143, 23);
		contentPane.add(btnViewPassword);
		
		JButton btnChangePort = new JButton("Change Port");
		btnChangePort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EnterPort.main(null);
			}
		});
		btnChangePort.setBounds(10, 209, 143, 23);
		contentPane.add(btnChangePort);
		
		JButton btnCurrentPort = new JButton("Current Port");
		btnCurrentPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JOptionPane.showMessageDialog(contentPane,
						"Current Port No : " + Values.port,
						"Password", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnCurrentPort.setBounds(481, 209, 143, 23);
		contentPane.add(btnCurrentPort);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 270, 614, 10);
		contentPane.add(separator_1);
		
		JButton btnSendFile = new JButton("Send File");
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileChoserUtil accessory = new FileChoserUtil(fc);
				fc.setAccessory(accessory);
				
				int open = fc.showOpenDialog(fc);
				
				if(open == JFileChooser.APPROVE_OPTION) {
					DefaultListModel<?> model = accessory.getModel();
                    for (int i = 0; i < model.getSize(); i++) {
//                        System.out.println(((File)model.getElementAt(i)).getName());
                    	
                    	Values.addFile(((File)model.getElementAt(i)).getAbsolutePath());
                    }
				}
				
			}
		});
		
		
		btnSendFile.setBounds(10, 291, 143, 23);
		contentPane.add(btnSendFile);
		
		JButton btnClipboard = new JButton("Clipboard");
		btnClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Clipboard.main(null);
				
			}
		});
		btnClipboard.setBounds(481, 291, 143, 23);
		contentPane.add(btnClipboard);
		
		JButton btnReceiveFile = new JButton("Receive File");
		btnReceiveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JOptionPane.showMessageDialog(contentPane,
						"Sender's need to click on Send File on browser and then upload the file...\nFile will be uploaded to current directory...",
						":-)", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		btnReceiveFile.setBounds(481, 344, 143, 23);
		contentPane.add(btnReceiveFile);
		
		JButton btnSendDirectory = new JButton("Send Directory");
		btnSendDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				new DirectoryChooserUtil();
				
			}
		});
		btnSendDirectory.setBounds(10, 344, 143, 23);
		contentPane.add(btnSendDirectory);
		
		
		
		try {
			BufferedImage image = ImageIO.read(new File("assets/logo/main.jpg"));
			JLabel piclabel = new JLabel(new ImageIcon(image));
			piclabel.setBounds(163, 141, 308, 118);
			contentPane.add(piclabel);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
