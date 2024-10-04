package timetable;	// 패키지가 timetable임

import java.util.Calendar;	// 프로그램 실행당시의 날짜를 가져오기 위해 import하기
import main.Menu;			// 객체는 main패키지의 Menu클래스에서 생성함 & 객체의 ID를 가져와 개별맞춤 시간표를 출력하기 위해 import함

public class TimeTable {	// TimeTable의 클래스
	// ※ 필드
	private int ID;
	// ※ 생성자
	public TimeTable(Menu that) { this.ID = that.getID(); }
	
	// ※ 시간표/학식 출력 메서드
	public void timetable() {
		// 시간표 출력
		System.out.println();
		System.out.println("🏫 " + this.ID + "님의 시간표 🏫");
		System.out.println("월               화               수           목                금");
		
		if (this.ID >= 3701 && this.ID <= 3714) {	// 학번이 3701 ~3714일 경우 이 코드를 출력
			System.out.println("생명과학2   공학일반      심화국어   통합수학1    논리학");
			System.out.println("영독작          통합수학1  공학일반    생명과학2    진로활동");
			System.out.println("공학일반       영독작         체육         영독작           심화국어");
			System.out.println("심화국어       심화국어      논리학       공학일반        통합수학1");
			System.out.println("음악            영독작         창체          자율             통합수학1");
			System.out.println("통합수학1  통합수학2   창체          논리학          생명과학2");
			System.out.println("논리학         생명과학2  통합수학2  통합수학2   한국사");
		}
		else {										// 학번이 3715 ~ 3728일 경우 이 코드를 출력
			System.out.println("통합수학2   심화국어      공학일반   통합수학1    교육학");
			System.out.println("영독작          생명과학2  심화국어    통합수학2    진로활동");
			System.out.println("심화국어       영독작         체육         영독작           공학일반");
			System.out.println("공학일반       심화국어      교육학       심화국어        통합수학2");
			System.out.println("미술            영독작         창체          자율             통합수학1");
			System.out.println("통합수학1  생명과학2   창체          논리학          생명과학2");
			System.out.println("교육학         통합수학2  생명과학2  통합수학2   한국사");
		}
		
		// 학식 자료입력(2차원 배열)
		String[][] lunches = {		// 중식
				{"현미밥","된장국","로제치즈옹볶이","밤송송함박","달고나우유마카롱"},
				{"백미밥","미역국","안동찜닭","바나나칩고구마맛탕","노랑잡채만두+비빔야채"},
				{"백미밥","참치김치찌개","더블치즈돈까스","투움바파스타","레몬스틱젤리"},
				{"잡곡밥","모듬어묵국","연두부+양념장","고추장돼지두루치기","블랙타이거새우치즈구이"},
				{"통밀밥","냉이단배추쑥국","불마요들기름막국수","부들어묵볶음","크리스피돈육강정"},
				{"없음","없음","없음","없음","없음"},
				{"없음","없음","없음","없음","없음"}
		};
		String[][] dinners = {		// 석식
				{"쇠고기야채죽","TX치킨버거","벌집감자튀김","치즈시즈닝","델몬트망고주스"}
		};
		
		// - 날짜 가져오기
		Calendar c = Calendar.getInstance();		// 추상클래스 Calendar를 상속하는 하위클래스에서 객체를 생성함
		int day = c.get(Calendar.DAY_OF_MONTH);		// get() 메소드를 통해 다양한 정보(연도, 월, 일 등..)를 얻으며, Calender클래스 정의해둔 상수로 사용함
		
		// - 그 날짜의 중식 출력
		System.out.println();
		System.out.println("🍚 중식 🍚");
		for (int i = 0; i < lunches[0].length; i += 1) {
			System.out.println("● " + lunches[day-1][i]);
		}
		
		// - 그 날짜의 석식 출력
		System.out.println();
		System.out.println("🍚 석식 🍚");
		for (int i = 0; i < dinners[0].length; i += 1) {
			System.out.println("● " + dinners[day-1][i]);
		}
	}
}