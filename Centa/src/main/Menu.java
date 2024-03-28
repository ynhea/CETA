package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import community.Server;
import testmanager.Marking;
import timetable.TimeTable;


public class Menu {
	private int ID;
	private int PW;
	private Date now;
	private SimpleDateFormat formatter;
	private String presentTime;
	private long beforeTime;
	
	public Menu(int ID, int PW) { 
		this.ID = ID;
		this.PW = PW;
		this.now = new Date();	// 현재 시간 구하기
		this.formatter = new SimpleDateFormat("yyyy년 MM월 dd일");		// 원하는 형식 지정
		this.presentTime = formatter.format(now);	// 현재 시간을 원하는 형식으로 저장
	}
	
	public int getID() { return this.ID; }
	public int getPW() { return this.PW; }
	
	public void StartPage() {
		Scanner sc = new Scanner(System.in);
		System.out.println();
		System.out.println("✿ ✿ ✿ " + presentTime + " ✿ ✿ ✿");
		System.out.println("✿ ✿ ✿ " + this.getID() + "님 반갑습니다! ✿ ✿ ✿");
		this.Menu();
	}
	
	public void Menu() {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println();
			System.out.println("✿ ✿ ✿ 메뉴 ✿ ✿ ✿");
			System.out.println("1.시간표 🏫       2.커뮤니티🙂       3.채점&분석서비스✍       4.종료🖐️ ");
			System.out.print("✿ 메뉴입력 → ");
			int choice = sc.nextInt();
			
			if (choice == 4) {
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
				case 1:
					TimeTable t = new TimeTable(this);
					t.timetable();
					break;
				case 2:
					Server server = new Server();
					server.start();
					break;
				case 3:
					Marking m = new Marking(this);
					m.marking();
					break;
				default:
					System.out.println();
					System.out.println("※ 잘못된 입력입니다! ※");
					break;
				}
			}
		}
	}
}
