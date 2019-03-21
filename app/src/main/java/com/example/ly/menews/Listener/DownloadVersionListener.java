package com.example.ly.menews.Listener;

public interface DownloadVersionListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}
