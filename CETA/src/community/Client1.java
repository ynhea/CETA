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
	// ※ main함수 (client 객체생성 + start())
	public static void main(String[] args) {
		Client1 client = new Client1();
		client.start(); 
	}
	
	// ※ start함수
	public void start() {
		Scanner sc = new Scanner(System.in);
		Socket socket = null;
		BufferedReader in = null;
		// - 입장&채팅: socket생성 - 이름설정 - SendThread생성 - 읽기
		try {
			socket = new Socket("localhost", 7000);		// 서버와 연결할 소켓 생성
			System.out.println("[서버와 연결되었습니다]");
			
			String name = "user" + (int)(Math.random() * 10);
			Thread sendThread = new SendThread(socket, name);
			sendThread.start();
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (in != null) {
				String inputMsg = in.readLine();
				if (("[" + name +"]님이 나가셨습니다.").equals(inputMsg)) break;
				System.out.println("From : " + inputMsg);
			}
		}
		// - 예외: 접속끊김
		catch (IOException e) {
			System.out.println("[서버 접속끊김]");
		}
		// - 퇴장: socket닫기 - 메인페이지
		finally {
			try { socket.close();
			Login.main(new String[0]);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

// ※ 클라이언트가 서버에게 메세지를 전달하기 위한 새로운 스레드를 생성하는 클래스
class SendThread extends Thread{
	Scanner sc = new Scanner(System.in);
	// - 필드
	Socket socket = null;
	String name;
	// - 생성자
	public SendThread(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}
	
	// ※ 메세지 전달 함수
	// -> 스트림 생성 - 이름&메세지 출력 - 퇴장 & 예외처리
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