package guiLEDGameV4.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import guiLEDGameV4.dto.PlayerDTO;
import guiLEDGameV4.util.AESUtil;

// 직접적으로 파일에 접근하는 클래스
public class PlayerDAO implements PlayerDAOInterface {
	private static PlayerDAOInterface instance;
	private File file = null;
	private String fileRootPath = "D:\\Players";
	private String  fileName = null;
	private BufferedReader br = null;
	private FileWriter fw = null;
	private AESUtil aes = new AESUtil();
	
	private PlayerDAO() {} 	//싱글톤으로 구현 기본생성자 private으로 막음
	
	public static PlayerDAOInterface getInstance() { // 인스턴스가 이미 있다면 참조를 리턴 아니면 인스턴스 생성
		if(instance == null) {
			instance = new PlayerDAO();
		}
		return instance;
	}
	
	// 파일 중복 체크 메서드 중복이면 true, 아니면 false
	@Override
	public boolean duplicationCheck(String id) {
		fileName = id+".txt";
		file = new File(fileRootPath,fileName);
		if(file.exists()) {
			return true;
		}
		return false;
	}
	
	// 프로그램의 플레이어 데이터를 외부파일로 생성해주는 메서드 => 회원가입, 게임전적 업데이트에 사용
	@Override
	public int createUserFile(PlayerDTO player) { // 가입 할 플레이어의 정보를 받고 파일 생성 => 생성 성공시 1,예외 -1 리턴
		fileName = player.getId()+".txt";  // 파일 이름을 유저의 아이디로 설정
		String date = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(new Date());	// 가입한 날짜를 위한 날짜 포맷
		File filePath = new File(fileRootPath);	// 가입된 유저의 정보가 저장될 경로 (D:\Player)
		file = new File(fileRootPath, fileName);	// 경로 하위에 플레이어 아이디를 파일명으로 지정
		
		if(!filePath.exists()) {	//Players 폴더가 없다면 
			filePath.mkdir(); // Players 폴더 생성
		}
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));	// 유저 파일에 정보입력을 위해 버퍼활용
			//파일에 유저 정보 쓰기
			bw.write(player.toString());
			
			bw.close();	//버퍼닫기
			return 1;	//가입성공 시 1 리턴
		} catch (Exception e) {	//예외처리
			e.printStackTrace();
			return -1; //예외 시 -1리턴
		}
	}
	
	// 플레이어의 정보와 찾고싶은 정보의 key 를 입력하면 그 key에 해당하는 value를 리턴
	@Override
	public String getValue(PlayerDTO player, String key) {  
		file = new File (fileRootPath,player.getId()+".txt");		// 유저의 정보가 담긴 파일 지정
		String result = "";		//	찾은 value를 담아 리턴하기위한 변수
		try {
			br = new BufferedReader(new FileReader(file));	// 유저 정보가 담긴 파일을 읽기위해 버퍼활용
			String search = "";	// 읽어들인 내용을 순회하기위해 임시변수 초기화
			while((search = br.readLine()) != null) {	// search에 br로 읽은 정보의 한줄씩 대입
				if(search.startsWith(key)) {		// 순회 중 만약 내가 찾을려고 하는 key로 시작하는 내용이 오면 
					result = search.substring(search.indexOf(":")+1,search.length());		// result에 key: 를 제외한 value만 대입
					break;	// 찾아냈으니 루프 종료
				}
			}
			br.close();
		}catch(IOException e) {	// 파일이 없을 때 예외처리, 
			e.printStackTrace();
		}
		return result; // 내가 원하는 key의 value를 담은 result 리턴
	}
	
	// getValue오버로딩 => DTO 대신 File 객체를 받아 그 파일의 찾고싶은 정보의 key를 주면 그 key에 해당하는 value리턴
	@Override
	public String getValue(File file, String key) { 
		this.file = file;		// 유저의 정보가 담긴 파일 지정
		String result = "";		//	찾은 value를 담아 리턴하기위한 변수
		try {
			br = new BufferedReader(new FileReader(file));	// 유저 정보가 담긴 파일을 읽기위해 버퍼활용
			String search = "";	// 읽어들인 내용을 순회하기위해 임시변수 초기화
			while((search = br.readLine()) != null) {	// search에 br로 읽은 정보의 한줄씩 대입
				if(search.startsWith(key)) {		// 순회 중 만약 내가 찾을려고 하는 key로 시작하는 내용이 오면 
					result = search.substring(search.indexOf(":")+1,search.length());		// result에 key: 를 제외한 value만 대입
					break;	// 찾아냈으니 루프 종료
				}
			}
			br.close();
		}catch(IOException e) {	// 파일이 없을 때 예외처리,
			e.printStackTrace();
		}
		return result; // 내가 원하는 key의 value를 담은 result 리턴
	}
	
	// getValue오버로딩 => DTO 대신 플레이어 ID를 받아 그 파일의 찾고싶은 정보의 key를 주면 그 key에 해당하는 value리턴
	@Override
	public String getValue(String id, String key) { 
		this.file = new File (fileRootPath,id+".txt");		// 유저의 정보가 담긴 파일 지정
		String result = "";		//	찾은 value를 담아 리턴하기위한 변수
		if(!file.exists()){
			return null;
		}
		
		try {
			br = new BufferedReader(new FileReader(file));	// 유저 정보가 담긴 파일을 읽기위해 버퍼활용
			String search = "";	// 읽어들인 내용을 순회하기위해 임시변수 초기화
			while((search = br.readLine()) != null) {	// search에 br로 읽은 정보의 한줄씩 대입
				if(search.startsWith(key)) {		// 순회 중 만약 내가 찾을려고 하는 key로 시작하는 내용이 오면 
					result = search.substring(search.indexOf(":")+1,search.length());		// result에 key: 를 제외한 value만 대입
					break;	// 찾아냈으니 루프 종료
				}
			}
			br.close();
		}catch(IOException e) {	// 파일이 없을 때 예외처리
			e.printStackTrace();
		}
		return result; // 내가 원하는 key의 value를 담은 result 리턴
	}
	
 // 유저 아이디와 동일한 파일의 정보를 한줄씩 읽어서 문자열로 통째로 저장한 뒤 리턴시켜주는 메서드
	@Override
	public String usersInfo(String id) { // 유저 아이디를 입력받아 그 유저의 정보를 : 를 기준으로 나눠서 문자열로 리턴해줌
		String playerInfo = "";
		String list = "";
		file = new File (fileRootPath,id+".txt");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF8"));
			String idLine = "";
			while((idLine = br.readLine()) != null) {
				if (idLine.trim().length() > 0) {
					playerInfo += idLine + "\n";
				}
			}
			br.close();
			String[] idList = playerInfo.split(":");
			list = Arrays.toString(idList);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// 게임 종료 시 데이터 저장 이전 데이터 누적
	@Override
	public void savePlayer(PlayerDTO player) { // 종료 시 로그인한 유저의 정보를 받아서 수행한다.
		String id = player.getId()+".txt";			// 파일이름 유저의 아이디로 지정
		String[] key = { "Win", "Lose", "Draw", "GameCount" };		// 세이브 할 key 가 담긴 String 배열 초기화
		int[] newData = { player.getWin(), player.getLose(), player.getDraw(), player.getGameCount() };	// 업데이트 할 value 가 담긴 정수배열 초기화
		for (int i = 0; i < key.length; i++) { 	// 루프를 돌려서
			cover(key[i], newData[i], id);		// coverStats 메서드를 이용해서 Win, Lose, Draw 에 이전 값과 새로운 값을 더해서 업데이트해준다
		}
	}
	
	// Win, Lose, Draw,GameCount 의 값을 이전 값과 새로운 값을 더해서 갱신해주고 최신 전적으로 평균을 구해서 갱신해준다.
	@Override
	public void cover(String key, int newData, String id) {
		File file = new File(fileRootPath, id);
		int win = 0;
		int lose = 0;
		try {
			br = new BufferedReader(new FileReader(file)); // 유저 file 을 버퍼로 모아서 문자열로 리턴해준다
			String newFile = ""; // 갱신 된 정보를 담을 변수
			String temp;	// 순회하기위한 임시변수
			while ((temp = br.readLine()) != null) {		//루프를 돌려서 readLine() 을 이용해 한줄씩 temp에 대입한다
				if (temp.startsWith(key)) {		// 만약 읽어들인 한줄의 시작이 파라미터로 받은 key 로 시작한다면
					int oldData = Integer.parseInt(temp.substring(temp.indexOf(":") + 1, temp.length()));		// 이전 key의 value는 oldData에 저장해놓고 
					if(key.equals("Win")) {	// 평균을 위한 win과 lose값 저장
						win = oldData + newData;
					}else if(key.equals("Lose")){
						lose = oldData + newData;
					}
					newFile += (temp.substring(0, temp.indexOf(":") + 1) + (oldData + newData) + "\n"); // 이전 value와 새로운 value를 더해서 Key:value\n 형태로 누적한다
					continue;
				}else if(temp.startsWith("WinRate")){
					newFile += (temp.substring(0, temp.indexOf(":") + 1) + String.format("%.2f", ( win /( (double)win + lose ) )*100 )+"\n");
				}
				newFile += (temp + "\n");
			}
    
			fw = new FileWriter(file); // FileWriter로 유저 파일에 접근하는 객체를 만든다
			fw.write(newFile);	// 갱신된 정보로 업데이트한다.
    
			fw.close();	// FileWriter를 닫는다
			br.close();	//BufferedReader 를 닫는다
    
		} catch (Exception e) { // 예외 처리를 한다
			e.printStackTrace();
		}
	}
	
	// 플레이어의 정보에서 변경할 key를 주면 value를 변경해주는 메서드
	@Override
	public void setValue(PlayerDTO player, String key, String value) {
		File file = new File(fileRootPath, player.getId()+".txt");
		
		try {
			br = new BufferedReader(new FileReader(file));
			String completeChange = "";
			String temp;
			while((temp = br.readLine()) != null) {
				if(temp.startsWith(key)) {
					completeChange += temp.replace(temp.substring(temp.indexOf(':')+1), value)+"\n";
					continue;
				}
				completeChange += (temp+"\n");
			}
			System.out.println(completeChange);
			fw = new FileWriter(file);
			fw.write(completeChange);
			br.close();
			fw.close();
			System.out.println(key+" 변경완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 플레이어의 정보에서 변경할 key를 주면 value를 변경해주는 메서드
	@Override
	public void setValue(String id, String key, String value) {
		File file = new File(fileRootPath, id+".txt");
		try {
			br = new BufferedReader(new FileReader(file));
			String completeChange = "";
			String temp;
			while((temp = br.readLine()) != null) {
				if(temp.startsWith(key)) {
					completeChange += temp.replace(temp.substring(temp.indexOf(':')+1), value)+"\n";
					continue;
				}
				completeChange += (temp+"\n");
			}
			fw = new FileWriter(file);
			fw.write(completeChange);
			br.close();
			fw.close();
			System.out.println(key+" 변경완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 플레이어 아이디를 key, 파라미터의 주어진 key의 벨류를 map에 key : value 로 담아서 map리턴
	@Override
	public Map<String, Integer> fileList(String v){	
		Map<String, Integer> map = new HashMap<>();
		File[] fileList = new File(fileRootPath).listFiles();
		String userId = null;
		int value = 0;
		for(int i = 0; i < fileList.length; i++) {
			userId = getValue(fileList[i],"USER ID");
			if(v.equals("WinRate")) {
				value = (int)Double.parseDouble(getValue(fileList[i], v));
				map.put(userId, value);
				continue;
			}
			value = Integer.parseInt(getValue(fileList[i], v));
			map.put(userId, value);
		}
		return map;
	}
	
	// 입력된 key를 기준으로 랭킹출력 2번째 파라미터로 0,1 을 주게되면 오름차,내림차 정렬 
	@Override
	public void sortRank(String value, int n) {
		Map<String, Integer>map =  fileList(value);
		ArrayList<String> arrL = new ArrayList(map.keySet());
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
		int rank = 1;
		boolean count = true;
		if(n == 0) {
			rank = arrL.size();
			count = false;
		}
		int RankCount = 1;
		System.out.println("[ Rate ]----------------------------------------------------------------------------------------");
		for(String k : arrL) {
			if(value.equals("WinRate")) {
				System.out.printf("[RANK %2d] %15s 님\t %8s : %5d %% \n", rank, k, value, map.get(k));
			}else {
			System.out.printf("[RANK %2d] %15s 님\t %8s : %5d \n", rank, k, value, map.get(k));
			}
			System.out.println("-----------------------------------------------------------------------------------------------------");
			if(count) {
				rank++;
			}else
			rank--;
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			RankCount++;
			if(RankCount > 10) {
				break;
			}
		}
	}
	
	public Map<String, Integer> sortRankFile(String value, int n) {
		Map<String, Integer>map =  fileList(value);
		ArrayList<String> arrL = new ArrayList(map.keySet());
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
		return map;
	}
	
	// 플레이서 파일 삭제 메서드 => 회원탈퇴에 사용
	@Override
	public void deletePlayer(PlayerDTO player) {
		this.fileName = player.getId()+".txt";
		this.file = new File(fileRootPath,fileName);
		
		if(file.exists()) {
			System.out.println(file.delete()); 
		}
	}
	
	// 플레이어 아이디로 삭제
	@Override
	public void deletePlayer(String id) {
		this.fileName = id+".txt";
		this.file = new File(fileRootPath,fileName);
		
		if(file.exists()) {
			System.out.println(file.delete()); 
		}
	}
	
	// 모든 플레이어 아이디 조회
	public void checkPlayer() {
		this.file = new File(fileRootPath);
		File[] files= file.listFiles();
		
		for(File f : files) {
			String playerId=f.getName().substring(0,f.getName().indexOf("."));
			System.out.println(playerId);
		}
	}
	


}

