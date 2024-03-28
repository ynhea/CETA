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
	public void start() {
		ServerSocket serverSocket = null;	// 네트워크 서버를 구현하는 데 사용되는 클래스
		Socket socket = null;				// -> 서버에서 클라이언트의 연결 요청을 수락하고, 클라이언트와의 통신을 위한 소켓을 생성한다.
		try {								// 소켓(socket) = 컴퓨터 네트워크에서 데이터를 주고받을 수 있는 창구 역할
			serverSocket = new ServerSocket(7000);
			while(true) {
				System.out.println("[클라이언트 연결대기중]");
				socket = serverSocket.accept();	// 서버가 클라이언트의 연결 요청을 수락하고, 이후의 통신에 사용할 소켓을 생성하는 역할
				ReceiveThread receiveThread = new ReceiveThread(socket);	// 서버가 클라이언트와의 통신을 처리하기 위해 새로운 스레드를 생성하고
				receiveThread.start();										// 그 스레드에 클라이언트와의 통신을 위한 소켓을 전달하는 것
			}
		} catch (IOException e) { e.printStackTrace(); }	// 예외처리
	}
}

class ReceiveThread extends Thread{
	static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
	// PrintWriter의 객체에 대한 동기화된 리스트를 생성함
	// 서버 측에서 PrintWriter를 사용하는 경우 -> 서버가 클라이언트에게 데이터를 보낼 때 사용되는 출력 스트림을 나타낸다.
	
	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	
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
	
	public void run() {
		String name = "";
		
		try {
			name = in.readLine();
			System.out.println("[" + name + " 새 연결 생성]");
			sendAll("[" + name + "]님이 들어오셨습니다.");
			
			while (in != null) {
				String inputMsg = in.readLine();
				if ("quit".equals(inputMsg)) break;
				sendAll(name + ">>" + inputMsg);
			}
		} catch (IOException e) {
			System.out.println("[" + name +" 접속끊김]");
		} finally {
			sendAll("[" + name +"]님이 나가셨습니다.");
			list.remove(out);
			try {socket.close();}
			catch (IOException e) { 
				e.printStackTrace();
			}
		 }
		System.out.println("[" + name +" 연결종료]");
	}
	
	public void sendAll(String s) {
		for (PrintWriter out: list) {
			out.println(s);
			out.flush();
		}
	}
	
}