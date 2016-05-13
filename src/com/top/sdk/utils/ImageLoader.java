package com.top.sdk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

	private MemoryCache memoryCache = new MemoryCache();
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	private ExecutorService executorService;
	List<Map<String,Object>> mData=new ArrayList<Map<String,Object>>();
	public static ImageLoader getInstance(Context context){
		return new ImageLoader(context);
	}
	private ImageLoader(Context context) {
		executorService = Executors.newFixedThreadPool(5);
	}
		
	// 最主要的方法
	public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			LogUtil.d("test", "显示图片缓存中取到的图片--》"+bitmap);
			imageView.setImageBitmap(bitmap);
		}else{
    		// 先从文件缓存中查找是否有
			File f = FileUtil.getPushPicFile(url);
    		if (f != null && f.exists()){
    			Bitmap bmp = decodeFile(f);
				//显示SD卡中的图片缓存
        		if(bmp != null){
        			LogUtil.d("test", "显示图片时sd卡中取到的图片--》"+bmp);
        			imageView.setImageBitmap(bmp);
        		}
    		}else{
				//线程加载网络图片
    			LogUtil.d("test", "显示图片时网络取图片--》"+url);
    			queuePhoto(url, imageView);
        	}
		}
	}
	
	/*
	 * @type
	 * type == LOCK_ITEM_PICTURE     代表请求首页推荐列表的item小图片
	 * type == LOCK_SCREEN_PICTURE   代表请求锁屏的大图片
	 * type == LOCK_FOCUS_PICTURE    代表请求焦点图
	 * type == LOCK_DESK_ICON        代表请求锁屏小图
	 * type == LOCK_PUSH_PICTURE	 代表push请求的图片
	 */
	
	public void queuePhoto(String url, ImageView imageView) { 
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	@SuppressLint("NewApi") 
	private Bitmap getBitmap(String url) {
		LogUtil.d("test","getBitmap---url--->"+ url);
		File f = FileUtil.getPushPicFile(url);
		LogUtil.d("test","Bitmap---File--->"+ f);
		if(!f.getParentFile().exists()){
			boolean rtn = f.getParentFile().mkdirs();
		}
		// 先从缓存中取
		
		
		// 先从文件缓存中查找是否有
		Bitmap b = null;
		if (f != null && f.exists()){
			b = decodeFile(f);
			LogUtil.d("test", "文件中图片读取中--->"+f.getPath());
		}
		LogUtil.d("test", "文件中图片获取到的图片Bitmap"+b);
		if (b != null){
			return b;
		}
		// 最后从指定的url中下载图片
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			LogUtil.d("test", "网络取图片中。。。");
			if(!CopyStream(is, os) && f.exists()){
				LogUtil.d("test", "图片文件删除了。。。。");
				f.delete();
			}
			os.close();
			if(mILoadedFinishLister!=null){
				mILoadedFinishLister.dowloadOk(url);
			}
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			return null;
		}
	}
	
	private static long getFileSize(File file) throws Exception
	{
		long size = 0;
		 if (file.exists()){
		 FileInputStream fis = null;
		 fis = new FileInputStream(file);
		 size = fis.available();
		 }
		 else{
		 file.createNewFile();
		 Log.e("获取文件大小","文件不存在!");
		 }
		 return size;
	}
	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	public static Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			// Find the correct scale value. It should be the power of 2.
//			final int REQUIRED_SIZE = 200;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			String imageType = o.outMimeType;
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}
	
	
	
	public static Bitmap decodeFile(File f,int reqHeight, int reqWith) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			// Find the correct scale value. It should be the power of 2.
//			final int REQUIRED_SIZE = 200;
//			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
//			String imageType = o.outMimeType;
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}
	


	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		
		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			LogUtil.d("test", "photoToLoad.url--->"+photoToLoad.url);
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if(photoToLoad.imageView != null){
				LogUtil.i("test", "~~~~photoToLoad.imageView != null~~~~");
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				// 更新的操作放在UI线程中
//				Activity a = (Activity) photoToLoad.imageView.getContext();
//				a.runOnUiThread(bd);
				new Handler(photoToLoad.imageView.getContext().getMainLooper()).post(bd);
			}else{
//				LogUtil.i("jyh1", "~~~~photoToLoad.imageView = null~~~~");
//				mHandler.obtainMessage(Constants.GET_LOCKSCREEN_IMAGE, photoToLoad.url).sendToTarget();
			}
		}
	}
	
	

	/**
	 * 防止图片错位
	 * 
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
	
		}
	}

	public void clearCache() {
		memoryCache.clear();
	}

	public static boolean CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					return true;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			LogUtil.e("", "CopyStream catch Exception...");
			return false;
		}
	}
	
	interface ILoadedFinishLister{
		void dowloadOk(String url);
	}
	
	private ILoadedFinishLister mILoadedFinishLister;
	public ILoadedFinishLister getmILoadedFinishLister() {
		return mILoadedFinishLister;
	}
	public void setmILoadedFinishLister(ILoadedFinishLister mILoadedFinishLister) {
		this.mILoadedFinishLister = mILoadedFinishLister;
	}
	
	
	
	
}
