package com.top.sdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.top.sdk.entity.Constants;

/**
 * 文件帮助�?
 * 
 * @author kinsgame
 */
public class FileUtil {
	private static final int BYTE_BUFFER_SIZE = 256;
	private static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/";

	public static boolean haveSDCard() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}


	public static boolean saveToFile(String content, String path){
		if (path == null) {
			return false;
		}
		if(content == null){
			content = "";
		}
		if (!haveSDCard())
			return false;
		File file = new File(path);
		if (file.exists())
			file.delete();
		FileOutputStream fos = null;
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public static boolean appendToFile(String content, String path){
		if (path == null) {
			return false;
		}
		if(content == null){
			content = "";
		}
		if (!haveSDCard())
			return false;
		File file = new File(path);
		FileOutputStream fos = null;
		try {
			file.createNewFile();
			fos = new FileOutputStream(file, true);
			fos.write(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	

	

	

	private static final boolean Use_Also_Data_Dir_Cache_APK_If_Sdcard_Not_Avaiable = true;

	@SuppressWarnings("unused")

	/**
	 * 从网络上获得图片
	 */
	public static byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection httpURLconnection = (HttpURLConnection) url
				.openConnection();
		httpURLconnection.setRequestMethod("GET");
		httpURLconnection.setReadTimeout(6 * 1000);
		httpURLconnection.setConnectTimeout(6 * 1000);
		InputStream in = null;
		if (httpURLconnection.getResponseCode() == 200) {
			in = httpURLconnection.getInputStream();
			byte[] result = readStream(in);
			in.close();
			return result;

		}
		return null;
	}

	public static byte[] readStream(InputStream in) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
		in.close();
		return outputStream.toByteArray();
	}

	public static String getProcessName(int pid) {
		String processName = null;
		try {
			if (processName == null) {
				processName = FileUtil.readFile("/proc/" + pid + "/cmdline",
						'\0');
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processName;
	}

	public static byte[] readFile(String file) {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream(
				BYTE_BUFFER_SIZE);
		byte[] buffer = new byte[BYTE_BUFFER_SIZE];
		byte[] result = null;
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			int len = -1;
			while ((len = is.read(buffer)) > 0) {
				byteBuffer.write(buffer, 0, len);
			}
			result = byteBuffer.toByteArray();
		} catch (Exception e) {
			return null;
		}finally {
			if(byteBuffer != null){
				try {
					byteBuffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	

	public static String readFile(String file, char endBit) {
		byte[] b = readFile(file);
		if (b == null) {
			return null;
		}
		for (int i = 0; i < b.length; i++) {
			if (endBit == b[i]) {
				return new String(b, 0, i);
			}
		}
		return new String(b);
	}

	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}
	
	/**
	 * 写文本文件
	 * 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * @param context
	 * @param msg
	 */
	public static void writeToData(Context context, String fileName, String content) 
	{ 
		if( content == null )	content = "";
		
		FileOutputStream fos = null;
		try 
		{
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write( content.getBytes() ); 
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 读取文本文件
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String readFromData( Context context, String fileName ) 
	{
		try 
		{
			FileInputStream in = context.openFileInput(fileName);
			String content = readInStream(in); 
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	} 
	
	
	private static String readInStream(FileInputStream inStream)
	{
		try 
		{
		   byte[] buffer = new byte[512];
		   int length = -1;
		   StringBuilder sb = new StringBuilder();
		   while((length = inStream.read(buffer)) != -1 )
		   {
			   sb.append(new String(buffer, 0, length));
		   }
		   inStream.close();
		   String rtn = sb.toString();
		   return rtn;
		} catch (IOException e)	{
		   Log.i("FileTest", e.getMessage()); 
		   return null;
		}
	}

	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int n = 0;
			while (((n = input.read(buffer))) != -1) {
				output.write(buffer, 0, n);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	public File createSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	public File createSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}
	
	public static int getTotalLines(String fileName) {
        FileReader in;
		try {
			in = new FileReader(fileName);
			LineNumberReader reader = new LineNumberReader(in);
	        String strLine = reader.readLine();
	        int totalLines = 0;
	        while (strLine != null) {
	            totalLines++;
	            strLine = reader.readLine();
	        }
	        reader.close();
	        in.close();
	        return totalLines;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
        
    }

	// 复制文件
	public static void copyFile(InputStream is, File targetFile)
			throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(is);
			if (targetFile.exists()) {
				targetFile.delete();
			}
			targetFile.createNewFile();
			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出�?
			outBuff.flush();
		} finally {
			// 关闭�?
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	/**
	 * 把图片压缩到200K
	 *
	 * @param oldpath
	 *            压缩前的图片路径
	 * @param newPath
	 *            压缩后的图片路径
	 * @return
	 */
	/**
	 * 把图片压缩到200K
	 *
	 * @param oldpath
	 *            压缩前的图片路径
	 * @param newPath
	 *            压缩后的图片路径
	 * @return
	 */
	public static File compressFile(String oldpath, String newPath) {
		Bitmap compressBitmap = decodeFile(oldpath);
		Bitmap newBitmap = ratingImage(oldpath, compressBitmap);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		newBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		byte[] bytes = os.toByteArray();

		File file = null ;
		try {
			file = getFileFromBytes(bytes, newPath);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(newBitmap != null ){
				if(!newBitmap.isRecycled()){
					newBitmap.recycle();
				}
				newBitmap  = null;
			}
			if(compressBitmap != null ){
				if(!compressBitmap.isRecycled()){
					compressBitmap.recycle();
				}
				compressBitmap  = null;
			}
		}
		return file;
	}

	private static Bitmap ratingImage(String filePath,Bitmap bitmap){
		int degree = readPictureDegree(filePath);
		return rotaingImageView(degree, bitmap);
	}

	/**
	 *  旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();;
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 把字节数组保存为一个文件
	 *
	 * @param b
	 * @param outputFile
	 * @return
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		File ret = null;
		BufferedOutputStream stream = null;
		try {
			ret = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			// log.error("helper:get file from byte process error!");
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// log.error("helper:get file from byte process error!");
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * 图片压缩
	 *
	 * @param fPath
	 * @return
	 */
	public static Bitmap decodeFile(String fPath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inDither = false; // Disable Dithering mode
		opts.inPurgeable = true; // Tell to gc that whether it needs free
		opts.inInputShareable = true; // Which kind of reference will be used to
		BitmapFactory.decodeFile(fPath, opts);
		final int REQUIRED_SIZE = 200;
		int scale = 1;
		if (opts.outHeight > REQUIRED_SIZE || opts.outWidth > REQUIRED_SIZE) {
			final int heightRatio = Math.round((float) opts.outHeight
					/ (float) REQUIRED_SIZE);
			final int widthRatio = Math.round((float) opts.outWidth
					/ (float) REQUIRED_SIZE);
			scale = heightRatio < widthRatio ? heightRatio : widthRatio;//
		}
		Log.i("scale", "scal ="+ scale);
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scale;
		Bitmap bm = BitmapFactory.decodeFile(fPath, opts).copy(Bitmap.Config.ARGB_8888, false);
		return bm;
	}



	/**
	 * 创建目录
	 * @param path
	 */
	public static void setMkdir(String path)
	{
		if(TextUtils.isEmpty(path)){
			return;
		}
		File file = new File(path);
		if(!file.exists())
		{
			file.mkdirs();
			Log.e("file", "目录不存在  创建目录    ");
		}else{
			Log.e("file", "目录存在");
		}
	}

	/**
	 * 获取目录名称
	 * @param url
	 * @return FileName
	 */
	public static String getFileName(String url)
	{
		int lastIndexStart = url.lastIndexOf("/");
		if(lastIndexStart!=-1)
		{
			return url.substring(lastIndexStart+1, url.length());
		}else{
			return null;
		}
	}

	/**
	 * 删除该目录下的文件
	 *
	 * @param path
	 */
	public static void delFile(String path) {
		if (!TextUtils.isEmpty(path)) {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	
	public static File getPushPicFile(String url) {
		String filename = String.valueOf(url.hashCode());
		File f = new File(Constants.FOLDER_PUSHPIC + filename);
		return f;
	}
	
}
