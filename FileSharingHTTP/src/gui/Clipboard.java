package gui;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.Values;

public class Clipboard extends JFrame {

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
					Clipboard frame = new Clipboard();
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
	public Clipboard() {
		setResizable(false);
		setTitle("Clipboard");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		
		
		
		
		
		
		
		JScrollPane scrollPane = new JScrollPane(Values.list);
		scrollPane.setBounds(10, 11, 614, 336);
		
		contentPane.add(scrollPane);
		
		JButton btnCopy = new JButton("Copy");
		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Values.list.getSelectedIndex() != -1) {
					copy(Values.list.getSelectedValue());
				}
			}
		});
		btnCopy.setBounds(271, 358, 89, 23);
		contentPane.add(btnCopy);
		
		textField = new JTextField();
		textField.setBounds(10, 406, 496, 44);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String txt = textField.getText();
				if(txt.length()<1)return;
				Values.addClip(txt);
				
				textField.setText("");
				
			}
		});
		btnAdd.setBounds(535, 417, 89, 23);
		contentPane.add(btnAdd);
		
		
		
	}
	
	
	
	public void copy(String myString) {
		StringSelection stringSelection = new StringSelection(myString);
		java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
	
}
