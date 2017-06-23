package com.mss.arrivalfiletransfer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mss.arrivalfiletransfer.Utils.Constant;
import com.mss.arrivalfiletransfer.R;
import com.mss.arrivalfiletransfer.filter.entity.AudioFile;
import com.mss.arrivalfiletransfer.filter.entity.ImageFile;
import com.mss.arrivalfiletransfer.filter.entity.NormalFile;
import com.mss.arrivalfiletransfer.filter.entity.VideoFile;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {


    @Bind(R.id.btn_images)
    Button btnImages;
    @Bind(R.id.btn_audio)
    Button btnAudio;
    @Bind(R.id.btn_video)
    Button btnVideo;
    @Bind(R.id.btn_file)
    Button btnFile;
    TextView txtFileName;
    @Bind(R.id.txt_name)
    TextView txtName;
    @Bind(R.id.btn_all_user)
    Button btnAllUser;
    @Bind(R.id.btn_all_files)
    Button btnAllFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }


    @OnClick({R.id.btn_images, R.id.btn_audio, R.id.btn_video, R.id.btn_file, R.id.btn_all_user,R.id.btn_all_files})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_images:


                break;
            case R.id.btn_audio:


                break;
            case R.id.btn_video:


                break;
            case R.id.btn_file:


                break;

            case R.id.btn_all_user:
                Intent allUserIntent = new Intent(MainActivity.this, AllUserActivity.class);
                startActivity(allUserIntent);

                break;
            case R.id.btn_all_files:
                Intent allFileIntent = new Intent(MainActivity.this, AllFilesPicker.class);
                startActivity(allFileIntent);
                break;
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
                    txtName.setText(builder.toString());
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
                    txtName.setText(builder.toString());
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
                    txtName.setText(builder.toString());
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
                    txtName.setText(builder.toString());
                }
                break;
        }
    }


}
