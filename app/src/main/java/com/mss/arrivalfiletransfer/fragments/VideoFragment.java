package com.mss.arrivalfiletransfer.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mss.arrivalfiletransfer.Utils.Constant;
import com.mss.arrivalfiletransfer.Utils.DividerGridItemDecoration;
import com.mss.arrivalfiletransfer.R;
import com.mss.arrivalfiletransfer.Utils.Session;
import com.mss.arrivalfiletransfer.activity.AllFilesPicker;
import com.mss.arrivalfiletransfer.adapter.VideoPickAdapter;
import com.mss.arrivalfiletransfer.filter.FileFilter;
import com.mss.arrivalfiletransfer.filter.callback.FilterResultCallback;
import com.mss.arrivalfiletransfer.filter.entity.Directory;
import com.mss.arrivalfiletransfer.filter.entity.ImageFile;
import com.mss.arrivalfiletransfer.filter.entity.VideoFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ${GC} on 21/6/17.
 */

public class VideoFragment extends Fragment {

    View view;
    @Bind(R.id.rv_video_pick)
    RecyclerView rvVideoPick;
    @Bind(R.id.pb_video_pick)
    ProgressBar pbVideoPick;
    ////////////////////////
    public static final String THUMBNAIL_PATH = "FilePick";
    public static final String IS_NEED_CAMERA = "IsNeedCamera";
    public static final int DEFAULT_MAX_NUMBER = 9;
    public static final int COLUMN_NUMBER = 3;
    private int mMaxNumber;
    private int mCurrentNumber = 0;
    private Toolbar mTbImagePick;
    private VideoPickAdapter mAdapter;
    private boolean isNeedCamera;
    private ArrayList<VideoFile> mSelectedList = new ArrayList<>();
    ///////////////////////////
    Toolbar toolbar;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        showProgressBar();
        initUI();
        return view;


    }

    private void initUI() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                loadData();
                mProgressDialog.dismiss();
            }
        }, 1000);

        toolbar = ((AllFilesPicker) getActivity()).mtoolbar;
        setHasOptionsMenu(true);
        mMaxNumber = 9;
        isNeedCamera = true;

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), COLUMN_NUMBER);
        rvVideoPick.setLayoutManager(layoutManager);
        rvVideoPick.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        mAdapter = ((AllFilesPicker) getActivity()).mVideoAdapter;
        rvVideoPick.setAdapter(mAdapter);

       /* mAdapter.setOnSelectStateListener(new OnSelectStateListener<VideoFile>() {
            @Override
            public void OnSelectStateChanged(boolean state, VideoFile file) {
                if (state) {
                    mSelectedList.add(file);
                    mCurrentNumber++;
                } else {
                    mSelectedList.remove(file);
                    mCurrentNumber--;
                }
                mTbImagePick.setTitle(mCurrentNumber + "/" + mMaxNumber);
            }
        });*/

        // pbVideoPick = (ProgressBar) getActivity().findViewById(R.id.pb_video_pick);
        File folder = new File(getActivity().getExternalCacheDir().getAbsolutePath() + File.separator + THUMBNAIL_PATH);
        if (!folder.exists()) {
          //  pbVideoPick.setVisibility(View.VISIBLE);
        } else {
          //  pbVideoPick.setVisibility(View.GONE);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_TAKE_VIDEO:
                if (resultCode == RESULT_OK) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File file = new File(mAdapter.mVideoPath);
                    Uri contentUri = Uri.fromFile(file);
                    mediaScanIntent.setData(contentUri);
                    getActivity().sendBroadcast(mediaScanIntent);

                    loadData();
                }
                break;
        }
    }

    private void loadData() {
        FileFilter.getVideos(getActivity(), new FilterResultCallback<VideoFile>() {
            @Override
            public void onResult(List<Directory<VideoFile>> directories) {
               // pbVideoPick.setVisibility(View.GONE);
                List<VideoFile> list = new ArrayList<>();
                for (Directory<VideoFile> directory : directories) {
                    list.addAll(directory.getFiles());
                }

                for (VideoFile file : mSelectedList) {
                    int index = list.indexOf(file);
                    if (index != -1) {
                        list.get(index).setSelected(true);
                    }
                }
                for (VideoFile file : mAdapter.getDataSet()) {
                    int index = list.indexOf(file);
                    if (file.isSelected()) {
                        list.get(index).setSelected(true);
                    } else {
                        file.setSelected(false);
                    }
                }

                mAdapter.refresh(list);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_image_pick, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
           /* Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO, mSelectedList);
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();*/
            Session.getUpdatedSelectedDataList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressBar() {

        try {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.show();
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.custom_progressdialog);
            TextView mProgTitle = (TextView) mProgressDialog.findViewById(R.id.tv_progressbar_loading);
            ProgressBar p = (ProgressBar) mProgressDialog.findViewById(R.id.progressBar);
            p.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), android.graphics.PorterDuff.Mode.SRC_ATOP);
            mProgTitle.setText("Loading...");
            mProgressDialog.setCancelable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
