package br.com.fatec.downloadmanager;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        TextView nameTextView = findViewById(R.id.video_name);

        nameTextView.setText(getIntent().getStringExtra("VIDEO_NAME"));
        path = getIntent().getStringExtra("VIDEO_PATH");
        setVideo();
    }

    private void setVideo() {
        final MediaController mediaController = new MediaController(this);
        videoView.setVideoURI(Uri.parse(path));
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaController.setAnchorView(videoView);
                        videoView.requestFocus();
                        videoView.start();
                    }
                });
    }
}