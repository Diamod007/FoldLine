package com.magicwork.foldline;

import java.util.ArrayList;
import java.util.List;

import com.magic.foldline.R;

import android.R.color;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * ����View
 * 
 * @author licq
 *
 */
public class FoldLineView extends View {

	List<CrossAxisScale> crossAxis = new ArrayList<CrossAxisScale>();// ����̶�
	List<VerticalAxisScale> verticalAxis = new ArrayList<VerticalAxisScale>();// ����̶�
	List<FoldLinePoint> foldLinePoints = new ArrayList<FoldLinePoint>();// ���ߵ�

	private int foldLineColor;// ���ߵ���ɫ
	private int verticalAxisColor;// �������ɫ
	private int crossAxisColor;// �������ɫ
	private int crossAxisScaleTextColor;// ����̶����ֵ���ɫ
	private int crossAxisScaleTextSize;// ����̶����ֵĴ�С
	private int axisWidth;// �ݡ�������
	private int axisScaleWidth = 50;// �ݡ���̶ȿ��
	private boolean isFillMode;// �Ƿ������ģʽ
	private boolean isDrawFoldLineCircle;// �Ƿ�������ߵ��ԲȦ
	private int foldLineCircleRadius;// ���ߵ�ԲȦ�İ뾶
	private int foldLineCircleColor;// ���ߵ�ԲȦ����ɫ
	private int foldLineWidth = 5;// ���ߵĿ��
	private int foldLineOuterColor;

	private int foldLineCount = 10;// ÿ����ʾ����������
	private int startIndex;// �������ߵ����ʼλ��
	private int endIndex;// �������ߵ�Ľ���λ��
	private int foldLineDis;// ���Ƶļ������

	private Paint verticalAxisPaint;
	private Paint crossAxisPaint;
	private Paint crossAxispScalePaint;
	private Paint verticalAxisTextPaint;
	private Paint crcossAxisTextPaint;
	private Paint foldLinePaint;
	private Paint foldLineOutPaint;
	private Paint foldLineCirclePaint;
	private Paint foldLineCircleInnerPaint;
	private Paint foldLinePointTextPaint;
	private int foldLineAreaWidth;// ����ͼ�Ŀ��

	private int moveX = -100;

	private int lastTouchX;
	private int touchSlop;

	private int clickX;
	private int clickY;

	public interface onFoldPointSelctedListener {
		public void onFoldPointSelected(FoldLinePoint point);
	}

	public onFoldPointSelctedListener foldPointSelectedListner;

	public void setOnFoldPointSelectedListener(onFoldPointSelctedListener listener) {
		this.foldPointSelectedListner = listener;
	}

