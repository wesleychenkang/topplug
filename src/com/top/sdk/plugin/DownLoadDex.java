package com.top.sdk.plugin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;

import com.top.sdk.utils.LogUtil;

public class DownLoadDex {

	public boolean downLoadFile(String downloadUrl) {
		InputStream is = null;
		OutputStream os = null;
		try {

			URL url = new URL(downloadUrl);
			// 打开连接
			URLConnection conn = url.openConnection();
			// 打开输入流
			is = conn.getInputStream();
			// 获得长度
			int contentLength = conn.getContentLength();
			// 创建文件夹 MyDownLoad，在存储卡下
			String dirName = Environment.getExternalStorageDirectory()
					+ "/Android/data";
			File file = new File(dirName);
			// 不存在创建
			if (!file.exists()) {
				file.mkdir();
			}
			// 下载后的文件名
			String fileName = dirName + "a.jar";
			File file1 = new File(fileName);
			if (file1.exists()) {
				file1.delete();
			}
			// 创建字节流
			byte[] bs = new byte[1024];
			int len;
			os = new FileOutputStream(fileName);
			// 写数据
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			return true;
			// 完成后关闭流

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {

			LogUtil.d("download-finish");
			try {
				if(os!=null)
				os.close();
				if(is!=null)
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
