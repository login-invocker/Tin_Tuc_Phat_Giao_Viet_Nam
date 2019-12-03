package com.invocker.phatgiaovietnam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.invocker.phatgiaovietnam.adapter.YoutubeAdapter;
import com.invocker.phatgiaovietnam.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerVideo;
    String API_KEY="AIzaSyCegXx0Am2AyAte_sqM4mUv3PvM1uTe11M";
    String ID_PLAYLIST="PLBcAa442MLAp2Pg5g1KRutINpmJF2wXEC";
    String url="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+ID_PLAYLIST+"&key="+API_KEY+"&maxResults=6" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerVideo = (RecyclerView) findViewById(R.id.recycler_video);
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
                jSonUrl = new URL(url);
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
            RecyclerView.LayoutManager manager = new LinearLayoutManager(MainActivity.this);
            recyclerVideo.setHasFixedSize(true);
            recyclerVideo.setLayoutManager(manager);
            recyclerVideo.setItemAnimator(new DefaultItemAnimator());
            YoutubeAdapter adapter = new YoutubeAdapter(MainActivity.this,listVideo);
            recyclerVideo.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }

}


