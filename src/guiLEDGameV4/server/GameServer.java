package guiLEDGameV4.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GameServer {

	private List<ClientThread> clients = new ArrayList<>();
	private CountDownLatch gestureLatch = new CountDownLatch(2);
	private ServerSocket serverSocket;

	public static void main(String[] args) {
		GameServer gameServer = new GameServer();
		gameServer.connect();
	}

	private void connect() {
		try {
		serverSocket = new ServerSocket(9887);
		System.out.println("서버 시작");
		
		while(true) {
			Socket socket  = serverSocket.accept();
			System.out.println("플레이어 연결");
			
			ClientThread clientThread = new ClientThread(socket);
			clients.add(clientThread);
			
			new Thread(clientThread).start();
			
			clients.get(0).send("wait");
			
			if(clients.size() == 2) {
				clients.get(0).send("GameStart");
				clients.get(1).send("GameStart");
				break;
			}
		}
		gameStart();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void gameStart() {
		while(true) {
			try {
				ClientThread client1 = clients.get(0);
				ClientThread client2 = clients.get(1);
				
				String player1 = client1.getSelect(gestureLatch);
				String player2 = client2.getSelect(gestureLatch);
				
				if(player1 != null && player2 != null) {
					client1.send(fightP1(player1, player2));
					Thread.sleep(150);
					client2.send(fightP2(player1, player2));
					Thread.sleep(150);
					Thread.sleep(2500);
					client1.send("reset");
					Thread.sleep(150);
					client2.send("reset");
					
					gestureLatch = new CountDownLatch(2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String fightP1(String player1, String player2) {
		if(player1.equals("가위") && player2.equals("보") || player1.equals("바위") && player2.equals("가위") || player1.equals("보") && player2.equals("바위")) {
			return "승";
		}else if(player1.equals(player2)) {
			return "무";
		}else {
			return "패";
		}
	}
	
	private String fightP2(String player1, String player2) {
		if(player2.equals("가위") && player1.equals("보") || player2.equals("바위") && player1.equals("가위") || player2.equals("보") && player1.equals("바위")) {
			return "승";
		}else if(player2.equals(player1)) {
			return "무";
		}else {
			return "패";
		}
	}
	
	private class ClientThread implements Runnable {
		private Socket socket;
		private DataInputStream dis;
		private DataOutputStream dos;
		private String select;
		
		public ClientThread(Socket clientSocket){
			this.socket = clientSocket;
			try {
				this.dis = new DataInputStream(socket.getInputStream());
				this.dos = new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} // ClientThread 생성자 끝
		
		@Override
		public void run() {
			try {	
				while(true) {
					String msg = dis.readUTF();
					if(msg != null) {
						System.out.println(msg);
						if(msg.equals("가위") || msg.equals("바위") || msg.equals("보")) {
							select = msg;
							cast(msg, this);
							gestureLatch.countDown();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					dis.close();
					dos.close();
					socket.close();
					clients.remove(this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void send(String msg) {
			try {
				dos.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public String getSelect(CountDownLatch latch) {
			try {
				latch.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return select;
		}
	}

	public void cast(String msg, ClientThread clientThread) {
		for(ClientThread ct : clients) {
			if(ct != clientThread ) {
				ct.send(msg);
			}
		}
	}
}
