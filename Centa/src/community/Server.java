package community;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
	// MENU에서 커뮤니티 서비스로 들어오는 도입메서드
	public void start() {
		// 변수 초기화
		ServerSocket serverSocket = null;	// 클라이언트의 요청을 받기위한 준비를 하는 서버소켓
		Socket socket = null;				// 서버에 접속 요성을 하는 클라이언트 소켓
		// 실제								// 소켓 = 데이터를 주고받을 수 있는 창구 역할 (송/수신, 양방향)
		try {
			serverSocket = new ServerSocket(7000);
			while(true) {
				System.out.println("[클라이언트 연결대기중]");
				socket = serverSocket.accept();								// 접근을 요청한 클라이언트 소켓을 socket변수에 저장함
				ReceiveThread receiveThread = new ReceiveThread(socket);	// 데이터 수신 쓰레드 생성
				receiveThread.start();
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
}

// 수신하는 쓰레드를 만드는 ReceiveThread 클래스
class ReceiveThread extends Thread{
	// 클라이언트가 접속할 때마다/연결을 종료할 때마다 해당 출력 스트림을 리스트에 추가/제거하는 방식으로 관리함
	static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
	
	// 변수 초기화
	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	// 생성자
	public ReceiveThread (Socket socket) {
		this.socket = socket;
		try {
			// out : 클라이언트로부터 받은 메시지에 대해 서버가 응답해야할 경우를 대비
			out = new PrintWriter(socket.getOutputStream());
			// in : getInputStream() - 소켓으로부터 입력스트림을 가져와 바이트 단위로 데이터 읽음
			//		new InputStreamReader() - 바이트를 문자로 변환하는 문자스트림
			//		new BufferedReader() - 문자 스트림을 버퍼링하여 효율적으로 읽음
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			list.add(out);
		} catch (IOException e) { e.printStackTrace(); }
	}
	// run 메서드 (.start()호출 시, run()이 내부에서 호출됨)
	public void run() {
		// 변수 초기화
		String name = "";
		// 실제
		try {
			// 도입부
			name = in.readLine();	// 이름 전달받음
			System.out.println("[" + name + " 새 연결 생성]");
			sendAll("[" + name + "]님이 들어오셨습니다.");
			// 무한루프를 돌며 클라이언트에게 전달받은 데이터를 출력 & quit파악
			while (in != null) {
				String inputMsg = in.readLine();
				if ("quit".equals(inputMsg)) break;
				sendAll(name + ">>" + inputMsg);
			}
		} catch (IOException e) {
			System.out.println("[" + name +" 접속끊김]");
		} finally {
			// 엔딩
			sendAll("[" + name +"]님이 나가셨습니다.");
			list.remove(out);		// 클라이언트에게 출력하는 출력스트림을 제거함
			try {socket.close();}	// 소켓 닫음 -> 그에따라 그 소켓을 통해 작업하던 송/수신 쓰레드들도 종료됨
			catch (IOException e) { e.printStackTrace(); }
		 }
		System.out.println("[" + name +" 연결종료]");
	}
	// sendAll 메서드
	public void sendAll(String s) {
		for (PrintWriter out: list) {	// 클라이언트에게 연결된 모든 출력스트림한테
			out.println(s);				// s를 출력하게함
			out.flush();				// flush() : PrintWriter객체의 버퍼를 비움 & 모든 버퍼링된 데이터를 출력하게함
										//			 println()이 자동으로 flush를 수행하지만, 그렇지 않을 수도 있기에, 안전성을 고려해 적어줌
		}
	}
	
}
