package su.whs.widgets;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewR extends TextView {

	  private static final String FONT_ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
	  private static final String FONT_ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";
	  private static final String FONT_ROBOTO_ITALIC = "fonts/Roboto-Italic.ttf";
	  private static final String FONT_ROBOTO_BOLD_ITALIC = "fonts/Roboto-BoldItalic.ttf";

	  private static final Map<Integer, Typeface> typefaceMap = new HashMap<Integer, Typeface>();

	  public TextViewR(Context context) {
	    super(context);
	  }

	  public TextViewR(Context context, AttributeSet attrs) {
	    super(context, attrs);
	  }

	  public TextViewR(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	  }

	  @Override
	  public void setTypeface(Typeface tf) {
	    super.setTypeface(styleToTypefaceMap(Typeface.NORMAL));
	  }

	  @Override
	  public void setTypeface(Typeface tf, int style) {
		if (isInEditMode()) {
			// ssuper.setTypeface(tf,style);
			return;
		}
	    Typeface result = styleToTypefaceMap(style);
	    super.setTypeface(result);
	  }

	  private Typeface styleToTypefaceMap(int style) {	  
			  
	    Typeface result = typefaceMap.get(style);
	    if (result == null) {
	      result = Typeface.createFromAsset(
	          getContext().getAssets(),
	          typefaceMap(style));
	      typefaceMap.put(style, result);
	    }
	    return result;
	  }

	  protected String typefaceMap(int style) {
	    switch (style) {
	      case Typeface.BOLD:
	        return FONT_ROBOTO_BOLD;
	      case Typeface.BOLD_ITALIC:
	        return FONT_ROBOTO_BOLD_ITALIC;
	      case Typeface.ITALIC:
	        return FONT_ROBOTO_ITALIC;
	      case Typeface.NORMAL:
	      default:
	        return FONT_ROBOTO_REGULAR;
	    }
	  }

	}

/*
public class TextViewR extends TextView {
	private String mName = "Roboto-Thin";
	public TextViewR(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);		
		init(context,attrs);
	}

	public TextViewR(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	public TextViewR(Context context) {
		super(context);
		init(context,null);
	}

	private void init(Context context, AttributeSet attrs) {
		if (!isInEditMode()) {
			if (attrs!=null) {
				TypedArray a = context.obtainStyledAttributes(R.styleable.TextViewR);
				String name = a.getString(R.styleable.TextViewR_fontname);
				if (name!=null && !"".equals(name)) mName = name;
				a.recycle();
			}
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+mName+".ttf");
			setTypeface(tf);
		} 
	}
	
} */
