package funia.maker.pip.picjoke.scopic.activity;

import android.content.Intent;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.bumptech.glide.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.other.Defines;
import funia.maker.pip.picjoke.scopic.other.Device;
import funia.maker.pip.picjoke.scopic.other.GetActive;
import funia.maker.pip.picjoke.scopic.utility.Utils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton imb_Gallery;
    private ImageButton imb_Create;
    private ImageButton imb_MoreApp;
    //GG ads
    private NativeExpressAdView nativeAdGG;
    private RelativeLayout rlNativeAds;
    private int mAdViewWidth;
    private int mAdViewHeight;

    //Ads gg native

    private static MainActivity mContext;

    public static MainActivity getContext() {
        return mContext;
    }

    @Override
    protected int getLayoutResource() {
        mContext = this;
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables() {
        imb_Gallery = (ImageButton) findViewById(R.id.imb_gallery);
        imb_Create = (ImageButton) findViewById(R.id.imb_create);
        imb_MoreApp = (ImageButton) findViewById(R.id.imb_more_app);
        rlNativeAds = (RelativeLayout) findViewById(R.id.rl_native_ads);

        rlNativeAds.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d("test Global layout", "test test");
                rlNativeAds.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mAdViewWidth = rlNativeAds.getWidth();
                mAdViewHeight = rlNativeAds.getHeight();
                if (!Device.getInstance().isActivated()) {
                    showNativeAdmob();
                }
                else
                {
                    rlNativeAds.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void initData() {
        imb_Create.setOnClickListener(this);
        imb_Gallery.setOnClickListener(this);
        imb_MoreApp.setOnClickListener(this);
//        if (Utils.checkInternetConnection(this)) {
//            if(!GetActive.getActiveForSku(getFilesDir())){
//                showNativeAdsOfGG();
//            }
//        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_gallery:
                imb_Create.setAlpha(0.6f);
                imb_Gallery.setAlpha(1f);
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.imb_create:
                imb_Create.setAlpha(1f);
                imb_Gallery.setAlpha(0.6f);
                startActivity(new Intent(MainActivity.this, FrameActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.imb_more_app:
                startActivity(new Intent(MainActivity.this, MoreAppActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    public void showNativeAdsOfGG() {
        nativeAdGG = (NativeExpressAdView) findViewById(R.id.native_add_gg);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getResources().getString(R.string.id_device_test_gg))
                .build();
        nativeAdGG.loadAd(adRequest);
    }

    //SonTH added
    private void showNativeAdmob() {
        //int width = rlNativeAds.getWidth();
        //int height = rlNativeAds.getHeight();
        //Log.d("test Global layout", "size " + mAdViewWidth + mAdViewHeight);
        rlNativeAds.removeAllViews();
        NativeExpressAdView adView = new NativeExpressAdView(mContext);
        int sizeW = Utils.convertPixelsToDp(mAdViewWidth, mContext);
        int sizeH = Utils.convertPixelsToDp(mAdViewHeight, mContext);
        adView.setAdSize(new AdSize((int)(sizeW), (int)(sizeH)));
        //Log.d("test Global layout 1", "size " + sizeW + sizeH);
        //adView.setAdUnitId(Defines.ADMOB_NATIVE_ID_START_MEDIUM);
        //adView.setAdUnitId(Defines.ADMOB_NATIVE_ID_SETTING);
        adView.setAdUnitId(Defines.ADMOB_NATIVE_ID_START_LARGE);
        rlNativeAds.addView(adView, mAdViewWidth, mAdViewHeight);

        adView.loadAd(new AdRequest.Builder().build());

        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("assss", "Error :" + i);
                switch (i) {
                    case com.google.android.gms.ads.AdRequest.ERROR_CODE_INTERNAL_ERROR:
                        Log.d("assss", "Error: ERROR_CODE_INTERNAL_ERROR :" + i);
                        break;
                    case com.google.android.gms.ads.AdRequest.ERROR_CODE_INVALID_REQUEST:
                        Log.d("assss", "Error: ERROR_CODE_INVALID_REQUEST :" + i);
                        break;
                    case com.google.android.gms.ads.AdRequest.ERROR_CODE_NETWORK_ERROR:
                        Log.d("assss", "Error: ERROR_CODE_NETWORK_ERROR :" + i);
                        break;
                    case com.google.android.gms.ads.AdRequest.ERROR_CODE_NO_FILL:
                        Log.d("assss", "Error: ERROR_CODE_NO_FILL :" + i);
                        break;
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d("assss", "Opened");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("assss", "Loaded");
                //imgvBackground.setImageDrawable(null);
            }
        });
    }

    public static String encryptIt(String value, String cryptoPass) {
        try {
            DESKeySpec keySpec = new DESKeySpec(cryptoPass.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] clearText = value.getBytes("UTF8");
            // Cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
            return encrypedValue;

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return value;
    }

    ;

    public static String decryptIt(String value, String cryptoPass) {
        try {
            DESKeySpec keySpec = new DESKeySpec(cryptoPass.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
            // cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

            String decrypedValue = new String(decrypedValueBytes);
            return decrypedValue;

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return value;
    }

    static {
        System.loadLibrary("encrypt");
    }

    public static native String getBaseUrl();

    public static native String getImageBaseUrl();

    public static native String getPart0();

    public static native String getPart1();

    public static native String getPart2();

    public static native String getPart3();

    public static native String getPart4();

    public static native String getCrypto(String s0, String s1, String s2, String s3, String s4);
}
