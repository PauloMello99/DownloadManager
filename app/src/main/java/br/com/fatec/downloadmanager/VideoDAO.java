package br.com.fatec.downloadmanager;


import android.util.Log;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class VideoDAO {

    public VideoDAO() { }

    public void getVideos(VideoDBCallback callback) {
        List<VideoReference> list = DataSupport.findAll(VideoReference.class);
        callback.onDataLoaded(list);
    }

    public void getVideo(int id, VideoDBCallback callback) {
        VideoReference videoReference = DataSupport.find(VideoReference.class, id);
        List<VideoReference> list = new ArrayList<>();
        list.add(videoReference);
        callback.onDataLoaded(list);
    }

    public void saveVideo(int videoId,String videoName,int progress,String link,String path){
        VideoReference videoReference = DataSupport.where("name = ? ",videoName).findFirst(VideoReference.class);
        if(videoReference==null) videoReference = new VideoReference();
        videoReference.setName(videoName);
        videoReference.setVideoRequestId(videoId);
        videoReference.setProgress(progress);
        videoReference.setLink(link);
        videoReference.setPath(path);
        if(!videoReference.save()) Log.e("SAVE_VIDEO_DB","FALHA AO SALVAR NO BANCO DE DADOS");
    }

    public void deleteVideo(VideoReference video){
        video.delete();
        DataSupport.delete(VideoReference.class,video.getId());
    }
}