package funia.maker.pip.picjoke.scopic.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.model.MoreApp;


public class MoreAppAdapter extends BaseAdapter {
    ArrayList<MoreApp> arr;
    Context mContext;


    public MoreAppAdapter(ArrayList<MoreApp> arr, Context mContext) {
        this.arr = arr;
        this.mContext = mContext;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        if (!arr.isEmpty()) {
            return arr.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (!arr.isEmpty()) {
            return arr.size();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        MoreApp app = arr.get(position);
        ViewHolder holder;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_more_app, null);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 325);
            rowView.setLayoutParams(param);
            holder = new ViewHolder(rowView);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.tv_NameApp.setText(app.getStrName());
        holder.tv_Description.setText(app.getStrDescription());
        Glide.with(mContext).load(app.getStrThumb()).into(holder.imv_Logo_App);
        return rowView;
    }

    private class ViewHolder {
        ImageView imv_Logo_App;
        TextView tv_NameApp;
        TextView tv_Description;

        public ViewHolder(View view) {
            imv_Logo_App = (ImageView) view.findViewById(R.id.imv_logo_app);
            tv_NameApp = (TextView) view.findViewById(R.id.tv_name_app);
            tv_Description = (TextView) view.findViewById(R.id.tv_description);
        }
    }
}
