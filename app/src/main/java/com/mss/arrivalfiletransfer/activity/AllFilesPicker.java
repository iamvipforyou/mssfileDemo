package com.mss.arrivalfiletransfer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mss.arrivalfiletransfer.Interface.AfterBrowseAddToSelectedList;
import com.mss.arrivalfiletransfer.Interface.updateSelectedDataList;
import com.mss.arrivalfiletransfer.Utils.Constant;
import com.mss.arrivalfiletransfer.R;
import com.mss.arrivalfiletransfer.Utils.Session;
import com.mss.arrivalfiletransfer.Utils.Util;
import com.mss.arrivalfiletransfer.adapter.AudioPickAdapter;
import com.mss.arrivalfiletransfer.adapter.ImagePickAdapter;
import com.mss.arrivalfiletransfer.adapter.NormalFilePickAdapter;
import com.mss.arrivalfiletransfer.adapter.OnSelectStateListener;
import com.mss.arrivalfiletransfer.adapter.VideoPickAdapter;
import com.mss.arrivalfiletransfer.filter.entity.AudioFile;
import com.mss.arrivalfiletransfer.filter.entity.ImageFile;
import com.mss.arrivalfiletransfer.filter.entity.NormalFile;
import com.mss.arrivalfiletransfer.filter.entity.VideoFile;
import com.mss.arrivalfiletransfer.fragments.AudioPickFragment;
import com.mss.arrivalfiletransfer.fragments.DocumentFragment;
import com.mss.arrivalfiletransfer.fragments.PicturesFragment;
import com.mss.arrivalfiletransfer.fragments.VideoFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${GC} on 19/6/17.
 */

public class AllFilesPicker extends AppCompatActivity implements updateSelectedDataList, AfterBrowseAddToSelectedList {

