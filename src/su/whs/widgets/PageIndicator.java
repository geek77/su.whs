package su.whs.widgets;
import su.whs.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PageIndicator extends View {
	int totalNoOfDots;
	int activeDot;
	int dotSpacing;
	int horizontalSpace = 0;
	Bitmap activeDotBitmap;
	Bitmap normalDotBitmap;

	private Paint paint;
	
	public PageIndicator(Context context) {
		super(context);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		activeDotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dot_active);
		normalDotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dot_inactive);
	}
 
	public PageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		activeDotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dot_active);
		normalDotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dot_inactive);
 
	}
 
	public PageIndicator(Context context, AttributeSet attrs , int defStyle) {
		super(context,attrs,defStyle);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		activeDotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dot_active);
		normalDotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dot_inactive);
 
	}
 
	@Override
	protected void onDraw(Canvas canvas) {
		if (isInEditMode())
			dotSpacing = 0;
		// super.onDraw(canvas);
		drawDot(canvas); 
		setDrawingCacheEnabled(true);
	}
 
 
	private void drawDot(Canvas canvas){
		int total = totalNoOfDots;
		int x = 0;
		if (isInEditMode())
			total = 7;
		for(int i=0;i<total;i++){
			if(i==activeDot){
				canvas.drawBitmap(activeDotBitmap, x, 0, paint);
			}else{
				canvas.drawBitmap(normalDotBitmap, x, 0, paint);
			}
			x=x+activeDotBitmap.getWidth()+horizontalSpace+dotSpacing;
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (isInEditMode())
			totalNoOfDots = 7;
		int width = totalNoOfDots*(activeDotBitmap.getWidth()+horizontalSpace+ getDotSpacing());
		width = resolveSize(width, widthMeasureSpec);
		int height = activeDotBitmap.getHeight();
		height = resolveSize(height, heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	public void refresh(){
		invalidate();
	}
 
 
	public int getTotalNoOfDots() {		
		return totalNoOfDots;
	}

	public void setTotalNoOfDots(int totalNoOfDots) {
		this.totalNoOfDots = totalNoOfDots;
		setDrawingCacheEnabled(false);
		requestLayout();
		refresh();
	}

	public int getActiveDot() {
		return activeDot;
	}

	public void setActiveDot(int activeDot) {
		this.activeDot = activeDot;
		setDrawingCacheEnabled(false);
		refresh();
	}

	public int getDotSpacing() {
		return dotSpacing;
	}

	public void setDotSpacing(int dotSpacing) {
		this.dotSpacing = dotSpacing;
		invalidate();
	}
  
}