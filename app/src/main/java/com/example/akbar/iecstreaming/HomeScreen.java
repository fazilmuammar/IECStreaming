package com.example.akbar.iecstreaming;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.akbar.iecstreaming.Adapter.ESHomeListNewsAdapter;
import com.example.akbar.iecstreaming.Adapter.ESHomeListTrailerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeScreen extends AppCompatActivity {

    public static Context context;
    private RelativeLayout.LayoutParams paramsNotFullscreen; //if you're using RelativeLatout


    private RecyclerView sport,trailer,funny,music;
    private LinearLayoutManager newsLinearLayoutManager, watchLinearLayoutManager, musicLinearLayoutManager;

    ArrayList<HashMap<String, String>> trailervideo = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> sportvideo = new ArrayList<HashMap<String, String>>();

    private ESHomeListNewsAdapter listMusicAdapter;
    private ESHomeListTrailerAdapter listTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        trailer = (RecyclerView) findViewById(R.id.listMusic);
        watchLinearLayoutManager = new LinearLayoutManager(context);
        watchLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        trailer.setLayoutManager(watchLinearLayoutManager);

        music = (RecyclerView) findViewById(R.id.listFunny);
        musicLinearLayoutManager = new LinearLayoutManager(context);
        musicLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        music.setLayoutManager(musicLinearLayoutManager);

         Trailer();
         Music();
    }

    private void Trailer(){
        AndroidNetworking.post("https://fazilmuammar.000webhostapp.com/SpencerEnglishCenter/contoh/tenses_home.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.optJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject responses    = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("tenses_id",     responses.getString("tenses_id"));
                                map.put("summary",        responses.getString("summary"));
                                map.put("filename",        responses.getString("filename"));

                                trailervideo.add(map);
                                listTrailerAdapter = new ESHomeListTrailerAdapter(trailervideo, HomeScreen.this,true,context);
                                trailer.invalidate();
                                trailer.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
                                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                trailer.setLayoutManager(linearLayoutManager);
                                trailer.setAdapter(listTrailerAdapter);
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

    private void Music(){

        AndroidNetworking.post("https://fazilmuammar.000webhostapp.com/SpencerEnglishCenter/contoh/specspeaking_home.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.optJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject responses    = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("video_id",     responses.getString("video_id"));
                                map.put("title",        responses.getString("title"));
                                map.put("filename",        responses.getString("filename"));
                                Log.d("cover",responses.getString("filename"));


                                sportvideo.add(map);
                                listMusicAdapter = new ESHomeListNewsAdapter(sportvideo, HomeScreen.this,true,context);
                                music.invalidate();
                                music.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
                                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
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
