// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package funia.maker.pip.picjoke.scopic.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.cropper.CropImage;
import funia.maker.pip.picjoke.scopic.cropper.CropImageOptions;
import funia.maker.pip.picjoke.scopic.cropper.CropImageView;
import funia.maker.pip.picjoke.scopic.impls.OnDownloadFrameListener;
import funia.maker.pip.picjoke.scopic.model.Image;
import funia.maker.pip.picjoke.scopic.task.DownloadFrameTask;
import funia.maker.pip.picjoke.scopic.utility.Utils;

/**
 * Built-in activity for image cropping.<br>
 * Use {@link CropImage#activity(Uri)} to create a builder to start this activity.
 */

public class CropImageActivity extends BaseActivity implements View.OnClickListener,
        CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener,
        OnDownloadFrameListener {

    /**
     * The crop image view library widget used in the activity
     */
    private CropImageView mCropImageView;

    /**
     * Persist URI image to crop URI if specific permissions are required
     */
    private Uri mCropImageUri;

    /**
     * the options that were set for the crop image
     */
    private CropImageOptions mOptions;

    /**
     *
     */
    private ImageButton imb_Done, imb_Back;
    private RelativeLayout imgRotateLeft, imgRotateRight, imgFlipHorizontal, imgFlipVertical;

    //    private int imgFrame;
    private Uri imgUri;
    private Image imageInfo;
    private String framePath;
    private int aspectRatioX, aspectRatioY;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_crop_image;
    }

    @Override
    protected void initVariables() {
        mCropImageView = (CropImageView) findViewById(R.id.cropImageView);
        imb_Done = (ImageButton) findViewById(R.id.imb_done_crop);
        imb_Back = (ImageButton) findViewById(R.id.imb_back_crop);

        imgRotateLeft = (RelativeLayout) findViewById(R.id.imgRotateLeft);
        imgRotateRight = (RelativeLayout) findViewById(R.id.imgRotateRight);
        imgFlipHorizontal = (RelativeLayout) findViewById(R.id.imgFlipHorizontal);
        imgFlipVertical = (RelativeLayout) findViewById(R.id.imgFlipVertical);
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        mCropImageUri = intent.getParcelableExtra(CropImage.CROP_IMAGE_EXTRA_SOURCE);
        mOptions = intent.getParcelableExtra(CropImage.CROP_IMAGE_EXTRA_OPTIONS);

        mCropImageView.setImageUriAsync(mCropImageUri);

        imb_Done.setOnClickListener(this);
        imb_Back.setOnClickListener(this);


//        imgFrame = intent.getExtras().getInt("Image");
        imageInfo = (Image) intent.getExtras().getSerializable("Rect");

        aspectRatioX = Integer.parseInt(imageInfo.getAspectRatioX());
        aspectRatioY = Integer.parseInt(imageInfo.getAspectRatioY());

        mCropImageView.setAspectRatio(aspectRatioX, aspectRatioY);

        imgRotateRight.setOnClickListener(this);
        imgRotateLeft.setOnClickListener(this);
        imgFlipVertical.setOnClickListener(this);
        imgFlipHorizontal.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCropImageView.setOnSetImageUriCompleteListener(null);
        mCropImageView.setOnCropImageCompleteListener(null);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResultCancel();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imb_done_crop:
                cropImage();
                break;
            case R.id.imb_back_crop:
                onBackPressed();
                break;

            case R.id.imgRotateLeft:
                rotateImage(-90);
                break;

            case R.id.imgRotateRight:
                rotateImage(90);
                break;

            case R.id.imgFlipHorizontal:
                mCropImageView.setImageBitmap(flip(mCropImageView.getImageBitmap(), Direction.HORIZONTAL));
                break;

            case R.id.imgFlipVertical:
                mCropImageView.setImageBitmap(flip(mCropImageView.getImageBitmap(), Direction.VERTICAL));
                break;
        }
    }

    private Bitmap flip(Bitmap src, Direction type) {
        Matrix matrix = new Matrix();
        if (type == Direction.HORIZONTAL) {
            matrix.preScale(1.0f, -1.0f);
        } else if (type == Direction.VERTICAL) {
            matrix.preScale(-1.0f, 1.0f);
        } else {
            return src;
        }
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    @Override
    public void onDownloadComplete(boolean complete) {
        // complete download start activity
        if (complete) {
            startActivity(EditActivity.createIntent(this, imgUri, imgUri.toString(), imageInfo, framePath));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }


    private enum Direction {
        VERTICAL, HORIZONTAL
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            if (mOptions.initialCropWindowRectangle != null) {
                mCropImageView.setCropRect(mOptions.initialCropWindowRectangle);
            }
            if (mOptions.initialRotation > -1) {
                mCropImageView.setRotatedDegrees(mOptions.initialRotation);
            }
        } else {
            setResult(null, error, 1);
        }

        mCropImageView.setImageBitmap(flip(mCropImageView.getImageBitmap(), Direction.VERTICAL));
        mCropImageView.setImageBitmap(flip(mCropImageView.getImageBitmap(), Direction.VERTICAL));
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        startResultActivity(result.getUri());
    }

    private void startResultActivity(Uri uri) {
        if (isFinishing()) return;
        File frameFolder = new File(getFilesDir(), "PicJokeFrame");
        if (!frameFolder.exists()) {
            frameFolder.mkdirs();
        }
        File frame = new File(frameFolder, imageInfo.getName());
        framePath = frame.getPath();
        if (frame.exists()) {
            // start activity
            startActivity(EditActivity.createIntent(this, uri, uri.toString(), imageInfo, framePath));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        } else {
            //download frame
            if (Utils.checkInternetConnection(this)) {
                imgUri = uri;
                DownloadFrameTask downloadFrameTask = new DownloadFrameTask(this, framePath);
                downloadFrameTask.setOnDownloadFrameListener(this);
                downloadFrameTask.execute(imageInfo.getUrl());
            } else {
                Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
                frame.delete();
                finish();
            }
        }

    }


    //region: Private methods

    /**
     * Execute crop image and save the result tou output uri.
     */
    protected void cropImage() {
        if (mOptions.noOutputImage) {
            setResult(null, null, 1);
        } else {
            Uri outputUri = getOutputUri();
            mCropImageView.saveCroppedImageAsync(outputUri,
                    mOptions.outputCompressFormat,
                    mOptions.outputCompressQuality,
                    mOptions.outputRequestWidth,
                    mOptions.outputRequestHeight,
                    mOptions.outputRequestSizeOptions);
        }
    }

    /**
     * Rotate the image in the crop image view.
     */
    protected void rotateImage(int degrees) {
        mCropImageView.rotateImage(degrees);
    }

    /**
     * Get Android uri to save the cropped image into.<br>
     * Use the given in options or create a temp file.
     */
    protected Uri getOutputUri() {
        Uri outputUri = mOptions.outputUri;
        if (outputUri.equals(Uri.EMPTY)) {
            try {
                String ext = mOptions.outputCompressFormat == Bitmap.CompressFormat.JPEG ? ".jpg" :
                        mOptions.outputCompressFormat == Bitmap.CompressFormat.PNG ? ".png" : ".webp";
                outputUri = Uri.fromFile(File.createTempFile("cropped", ext, getCacheDir()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to create temp file for output image", e);
            }
        }
        return outputUri;
    }

    /**
     * Result with cropped image data or error if failed.
     */
    protected void setResult(Uri uri, Exception error, int sampleSize) {
        int resultCode = error == null ? RESULT_OK : CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;
        setResult(resultCode, getResultIntent(uri, error, sampleSize));
        finish();
    }

    /**
     * Cancel of cropping activity.
     */
    protected void setResultCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Get intent instance to be used for the result of this activity.
     */
    protected Intent getResultIntent(Uri uri, Exception error, int sampleSize) {
        CropImage.ActivityResult result = new CropImage.ActivityResult(null,
                uri,
                error,
                mCropImageView.getCropPoints(),
                mCropImageView.getCropRect(),
                mCropImageView.getRotatedDegrees(),
                sampleSize);
        Intent intent = new Intent();
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_RESULT, result);
        return intent;
    }
    //endregion
}

