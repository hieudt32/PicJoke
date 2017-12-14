package funia.maker.pip.picjoke.scopic.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.activity.MainActivity;
import funia.maker.pip.picjoke.scopic.other.Defines;


public class RatingDialog {
    public static final String DISABLE = "disable";
    public static final String COUNT = "count";
    private Context activity;
    private Dialog dialog;
    private Handler mHandler;
    public RatingDialog(Context activity) {
        this.activity = activity;
        init();
    }
    private void init() {
        View contentView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.dialog_rating, null);
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mHandler = new Handler();

        TextView tvOk = (TextView) contentView.findViewById(R.id.tvOK);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeWindow();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).edit();
                editor.putInt(Defines.RATE_PREF, 0);
                editor.commit();
            }
        });
        TextView tvDont = (TextView) contentView.findViewById(R.id.tvDontShow);
        tvDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disable();
                closeWindow();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).edit();
                editor.putInt(Defines.RATE_PREF, 1);
                editor.commit();
            }
        });
        RatingBar ratingBar = (RatingBar) contentView.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v >= 4) {
                    openMarket();
                } else {
                    Toast.makeText(activity, "Thank you for your rating!", Toast.LENGTH_SHORT)
                            .show();
                }
                disable();
                closeWindow();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).edit();
                editor.putInt(Defines.RATE_PREF, 1);
                editor.commit();
            }
        });
    }
    private void disable() {
        SharedPreferences shared = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(DISABLE, true);
        editor.apply();
    }

    private void openMarket() {
        final String appPackageName = activity.getPackageName();
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void showAfter(int time) {
        SharedPreferences mPrefs = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        int countTime = mPrefs.getInt(COUNT, 1);
        editor.putInt(COUNT, countTime + 1);
        editor.apply();
        if (countTime >= time) {
            showWindow();
        }
    }
    public void showWindow() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 400);
    }
    public void closeWindow() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
