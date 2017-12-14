package funia.maker.pip.picjoke.scopic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageOverlayBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import funia.maker.pip.picjoke.scopic.impls.OnAnimationChangeListener;
import funia.maker.pip.picjoke.scopic.utility.BitmapUtils;

public class SolShapeView extends View {

    private float mRatioWidth, mRatioHeight;
    private int mLeft, mTop, mRight, mBottom;
    private Bitmap mBitmap;
    private Rect mRect;
    private Bitmap drawBitmap;
    private float mRotationDegrees = 0.f;
    private LinkedHashMap<String, GPUImageFilter> mFilterLinkedHashMap;
    private Context mContext;

    public void setOnAnimationChangeListener(OnAnimationChangeListener onAnimationChangeListener) {
        this.mOnAnimationChangeListener = onAnimationChangeListener;
    }

    private OnAnimationChangeListener mOnAnimationChangeListener;


    public SolShapeView(Context context) {
        this(context, null, 0);
    }

    public SolShapeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SolShapeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(Context context, int actualWidth, int widthImageView, int actualHeight, int heightImageView,
                     Bitmap bitmap, int actualLeft, int actualTop, int actualRight, int actualBottom,
                     float rotation, String textureName, float saturation) {

        this.mContext = context;
        this.mBitmap = bitmap;
        this.mRotationDegrees = rotation;
        this.mFilterLinkedHashMap = new LinkedHashMap<>();

        mRatioWidth = (float) actualWidth / widthImageView;
        mRatioHeight = (float) actualHeight / heightImageView;

        mLeft = (int) (actualLeft / mRatioWidth);
        mTop = (int) (actualTop / mRatioHeight);
        mRight = (int) (actualRight / mRatioWidth);
        mBottom = (int) (actualBottom / mRatioHeight);

        mRect = new Rect(mLeft, mTop, mRight, mBottom);

        onSelectedTexture(textureName, mRotationDegrees, saturation);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawBitmap != null) {
            canvas.drawBitmap(drawBitmap, null, mRect, null);
        }

    }

    private Bitmap rotateBitmap(Bitmap source, float rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void onSelectedTexture(String textureName, float rotate, float saturation) {
        //If "texture": "NULL" => Image no contain texture
        if (!textureName.equalsIgnoreCase("NULL")) {
            GPUImageOverlayBlendFilter filter = new GPUImageOverlayBlendFilter();
            try {
                String s = "textures/" + textureName;
                filter.setBitmap(BitmapFactory.decodeStream(mContext.getAssets().open(s)));
                initializeFilters("texture", filter, rotate);

            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else {
            drawBitmap = rotateBitmap(mBitmap, rotate);
        }

        //Default: Saturation = 1.0f
        if (saturation != 1.0f) {
            GPUImageSaturationFilter gpuImageSaturationFilter = new GPUImageSaturationFilter();
            gpuImageSaturationFilter.setSaturation(saturation);
            initializeFilters("saturation", gpuImageSaturationFilter, rotate);
        }

    }


    private void initializeFilters(String key, GPUImageFilter filter, float rotate) {
        new ApplyFilterTask(mContext, mBitmap, key, filter, mFilterLinkedHashMap, rotate).execute();
    }

    private class ApplyFilterTask extends AsyncTask<Void, Void, Bitmap> {

        private Context mContext;
        private String mKey;
        private Bitmap bm;
        private GPUImageFilter mFilter;
        private LinkedHashMap<String, GPUImageFilter> mFilterLinkedHashMap;
        private ArrayList<GPUImageFilter> mArrFilter;
        private float rotate;

        private ApplyFilterTask(Context context, Bitmap bitmap, String key, GPUImageFilter filter, LinkedHashMap<String, GPUImageFilter> filterLinkedHashMap, float rotate) {
            mContext = context;
            mKey = key;
            mFilter = filter;
            bm = bitmap;
            mFilterLinkedHashMap = filterLinkedHashMap;
            this.rotate = rotate;
        }

        protected void onPreExecute() {

            mArrFilter = new ArrayList();
            if (mOnAnimationChangeListener != null) {
                mOnAnimationChangeListener.onStartAnimation();
            }

        }

        protected Bitmap doInBackground(Void... params) {
            GPUImage thumbView = new GPUImage(mContext);
            if (!mKey.equals("none")) {
                mFilterLinkedHashMap.put(mKey, this.mFilter);
            }
            for (String key : mFilterLinkedHashMap.keySet()) {
                mArrFilter.add(mFilterLinkedHashMap.get(key));
            }
            thumbView.setFilter(new GPUImageFilterGroup(mArrFilter));
            return thumbView.getBitmapWithFilterApplied(bm);
        }


        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (mOnAnimationChangeListener != null) {
                mOnAnimationChangeListener.onEndAnimation();
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            if (bitmap.getWidth() > 1080.0f) {
                bitmap = BitmapUtils.getThumbnail(byteArray, 1920, 1080);
            }
            drawBitmap = rotateBitmap(bitmap, rotate);
            invalidate();
        }
    }


}
