package funia.maker.pip.picjoke.scopic.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import funia.maker.pip.picjoke.scopic.impls.OnFinishAsyncTaskListener;


public class SaveImageAsyncTask extends AsyncTask<Bitmap, Void, Void> {

    private String pathImage = null;
    private Bitmap mBitmap;
    private OnFinishAsyncTaskListener mOnFinishAsyncTask;

    public void setOnFinishAsyncTask(OnFinishAsyncTaskListener finish) {
        this.mOnFinishAsyncTask = finish;
    }

    public SaveImageAsyncTask() {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Bitmap... params) {
        mBitmap = params[0];

        FileOutputStream out = null;
        String root = Environment.getExternalStorageDirectory().toString();
        File mydir = new File(root + File.separator + "PicJoke");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }
        String fileName = "PicJoke-" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        File file = new File(mydir, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            pathImage = file.getPath();
        } catch (Exception e) {
            pathImage = null;
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (mOnFinishAsyncTask != null) {
            mOnFinishAsyncTask.onFinishAsyncTask(pathImage);
        }

    }


}
