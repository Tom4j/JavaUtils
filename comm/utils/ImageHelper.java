package com.siweidg.comm.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

import sun.misc.BASE64Encoder;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageHelper {
	static BASE64Encoder encoder = new sun.misc.BASE64Encoder();    

	/*
	 * 根据尺寸图片居中裁剪
	 */
	@SuppressWarnings("rawtypes")
	public static void cutCenterImage(String src, String dest, int w, int h)
			throws IOException {
		Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		int imageIndex = 0;
		Rectangle rect = new Rectangle((reader.getWidth(imageIndex) - w) / 2,
				(reader.getHeight(imageIndex) - h) / 2, w, h);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, "jpg", new File(dest));

	}

	/*
	 * 图片裁剪二分之一
	 */
	@SuppressWarnings("rawtypes")
	public static void cutHalfImage(String src, String dest) throws IOException {
		Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		int imageIndex = 0;
		int width = reader.getWidth(imageIndex) / 2;
		int height = reader.getHeight(imageIndex) / 2;
		Rectangle rect = new Rectangle(width / 2, height / 2, width, height);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, "jpg", new File(dest));
	}

	/*
	 * 图片裁剪通用接口
	 */

	@SuppressWarnings("rawtypes")
	public static void cutImage(String src, String dest, int x, int y, int w,
			int h) throws IOException {
		Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		Rectangle rect = new Rectangle(x, y, w, h);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, "jpg", new File(dest));

	}

	/*
	 * 图片裁剪成正方形
	 */

	@SuppressWarnings("rawtypes")
	public static void cutSquareImage(String src, String dest)
			throws IOException {
		Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		int imageIndex = 0;
		int w = reader.getWidth(imageIndex);
		int h = reader.getHeight(imageIndex);
		Rectangle rect = null;
		if (w > h) {
			rect = new Rectangle(h, h);
		} else {
			rect = new Rectangle(0, 0, w, w);
		}

		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, "jpg", new File(dest));

	}

	/*
	 * 图片缩放
	 */
	@SuppressWarnings("static-access")
	public static void zoomImage(String src, String dest, int w, int h)
			throws Exception {
		double wr = 0, hr = 0;
		File srcFile = new File(src);
		File destFile = new File(dest);
		BufferedImage bufImg = ImageIO.read(srcFile);
		Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);
		wr = w * 1.0 / bufImg.getWidth();
		hr = h * 1.0 / bufImg.getHeight();
		AffineTransformOp ato = new AffineTransformOp(
				AffineTransform.getScaleInstance(wr, hr), null);
		Itemp = ato.filter(bufImg, null);
		try {
			ImageIO.write((BufferedImage) Itemp,
					dest.substring(dest.lastIndexOf(".") + 1), destFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * 获取图片的宽高
	 * 
	 * @param img
	 *            图片文件
	 * @return
	 * @throws IOException
	 */
	public static Integer[] getImgWidhtAndHeight(File img) throws IOException {
		Integer[] integers = null;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image srcImage = toolkit.getImage(img.getAbsolutePath());//  构造Image对象
		boolean flag = true;
		int width = 0;
		int height = 0;
		while (flag) {
			width = srcImage.getWidth(null); // 得到源图宽
			height = srcImage.getHeight(null); // 得到源图长
			if (width > 0 && height > 0) {
				flag = false;
			} else {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		integers = new Integer[2];
		integers[0] = width;
		integers[1] = height;
		return integers;
	}

	/**
	 * 缩放图片
	 * 
	 * @param originalFile
	 *            原图片路径
	 * @param resizedFile
	 *            缩放后图片路径
	 * @param newWidth
	 *            缩放后较长边的值
	 * @param quality
	 *            缩放质量 0f-1f
	 * @throws IOException
	 */
	public static void resize(String src, String dest, int newWidth,
			float quality) throws IOException {
		File originalFile = new File(src);
		File resizedFile = new File(dest);

		if (quality > 1) {
			throw new IllegalArgumentException(
					"Quality has to be between 0 and 1");
		}

		ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
		Image i = ii.getImage();
		Image resizedImage = null;

		int iWidth = i.getWidth(null);
		int iHeight = i.getHeight(null);

		if (iWidth > iHeight) {
			resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight)
					/ iWidth, Image.SCALE_SMOOTH);
		} else {
			resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight,
					newWidth, Image.SCALE_SMOOTH);
		}

		// This code ensures that all the pixels in the image are loaded.
		Image temp = new ImageIcon(resizedImage).getImage();

		// Create the buffered image.
		BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
				temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

		// Copy image to buffered image.
		Graphics g = bufferedImage.createGraphics();

		// Clear background and paint the image.
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

		// Write the jpeg to a file.
		FileOutputStream out = new FileOutputStream(resizedFile);

		// Encodes image as a JPEG data stream
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

		JPEGEncodeParam param = encoder
				.getDefaultJPEGEncodeParam(bufferedImage);

		param.setQuality(quality, true);

		encoder.setJPEGEncodeParam(param);
		encoder.encode(bufferedImage);
		out.close();
	} // Example usage
	
	public static String getImageBinary(String path){    
	        File f = new File(path);           
	        BufferedImage bi;    
	        try {    
	            bi = ImageIO.read(f);    
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();    
	            ImageIO.write(bi, "jpg", baos);    
	            byte[] bytes = baos.toByteArray();    
	           
	            return encoder.encodeBuffer(bytes).trim();    
	        } catch (IOException e) {    
	            e.printStackTrace();    
	        }    
	        return null;    
	}    
}
