package community;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import main.Login;
import main.Menu;

public class Client1 {
	public static void main(String[] args) {
		Client1 client = new Client1();
		client.start(); 
	}
	
	// ※ main함수를 통해 실행되는 start함수
	public void start() {
		Scanner sc = new Scanner(System.in);
		Socket socket = null;
		BufferedReader in = null;
		// - 입장&채팅: socket생성 - 이름설정 - SendThread생성 - 읽기
		try {
			// (1) 서버와 연결할 소켓 & 이름 생성
			socket = new Socket("localhost", 7000);
			System.out.println("[서버와 연결되었습니다]");
			String name = "user" + (int)(Math.random() * 10);
			// (2) 클라이언트가 서버에게 메세지를 전달하기위한 스레드 & 스트림 생성
			Thread sendThread = new SendThread(socket, name);
			sendThread.start();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// (3) 메세지 보내기 & 예외처리 & 자원처리 & 메뉴이동
			while (in != null) {
				String inputMsg = in.readLine();
				if (("[" + name +"]님이 나가셨습니다.").equals(inputMsg)) break;
				System.out.println(inputMsg);
			}
		}
		catch (IOException e) { System.out.println("[서버 접속끊김]"); }
		finally {
			try {
				if (socket != null) socket.close();
				Login.main(new String[0]);}
			catch (IOException e) { e.printStackTrace(); }
		}
	}

}

// ※ 스레드를 생성하는 클래스
class SendThread extends Thread{
	// (1) 소켓과 이름을 가져옴
	Scanner sc = new Scanner(System.in);
	Socket socket = null;
	String name;

	public SendThread(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}
	
	// (2) run() 재정의 : 메세지 출력하기 & 나가기 & 예외처리
	public void run() {
		try {
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.println(name);
			out.flush();
			
			while (true) {
				String outputMsg = sc.nextLine();
				out.println(outputMsg);
				out.flush();
				if ("quit".equals(outputMsg)) break;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
