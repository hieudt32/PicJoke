package funia.maker.pip.picjoke.scopic.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import funia.maker.pip.picjoke.scopic.other.Defines;


public class TextViewRegular extends android.support.v7.widget.AppCompatTextView {
    public TextViewRegular(Context context) {
        super(context);
        init(context);
    }

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setTypeface(Typeface.createFromAsset(context.getAssets(), Defines.FONT_GUIDE));
    }
}