    public Toolbar mtoolbar;
    public static final String IS_NEED_CAMERA = "IsNeedCamera";
    public ArrayList<ImageFile> mSelectedList = new ArrayList<>();
    private ArrayList<VideoFile> mSelectedVideoList = new ArrayList<>();
    private ArrayList<AudioFile> mSelectedAudioList = new ArrayList<>();
    public NormalFilePickAdapter mFilePickAdapter;
    private ArrayList<NormalFile> mSelectedDocumentList = new ArrayList<>();
    public ImagePickAdapter mAdapter;
    public VideoPickAdapter mVideoAdapter;
    public AudioPickAdapter mAudioAdapter;
    private boolean isNeedCamera;
    private int mMaxNumber = 9;
    private int mCurrentNumber = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_files_picker);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        Session.setUpdatedSelectedDataList(this);
        Session.setAfterBrowseSelectedList(this);
        initAdapters();


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setUpViewPager(viewPager);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initAdapters() {
        //////// Image Adapters//////////////////
        mAdapter = new ImagePickAdapter(this, isNeedCamera, mMaxNumber);
        mAdapter.setOnSelectStateListener(new OnSelectStateListener<ImageFile>() {
            @Override
            public void OnSelectStateChanged(boolean state, ImageFile file) {
                if (state) {
                    mSelectedList.add(file);
                    mCurrentNumber++;
                } else {
                    mSelectedList.remove(file);
                    mCurrentNumber--;
                }
                Util.setClassTitle(AllFilesPicker.this, mCurrentNumber + "", mtoolbar);
                // mTbImagePick.setTitle();
            }
        });
        /////////////////////////////////////////////////////////////
        ///////////////////Video Adapter ///////////////////////
        mVideoAdapter = new VideoPickAdapter(this, isNeedCamera, mMaxNumber);
        mVideoAdapter.setOnSelectStateListener(new OnSelectStateListener<VideoFile>() {
            @Override
            public void OnSelectStateChanged(boolean state, VideoFile vfile) {
                if (state) {
                    mSelectedVideoList.add(vfile);
                    mCurrentNumber++;
                } else {
                    mSelectedVideoList.remove(vfile);
                    mCurrentNumber--;
                }
                Util.setClassTitle(AllFilesPicker.this, mCurrentNumber + "", mtoolbar);
            }
        });
        /////////////////////Audio Adapter ///////////////////////////
        mAudioAdapter = new AudioPickAdapter(this, mMaxNumber);
        mAudioAdapter.setOnSelectStateListener(new OnSelectStateListener<AudioFile>() {
            @Override
            public void OnSelectStateChanged(boolean state, AudioFile file) {
                if (state) {
                    mSelectedAudioList.add(file);
                    mCurrentNumber++;
                } else {
                    mSelectedAudioList.remove(file);
                    mCurrentNumber--;
                }

                Util.setClassTitle(AllFilesPicker.this, mCurrentNumber + "", mtoolbar);
            }


        });
        /////////////////////////Document Adapter ///////////////////
        mFilePickAdapter = new NormalFilePickAdapter(this, mMaxNumber);

        mFilePickAdapter.setOnSelectStateListener(new OnSelectStateListener<NormalFile>() {
            @Override
            public void OnSelectStateChanged(boolean state, NormalFile file) {
                if (state) {
                    mSelectedDocumentList.add(file);
                    mCurrentNumber++;
                } else {
                    mSelectedDocumentList.remove(file);
                    mCurrentNumber--;
                }
                Util.setClassTitle(AllFilesPicker.this, mCurrentNumber + "", mtoolbar);
            }
        });


    }

    private void setUpViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new PicturesFragment(), "Images");
        adapter.addFragment(new VideoFragment(), "Video");
        adapter.addFragment(new AudioPickFragment(), "Audio");
        adapter.addFragment(new DocumentFragment(), "Document");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateAllList() {

        int size = mSelectedAudioList.size() + mSelectedDocumentList.size() + mSelectedVideoList.size() + mSelectedList.size();

        if (size > 0) {
            ArrayList<Uri> allList = new ArrayList<Uri>();
            for (int images = 0; images < mSelectedList.size(); images++) {
                ImageFile imageFile = mSelectedList.get(images);
                if (imageFile.isSelected()) {
                    allList.add(Uri.fromFile(new File(imageFile.getPath())));
                }
            }

            for (int videos = 0; videos < mSelectedVideoList.size(); videos++) {
                VideoFile videoFile = mSelectedVideoList.get(videos);
                if (videoFile.isSelected()) {
                    allList.add(Uri.fromFile(new File(videoFile.getPath())));
                }
            }
            for (int audios = 0; audios < mSelectedAudioList.size(); audios++) {
                AudioFile audioFile = mSelectedAudioList.get(audios);
                if (audioFile.isSelected()) {
                    allList.add(Uri.fromFile(new File(audioFile.getPath())));
                }
            }
            for (int documents = 0; documents < mSelectedDocumentList.size(); documents++) {
                NormalFile normalFile = mSelectedDocumentList.get(documents);
                if (normalFile.isSelected()) {
                    allList.add(Uri.fromFile(new File(normalFile.getPath())));
                }
            }

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, allList);
            shareIntent.setType("*/*");
            startActivity(Intent.createChooser(shareIntent, "Share images to.."));

        }else{

            Toast.makeText(this, "No File Selected !!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void addToSelectedList(ArrayList<ImageFile> list) {

        for (int i = 0; i < list.size(); i++) {
            ImageFile file = list.get(i);
            if (file.isSelected()) {
                if (!mSelectedList.contains(file)) {
                    mSelectedList.add(file);
                    mCurrentNumber++;
                }
            } else {
                if (mSelectedList.contains(file)) {
                    mSelectedList.remove(file);
                    mCurrentNumber--;
                }
            }
        }
        Util.setClassTitle(AllFilesPicker.this, mCurrentNumber + "", mtoolbar);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    StringBuilder builder = new StringBuilder();
                    for (ImageFile file : list) {
                        String path = file.getPath();
                        builder.append(path + "\n");
                    }
                    Toast.makeText(this, "" + builder.toString(), Toast.LENGTH_SHORT).show();
                    //   txtName.setText(builder.toString());
                }
                break;
            case Constant.REQUEST_CODE_PICK_VIDEO:
                if (resultCode == RESULT_OK) {
                    ArrayList<VideoFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                    StringBuilder builder = new StringBuilder();
                    for (VideoFile file : list) {
                        String path = file.getPath();
                        builder.append(path + "\n");
                    }
                    //  txtName.setText(builder.toString());
                }
                break;
            case Constant.REQUEST_CODE_PICK_AUDIO:
                if (resultCode == RESULT_OK) {
                    ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
                    StringBuilder builder = new StringBuilder();
                    for (AudioFile file : list) {
                        String path = file.getPath();
                        builder.append(path + "\n");
                    }
                    //  txtName.setText(builder.toString());
                }
                break;
            case Constant.REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK) {
                    ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                    StringBuilder builder = new StringBuilder();
                    for (NormalFile file : list) {
                        String path = file.getPath();
                        builder.append(path + "\n");
                    }
                    //   txtName.setText(builder.toString());
                }
                break;

        }


    }

}
