package com.ym.ocr.img;

import android.util.Log;

/**
 * @purpose: Native class for image operations
 * 
 * @author: chongxishen
 * 
 * @date: Jan. 2009
 * 
 *        使用流程：createEngine() --> initImage() --> loadmemjpg() --> ···图像相关操作···
 *        --> freeImage() --> closeEngine();
 * 
 */
public class NativeImage {

	private static final String TAG = "NativeImage";

	private static final String LIB = "imageengine";
	static {
		try {
			Log.e("imageengine", "static block");
			System.loadLibrary(LIB);
		} catch (Exception e) {
			Log.e("imageengine", "" + e);
		}
	}

	// ///////////////////////////////////////////////////////
	// Constructor/Destructor

	public NativeImage() {

	}

	@Override
	public void finalize() {

	}

	/**
	 * NOTE: Use long type instead of Pointer value of the real C pointer. Type
	 * int is also OK, but Use long to be 64-bit safe.
	 * 
	 * Here just list all APIs of OCR Engine, MAYBE will write a class to wrap
	 * this class for better encapsulation and easy usage.
	 */

	/**
	 * 初始化图像引擎 Image engine must be initialized first before it is able to
	 * process jpg/jpeg/bmp images.
	 * 
	 * @param pIEngine
	 *            引擎标识
	 * @param components
	 * @param quality
	 *            图像压缩质量(0~100)
	 * @return 1：成功
	 */
	public native int initImage(long pIEngine, int components, int quality);

	// 从指定路径加载图像
	// 废弃
	public native int loadImage(long pIEngine, byte[] filename);

	/**
	 * 载入图像
	 * 
	 * @param pIEngine
	 * @param pImgMem
	 *            图像字节流
	 * @param imgSize
	 *            图像大小
	 * @return
	 */
	public native int loadmemjpg(long pIEngine, byte[] pImgMem, int imgSize);

	/**
	 * 保存图像到指定路径
	 * 
	 * @param pIEngine
	 * @param filename
	 *            指定路径
	 * @return
	 */
	public native int saveImage(long pIEngine, byte[] filename);

	/**
	 * 释放图像引擎资源
	 * 
	 * @param pIEngine
	 * @return
	 */
	public native int freeImage(long pIEngine);

	// 设置图像保存选项(???)
	// 废弃
	public native int setSaveParams(long pIEngine, byte[] pImage, int width, int height, int components);

	// 废弃
	public native int setSaveParamsEx(long pIEngine, long pImage, int width, int height, int components);

	/**
	 * 创建一个图像引擎
	 * 
	 * @return
	 */
	public native long createEngine();

	/**
	 * 关闭图像引擎
	 * 
	 * @param pIEngine
	 * @return
	 */
	public native int closeEngine(long pIEngine);

	// 废弃
	public native byte[] getImageData(long pIEngine); // use for get data, but

	// use
	// long parameter instead while saving
	/**
	 * 
	 * @param pIEngine
	 * @return
	 */
	public native long getImageDataEx(long pIEngine);

	/**
	 * 
	 * @param pIEngine
	 * @return
	 */
	public native int getImageWidth(long pIEngine);

	/**
	 * 
	 * @param pIEngine
	 * @return
	 */
	public native int getImageHeight(long pIEngine);

	/**
	 * 
	 * @param pIEngine
	 * @return
	 */
	public native int getImageComponent(long pIEngine);

	/**
	 * 
	 * @param pIEngine
	 * @return
	 */
	public native int isGrayImage(long pIEngine);

	/**
	 * 
	 * @param filenameSrc
	 * @param filenameDst
	 * @param widthDst
	 * @param heightDst
	 * @return
	 */
	public native int scale(byte[] filenameSrc, byte[] filenameDst, int widthDst, int heightDst);

	/**
	 * 
	 * Get image component. Maybe be 1 || 3.
	 * 
	 * @param pIEngine
	 * @param filename
	 * @return
	 */
	public native int getProperty(long pIEngine, byte[] filename);

}