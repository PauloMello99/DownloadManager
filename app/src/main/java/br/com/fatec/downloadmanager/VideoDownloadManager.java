package br.com.fatec.downloadmanager;

import android.content.Context;
import android.net.Uri;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

public class VideoDownloadManager {

    private DownloadListener listener;
    private ThinDownloadManager downloadManager;
    private Context context;

    public VideoDownloadManager(Context context, DownloadListener listener) {
        downloadManager = new ThinDownloadManager();
        this.listener = listener;
        this.context = context;
    }

    public static String getPath(Context context){
        return context.getFilesDir().getAbsolutePath() + File.separator + ".pecege-videos/";
    }

    public DownloadRequest downloadVideo(String link, String destination){
        DownloadRequest request = new DownloadRequest(Uri.parse(link))
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(Uri.parse(destination))
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadResumable(true)
                .setStatusListener(new DownloadStatusListenerV1() {
                    @Override
                    public void onDownloadComplete(DownloadRequest downloadRequest) {
                        listener.onDownloadCompleted(downloadRequest.getDownloadId());
                    }

                    @Override
                    public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                        listener.onDownloadFailed(errorMessage,downloadRequest.getDownloadId());
                    }

                    @Override
                    public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                        if(downloadedBytes==0 && totalBytes>Utils.getAvailableSpace(true,context)){
                            downloadManager.cancel(downloadRequest.getDownloadId());
                            listener.onDownloadFailed("Espaço insuficiente na memória",downloadRequest.getDownloadId());
                        }
                        else
                            listener.onDownloadProgress(downloadRequest.getDownloadId(),
                                    (int)((100*downloadedBytes)/totalBytes));
                    }
                });
        downloadManager.add(request);
        return request;
    }

    public ThinDownloadManager getDownloadManager(){
        return downloadManager;
    }
}
