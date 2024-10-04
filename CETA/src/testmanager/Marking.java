package testmanager;

import java.util.Arrays;
import java.util.Scanner;

import main.Menu;

public class Marking {
	// ※ 필드
	private int ID;
	private int PW;
	// ※ 생성자 & get()
	public Marking(Menu that) { this.ID = that.getID(); this.PW = that.getPW(); }
	public int getID() { return this.ID; }
	public int getPW() { return this.PW; }
	
	// ※ 채점
	public void marking() {
		Scanner sc = new Scanner(System.in);
		// - 도입부
		System.out.println();
		System.out.println("✍ 채점 서비스 ✍");
		System.out.println("\n1. 영어 독해와 작문\n2. 한국사\n3. 통합수학1\n4. 통합수학2\n"
						 + "5. 심화국어\n6. 생명과학2\n7. 화학2\n8. 언어와 매체");
		
		// - 채점 과목 개수 (while문 + if문 / 개수초과 예외처리)
		int num;
		while (true) {
			System.out.print("✍ 채점 과목 개수 → ");
			num = sc.nextInt();		// 과목 개수
			if (num >= 1 && num <= 8) break;
			else					  System.out.println("\n※ 잘못된 입력입니다! ※\n");
		}
		
		// - 채점 과목 선택 (while문 + if문 / 범위 초과 예외 처리)
		int[] subjectChoices = new int[num];
		while (true) {
			int u = 0;
			System.out.print("✍ 채점 과목 선택 → ");
			for (int i = 0; i < num; i += 1) {	// 선택한 과목
				subjectChoices[i] = sc.nextInt() - 1;
				if (subjectChoices[i] >= 0 && subjectChoices[i] <= 7) u++;
			}
			if (u == num) break;	// 잘못 들어간 값이 1개라도 있다면 다시 입력을 받음
			else 		{System.out.println("\n※ 잘못된 입력입니다! ※\n"); Arrays.fill(subjectChoices, 0);}
		}

		
		// - 시험자료 입력 (2차원 배열)
		String[] subjectName = {"영어 독해와 작문","한국사","통합수학1","통합수학2",		// 선택한 과목의 이름
							 	"심화국어","생명과학2","화학2","언어와 매체"};
		int[][] Corrects = {
				{5,4,1,3,5,2,4,4,3,4,1,2,3,1,5,3,2,5,4,3,2,4,5,2,2,2,1,2,5,1},	// 정답
				{5,4,3,3,2,2,2,1,2,5,3,5,3,1,4,4,3,4,1,5,4,1,4,2,2,3,3,4,1,3}
		};
		int[][] CorrectScores = {
				{4,3,2,4,4,3,3,3,2,4,3,3,4,4,4,2,3,4,3,2,3,4,4,3,4,3,4,3,4,4},	// 배점
				{2,4,4,3,3,3,4,4,3,4,4,4,2,3,3,3,4,4,3,2,4,3,4,3,4,3,2,3,4,4}
		};
		int[][] studentWrite = new int[num][30];		// 학생의 답안
		String[][] Result = new String[num][30];		// O/X표시
		int[] testScore = new int[num];					// 학생의 시험점수
		for (int i = 0; i < num; i += 1) { testScore[i] = 0; }
		
		// - 점수 입력 (while문 + if문 / 범위초과 예외처리)
		while (true) {
			int t = 0;
			for (int i = 0; i < num; i += 1) {
				System.out.println("✍ " + subjectName[subjectChoices[i]] + " 답 입력");
				System.out.print("→ ");
				for (int u = 0; u < Corrects[0].length; u += 1) {
					studentWrite[i][u] = sc.nextInt(); 
					if (studentWrite[i][u] >= 1 && studentWrite[i][u] <= 5) t++;}
			}
			if (t == num*Corrects[0].length) break;
			else							 System.out.println("\n※ 잘못된 입력입니다! ※\n");
		}

		
		// - 채점 (for문 + if문)
		for (int i = 0; i < num; i += 1) {
			for (int u = 0; u < Corrects[0].length; u += 1) {
				if (studentWrite[i][u] == Corrects[subjectChoices[i]][u]) {
					 Result[i][u] = "O";
					 testScore[i] += CorrectScores[subjectChoices[i]][u]; }
				else Result[i][u] = "X";
			}
		}
		
		// - 채점 결과 (for문)
		System.out.println();
		System.out.println("✍  " + this.ID + "님의 채점 결과");
		for (int i = 0; i < num; i += 1) {
			System.out.println("✏️  " + subjectName[subjectChoices[i]]);
			System.out.println("총점 : " + testScore[i] + "점");
			for (int u = 0; u < Corrects[0].length; u += 1) {
				System.out.print((u + 1) + "번: " + Result[i][u] + " ");
			}
			System.out.println();
		}
		
		// - 분석서비스/메뉴로 이동 (while문 + switch문)
		System.out.println("✍ 채점 서비스가 끝났습니다!\n");
		while (true) {
			System.out.println("✍ 원하는 서비스를 선택해주세요!");
			System.out.println("1. 분석서비스      2. 메뉴로 돌아가기");
			System.out.print("→ ");
			int move = sc.nextInt();
			
			switch (move) {
			case 1:
				Analysis a = new Analysis(this, Result, num, subjectName, subjectChoices);
				a.start();
				break;
			case 2:
				Menu m = new Menu(this.ID, this.PW);
				m.Menu();
				break;
			default:
				System.out.println("\n※ 잘못된 입력입니다! ※\n");
				break;
			}
		}
	}
}