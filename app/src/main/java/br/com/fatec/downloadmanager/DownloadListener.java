package br.com.fatec.downloadmanager;

public interface DownloadListener {
    void onDownloadProgress(int downloadId,int progress);
    void onDownloadCompleted(int downloadId);
    void onDownloadFailed(String error,int downloadId);
}
