package funia.maker.pip.picjoke.scopic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.utility.Measure;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private ArrayList<String> listFiles;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public GalleryAdapter(Context context, ArrayList<String> files) {

        this.mContext = context;
        this.listFiles = files;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_gallery_pic_joke, parent, false);
        return new GalleryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        int width = (mContext.getResources().getDisplayMetrics().widthPixels - 3 * Measure.pxToDp(3, mContext)) / 3;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        holder.itemView.setLayoutParams(params);
        Glide.with(mContext).load(listFiles.get(position)).into(holder.getImgGallery());
    }

    @Override
    public int getItemCount() {
        return (listFiles != null) ? listFiles.size() : 0;
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgGallery;

        public ImageView getImgGallery() {
            return imgGallery;
        }

        public GalleryViewHolder(View itemView) {
            super(itemView);
            imgGallery = (ImageView) itemView.findViewById(R.id.imgGallery);
        }
    }
}
