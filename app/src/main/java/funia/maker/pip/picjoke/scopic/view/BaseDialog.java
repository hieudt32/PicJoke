package funia.maker.pip.picjoke.scopic.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import funia.maker.pip.picjoke.scopic.R;


public class BaseDialog extends Dialog {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterContentView();
    }

    public void setContentView(View view) {
        super.setContentView(view);
        afterContentView();
    }

    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterContentView();
    }

    private void afterContentView() {
        LayoutParams lp = getWindow().getAttributes();
        lp.width = -1;
        getWindow().setAttributes((WindowManager.LayoutParams) lp);
    }

    public BaseDialog(Context context) {
        super(context, R.style.BaseDialog);
    }

    public BaseDialog(Context context, int theme) {
        super(context, R.style.BaseDialog);
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
