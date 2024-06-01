package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

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
			try { socket.close(); }
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}