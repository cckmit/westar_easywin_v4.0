package com.westar.base.util;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 用于头像的大小修改
 * @author H87
 *
 */
public class DifPic {

	/**
	 * 重新设置图片信息（用于存放到数据库）
	 * @param originalFile
	 * @param resizedFile
	 * @param newWidth
	 * @param quality
	 * @throws IOException
	 */
	public static void resize(File originalFile, File resizedFile,
			int newWidth, float quality) throws IOException {

		//图片的清晰度不能比1大
		if (quality > 1) {
			throw new IllegalArgumentException(
					"Quality has to be between 0 and 1");
		}

		//根据客户端信息实例化图片信息
		ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
		Image i = ii.getImage();
		Image resizedImage = null;

		//取得图片的宽度和高度
		int iWidth = i.getWidth(null);
		int iHeight = i.getHeight(null);

		if (iWidth > iHeight) {
			resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight)
					/ iWidth, Image.SCALE_SMOOTH);
		} else {
			resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight,
					newWidth, Image.SCALE_SMOOTH);
		}

		//确保图像完全加载
		Image temp = new ImageIcon(resizedImage).getImage();

		//创建一个图片流
		BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
				temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

		//复制图片流
		Graphics g = bufferedImage.createGraphics();

		//清空背景图并开始画图
		g.setColor(Color.white);
		g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
		g.drawImage(temp, 0, 0, null);
		g.dispose();

		// Soften.
		float softenFactor = 0.05f;
		float[] softenArray = { 0, softenFactor, 0, softenFactor,
				1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };
		Kernel kernel = new Kernel(3, 3, softenArray);
		ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		bufferedImage = cOp.filter(bufferedImage, null);

		//将jpeg写到文件中
		FileOutputStream out = new FileOutputStream(resizedFile);

		//JPEG图片流编码
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

		JPEGEncodeParam param = encoder
				.getDefaultJPEGEncodeParam(bufferedImage);

		param.setQuality(quality, true);

		encoder.setJPEGEncodeParam(param);
		encoder.encode(bufferedImage);
	} // 案例完毕

//	public static void main(String[] args) throws IOException {
////		 File originalImage = new File("C:\\11.jpg");
////		 resize(originalImage, new File("c:\\11-0.jpg"),150, 0.7f);
////		 resize(originalImage, new File("c:\\11-1.jpg"),150, 1f);
//		 File originalImage = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg");
//		 resize(originalImage, new File("D:\\1504-0.jpg"),100, 1f);
//		 resize(originalImage, new File("D:\\1504-1.jpg"),48, 1f);
//		 resize(originalImage, new File("D:\\1504-2.jpg"),24, 1f);
//	}
}
