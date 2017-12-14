package funia.maker.pip.picjoke.scopic.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import funia.maker.pip.picjoke.scopic.impls.OnDownloadFrameListener;


public class DownloadFrameTask extends AsyncTask<String, Integer, Void> {
    private static final String TAG="DownloadFrameTask: ";
    private Context mContext;
    private ProgressDialog progressBar;
    private String framePath;
    private OnDownloadFrameListener downloadFrameListener;
    private boolean isCancel = false;

    public void setOnDownloadFrameListener(OnDownloadFrameListener complete) {
        this.downloadFrameListener = complete;
    }

    private ProgressDialog createProgressBar(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCancelled();
            }
        });
        return progressDialog;
    }

    public DownloadFrameTask(Context mContext, String framePath) {
        this.mContext = mContext;
        this.framePath = framePath;
    }

    @Override
    protected void onPreExecute() {
        progressBar = createProgressBar(mContext);
        progressBar.show();
        super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        isCancel = true;
    }

    @Override
    protected Void doInBackground(String... param) {
        int count;
        try {
            URL url = new URL(param[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            int lengthOfFile = connection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(framePath);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress((int) ((total * 100) / lengthOfFile));
                // writing data to file
                output.write(data, 0, count);
                if (isCancel) {
                    Log.d(TAG, "Cancel down load");
                    output.close();
                    input.close();
                    File file = new File(framePath);
                    if (file.exists()) file.delete();
                    break;
                }
            }
            // flushing output
            output.flush();
            // closing streams
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(progressBar.isShowing()){
            progressBar.dismiss();
        }
        if (downloadFrameListener != null) {
            if (isCancel) {
                downloadFrameListener.onDownloadComplete(false);
            } else {
                downloadFrameListener.onDownloadComplete(true);
            }
        }
    }
}
