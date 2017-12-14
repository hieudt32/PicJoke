package funia.maker.pip.picjoke.scopic.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.billing.IabHelper;
import com.google.android.billing.IabResult;
import com.google.android.billing.Inventory;
import com.google.android.billing.Purchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import funia.maker.pip.picjoke.scopic.BuildConfig;
import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.adapter.CategoryAdapter;
import funia.maker.pip.picjoke.scopic.adapter.PicJokeAdapter;
import funia.maker.pip.picjoke.scopic.common.Constants;
import funia.maker.pip.picjoke.scopic.cropper.CropImage;
import funia.maker.pip.picjoke.scopic.cropper.CropImageView;
import funia.maker.pip.picjoke.scopic.impls.OnFinishAsyncTaskListener;
import funia.maker.pip.picjoke.scopic.impls.OnItemRecyclerViewClickListener;
import funia.maker.pip.picjoke.scopic.impls.OnParsingJsonListener;
import funia.maker.pip.picjoke.scopic.model.Category;
import funia.maker.pip.picjoke.scopic.model.Image;
import funia.maker.pip.picjoke.scopic.model.Rect;
import funia.maker.pip.picjoke.scopic.other.Defines;
import funia.maker.pip.picjoke.scopic.other.Device;
import funia.maker.pip.picjoke.scopic.task.ParsingJsonTask;
import funia.maker.pip.picjoke.scopic.utility.OnItemRecyclerViewTouchListener;

