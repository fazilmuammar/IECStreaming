package com.example.akbar.iecstreaming.Adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akbar.iecstreaming.DetailStreaming;
import com.example.akbar.iecstreaming.PlayAudio;
import com.example.akbar.iecstreaming.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ESHomeListMusicAdapter extends RecyclerView.Adapter<ESHomeListMusicAdapter.ContentHolder> {
    public ArrayList<HashMap<String,String>> items;
    private HashMap<String, Object> item;
    private Context context;
    private DownloadManager downloadManager;



    public ESHomeListMusicAdapter(ArrayList<HashMap<String, String>> news, DetailStreaming esTabHomeFragment, boolean b, Context contextcx) {
        this.context=contextcx;
        this.items=news;
    }

    public class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView txt;
        public ImageView play, download;


        public ContentHolder(View v) {
            super(v);
            txt = (TextView) v.findViewById(R.id.judul);
            download = (ImageView) v.findViewById(R.id.download);
            v.setOnClickListener(this);
            context = v.getContext();

        }


        @Override
        public void onClick(View view) {


            int clickedPosition = getAdapterPosition();
            Intent seksi = new Intent(context, PlayAudio.class);
            //  Toast.makeText(context,items.get(clickedPosition).get("slug"),Toast.LENGTH_LONG).show();

            seksi.putExtra("video_id", items.get(clickedPosition).get("video_id"));
            seksi.putExtra("title", items.get(clickedPosition).get("title"));
            seksi.putExtra("music_path", items.get(clickedPosition).get("filename"));
            context.startActivity(seksi);

        }

    }

    @Override
    public ESHomeListMusicAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_music, parent,  false);
        return new ContentHolder(thisView);
    }

    @Override
    public void onBindViewHolder(ESHomeListMusicAdapter.ContentHolder holder, final int position) {

        holder.txt.setText(items.get(position).get("title"));
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadManager =(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(items.get(position).get("filename"));
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);
                Toast.makeText(context,"Downloading",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
