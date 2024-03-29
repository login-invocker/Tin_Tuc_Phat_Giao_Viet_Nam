package com.invocker.phatgiaovietnam.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.invocker.phatgiaovietnam.PlayVideoYoutube;
import com.invocker.phatgiaovietnam.R;
import com.invocker.phatgiaovietnam.model.Video;

import java.util.ArrayList;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.VideoHolder>{
    private Activity activity;
    private ArrayList<Video> listVideo;

    public YoutubeAdapter(Activity activity, ArrayList<Video> listVideo) {
        this.activity = activity;
        this.listVideo = listVideo;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        final Video video = listVideo.get(position);
        holder.tvTitle.setText(video.getTitle());
        holder.tvDecription.setText(video.getDecription());
        Glide.with(activity)
                .load(video.getThumnail())
                .centerCrop()
                .into(holder.imgThumnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Video",video);
                Intent intent = new Intent(activity, PlayVideoYoutube.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVideo.size();
    }

    class VideoHolder extends RecyclerView.ViewHolder{
        ImageView imgThumnail;
        TextView tvTitle;
        TextView tvDecription;
        public VideoHolder(View itemView) {
            super(itemView);
            imgThumnail = (ImageView) itemView.findViewById(R.id.img_thumnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDecription = (TextView) itemView.findViewById(R.id.tv_decription);
        }
    }
}
