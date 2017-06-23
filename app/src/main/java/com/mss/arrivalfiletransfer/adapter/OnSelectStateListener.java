package com.mss.arrivalfiletransfer.adapter;



public interface OnSelectStateListener<T> {
    void OnSelectStateChanged(boolean state, T file);
}