public class FrameActivity extends BaseActivity implements View.OnClickListener,
        OnFinishAsyncTaskListener, OnParsingJsonListener {

    private RecyclerView rcvCategory;
    private RecyclerView rcvItems;

    private ImageButton imb_Back;
    private RelativeLayout rl_Template;
    private ImageButton imb_ShowTemplate;
    private FrameLayout fr_Loading;
    private ImageView imv_Loading;
    private Animation mAnimation;
    private int imgFrame;
    private Image imageInfo;
    private Uri uri;


    private String mStrHost;
    public static int FLAG_GET_CATEGORY = 0;
    public static int FLAG_GET_ITEMS = 1;
    public static final String CAT_PREF = "category_pref";
    private CategoryAdapter mCategoryAdapter;
    private ArrayList<Category> mListCategory;
    private ArrayList<Image> mListItems;
    private Device mDevice;
    private IabHelper mHelper;
    private Category mSelectedCat;

    private boolean createdItemClick = false;

    private AlertDialog dialogCamera;
    private AlertDialog dialogPurchase;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_frame;
    }

    @Override
    protected void initVariables() {
        imb_Back = (ImageButton) findViewById(R.id.imb_back_frame);

        rcvCategory = (RecyclerView) findViewById(R.id.rcv_category);

        rcvCategory.setHasFixedSize(true);
        rcvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        rcvItems = (RecyclerView) findViewById(R.id.rcv_item);

        fr_Loading = (FrameLayout) findViewById(R.id.fr_loading_frame);
        imv_Loading = (ImageView) findViewById(R.id.imv_loading_frame);
        rl_Template = (RelativeLayout) findViewById(R.id.rl_template);
        imb_ShowTemplate = (ImageButton) findViewById(R.id.imb_show_template);
        rl_Template.setVisibility(View.GONE);

        imb_Back.setOnClickListener(this);
        imb_ShowTemplate.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mListCategory = new ArrayList<>();
        mListItems = new ArrayList<>();

        onStartAnim();

        //SonTH added
        mDevice = Device.getInstance();
        checkingPurchase();
        mStrHost = MainActivity.decryptIt(MainActivity.getBaseUrl(),
                MainActivity.getCrypto(MainActivity.getPart0(), MainActivity.getPart1(),
                        MainActivity.getPart2(), MainActivity.getPart3(), MainActivity.getPart4()));

        mStrHost = "http://picjoke.liforte.com/api/picjoke"; //for testing
        getCategory();
    }

    public boolean checkNetworkState(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public void getCategory() {
        if (checkNetworkState(this)) {
            ParsingJsonTask parsingJsonTask = new ParsingJsonTask(this);
            parsingJsonTask.setOnFinishAsyncTask(this);
            parsingJsonTask.setOnParsingJsonListener(this);
            parsingJsonTask.setFlag(FLAG_GET_CATEGORY);
            parsingJsonTask.execute(mStrHost + "/v1/GetTopic");
        } else {
            fr_Loading.setVisibility(View.GONE);
            File fileCategory = new File(getFilesDir(), "category.txt");
            FileInputStream fin = null;
            String decrypt = "";
            String strTemp = "";
            try {
                fin = new FileInputStream(fileCategory);
                try {
                    decrypt = convertStreamToString(fin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (decrypt.length() > 0) {
                strTemp = MainActivity.decryptIt(decrypt, MainActivity.getCrypto(MainActivity.getPart0(),
                        MainActivity.getPart1(), MainActivity.getPart2(), MainActivity.getPart3(), MainActivity.getPart4()));
                if (strTemp.length() > 0) {
                    mListCategory.clear();
                    try {
                        JSONObject rootObject = new JSONObject(strTemp);
                        JSONArray arrCategory = rootObject.getJSONArray("content");
                        for (int i = 0; i < arrCategory.length(); i++) {
                            JSONObject category = arrCategory.getJSONObject(i);
                            String id = category.getString("ID");
                            String name = category.getString("Name");
                            boolean lock = category.getBoolean("Lock");
                            mListCategory.add(new Category(id, name, lock));
                        }
                        buildCategory(mListCategory);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void buildCategory(ArrayList<Category> arr) {
        mCategoryAdapter = new CategoryAdapter(this, arr);
        rcvCategory.setAdapter(mCategoryAdapter);

        String catePref = PreferenceManager.getDefaultSharedPreferences(this).getString(CAT_PREF, "1");
        android.content.SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        boolean check = false;
        for (int i = 0; i < arr.size(); i++) {
            Category category = arr.get(i);
            if (category.getId().equals(catePref)) {
                check = true;
                getItemsWithCategory(category);
                break;
            }
        }

        if (!check) {
            if (arr.size() > 0) {
                Category category = arr.get(0);
                if (category.getId().equals(catePref)) {
                    getItemsWithCategory(category);
                }
            }
        }
    }

    public void getItemsWithCategory(Category cat) {
        mSelectedCat = cat;
        android.content.SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(CAT_PREF, cat.getId());
        editor.commit();

        if (checkNetworkState(this)) {
            ParsingJsonTask parsingJsonTask = new ParsingJsonTask(this);
            parsingJsonTask.setOnFinishAsyncTask(this);
            parsingJsonTask.setOnParsingJsonListener(this);
            parsingJsonTask.setFlag(FLAG_GET_ITEMS);
            parsingJsonTask.setCategoryId(cat.getId());
            parsingJsonTask.execute(mStrHost + "/v1/GetTemplate/" + cat.getId());
        } else {
            fr_Loading.setVisibility(View.GONE);
            rl_Template.setVisibility(View.GONE);
            imb_ShowTemplate.setImageResource(R.drawable.ic_show_template);
            String fileName = "cat_" + cat.getId() + ".txt";
            File fileItem = new File(getFilesDir(), fileName);
            FileInputStream fin = null;
            String decrypt = "";
            String strTemp = "";
            try {
                fin = new FileInputStream(fileItem);
                try {
                    decrypt = convertStreamToString(fin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (decrypt.length() > 0) {
                strTemp = MainActivity.decryptIt(decrypt, MainActivity.getCrypto(MainActivity.getPart0(),
                        MainActivity.getPart1(), MainActivity.getPart2(), MainActivity.getPart3(), MainActivity.getPart4()));
                try {
                    mListItems.clear();
                    JSONObject Images = new JSONObject(strTemp);
                    JSONArray ImagesJSONArray = Images.getJSONArray("content");
                    for (int i = 0; i < ImagesJSONArray.length(); i++) {
                        JSONObject image = ImagesJSONArray.getJSONObject(i);
                        String id = image.getString("TopicID");
                        String name = image.getString("Name");
                        String url = image.getString("Url");
                        String thumbnail = image.getString("Thumbnail");
                        String width = image.getString("Width");
                        String height = image.getString("Height");
                        String rotate = image.getString("Rotate");
                        String aspectRatioX = image.getString("AspectRatioX");
                        String aspectRatioY = image.getString("AspectRatioY");
                        String textureName = image.getString("TextureName");
                        String saturation = image.getString("Saturation");

                        JSONObject rect = image.getJSONObject("Rect");
                        String left = rect.getString("left");
                        String top = rect.getString("top");
                        String right = rect.getString("right");
                        String bottom = rect.getString("bottom");
                        Rect myRect = new Rect(left, top, right, bottom);
                        mListItems.add(new Image(id, name, url, thumbnail, width, height, myRect, rotate, aspectRatioX, aspectRatioY, textureName, saturation));
                    }
                    createRecycleView(mListItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private AlertDialog showPurchaseDialog() {
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(FrameActivity.this);
        View mDialogView = View.inflate(FrameActivity.this, R.layout.dialog_purchase, null);

        mDialogBuilder.setView(mDialogView);
        final AlertDialog mDialog = mDialogBuilder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(true);

        Button btnUnlockCat = (Button) mDialogView.findViewById(R.id.imbUnlockCat);
        btnUnlockCat.setText(mSelectedCat.getName() + " (~ $1.99)");
        mDialogView.findViewById(R.id.imbUnlockCat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                requestPurchase(Defines.SKU_UNLOCK_CAT + "_" + mSelectedCat.getId(), Defines.PAYLOAD_UNLOCK_CAT + "_" + mSelectedCat.getId());
            }
        });

        mDialogView.findViewById(R.id.imbUnlockAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                requestPurchase(Defines.SKU_UNLOCK_ALL, Defines.PAYLOAD_UNLOCK_ALL);
            }
        });
        return mDialog;
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(FrameActivity.this);
        View mDialogView = View.inflate(FrameActivity.this, R.layout.dialog_camera_album, null);

        mDialogBuilder.setView(mDialogView);
        final AlertDialog mDialog = mDialogBuilder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(true);

        mDialogView.findViewById(R.id.imbAlbum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                startActivityForResult(pickIntent, Constants.REQUEST_PICK_IMAGE);
                mDialog.dismiss();
            }
        });

        mDialogView.findViewById(R.id.imbCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    File phoFile = null;
//                    try {
//                        phoFile = createImageFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (phoFile != null) {
//                        Uri phoUri = Uri.fromFile(phoFile);
//                        uri = Uri.fromFile(phoFile);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, phoUri);
//                        startActivityForResult(intent, Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//                    }
//                }

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uriImage;

                File phoFile = null;
                try {
                    phoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (phoFile != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        String s = BuildConfig.APPLICATION_ID + ".provider";
                        uriImage = FileProvider.getUriForFile(getApplicationContext(), s, phoFile);
                    } else {
                        uriImage = Uri.fromFile(phoFile);
                    }
                    uri = uriImage;
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
                    startActivityForResult(intent, Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
                mDialog.dismiss();
            }
        });
        return mDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (mHelper == null) return;
        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, result)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, result);
            if (requestCode == Constants.REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
                startCropImageActivity(result.getData());
            } else if (requestCode == Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                startCropImageActivity(uri);
            }
        }

    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setMultiTouchEnabled(true)
                .start(this, imageInfo);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = Constants.PHOTO_FILE_NAME + "_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_back_frame:
                onBackPressed();
                break;
            case R.id.imb_show_template:
                //showAndHideListTemplate(true);
                if (rl_Template.getVisibility() != View.VISIBLE) {
                    rl_Template.setVisibility(View.VISIBLE);
                    imb_ShowTemplate.setImageResource(R.drawable.ic_hide_template);
                    mCategoryAdapter.notifyDataSetChanged();
                } else {
                    rl_Template.setVisibility(View.GONE);
                    imb_ShowTemplate.setImageResource(R.drawable.ic_show_template);
                }
                break;
        }
    }

    private void createRecycleView(final ArrayList<Image> listImage) {
        PicJokeAdapter adapter = new PicJokeAdapter(this, listImage);
        rcvItems.setAdapter(adapter);
        rcvItems.setLayoutManager(new GridLayoutManager(this, 2));
        if (!createdItemClick) {
            rcvItems.addOnItemTouchListener(new OnItemRecyclerViewTouchListener(this, new OnItemRecyclerViewClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (rl_Template.isShown()) {
                        rl_Template.setVisibility(View.GONE);
                        imb_ShowTemplate.setImageResource(R.drawable.ic_show_template);
                    }
                    imageInfo = mListItems.get(position);
                    boolean bUnlock = false;
                    if (mSelectedCat.getLock()) {
                        if (Device.getInstance().checkActiveForSku(Defines.SKU_UNLOCK_ALL)) {
                            bUnlock = true;
                        } else {
                            if (Device.getInstance().checkActiveForSku(Defines.SKU_UNLOCK_CAT + "_" + mSelectedCat.getId())) {
                                bUnlock = true;
                            } else {
                                bUnlock = false;
                            }
                        }
                    } else {
                        bUnlock = true;
                    }

                    if (bUnlock) {
//                        if (dialogCamera != null) {
//                            dialogCamera.show();
//                        } else {
//                            dialogCamera = createDialog();
//                            dialogCamera.show();
//                        }
                        dialogCamera = createDialog();
                        dialogCamera.show();
                    } else {
//                        if (dialogPurchase != null) {
//                            dialogPurchase.show();
//                        } else {
//                            dialogPurchase = showPurchaseDialog();
//                            dialogPurchase.show();
//                        }
                        dialogPurchase = showPurchaseDialog();
                        dialogPurchase.show();
                    }
                }
            }));
            createdItemClick = true;
        }
        adapter.notifyDataSetChanged();
    }

    private void onStartAnim() {
        fr_Loading.setVisibility(View.VISIBLE);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_loading);
        fr_Loading.setVisibility(View.VISIBLE);
        imv_Loading.startAnimation(mAnimation);
    }

    @Override
    public void onFinishAsyncTask(String path) {
        fr_Loading.setVisibility(View.GONE);
        if (mAnimation != null) {
            mAnimation.cancel();
        }
    }

    @Override
    public void onCompleteParsingJson(ParsingJsonTask parsingJsonTask, ArrayList arr) {
        if (parsingJsonTask.getFlag() == FLAG_GET_CATEGORY) {
            mListCategory.clear();
            mListCategory = arr;
            buildCategory(mListCategory);
        } else {
            rl_Template.setVisibility(View.GONE);
            imb_ShowTemplate.setImageResource(R.drawable.ic_show_template);
            mListItems.clear();
            mListItems = arr;
            createRecycleView(arr);
        }
    }

    public void showToastMessage(String message, int flag) {
        if (flag == 0) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    public void checkingPurchase() {
        mHelper = new IabHelper(this, Defines.PUBLIC_ENCODE);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    showToastMessage("Problem checking your activated status", 0);
                    return;
                }
                if (mHelper == null) return;
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    public void requestPurchase(String sku, String payload) {
        mHelper.launchPurchaseFlow(this, sku, Defines.RC_REQUEST, mPurchaseFinishedListener, payload);
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (mHelper == null) return;
            if (result != null) {
                if (result.isFailure()) {
                    showToastMessage("Failed to query inventory: " + result, 0);
                    return;
                }
            }

            if (inventory != null) {
                boolean bCheckPurchase;
                Purchase purchase;

                bCheckPurchase = false;
                purchase = inventory.getPurchase(Defines.SKU_UNLOCK_ALL);
                if (purchase != null) {
                    bCheckPurchase = verifyDeveloperPayload(purchase, Defines.PAYLOAD_UNLOCK_ALL);
                    mDevice.setPreferenceForSku(Defines.SKU_UNLOCK_ALL, bCheckPurchase);
                    if (bCheckPurchase) {
                        for (int i = 0; i < mListCategory.size(); i++) {
                            Category category = mListCategory.get(i);
                            String sku = Defines.SKU_UNLOCK_CAT + "_" + category.getId();
                            String payload = Defines.PAYLOAD_UNLOCK_CAT + "_" + category.getId();
                            mDevice.setPreferenceForSku(sku, bCheckPurchase);
                        }
                    }
                }

                if (!bCheckPurchase) {
                    for (int i = 0; i < mListCategory.size(); i++) {
                        Category category = mListCategory.get(i);
                        bCheckPurchase = false;
                        String sku = Defines.SKU_UNLOCK_CAT + "_" + category.getId();
                        String payload = Defines.PAYLOAD_UNLOCK_CAT + "_" + category.getId();
                        purchase = inventory.getPurchase(sku);
                        if (purchase != null) {
                            bCheckPurchase = verifyDeveloperPayload(purchase, payload);
                            mDevice.setPreferenceForSku(sku, bCheckPurchase);
                        }
                    }
                }

                if (mCategoryAdapter != null) {
                    mCategoryAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (mHelper == null) return;
            if (result.isFailure()) {
                showToastMessage("Error purchasing: " + result, 0);
                return;
            }
            /*
            if (!verifyDeveloperPayload(purchase))
            {
                showErrorMessage("Error purchasing. Authenticity verification failed.");
                return;
            }
            */

            if (purchase != null) {
                for (int i = 0; i < mListCategory.size(); i++) {
                    Category category = mListCategory.get(i);
                    String sku = Defines.SKU_UNLOCK_CAT + "_" + category.getId();
                    if (purchase.getSku().equals(sku)) {
                        mDevice.setPreferenceForSku(sku, true);
                    }
                }

                if (purchase.getSku().equals(Defines.SKU_UNLOCK_ALL)) {
                    mDevice.setPreferenceForSku(Defines.SKU_UNLOCK_ALL, true);
                    for (int i = 0; i < mListCategory.size(); i++) {
                        Category category = mListCategory.get(i);
                        String sku = Defines.SKU_UNLOCK_CAT + "_" + category.getId();
                        mDevice.setPreferenceForSku(sku, true);
                    }
                }

                if (mCategoryAdapter != null) {
                    mCategoryAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    public boolean verifyDeveloperPayload(Purchase purchase, String payload) {
        if (purchase.getDeveloperPayload().equals(payload)) {
            return true;
        } else {
            return false;
        }
    }
}
