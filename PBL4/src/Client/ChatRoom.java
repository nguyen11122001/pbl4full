package Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import DTO.Message;

import javax.swing.JTextField;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JEditorPane;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.border.SoftBevelBorder;

public class ChatRoom extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPanel chatbox_panel;
	private static JTextPane textPane;
	private ChatClient chat;
	private String username;
	private String peer;
	public static JLabel img_server;
	public static JLabel img_client;
	public static ClientVideo client_videoClient;

	/**
	 * Create the frame.
	 * @throws LineUnavailableException 
	 * @throws SocketException 
	 * @throws UnknownHostException 
	 */
	public ChatRoom(String username, String peer) throws UnknownHostException, SocketException, LineUnavailableException {
		this.username = username;
		this.peer = peer;
		
		initialize();
		this.setTitle(username);
		this.chat = new ChatClient(username,peer);
		new Thread(chat).start();
		client_videoClient=new ClientVideo(chat.udp_socket, username, peer);
		client_videoClient.StartReceiveVideo();
	}
	
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1197, 740);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel video_panel = new JPanel();
		video_panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		video_panel.setBounds(10, 173, 805, 510);
		panel.add(video_panel);
		video_panel.setLayout(null);
		
		img_server = new JLabel("");
		img_server.setBounds(10, 10, 785, 490);
		video_panel.add(img_server);
		
		JPanel user_panel = new JPanel();
		user_panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		user_panel.setBounds(10, 10, 550, 153);
		panel.add(user_panel);
		user_panel.setLayout(null);
		
		JButton btn_Video_voice = new JButton("Call");
		btn_Video_voice.setBounds(23, 10, 200, 117);
		user_panel.add(btn_Video_voice);
		
		JButton btnNewButton = new JButton("Stop");
		btnNewButton.setBounds(258, 10, 200, 117);
		user_panel.add(btnNewButton);
		btn_Video_voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					client_videoClient.StartSendVideo();
				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		});
		
		chatbox_panel = new JPanel();
		chatbox_panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		chatbox_panel.setBounds(825, 10, 338, 673);
		panel.add(chatbox_panel);
		chatbox_panel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 618, 243, 44);
		chatbox_panel.add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = textField.getText();
				Message m = new Message(username,peer, msg);
				try {
					chat.sendMessage(m);
					textField.setText("");
					updateChat_send(msg);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSend.setBackground(Color.GRAY);
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnSend.setBounds(263, 618, 65, 44);
		chatbox_panel.add(btnSend);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 318, 598);
		chatbox_panel.add(scrollPane);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		scrollPane.setViewportView(textPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBounds(570, 10, 239, 153);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		img_client = new JLabel("");
		img_client.setBounds(10, 10, 219, 133);
		panel_1.add(img_client);
	}
	
	public static void updateChat_receive(String sender,String msg) {
		appendToPane(textPane, "<div class='left' style='width: 40%; background-color: #f1f0f0;'>"+"<strong>"+sender+": </strong>" + msg +"</div>");
	}

	public void updateChat_send(String msg) {
		appendToPane(textPane, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 59%; '></td>"
				+ "<td style='width: 40%; background-color: #0084ff;'>" + msg 
				+"</td> </tr>"
				+ "</table>");
	}
	
	public void updateChat_notify(String msg) {
		appendToPane(textPane, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 59%; '></td>"
				+ "<td style='width: 40%; background-color: #f1c40f;'>" + msg 
				+"</td> </tr>"
				+ "</table>");
	}
	
	// send html to pane
	public static void appendToPane(JTextPane tp, String msg){
		  HTMLDocument doc = (HTMLDocument)tp.getDocument();
		    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
		    try {
		    	
		      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
		      tp.setCaretPosition(doc.getLength());
		      
		    } catch(Exception e){
		      e.printStackTrace();
		    }
	}
	
	class ChatClient implements Runnable{
		private final String address = "localhost";
		private final int port = 9999;
		
		String username;
		String peer;
		Socket socket;
		DatagramSocket udp_socket;
		
		public ChatClient(String username, String peer) {
			this.username = username;
			this.peer = peer;
			try {
				this.socket = new Socket(InetAddress.getByName(address),port);
				this.udp_socket = new DatagramSocket();
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF(username);
				DatagramPacket packet =  new DatagramPacket(new byte[1], 1, InetAddress.getByName(address),6789);
				this.udp_socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			System.out.println("Chat client is running");
			// TODO Auto-generated method stub
			ObjectInputStream read = null;
			while(true) {
				try {
					read = new ObjectInputStream(this.socket.getInputStream());
					Object obj = read.readObject();
					if(obj instanceof Message) {
						System.out.println("received a message: "+ ((Message)obj).getMessage());
						String msg = ((Message)obj).getMessage();
						String sender = ((Message)obj).getName();
						updateChat_receive(sender, msg);
					}
					read.reset();
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		
		public void sendMessage(Object obj) throws IOException {
			ObjectOutputStream outPeer = new ObjectOutputStream(this.socket.getOutputStream());
			// only send text
			if (obj instanceof Message) {
				outPeer.writeObject(obj);
				outPeer.flush();
			} 
			// send attach file
//			else if (obj instanceof DataFile) {
//				outPeer.writeObject(obj);
//				outPeer.flush();
//			}
		}
		
	}
}
