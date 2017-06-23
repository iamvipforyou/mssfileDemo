package com.mss.arrivalfiletransfer.models;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.mss.arrivalfiletransfer.chipsViewHelper.model.ChipInterface;

/**
 * Created by ${GC} on 16/6/17.
 */

public class AllUserModel implements ChipInterface {

    private String id;
    private Uri avatarUri;
    private String name;
    private String phoneNumber;

    public AllUserModel() {

    }

    public AllUserModel(String id, Uri avatarUri, String name, String phoneNumber) {

        this.id = id;
        this.avatarUri = avatarUri;
        this.name = name;
        this.phoneNumber = phoneNumber;

    }


    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Uri getAvatarUri() {
        return avatarUri;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String getInfo() {
        return phoneNumber;
    }


}


