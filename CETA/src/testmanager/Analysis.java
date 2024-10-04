package testmanager;

import java.util.Scanner;
import main.Menu;

public class Analysis {
	// ※ 필드
	private int ID;
	private int PW;
	private String[][] Result;
	private int num;
	private String[] subjectName;
	private int[] subjectChoices;
	// ※ 생성자
	public Analysis(Marking mark, String[][] Result, int num, String[] subjectName, int[] subjectChoices) {
		this.ID = mark.getID();
		this.PW = mark.getPW();
		this.Result = Result;						// O/X표시
		this.num = num;								// 채점 과목 개수
		this.subjectName = subjectName;
		this.subjectChoices = subjectChoices;		// 채점한 과목 배열
	}
	
	// ※ 자료 입력 (2차원 배열)
	String[][] testWhat = {		// 시험출제 단원
			{"교과서 1단원","교과서 1단원","교과서 1단원","교과서 1단원","모의고사 20번","모의고사 21번","모의고사 22번","모의고사 23번",
			"담화","단어","음운","담화","담화","언어와 우리삶","단어",
			"문장","국어의 특성","국어의 특성","단어","음운","음운","문장","문장"}
	};
	int[][] testWhere = {		// 시험 출제 위치
			{0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,1,1,1,1},	// 교과서/학습지
			{1,1,1,0,1,1,1,1,0,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,0},	// 0   / 1
	};
	
	// ※ 도입 메서드 (switch문)
	public void start() {
		Scanner sc = new Scanner(System.in);
		System.out.println();
		System.out.println("📌 분석 서비스 📌");
		System.out.println("1. 문제 해당 범위 찾기 서비스        2. 학습지/교과서 비율 계산 서비스        3. 메뉴로 이동");
		System.out.print("→ ");
		int choice = sc.nextInt();
		
		switch (choice) {
		case 1:
			this.first();
			break;
		case 2:
			this.second();
			break;
		case 3:
			Menu m = new Menu(this.ID, this.PW);
			m.Menu();
			break;
		default:
			System.out.println("\n※ 잘못된 입력입니다! ※");
			this.start();
			break;
		}
	}
	
	// ※ 문제 해당 범위 찾기 서비스 (for문 + if문)
	public void first() {
		System.out.println();
		System.out.println("📌 문제 해당 범위 찾기 서비스 📌");
		
		for (int i = 0; i < this.num; i += 1) {
			System.out.println("📌 " + this.subjectName[this.subjectChoices[i]] + " 오답  해당 범위");
			for (int u = 0; u < testWhere[0].length; u += 1) {
				if (this.Result[i][u].equals("X")) 
					System.out.print((u + 1) + "번: " + testWhat[this.subjectChoices[i]][u] + " ");
			}
			System.out.println();
		}
		this.start();
	}
	
	// ※ 학습지/교과서 비율 계산 서비스 (for문 + if문)
	public void second() {
		System.out.println();
		System.out.println("📌 학습지/교과서 비율 계산 서비스 📌");
		System.out.println("※ 주의사항 - 소수점 이하의 숫자를 버리므로 약간의 오차가 발생할 수 있습니다. ※");
		
		int[] textBookNum = new int[num];		// 교과서에서 나온 문제의 개수
		int[] pulusPaperNum = new int[num];		// 학습지에서 나온 문제의 개수
		for (int i = 0; i < num; i += 1) { textBookNum[i] = 0; pulusPaperNum[i] = 0; }
		
		for (int i = 0; i < num; i += 1) {		// 개수 계산
			for (int u = 0; u < testWhere[0].length; u += 1) {
				if (this.testWhere[i][u] == 0) textBookNum[i] += 1;
				else 						   pulusPaperNum[i] += 1;
			}
		}
		
		for (int i = 0; i < num; i += 1) {		// 개수 출력
			System.out.println("📌 " + this.subjectName[this.subjectChoices[i]]);
			System.out.println("교과서 : " + (int)(textBookNum[i] / 30.0 * 100) + "%");
			System.out.println("학습지 : " + (int)(pulusPaperNum[i] / 30.0 * 100) + "%");
		}
		this.start();
	}
}