package main;

import java.util.Scanner;

public class Login {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// 학생 생성
		Menu[] students = new Menu[28];
		for (int i = 0; i < students.length; i += 1) {
			students[i] = new Menu(3701 + i, 3701 + i);
		}
		
		// 도입부
		System.out.println("우리의 타임, 나만의 타임!");
		try { Thread.sleep(2000); }
		catch (InterruptedException e) { e.printStackTrace(); }
		System.out.println("부산중앙여자고등학교의 𝑪𝑬𝑻𝑨, 지금 바로 시작합니다!");
		try { Thread.sleep(1000); } 
		catch (InterruptedException e) { e.printStackTrace(); }
		System.out.println("🌸 🏫 💯✍ 👑  𝑪𝑬𝑻𝑨 👑 ✍ 💯  🏫 🌸");
		try { Thread.sleep(1000); }
		catch (InterruptedException e) { e.printStackTrace(); }
		System.out.println();
		System.out.println("✿ ✿ ✿ 로그인 ✿ ✿ ✿");
		
		// 로그인
		while (true) {
		System.out.print("ID : ");
		int IdWrite = sc.nextInt();		
		System.out.print("PW : ");
		int PwWrite = sc.nextInt();
		 
		if (IdWrite < 3701 || IdWrite > 3728)				    System.out.println("\n※ 잘못된 ID를 입력하였습니다. ※\n");
		else if (PwWrite == students[IdWrite - 3701].getPW()) { students[IdWrite - 3701].StartPage(); break; }
		else 													System.out.println("\n※ 잘못된 PW를 입력하였습니다. ※\n");
		}
	}
}
