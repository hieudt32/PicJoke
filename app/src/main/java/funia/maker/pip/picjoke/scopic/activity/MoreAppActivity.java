package funia.maker.pip.picjoke.scopic.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.adapter.MoreAppAdapter;
import funia.maker.pip.picjoke.scopic.model.MoreApp;
import funia.maker.pip.picjoke.scopic.service.Service;

public class MoreAppActivity extends BaseActivity {

    private ImageButton imb_Back;
    private ListView lv_More_App;
    private ArrayList<MoreApp> arr;
    private MoreAppAdapter appAdapter;
    private String strLink;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_more_app;
    }

    @Override
    protected void initVariables() {
        imb_Back = (ImageButton) findViewById(R.id.imb_back_more);
        lv_More_App = (ListView) findViewById(R.id.lv_more_app);
        imb_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lv_More_App.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String appPackageName = arr.get(position).getStrPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }

    @Override
    protected void initData() {
        strLink = "http://ads.liforte.com/api/ads/v1/GetAllActiveForMoreApps";
        arr = new ArrayList<>();
        new loadMoreApp().execute(strLink);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class loadMoreApp extends AsyncTask<String, Void, Void> {
        private Service service = new Service();

        @Override
        protected Void doInBackground(String... params) {
            String strTemp = service.readJsonFromUrl(params[0]);
            if (strTemp != null) {
                try {
                    JSONArray jsonArray = new JSONArray(strTemp);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        MoreApp app = new MoreApp();
                        if (object.has("Icon")) {
                            app.setStrThumb((String) object.get("Icon"));
                        }
                        if (object.has("Name")) {
                            app.setStrName((String) object.get("Name"));
                        }
                        if (object.has("Description")) {
                            app.setStrDescription((String) object.get("Description"));
                        }
                        if (object.has("Package")) {
                            app.setStrPackageName((String) object.get("Package"));
                        }
                        arr.add(app);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            appAdapter = new MoreAppAdapter(arr, MoreAppActivity.this);
            lv_More_App.setAdapter(appAdapter);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
