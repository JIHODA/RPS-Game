package guiLEDGameV4.view;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import guiLEDGameV4.dao.PlayerDAO;
import guiLEDGameV4.dao.PlayerDAOInterface;
import guiLEDGameV4.dto.PlayerDTO;
import guiLEDGameV4.util.AESUtil;
import javax.swing.border.BevelBorder;

public class Login extends JFrame implements ActionListener{
	private JLabel logoLabel;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton loginBtn;
	private JButton registerBtn;
	private JButton rankBtn;
	
	private PlayerDTO player;
	private PlayerDAOInterface playerDao = PlayerDAO.getInstance();
	private AESUtil aes = new AESUtil();
	
	public Login() {
		start();
	}
	//GUI
	private void start() {
		
		rankBtn = new JButton("\uC804\uCCB4\uB7AD\uD0B9\uBCF4\uAE30");
		rankBtn.setBounds(12, 10, 131, 38);
		rankBtn.addActionListener(this);
		getContentPane().add(rankBtn);
		logoLabel = new JLabel("");
		logoLabel.setIcon(new ImageIcon(Login.class.getResource("/img/mainLogo.png")));
		logoLabel.setBounds(43, 10, 500, 340);
		getContentPane().add(logoLabel);
		
		getContentPane().setBackground(SystemColor.desktop);
		setBounds(EXIT_ON_CLOSE, ABORT, 600, 700);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.desktop);
		panel.setBounds(43, 360, 500, 250);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("굴림", Font.PLAIN, 18));
		textField.setBounds(159, 32, 200, 45);
		panel.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("굴림", Font.PLAIN, 18));
		passwordField.setBounds(159, 99, 200, 45);
		panel.add(passwordField);
		
		loginBtn = new JButton("로그인");
		loginBtn.setBounds(169, 154, 181, 45);
		loginBtn.addActionListener(this);
		panel.add(loginBtn);
		
		registerBtn = new JButton("회원가입");
		registerBtn.setBounds(179, 209, 159, 31);
		registerBtn.addActionListener(this);
		panel.add(registerBtn);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//메인
	public static void main(String[] args) {
		new Intro();
		
		new Login();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 회원가입
		if(registerBtn.equals(e.getSource()) ) {
			this.setVisible(false);
			new Register();
			
		}
		// 로그인
		if(loginBtn.equals(e.getSource())) {
			player = new PlayerDTO();
			try {
				if (loginCheck() == 1) {
					
					playerDao.createUserFile(player);
					this.setVisible(false);
					 Game client = new Game(player);
					 client.start();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}
		
		if(rankBtn.equals(e.getSource())) {
			new Ranking();
			setVisible(false);
		}
		
	}
	
	public int loginCheck() throws Exception {
		String email = textField.getText();
		String pass = passwordField.getText();
		String id;
		if(textField.getText().indexOf("@") == -1) {
			JOptionPane.showMessageDialog(null, "이메일 형식이 아닙니다.");
			return 0;
		}
		id = email.substring(0, email.indexOf('@'));
		
		if(id.equals(playerDao.getValue(id, "USER ID"))) {
			if(pass.equals(aes.decrypt(playerDao.getValue(id, "USER PW")) )) {
				JOptionPane.showMessageDialog(null, "로그인 성공");
				JOptionPane.showMessageDialog(null, "마지막 로그인 : "+playerDao.getValue(id, "LastLogin"));
				String loginHistory = new SimpleDateFormat("yyyy년MM월dd일 a hh:mm").format(new Date());
				player.setJoin(playerDao.getValue(id, "JOIN"));
				player.setEmail(email);
				player.setId(id);
				player.setPw(pass);
				player.setWin(Integer.parseInt(playerDao.getValue(id, "Win")));
				player.setDraw(Integer.parseInt(playerDao.getValue(id, "Draw")));
				player.setLose(Integer.parseInt(playerDao.getValue(id, "Lose")));
				player.setWinRate((int)Double.parseDouble(playerDao.getValue(id, "WinRate")));
				player.setLastLogin(loginHistory);
				return 1;
			}else {
				JOptionPane.showMessageDialog(null, "비밀번호가 틀렸습니다.");
			}
		}else {
			JOptionPane.showMessageDialog(null, "아이디가 틀렸습니다.");
		}
		return 0;
	}
	
}
