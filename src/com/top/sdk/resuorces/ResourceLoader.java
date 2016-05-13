package com.top.sdk.resuorces;

import java.io.InputStream;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.top.sdk.utils.LogUtil;

public class ResourceLoader {
	
	public static final String TAG = "ResourceLoader";

    public static String resourcePath =  "com/top/sdk/res/";

    public static InputStream getInputStream(String resourceName) {

        String resource = resourcePath + resourceName;
        try {
            return ResourceLoader.class.getClassLoader().getResourceAsStream(resource);
        } catch (Exception ex) {
        	LogUtil.e(TAG, "no resource file:" + resource);
        }
        return null;

    }

    public static Drawable getBitmapDrawable(String resourceName) {

        Drawable drawable = null;
        String resource = resourcePath + resourceName;
        try {
            InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(resource);
            drawable = BitmapDrawable.createFromStream(is, resource);
        } catch (Exception ex) {
        	LogUtil.e(TAG, "no resource file:" + resource);
        }
        return drawable;

    }

    public static Drawable getBitmapDrawableFromAssets(Context context, String resourceName) {

        Drawable drawable = null;
        try {
            InputStream is = context.getAssets().open(resourceName);
            drawable = is != null ? BitmapDrawable.createFromStream(is, resourceName) : null;
        } catch (Exception ex) {
        	LogUtil.e(TAG, "no resource file:" + resourceName);
        }
        return drawable;

    }

    public static Drawable getNinePatchDrawable(String resourceName) {

        Drawable drawable = null;
        String resource = resourcePath + resourceName;
        try {
            InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(resource);
            drawable = NinePatchTool.decodeDrawableFromStream(is);
        } catch (Exception ex) {
        	LogUtil.e(TAG, "no resource file:" + resource);
        }
        return drawable;

    }
}