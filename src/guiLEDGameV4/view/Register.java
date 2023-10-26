package guiLEDGameV4.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import guiLEDGameV4.dao.PlayerDAO;
import guiLEDGameV4.dto.PlayerDTO;
import guiLEDGameV4.util.AESUtil;

public class Register extends JFrame {
	private JTextField idField;
	private JPasswordField pwField;
	private PlayerDTO player;
	private AESUtil aes;
	private PlayerDAO playerDao;

	public Register() {
		getContentPane().setBackground(SystemColor.desktop);
		getContentPane().setLayout(null);
		
		JLabel registerLogo = new JLabel("");
		registerLogo.setIcon(new ImageIcon(Register.class.getResource("/img/register.png")));
		registerLogo.setBounds(109, 25, 380, 330);
		getContentPane().add(registerLogo);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.desktop);
		panel.setBounds(109, 365, 380, 241);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel emailLabel = new JLabel("EMAIL");
		emailLabel.setFont(new Font("Serif", Font.PLAIN, 16));
		emailLabel.setForeground(SystemColor.window);
		emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
		emailLabel.setBounds(161, 10, 65, 27);
		panel.add(emailLabel);
		
		idField = new JTextField();
		idField.setBounds(97, 35, 200, 45);
		panel.add(idField);
		idField.setColumns(10);
		
		pwField = new JPasswordField();
		pwField.setBounds(97, 113, 200, 45);
		panel.add(pwField);
		
		JLabel pwLabel = new JLabel("PASSWORD");
		pwLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pwLabel.setForeground(Color.WHITE);
		pwLabel.setFont(new Font("Serif", Font.PLAIN, 16));
		pwLabel.setBounds(150, 90, 94, 27);
		panel.add(pwLabel);
		
		JButton registerBtn = new JButton("회원가입");
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player = new PlayerDTO();
				if(emailCheck(player) == 1) {	// 이메일 체크 성공시 1리턴 그 외 다이얼로그 띄우고 0리턴
					JOptionPane.showMessageDialog(null, "가입 성공");
					playerDao.getInstance().createUserFile(player);	// 성공시 파일 생성
					new Login().setVisible(true);
					dispose();
				}
				
			}
		});
		registerBtn.setBounds(107, 168, 180, 45);
		panel.add(registerBtn);
		start();
	}
	
	private void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(EXIT_ON_CLOSE, ABORT, 600, 700);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private int emailCheck(PlayerDTO player) {
		if(idField.getText().indexOf("@") != -1) {	//	이메일의 형식이 맞다면
			player.setEmail(idField.getText());
			player.setId(player.getEmail().substring(0, player.getEmail().indexOf("@")));
			player.setPw(pwField.getText());
			
			/*
			 * if(playerDao.duplicationCheck(player.getId())) {
			 * JOptionPane.showMessageDialog(null, "아이디가 중복되었습니다."); return 0; }
			 */
			
			if(player.getId().length() >= 8 && player.getId().length() <= 12) {	// 그 id의 길이가 8~12 사이인지 확인하고 맞다면
				char[] idArr = player.getId().toCharArray();	// id를 char배열로 변경
				int uCnt = 0;	// 대문자 카운트
				int nCnt = 0;	// 숫자 카운트
				for(char c : idArr) {	// 순회하며
					if(Character.isUpperCase(c))	//대문자 일 때
						uCnt++;	// 대문자 카운트 증가
					if(Character.isDigit(c))	// 숫자 일 때
						nCnt++;	// 숫자 카운트 증가
					if(uCnt > 0 && nCnt > 0)	// 대문자와 숫자가 모두 1 이상이 됐을 때
						return 1;	// 이메일 검증 통과
				}
				JOptionPane.showMessageDialog(null, "대문자와 숫자 1개 이상 포함해주세요 !");
				return 0;
			}else {
				JOptionPane.showMessageDialog(null, "아이디 길이를 확인해주세요 !");
				return 0;
			}
		}else {
			JOptionPane.showMessageDialog(null, "이메일 형식이 아닙니다. 다시 입력해주세요 !");
			return 0;
		}
	}

}
