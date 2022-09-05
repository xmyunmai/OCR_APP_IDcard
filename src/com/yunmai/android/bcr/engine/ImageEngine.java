/*
 * File Name: 		ImageEngine.java
 * 
 * Copyright(c) 2011 Yunmai Co.,Ltd.
 * 
 * 		 All rights reserved.
 * 					
 */

package com.yunmai.android.bcr.engine;

import android.util.Log;

import com.ym.ocr.img.NativeImage;
import com.yunmai.android.bcr.other.StringUtil;

/**
 * @purpose: A low level wrapper class for native image operators.
 * 
 * @author: chongxishen
 * 
 * @date: Jan. 2009
 */
public class ImageEngine {

	// format
	public static final int IMG_FMT_UNK = 0;
	public static final int IMG_FMT_BMP = 1;
	public static final int IMG_FMT_JPG = 2;

	// component
	public static final int IMG_COMPONENT_GRAY = 1;
	public static final int IMG_COMPONENT_RGB = 3;

	public static final int RET_OK = 1;
	public static final int RET_ERR_UNK = 0; /* unknown error */
	public static final int RET_ERR_PTR = -1; /* invalid pointer */
	public static final int RET_ERR_ARG = -2; /* error parameter */
	public static final int RET_ERR_MEM = -3; /* fail to allocate memory */

	protected long mEngine = 0;
	protected long mImage = 0;
	protected NativeImage mImageEngine = null;

	// ///////////////////////////////////////////////////////////////////////
	// Constructor/Destructor

	public ImageEngine() {
		mImageEngine = new NativeImage();
		Log.i("imageengine", "ImageEngine");
		mEngine = mImageEngine.createEngine();
	}

	@Override
	public void finalize() {
		if (mImageEngine != null) {
			if (mEngine != 0) {
				mImageEngine.freeImage(mEngine);
				mImageEngine.closeEngine(mEngine);
				mEngine = 0;
				mImage = 0;
			}
		}
	}

	// ///////////////////////////////////////////////////////////
	// Methods

	public boolean init(int components, int quality) {
		if (mImageEngine != null) {
			int ret = mImageEngine.initImage(mEngine, components, quality);
			if (ret == RET_OK) {
				return true;
			}
		}
		return false;
	}

	public boolean load(byte[] imgbuffer, String filename) {
		if (mImageEngine != null) {
			int ret = mImageEngine.loadmemjpg(mEngine, imgbuffer, imgbuffer.length);
			if (ret == RET_OK) {
				// mImage = mImageEngine.getImageDataEx(mEngine);

				// if (!filename.equals("no_need")) {
				// String src = App.getImagePath(filename);
				// // String thumb_src = App.getThumbPathByName(filename);
				// // String icon_src = App.getIconPathByName(filename);
				// if (mImageEngine.saveImage(mEngine, StringUtil
				// .convertToUnicode(src)) == 1) {
				// // ABizcardEdit 进行保存thunb,icon(节省时间)
				// // scale(src, thumb_src, 800, 480);
				// // scale(src, icon_src, 120, 90);
				// }
				// }

				return true;
			}
		}
		return false;
	}

	public boolean save(String filename) {
		if (mImageEngine != null) {
			int ret = mImageEngine.saveImage(mEngine, StringUtil.convertToUnicode(filename));
			if (ret == RET_OK) {
				return true;
			}
		}
		return false;
	}

	public boolean save(byte[] filename) {
		if (mImageEngine != null) {
			int ret = mImageEngine.saveImage(mEngine, filename);
			if (ret == RET_OK) {
				return true;
			}
		}
		return false;
	}

	public boolean setSaveParams(byte[] data, int width, int height, int components) {
		if (mImageEngine != null) {
			int ret = mImageEngine.setSaveParams(mEngine, data, width, height, components);
			if (ret == RET_OK) {
				return true;
			}
		}
		return false;
	}

	public boolean isGray() {
		if (mImageEngine != null) {
			int ret = mImageEngine.isGrayImage(mEngine);
			if (ret == 1) {
				return true;
			}
		}
		return false;
	}

	public int getWidth() {
		if (mImageEngine != null) {
			return mImageEngine.getImageWidth(mEngine);
		}
		return 0;
	}

	public int getHeight() {
		if (mImageEngine != null) {
			return mImageEngine.getImageHeight(mEngine);
		}
		return 0;
	}

	public int getComponent() {
		if (mImageEngine != null) {
			return mImageEngine.getImageComponent(mEngine);
		}
		return 0;
	}

	public byte[] getData() {
		if (mImageEngine != null) {
			return mImageEngine.getImageData(mEngine);
		}
		return null;
	}

	public long getDataEx() {
		if (mImageEngine != null) {
			return mImageEngine.getImageDataEx(mEngine);
		}
		return 0;
	}

	/**
	 * 保存缩放图像
	 * 
	 * @param src
	 *            图像原图路径
	 * @param dst
	 *            图像经过引擎过滤生成的新图像的保存路径
	 * @param dstWidth
	 *            保存图像的宽
	 * @param dstHeight
	 *            保存图像的高
	 * @return
	 */
	public boolean scale(String src, String dst, int dstWidth, int dstHeight) {
		if (mImageEngine != null) {
			int ret = mImageEngine.scale(StringUtil.convertToUnicode(src), StringUtil.convertToUnicode(dst), dstWidth,
					dstHeight);
			if (ret == RET_OK) {
				return true;
			}
		}
		return false;
	}

	public class ImageProperty {
		public int mWidth = 0;
		public int mHeight = 0;
		public int mComponent = 0;
	}

	public ImageProperty getProperty(String filename) {
		int ret = mImageEngine.getProperty(mEngine, StringUtil.convertToUnicode(filename));
		ImageProperty property = null;
		if (ret == RET_OK) {
			property = new ImageProperty();
			property.mWidth = getWidth();
			property.mHeight = getHeight();
			property.mComponent = getComponent();
		}
		return property;
	}
}
