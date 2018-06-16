package sources;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientForm extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField textField;
	static JTextArea textArea;
	private static JTextArea textArea_1;
	private JComboBox<String> comboBox;
	
	public void displayMessage(String msg) {
		textArea.append(msg + "\n");
	}

	public void displayUserName(String UserName) {
		textArea_1.append(UserName);
	}
	
	public void updateUsers(ArrayList<String> Users) {
		comboBox.removeAllItems();
		comboBox.addItem("Server");
		for(int i = 0; i < Users.size(); i++) {
			comboBox.addItem(Users.get(i));
		}
	}
	
	public String getRecipient() {
		return (String) comboBox.getSelectedItem();
	}
	
	/**
	 * Create the frame.
	 */
	public ClientForm() {
		super ("Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 622, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDialog = new JLabel("Dialog");
		lblDialog.setBounds(10, 11, 440, 28);
		lblDialog.setVerticalAlignment(JLabel.CENTER);
		lblDialog.setHorizontalAlignment(JLabel.CENTER);
		contentPane.add(lblDialog);
		
		JLabel lblUserName = new JLabel("User Name:");
		lblUserName.setBounds(460, 127, 131, 21);
		lblUserName.setVerticalAlignment(JLabel.CENTER);;
		lblUserName.setHorizontalAlignment(JLabel.CENTER);
		contentPane.add(lblUserName);
		
		textField = new JTextField();
		textField.setBounds(10, 466, 440, 34);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					ClientLogic.sendMessage(textField.getText());
				} catch (IOException e) { e.printStackTrace(); }
				textField.setText("");
			}
		});
		
		JButton btnSendMessage = new JButton("Send Message");
		btnSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try { ClientLogic.sendMessage(textField.getText());	}
				catch (IOException e) { e.printStackTrace(); }
				textField.setText("");
			}
		});
		btnSendMessage.setBounds(460, 466, 136, 34);
		contentPane.add(btnSendMessage);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 440, 416);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		textArea_1.setBounds(460, 159, 136, 28);
		contentPane.add(textArea_1);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(460, 200, 136, 28);
		contentPane.add(comboBox);
		
		JButton btnNewButton = new JButton("Disconnect");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try { ClientLogic.sendClientStatus("removing");	} 
				catch (IOException e) { e.printStackTrace();	}
			}
		});
		btnNewButton.setBounds(465, 42, 131, 40);
		contentPane.add(btnNewButton);
		
		/* listModel = new DefaultListModel();
		JList list = new JList(listModel);
		list.setBounds(460, 95, 239, 360);
		contentPane.add(list); */
	}
}
