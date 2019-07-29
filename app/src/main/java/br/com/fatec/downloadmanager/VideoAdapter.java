package br.com.fatec.downloadmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private VideoListener listener;
    private List<VideoViewHolder> holders;
    private List<VideoReference> videoList;
    private Context context;

    public VideoAdapter(List<VideoReference> videoList, VideoListener listener, Context context) {
        this.videoList = videoList;
        this.context = context;
        holders = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_video, viewGroup, false);
        VideoViewHolder videoViewHolder = new VideoViewHolder(view);
        holders.add(videoViewHolder);
        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoViewHolder holder, int position) {
        final VideoReference video = videoList.get(position);
        if (video.getProgress() == 100) holder.progressBar.setVisibility(View.GONE);
        else {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progressBar.setProgress(video.getProgress());
        }
        holder.nameTextView.setText(video.getName());
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteVideo(holder.getAdapterPosition());
                listener.onVideoDelete(holder.getAdapterPosition());
            }
        });
        holder.playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToVideoActivity(video);
                listener.onVideoPlay(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setData(List<VideoReference> list) {
        videoList = list;
        notifyDataSetChanged();
    }

    public void setProgress(int position, int progress) {
        if (progress >= 100)
            holders.get(position).progressBar.setVisibility(View.GONE);
        else
            holders.get(position).progressBar.setProgress(progress);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView playImageView;
        ImageView deleteImageView;
        ProgressBar progressBar;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name_video);
            playImageView = itemView.findViewById(R.id.image_play);
            deleteImageView = itemView.findViewById(R.id.image_delete);
            progressBar = itemView.findViewById(R.id.progress_video);
        }

    }

}