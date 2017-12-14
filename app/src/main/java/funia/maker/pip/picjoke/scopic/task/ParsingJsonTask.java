package funia.maker.pip.picjoke.scopic.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import funia.maker.pip.picjoke.scopic.activity.MainActivity;
import funia.maker.pip.picjoke.scopic.impls.OnFinishAsyncTaskListener;
import funia.maker.pip.picjoke.scopic.impls.OnParsingJsonListener;
import funia.maker.pip.picjoke.scopic.model.Category;
import funia.maker.pip.picjoke.scopic.model.Image;
import funia.maker.pip.picjoke.scopic.model.Rect;
import funia.maker.pip.picjoke.scopic.service.Service;


public class ParsingJsonTask extends AsyncTask<String, Void, Void> {

    private ArrayList<Image> listImage = new ArrayList<>();
    private OnFinishAsyncTaskListener mOnFinishAsyncTask;
    private OnParsingJsonListener mOnParsingJsonListener;
    private Service service = new Service();

    //SonTH added
    private Activity mDelegate;
    private int mFlag;
    private String mStrCatId;
    private ArrayList<Category> listCategory = new ArrayList<>();

    public void setOnFinishAsyncTask(OnFinishAsyncTaskListener finish) {
        this.mOnFinishAsyncTask = finish;
    }

    public void setOnParsingJsonListener(OnParsingJsonListener parsed) {
        this.mOnParsingJsonListener = parsed;
    }

    public ParsingJsonTask(Activity delegate) {
        mDelegate = delegate; //SonTH added
    }

    //SonTH added
    public void setFlag(int flag) {
        mFlag = flag;
    }

    public int getFlag() {
        return mFlag;
    }

    public void setCategoryId(String catId) {
        mStrCatId = catId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

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

    @Override
    protected Void doInBackground(String... params) {
        String strTemp = service.readJsonFromUrl(params[0]);
        String encrypt = "";
        String decrypt = "";

        if (mFlag == 0) {
            File fileCategory = new File(mDelegate.getFilesDir(), "category.txt");
            if (strTemp != null) {
                encrypt = MainActivity.encryptIt(strTemp, MainActivity.getCrypto(MainActivity.getPart0(),
                        MainActivity.getPart1(), MainActivity.getPart2(), MainActivity.getPart3(), MainActivity.getPart4()));
                try {
                    fileCategory.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(fileCategory);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(encrypt);
                    myOutWriter.close();
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            } else {
                FileInputStream fin = null;
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
                }
            }

            if (strTemp.length() > 0) {
                try {
                    JSONObject rootObject = new JSONObject(strTemp);
                    JSONArray arrCategory = rootObject.getJSONArray("content");
                    for (int i = 0; i < arrCategory.length(); i++) {
                        JSONObject category = arrCategory.getJSONObject(i);
                        String id = category.getString("ID");
                        String name = category.getString("Name");
                        boolean lock = category.getBoolean("Lock");
                        listCategory.add(new Category(id, name, lock));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {


            String fileName = "cat_" + mStrCatId + ".txt";
            File fileItem = new File(mDelegate.getFilesDir(), fileName);
            if (strTemp != null) {
                encrypt = MainActivity.encryptIt(strTemp, MainActivity.getCrypto(MainActivity.getPart0(),
                        MainActivity.getPart1(), MainActivity.getPart2(), MainActivity.getPart3(), MainActivity.getPart4()));
                try {
                    fileItem.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(fileItem);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(encrypt);
                    myOutWriter.close();
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            } else {
                FileInputStream fin = null;
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
                }
            }
            if (strTemp.length() > 0) {
                try {
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

                        listImage.add(new Image(id, name, url, thumbnail, width, height, myRect, rotate, aspectRatioX, aspectRatioY, textureName, saturation));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mOnParsingJsonListener != null) {
            if (mFlag == 0) {
                mOnParsingJsonListener.onCompleteParsingJson(this, listCategory);
            } else {
                mOnParsingJsonListener.onCompleteParsingJson(this, listImage);
            }
        }
        if (mOnFinishAsyncTask != null) {
            mOnFinishAsyncTask.onFinishAsyncTask(null);
        }
    }
}
