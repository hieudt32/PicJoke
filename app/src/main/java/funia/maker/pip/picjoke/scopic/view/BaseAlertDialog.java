package funia.maker.pip.picjoke.scopic.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class BaseAlertDialog extends BaseDialog implements View.OnClickListener {
    public static final int BUTTON_NEGATIVE = -1;
    public static final int BUTTON_NETURAL = 0;
    public static final int BUTTON_POSITIVE = 1;
    private View mNegativeButton;
    private OnClickListener mNegativeListenr;
    private View mNeturalButton;
    private OnClickListener mNeturalListenr;
    private View mPositiveBtn;
    private OnClickListener mPositiveListenr;

    public interface OnClickListener {
        void onClick(Dialog dialog, int i, View view);
    }

    public BaseAlertDialog(Context context) {
        super(context);
    }

    public BaseAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    public BaseAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setPositiveButton(View v, OnClickListener listener) {
        this.mPositiveBtn = v;
        this.mPositiveListenr = listener;
        this.mPositiveBtn.setOnClickListener(this);
    }

    protected void setNegativeButton(View v, OnClickListener listener) {
        this.mNegativeButton = v;
        this.mNegativeListenr = listener;
        this.mNegativeButton.setOnClickListener(this);
    }

    protected void setNeutralButton(View v, OnClickListener listener) {
        this.mNeturalButton = v;
        this.mNeturalListenr = listener;
        this.mNeturalButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == this.mPositiveBtn) {
            if (this.mPositiveListenr != null) {
                this.mPositiveListenr.onClick(this, BUTTON_POSITIVE, this.mPositiveBtn);
            }
        } else if (v == this.mNeturalButton) {
            if (this.mNeturalListenr != null) {
                this.mNeturalListenr.onClick(this, BUTTON_NETURAL, this.mNeturalButton);
            }
        } else if (this.mNegativeListenr != null) {
            this.mNegativeListenr.onClick(this, BUTTON_NEGATIVE, this.mNegativeButton);
        }
    }
}
