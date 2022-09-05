package com.yunmai.android.bcr.other;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;


public class CameraManager implements AutoFocusCallback {
	
	public static final int mWidth = 1600;
	public static final int mHeight = 1200;
	
//	private Context mContext;
	private Camera mCamera;
	private Handler mHandler;

	public CameraManager(Context context, Handler handler) {
		// TODO Auto-generated constructor stub
//		this.mContext = context;
		this.mHandler = handler;
	}

	public void openCamera(SurfaceHolder holder) throws RuntimeException, IOException {
		// TODO Auto-generated method stub
		if (mCamera == null) {
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(holder);
			setPictureSize();
		}
	}

	public void initDisplay() {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			mCamera.startPreview();
		}
	}

	public void closeCamera() {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	public void requestFocuse() {
		// TODO Auto-generated method stub
		if (mCamera != null) {
				mCamera.autoFocus(this);
		}
	}
	
	/**
	 * 設置預覽大小
	 * @param width
	 * @param height
	 */
	public void setPreviewSize(int width, int height) {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			Camera.Parameters parameters = mCamera.getParameters();
			List<Size> previewSize = parameters.getSupportedPreviewSizes();
			Collections.sort(previewSize, new SizeComparator());
			if (previewSize != null) {
				int size = previewSize.size();
				int picIndex = 0;
				for (int i = 0; i < size; i++) {
					if (previewSize.get(i).width == width) {
						picIndex = i;
						break;
					} else if (previewSize.get(i).width < width) {
						picIndex = i - 1;
						if (picIndex < 0) {
							picIndex = 0;
						}
						int diffW1 = previewSize.get(picIndex).width - width;
						int diffW2 = width - previewSize.get(i).width;
						if (diffW1 > diffW2) {
							picIndex = i;
						}
						break;
					}
				}
				if (Build.MODEL.startsWith("MI-ONE")) {
					if (Build.VERSION.INCREMENTAL.equals("2.10.12")) {
						parameters.setPreviewSize(640, 480);
					} else {
						parameters.setPreviewSize(1280, 720);
					}
				} else {
					parameters.setPreviewSize(previewSize.get(picIndex).width,
							previewSize.get(picIndex).height);
				}
			}
			mCamera.setParameters(parameters); 
		}
	}
	
	/**
	 * 設置照片大小
	 */
	private void setPictureSize() {

		// TODO Auto-generated method stub
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPictureFormat(ImageFormat.JPEG);
		List<Size> pictureSize = parameters.getSupportedPictureSizes();
		Collections.sort(pictureSize, new SizeComparator());
		if (pictureSize != null) {
			int size = pictureSize.size();
			int picIndex = 0;
			for (int i = 0; i < size; i++) {
				if (pictureSize.get(i).width == mWidth) {
					picIndex = i;
					break;
				} else if (pictureSize.get(i).width < mWidth) {
					picIndex = i - 1;
					if (picIndex < 0) {
						picIndex = 0;
					}
					int diffW1 = pictureSize.get(picIndex).width - mWidth;
					int diffW2 = mWidth - pictureSize.get(i).width;
					if (diffW1 > diffW2 && pictureSize.get(i).width > 1280) {
						picIndex = i;
					}
					break;
				}
			}
			parameters.setPictureSize(pictureSize.get(picIndex).width,
					pictureSize.get(picIndex).height);
		}
		mCamera.setParameters(parameters); 
	
	}
	
	/**
	 * 设置闪关灯模式
	 * @param mode
	 */
	public void setCameraFlashMode(String mode) {
		// TODO Auto-generated method stub
		Camera.Parameters parameters = mCamera.getParameters(); 
		parameters.setFlashMode(mode); 
		mCamera.setParameters(parameters); 
	}
	
	/**
	 * 是否支持该模式闪关
	 * @param mode	闪光模式
	 * @return
	 */
	public boolean isSupportFlash(String mode) {
		// TODO Auto-generated method stub
		List<String> modes = mCamera.getParameters().getSupportedFlashModes();
		if (mCamera == null || modes == null || !modes.contains(mode)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取默认闪关灯模式
	 * @return
	 */
	public String getDefaultFlashMode() {
		// TODO Auto-generated method stub
		if (mCamera.getParameters().getSupportedFlashModes() != null) {
			return mCamera.getParameters().getSupportedFlashModes().get(0);
		}
		return Parameters.FLASH_MODE_OFF;
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		// TODO Auto-generated method stub
		takePicture(success);
	}
	
	private void takePicture(boolean captureOnly) throws RuntimeException {
		if (mCamera != null) {
			try {
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setJpegQuality(40);
				mCamera.setParameters(parameters);
				mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
			} catch (RuntimeException e) {
			}
		}
	}
	
	private ShutterCallback shutterCallback = new ShutterCallback() {
		
		public void onShutter() {
			/* 按下快门瞬间会调用这里的程序 */
		}
		
	};

	private PictureCallback rawCallback = new PictureCallback() {
		
		public void onPictureTaken(byte[] _data, Camera _camera) {
			/* 要处理raw data */
		}
		
	};

	private PictureCallback jpegCallback = new PictureCallback() {
		
		public void onPictureTaken(byte[] _data, Camera _camera) {
			if(false){
				   Message msg = mHandler.obtainMessage();
			        msg.what = 1;
			        msg.obj = _data;
			        mHandler.sendMessage(msg);
			}else{
				Bitmap bm = null ;
				BufferedOutputStream bos = null;
				try {
					String imageName = FileUtil.newImageName();
					bm = BitmapFactory.decodeByteArray(_data, 0, _data.length);
					File imageFile = new File("/sdcard/yunmaiocr", imageName);
			        bos = new BufferedOutputStream(new FileOutputStream(imageFile));
			        bm.compress(Bitmap.CompressFormat.JPEG, 30, bos);
			        bos.flush();
			        Message msg = new Message();
			        msg.what = 5;
			        msg.obj = imageFile.getPath();
			        mHandler.sendMessage(msg);
				} catch(FileNotFoundException ex){
					mHandler.sendEmptyMessage(6);
				} catch(Exception e) {
					mHandler.sendEmptyMessage(0);
				} finally{
					if(bm != null){
						  bm.recycle();
						  bm = null;
					}
					if(bos != null){
						try {
							bos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	};
	
	/**
	 * 倒序
	 */
	public class SizeComparator implements Comparator<Size> {

		@Override
		public int compare(Size s1, Size s2) {
			return s2.width*s2.height - s1.width*s1.height;
		}
		
	}

}
