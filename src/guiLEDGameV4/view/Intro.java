package guiLEDGameV4.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Intro extends JFrame {
	private final JLabel lblNewLabel = new JLabel("");

	public Intro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 720);
		getContentPane().setLayout(null);
		lblNewLabel.setIcon(new ImageIcon(Intro.class.getResource("/img/introV4.gif")));
		lblNewLabel.setBounds(0, 0, 704, 681);
		setLocationRelativeTo(null);
		getContentPane().add(lblNewLabel);
		setVisible(true);
		
		try {
			Thread.sleep(7000);
		} catch (Exception e) {
		}
		
		dispose();
		
	}

}
