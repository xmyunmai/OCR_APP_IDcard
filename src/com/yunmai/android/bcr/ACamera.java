/*
 * File Name: 		ACamera.java
 * 
 * Copyright(c) 2011 Yunmai Co.,Ltd.
 * 
 * 		 All rights reserved.
 * 					
 */

package com.yunmai.android.bcr;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.params.CoreConnectionPNames;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import cc.ocr.yunapp.idcard.R;

import com.umeng.analytics.MobclickAgent;
import com.yunmai.android.bcr.engine.ImageEngine;
import com.yunmai.android.bcr.other.CameraManager;
import com.yunmai.android.bcr.other.FileUtil;
import com.yunmai.android.bcr.other.XMLFunctions;
import com.yunmai.android.bcr.update.UpdateManager;
import com.yunmai.android.bcr.update.VersionEntity;

/**
 * 拍摄图像
 * 
 * @author fangcm 2013-08-19
 * 
 */
public class ACamera extends Activity implements SurfaceHolder.Callback {

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private ImageButton mShutter;
	private ImageButton mFlash;
	private byte[] jpegData = null;
	private CameraManager mCameraManager;
	// private String flashModel = Parameters.FLASH_MODE_AUTO;
	private String flashModel = Parameters.FLASH_MODE_OFF;
	private ProgressBar pb;
	private EditText etResult;
	// private EditText etAbout;
	private Button bt_s, bt_about, bt_import;
	private ImageView ivHead;
	private String[] items;
	private String[] action;
	private int position = 0;
	private Dialog dialog;
	private VersionEntity versionEntity;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 抓取图像成功
				jpegData = (byte[]) msg.obj;
				if (jpegData != null && jpegData.length > 0) {
					pb.setVisibility(View.VISIBLE);
					new Thread() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
							String result = "";
							if (position == 2 || position == 3) {
								result = Scan(action[position], jpegData, 2, 0, "jpg");
							} else {
								result = Scan(action[position], jpegData, "jpg");
							}
							Log.d("tag", position + "<----->>" + result);
							if (result.indexOf("<result>1</result>") == 0
									|| (position == 1 && result.indexOf("<result><status>1</status>") == 0)) {
								// try {
								// IDCardHandler handler = new IDCardHandler();
								// SAXParserFactory factory =
								// SAXParserFactory.newInstance();
								// SAXParser parser = factory.newSAXParser();
								// // parser.parse(result, handler);
								// parser.parse(new File("/sdcard/axxx.xml"),
								// handler);
								// result = handler.getResult();
								// } catch(Exception e) {
								// // TODO Auto-generated catch block
								// mHandler.sendMessage(mHandler.obtainMessage(3,
								// "-->"+e));
								// Log.d("tag", "-->"+e);
								// e.printStackTrace();
								// }
								// parser.parse(entity.getContent(), handler);
								// result = handler.getResult();

								switch (position) {
								case 0:
									result = xmlIdcard(result);
									break;
								case 1:
									result = xmlBankCard(result);
									break;
								case 2:
									result = xmlBCR(result);
									break;
								case 3:
									result = xmlDoc(result);
									break;
								case 4:
									result = xmlDriving(result);
									break;
								case 5:
									result = xmlDriver(result);
									break;
								case 6:
									result = xmlPassport(result);
									break;
								case 7:
									result = xmlPlate(result);
									break;

								default:
									break;
								}

								mHandler.sendMessage(mHandler.obtainMessage(4, result));
							} else if (result.equals("-2")) {
								mHandler.sendMessage(mHandler.obtainMessage(3, "请确认是否有连接网络！"));
							} else if (result.equals("<result>-1;time is out!</result>")) {
								mHandler.sendMessage(mHandler.obtainMessage(3, "请确认手机时间是否正常！"));
							} else {
								mHandler.sendMessage(mHandler.obtainMessage(3, "识别失败，请重新拍照！"));
							}
						}

					}.start();
				} else {
					mHandler.sendEmptyMessage(0);
				}
				break;
			case 3:// 识别失败
				pb.setVisibility(View.GONE);
				String str = String.valueOf(msg.obj);
				Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
				mCameraManager.initDisplay();
				mShutter.setEnabled(true);
				break;
			case 4:
				mCameraManager.initDisplay();
				mShutter.setEnabled(true);
				pb.setVisibility(View.GONE);
				String result = "" + msg.obj;
				String[] s = result.split("《头像地址》");
				if (s != null) {
					etResult.setText(s[0]);
					etResult.setVisibility(View.VISIBLE);
					if (s.length > 1) {
						ivHead.setImageURI(Uri.parse(s[1]));
						ivHead.setVisibility(View.VISIBLE);
					}
				}
				break;
			case 5:
				String path = "" + msg.obj;
				try {
					byte[] data = FileUtil.getBytesFromFile(path);
					String descpath = saveImage(path, data);
					byte[] data1 = FileUtil.getBytesFromFile(descpath);
					mHandler.sendMessage(mHandler.obtainMessage(1, data1));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mHandler.sendEmptyMessage(0);
				}
				break;
			case 6:
				pb.setVisibility(View.GONE);
				Toast.makeText(getBaseContext(), "请插入存储卡！", Toast.LENGTH_SHORT).show();
				mCameraManager.initDisplay();
				mShutter.setEnabled(true);
				break;
			default:// 拍照失败
				pb.setVisibility(View.GONE);
				Toast.makeText(getBaseContext(), "拍照失败", Toast.LENGTH_SHORT).show();
				mCameraManager.initDisplay();
				mShutter.setEnabled(true);
				break;
			}
		}

	};

	/**
	 * 压缩并保存图片
	 * 
	 * @param srcPath
	 * @param data
	 * @param desPath
	 *            保存压缩后的图片路径
	 * @throws IOException
	 */
	private String saveImage(String srcPath, byte[] data) throws IOException {
		String desPath = "/sdcard/yunmaiocr/scale";
		File dir = new File(desPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File desFile = new File(dir, FileUtil.newImageName());
		ImageEngine engine = new ImageEngine();
		desPath = desFile.getAbsolutePath();
		boolean init = engine.init(ImageEngine.IMG_COMPONENT_GRAY, 90);
		if (init) {
			if (engine.load(data, srcPath)) {
				engine.save(desPath);
				byte[] data1 = FileUtil.getBytesFromFile(desPath);
				engine.finalize();
			}
		}
		return desPath;
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ocr_camera);
		mCameraManager = new CameraManager(ACamera.this, mHandler);
		items = getResources().getStringArray(R.array.select_items);
		action = getResources().getStringArray(R.array.select_action);
		initViews();
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getBaseContext(), "请插入存储卡", Toast.LENGTH_LONG).show();
			finish();
		}

		File file = new File("/sdcard/yunmaiocr");
		if (!file.exists()) {
			file.mkdir();
		}
		checkUpdate();
	}

	/**
	 * 检查更新
	 */
	public void checkUpdate() {

		new Thread() {
			@Override
			public void run() {
				versionEntity = UpdateManager.getOnlineVersion();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (versionEntity != null) {
							PackageInfo info;
							try {
								String packageName = getPackageName();
								PackageManager manager = getPackageManager();
								info = manager.getPackageInfo(packageName, 0);
								int version = info.versionCode;
								if (version < Integer.valueOf(versionEntity.getVersionCode())) {

									UpdateManager.showUpdataDialog(ACamera.this, versionEntity);
								}
							} catch (NameNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				});

			};
		}.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		mCameraManager.initDisplay();
		if (requestCode == 101) {
			if (resultCode == Activity.RESULT_OK) {
				try {
					// isFromCamOrImage = true;
					String filePath = "";
					String mimeType = "";
					Uri uriData = data.getData();
					String scheme = uriData.getScheme();
					if ("content".equals(scheme)) {
						ContentResolver localContentResolver = getContentResolver();
						String[] columns = new String[2];
						columns[0] = "mime_type";
						columns[1] = "_data";
						Cursor rets = localContentResolver.query(uriData, columns, null, null, null);
						if (rets != null && rets.getCount() > 0) {
							rets.moveToFirst();
							mimeType = rets.getString(0);
							filePath = rets.getString(1);
						}
					} else {
						filePath = uriData.getPath();
						String Extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
						mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(Extension);
					}
					if ("image/jpeg".equals(mimeType)) {
						// FileUtil.getBytesFromFile(filePath);
						Message msg = new Message();
						msg.what = 5;
						msg.obj = filePath;
						mHandler.sendMessage(msg);
					} else {
						Toast.makeText(this, "请导入JPG格式图片", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "导入图片失败", Toast.LENGTH_SHORT).show();
				}
			} else {
				// Toast.makeText(this, "导入图片失败",Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void initViews() {
		// find view
		ivHead = (ImageView) findViewById(R.id.iv_head);
		mShutter = (ImageButton) findViewById(R.id.camera_shutter);
		mFlash = (ImageButton) findViewById(R.id.camera_flash);
		pb = (ProgressBar) findViewById(R.id.reco_recognize_bar);
		etResult = (EditText) findViewById(R.id.et_result);
		// etAbout = (EditText) findViewById(R.id.et_about);
		bt_about = (Button) findViewById(R.id.bt_about);
		bt_about.setOnClickListener(mLsnClick);
		bt_import = (Button) findViewById(R.id.camera_import);
		bt_import.setOnClickListener(mLsnClick);
		bt_s = (Button) findViewById(R.id.bt_s);
		bt_s.setOnClickListener(mLsnClick);
		bt_s.setText(items[position]);
		// set view
		mShutter.setOnClickListener(mLsnClick);
		mFlash.setOnClickListener(mLsnClick);

		mSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(ACamera.this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	private OnClickListener mLsnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.camera_shutter:
				mShutter.setEnabled(false);
				mCameraManager.requestFocuse();
				break;
			case R.id.camera_flash:

				break;
			case R.id.camera_import:
				Intent mIntent = new Intent(Intent.ACTION_PICK);
				mIntent.setType("image/*");
				startActivityForResult(mIntent, 101);
				break;
			case R.id.bt_about:
				Intent intent = new Intent(ACamera.this, AboutActivity.class);
				startActivity(intent);
				// etAbout.setVisibility(View.VISIBLE);
				break;
			case R.id.bt_s:
				if (dialog == null) {
					dialog = new AlertDialog.Builder(ACamera.this).setTitle("请选择要识别的类型")
							.setItems(items, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									position = which;
									bt_s.setText(items[position]);
								}
							}).create();
					dialog.setCanceledOnTouchOutside(true);
				}
				dialog.show();
				break;
			}
		}

	};

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Debug.i(TAG, "surfaceCreated");
		try {
			mCameraManager.openCamera(holder);
			if (flashModel == null || !mCameraManager.isSupportFlash(flashModel)) {
				flashModel = mCameraManager.getDefaultFlashMode();
			}
			mCameraManager.setCameraFlashMode(flashModel);
		} catch (RuntimeException e) {
			Toast.makeText(ACamera.this, R.string.camera_open_error, Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(ACamera.this, R.string.camera_open_error, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (width > height) {
			mCameraManager.setPreviewSize(width, height);
		} else {
			mCameraManager.setPreviewSize(height, width);
		}
		mCameraManager.initDisplay();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCameraManager.closeCamera();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (etResult.getVisibility() == View.VISIBLE) {
				etResult.setVisibility(View.GONE);
				ivHead.setVisibility(View.GONE);
				return true;
				// } else if (etAbout.getVisibility() == View.VISIBLE) {
				// etAbout.setVisibility(View.GONE);
				// return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	// 识别API的URL
	public static final String ENGINE_URL = "http://eng.ccyunmai.com:5008/SrvEngine";
	public static final String username = "";
	public static final String password = "";

	public static String Scan(String type, byte[] file, String ext) {
		String xml = getSendXML(type, ext);
		return send(xml, file);
	}

	public static String Scan(String type, byte[] file, int ocrLang, int keyLang, String ext) {
		String xml = getSendXML(type, ocrLang + "", keyLang + "", ext);
		return send(xml, file);
	}

	private static String getSendXML(String action, String ext) {
		ArrayList<String[]> arr = new ArrayList<String[]>();
		String key = UUID.randomUUID().toString();
		String time = String.valueOf(System.currentTimeMillis());
		String verify = MD5(action + username + key + time + password);
		arr.add(new String[] { "action", action });
		arr.add(new String[] { "client", username });
		arr.add(new String[] { "system", "test pc" });
		arr.add(new String[] { "key", key });
		arr.add(new String[] { "time", time });
		arr.add(new String[] { "verify", verify });
		arr.add(new String[] { "ext", ext });
		arr.add(new String[] { "header", "1" });
		return getXML(arr, false);
	}

	private static String getSendXML(String action, String ocrLang, String keyLang, String ext) {
		ArrayList<String[]> arr = new ArrayList<String[]>();
		String key = UUID.randomUUID().toString();
		String time = String.valueOf(System.currentTimeMillis());
		String verify = MD5(action + username + key + time + password);
		arr.add(new String[] { "action", action });
		arr.add(new String[] { "client", username });
		arr.add(new String[] { "system", "test pc" });
		arr.add(new String[] { "key", key });
		arr.add(new String[] { "time", time });
		arr.add(new String[] { "verify", verify });
		arr.add(new String[] { "ocrlang", ocrLang });
		arr.add(new String[] { "keylang", keyLang });
		arr.add(new String[] { "ext", ext });
		arr.add(new String[] { "er", "0" });
		return getXML(arr, false);
	}

	public static String getXML(ArrayList<String[]> arr, boolean IsUpper) {
		if (arr == null || arr.size() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		String tag = "";
		for (int idx = 0; idx < arr.size(); idx++) {
			tag = arr.get(idx)[0];
			if (IsUpper) {
				tag = tag.toUpperCase();
			}
			sb.append("<");
			sb.append(tag);
			sb.append(">");
			sb.append(arr.get(idx)[1]);
			// sb.append(XMLFunctions.code(arr.get(idx)[1]));
			sb.append("</");
			sb.append(tag);
			sb.append(">");
		}
		return sb.toString();
	}

	private static String send(String xml, byte[] file) {
		byte[] dest = new byte[xml.getBytes().length + file.length + "<file></file>".getBytes().length];
		int pos = 0;
		System.arraycopy(xml.getBytes(), 0, dest, pos, xml.getBytes().length);
		pos += xml.getBytes().length;
		System.arraycopy("<file>".getBytes(), 0, dest, pos, "<file>".getBytes().length);
		pos += "<file>".getBytes().length;
		System.arraycopy(file, 0, dest, pos, file.length);
		pos += file.length;
		System.arraycopy("</file>".getBytes(), 0, dest, pos, "</file>".getBytes().length);
		try {
			return httpClient(ENGINE_URL, dest);
		} catch (IOException e) {
			return "-1";
		}
	}

	public static String httpClient(String url, byte[] content) throws IOException {
		HttpClient httpClient = new HttpClient();
		HttpClientParams httparams = new HttpClientParams();
		httparams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);// 连接
		httparams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);// 传输
		httpClient.setParams(httparams);

		PostMethod method = new PostMethod(url);
		RequestEntity requestEntity = new ByteArrayRequestEntity(content);
		method.setRequestEntity(requestEntity);
		String responseBody = null;
		try {
			method.getParams().setContentCharset("utf-8");
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			int statusCode = httpClient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("\r\nMethod failed: " + method.getStatusLine() + ",url:\r\n" + url + "\r\n");
			}
			StringBuffer resultBuffer = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),
					method.getResponseCharSet()));

			// StringBuffer resultBuffer = new StringBuffer();
			// InputStream is = method.getResponseBodyAsStream();
			// BufferedReader in = new BufferedReader(new InputStreamReader(is,
			// method.getResponseCharSet()));
			//
			//
			// IDCardHandler handler = new IDCardHandler();
			// SAXParserFactory factory = SAXParserFactory.newInstance();
			// SAXParser parser = null;
			// try {
			// parser = factory.newSAXParser();
			// } catch(Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// parser.parse(is, handler);
			// Log.d("tag", "-2-->"+handler.getResult());

			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				resultBuffer.append(inputLine);
				resultBuffer.append("\r\n");
			}
			in.close();
			responseBody = resultBuffer.toString().trim();
		} catch (Exception e) {
			System.out.println(">>> http请求异常，url=" + url);
			e.printStackTrace();
			responseBody = "-2";
		} finally {
			if (method != null) {
				method.releaseConnection();
				method = null;
			}
			return responseBody;
		}

	}

	public static byte[] file2byte(File file) throws IOException {
		byte[] bytes = null;
		if (file != null) {
			InputStream is = new FileInputStream(file);
			int length = (int) file.length();
			if (length > Integer.MAX_VALUE) // 当文件的长度超过了int的最大值
			{
				System.out.println("this file is max ");
				return null;
			}
			bytes = new byte[length];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			// 如果得到的字节长度和file实际的长度不一致就可能出错了
			if (offset < bytes.length) {
				System.out.println("file length is error");
				return null;
			}
			is.close();
		}
		return bytes;
	}

	public final static String MD5(String pwd) {
		// 用于加密的字符
		char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = pwd.getBytes();

			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) { // i = 0
				byte byte0 = md[i]; // 95
				str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
				str[k++] = md5String[byte0 & 0xf]; // F
			}
			return new String(str);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String xmlIdcard(String xml) {
		String r1 = XMLFunctions.getXMLValue(xml, "data");
		StringBuffer sb = new StringBuffer("");
		sb.append("姓名：").append(XMLFunctions.getXMLValue(r1, "name")).append("\r\n");
		sb.append("证号：").append(XMLFunctions.getXMLValue(r1, "cardno")).append("\r\n");
		sb.append("性别：").append(XMLFunctions.getXMLValue(r1, "sex")).append("\r\n");
		sb.append("民族：").append(XMLFunctions.getXMLValue(r1, "folk")).append("\r\n");
		sb.append("出生：").append(XMLFunctions.getXMLValue(r1, "birthday")).append("\r\n");
		sb.append("地址：").append(XMLFunctions.getXMLValue(r1, "address")).append("\r\n");
		sb.append("签发机关：").append(XMLFunctions.getXMLValue(r1, "issue_authority")).append("\r\n");
		sb.append("有效期：").append(XMLFunctions.getXMLValue(r1, "valid_period")).append("\r\n");

		String pic = XMLFunctions.getXMLValue(r1, "header_pic");
		if (pic != null && !pic.equals("")) {
			byte[] ss = null;
			try {
				ss = decryptBASE64(pic);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 对字符串进行Base64解密
			if (ss != null) {
				String path = "/sdcard/yunmaiocr/" + FileUtil.newImageName();
				boolean s2 = byteArrayToFile(ss, path);// 文件流转成文件
				if (s2) {
					sb.append("《头像地址》").append(path);
				}
			}
		}
		return sb.toString();
	}

	private String xmlPlate(String xml) {
		String r1 = XMLFunctions.getXMLValue(xml, "data");
		StringBuffer sb = new StringBuffer("");
		sb.append("车牌：").append(XMLFunctions.getXMLValue(r1, "number")).append("\r\n");
		sb.append("颜色：").append(XMLFunctions.getXMLValue(r1, "color")).append("\r\n");
		sb.append("层数：").append(XMLFunctions.getXMLValue(r1, "layer")).append("\r\n");
		return sb.toString();
	}

	private String xmlBCR(String xml) {
		StringBuffer sb = new StringBuffer("");
		String r1 = XMLFunctions.getXMLValue(xml, "data");
		String r2[] = XMLFunctions.getXMLValues(r1, "f");
		if (r2 != null) {
			// for (int i = 0; i < r2.length; i++) {
			// r2[i]
			// }
			for (String str : r2) {
				if (XMLFunctions.getXMLValue(str, "n").equals("name")) {
					sb.append("姓名：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("company")) {
					sb.append("公司：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("department")) {
					sb.append("部门：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("jobtitle")) {
					sb.append("职称：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("tel_main")) {
					sb.append("电话：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("tel_mobile")) {
					sb.append("手机：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("tel_home")) {
					sb.append("家庭电话：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("tel_inter")) {
					sb.append("直播电话：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("fax")) {
					sb.append("传真：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("pager")) {
					sb.append("寻呼机：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("web")) {
					sb.append("网址：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("email")) {
					sb.append("邮箱：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("address")) {
					sb.append("地址：");
				} else if (XMLFunctions.getXMLValue(str, "n").equals("postcode")) {
					sb.append("邮编：");
				} else {
					sb.append("其它：");
				}
				String r3[] = XMLFunctions.getXMLValues(str, "v");
				for (String sr3 : r3) {
					sb.append(sr3).append(";");
				}
				sb.append("\r\n");
			}
		}

		// sb.append("姓名：").append(XMLFunctions.getXMLValue(r1,
		// "name")).append("\r\n");
		// sb.append("公司：").append(XMLFunctions.getXMLValue(r1,
		// "company")).append("\r\n");
		// sb.append("部门：").append(XMLFunctions.getXMLValue(r1,
		// "department")).append("\r\n");
		// sb.append("职称：").append(XMLFunctions.getXMLValue(r1,
		// "jobtitle")).append("\r\n");
		// sb.append("电话：").append(XMLFunctions.getXMLValue(r1,
		// "tel_main")).append("\r\n");
		// sb.append("手机：").append(XMLFunctions.getXMLValue(r1,
		// "tel_mobile")).append("\r\n");
		// sb.append("家庭电话：").append(XMLFunctions.getXMLValue(r1,
		// "tel_home")).append("\r\n");
		// sb.append("直播电话：").append(XMLFunctions.getXMLValue(r1,
		// "tel_inter")).append("\r\n");
		// sb.append("传真：").append(XMLFunctions.getXMLValue(r1,
		// "fax")).append("\r\n");
		// sb.append("传呼机：").append(XMLFunctions.getXMLValue(r1,
		// "pager")).append("\r\n");
		// sb.append("网址：").append(XMLFunctions.getXMLValue(r1,
		// "web")).append("\r\n");
		// sb.append("电邮：").append(XMLFunctions.getXMLValue(r1,
		// "email")).append("\r\n");
		// sb.append("地址：").append(XMLFunctions.getXMLValue(r1,
		// "address")).append("\r\n");
		// sb.append("邮编：").append(XMLFunctions.getXMLValue(r1,
		// "postcode")).append("\r\n");
		return sb.toString();
	}

	private String xmlDoc(String xml) {
		StringBuffer sb = new StringBuffer("");
		String r1 = XMLFunctions.getXMLValue(xml, "data");
		String r2[] = XMLFunctions.getXMLValues(r1, "o");
		for (String str : r2) {
			sb.append(XMLFunctions.getXMLValue(str, "r"));
		}

		return sb.toString();
	}

	private String xmlPassport(String xml) {
		String r1 = XMLFunctions.getXMLValue(xml, "data");
		StringBuffer sb = new StringBuffer("");
		sb.append("姓名：").append(XMLFunctions.getXMLValue(r1, "name")).append("\r\n");
		sb.append("中文姓名：").append(XMLFunctions.getXMLValue(r1, "nameCh")).append("\r\n");
		sb.append("护照号：").append(XMLFunctions.getXMLValue(r1, "cardno")).append("\r\n");
		sb.append("性别：").append(XMLFunctions.getXMLValue(r1, "sex")).append("\r\n");
		sb.append("出生日期：").append(XMLFunctions.getXMLValue(r1, "birthday")).append("\r\n");
		sb.append("地址：").append(XMLFunctions.getXMLValue(r1, "address")).append("\r\n");
		sb.append("签发地址：").append(XMLFunctions.getXMLValue(r1, "issueAuthority")).append("\r\n");
		sb.append("发证日期：").append(XMLFunctions.getXMLValue(r1, "issueDate")).append("\r\n");
		sb.append("有效期：").append(XMLFunctions.getXMLValue(r1, "validPeriod")).append("\r\n");
		sb.append("国籍：").append(XMLFunctions.getXMLValue(r1, "nation")).append("\r\n");
		sb.append("personalno：").append(XMLFunctions.getXMLValue(r1, "personalNo")).append("\r\n");
		return sb.toString();
	}

	private String xmlDriver(String xml) {
		String r1 = XMLFunctions.getXMLValue(xml, "data");
		StringBuffer sb = new StringBuffer("");
		sb.append("姓名：").append(XMLFunctions.getXMLValue(r1, "name")).append("\r\n");
		sb.append("证号：").append(XMLFunctions.getXMLValue(r1, "cardno")).append("\r\n");
		sb.append("性别：").append(XMLFunctions.getXMLValue(r1, "sex")).append("\r\n");
		sb.append("出生日期：").append(XMLFunctions.getXMLValue(r1, "birthday")).append("\r\n");
		sb.append("地址：").append(XMLFunctions.getXMLValue(r1, "address")).append("\r\n");
		sb.append("发证日期：").append(XMLFunctions.getXMLValue(r1, "issueDate")).append("\r\n");
		sb.append("国籍：").append(XMLFunctions.getXMLValue(r1, "nation")).append("\r\n");
		sb.append("准驾车型：").append(XMLFunctions.getXMLValue(r1, "drivingType")).append("\r\n");
		sb.append("有效日期：").append(XMLFunctions.getXMLValue(r1, "registerDate")).append("\r\n");
		return sb.toString();
	}

	private String xmlDriving(String xml) {
		String r1 = XMLFunctions.getXMLValue(xml, "data");
		StringBuffer sb = new StringBuffer("");
		sb.append("所有人：").append(XMLFunctions.getXMLValue(r1, "name")).append("\r\n");
		sb.append("号牌号码：").append(XMLFunctions.getXMLValue(r1, "cardno")).append("\r\n");
		sb.append("住址：").append(XMLFunctions.getXMLValue(r1, "address")).append("\r\n");
		sb.append("发证日期：").append(XMLFunctions.getXMLValue(r1, "issueDate")).append("\r\n");
		sb.append("车辆类型：").append(XMLFunctions.getXMLValue(r1, "vehicleType")).append("\r\n");
		sb.append("使用性质：").append(XMLFunctions.getXMLValue(r1, "useCharacte")).append("\r\n");
		sb.append("品牌型号：").append(XMLFunctions.getXMLValue(r1, "model")).append("\r\n");
		sb.append("车辆识别代码：").append(XMLFunctions.getXMLValue(r1, "vin")).append("\r\n");
		sb.append("发动机号码：").append(XMLFunctions.getXMLValue(r1, "enginePN")).append("\r\n");
		sb.append("注册日期：").append(XMLFunctions.getXMLValue(r1, "registerDate")).append("\r\n");
		return sb.toString();
	}

	private String xmlBankCard(String xml) {
		// String r1 = XMLFunctions.getXMLValue(xml, "data");
		StringBuffer sb = new StringBuffer("");
		sb.append("卡号：").append(XMLFunctions.getXMLValue(xml, "Num")).append("\r\n");
		sb.append("银行名称：").append(XMLFunctions.getXMLValue(xml, "Bankname")).append("\r\n");
		sb.append("银行卡名称：").append(XMLFunctions.getXMLValue(xml, "Cardname")).append("\r\n");
		sb.append("银行卡类型：").append(XMLFunctions.getXMLValue(xml, "Cardtype")).append("\r\n");
		return sb.toString();
	}

	public static byte[] decryptBASE64(String key) throws Exception {
		// return (new BASE64Decoder()).decodeBuffer(key);
		return Base64.decode(key, 0);
	}

	public static String encryptBASE64(byte[] key) throws Exception {
		// return (new BASE64Encoder()).encodeBuffer(key);
		return Base64.encodeToString(key, 0);
	}

	public static boolean byteArrayToFile(byte[] buff, String TO_PATH) {
		OutputStream os = null;
		try {
			File file = new File(TO_PATH);
			if (file.exists()) {
				file.delete();
			}
			os = new BufferedOutputStream(new FileOutputStream(file));
			os.write(buff);
			os.flush();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}
}