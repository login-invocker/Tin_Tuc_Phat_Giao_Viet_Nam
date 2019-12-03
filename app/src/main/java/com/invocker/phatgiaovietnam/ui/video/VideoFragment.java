package com.invocker.phatgiaovietnam.ui.video;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.invocker.phatgiaovietnam.R;
import com.invocker.phatgiaovietnam.adapter.YoutubeAdapter;
import com.invocker.phatgiaovietnam.model.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class VideoFragment extends Fragment {
    RecyclerView recyclerVideo;
    String API_KEY="AIzaSyCegXx0Am2AyAte_sqM4mUv3PvM1uTe11M";
    String ID_PLAYLIST="PLBcAa442MLAp2Pg5g1KRutINpmJF2wXEC";
    String LIST_PHAT="PLeOd5GQYbT4OMPVUpHsAQCNouwBz_8ABX";
    String url="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+ID_PLAYLIST+"&key="+API_KEY+"&maxResults=6" ;
    String urlPhat="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+LIST_PHAT+"&key="+API_KEY+"&maxResults=14" ;

    private VideoViewModel videoViewModel;/*
    @BindView(R.id.list_video_api)
    private RecyclerView listVideo;*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        videoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_video, container, false);
        recyclerVideo=root.findViewById(R.id.list_video_api);
        videoViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ParseVideoYoutube().execute();
    }

    //AsyncTask parse Json
    private class ParseVideoYoutube extends AsyncTask<Void, Void, ArrayList<Video>> {
        @Override
        protected ArrayList<Video> doInBackground(Void... params) {
            ArrayList<Video> listVideo = new ArrayList<>();
            URL jSonUrl;
            URLConnection jSonConnect;
            try {
                jSonUrl = new URL(urlPhat);
                jSonConnect = jSonUrl.openConnection();
                InputStream inputstream = jSonConnect.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                inputstream.close();
                String jSontxt = stringBuilder.toString();

                JSONObject jsonobject = new JSONObject(jSontxt);
                JSONArray allItem = jsonobject.getJSONArray("items");
                for (int i = 0; i < allItem.length(); i++) {
                    JSONObject item = allItem.getJSONObject(i);
                    JSONObject snippet = item.getJSONObject("snippet");
                    String title = snippet.getString("title");              // Get Title Video
                    String decription = snippet.getString("description");   // Get Description
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");    //Get Url Thumnail
                    JSONObject thumnailsIMG = thumbnails.getJSONObject("medium");
                    String thumnailurl = thumnailsIMG.getString("url");

                    JSONObject resourceId = snippet.getJSONObject("resourceId");    //Get ID Video
                    String videoId = resourceId.getString("videoId");

                    Video video = new Video();
                    video.setTitle(title);
                    video.setThumnail(thumnailurl);
                    video.setDecription(decription);
                    video.setUrlVideo(videoId);
                    //Add video to List
                    listVideo.add(video);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listVideo;
        }

        @Override
        protected void onPostExecute(ArrayList<Video> videos) {
            super.onPostExecute(videos);
            setupRecyclerView(videos);
        }
        //Setup recyclerView
        private void setupRecyclerView(ArrayList<Video> listVideo) {
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
            recyclerVideo.setLayoutManager(manager);
            recyclerVideo.setItemAnimator(new DefaultItemAnimator());
            YoutubeAdapter adapter = new YoutubeAdapter(getActivity(),listVideo);
            recyclerVideo.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }
}