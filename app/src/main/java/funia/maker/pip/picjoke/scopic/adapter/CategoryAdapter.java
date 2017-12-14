package funia.maker.pip.picjoke.scopic.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.activity.FrameActivity;
import funia.maker.pip.picjoke.scopic.model.Category;
import funia.maker.pip.picjoke.scopic.other.Defines;
import funia.maker.pip.picjoke.scopic.other.Device;
import funia.maker.pip.picjoke.scopic.utility.Measure;

/**
 * Created by st on 6/6/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<Category> listFiles;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private String mStrCatPref;

    public CategoryAdapter(Context context, ArrayList<Category> files) {
        this.mContext = context;
        this.listFiles = files;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.CategoryViewHolder holder, int position) {
        final Category category = listFiles.get(position);
        mStrCatPref = PreferenceManager.getDefaultSharedPreferences(mContext).getString(FrameActivity.CAT_PREF, "1");
        if (category.getId().equals(mStrCatPref)) {
            holder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.pic_joke_65_blue));
        } else {
            holder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        }

        boolean bUnlock = false;
        if (category.getLock()) {
            if (Device.getInstance().checkActiveForSku(Defines.SKU_UNLOCK_ALL)) {
                bUnlock = true;
            } else {
                if (Device.getInstance().checkActiveForSku(Defines.SKU_UNLOCK_CAT + "_" + category.getId())) {
                    bUnlock = true;
                } else {
                    bUnlock = false;
                }
            }
        } else {
            bUnlock = true;
        }

        if (bUnlock) {
            holder.tvName.setText(category.getName());
        } else {
            holder.tvName.setText(category.getName() + "\n(~ $1.99)");
        }
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FrameActivity) mContext).getItemsWithCategory(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (listFiles != null) ? listFiles.size() : 0;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        public TextView getName() {
            return tvName;
        }

        public CategoryViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
