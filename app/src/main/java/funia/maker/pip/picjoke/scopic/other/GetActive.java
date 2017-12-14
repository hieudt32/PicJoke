package funia.maker.pip.picjoke.scopic.other;

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
import java.util.ArrayList;

import funia.maker.pip.picjoke.scopic.activity.MainActivity;
import funia.maker.pip.picjoke.scopic.model.Category;

/**
 * Created by baotu on 6/28/2017.
 */

public class GetActive {
    private static ArrayList<Category> mListCategory = new ArrayList<>();

    public static boolean getActiveForSku(File fileDir) {
        if (Device.getInstance().checkActiveForSku(Defines.SKU_UNLOCK_ALL)) {
            return true;
        } else {
            getCategory(fileDir);
            for (int i = 0; i < mListCategory.size(); i++) {
                if (mListCategory.get(i).getLock()) {
                    if (Device.getInstance().checkActiveForSku(Defines.SKU_UNLOCK_CAT + "_" + mListCategory.get(i).getId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void getCategory(File fileDir) {
        File fileCategory = new File(fileDir, "category.txt");
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
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
}
