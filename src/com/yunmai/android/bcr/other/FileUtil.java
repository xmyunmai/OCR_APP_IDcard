/*
 * File Name: 		FileUtil.java
 * 
 * Copyright(c) 2011 Yunmai Co.,Ltd.
 * 
 * 		 All rights reserved.
 * 					
 */

package com.yunmai.android.bcr.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;


public class FileUtil {
	
	// ///////////////////////////////////////////////////
	// Constructor/Destructor
	public FileUtil() {

	}

	@Override
	public void finalize() {

	}

	// ///////////////////////////////////////////////////
	// Operations

	/**
	 * check file whether exist or not
	 * 
	 * @param filepath
	 *            full path of file
	 * 
	 * @return true -- exist false -- no exist
	 */
	public static boolean exist(String filepath) {
		if(filepath == null)return false;
		File file = new File(filepath);
		boolean exist = file.exists();
		file = null;
		return exist;
	}

	/**
	 * check whether is directory or file
	 * 
	 * @param filepath
	 *            full path of file
	 * 
	 * @return true -- directory false -- file
	 */
	public static boolean isDirectory(String filepath) {
		File file = new File(filepath);
		return file.isDirectory();
	}

	/**
	 * make sure that the directory exist. 1stly check whether the directory
	 * exist or not, if not, then create it. if yes, do nothing.
	 * 
	 * @param dirpath
	 *            path of the directory
	 * 
	 * @return true -- the directory exist false -- the directory do not exist
	 */
	public static boolean makeSureDirExist(String dirpath) {
		boolean exist = true;
		File file = new File(dirpath);
		if (!file.exists()) {
			exist = file.mkdir();
		}
		return exist;
	}

	/**
	 * make sure that the file exist, if not, then create a new file, otherwise
	 * do nothing.
	 * 
	 * @param filepath
	 *            full path of the file
	 * 
	 * @return true -- exist false -- missing
	 */
	public static boolean makeSureFileExist(String filepath) {
		boolean exist = true;
		File file = new File(filepath);
		if (!file.exists()) {
			try {
				exist = file.createNewFile();
			} catch (IOException e) {
				exist = false;
			}
		}
		return exist;
	}

	/**
	 * make sure that the file exist, if not, then create a new file, otherwise
	 * get the length of file.
	 * 
	 * @param filepath
	 *            full path of the file
	 * 
	 * @return -1 -- fail >= 0 -- length of file
	 */
	public static int makeSureFileExistEx(String filepath) {
		int status = -1;
		File file = new File(filepath);
		if (!file.exists()) {
			try {
				if (file.createNewFile())
					status = 0;
			} catch (IOException e) {
				status = -1;
			}
		} else {
			status = (int) file.length();
		}
		return status;
	}

	/**
	 * get the file length
	 * 
	 * @param filepath
	 *            file path
	 * 
	 * @return -1 -- file missing >= 0 -- file length
	 */
	public static int getFileLength(String filepath) {
		File file = new File(filepath);
		if (file.exists()) {
			return (int) file.length();
		}
		return -1;
	}

	/**
	 * delete the specified file
	 * 
	 * @param filepath
	 *            file path
	 * 
	 * @return true -- success false -- fail
	 */
	public static boolean deleteFile(String filepath) {
		File file = new File(filepath);
		if (file.exists()) {
			return file.delete();
		}
		return true;
	}

	/**
	 * copy file from source path to destination path
	 * 
	 * @param src
	 *            source file path
	 * 
	 * @param dst
	 *            destination file path
	 * 
	 * @return true -- success false -- error occur
	 */
	public static boolean copyFile(String src, String dst) {
		boolean result = false;
		try {
			File in = new File(src);
			File out = new File(dst);
			FileInputStream inFile = new FileInputStream(in);
			FileOutputStream outFile = new FileOutputStream(out);

			int i = 0;
			byte[] buffer = new byte[1024];
			while ((i = inFile.read(buffer)) != -1) {
				outFile.write(buffer, 0, i);
			}

			buffer = null;
			inFile.close();
			outFile.close();
			result = true;
		} catch (IOException e) {
		}
		return result;
	}
	
	public static String newImageName() {
		String uuidStr = UUID.randomUUID().toString();
		return uuidStr.replaceAll("-", "") + ".jpg";
	}
	
	public static byte[] getBytesFromFile(String path) throws IOException {
		File file = new File(path);
		return getBytesFromFile(file);
	}
	
	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			throw new IOException("File is to large " + file.getName());
		}
		byte[] bytes = new byte[(int) length];
		try {
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && 
					(numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file "
						+ file.getName());
			}
			is.close();
			return bytes;
		} catch (Exception e) {
			return null;
		} finally {
			bytes = null;
		}
	}
	
	public static String getStrFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		// 获取文件大小
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// 文件太大，无法读取
			throw new IOException("File is to large " + file.getName());
		}
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
		String data = "";
		while ((data = br.readLine()) != null) {
			sb.append(data);
			sb.append("\n");
		}
		String result = sb.toString();
		is.close();
		return result;

	}
	
	/**
	 * 依据原图生成略缩图(Thumb)和图标(Icon)
	 * @param imagePath
	 */
	public static void generateOtherImg(String imagePath) {
//		if (makeSureDirExist(App.getThumbsDir()) && makeSureDirExist(App.getIconsDir()) && imagePath != null) {
//			String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
//			ImageEngine imageEngine = new ImageEngine();
//			// check the thumb files
//			String thumbPath = App.getThumbPathByName(fileName);
//			if (!exist(thumbPath)) {
//				// if missing, then create it
//				if (imageEngine.scale(imagePath, thumbPath, App.THUMB_WIDTH, App.THUMB_HEIGHT)) {
//					; // do nothing
//				}
//			}
//			// check the icon files
//			String iconPath = App.getIconPathByName(fileName);
//			if (!exist(iconPath)) {
//				// if missing, then create it
//				if (imageEngine.scale(imagePath, iconPath, App.ICON_WIDTH, App.ICON_HEIGHT)) {
//					; // do nothing
//				}
//			}
//			imageEngine.finalize();
//			imageEngine = null;
//		}
	}
	
//	/**
//	 * complete the image file path
//	 * 
//	 * @param filename
//	 *            the file name not absolute path, not contain symbol '/' or '\'
//	 * 
//	 * @return full path of the file
//	 */
//	public static String completeImagePath(String filename) {
//		return App.IMAGE_PATH + "/" + filename;
//	}
//
//	/**
//	 * complete the thumb image file path
//	 * 
//	 * @param filename
//	 *            the file name not absolute path, not contain symbol '/' or '\'
//	 * 
//	 * @return full path of the thumb image
//	 */
//	public static String completeThumbPath(String filename) {
//		return App.THUMB_PATH + "/" + filename;
//	}
//
//	/**
//	 * complete the icon image file path
//	 * 
//	 * @param filename
//	 *            the file name not absolute path, not contain symbol '/' or '\'
//	 * 
//	 * @return full path of the icon image
//	 */
//	public static String completeIconPath(String filename) {
//		return App.ICON_PATH + "/" + filename;
//	}
	
}
