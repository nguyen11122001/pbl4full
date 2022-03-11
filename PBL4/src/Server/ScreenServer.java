
package Server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.imageio.ImageIO;

public class ScreenServer{
	Vector<Xuly> clients = new Vector<Xuly>();
//	Vector<receive> recei = new Vector<receive>();
	Socket soc1;
	ServerSocket server ;
	public static void main(String args[]) {
		new ScreenServer();
	}

	public ScreenServer() {
		try {
			System.out.print("Started111!!!");
			server = new ServerSocket(7749);
			System.out.println(" Start accept!!");
			soc1 = server.accept();
			

			while (true) {
				System.out.println(" Start accept!!");
				Socket soc = server.accept();
				Xuly x = new Xuly(soc, this, soc1);
				clients.add(x);
				x.start();

				
			}
		} catch (Exception e) {

		}
	}
}

class Xuly extends Thread {
	ScreenServer cs;
	Socket sock, sockgui;
	String name;

	public Xuly(Socket soc, ScreenServer cs, Socket socgui) {
		this.sock = soc;
		this.cs = cs;
		this.sockgui = socgui;
	}

	public void run() {
		try {
			

			InputStream din = sockgui.getInputStream();
			System.out.println("111!!!");
			int status=0;
			while (status<10) {
				if(cs.clients.size()>0) {
							// Nhan Screen
							System.out.println("222!!!");
							BufferedImage img = ImageIO.read(din);
							
							if(img!=null) {status=0;
							for (Xuly c : cs.clients) {
			
									System.out.print("333!!!");
				
										try {
			//								c.sock = cs.server.accept();
											OutputStream dout = c.sock.getOutputStream();
											ByteArrayOutputStream ous = new ByteArrayOutputStream();
											ImageIO.write(img, "png", ous);
											dout.write(ous.toByteArray());
											System.out.println("Startednnn!!!");
											dout.flush();
			//								c.sock.close();
			//								cs.clients.remove(c);
			//								System.out.println(cs.clients.size());
			
										} catch (Exception e1) {
											e1.printStackTrace();
										}						
								
								
							}
							try {
			                    Thread.sleep(10);
			                } catch (Exception e) {
			                	e.printStackTrace();
			                }
							}else {
								status++;
							}
				}
				
			}
			cs.clients.clear();
			cs.soc1.close();
			
		} catch (Exception e) {
		}
	}
}
