package com.mss.arrivalfiletransfer.Utils;

import com.mss.arrivalfiletransfer.Interface.upDateBrowseImage;
import com.mss.arrivalfiletransfer.Interface.updateSelectedDataList;
import com.mss.arrivalfiletransfer.filter.entity.ImageFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${GC} on 22/6/17.
 */

public class Session {

    public static updateSelectedDataList mUpdateSelectedDataList;
    public static upDateBrowseImage mUpDateBrowseImage;


    public static void setUpdatedSelectedDataList(updateSelectedDataList listner) {

        if (listner != null) {

            mUpdateSelectedDataList = listner;
        }
    }


    public static void getUpdatedSelectedDataList() {

        if (mUpdateSelectedDataList != null) {

            mUpdateSelectedDataList.updateAllList();
        }

    }


    public static void setUpDateBrowseImage(upDateBrowseImage listner) {
        if (listner != null) {
            mUpDateBrowseImage = listner;

        }
    }


    public static void getUpdateBrowsedImage(ArrayList<ImageFile> list) {
        if (mUpDateBrowseImage != null) {

            mUpDateBrowseImage.selectedBrowsedImage(list);
        }
    }

}
