package community;

import java.io.BufferedReader;		// 빨리 읽어들이기
import java.io.IOException;		// 입출력 예외
import java.io.InputStreamReader;	// 문자단위의 데이터로 변환
import java.io.PrintWriter;		// 문자단위로 출력
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.Menu;

public class Server {
	// ※ 메뉴와 연결된 start()함수
	public void start() {
		// 소켓(socket) = 인터넷을 경유하도록 설계된 스트림(컴퓨터 네트워크에서 데이터를 주고받을 수 있는 창구 역할)
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			// (1) 서버 소켓 생성
			serverSocket = new ServerSocket(7000);
			while(true) {
				// (2) 요청이 오면 클라이언트와 통신하는 새 소켓을 accept()를 활용해 만듦
				System.out.println("[클라이언트 연결대기중]");
				socket = serverSocket.accept();
				// (3) 서버가 클라이언트의 메세지를 받기위한 스레드 생성 & 실행
				ReceiveThread receiveThread = new ReceiveThread(socket);
				receiveThread.start();
				// run(): 호출스택이 생성되지 않아 쓰레드가 독립적으로 수행되지 않음 -> start()사용!
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
}

// ※ 스레드를 생성하는 클래스
class ReceiveThread extends Thread{
	// (1-1) 클라이언트에게 메시지를 보내기위해 (Printwriter객체들을 저장하는) 동기화된 arrayList 생성
	static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
	// - ArrayList: 배열 기반 리스트, 크기가 가변임: List<> list = new ArrayList<>();
	// - PrintWriter: 데이터를 문자로 파일에 기록하거나 인쇄하는 클래스
	// -> 다중 스레드 환경에서, Arraylist는 가변 -> 동기화시켜야 함
	// - static: 클래스 전체에 공유되는 변수 -> 모든 객체가 동일한 list에 접근하게 됨
	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	
	// (1-2) 생성자에서, 입출력스트림 생성 & 동기화 리스트에 저장
	public ReceiveThread (Socket socket) {
		this.socket = socket;
		try {
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			list.add(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// (2) run() 재정의 : try-with-resourse를 사용해 자원관리, 메세지 보내기 과정을 수행
	public void run() {
		String name = "";
		// - 자원관리
		try {
			// - 입장시, 이름 보내기
			name = in.readLine();
			System.out.println("[" + name + " 새 연결 생성]");
			sendAll("[" + name + "]님이 들어오셨습니다.");
			// - 메세지 보내기
			while (in != null) {
				String inputMsg = in.readLine();
				if ("quit".equals(inputMsg)) break;
				sendAll(name + ">>" + inputMsg);
			}
		}
		// - 접속이 끊기는 예외처리
		catch (IOException e) {
			System.out.println("[" + name +" 접속끊김]");
		}
		// - 퇴장시, 이름 보내기 & 동기화된 리스트에서 제거
		finally {
			sendAll("[" + name +"]님이 나가셨습니다.");
			list.remove(out);
			try {socket.close();}
			catch (IOException e) { 
				e.printStackTrace();
			}
		 }
		System.out.println("[" + name +" 연결종료]");
	}
	
	// - 전원에게 메세지를 보내는 함수
	//   -> for( A : B )구문 = B에서 차례대로 객체를 꺼내 A에 넣겠다는 의미
	public void sendAll(String s) {
		for (PrintWriter out: list) {
			out.println(s);
			out.flush();
		}
	}
}
