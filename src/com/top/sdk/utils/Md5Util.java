package com.top.sdk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class Md5Util {
    public static boolean checkMd5(File file, String md5String) {
        if ((file == null) || (md5String == null)) {
            return false;
        }
        String hash = md5(file);
        return (hash != null) && (hash.equals(md5String));
    }

    public static String md5(File file) {
        if (!file.exists()) {
            return null;
        }
        FileInputStream in = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            int b;
            while ((b = in.read(buffer)) > 0) {
                md5.update(buffer, 0, b);
            }
            return getMd5String(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getMD5(String content) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(content.getBytes());
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
    
    private static String getMd5String(byte[] md) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        int j = md.length;
        char[] str = new char[j * 2];
        for (int i = 0; i < j; i++) {
            byte hex = md[i];
            str[(2 * i)] = hexDigits[(hex >>> 4 & 0xF)];
            str[(i * 2 + 1)] = hexDigits[(hex & 0xF)];
        }
        return new String(str);
    }
}