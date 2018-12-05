package com.example.akbar.iecstreaming.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akbar.iecstreaming.HomeScreen;
import com.example.akbar.iecstreaming.R;
import com.example.akbar.iecstreaming.VideoStreaming;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ESHomeListTrailerAdapter extends RecyclerView.Adapter<ESHomeListTrailerAdapter.ContentHolder> {
    public ArrayList<HashMap<String,String>> items;
    private HashMap<String, Object> item;
    private Context context;

    private ListItemClickListener mOnClickListener;

    public ESHomeListTrailerAdapter(ArrayList<HashMap<String, String>> news, HomeScreen esTabHomeFragment, boolean b, Context contextcx) {
        this.context=contextcx;
        this.items=news;
    }

    public class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView img;
        public TextView txt;
        public ImageView play;

        public ContentHolder(View v){
            super(v);

            img = (ImageView) v.findViewById(R.id.video);
            txt = (TextView) v.findViewById(R.id.textNews);
            v.setOnClickListener(this);

            context = v.getContext();

        }



        //buat pindah ke newsdetail
        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();
            Intent seksi=new Intent(context, VideoStreaming.class);
            Toast.makeText(context,items.get(clickedPosition).get("tenses"),Toast.LENGTH_LONG).show();

            seksi.putExtra("tenses_id",items.get(clickedPosition).get("tenses_id"));
            seksi.putExtra("summary",items.get(clickedPosition).get("summary"));
            context.startActivity(seksi);

        }


        public void bindContent(ArrayList<HashMap<String, String>> item) {


        }
    }

    public interface ListItemClickListener{
        void onListItemClick(String id, int type);
    }


    @Override
    public ESHomeListTrailerAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list, parent,  false);
        return new ContentHolder(thisView);
    }

    @Override
    public void onBindViewHolder(ESHomeListTrailerAdapter.ContentHolder holder, int position) {


        Picasso.get().load(items.get(position).get("filename")).fit().centerCrop().into(holder.img);
        holder.txt.setText(items.get(position).get("summary"));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
