package com.mss.arrivalfiletransfer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.mss.arrivalfiletransfer.Utils.Constant;
import com.mss.arrivalfiletransfer.Utils.DividerListItemDecoration;
import com.mss.arrivalfiletransfer.R;
import com.mss.arrivalfiletransfer.Utils.Session;
import com.mss.arrivalfiletransfer.activity.AllFilesPicker;
import com.mss.arrivalfiletransfer.adapter.AudioPickAdapter;
import com.mss.arrivalfiletransfer.filter.FileFilter;
import com.mss.arrivalfiletransfer.filter.callback.FilterResultCallback;
import com.mss.arrivalfiletransfer.filter.entity.AudioFile;
import com.mss.arrivalfiletransfer.filter.entity.Directory;
import com.mss.arrivalfiletransfer.filter.entity.VideoFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ${GC} on 21/6/17.
 */

public class AudioPickFragment extends Fragment {

    View view;
    /////////////////////////////////
    public static final String IS_NEED_RECORDER = "IsNeedRecorder";
    public static final int DEFAULT_MAX_NUMBER = 9;
    @Bind(R.id.rv_audio_pick)
    RecyclerView rvAudioPick;
    private int mMaxNumber;
    private int mCurrentNumber = 0;
    private Toolbar mTbImagePick;
    private RecyclerView mRecyclerView;
    private AudioPickAdapter mAdapter;
    private boolean isNeedRecorder;
    private ArrayList<AudioFile> mSelectedList = new ArrayList<>();
    /////////////////////////////////////
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_audio, container, false);
        toolbar = ((AllFilesPicker) getActivity()).mtoolbar;
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        initUI();

        return view;


    }

    private void initUI() {
        loadData();
        mMaxNumber = 9;
        isNeedRecorder = false;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvAudioPick.setLayoutManager(layoutManager);
        rvAudioPick.addItemDecoration(new DividerListItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL, R.drawable.divider_rv_file));
        mAdapter = ((AllFilesPicker) getActivity()).mAudioAdapter;
        rvAudioPick.setAdapter(mAdapter);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void loadData() {
        FileFilter.getAudios(getActivity(), new FilterResultCallback<AudioFile>() {
            @Override
            public void onResult(List<Directory<AudioFile>> directories) {
                List<AudioFile> list = new ArrayList<>();
                for (Directory<AudioFile> directory : directories) {
                    list.addAll(directory.getFiles());
                }

                for (AudioFile file : mSelectedList) {
                    int index = list.indexOf(file);
                    if (index != -1) {
                        list.get(index).setSelected(true);
                    }
                }
                for (AudioFile file : mAdapter.getDataSet()) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_TAKE_AUDIO:
                if (resultCode == RESULT_OK) {
                    loadData();
                }
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_audio_pick, menu);

       // menu.getItem(R.id.action_record).setVisible(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
           /* Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO, mSelectedList);
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();*/
            Session.getUpdatedSelectedDataList();
            return true;
        } else if (id == R.id.action_record) {
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivityForResult(intent, Constant.REQUEST_CODE_TAKE_AUDIO);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
