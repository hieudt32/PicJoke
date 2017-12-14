package funia.maker.pip.picjoke.scopic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.model.Image;
import funia.maker.pip.picjoke.scopic.utility.Measure;


public class PicJokeAdapter extends RecyclerView.Adapter<PicJokeAdapter.PicJokeHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Image> mListImage;

    public PicJokeAdapter(Context context, ArrayList<Image> arrImage) {
        this.mContext = context;
        this.mListImage = arrImage;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public PicJokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_pic_joke, parent, false);
        return new PicJokeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PicJokeHolder holder, int position) {
        int width = (mContext.getResources().getDisplayMetrics().widthPixels - 3 * Measure.pxToDp(3, mContext)) / 2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        holder.itemView.setLayoutParams(params);
        Image image = mListImage.get(position);
        Glide.with(mContext).load(image.getThumbnail()).into(holder.getImgPicJoke());
    }

    @Override
    public int getItemCount() {
        return mListImage != null ? mListImage.size() : 0;
    }

    static class PicJokeHolder extends RecyclerView.ViewHolder {

        private ImageView imgPicJoke;


        public ImageView getImgPicJoke() {
            return imgPicJoke;
        }

        public void setImgPicJoke(ImageView imgPicJoke) {
            this.imgPicJoke = imgPicJoke;
        }

        public PicJokeHolder(View itemView) {
            super(itemView);
            imgPicJoke = (ImageView) itemView.findViewById(R.id.imgPicJoke);
        }
    }
}
