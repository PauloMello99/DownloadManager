package br.com.fatec.downloadmanager;

import java.util.List;

public interface VideoDBCallback {
    void onDataLoaded(List<VideoReference> list);
}
