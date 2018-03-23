package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import client.Client.DirectoryExistsException;

public class Application {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 779, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblInsertYourdir = new JLabel("Please enter a folder's name to be created in the downloads\r\n");
		lblInsertYourdir.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblInsertYourdir.setBounds(12, 13, 760, 41);
		frame.getContentPane().add(lblInsertYourdir);

		textField = new JTextField();
		textField.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		textField.setBounds(12, 149, 312, 48);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnJoin = new JButton("Join");

		btnJoin.setFont(new Font("Source Sans Pro Semibold", Font.BOLD, 22));
		btnJoin.setBounds(358, 148, 116, 48);
		frame.getContentPane().add(btnJoin);

		JLabel lblDirectorySoThat = new JLabel("directory, so that your files will be saved there.");
		lblDirectorySoThat.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblDirectorySoThat.setBounds(12, 55, 760, 41);
		frame.getContentPane().add(lblDirectorySoThat);
		btnJoin.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				try {
					String dir = textField.getText();
					if (dir == null || dir.equals("")) {
						JOptionPane.showMessageDialog(null, "Folder's name cannot be empty!");
						return;
					}
					Client client;
					try {
						client = new Client(4444, dir);
					} catch (DirectoryExistsException e) {
						JOptionPane.showMessageDialog(null, "Directory already exists!");
						return;
					}
					JOptionPane.showMessageDialog(null,
							"Folder created successfully, you can now find your downloads in the '" + dir
									+ "' Directory");
					new ClientGUI(client);
					frame.dispose();
				} catch (Exception e) {

				}
			}
		});

	}
}
