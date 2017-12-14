package funia.maker.pip.picjoke.scopic.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.common.Constants;
import funia.maker.pip.picjoke.scopic.other.Defines;
import funia.maker.pip.picjoke.scopic.view.RatingDialog;

public class ShareActivity extends BaseActivity implements View.OnClickListener {
    public static final String PARENT_ACT = "parent_activity";
    private ImageView imgPreview;
    private ImageButton imb_Back, imb_ShowShare;
    private RelativeLayout rl_ListShare, rl_Share;
    private Button btn_Facebook, btn_Twitter, btn_LinkedIn, btn_Instagram, btn_Pinterest;
    private Uri uriImg;
    private AlertDialog mDialog;

    private int intCountStart;
    private int intFlagParentActivity;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_share;
    }

    @Override
    protected void initVariables() {
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        imb_Back = (ImageButton) findViewById(R.id.imb_back_share);
        imb_ShowShare = (ImageButton) findViewById(R.id.imb_show_share);
        rl_ListShare = (RelativeLayout) findViewById(R.id.rl_list_share);
        rl_Share = (RelativeLayout) findViewById(R.id.rl_share);
        btn_Facebook = (Button) findViewById(R.id.btn_share_facebook);
        btn_Instagram = (Button) findViewById(R.id.btn_share_instagram);
        btn_LinkedIn = (Button) findViewById(R.id.btn_share_linkedin);
        btn_Pinterest = (Button) findViewById(R.id.btn_share_pinterest);
        btn_Twitter = (Button) findViewById(R.id.btn_share_twitter);
        rl_ListShare.setVisibility(View.GONE);

    }

    @Override
    protected void initData() {

        String uriImage = getIntent().getExtras().getString("URI_IMAGE");
        uriImg = Uri.parse(uriImage);
        Glide.with(this).load(uriImage).into(imgPreview);
        imb_Back.setOnClickListener(this);
        imb_ShowShare.setOnClickListener(this);
        btn_Twitter.setOnClickListener(this);
        btn_Facebook.setOnClickListener(this);
        btn_LinkedIn.setOnClickListener(this);
        btn_Instagram.setOnClickListener(this);
        btn_Pinterest.setOnClickListener(this);
        rl_Share.setOnClickListener(this);

        intFlagParentActivity = Integer.parseInt(getIntent().getExtras().getString(PARENT_ACT));
        intCountStart = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).getInt(Defines.COUNT_START_PREF, 0);
        //showRateDialog();

    }

    private void showHideListShare(boolean flag) {
        if (rl_ListShare.isShown()) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_hide_template);
            rl_ListShare.startAnimation(animation);
            rl_ListShare.setVisibility(View.GONE);
            btn_Twitter.setEnabled(false);
            btn_Facebook.setEnabled(false);
            btn_LinkedIn.setEnabled(false);
            btn_Instagram.setEnabled(false);
            btn_Pinterest.setEnabled(false);
            imb_ShowShare.setImageResource(R.drawable.ic_share);
        } else {
            if (flag) {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_show_template);
                rl_ListShare.setVisibility(View.VISIBLE);
                rl_ListShare.startAnimation(animation);
                btn_Twitter.setEnabled(true);
                btn_Facebook.setEnabled(true);
                btn_LinkedIn.setEnabled(true);
                btn_Instagram.setEnabled(true);
                btn_Pinterest.setEnabled(true);
                imb_ShowShare.setImageResource(R.drawable.ic_hide_template);
            }
        }
    }

    public void showRateDialog()
    {
        int ratePref = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).getInt(Defines.RATE_PREF, 0);
        android.content.SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).edit();

        if (ratePref == 0 && (intCountStart == 0 || intCountStart > 3))
        {
            RatingDialog ratingDialog = new RatingDialog(this);
            ratingDialog.showAfter(1);
            intCountStart = 1;
        }

        intCountStart++;

        editor.putInt(Defines.COUNT_START_PREF, intCountStart);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        onBtnBack();
        return super.onKeyDown(keyCode, event);
    }

    public void onBtnBack()
    {
        if (intFlagParentActivity == 0) {
            doHomeClicked();
        } else {
            doBackClicked();
        }
    }

    private void doBackClicked() {
        finish();
    }

    private void doHomeClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_back_share:
                if (rl_ListShare.isShown()) {
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_hide_template);
                    rl_ListShare.startAnimation(animation);
                    rl_ListShare.setVisibility(View.GONE);
                    imb_ShowShare.setImageResource(R.drawable.ic_share);
                }
