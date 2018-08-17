package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import main.Values;

public class EnterPort extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EnterPort frame = new EnterPort();
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

	public EnterPort() {
		setResizable(false);
		setTitle("Port");
		setBounds(100, 100, 450, 263);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		
		JLabel lblEnterNewPassword = new JLabel("Enter New Port : ");
		lblEnterNewPassword.setBounds(10, 11, 414, 30);
		contentPane.add(lblEnterNewPassword);
		
		JLabel label = new JLabel(": - )");
		label.setBounds(10, 183, 414, 14);
		contentPane.add(label);
		
		
		
		
		
		JButton btnOk = new JButton("Done");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Values.port = Integer.parseInt(textField.getText());
				contentPane.setVisible(false);
			}
		});
		btnOk.setBounds(178, 117, 89, 23);
		btnOk.setEnabled(false);
		contentPane.add(btnOk);
		
		
		
		
		
		
		
		textField = new JTextField();
		textField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				String txt = ((JTextField)arg0.getSource()).getText();
				String msg;
				
				if(txt == null || txt.length()<1) {
					msg = "Enter a valid port...";
				}
				else {
					msg = "1";
					
					try {
						int p = Integer.parseInt(txt);
						
						if(p>=1 && p<=65535) {}
						else msg = "Enter between 80 and 65535...";
					}catch(Exception e) {msg = "Enter a valid port number...";}
					
				}
				if(msg.equals("1")) {
					btnOk.setEnabled(true);
					label.setForeground(Color.GREEN);
					label.setText("Good to Go...");
				}
				else {
					btnOk.setEnabled(false);
					label.setForeground(Color.RED);
					label.setText(msg);
				}
			}
		});
		
		textField.setBounds(10, 58, 414, 37);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		
		
		
		
	}


}
