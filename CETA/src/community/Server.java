package community;

import java.io.BufferedReader;		// 빨리 읽어들이기
import java.io.IOException;			// 입출력 예외
import java.io.InputStreamReader;	// 문자단위의 데이터로 변환
import java.io.PrintWriter;			// 문자단위로 출력
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.Menu;

public class Server {
	// ※ 서버 생성 (socket + thread)
	public void start() {
		// 소켓(socket) = 인터넷을 경유하도록 설계된 스트림(컴퓨터 네트워크에서 데이터를 주고받을 수 있는 창구 역할)
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(7000);	// 클라이언트의 요청을 받기 위한 준비를 한다
			while(true) {
				System.out.println("[클라이언트 연결대기중]");
				socket = serverSocket.accept();		// 클라이언트의 요청을 받아들인다
				ReceiveThread receiveThread = new ReceiveThread(socket);	// 서버-클라이언트의 통신을 처리하는 새로운 스레드를 생성한다
				receiveThread.start();
			}
		} catch (IOException e) { e.getStackTrace(); }	// 예외처리
	}
}

// ※ 서버가 클라이언트의 메세지를 받기 위한 새로운 스레드를 생성하는 클래스
class ReceiveThread extends Thread{
	// ※ 필드
	static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
	// - ArrayList: 배열 기반 리스트, 크기가 가변임: List<> list = new ArrayList<>();
	// - PrintWriter: 데이터를 문자로 파일에 기록하거나 인쇄하는 클래스
	// -> 다중 스레드 환경에서, Arraylist는 가변 -> 동기화시켜야 함
	// - static: 클래스 전체에 공유되는 변수 -> 모든 객체가 동일한 list에 접근하게 됨
	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	
	// ※ 생성자
	public ReceiveThread (Socket socket) {
		this.socket = socket;
		try {
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			list.add(out);
		} catch (IOException e) {
			e.getStackTrace();
		}
	}
	
	// ※ 채팅방 입장-채팅-접속끊김-퇴장 함수
	public void run() {
		String name = "";
		// - 입장&채팅: 이름읽기 - sendAll - !null,메세지 sendAll
		try {
			name = in.readLine();
			System.out.println("[" + name + " 새 연결 생성]");
			sendAll("[" + name + "]님이 들어오셨습니다.");
			
			while (in != null) {
				String inputMsg = in.readLine();
				if ("quit".equals(inputMsg)) break;
				sendAll(name + ">>" + inputMsg);
			}
		}
		// - 예외: 접속끊김
		catch (IOException e) {
			System.out.println("[" + name +" 접속끊김]");
		}
		// - 퇴장: 나갔다고 sendAll - list에서 제거 - socket.close() - 서버에 print
		finally {
			sendAll("[" + name +"]님이 나가셨습니다.");
			list.remove(out);
			try {socket.close();}
			catch (IOException e) { 
				e.getStackTrace();
			}
		 }
		System.out.println("[" + name +" 연결종료]");
	}
	
	// ※ 전원에게 메세지를 보내는 함수
	// - for( A : B )구문 = B에서 차례대로 객체를 꺼내 A에 넣겠다는 의미
	public void sendAll(String s) {
		for (PrintWriter out: list) {
			out.println(s);
			out.flush();
		}
	}
}