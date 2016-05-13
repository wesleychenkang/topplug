package com.top.sdk.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import android.util.Base64;

public class DESCoder {

	// 编码
	private static String encoding = "UTF-8";

	private static native String JniGetpublickey();

	private static native String JniGetUrl();

	private static String publicKey;
	private static String initUrl;

	static {
	}

	public static String getPublicKey() {
//		if (StringUtil.isEmpty(DESCoder.publicKey)) {
//			DESCoder.publicKey = DESCoder.JniGetpublickey();
//		}
//		LogUtil.i("DESCoder", "DESCoder.publicKey-->" + DESCoder.publicKey);
//		return DESCoder.publicKey;
		return "scc8754DES";
	}

	public static String getInitUrl() {
//		if (StringUtil.isEmpty(DESCoder.initUrl)) {
//			DESCoder.initUrl = DESCoder.JniGetUrl();
//		}
//		LogUtil.i("DESCoder", "DESCoder.initUrl-->" + DESCoder.initUrl);
//		return DESCoder.initUrl;
		return "http://sdk.vsoyou.net/api/init.htm";
	}

	/**
	 * 加密字符串
	 */
	public static String ebotongEncrypto( String str) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = symmetricEncrypto(getPublicKey(),
						str.getBytes(encoding));
				result = Base64.encodeToString(encodeByte, 0);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 解密字符串
	 */
	public static String ebotongDecrypto(String str) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = Base64.decode(str, 0);

				byte[] decoder = symmetricDecrypto(getPublicKey(), encodeByte);
				result = new String(decoder, encoding);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 对称加密字节数组并返回
	 * 
	 * @param byteSource
	 *            需要加密的数据
	 * @return 经过加密的数据
	 * @throws Exception
	 */
	public static byte[] symmetricEncrypto(String mKey, byte[] byteSource)
			throws Exception {
		try {
			int mode = Cipher.ENCRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			byte[] keyData = mKey.getBytes();
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);

			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	/**
	 * 对称解密字节数组并返回
	 * 
	 * @param byteSource
	 *            需要解密的数据
	 * @return 经过解密的数据
	 * @throws Exception
	 */
	public static byte[] symmetricDecrypto(String mKey, byte[] byteSource)
			throws Exception {
		try {
			int mode = Cipher.DECRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			byte[] keyData = mKey.getBytes();
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

}
