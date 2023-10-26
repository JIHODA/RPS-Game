package guiLEDGameV4.view;

import java.awt.Color;
import java.awt.SystemColor;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableRowSorter;

import guiLEDGameV4.dao.PlayerDAO;
import guiLEDGameV4.dao.PlayerDAOInterface;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Ranking extends JFrame {
	private static PlayerDAOInterface playerDao = PlayerDAO.getInstance();
	private JPanel contentPane;
	private String[][] rankList;
	private String[] title = {"순위", "아이디", "점수"};
	private JTable table;
	private JTable table_1;
	private JTable table_2;
	public Ranking() {
		setBackground(new Color(0, 0, 0));
		rankList = rankList("WinRate",1);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(SystemColor.desktop);
		setBounds(EXIT_ON_CLOSE, ABORT, 600, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setVisible(true);

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel rankLogo = new JLabel("");
		rankLogo.setIcon(new ImageIcon(Ranking.class.getResource("/img/ranking.png")));
		rankLogo.setBounds(130, 10, 325, 306);
		contentPane.add(rankLogo);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.desktop);
		panel.setBounds(0, 0, 584, 661);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setForeground(SystemColor.menu);
		tabbedPane.setBorder(null);
		tabbedPane.setBackground(SystemColor.desktop);
		tabbedPane.setBounds(108, 328, 338, 177);
		panel.add(tabbedPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		tabbedPane.addTab("승률", null, scrollPane, null);
		
		table = new JTable(rankList, title);
		table.setMinimumSize(new Dimension(50, 144));
		table.setFont(UIManager.getFont("Viewport.font"));
		table.setForeground(SystemColor.window);
		table.setBackground(SystemColor.desktop);
		table.setAutoCreateRowSorter(true);
		TableRowSorter tbsorter1 = new TableRowSorter<>(table.getModel());
		scrollPane.setViewportView(table);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(null);
		tabbedPane.addTab("게임 수", null, scrollPane_1, null);
		rankList = rankList("GameCount",1);
		
		table_1 = new JTable(rankList,title);
		table_1.setMinimumSize(new Dimension(50, 144));
		table_1.setFont(UIManager.getFont("Viewport.font"));
		table_1.setForeground(SystemColor.window);
		table_1.setBackground(SystemColor.desktop);
		table_1.setAutoCreateRowSorter(true);
		scrollPane_1.setViewportView(table_1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(null);
		tabbedPane.addTab("승리 수", null, scrollPane_2, null);
		
		rankList = rankList("Win", 1);
		table_2 = new JTable(rankList, title);
		table_2.setMinimumSize(new Dimension(50, 144));
		table_2.setFont(UIManager.getFont("Viewport.font"));
		table_2.setForeground(SystemColor.window);
		table_2.setBackground(SystemColor.desktop);
		table_2.setAutoCreateRowSorter(true);
		scrollPane_2.setViewportView(table_2);
		
		JButton backBtn = new JButton("메뉴로 돌아가기");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new Login().setVisible(true);
				dispose();
			}
		});
		backBtn.setBounds(217, 556, 136, 34);
		panel.add(backBtn);
		
		
	}
	
	public String[][] rankList(String key, int n){
		File file = new File("D:\\Players");
		File[] fileList = file.listFiles();
		Map<String, Integer> map = playerDao.fileList(key);
		ArrayList<String> arrL = new ArrayList<>(map.keySet());
		
		arrL.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				if(n == 1)
					return map.get(o2).compareTo(map.get(o1));
				else
				return map.get(o1).compareTo(map.get(o2));
			}
		});
		String[][] rankList = new String[9][3];
		if(n == 1) {
			for(int i = 1; i <= 9; i++) {
				rankList[i-1][0] = i+"";
			}
		}else {
			for(int i = fileList.length,j=0; j <= 9; i--, j++) {
				rankList[j][0] = i+"";
			}
		}
		int idx = 0;
		for(String k : arrL) {
			rankList[idx][1] = k;
			rankList[idx][2] = ""+map.get(k);
			idx++;
			if (idx == 9)
				break;
		}
		return rankList;
	}
}
