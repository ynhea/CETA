package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client1 {
	public static void main(String[] args) {
		Client1 client = new Client1();
		client.start(); 
	}
	public void start() {
		Scanner sc = new Scanner(System.in);
		Socket socket = null;
		BufferedReader in = null;
		
		try {
			// 수신 (수신하는 쓰레드에 소켓(데이터 이동 창구)을 연결함)
			socket = new Socket("localhost", 7000);
			System.out.println("[서버와 연결되었습니다]");
			// 송신 (데이터를 서버로 송신하는 쓰레드를 생성 & 시작함) (똑같은 소켓(데이터 이동 창구) 사용함)
			String name = "user" + (int)(Math.random() * 10);
			// 송신 쓰레드를 virtual thread로 변경
            Thread.startVirtualThread(new SendTask(socket, name)); // 데이터 송신 쓰레드
			// 수신 (무한루프를 돌며 서버로부터 데이터 수신함)
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (in != null) {
				String inputMsg = in.readLine();
				if (("[" + name +"]님이 나가셨습니다.").equals(inputMsg)) break;
				System.out.println("From : " + inputMsg);
			}
		} catch (IOException e) {				// 예외처리
			System.out.println("[서버 접속끊김]");
		} finally {								// catch에 잡히지 않고  try이후 실행될 코드
			try { socket.close(); 		// 소켓연결 끊기 	// MENU 페이지로 돌아가기
			}
			catch (IOException e) { e.printStackTrace(); }
		}
	}
}

//송신 작업을 처리하는 SendTask 클래스 (Runnable을 구현)
class SendTask implements Runnable{
	Scanner sc = new Scanner(System.in);
	Socket socket = null;
	String name;
	
	// 생성자
	public SendTask(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}
	
	// run 메서드 (.start()호출 시, run()이 내부에서 호출됨)
	public void run() {
		try {
			// 처음 연결되면 서버에게 클라이언트의 이름을 전달함
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.println(name);
			out.flush();
			// 무한루프를 돌며 사용자의 입력을 반복적으로 송신함
			while (true) {
				String outputMsg = sc.nextLine();
				out.println(outputMsg);
				out.flush();
				if ("quit".equals(outputMsg)) break;
			}
		} catch(IOException e) { e.printStackTrace(); }
	}
	
}