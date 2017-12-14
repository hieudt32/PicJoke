package funia.maker.pip.picjoke.scopic.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class GardeTextView extends AppCompatTextView {

    public GardeTextView(Context context) {
        super(context);
        init(context);
    }

    public GardeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GardeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface mTypeface = Typeface.createFromAsset(context.getAssets(), "font/Garde.ttf");
        setTypeface(mTypeface);
    }
}
