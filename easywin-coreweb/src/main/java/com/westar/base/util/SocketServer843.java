package com.westar.base.util;

/**
 * 用于兼容IE的聊天
 * @author H87
 *
 */
public class SocketServer843 implements Runnable {
	@Override
	public void run() {
//		ServerSocket serverSocket;
//		try {
//			serverSocket = new ServerSocket(843);
//			System.out.println("843端口开始监听socket");
//			while (true) {
//				String xml = "<?xml version=\"1.0\"?><cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0";
//				Socket socket = serverSocket.accept();
//				BufferedReader br = new BufferedReader(new InputStreamReader(
//						socket.getInputStream(), "UTF-8"));
//				PrintWriter pw = new PrintWriter(socket.getOutputStream());
//				char[] by = new char[22];
//				br.read(by, 0, 22);
//				String s = new String(by);
//				System.out.println("s=" + s);
//				if (s.equals("<policy-file-request/>")) {
//					pw.print(xml);
//					pw.flush();
//					br.close();
//					pw.close();
//					socket.close();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}
}