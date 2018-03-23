package client;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class ClientGUI {

	private JFrame frmChatApplication;
	public JTextField userInput;
	public JTextPane textPane_req;
	public JTextPane textPane_res;
	Client client;

	public void terminate() {
		JOptionPane.showMessageDialog(null, "Thanks for using the application :)");
		frmChatApplication.dispose();
	}

	public ClientGUI(Client client) {
		this.client = client;
		initialize();
		this.frmChatApplication.setVisible(true);
		client.setGUI(this);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChatApplication = new JFrame();
		frmChatApplication.setTitle("Chat application");
		frmChatApplication.setResizable(false);
		frmChatApplication.setBounds(100, 100, 817, 521);
		frmChatApplication.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmChatApplication.getContentPane().setLayout(null);

		userInput = new JTextField();
		userInput.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
		userInput.setBounds(12, 420, 370, 58);
		frmChatApplication.getContentPane().add(userInput);
		userInput.setColumns(10);

		JButton sendButton = new JButton("Send !");
		sendButton.setBounds(589, 420, 175, 58);
		sendButton.setBackground(Color.LIGHT_GRAY);
		sendButton.setFont(new Font("Verdana", Font.BOLD, 24));
		frmChatApplication.getContentPane().add(sendButton);

		JLabel lblChooseTtl = new JLabel("Choose connection:");
		lblChooseTtl.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		lblChooseTtl.setBounds(402, 378, 145, 29);
		frmChatApplication.getContentPane().add(lblChooseTtl);

		Choice choice_CONN = new Choice();
		choice_CONN.setBounds(402, 420, 145, 22);
		frmChatApplication.getContentPane().add(choice_CONN);

		JLabel currentUser = new JLabel("Current directory: " + client.dirName);
		currentUser.setHorizontalAlignment(SwingConstants.CENTER);
		currentUser.setFont(new Font("Verdana", Font.BOLD, 20));
		currentUser.setBounds(22, 13, 777, 42);
		frmChatApplication.getContentPane().add(currentUser);

		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		btnQuit.setFont(new Font("Times New Roman", Font.BOLD, 31));
		btnQuit.setBackground(Color.RED);
		btnQuit.setBounds(589, 127, 175, 62);
		frmChatApplication.getContentPane().add(btnQuit);

		JLabel lblTypeYourMessage = new JLabel("Type the file name you want to retrieve:");
		lblTypeYourMessage.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblTypeYourMessage.setBounds(12, 378, 370, 29);
		frmChatApplication.getContentPane().add(lblTypeYourMessage);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(22, 68, 777, 7);
		frmChatApplication.getContentPane().add(separator_1);

		JLabel lblNewLabel = new JLabel("REQUEST:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Source Code Pro Light", Font.BOLD, 18));
		lblNewLabel.setBounds(32, 88, 219, 26);
		frmChatApplication.getContentPane().add(lblNewLabel);

		JLabel lblResponse = new JLabel("RESPONSE:");
		lblResponse.setHorizontalAlignment(SwingConstants.CENTER);
		lblResponse.setFont(new Font("Source Code Pro Light", Font.BOLD, 18));
		lblResponse.setBounds(328, 88, 219, 26);
		frmChatApplication.getContentPane().add(lblResponse);

		textPane_req = new JTextPane();
		textPane_req.setEditable(false);
		textPane_req.setBounds(32, 127, 219, 219);
		frmChatApplication.getContentPane().add(textPane_req);

		textPane_res = new JTextPane();
		textPane_res.setEditable(false);
		textPane_res.setBounds(328, 127, 219, 219);
		frmChatApplication.getContentPane().add(textPane_res);
		choice_CONN.add("keep-alive");
		choice_CONN.add("close");

		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (userInput.getText().equals("") || userInput.getText() == null) {
						JOptionPane.showMessageDialog(null, "You must enter a file URL to be requested");
						return;
					}
					client.sendRequest(userInput.getText(), choice_CONN.getSelectedItem());
					userInput.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});

		btnQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					client.sendRequest("NONE", "close");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}
}