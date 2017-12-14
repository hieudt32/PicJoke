package funia.maker.pip.picjoke.scopic.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.impls.OnAnimationChangeListener;
import funia.maker.pip.picjoke.scopic.impls.OnFinishAsyncTaskListener;
import funia.maker.pip.picjoke.scopic.model.Image;
import funia.maker.pip.picjoke.scopic.model.Rect;
import funia.maker.pip.picjoke.scopic.other.GetActive;
import funia.maker.pip.picjoke.scopic.task.SaveImageAsyncTask;
import funia.maker.pip.picjoke.scopic.utility.Utils;
import funia.maker.pip.picjoke.scopic.view.SolShapeView;


public class EditActivity extends BaseActivity implements OnAnimationChangeListener, View.OnClickListener, OnFinishAsyncTaskListener {

    private static final String TAG = "EDIT: ";
    private ImageView imv_PicJoke;
    private ImageView imv_Watermark;
    private SolShapeView mSolShapeView;
    private FrameLayout frLoading;
    private ImageView imgLoading;
    private Animation mAnimation;
    private ImageButton imb_Back, imb_Done;
    private boolean isSaved;
    private RelativeLayout rlImage;
    private boolean checkAd = false;
    private boolean bUnlock = false;
    //gg ads
    private com.google.android.gms.ads.InterstitialAd interstitialAdGG;

    public static Intent createIntent(Activity activity, Uri uri, String imgUri, Image imageInfo, String framePath) {
        Intent intent = new Intent(activity, EditActivity.class);
        intent.setData(uri);
        intent.putExtra("ImageUri", imgUri);
        intent.putExtra("Rect", imageInfo);
        intent.putExtra("Frame", framePath);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        return intent;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initVariables() {
        imv_PicJoke = (ImageView) findViewById(R.id.imv_pic_joke);
        mSolShapeView = (SolShapeView) findViewById(R.id.solShapeView);
        frLoading = (FrameLayout) findViewById(R.id.frLoading);
        imgLoading = (ImageView) findViewById(R.id.imgLoading);

        imb_Back = (ImageButton) findViewById(R.id.imb_back_edit);
        imb_Done = (ImageButton) findViewById(R.id.imb_done_edit);

        rlImage = (RelativeLayout) findViewById(R.id.rlImage);

        imv_Watermark = (ImageView) findViewById(R.id.imv_watermark);

    }

    @Override
    protected void initData() {
        isSaved = false;
        setUpAds();
        bUnlock = GetActive.getActiveForSku(getFilesDir());
        Intent intent = getIntent();

        final String framePath = intent.getExtras().getString("Frame");
        Bitmap myBitmap = BitmapFactory.decodeFile(framePath);
        imv_PicJoke.setImageBitmap(myBitmap);
//        imv_PicJoke.setImageBitmap(null);

        final String imageUri = intent.getExtras().getString("ImageUri");
        final Image image = (Image) intent.getExtras().getSerializable("Rect");

        mSolShapeView.setOnAnimationChangeListener(this);

        imv_PicJoke.post(new Runnable() {
            @Override
            public void run() {

                mSolShapeView.getLayoutParams().height = imv_PicJoke.getMeasuredHeight();
                mSolShapeView.getLayoutParams().width = imv_PicJoke.getMeasuredWidth();
                if (!bUnlock) {
                    imv_Watermark.setImageResource(R.drawable.watermark);
                    imv_Watermark.setMaxWidth(imv_PicJoke.getMeasuredWidth() / 6);
                } else {
                    imv_Watermark.setVisibility(View.GONE);
                }
                Rect rect = image != null ? image.getRect() : null;

                Bitmap mBitmap = getBitmapFromUri(imageUri);
                if (mBitmap != null) {
                    assert image != null;
                    int actualWidth = Integer.parseInt(image.getWidth());
                    int widthImageView = imv_PicJoke.getMeasuredWidth();
                    int actualHeight = Integer.parseInt(image.getHeight());
                    int heightImageView = imv_PicJoke.getMeasuredHeight();
                    int actualLeft = Integer.parseInt(rect.getLeft());
                    int actualTop = Integer.parseInt(rect.getTop());
                    int actualRight = Integer.parseInt(rect.getRight());
                    int actualBottom = Integer.parseInt(rect.getBottom());
                    mSolShapeView.init(EditActivity.this, actualWidth, widthImageView, actualHeight,
                            heightImageView, mBitmap, actualLeft, actualTop, actualRight, actualBottom,
                            0, convertTextureName(image.getTextureName()),
                            Float.parseFloat(image.getSaturation()));
                    float mRatioWidth = (float) actualWidth / widthImageView;
                    float mRatioHeight = (float) actualHeight / heightImageView;

                    int mLeft = (int) (actualLeft / mRatioWidth);
                    int mTop = (int) (actualTop / mRatioHeight);

                    mSolShapeView.setPivotX(mLeft);
                    mSolShapeView.setPivotY(mTop);
                    mSolShapeView.setRotation(Float.parseFloat(image.getRotate()));
                }
            }
        });
        rlImage.invalidate();

        imb_Done.setOnClickListener(this);
        imb_Back.setOnClickListener(this);
    }

    private String convertTextureName(String urlTexture) {
        if (urlTexture != null) {
            String arr[] = urlTexture.split("/");
            return arr[arr.length - 1];
        }
        return null;
    }

    @Nullable
    private Bitmap getBitmapFromUri(String paths) {
        try {
            return MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(paths));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void onStartAnimation() {
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_loading);
        frLoading.setVisibility(View.VISIBLE);
        imgLoading.startAnimation(mAnimation);
    }

    @Override
    public void onEndAnimation() {
        frLoading.setVisibility(View.INVISIBLE);
        mAnimation.cancel();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imb_done_edit:
                imb_Done.setEnabled(false);
                imb_Back.setEnabled(false);
                if (isSaved) {
                    Log.d(TAG, "Image saved -> Gallery Screen");
                    startActivity(new Intent(this, GalleryActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } else {
                    Log.d(TAG, "Start AsyncTask -> Saved Image...");

                    frLoading.setVisibility(View.VISIBLE);
                    mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_loading);
                    frLoading.setVisibility(View.VISIBLE);
                    imgLoading.startAnimation(mAnimation);

                    SaveImageAsyncTask saveImageAsyncTask = new SaveImageAsyncTask();
                    saveImageAsyncTask.setOnFinishAsyncTask(this);
                    saveImageAsyncTask.execute(captureBitmapOfViews(rlImage));

                }

                break;
            case R.id.imb_back_edit:
                onBackPressed();
                break;
        }
    }

    /**
     * Capture View and Return Bitmap
     *
     * @param view
     * @return bitmap
     */
    private Bitmap captureBitmapOfViews(View view) {

        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true).copy(
                Bitmap.Config.ARGB_8888, false);
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }


