package su.whs.widgets;


import su.whs.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

public class HelpOverlayScreen extends View {
	private static final String TAG = "HelpOverlay";
	private String mText;
	private String mDisclaimer;
	private Rect mRect = new Rect();
	private Paint pHole;
	
	public enum TextPlacement {
		LEFT_BOTTOM,
		RIGHT_BOTTOM,
		LEFT_TOP,
		RIGHT_TOP,
		CENTER_BOTTOM,
		CENTER_TOP, LEFT_BOTTOM_BELOW
	}
	
	private TextPlacement mPlacement;
	private boolean mSquare = true;
	private Bitmap mOverlay;
	private Rect pRect = new Rect();
	private Bitmap mArrow;
	private Typeface mFont;
	private Align mAlign;
	private int mBottomBound = 0;
		
	public HelpOverlayScreen(Context context, int topOffset, View baseView, int aroundViewId, String text, TextPlacement placement, Align align, boolean isSquare, String disclaimer) {
		super(context);
		mAlign = align;
		mText = text;
		View base = baseView.findViewById(aroundViewId);
		int[] location = new int[2];
		base.getLocationInWindow(location);
		base.getLocalVisibleRect(mRect);
		baseView.getLocalVisibleRect(pRect);
		mBottomBound = baseView.findViewById(R.id.helpOverlayBtn).getTop();
		mRect.left += location[0];
		mRect.right += location[0];
		mRect.top += location[1] - topOffset;
		mRect.bottom += location[1] - topOffset;
		mPlacement = placement;
		pRect.top -= topOffset;
		pRect.bottom -= topOffset;
		mSquare = isSquare;
		pHole = new Paint(Paint.ANTI_ALIAS_FLAG);         
        pHole.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT)); 
        pHole.setColor(Color.TRANSPARENT);
        // pHole.setAlpha(128);
        mFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Hint.ttf");
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        int w = pRect.right;
        int h = pRect.bottom;
        mOverlay = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
        pHole.setMaskFilter(new BlurMaskFilter(5, Blur.NORMAL));
        // pTransparent = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDisclaimer = disclaimer;
        switch(mPlacement) {
        case LEFT_TOP:
        	mArrow = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_arrow_4left_top);
        	break;
        case LEFT_BOTTOM_BELOW:
        case LEFT_BOTTOM:
        	mArrow = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_arrow_4left_bottom);
        	break;
        case RIGHT_TOP:
        	mArrow = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_arrow_4right_top);
        	break;
        case RIGHT_BOTTOM:
        	mArrow = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_arrow_4right_bottom);
        	break;
		default:
			break;
        }
	}
	
	private Canvas mCanvas = null;
	private RectF mRectF = null;
	private Paint textPaint = new Paint();
	private Rect bounds = new Rect();
	
	private void initializePaint() {
		mCanvas = new Canvas(mOverlay);
		mRectF = new RectF(mRect);
		textPaint.setColor(Color.WHITE);
		 // set typeface
		textPaint.setTypeface(mFont);
		textPaint.setTextSize(mCanvas.getWidth()*0.06f);
		
	}
	
	private int aX = 0;
	private int aY = 0;
	private int oY = 0;
	private int oX = 0;
	private int dX = 0;
	private int dY = 0;
	private int tX = 0;
	private int tY = 0;
	
	private void calculatePlacement() {

	    /*
	     * 
	     * _____________
	     * |             
	     * | LT      RT
	     * | ATL    ATR
	     * |  C      C
	     * | 
	     * l
	     * |
	     * |
	     */
		
	    switch (mPlacement) {
	    case LEFT_TOP:
	    		// arrow at right, points from left top to right bottom
	    		
	    		aY = mRect.bottom;
	    		aX -= mArrow.getWidth();
	    		break;
	    case LEFT_BOTTOM_BELOW:
	    		// TODO: 
	    		// FIXME:
	    		aY = mRect.bottom + oY;
	    		aX -= mArrow.getWidth();
	    		break;
	    case LEFT_BOTTOM:
	    		// arrow at right, points from left bottom to right top
	    		aY = mRect.bottom;
	    		aX -= mArrow.getWidth();
	    		break;
	    case RIGHT_TOP:
	    		// arrow at left, points from right top to left bottom
	    		aY = mRect.top - mArrow.getHeight();
	    		
	    		break;
	    case RIGHT_BOTTOM:
	    		// arrow at right, points from right bottom to left top
	    		aY = mRect.bottom; 
	    		
	    		break;
		default:
			break;
	    }


    	switch(mPlacement) {
    	case LEFT_BOTTOM_BELOW:
    	case LEFT_BOTTOM:
    	case CENTER_BOTTOM:
    	case RIGHT_TOP:
    	case RIGHT_BOTTOM:
    		dY = bounds.bottom;
    		break;
		default:
			break;
    	}

	    getMultilineBounds(mText,textPaint,aX,aY,bounds);
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		calculatePlacement();
	}
	
	@Override 
	public void onDraw(Canvas canvas) {
		if (mCanvas==null)
			initializePaint();
		//pTransparent.setARGB(128, 0,0,0);
	
		mCanvas.drawColor(0xbb0a0a0a);
		
		if (mSquare) {
			mCanvas.drawRoundRect(mRectF, 5, 5,  pHole);
			aX = mRect.left + ( mRect.right-mRect.left ) / 2;
		    aY = mRect.top;
		} else {
			float w = (mRectF.right-mRectF.left);
			float h = (mRectF.bottom-mRectF.top);
			float radius = (float) (java.lang.Math.sqrt(w*w+h*h) / 2);
			mCanvas.drawCircle(mRectF.left+w/2, mRectF.top+h/2, radius, pHole);
			aX = mRect.left + mRect.width()/2;
			Double d = (double) radius;
			aY = mRect.top + mRect.height()/2 + d.intValue();
			oY = aY - (mRect.top + mRect.height());
		}
		// draw text and arrow on c
		
	    
		// move to precal
	    // first, calculate arrow placement
	    
	    
	    mCanvas.drawBitmap(mArrow,aX,aY,null);
	    
	    
	    
	    
	    // set font size
	    
	    // paint text
	    
	    // c.drawText(mText,tX,tY,textPaint);
	    
	    drawMultilineText(mCanvas, textPaint, mText, bounds, mPlacement, mAlign);
	    // paint arrow
	    if (mDisclaimer!=null) {
	    	getMultilineBounds(mDisclaimer, textPaint, dX, dY, bounds);
	    	int left = mBottomBound - dY;
	    	// center vertical on left space
	    	int offsetY = (left - bounds.height()) / 2;
	    	bounds.top += offsetY;
	    	bounds.bottom += offsetY;
	    	bounds.left = 0;
	    	bounds.right = mCanvas.getWidth();
	    	drawMultilineText(mCanvas, textPaint, mDisclaimer, bounds, TextPlacement.CENTER_BOTTOM, Align.CENTER);
	    }
		canvas.drawBitmap(mOverlay, 0, 0, null);
		setDrawingCacheEnabled(true);
	}
	
	private static int LINESPACE = 20;
	
	private void getMultilineBounds(String text, Paint p,int x, int y,Rect bounds) {
		String[] lines = text.split("\n");
		int totalHeight = 0;
		int maxWidth = 0;
		Rect r = new Rect();
		for(String line: lines ) {
			if (line.length()<1) continue;
			p.getTextBounds(line, 0, line.length(), r);
			if (r.right>maxWidth) 
				maxWidth = r.right;
			totalHeight += r.bottom + LINESPACE;
		}
		bounds.left = x;
		bounds.top = y;
		bounds.right = x + maxWidth;
		if (lines.length>1) totalHeight -= LINESPACE;
		bounds.bottom = y + totalHeight;
	}
	
	private void drawMultilineText(Canvas c, Paint p, String text, Rect bounds, TextPlacement placement, Align a) {
		String[] lines = text.split("\n");
		/*
		int totalHeight = 0;
		int maxWidth = 0;
		Rect r = new Rect();
		for(String line: lines ) {
			if (line.length()<1) continue;
			p.getTextBounds(line, 0, line.length(), r);
			if (r.right>maxWidth) 
				maxWidth = r.right;
			totalHeight += r.bottom + LINESPACE;
		} 
		if (lines.length>1) totalHeight -= LINESPACE; */
		Log.v(TAG,"text height: "+String.valueOf(bounds.height()));
		Log.v(TAG,"text width: " + String.valueOf(bounds.width()));
		// int x = bounds.left;
		// int y = bounds.top;
		int maxWidth = bounds.width();
		bounds.height();
		
		switch(placement) {
		case CENTER_BOTTOM:
			tY = bounds.top + mArrow.getHeight() + LINESPACE;
			tX = bounds.left;
			break;
		case LEFT_TOP:
			tX = bounds.left - maxWidth;
			tY = bounds.top;
			break;
		case LEFT_BOTTOM:
			tX = bounds.left - maxWidth;
			tY = bounds.top + mArrow.getHeight();
			break;
		case LEFT_BOTTOM_BELOW:
			tY = bounds.top + mArrow.getHeight() + LINESPACE;
			tX = bounds.left + mArrow.getWidth() - maxWidth;
			break;
		case RIGHT_TOP:
			tX = bounds.left + mArrow.getWidth(); 
			tY = bounds.top;
			break;
		case RIGHT_BOTTOM:
			tX = bounds.left + mArrow.getWidth();
			tY = bounds.top + mArrow.getHeight();
		default:
			break;
		}
		
		for (String line: lines) {
			Rect subbounds = new Rect();
			p.getTextBounds(line, 0, line.length(), subbounds);
			int oX = 0, oY = 0;
			switch(a) {
			case LEFT:
				break;
			case RIGHT:
				oX = maxWidth - subbounds.right;
				break;
			case CENTER:
				oX = maxWidth / 2 - subbounds.right / 2;
				break;
			}
			c.drawText(line, tX+oX, tY+oY, p);
			tY += subbounds.bottom + LINESPACE;
		}
	}
	
	
}
