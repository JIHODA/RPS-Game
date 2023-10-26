package guiLEDGameV4.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import guiLEDGameV4.util.AESUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PlayerDTO {
	private String join;
	private String email;
	private String id;
	private String pw;
	private int win;
	private int draw;
	private int lose;
	private double winRate;
	private int gameCount;
	private String lastLogin = "첫 로그인 입니다.";
	private AESUtil aes = new AESUtil();
	
	public PlayerDTO(String email, String id, String pw) {
		this.join = new SimpleDateFormat("yy년MM월dd일 a hh:mm").format(new Date());
		this.email = email;
		this.id = id;
		this.pw = pw;
	}
	
	public String getWinRate() {
		return String.format("%.2f", ( win /( (double)win + lose ) )*100);
	}
	
	public void showRecord() {	// 해당 플레이어의 총 전적을 출력하는 메서드
		String now = new SimpleDateFormat("yy년MM월dd일").format(new Date());	// 오늘 날짜를 **년 **월 **일 로 포맷한 코드
		System.out.println("오늘 날짜 : "+now);		// 날짜, 전적 승률 출력
		System.out.println("[ "+id+"님의 전적 ]");
		System.out.println("승 : "+win);
		System.out.println("무 : "+draw);
		System.out.println("패 : "+lose);
		System.out.println("승률 : "+getWinRate());
		System.out.println("게임수 : "+gameCount);
	}

	@Override
	public String toString() {
		try {
			return "JOIN:" + join + "\nUSER EMAIL:" + email + "\nUSER ID:" + id + "\nUSER PW:" +aes.encrypt(pw) + "\nWin:" + win + "\nDraw:"
					+ draw + "\nLose:" + lose + "\nWinRate:" + getWinRate() + "\nGameCount:" + gameCount + "\nLastLogin:"
					+ lastLogin;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
