package com.top.sdk.view;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.top.sdk.entity.PopData;
import com.top.sdk.log.LogUtil;
import com.top.sdk.plugservice.ClickCallBack;
import com.top.sdk.resuorces.ResourceLoader;
import com.top.sdk.utils.FileUtil;
import com.top.sdk.utils.ImageLoader;
import com.top.sdk.utils.MetricUtil;

public class PopView extends FrameLayout implements OnClickListener {
	private ImageView imgAd;
	private ImageView imgMove;
	private ClickCallBack callBack;
	private final int imgAdId = 0x1233432;
	private final int imgRemoveId = 0x1233437;
	private static int screenWidth;
	private static int screenHeight;
	private int controlHeight;
	private int controlWidth;
	private Context context;
	private Bitmap screenImageBitmap = null;

	public PopView(Context context) {
		super(context);
		this.context = context;
	}

	@SuppressLint("NewApi")
	private void initView(Context context, int width, int height) {
		setBackgroundColor(Color.TRANSPARENT);

		FrameLayout lay = new FrameLayout(context);
		LayoutParams framelp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		framelp.gravity = Gravity.CENTER;
		addView(lay, framelp);

		imgAd = new ImageView(context);
		BitmapDrawable b = new BitmapDrawable(getResources(), screenImageBitmap);
		if (screenImageBitmap != null)
			imgAd.setBackground(b);
		imgAd.setId(imgAdId);
		imgAd.setOnClickListener(this);

		LayoutParams lpAd = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		// lpAd.gravity = Gravity.CENTER;
		lay.addView(imgAd, lpAd);

		Drawable drawable_move = ResourceLoader.getBitmapDrawable("lock_btn_remove_normal.png");
		imgMove = new ImageView(context);
		imgMove.setBackground(drawable_move);
		imgMove.setId(imgRemoveId);
		imgMove.setOnClickListener(this);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.height = MetricUtil.getDip(getContext(), 20);
		lp.width = MetricUtil.getDip(getContext(), 20);
		lp.gravity = Gravity.RIGHT;
		lay.addView(imgMove, lp);
		
//		TextView txt = new TextView(context);
//		txt.setText("1");
//		txt.setTextSize(50);
//		txt.setTextColor(Color.WHITE);
//		txt.setPadding(10, 10, 10, 10);
//		txt.setBackgroundColor(Color.BLACK);
//		txt.getBackground().setAlpha(125);
//		LayoutParams lp_text = new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//		lp_text.gravity = Gravity.CENTER;
//		lp_text.topMargin = MetricUtil.getDip(context, 50);
//		lay.addView(txt,lp_text);
		
	}

	public boolean setADContent(PopData popData) {
		if(null==popData){
		  return false;
		}
		boolean reuslt = calculateTargetWidthAndHeight(popData.getImgUrl());
		if (!reuslt) {
			// 计算图片结果失败直接返回;
			return false;
		}
		// 填充图片内容
		initView(context, controlWidth, controlHeight);

		return true;
	}

	// 控制目标广告的高度与宽度

	private boolean calculateTargetWidthAndHeight(String url) {
		calculateScreenWidthAndHeight(); // 去得屏幕的宽度与高度
		int controlAreaHeight = screenWidth;
		int controlAreaWidth = screenHeight;
		if(TextUtils.isEmpty(url)){
			LogUtil.d("图片url获取为空");
			return false;
		}
		url = url.split(",")[0];
		Bitmap screenBitmap = getAdBitmap(url);
		if (screenBitmap == null) {
			ImageLoader imagerLoader = ImageLoader.getInstance(context);
			imagerLoader.queuePhoto(url, null);
			return false;
		}
		int picWidth = screenBitmap.getWidth();
		int picHeight = screenBitmap.getHeight();
		float picWidthAndHeightRatio = 1.0f * picWidth / picHeight; // 图片的宽度与高度的比例
		float contolWidthAndHeightRatio = 1.0f * controlAreaWidth // 显示控制的区域的宽度与高度的比例
				/ controlAreaHeight;

		int newHeight = 0, newWidth = 0;

		if (picWidthAndHeightRatio > contolWidthAndHeightRatio) {
			newWidth = controlAreaWidth;
			newHeight = controlAreaHeight;
		} else {
			// 宽图
			newHeight = controlAreaHeight;
			newWidth = (int) (1.0 * newHeight * picWidth / picHeight);

		}
		controlHeight = newHeight;
		controlWidth = newWidth;
		return true;
	}

	private Bitmap getAdBitmap(String url) {
		if(TextUtils.isEmpty(url)){
			return null;
		}
		if (screenImageBitmap != null) {
			return screenImageBitmap;
		}
		File file = FileUtil.getPushPicFile(url);
		if (file != null && file.exists()) {
			screenImageBitmap = ImageLoader.decodeFile(file);
		}
		return screenImageBitmap;
	}

	// 计算一下屏幕的宽与高
	private void calculateScreenWidthAndHeight() {
		// TODO Auto-generated method stub
		if (screenWidth != 0 && screenHeight != 0) {
			// 已经计算过了
			return;
		}
		WindowManager window = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		window.getDefaultDisplay().getMetrics(metrics);
		if (metrics.heightPixels > metrics.widthPixels) {
			screenHeight = metrics.heightPixels;
			screenWidth = metrics.widthPixels;
		} else {
			screenHeight = metrics.widthPixels;
			screenWidth = metrics.heightPixels;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case imgAdId:
			if (callBack != null) {
				callBack.download();
			}
			break;
		case imgRemoveId:
			if (callBack != null) {
				callBack.remove();
			}
			break;
		}
	}

	public void setClickCallBack(ClickCallBack callBack) {
		this.callBack = callBack;
	}



}
