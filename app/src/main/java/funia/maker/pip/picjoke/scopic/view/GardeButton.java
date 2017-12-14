package funia.maker.pip.picjoke.scopic.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


public class GardeButton extends AppCompatButton {
    public GardeButton(Context context) {
        super(context);
        init(context);
    }

    public GardeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GardeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface mTypeface = Typeface.createFromAsset(context.getAssets(), "font/Garde.ttf");
        setAllCaps(false);
        setTypeface(mTypeface);
    }
}
