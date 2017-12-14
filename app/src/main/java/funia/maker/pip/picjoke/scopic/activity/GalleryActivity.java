package funia.maker.pip.picjoke.scopic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;

import funia.maker.pip.picjoke.scopic.R;
import funia.maker.pip.picjoke.scopic.adapter.GalleryAdapter;
import funia.maker.pip.picjoke.scopic.impls.OnItemRecyclerViewClickListener;
import funia.maker.pip.picjoke.scopic.utility.GridSpacingItemDecoration;
import funia.maker.pip.picjoke.scopic.utility.Measure;
import funia.maker.pip.picjoke.scopic.utility.OnItemRecyclerViewTouchListener;

public class GalleryActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<String> listFiles;
    private RecyclerView rvGallery;
    private GalleryAdapter mAdapter;
    private File mFile;
    private ImageButton imb_Back;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void initVariables() {
        rvGallery = (RecyclerView) findViewById(R.id.rvGallery);
        imb_Back = (ImageButton) findViewById(R.id.imb_back_gallery);

    }

    @Override
    protected void initData() {

        imb_Back.setOnClickListener(this);

        getListFiles();

        mAdapter = new GalleryAdapter(this, listFiles);
        rvGallery.setAdapter(mAdapter);
        rvGallery.addItemDecoration(new GridSpacingItemDecoration(3, Measure.pxToDp(3, getApplicationContext()), true));
        rvGallery.setLayoutManager(new GridLayoutManager(this, 3));

        rvGallery.addOnItemTouchListener(new OnItemRecyclerViewTouchListener(this, new OnItemRecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(GalleryActivity.this, ShareActivity.class);
                intent.putExtra("URI_IMAGE", "file://" + listFiles.get(position));
                intent.putExtra("parent_activity", "1");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }));
        mAdapter.notifyDataSetChanged();
    }

    private void getListFiles() {

        listFiles = new ArrayList<>();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mFile = getDir("PicJoke", Context.MODE_PRIVATE);
            if (!mFile.exists()) {
                mFile.mkdirs();
            }

        } else {
            mFile = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "PicJoke");
            mFile.mkdirs();
        }

        if (mFile.isDirectory() && mFile != null) {
            File[] listFile = mFile.listFiles();
            if (listFile != null) {
                for (File aListFile : listFile) {
                    listFiles.add(aListFile.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
