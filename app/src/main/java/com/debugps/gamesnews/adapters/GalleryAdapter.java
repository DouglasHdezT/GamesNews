package com.debugps.gamesnews.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ImageView;

import com.debugps.gamesnews.R;
import com.debugps.gamesnews.interfaces.MainTools;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<String> image_list;
    private MainTools tools;

    public GalleryAdapter(MainTools tools) {
        this.tools = tools;
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            cover =  itemView.findViewById(R.id.gallery_image);
        }
    }

    @NonNull
    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_card_view, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.GalleryViewHolder holder, final int position) {
        Picasso.get().load(image_list.get(position))
                .error(R.drawable.ic_game_white)
                .into(holder.cover);

        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.showGaleryDialog(image_list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(image_list ==  null){
            return 0;
        }else {
            return image_list.size();
        }
    }

    public void setImage_list(List<String> image_list) {
        this.image_list = image_list;
        notifyDataSetChanged();
    }
}
