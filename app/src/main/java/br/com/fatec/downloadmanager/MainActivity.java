package br.com.fatec.downloadmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DownloadListener, VideoListener {

    private List<VideoReference> videoList;
    private EditText editText;
    private EditText nameText;
    private VideoAdapter adapter;
    private VideoDownloadManager manager;
    private VideoDAO videoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoList = new ArrayList<>();
        editText = findViewById(R.id.editText);
        nameText = findViewById(R.id.nameEditText);
        Button button = findViewById(R.id.button);
        LitePal.initialize(this);

        videoDAO = new VideoDAO();
        manager = new VideoDownloadManager(this, this);

        adapter = new VideoAdapter(videoList, this, this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameText.getText().toString().trim().isEmpty() && !editText.getText().toString().trim().isEmpty()) {
                    if (editText.getText().toString().contains(".mp4") || editText.getText().toString().contains(".3gp") || editText.getText().toString().contains(".webm")){
                        String destination = VideoDownloadManager.getPath(MainActivity.this) + nameText.getText().toString().trim();
                        int downloadId = manager.downloadVideo(editText.getText().toString().trim(), destination).getDownloadId();
                        VideoReference video = new VideoReference(downloadId, nameText.getText().toString().trim(),
                                0, editText.getText().toString().trim(), destination);
                        videoList.add(video);
                        adapter.setData(videoList);
                    } else
                        Toast.makeText(MainActivity.this, "Arquivo não suportado...", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MainActivity.this, "Preencha todos os campos...", Toast.LENGTH_SHORT).show();
            }
        });

        videoDAO.getVideos(new VideoDBCallback() {
            @Override
            public void onDataLoaded(List<VideoReference> list) {
                videoList = list;
                for (VideoReference reference : list) {
                    // Verifica se realmente existe na pasta
                    if (reference.getProgress() < 100) {
                        int downloadId = manager.downloadVideo(reference.getLink(),
                                VideoDownloadManager.getPath(MainActivity.this) +
                                        reference.getName()).getDownloadId();
                        reference.setVideoRequestId(downloadId);
                    } else reference.setVideoRequestId(-1);
                }
                adapter.setData(videoList);
            }
        });
    }

    @Override
    public void onDownloadProgress(int downloadId, int progress) {
        // Procura a referencia de video e atualiza o progresso
        int i = 0;
        for (VideoReference video : videoList) {
            if (video.getVideoRequestId() == downloadId && progress != videoList.get(i).getProgress()) {
                adapter.setProgress(i, progress);
                video.setProgress(progress);
                videoDAO.saveVideo(downloadId, video.getName(), progress, video.getLink(), video.getPath());
                break;
            }
            i++;
        }
    }

    @Override
    public void onDownloadCompleted(int downloadId) {
        // Altera status como finalizado
        for (VideoReference video : videoList)
            if (video.getVideoRequestId() == downloadId) {
                Toast.makeText(this, video.getName() + " finalizado", Toast.LENGTH_SHORT).show();
                break;
            }
    }

    @Override
    public void onDownloadFailed(String error, int downloadId) {
        // Remove video que ocorreu erro
        Log.e("ERROR_DOWNLOAD_FILE", error);
        Toast.makeText(this, "Ocorreu um erro ao fazer download", Toast.LENGTH_SHORT).show();
        int i = 0;
        for (VideoReference video : videoList) {
            if (video.getVideoRequestId() == downloadId) {
                File file = new File(VideoDownloadManager.getPath(this) + videoList.get(i).getName());
                file.delete();
                videoList.remove(video);
                videoDAO.deleteVideo(video);
                adapter.notifyItemRemoved(i);
                break;
            }
            i++;
        }
    }

    @Override
    public void onVideoDelete(final int position) {
        final File file = new File(VideoDownloadManager.getPath(this) + videoList.get(position).getName());
        if (videoList.get(position).getProgress() < 100) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Excluir")
                    .setMessage("Download em progresso,deseja cancelar e excluir?")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            manager.getDownloadManager().cancel(videoList.get(position).getVideoRequestId());
                            file.delete();
                            videoDAO.deleteVideo(videoList.get(position));
                            videoList.remove(position);
                            adapter.notifyItemRemoved(position);
                        }
                    });
            builder.create().show();
        } else {
            file.delete();
            videoDAO.deleteVideo(videoList.get(position));
            videoList.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void onVideoPlay(int position) {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        intent.putExtra("VIDEO_NAME", videoList.get(position).getName());
        intent.putExtra("VIDEO_PATH", "file://" +
                VideoDownloadManager.getPath(this) +
                videoList.get(position).getName());
        startActivity(intent);
    }
}