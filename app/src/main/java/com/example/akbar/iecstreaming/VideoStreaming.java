package com.example.akbar.iecstreaming;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VideoStreaming extends AppCompatActivity {

    TextView summary,ringkasan,views,tanggal;

    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    private String video_id, title,tenses_id,audio_id,filename;


    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private SimpleExoPlayerView mExoPlayerView;
    private MediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;
    private Context context;
    ImageView download;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_streaming);

        download=(ImageView) findViewById(R.id.download);



        summary =(TextView) findViewById(R.id.judul);
        views =(TextView) findViewById(R.id.text_view);
        tanggal =(TextView) findViewById(R.id.text_date);

        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayer);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }


        TrailerparseIntentFromLink();
    }




    private void TrailerparseIntentFromLink() {
        Intent intent = getIntent();
        Uri uri = intent.getData();

        // Test deep link data
        if (uri != null) {

            tenses_id = uri.getQueryParameter("tenses_id");
            title = uri.getQueryParameter("title");
            filename = uri.getQueryParameter("filename");
        } else {
            tenses_id = getIntent().getStringExtra("tenses_id");
            title = getIntent().getStringExtra("title");
            filename = getIntent().getStringExtra("filename");
        }
        TrailerDetail();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }


    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog( this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoStreaming.this, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoStreaming.this, R.drawable.ic_fullscreen_expand));
    }


    private void initFullscreenButton() {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }


    private void initExoPlayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector, loadControl);
        mExoPlayerView.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        mExoPlayerView.getPlayer().prepare(mVideoSource);
        mExoPlayerView.getPlayer().setPlayWhenReady(true);
    }


    @Override
    protected void onResume() {

        super.onResume();

        if (mExoPlayerView == null) {

            mExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayer);
            initFullscreenDialog();
            initFullscreenButton();

            String streamUrl = "filename";
            String userAgent = Util.getUserAgent(VideoStreaming.this, getApplicationContext().getApplicationInfo().packageName);
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(VideoStreaming.this, null, httpDataSourceFactory);
            Uri daUri = Uri.parse(streamUrl);

            mVideoSource = new HlsMediaSource(daUri, dataSourceFactory, 1, null, null);
        }

        initExoPlayer();

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoStreaming.this, R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }


    @Override
    protected void onPause() {

        super.onPause();

        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());

            mExoPlayerView.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }



    private void TrailerDetail() {
        AndroidNetworking.post("https://fazilmuammar.000webhostapp.com/SpencerEnglishCenter/contoh/tensesdetail.php?tenses_id=")
                .addBodyParameter("tenses_id",tenses_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("logtesttung", " Video: " + response);
                        try {
                            JSONArray jsonArray = response.optJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                final JSONObject responses = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("tenses_id", responses.getString("tenses_id"));
                                map.put("summary", responses.getString("summary"));
                                map.put("view",    responses.optString("view") + " views ");
                                map.put("created", responses.getString("created"));
                                summary.setText(responses.getString("summary"));
                                views.setText(responses.getString("view"));
                                tanggal.setText(responses.getString("created"));

                                Uri content_url = Uri.parse(responses.getString("filename"));
                                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                                MediaSource mediaSource = new ExtractorMediaSource(content_url, dataSourceFactory, extractorsFactory, null, null);
                                exoPlayerView.setPlayer(exoPlayer);
                                exoPlayer.prepare(mediaSource);
                                exoPlayer.setPlayWhenReady(true);

                                download.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            downloadManager =(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                                            Uri uri = null;
                                            uri = Uri.parse(responses.getString("filename"));
                                            DownloadManager.Request request = new DownloadManager.Request(uri);
                                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                            Long reference = downloadManager.enqueue(request);
                                            Toast.makeText(getApplicationContext(),"Downloading",Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }


                        } catch (Exception e) {
                            //    Toast.makeText(VideoStreaming.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        //     Toast.makeText(VideoStreaming.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                    }
                });
    }






    @Override
    public void onStop() {
        super.onStop();
        exoPlayer.release();
        exoPlayer.stop();
    }
}

