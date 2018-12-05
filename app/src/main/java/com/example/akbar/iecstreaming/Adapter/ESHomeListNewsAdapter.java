package com.example.akbar.iecstreaming.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.akbar.iecstreaming.DetailStreaming;
import com.example.akbar.iecstreaming.HomeScreen;
import com.example.akbar.iecstreaming.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ESHomeListNewsAdapter extends RecyclerView.Adapter<ESHomeListNewsAdapter.ContentHolder> {
    public ArrayList<HashMap<String,String>> items;
    private HashMap<String, Object> item;
    private Context context;

    private ListItemClickListener mOnClickListener;

    public ESHomeListNewsAdapter(ArrayList<HashMap<String, String>> news, HomeScreen esTabHomeFragment, boolean b, Context contextcx) {
        this.context=contextcx;
        this.items=news;
    }

    public class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView img;
        public TextView txt,summary,song;
        public ImageView play;

        public ContentHolder(View v){
            super(v);

            img = (ImageView) v.findViewById(R.id.video);
            txt = (TextView) v.findViewById(R.id.textNews);
           // summary = (TextView) v.findViewById(R.id.textSummary);
           // song = (TextView) v.findViewById(R.id.textSongku);
            v.setOnClickListener(this);

            context = v.getContext();

        }

        //buat pindah ke newsdetail
        @Override
        public void onClick(View view) {


            int clickedPosition = getAdapterPosition();
            Intent seksi=new Intent(context, DetailStreaming.class);
            //  Toast.makeText(context,items.get(clickedPosition).get("video_id"),Toast.LENGTH_LONG).show();

            seksi.putExtra("video_id",items.get(clickedPosition).get("video_id"));
            seksi.putExtra("title",items.get(clickedPosition).get("title"));
            context.startActivity(seksi);

        }

        public void bindContent(ArrayList<HashMap<String, String>> item) {


        }
    }

    public interface ListItemClickListener{
        void onListItemClick(String id, int type);
    }

    @Override
    public ESHomeListNewsAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list, parent,  false);
        return new ContentHolder(thisView);
    }

    @Override
    public void onBindViewHolder(ESHomeListNewsAdapter.ContentHolder holder, int position) {
        Picasso.get().load(items.get(position).get("filename")).fit().centerCrop().into(holder.img);
        holder.txt.setText(items.get(position).get("title"));
      //  holder.summary.setText(items.get(position).get("summary"));
      //  holder.song.setText(items.get(position).get("mylevel"));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