    @Override
    public void onFinishAsyncTask(String path) {
        isSaved = true;
        frLoading.setVisibility(View.INVISIBLE);
        mAnimation.cancel();
        if (path != null) {
            if (!bUnlock) {
                if (checkAd) {
                    startShareActivity(path);
                } else {
                    showAdsGG(path);
                }
            } else {
                startShareActivity(path);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    //ddt ads
    //show ads of google

    protected void setUpAds() {
        MobileAds.initialize(this, getResources().getString(R.string.id_app_pic_joke));
        interstitialAdGG = new com.google.android.gms.ads.InterstitialAd(this);
        interstitialAdGG.setAdUnitId(getResources().getString(R.string.id_unit_gg_interstitial));
    }

    protected void showAdsGG(String path) {
        if (Utils.checkInternetConnection(this)) {
            loadInterstitialAdGG(path);
        } else {
            startShareActivity(path);
        }

    }

    private void loadInterstitialAdGG(final String path) {
        requestNewInterstitial();
        interstitialAdGG.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                if (interstitialAdGG.isLoaded()) {
                    interstitialAdGG.show();
                }
            }

            @Override
            public void onAdClosed() {
                startShareActivity(path);
                checkAd = true;
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Intent intent = new Intent(EditActivity.this, GalleryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startShareActivity(String path) {
        Intent intent = new Intent(EditActivity.this, ShareActivity.class);
        intent.putExtra("URI_IMAGE", "file://" + path);
        intent.putExtra("parent_activity", "0");
        startActivity(intent);
        finish();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getResources().getString(R.string.id_device_test_gg))
                .build();
        interstitialAdGG.loadAd(adRequest);
    }
}
