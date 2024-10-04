package main;

import java.text.SimpleDateFormat;	// 시간을 표현하는 형식을 지정하기 위해 가져옴
import java.util.Date;				// 현재 시간(년도, 월, 일) 구하기 위해 Date클래스를 가져옴
import java.util.Scanner;			// 입력받기 위해 가져옴
import timetable.TimeTable;			// 시간표-학식 서비스 ~
import community.Server;			// 커뮤니티 서비스와의 연동을 위해 가져옴
import testmanager.Marking;			// 채점-분석 서비스 ~


public class Menu {
	// (1) 객체 설정 (필드, 생성자)
	private int ID;
	private int PW;
	private Date now;
	private SimpleDateFormat formatter;
	private String presentTime;
	private long beforeTime;
	
	public Menu(int ID, int PW) { 
		this.ID = ID;
		this.PW = PW;
		this.now = new Date();										// 현재 시간 구하기
		this.formatter = new SimpleDateFormat("yyyy년 MM월 dd일");		// 원하는 형식 지정
		this.presentTime = formatter.format(now);					// 현재 시간을 원하는 형식으로 저장
	}
	
	// (2) get(), set()
	public int getID() { return this.ID; }
	public int getPW() { return this.PW; }
	public void setPW(int PW) { this.PW = PW; }
	
	// (3) 프로그램의 시작함수
	public void StartPage() {
		Scanner sc = new Scanner(System.in);
		System.out.println();
		System.out.println("✿ ✿ ✿ " + presentTime + " ✿ ✿ ✿");
		System.out.println("✿ ✿ ✿ " + this.getID() + "님 반갑습니다! ✿ ✿ ✿");
		this.Menu();
	}
	
	// (4) 메뉴함수
	// - 사용자의 입력에 따라 메뉴이동이 달라지며, 무한반복문으로 프로그램을 계속 실행함
	// - 각 서비스의 객체를 생성하고, 객체의 함수를 통해 각 서비스로 이동함
	public void Menu() {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println();
			System.out.println("✿ ✿ ✿ 메뉴 ✿ ✿ ✿");
			System.out.println("1.시간표 🏫       2.커뮤니티🙂       3.채점&분석서비스✍       4.비밀번호변경🔒       5.종료🖐️");
			System.out.print("✿ 메뉴입력 → ");
			int choice = sc.nextInt();
			// - 입력이 4일 경우, 프로그램 종료 (Thread)
			if (choice == 5) {
				System.out.println("우리의 타임, 나만의 타임!");
				try { Thread.sleep(2000); }
				catch (InterruptedException e) { e.printStackTrace(); }
				System.out.println("부산중앙여자고등학교의 𝑪𝑬𝑻𝑨, 지금 종료합니다!");
				try { Thread.sleep(1000); }
				catch (InterruptedException e) { e.printStackTrace(); }
				System.out.println("🌸 🏫 💯✍ 👑  𝑪𝑬𝑻𝑨  👑 ✍ 💯  🏫 🌸");
				break;
			}
			else {
				switch (choice) {
				// - 입력이 1일 경우, 시간표 서비스로 이동
				case 1:
					TimeTable t = new TimeTable(this);
					t.timetable();
					break;
				// - 입력이 2일 경우, 커뮤니티 서비스로 이동
				case 2:
					Server server = new Server();
					server.start();
					break;
					// - 입력이 3일 경우, 채점-분석 서비스로 이동
				case 3:
					Marking m = new Marking(this);
					m.marking();
					break;
				// - 입력이 4일 경우, 비밀번호 변경
				case 4:	
					while(true) {
						System.out.print("이전 PW : ");
						int PwWrite = sc.nextInt();
						 
						if (PwWrite != this.getPW()) {
							System.out.println("\n※ 잘못된 PW를 입력하였습니다. ※\n");
						}
						else {
							System.out.print("새로운 PW : ");
							int newPw = sc.nextInt();
							this.setPW(newPw);
							System.out.println("비밀번호가 성공적으로 변경되었습니다! 새로운 PW: " + this.getPW());
							break;
						}
					}
					break;
				// - 이상한 값이 입력으로 들어올 경우, 예외처리
				default:
					System.out.println();
					System.out.println("※ 잘못된 입력입니다! ※");
					break;
				}
			}
		}
	}
}