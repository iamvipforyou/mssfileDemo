package com.mss.arrivalfiletransfer.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.mss.arrivalfiletransfer.Interface.upDateBrowseImage;
import com.mss.arrivalfiletransfer.Utils.Constant;
import com.mss.arrivalfiletransfer.Utils.DividerGridItemDecoration;
import com.mss.arrivalfiletransfer.R;
import com.mss.arrivalfiletransfer.Utils.Session;
import com.mss.arrivalfiletransfer.activity.AllFilesPicker;
import com.mss.arrivalfiletransfer.adapter.ImagePickAdapter;
import com.mss.arrivalfiletransfer.filter.FileFilter;
import com.mss.arrivalfiletransfer.filter.callback.FilterResultCallback;
import com.mss.arrivalfiletransfer.filter.entity.Directory;
import com.mss.arrivalfiletransfer.filter.entity.ImageFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ${GC} on 19/6/17.
 */

public class PicturesFragment extends Fragment implements upDateBrowseImage {

    View view;
    @Bind(R.id.rv_image_pick)
    RecyclerView rvImagePick;
    /////
    public static final String IS_NEED_CAMERA = "IsNeedCamera";
    public static final int DEFAULT_MAX_NUMBER = 9;
    public static final int COLUMN_NUMBER = 3;
    private int mMaxNumber;
    private int mCurrentNumber = 0;
    private Toolbar mTbImagePick;
    public ImagePickAdapter mAdapter;
    private boolean isNeedCamera;
    public ArrayList<ImageFile> mSelectedList = new ArrayList<>();
    /////
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.all_picture_fragment, container, false);

        ButterKnife.bind(this, view);

        Session.setUpDateBrowseImage(this);
        toolbar = ((AllFilesPicker) getActivity()).mtoolbar;
        setHasOptionsMenu(true);
        // mMaxNumber = getActivity().getIntent().getIntExtra(Constant.MAX_NUMBER, DEFAULT_MAX_NUMBER);
        // isNeedCamera = getActivity().getIntent().getBooleanExtra(IS_NEED_CAMERA, false);
        mMaxNumber = 9;
        isNeedCamera = true;
        initUI();
        return view;
    }

    private void initUI() {

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), COLUMN_NUMBER);
        rvImagePick.setLayoutManager(layoutManager);
        rvImagePick.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        mAdapter = ((AllFilesPicker) getActivity()).mAdapter;
        rvImagePick.setAdapter(mAdapter);

        loadData();
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
            case Constant.REQUEST_CODE_TAKE_IMAGE:
                if (resultCode == RESULT_OK) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File file = new File(mAdapter.mImagePath);
                    Uri contentUri = Uri.fromFile(file);
                    mediaScanIntent.setData(contentUri);
                    getActivity().sendBroadcast(mediaScanIntent);

                    loadData();
                }
                break;
            case Constant.REQUEST_CODE_BROWSER_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_BROWSER_IMAGE);
                    mCurrentNumber = list.size();
                    mAdapter.setCurrentNumber(mCurrentNumber);
                    //  mTbImagePick.setTitle(mCurrentNumber + "/" + mMaxNumber);
                    mSelectedList.clear();
                    mSelectedList.addAll(list);

                    for (ImageFile file : mAdapter.getDataSet()) {
                        if (mSelectedList.contains(file)) {
                            file.setSelected(true);
                        } else {
                            file.setSelected(false);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void loadData() {
        FileFilter.getImages(getActivity(), new FilterResultCallback<ImageFile>() {
            @Override
            public void onResult(List<Directory<ImageFile>> directories) {
                List<ImageFile> list = new ArrayList<>();
                for (Directory<ImageFile> directory : directories) {
                    list.addAll(directory.getFiles());
                }

                for (ImageFile file : mSelectedList) {
                    int index = list.indexOf(file);
                    if (index != -1) {
                        list.get(index).setSelected(true);
                    }
                }

                for (ImageFile file : mAdapter.getDataSet()) {
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

    private void refreshSelectedList(List<ImageFile> list) {
        for (ImageFile file : list) {
            if (file.isSelected() && !mSelectedList.contains(file)) {
                mSelectedList.add(file);
            }
        }
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
            intent.putParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE, mSelectedList);
            getActivity().setResult(RESULT_OK, intent);*/
            //getActivity().finish();

            Session.getUpdatedSelectedDataList();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedBrowsedImage(ArrayList<ImageFile> listt) {

        ArrayList<ImageFile> list = listt;
        mCurrentNumber = list.size();
        mAdapter.setCurrentNumber(mCurrentNumber);
        //  mTbImagePick.setTitle(mCurrentNumber + "/" + mMaxNumber);
        mSelectedList.clear();
        mSelectedList.addAll(list);

        for (ImageFile file : mAdapter.getDataSet()) {
            if (mSelectedList.contains(file)) {
                file.setSelected(true);
            } else {
                file.setSelected(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


}
