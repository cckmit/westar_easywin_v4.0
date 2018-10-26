package com.westar.core.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

/** 
 *
 *  该类是Socket服务端的入口
 */
public class Server extends Thread{
	private static final Log loger = LogFactory.getLog(Server.class);
	
	/**
	 * 端口
	 */
	public static int port = 9999;
	private static ServerSocket server = null;
	/**
	 * 存放录音文件的 文件夹
	 */
	private final String AUDIO_RECORD = "D:\\audio_record";
	
	public void run() {
		if(server == null){
			try{
				//1、新建ServerSocket实例
				server = new ServerSocket(port);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		loger.info(new Date().toString() + " \n 服务器启动...");
		
		while(true){
			try{
				//2、访问ServerSocket实例的accept方法取得一个客户端Socket对象
				Socket client = server.accept();
				if(client == null || client.isClosed()) {
					continue;
				}
				
				new SocketUpLoad(client).start();
				
				
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Server().start();
	}
	
}