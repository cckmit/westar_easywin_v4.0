package com.westar.core.thread;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.westar.core.web.controller.RegistController;

public class SocketUpLoad extends Thread {
	
	private static final Log loger = LogFactory.getLog(SocketUpLoad.class);
	
	/**
	 * socket对象
	 */
	private Socket client;
	
	/**
	 * 保存路径
	 */
	private String savepath="C:\\temp";
	
	/**
	 * 返回路径(相对路径)
	 */
	private String filepath="/";
	

	public SocketUpLoad(Socket client) {
		this.client = client;
	}

	/**
	 * 创建目录（不存在则创建）
	 * @param dir
	 * @return
	 */
	public File createDir(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public void run() {
		if (client == null){
			return;
		}

		DataInputStream in = null;
		BufferedOutputStream fo = null;
		DataOutputStream out = null;

		try {
			// 1、访问Socket对象的getInputStream方法取得客户端发送过来的数据流
			in = new DataInputStream(new BufferedInputStream(
					client.getInputStream()));

			String[] str = in.readUTF().split("\\|");
			//文件名
			String fileName =System.currentTimeMillis()+"."+ str[0].split("\\.")[1]; // 取得附带的文件名
			loger.info(new Date().toString() + " \n 文件名为:"
					+ fileName);
			String filetype=str[0];
			String userid = str[1];// 取用户ID
			//存储路径
			savepath=savepath+filetype+"\\"+userid;
			if (savepath.endsWith("/") == false
					&& savepath.endsWith("\\") == false) {
				savepath += "\\";
			}
			loger.info("保存路径："+savepath);
			//创建目录
			createDir(savepath);
			 
			
			//返回文件名和路径
			filepath = filepath+filetype+"/"+userid+"/"+fileName;
			loger.info("返回路径："+filepath);

			// 2、将数据流写到文件中
			fo = new BufferedOutputStream(new FileOutputStream(new File(
					savepath+"\\"+fileName)));

			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
				fo.write(buffer, 0, bytesRead);
			}
			fo.flush();
			fo.close();

			loger.info(new Date().toString() + " \n 数据接收完毕");

			// 3、获得Socket的输出流,返回一个值给客户端
			out = new DataOutputStream(new BufferedOutputStream(
					client.getOutputStream()));
			out.writeInt(1);
			out.writeUTF(filepath);
			out.flush();

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				out.writeInt(0);
				out.flush();
			} catch (IOException e) {
				loger.info(new Date().toString() + ":" + e.toString());
			}
		} finally {
			try {
				out.close();
				fo.close();
				in.close();
				client.close();
			} catch (IOException e) {
				loger.info(new Date().toString() + ":" + e.toString());
			}
		}

	}
}
