package su.whs.widgets;

import su.whs.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;

import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
/**
 * 
 * @author igor n. boulliev <igor@syncpad.ru>
 *
 */

public class RoundedImageView extends ImageView {
	private Paint mBackground;
	private Paint mBorder;
	private Paint mCropper;
	private int mStrokeColor = Color.WHITE;
	private int mStrokeWidth = 2;
	
	/**
	 * default constructor
	 * @param context 
	 * @param attrs
	 */
	
	
	
	public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init(context,attrs,defStyle);
	}
	
	public RoundedImageView(Context context, AttributeSet attrs) {
		super(context,attrs);
		init(context,attrs,0);
	}
	
	private void init(Context context, AttributeSet attrs, int defStyle) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView);
			// fill mStrokeColor
		mStrokeColor = a.getColor(R.styleable.RoundedImageView_circleStrokeColor, Color.WHITE);
		mStrokeWidth = a.getInt(R.styleable.RoundedImageView_circleStrokeWidth,2);
		a.recycle();
		
		turnSoftwareMode();
		mBackground = new Paint();
		mBackground.setColor(Color.RED);
		mBackground.setAntiAlias(true);
		mBorder = new Paint();
		mBorder.setStyle(Paint.Style.STROKE);
		
		mBorder.setColor(mStrokeColor);
		mBorder.setStrokeWidth(mStrokeWidth);
		mBorder.setAntiAlias(true);
		
		mCropper = new Paint();
		mCropper.setColor(Color.RED);
		mCropper.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		mCropper.setAntiAlias(true);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void turnSoftwareMode() {
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}
	
	private Path clipPath;
	private RectF rect;
	private Rect bmr;
	private Bitmap bm;
	private Canvas a;
	
	private void prepare() {
		clipPath = new Path();
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        bmr = new Rect(0,0,getWidth(),getHeight());
        bm = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        a = new Canvas(bm);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	public void onMeasure(int measureWidthSpec, int measureHeightSpec) {
		super.onMeasure(measureWidthSpec, measureHeightSpec);
		getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				prepare();
				getViewTreeObserver().removeOnPreDrawListener(this);
				return true;
			}
		});
	}
	
	
	@Override
	public void onDraw(Canvas canvas) {
        float cX = rect.width()/2;
        float cY = rect.height()/2;
        
        float r = cX - 8;
        
        clipPath.addCircle(cX, cY, r+1, Path.Direction.CW);
        canvas.clipPath(clipPath);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawColor(0x000000);
        
        canvas.drawCircle(cX, cY, r, mBackground);
        super.onDraw(a);
        canvas.drawBitmap(bm, bmr, bmr, mCropper);
        mBorder.setColor(mStrokeColor);
        canvas.drawCircle(cX, cY, r, mBorder);
	}

}