	public FoldLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.foldline);
		foldLineColor = typeArray.getColor(R.styleable.foldline_foldLineColor, color.black);
		verticalAxisColor = typeArray.getColor(R.styleable.foldline_verticalAxisColor, color.black);
		crossAxisColor = typeArray.getColor(R.styleable.foldline_crossAxisColor, color.black);
		axisWidth = typeArray.getDimensionPixelSize(R.styleable.foldline_axisWidth, 10);
		axisScaleWidth = typeArray.getDimensionPixelSize(R.styleable.foldline_axisScaleWidth, 10);
		isFillMode = typeArray.getBoolean(R.styleable.foldline_isFillMode, true);
		isDrawFoldLineCircle = typeArray.getBoolean(R.styleable.foldline_isDrawFoldLineCircle, true);
		foldLineCircleRadius = typeArray.getDimensionPixelSize(R.styleable.foldline_foldLineCircleRadius, 10);
		foldLineCircleColor = typeArray.getColor(R.styleable.foldline_foldLineCircleColor, Color.WHITE);
		crossAxisScaleTextColor = typeArray.getColor(R.styleable.foldline_crossAxisScaleTextColor, Color.BLACK);
		crossAxisScaleTextSize = typeArray.getColor(R.styleable.foldline_crossAxisScaleTextSize, 20);
		foldLineOuterColor = typeArray.getColor(R.styleable.foldline_foldLineOuterColor, Color.WHITE);

		touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		typeArray.recycle();
		initPaint();
	}

	public void initPaint() {
		verticalAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		crossAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		verticalAxisTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		crcossAxisTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		crossAxispScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		foldLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		foldLineOutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		foldLineCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		foldLineCircleInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		foldLinePointTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		verticalAxisPaint.setColor(crossAxisColor);
		verticalAxisPaint.setStyle(Style.STROKE);
		verticalAxisPaint.setStrokeWidth(axisWidth);
		// ���������ʼ��
		crossAxisPaint.setColor(crossAxisColor);
		crossAxisPaint.setStyle(Style.STROKE);
		crossAxisPaint.setStrokeWidth(axisWidth);
		// ����̶Ȳ�����ʼ��
		crossAxispScalePaint.setColor(crossAxisColor);
		crossAxispScalePaint.setStyle(Style.STROKE);
		crossAxispScalePaint.setStrokeWidth(axisWidth);
		// ����̶�����
		crcossAxisTextPaint.setColor(crossAxisScaleTextColor);
		crcossAxisTextPaint.setTextSize(crossAxisScaleTextSize);
		// ����̶�����
		verticalAxisTextPaint.setColor(crossAxisScaleTextColor);
		verticalAxisTextPaint.setTextSize(crossAxisScaleTextSize);
		// ���߳�ʼ��
		foldLinePaint.setColor(foldLineColor);
		foldLinePaint.setStrokeWidth(foldLineWidth);
		foldLinePaint.setStyle(Style.FILL_AND_STROKE);
		// ��������
		foldLineOutPaint.setColor(foldLineOuterColor);
		foldLineOutPaint.setStrokeWidth(foldLineWidth);
		foldLineOutPaint.setStyle(Style.STROKE);

		// ������ԲȦ
		foldLineCirclePaint.setColor(foldLineOuterColor);
		foldLineCirclePaint.setStrokeWidth(foldLineWidth / 2);
		foldLineCirclePaint.setStyle(Style.STROKE);
		foldLineCirclePaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
		// ������ԲȦ
		foldLineCircleInnerPaint.setColor(Color.WHITE);
		foldLineCircleInnerPaint.setStrokeWidth(foldLineWidth / 2);
		foldLineCircleInnerPaint.setStyle(Style.FILL);
		foldLineCircleInnerPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
		// ���ߵ㱻ѡ��ʱ
		foldLinePointTextPaint.setColor(crossAxisScaleTextColor);
		foldLinePointTextPaint.setTextSize(crossAxisScaleTextSize);
	}

	public void initData(List<CrossAxisScale> crossAxisScales, List<VerticalAxisScale> verticalAxisScales,
			List<FoldLinePoint> points) {
		crossAxis = crossAxisScales;
		verticalAxis = verticalAxisScales;
		foldLinePoints = points;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		foldLineDis = getRealWidth() / foldLineCount;
		drawCrossAxis(canvas);
		drawVerticalAxis(canvas);
		drawFoldLine(canvas);
	}

	/**
	 * ���ƺ�������
	 * 
	 * @param canvas
	 */
	private void drawCrossAxis(Canvas canvas) {
		canvas.save();
		Rect rectArea = new Rect(getAxisStartX(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(),
				getMeasuredHeight());
		canvas.clipRect(rectArea);
		Path axisPath = new Path();
		Path axisScalePath = new Path();
		FontMetrics fontMetrics = crcossAxisTextPaint.getFontMetrics();
		int fontHeight = (int) (fontMetrics.descent - fontMetrics.ascent);// ����ĸ߶�
		int startY = getAxisStartY();
		int startX = getAxisStartX();
		// ���ƺ���
		axisPath.setLastPoint(startX, startY);
		axisPath.lineTo(getMeasuredWidth() - getPaddingRight(), startY);

		// ���ƺ���̶�
		if (crossAxis != null) {// ���������Զ���̶������
			for (int i = 0; i < crossAxis.size(); i++) {
				CrossAxisScale axis = crossAxis.get(i);
				int x = startX + (int) ((axis.getPercentPos() * 0.01f * getRealWidth() - axisWidth / 2));
				axisScalePath.moveTo(x, startY);
				axisScalePath.lineTo(x, startY + axisScaleWidth);// ���ƿ̶�
				canvas.drawText(axis.getTitle(), x - crcossAxisTextPaint.measureText(axis.getTitle()) / 2,
						startY + axisScaleWidth + fontHeight, crcossAxisTextPaint);// ���ƿ̶ȵ�λ
			}
		} else {// �������޶�����������ߵ����
			int lastEndX = getCrossStartX() + moveX;// ���ȼ����ƫ�ƣ������еĲ����������Ļʱ��Ҫƫ�Ƶ�������ʼλ��

			for (int i = foldLinePoints.size() - 1; i >= 0; i--) {
				FoldLinePoint foldPonit = foldLinePoints.get(i);
				axisScalePath.moveTo(lastEndX, startY);
				axisScalePath.lineTo(lastEndX, startY + axisScaleWidth);// ���ƿ̶�
				canvas.drawText(foldPonit.getScaleTip(),
						lastEndX - crcossAxisTextPaint.measureText(foldPonit.getScaleTip()) / 2,
						startY + axisScaleWidth + fontHeight, crcossAxisTextPaint);// ���ƿ̶ȵ�λ
				lastEndX -= foldLineDis;
			}
		}

		canvas.drawPath(axisPath, crossAxisPaint);
		canvas.drawPath(axisScalePath, crossAxispScalePaint);
		canvas.restore();
	}

	/**
	 * ��ȡ����ĵ�һ����Ŀ�ʼ����
	 * 
	 * @return
	 */
	private int getCrossStartX() {
		return getMeasuredWidth() - getPaddingRight()
				- Math.max((getRealWidth() - (foldLinePoints.size() * foldLineDis)), 0);
	}
	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int currentTouchX = (int) event.getX();
		int deltaX;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastTouchX = (int) event.getX();
			clickX = currentTouchX;
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			deltaX = currentTouchX - lastTouchX;
			if (Math.abs(deltaX) > 8) {
				moveX += deltaX;
				fillMoveValue();
				clickX = currentTouchX;
				clickY = (int) event.getY();
				postInvalidate();
				lastTouchX = currentTouchX;
			}
			break;
		case MotionEvent.ACTION_UP:
			clickX = currentTouchX;
			clickY = (int) event.getY();
			lastTouchX = 0;
			postInvalidate();
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * ��ȡ������߻���ʱ������������Сֵ����ֹ���껬�����������ᣩ
	 * 
	 * @return
	 */
	private int getLeftMoveMinCrossX() {
		return getAxisStartX() + foldLineDis;
	}

	/**
	 * ��ȡ�����һ���ʱ���ұ���������ֵ����ֹ���껬�����������ᣩ
	 * 
	 * @return
	 */
	private int getRightMoveMaxCrossX() {
		return getFoldLineLength() - getAxisStartX();
	}

	/**
	 * ��move��ֵ������������ֹ���Ȼ���
	 * 
	 * @return
	 */
	private void fillMoveValue() {
		int expectX = getCrossStartX() + moveX;
		if (expectX < getLeftMoveMinCrossX()) {
			moveX = getLeftMoveMinCrossX() - getCrossStartX();
		}

		int max = getRightMoveMaxCrossX();
		if (expectX > getRightMoveMaxCrossX()) {
			moveX = getRightMoveMaxCrossX() - getCrossStartX();
		}
	}

	private int getFoldLineLength() {
		return foldLinePoints.size() * foldLineDis;
	}

	private void drawVerticalAxis(Canvas canvas) {
		Path axisPath = new Path();
		Path axisScalePath = new Path();
		FontMetrics fontMetrics = verticalAxisTextPaint.getFontMetrics();
		int fontHeight = (int) (fontMetrics.descent - fontMetrics.ascent);// ����ĸ߶�
		int startY = getAxisStartY();
		int startX = getAxisStartX();
		// ��������
		axisPath.setLastPoint(startX, startY);
		axisPath.lineTo(startX, getPaddingTop());
		canvas.drawPath(axisPath, verticalAxisPaint);

		for (int i = 0; i < verticalAxis.size(); i++) {
			VerticalAxisScale axis = verticalAxis.get(i);
			int y = startY - (int) ((axis.getPercentPos() * 0.01f * getRealHeight() - axisWidth / 2));
			axisScalePath.moveTo(startX, y);
			axisScalePath.lineTo(startX - axisScaleWidth, y);
			canvas.drawText(axis.getTitle(), startX - crcossAxisTextPaint.measureText(axis.getTitle()), y + fontHeight,
					crcossAxisTextPaint);// ���ƿ̶ȵ�λ
		}

		canvas.drawPath(axisScalePath, crossAxispScalePaint);

	}

	private void drawFoldLine(Canvas canvas) {
		canvas.save();
		Rect areaFold = new Rect(getAxisStartX(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(),getAxisStartY());// ����ͼ�Ļ��Ʒ�Χ
		canvas.clipRect(areaFold);
		Path foldPath = new Path();
		Path foldeLinePath = new Path();
		Path circlePath = new Path();
		Path circleSelectPath=new Path();
		FoldLinePoint slectedPoint = null;
		int lastEndX = getCrossStartX() + moveX;// ���ȼ����ƫ�ƣ������еĲ����������Ļʱ��Ҫƫ�Ƶ�������ʼλ��

		int fisrtPointX = lastEndX;// ����ߵ�X����
		int firstPointY = 0;// ����ߵ�Y����
		for (int i = foldLinePoints.size() - 1; i >= 0; i--) {
			FoldLinePoint foldPonit = foldLinePoints.get(i);
			int y = (int) (foldPonit.getPercentY() * 0.01f * getRealHeight());
			if (i == foldLinePoints.size() - 1) {
				firstPointY = y;
				foldPath.moveTo(fisrtPointX, y);
				foldeLinePath.moveTo(fisrtPointX, y);
			}

			foldPath.lineTo(lastEndX, y);
			foldeLinePath.lineTo(lastEndX, y);
			Rect rect = new Rect(lastEndX - foldLineDis / 2, getPaddingTop(), lastEndX + foldLineDis / 2,
					getMeasuredHeight());
			if (rect.contains(clickX, clickY)) {
				slectedPoint=foldPonit;
				FontMetrics fontMetricx=foldLinePointTextPaint.getFontMetrics();
				int textWidth=(int) foldLinePointTextPaint.measureText(slectedPoint.getTip());
				circleSelectPath.addRect(lastEndX-textWidth/2, y, lastEndX+textWidth/2, y+fontMetricx.descent-fontMetricx.ascent, Direction.CW);
				circlePath.addCircle(lastEndX, y, 30, Direction.CCW);// ��ȡԲ���·��
				if (foldPointSelectedListner != null) {
					foldPointSelectedListner.onFoldPointSelected(foldPonit);// ���ߵ㱻ѡ��
				}
			} else {
				circlePath.addCircle(lastEndX, y, 10, Direction.CCW);// ��ȡԲ���·��
			}
			// ���ƿ̶ȵ�λ
			lastEndX -= foldLineDis;
		}

		foldPath.lineTo(lastEndX + foldLineDis, getAxisStartY() - axisWidth * 2);
		foldPath.lineTo(fisrtPointX, getAxisStartY() - axisWidth);
		foldPath.lineTo(fisrtPointX, firstPointY);

		canvas.drawPath(foldPath, foldLinePaint);
		canvas.drawPath(foldeLinePath, foldLineOutPaint);
		canvas.drawPath(circlePath, foldLineCirclePaint);
		canvas.drawPath(circlePath, foldLineCircleInnerPaint);
		if(slectedPoint!=null){
			canvas.drawTextOnPath(slectedPoint.getTip(), circleSelectPath, 0, foldLinePointTextPaint.descent(), foldLinePointTextPaint);
		}
		canvas.restore();
	}

	private void drawFoldPointText(Canvas canvas,FoldLinePoint foldPoint) {
		canvas.drawText(foldPoint.getTip(), getMeasuredWidth()-getPaddingRight()-100, getPaddingTop()+200, foldLinePointTextPaint);
	}

	/**
	 * �����ʼX����
	 * 
	 * @return
	 */
	private int getAxisStartX() {
		FontMetrics fontMetris = verticalAxisTextPaint.getFontMetrics();
		int fontWidth = (int) verticalAxisTextPaint.measureText(verticalAxis.get(verticalAxis.size() - 1).getTitle());
		return getPaddingLeft() + fontWidth;
	}

	/**
	 * �����ʼY����
	 * 
	 * @return
	 */
	private int getAxisStartY() {
		FontMetrics fontMetrics = crcossAxisTextPaint.getFontMetrics();
		int fontHeight = (int) (fontMetrics.descent - fontMetrics.ascent);// ����ĸ߶�
		return getMeasuredHeight() - getPaddingBottom() - axisWidth / 2 - axisScaleWidth - fontHeight;
	}

	/**
	 * ������������Ŀ��
	 * 
	 * @return
	 */
	private int getRealWidth() {
		return getMeasuredWidth() - getAxisStartX() - getPaddingRight();
	}

	/**
	 * ������������ĸ߶�
	 * 
	 * @return
	 */
	private int getRealHeight() {
		return getAxisStartY() - getPaddingRight();
	}
}
