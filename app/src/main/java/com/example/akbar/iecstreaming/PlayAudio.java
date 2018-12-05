package com.example.akbar.iecstreaming;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayAudio extends AppCompatActivity implements Runnable {

    //Create by Fazil
    private String video_id, title, music_path;

    TextView judul;

    public MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar seekBar;
    boolean wasPlaying = false;
    ImageButton fab;
    private String lastTime,fistTime,last;
    private int times=0,xx=0,index=0,secret=1,check=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);

        parseIntentFromLink();


        judul =(TextView) findViewById(R.id.judul);
        judul.setText(title);

        fab = findViewById(R.id.button);
        fab.setImageDrawable(ContextCompat.getDrawable(PlayAudio.this, android.R.drawable.ic_media_play));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check==(int)0) {
                    playSong();
                }else{
                    mediaPlayer.stop();
                    check=0;
                }
            }
        });


        seekBar = findViewById(R.id.seekbar);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

                int x = (int) Math.ceil(progress / 1000f);
                times=(x/60); //1/60=
                xx=x;
                if (xx < 10) {

                }else {
                    if(times>=(int)secret){
                        index+=60;
                        secret+=1;
                    }else if(x<60){
                        index=0;
                    }

                }
                double percent = progress / (double) seekBar.getMax();
                int offset = seekBar.getThumbOffset();
                int seekWidth = seekBar.getWidth();
                int val = (int) Math.round(percent * (seekWidth - 2 * offset));



                if (progress > 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    clearMediaPlayer();
                    fab.setImageDrawable(ContextCompat.getDrawable(PlayAudio.this, android.R.drawable.ic_media_play));
                    PlayAudio.this.seekBar.setProgress(0);
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });
    }


    public void playSong() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                clearMediaPlayer();
                seekBar.setProgress(0);
                wasPlaying = true;
                fab.setImageDrawable(ContextCompat.getDrawable(PlayAudio.this, android.R.drawable.ic_media_play));
            }


            if (!wasPlaying) {

                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }

                fab.setImageDrawable(ContextCompat.getDrawable(PlayAudio.this, android.R.drawable.ic_media_pause));
                mediaPlayer.setDataSource(getApplicationContext(),Uri.parse(music_path));

                mediaPlayer.prepare();
                mediaPlayer.setVolume(0.5f, 0.5f);
                mediaPlayer.setLooping(false);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                new Thread(this).start();
                check=1;
            }
            wasPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void run() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();

        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            seekBar.setProgress(currentPosition);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        clearMediaPlayer();
    }

    @Override
    public void onBackPressed() {
        if(check!=(int)0) {
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            fab.setImageDrawable(ContextCompat.getDrawable(PlayAudio.this, android.R.drawable.ic_media_play));
            finish();
        }else{
            finish();
        }
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }


    private void parseIntentFromLink () {
        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (uri != null) {
            video_id = uri.getQueryParameter("video_id");
            title = uri.getQueryParameter("title");
            music_path = uri.getQueryParameter("music_path");
        } else {
            video_id = getIntent().getStringExtra("video_id");
            title = getIntent().getStringExtra("title");
            music_path = getIntent().getStringExtra("music_path");
        }
    }

}
