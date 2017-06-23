package com.mss.arrivalfiletransfer.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.mss.arrivalfiletransfer.Utils.DividerListItemDecoration;
import com.mss.arrivalfiletransfer.R;
import com.mss.arrivalfiletransfer.Utils.Session;
import com.mss.arrivalfiletransfer.activity.AllFilesPicker;
import com.mss.arrivalfiletransfer.adapter.NormalFilePickAdapter;
import com.mss.arrivalfiletransfer.filter.FileFilter;
import com.mss.arrivalfiletransfer.filter.callback.FilterResultCallback;
import com.mss.arrivalfiletransfer.filter.entity.AudioFile;
import com.mss.arrivalfiletransfer.filter.entity.Directory;
import com.mss.arrivalfiletransfer.filter.entity.NormalFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ${GC} on 21/6/17.
 */

public class DocumentFragment extends Fragment {

    View view;
    @Bind(R.id.rv_file_pick)
    RecyclerView rvFilePick;
    @Bind(R.id.pb_file_pick)
    ProgressBar pbFilePick;
    ///////////////////////////////////////
    public static final int DEFAULT_MAX_NUMBER = 9;
    public static final String SUFFIX = "Suffix";
    private int mMaxNumber;
    private int mCurrentNumber = 0;
    private Toolbar mTbImagePick;
    private NormalFilePickAdapter mAdapter;
    private ArrayList<NormalFile> mSelectedList = new ArrayList<>();
    private String[] mSuffix;
    private Toolbar toolbar;
    //////////////////////////////////
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_document, container, false);
        mMaxNumber = 9;
        mSuffix = new String[]{"xlsx", "xls", "doc", "dOcX", "ppt", ".pptx", "pdf", ".apk"};
        toolbar = ((AllFilesPicker) getActivity()).mtoolbar;
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);


        showProgressBar();
        initUI();
        return view;
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

    private void initUI() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                loadData();
            }
        }, 1000);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvFilePick.setLayoutManager(layoutManager);
        rvFilePick.addItemDecoration(new DividerListItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL, R.drawable.divider_rv_file));
        mAdapter = ((AllFilesPicker) getActivity()).mFilePickAdapter;
        rvFilePick.setAdapter(mAdapter);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void loadData() {
        FileFilter.getFiles(getActivity(), new FilterResultCallback<NormalFile>() {
            @Override
            public void onResult(List<Directory<NormalFile>> directories) {

                List<NormalFile> list = new ArrayList<>();
                for (Directory<NormalFile> directory : directories) {
                    list.addAll(directory.getFiles());
                }

                for (NormalFile file : mSelectedList) {
                    int index = list.indexOf(file);
                    if (index != -1) {
                        list.get(index).setSelected(true);
                    }
                }

                for (NormalFile file : mAdapter.getDataSet()) {
                    int index = list.indexOf(file);
                    if (file.isSelected()) {
                        list.get(index).setSelected(true);
                    } else {
                        file.setSelected(false);
                    }
                }

                mAdapter.refresh(list);
            }
        }, mSuffix);
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
            /*Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Constant.RESULT_PICK_FILE, mSelectedList);
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();*/
            Session.getUpdatedSelectedDataList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
