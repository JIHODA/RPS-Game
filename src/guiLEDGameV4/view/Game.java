package guiLEDGameV4.view;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import guiLEDGameV4.dao.PlayerDAO;
import guiLEDGameV4.dto.PlayerDTO;

public class Game extends JFrame implements ActionListener {
	private PlayerDTO player;
	private JLabel gameLogo;
	private JLabel rpsIcon1;
	private JLabel rpsIcon2;
	private JLabel resultLabel;
	private JPanel panel;
	private JButton scissorsBtn;
	private JButton rockBtn;
	private JButton paperBtn;
	private URL[] rpsImg = new URL[] { Game.class.getResource("/img/rock.png"),
			Game.class.getResource("/img/scissors.png"), Game.class.getResource("/img/paper.png") };

	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private boolean hasSelected = false;
	private Timer timer1;
	private Timer timer2;
	
	private int selectO;
	 
	public Game(PlayerDTO playe) {
		this.player = player;
	}
	
	private void GameGUI() {
		getContentPane().setBackground(SystemColor.desktop);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1036, 670);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		resultLabel = new JLabel("");
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resultLabel.setFont(new Font("굴림", Font.PLAIN, 18));
		resultLabel.setForeground(SystemColor.window);
		resultLabel.setBounds(329, 467, 361, 80);
		getContentPane().add(resultLabel);

		gameLogo = new JLabel("");
		gameLogo.setIcon(new ImageIcon(Game.class.getResource("/img/game.png")));
		gameLogo.setBounds(339, 10, 351, 621);
		getContentPane().add(gameLogo);

		rpsIcon1 = new JLabel("");
		rpsIcon1.setBounds(12, 10, 300, 300);
		getContentPane().add(rpsIcon1);

		rpsIcon2 = new JLabel("");
		rpsIcon2.setBounds(708, 10, 300, 300);
		getContentPane().add(rpsIcon2);

		panel = new JPanel();
		panel.setBackground(SystemColor.desktop);
		panel.setBounds(12, 443, 328, 132);
		getContentPane().add(panel);
		panel.setLayout(null);

		scissorsBtn = new JButton("가위");
		scissorsBtn.setBounds(12, 41, 71, 61);
		scissorsBtn.addActionListener(this);
		panel.add(scissorsBtn);

		rockBtn = new JButton("바위");
		rockBtn.setBounds(133, 41, 71, 61);
		rockBtn.addActionListener(this);
		panel.add(rockBtn);

		paperBtn = new JButton("보");
		paperBtn.setBounds(245, 41, 71, 61);
		paperBtn.addActionListener(this);
		panel.add(paperBtn);

		timer1 = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int idx = (int)(Math.random()*rpsImg.length);
				rpsIcon1.setIcon(new ImageIcon(rpsImg[idx]));
			}
		});
		timer2 = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int idx = (int)(Math.random()*rpsImg.length);
				rpsIcon2.setIcon(new ImageIcon(rpsImg[idx]));
			}
		});
		
		timer1.start();
		timer2.start();
		disableButtons();

	
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(rockBtn)) {
			handleSelection("바위");
			timer1.stop();
			rpsIcon1.setIcon(new ImageIcon(rpsImg[0]));
		} else if (e.getSource().equals(paperBtn)) {
			handleSelection("보");
			timer1.stop();
			rpsIcon1.setIcon(new ImageIcon(rpsImg[2]));
		} else if (e.getSource().equals(scissorsBtn)) {
			handleSelection("가위");
			timer1.stop();
			rpsIcon1.setIcon(new ImageIcon(rpsImg[1]));
		}
	}

	public void start() {
		try {
			socket = new Socket("localhost", 9887);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());

			GameGUI();
			startListening();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startListening() {
		Thread thread = new Thread(() -> {
			try {
				while (true) {
					String message = dis.readUTF();
					if (message != null) {
						handleReceivedMessage(message);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}

	private void handleReceivedMessage(String message) {
		// 받은 메시지를 처리하는 로직을 작성합니다.
		System.out.println(message);
		if (message.equals("GameStart")) {
			hasSelected = false;
			enableButtons();
			resultLabel.setText("가위, 바위, 보 중에 선택하세요.");
		} else if (message.equals("wait")) {
			hasSelected = false;
			disableButtons();
			resultLabel.setText("상대방 기다리는 중...");
		} else if (message.equals("가위") || message.equals("바위") || message.equals("보")) {
			switch (message) {
			case "가위":
				selectO = 1;
				break;
			case "바위":
				selectO = 0;
				break;
			case "보":
				selectO = 2;
				break;
			} 
		}else if(message.equals("승") || message.equals("무") || message.equals("패") ) {
			System.out.println(selectO);
			switch(message) {
			case "승": 
				timer2.stop();
				rpsIcon2.setIcon(new ImageIcon(rpsImg[selectO]));
				resultLabel.setText("승리했습니다 !!!!");
				break;
			case "무": 
				timer2.stop();
				rpsIcon2.setIcon(new ImageIcon(rpsImg[selectO]));
				resultLabel.setText("무승부입니다 !!!!");
				break;
			case "패": 
				timer2.stop();
				rpsIcon2.setIcon(new ImageIcon(rpsImg[selectO]));
				resultLabel.setText("패배했습니다 ㅜㅜ");
				break;
			}
		}else if(message.equals("reset")) {
			timer1.start();
			timer2.start();
			enableButtons();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sendMessage("reset");
		}
		
	}

	private void disableButtons() {
		rockBtn.setEnabled(false);
		paperBtn.setEnabled(false);
		scissorsBtn.setEnabled(false);
	}

	private void enableButtons() {
		rockBtn.setEnabled(true);
		paperBtn.setEnabled(true);
		scissorsBtn.setEnabled(true);
	}

	private void sendMessage(String message) {
		try {
			dos.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleSelection(String selection) {
		if (!hasSelected) {
			sendMessage(selection);
			disableButtons();
			resultLabel.setText("상대방의 선택을 기다리는 중...");
		}
	}

}
