package sources;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Authorization extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private String UserName;
	private String ServerName;
	
	public void setUserName(String s) {
		this.UserName = s;
	}
	
	public void setServerName(String s) {
		this.ServerName = s;
	}
	
	public String getUserName() {
		return UserName;
	}
	
	public String getServerName() {
		return ServerName;
	}
	
	public void end() {
		dispose();
	}
	
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Authorization frame = new Authorization();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Authorization() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 500, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setBounds(10, 11, 80, 41);
		contentPane.add(lblUserName);
		
		textField = new JTextField();
		textField.setBounds(100, 11, 374, 41);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblServerAddress = new JLabel("Server Address");
		lblServerAddress.setBounds(10, 63, 80, 41);
		contentPane.add(lblServerAddress);
		
		textField_1 = new JTextField();
		textField_1.setBounds(100, 63, 374, 41);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton = new JButton("Connect");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setUserName(textField.getText());
				setServerName(textField_1.getText());
			}
		});
		btnNewButton.setBounds(10, 115, 464, 35);
		contentPane.add(btnNewButton);
	}
}
