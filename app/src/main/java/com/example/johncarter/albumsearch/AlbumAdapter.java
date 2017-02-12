package com.example.johncarter.albumsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by john carter on 2/12/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<album> albums;

    public AlbumAdapter(Context mContext, ArrayList<album> albums) {
        this.mContext = mContext;
        this.albums = albums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_recycler,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(!albums.get(position).getImage().isEmpty()){
            Glide.with(mContext).load(albums.get(position).getImage()).into(holder.mAlbumImage);
        }
        holder.mAlbumTitle.setText(albums.get(position).getName());
        holder.mAlbumArtist.setText(albums.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAlbumImage;
        private TextView mAlbumTitle;
        private TextView mAlbumArtist;

        public ViewHolder(View itemView) {
            super(itemView);

            mAlbumImage = (ImageView) itemView.findViewById(R.id.ivAlbumImage);
            mAlbumTitle = (TextView) itemView.findViewById(R.id.tvAlbumTitle);
            mAlbumArtist = (TextView) itemView.findViewById(R.id.tvAlbumArtist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri albumUri = Uri.parse(albums.get(getAdapterPosition()).getUrl());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, albumUri);
                    mContext.startActivity(websiteIntent);
                }
            });

        }
    }
}
