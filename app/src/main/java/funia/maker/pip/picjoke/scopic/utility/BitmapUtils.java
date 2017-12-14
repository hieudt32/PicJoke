package funia.maker.pip.picjoke.scopic.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;


public class BitmapUtils {

    public static Bitmap getThumbnail(byte[] bytes, int height, int width) {
        Options onlyBoundsOptions = new Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;
        onlyBoundsOptions.inPreferredConfig = Config.ARGB_8888;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, onlyBoundsOptions);
        if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
            return null;
        }
        int originalSize = onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
        double ratio = originalSize > height ? (double) (originalSize / width) : 1.0d;
        Options bitmapOptions = new Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Config.ARGB_8888;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, bitmapOptions);
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) {
            return 1;
        }
        return k;
    }


    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight <= 0 || maxWidth <= 0) {
            return image;
        }
        float ratioBitmap = ((float) image.getWidth()) / ((float) image.getHeight());
        int finalWidth = maxWidth;
        int finalHeight = maxHeight;
        if (((float) maxWidth) / ((float) maxHeight) > 1.0f) {
            finalWidth = (int) (((float) maxHeight) * ratioBitmap);
        } else {
            finalHeight = (int) (((float) maxWidth) / ratioBitmap);
        }
        return Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
    }

    public static Bitmap getBitmapFromUri(Context context, Uri imageUri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
