package funia.maker.pip.picjoke.scopic.view;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import funia.maker.pip.picjoke.scopic.R;


public class AlertDialogDefault extends BaseAlertDialog {
    private TextView mMessageView;
    private Button mNegativeButton;
    private Button mPositiveButton;
    private TextView mTitleView;

    public AlertDialogDefault(Context context) {
        super(context);
        init();
    }

    public AlertDialogDefault(Context context, int theme) {
        super(context, theme);
        init();
    }

    public AlertDialogDefault(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView((int) R.layout.dialog_alert_default);
        this.mTitleView = (TextView) findViewById(R.id.tv_title);
        this.mMessageView = (TextView) findViewById(R.id.tv_message);
        this.mNegativeButton = (Button) findViewById(R.id.button_negative);
        this.mPositiveButton = (Button) findViewById(R.id.button_positive);
    }

    public void noTitle() {
        this.mTitleView.setVisibility(View.GONE);
        findViewById(R.id.stroke_title_bottom).setVisibility(View.GONE);
    }

    public void setTitle(int resId) {
        setTitle(getContext().getResources().getString(resId));
    }

    public void setTitle(CharSequence title) {
        this.mTitleView.setText(title);
    }

    public void setMessage(String message) {
        this.mMessageView.setText(message);
    }

    public void setPositiveButton(String text, OnClickListener listener) {
        this.mPositiveButton.setText(text);
        setPositiveButton(this.mPositiveButton, listener);
    }

    public void setNegativeButton(String text, OnClickListener listener) {
        this.mNegativeButton.setText(text);
        setNegativeButton(this.mNegativeButton, listener);
    }
}
