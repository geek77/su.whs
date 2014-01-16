package su.whs.widgets;

import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonR extends Button {
	private static final String FONT_ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
	private static final String FONT_ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";
	private static final String FONT_ROBOTO_ITALIC = "fonts/Roboto-Italic.ttf";
	private static final String FONT_ROBOTO_BOLD_ITALIC = "fonts/Roboto-BoldItalic.ttf";

	private static final Map<Integer, Typeface> typefaceMap = new HashMap<Integer, Typeface>();	
	
	public ButtonR(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);		
	}

	public ButtonR(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ButtonR(Context context) {
		super(context);
	}

	@Override
	  public void setTypeface(Typeface tf) {
	    super.setTypeface(styleToTypefaceMap(Typeface.NORMAL));
	  }

	  @Override
	  public void setTypeface(Typeface tf, int style) {
		  if (isInEditMode()) {
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
