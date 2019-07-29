package br.com.fatec.downloadmanager;

import org.litepal.crud.DataSupport;

public class VideoReference extends DataSupport {

    private long id;
    private int videoRequestId;
    private String name;
    private int progress;
    private String link;
    private String path;

    public VideoReference() { }

    public VideoReference(int videoRequestId, String name, int progress, String link, String path) {
        this.videoRequestId = videoRequestId;
        this.name = name;
        this.progress = progress;
        this.link = link;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVideoRequestId() {
        return videoRequestId;
    }

    public void setVideoRequestId(int videoRequestId) {
        this.videoRequestId = videoRequestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "VideoReference{" +
                "id=" + id +
                ", videoRequestId=" + videoRequestId +
                ", name='" + name + '\'' +
                ", progress=" + progress +
                ", link='" + link + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