//                if (getStatusRate("Rate", "rated")) {
//                    onBackPressed();
//                } else {
//                    mDialog = createDialog();
//                    mDialog.show();
//                }
                int ratePref = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).getInt(Defines.RATE_PREF, 0);
                android.content.SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).edit();
                if (ratePref == 0 && (intCountStart == 0 || intCountStart > 3)) {
                    RatingDialog ratingDialog = new RatingDialog(this);
                    ratingDialog.showAfter(1);
                    intCountStart = 1;
                } else {
                    onBtnBack();
                }
                intCountStart++;
                editor.putInt(Defines.COUNT_START_PREF, intCountStart);
                editor.commit();

                break;
            case R.id.imb_show_share:
                showHideListShare(true);
                break;
            case R.id.rl_share:
                showHideListShare(false);
                break;
            case R.id.btn_share_facebook:
                showHideListShare(false);
                sharingToSocialMedia("com.facebook.katana", uriImg);
                break;
            case R.id.btn_share_twitter:
                showHideListShare(false);
                sharingToSocialMedia("com.twitter.android", uriImg);
                break;
            case R.id.btn_share_linkedin:
                showHideListShare(false);
                sharingToSocialMedia("com.linkedin.android", uriImg);
                break;
            case R.id.btn_share_instagram:
                showHideListShare(false);
                sharingToSocialMedia("com.instagram.android", uriImg);
                break;
            case R.id.btn_share_pinterest:
                showHideListShare(false);
                sharingToSocialMedia("com.pinterest", uriImg);
                break;
        }
    }

    /**
     * Facebook - "com.facebook.katana"
     * Twitter - "com.twitter.android"
     * LinkedIn - "com.linkedin.android"
     * Instagram - "com.instagram.android"
     * Pinterest - "com.pinterest"
     *
     * @param packageName
     * @param bmpUri
     */
    private void sharingToSocialMedia(String packageName, Uri bmpUri) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, bmpUri);

        boolean installed = appInstallOrNot(packageName);
        if (installed) {
            intent.setPackage(packageName);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Installed application first", Toast.LENGTH_LONG).show();
        }

    }

    private boolean appInstallOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(ShareActivity.this);
        final View mDialogView = View.inflate(ShareActivity.this, R.layout.dialog_rate, null);

        mDialogBuilder.setView(mDialogView);
        final AlertDialog mDialog = mDialogBuilder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(true);
        final RatingBar ratingBar = (RatingBar) mDialogView.findViewById(R.id.rtb_rate);
        mDialogView.findViewById(R.id.imb_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingBar.getRating() >= 4) {
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        setRated("Rate", true);
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        setRated("Rate", true);
                    }
                    mDialog.dismiss();
                } else {
                    if (ratingBar.getRating() > 0) {
                        setRated("Rate", true);
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Thanks for rating", Toast.LENGTH_SHORT).show();
                    } else {
                        mDialog.dismiss();
                        onBackPressed();
                    }
                }

            }
        });

        mDialogView.findViewById(R.id.imb_cancel_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                onBackPressed();
            }
        });
        return mDialog;
    }

    private boolean getStatusRate(String name, String key) {
        SharedPreferences preferences = getSharedPreferences(name, MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    private void setRated(String name, boolean rated) {
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rated", rated);
        editor.commit();

    }
}
