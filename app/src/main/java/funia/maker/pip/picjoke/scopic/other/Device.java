package funia.maker.pip.picjoke.scopic.other;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import funia.maker.pip.picjoke.scopic.activity.MainActivity;

/**
 * Created by tranhoaison on 4/22/15.
 */

public class Device {

    private static Device self;
    /**
     * API >= 11 (3.0)
     */
    public static final boolean isHONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
    /**
     * API >= 16 (4.1)
     */
    public static final boolean isJELLYBEAN = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
    /**
     * API >= 19 (4.4)
     */
    public static final boolean isKITKAT = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT;

    private boolean isActive = false;


    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEditor;

    public static Device getInstance() {
        if (self == null) {
            self = new Device();
        }
        return self;
    }

    @SuppressLint("DefaultLocale")
    public Device() {

        mPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        mPrefEditor = mPref.edit();

        isActive = mPref.getBoolean(Defines.ACTIVE_PREF, false);
    }

    public boolean checkActiveForSku(String sku)
    {
        boolean bRet = mPref.getBoolean(sku, false);
        //bRet = true;
        return bRet;
    }

    public boolean isActivated()
    {
        return isActive;
    }

    public void setPreferenceForSku(String sku, boolean isActivated) {
        if (isActivated) {
            mPrefEditor.putBoolean(Defines.ACTIVE_PREF, isActivated);
        }

        mPrefEditor.putBoolean(sku, isActivated);
        mPrefEditor.commit();
    }
}
