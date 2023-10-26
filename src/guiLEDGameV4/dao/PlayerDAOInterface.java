package guiLEDGameV4.dao;

import java.io.File;
import java.util.Map;

import guiLEDGameV4.dto.PlayerDTO;

public interface PlayerDAOInterface {

	// 파일 중복 체크 메서드 중복이면 true, 아니면 false
	boolean duplicationCheck(String id);

	// 프로그램의 플레이어 데이터를 외부파일로 생성해주는 메서드 root는 D:\Players 파일은 루트 하위에 생성된다
	int createUserFile(PlayerDTO player);

	// 플레이어 파일데이터 중 알고싶은 value가 있다면 PlayerDTO와 key 를 주면 그 key의 value를 리턴해줌
	//로그인에 사용
	String getValue(PlayerDTO player, String key); 

	// getValue오버로딩 => DTO 대신 File 객체를 받아 그 파일의 찾고싶은 정보의 key를 주면 그 key에 해당하는 value리턴
	// 랭킹 value 취합에 사용함
	String getValue(File file, String key); 
	
	// getValue오버로딩 => DTO 대신 플레이어 ID를 받아 그 파일의 찾고싶은 정보의 key를 주면 그 key에 해당하는 value리턴
	// 유저 정보 검색기능 추가 예정
	String getValue(String id, String key); 

	// 플레이어의 정보에서 변경할 key를 주면 value를 변경해주는 메서드 (변경할플레이어, 변경할 key, 원하는 변경값)
	// 비밀번호 변경 또는 닉네임 추가 예정
	void setValue(PlayerDTO player, String key, String value); 
	
	// setValue 오버로딩 DTO대신 id를 직접 입력
	void setValue(String id, String key, String value);

	// 플레이어 아이디를 key, 파라미터의 주어진 key의 값을 value => map에 key : value 로 담아서 map리턴 => 랭킹 정렬메서드에 사용
	Map<String, Integer> fileList(String v);

	// 입력된 key를 기준으로 랭킹출력 2번째 파라미터로 0,1 을 주게되면 오름차,내림차 정렬 => 랭킹에 사용
	void sortRank(String value, int n);
	
	Map<String, Integer> sortRankFile(String value, int n);

	// 플레이어 파일 삭제 메서드 => 회원탈퇴에 사용
	void deletePlayer(PlayerDTO player);

	// 플레이어 이름으로 삭제 => 관리자용 삭제 메서드
	void deletePlayer(String id);
	
	// 모든플레이어 조회
	void checkPlayer();
	
	// 유저 아이디와 동일한 파일의 정보를 한줄씩 읽어서 문자열로 통째로 저장한 뒤 리턴시켜주는 메서드
	// 사용안함 공부용
	String usersInfo(String id); 

	// 게임 종료 후 플레이어의 정보를 세이브하는 메서드
	// 사용안함 공부용
	void savePlayer(PlayerDTO player);
	
	// Win, Lose, Draw,GameCount 의 값을 이전 값과 새로운 값을 더해서 갱신해주고 최신 전적으로 평균을 구해서 갱신해준다.
	//사용안함 공부용
	void cover(String key, int newData, String id);
}