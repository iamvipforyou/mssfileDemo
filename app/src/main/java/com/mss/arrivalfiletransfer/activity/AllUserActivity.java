package com.mss.arrivalfiletransfer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mss.arrivalfiletransfer.R;
import com.mss.arrivalfiletransfer.chipsViewHelper.ChipsInput;
import com.mss.arrivalfiletransfer.chipsViewHelper.model.ChipInterface;
import com.mss.arrivalfiletransfer.models.AllUserModel;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ${GC} on 16/6/17.
 */

public class AllUserActivity extends AppCompatActivity {

    @Bind(R.id.chips_input)
    ChipsInput chipsInput;
    URI uri;
    List<AllUserModel> allUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        allUser = new ArrayList<AllUserModel>();
        getAllUser();
        chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {

            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {

            }

            @Override
            public void onTextChanged(CharSequence text) {


            }
        });


    }

    private void getAllUser() {

        uri = null;
        AllUserModel dd = new AllUserModel("1", null, "Sunder", "");
        allUser.add(dd);
        AllUserModel dd2 = new AllUserModel("2", null, "Kul", "");
        allUser.add(dd2);
        AllUserModel dd3 = new AllUserModel("3", null, "Raman", "");
        allUser.add(dd3);
        AllUserModel dd4 = new AllUserModel("4", null, "Evan", "");
        allUser.add(dd4);
        AllUserModel dd5 = new AllUserModel("5", null, "Gaurav", "");
        allUser.add(dd5);
        AllUserModel dd6 = new AllUserModel("6", null, "Deepak", "");
        allUser.add(dd6);
        AllUserModel dd7 = new AllUserModel("7", null, "Pinku", "");
        allUser.add(dd7);
        AllUserModel dd8 = new AllUserModel("8", null, "Man", "");
        allUser.add(dd8);


        chipsInput.setFilterableList(allUser);

    }
}
