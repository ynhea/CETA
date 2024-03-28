package community;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import main.Menu;

public class Client2 {
	public static void main(String[] args) {
		Client2 client = new Client2();
		client.start();
	}
	public void start() {
		Scanner sc = new Scanner(System.in);
		Socket socket = null;
		BufferedReader in = null;
		
		try {
			socket = new Socket("localhost", 7000);
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
		} catch (IOException e) {
			System.out.println("[서버 접속끊김]");
		} finally {
			try { socket.close(); 
				while (true) {
					System.out.println("✿ ✿ ✿ 로그인 ✿ ✿ ✿");
					System.out.print("ID : ");
					int id = sc.nextInt();		
					System.out.print("PW : ");
					int pw = sc.nextInt();
					if (id == pw && id >= 3701 && id <= 3728)
				  	 		{ Menu menu = new Menu(id, 1234); menu.StartPage(); break;}
					else	System.out.println("\n※ 잘못된 입력입니다! ※\n"); }
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
/*
class SendThread extends Thread{
	Socket socket = null;
	String name;
	
	Scanner sc = new Scanner(System.in);
	
	public SendThread(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}
	
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
*/