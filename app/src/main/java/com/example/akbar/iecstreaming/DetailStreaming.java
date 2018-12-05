package com.example.akbar.iecstreaming;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.akbar.iecstreaming.Adapter.ESHomeListMusicAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailStreaming extends AppCompatActivity {
    private ProgressBar progressBar;

    public static Context context;
    private RecyclerView music;
    private LinearLayoutManager musicLinearLayoutManager;

    ArrayList<HashMap<String, String>> sportvideo = new ArrayList<HashMap<String, String>>();
    private ESHomeListMusicAdapter listMusicAdapter;

    private String video_id,title;
    TextView judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_streaming);

        music = (RecyclerView) findViewById(R.id.list);
        musicLinearLayoutManager = new LinearLayoutManager(context);
        musicLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        music.setLayoutManager(musicLinearLayoutManager);


        progressBar =(ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        parseIntentFromLink();

    }

    private void showProgress(boolean show) {
        if (show) {
            music.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {

            music.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void parseIntentFromLink() {
        Intent intent = getIntent();
        Uri uri = intent.getData();

        // Test deep link data
        if (uri != null) {

            video_id = uri.getQueryParameter("video_id");
            title = uri.getQueryParameter("title");
        } else {
            video_id = getIntent().getStringExtra("video_id");
            title = getIntent().getStringExtra("title");
        }
        DetailMusic();
    }

    private void DetailMusic() {
        showProgress(true);
        AndroidNetworking.post("https://fazilmuammar.000webhostapp.com/SpencerEnglishCenter/contoh/videodetail.php?video_id=")
                .addBodyParameter("video_id",video_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showProgress(false);
                        try {

                            JSONArray jsonArray = response.optJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject responses = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("list_video_id", responses.getString("list_video_id"));
                                map.put("video_id", responses.getString("video_id"));
                                map.put("title", responses.getString("title"));
                                map.put("filename", responses.getString("filename"));


                                Log.d("cover", responses.getString("filename"));

                                sportvideo.add(map);
                                listMusicAdapter = new ESHomeListMusicAdapter(sportvideo, DetailStreaming.this, true, context);
                                music.invalidate();
                                music.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                music.setLayoutManager(linearLayoutManager);
                                music.setAdapter(listMusicAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // Toast.makeText(context,"error"+error.getErrorDetail().toString(),Toast.LENGTH_LONG).show();
                        // handle error
                    }
                });
    }
}
