package com.mining.app.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.electronicBusiness.R;
import com.google.zxing.ResultPoint;
import com.mining.app.zxing.camera.CameraManager;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 */
public final class ViewfinderView extends View {
	private static final String TAG = "log";
	/**
	 * 刷锟铰斤拷锟斤拷锟绞憋拷锟�
	 */
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;

	/**
	 * 锟侥革拷锟斤拷色锟竭角讹拷应锟侥筹拷锟斤拷
	 */
	private int ScreenRate;
	
	/**
	 * 锟侥革拷锟斤拷色锟竭角讹拷应锟侥匡拷锟�
	 */
	private static final int CORNER_WIDTH = 10;
	/**
	 * 扫锟斤拷锟斤拷械锟斤拷屑锟斤拷叩目锟斤拷
	 */
	private static final int MIDDLE_LINE_WIDTH = 6;
	
	/**
	 * 扫锟斤拷锟斤拷械锟斤拷屑锟斤拷叩锟斤拷锟缴拷锟斤拷锟斤拷锟揭的硷拷隙
	 */
	private static final int MIDDLE_LINE_PADDING = 5;
	
	/**
	 * 锟叫硷拷锟斤拷锟斤拷锟斤拷每锟斤拷刷锟斤拷锟狡讹拷锟侥撅拷锟斤拷
	 */
	private static final int SPEEN_DISTANCE = 5;
	
	/**
	 * 锟街伙拷锟斤拷锟斤拷幕锟杰讹拷
	 */
	private static float density;
	/**
	 * 锟斤拷锟斤拷锟叫�
	 */
	private static final int TEXT_SIZE = 16;
	/**
	 * 锟斤拷锟斤拷锟斤拷锟缴拷锟斤拷锟斤拷锟斤拷木锟斤拷锟�
	 */
	private static final int TEXT_PADDING_TOP = 30;
	
	/**
	 * 锟斤拷锟绞讹拷锟斤拷锟斤拷锟斤拷锟�
	 */
	private Paint paint;
	
	/**
	 * 锟叫间滑锟斤拷锟竭碉拷锟筋顶锟斤拷位锟斤拷
	 */
	private int slideTop;
	
	/**
	 * 锟叫间滑锟斤拷锟竭碉拷锟斤拷锥锟轿伙拷锟�
	 */
	private int slideBottom;
	
	/**
	 * 锟斤拷扫锟斤拷亩锟轿拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫伙拷锟斤拷锟斤拷锟斤拷锟杰ｏ拷锟斤拷时锟斤拷锟斤拷锟斤拷
	 */
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	
	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	boolean isFirst;
	
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		density = context.getResources().getDisplayMetrics().density;
		//锟斤拷锟斤拷锟斤拷转锟斤拷锟斤拷dp
		ScreenRate = (int)(20 * density);

		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);

		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@Override
	public void onDraw(Canvas canvas) {
		//锟叫硷拷锟缴拷锟斤拷锟斤拷要锟睫革拷扫锟斤拷锟侥达拷小锟斤拷去CameraManager锟斤拷锟斤拷锟睫革拷
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		
		//锟斤拷始锟斤拷锟叫硷拷锟竭伙拷锟斤拷锟斤拷锟斤拷锟较边猴拷锟斤拷锟铰憋拷
		if(!isFirst){
			isFirst = true;
			slideTop = frame.top;
			slideBottom = frame.bottom;
		}
		
		//锟斤拷取锟斤拷幕锟侥匡拷透锟�
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		
		//锟斤拷锟斤拷扫锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷影锟斤拷锟街ｏ拷锟斤拷锟侥革拷锟斤拷锟街ｏ拷扫锟斤拷锟斤拷锟斤拷锟芥到锟斤拷幕锟斤拷锟芥，扫锟斤拷锟斤拷锟斤拷锟芥到锟斤拷幕锟斤拷锟斤拷
		//扫锟斤拷锟斤拷锟斤拷锟斤拷娴斤拷锟侥伙拷锟竭ｏ拷扫锟斤拷锟斤拷锟揭边碉拷锟斤拷幕锟揭憋拷
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);
		
		

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			//锟斤拷扫锟斤拷锟斤拷锟较的角ｏ拷锟杰癸拷8锟斤拷锟斤拷锟斤拷
			paint.setColor(Color.GREEN);
			canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
					+ ScreenRate, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - ScreenRate,
					frame.left + CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
					frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
					frame.right, frame.bottom, paint);

			
			//锟斤拷锟斤拷锟叫硷拷锟斤拷锟�,每锟斤拷刷锟铰斤拷锟芥，锟叫硷拷锟斤拷锟斤拷锟斤拷锟斤拷贫锟絊PEEN_DISTANCE
			slideTop += SPEEN_DISTANCE;
			if(slideTop >= frame.bottom){
				slideTop = frame.top;
			}
			canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH/2, frame.right - MIDDLE_LINE_PADDING,slideTop + MIDDLE_LINE_WIDTH/2, paint);
			
			
			//锟斤拷扫锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
			paint.setColor(Color.WHITE);
			paint.setTextSize(TEXT_SIZE * density);
			paint.setAlpha(0x40);
			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
			paint.setTextAlign(Align.CENTER);
			canvas.drawText(getResources().getString(R.string.scan_text), (frame.left+frame.right)/2, (float) (frame.bottom + (float)TEXT_PADDING_TOP *density), paint);
			
			

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 3.0f, paint);
				}
			}

			
			//只刷锟斤拷扫锟斤拷锟斤拷锟斤拷锟捷ｏ拷锟斤拷锟斤拷锟截凤拷锟斤拷刷锟斤拷
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
			
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
