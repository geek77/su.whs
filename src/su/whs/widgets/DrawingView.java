package su.whs.widgets;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
	public class FloatPoint {
		private float mX;
		private float mY;
		public FloatPoint(float x, float y) {
			mX = x;
			mY = y;
		}
		
		public float getX() { return mX; }
		public float getY() { return mY; }
	}
	public class DrawingPath {
		private Path mPath = null;
		private List<FloatPoint> mPoints = new ArrayList<FloatPoint>();
		private int mColor = Color.BLACK;
		private float mX;
		private float mY;
		
		public DrawingPath() {
			
		}
		
		public void add(float x, float y) {
			mX = x;
			mY = y;
			mPoints.add(new FloatPoint(x,y));
			if (mPath==null) {
				mPath = new Path();
				mPath.moveTo(x, y);
			} else {
				mPath.lineTo(x, y);
			}
		}
		
		public int getColor() { return mColor; }
		
		public void paintTo(Canvas c) {
			Paint p = new Paint();
			p.setColor(getColor());
			c.drawPath(mPath, p);
		}
		
		public boolean isShortMove(float x, float y) {
			
			if (x > mX - getClickRadius() && x < mX + getClickRadius() ) {
				if (y > mY - getClickRadius() && y < mY + getClickRadius() ) {
					return true;
				}
			}
			return false;
		}
		
		public float getClickRadius() {
			return (float)10.0;
		}
		
		public boolean isBelongs(float x, float y) {
			
			return false;
		}
		
	}

	private List<DrawingPath> mPaths = new ArrayList<DrawingPath>();
	private DrawingPath mCurrent;
	
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas c) {
		for (DrawingPath dp :mPaths) {
			dp.paintTo(c);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		float x = event.getX();
		float y = event.getY();
		
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mCurrent = new DrawingPath();
			break;
		case MotionEvent.ACTION_UP:
			if (mCurrent!=null) {
				// check if move action was called
				if (mCurrent.isShortMove(x, y)) {
					handleClickAt(x,y);
				} else {
					mCurrent.add(x, y);
					mPaths.add(mCurrent);
					mCurrent = null;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mCurrent!=null) {
				mCurrent.add(x, y);
			}
			break;
		}
		invalidate();
		return true;
	}
	
	public void handleClickAt(float x, float y) {
		
	}

}
