package com.debugps.gamesnews.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.R;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<New> newList;

    public MainAdapter() {

    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView game;

        CircleImageView favorited;
        ImageView coverImage;

        CardView cardView;

        public MainViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.main_card_vew);
            title =  itemView.findViewById(R.id.main_card_vew_title);
            game = itemView.findViewById(R.id.main_card_vew_game);
            favorited = itemView.findViewById(R.id.main_card_vew_favorited);
            coverImage = itemView.findViewById(R.id.main_card_vew_image);
        }
    }

    @NonNull
    @Override
    public MainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_view_layout, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, int position) {

        holder.coverImage.setBackgroundResource(MainActivity.getColorId());

        if(position%3 == 0){
            holder.cardView.setMinimumHeight(180);
        }else{
            holder.cardView.setMinimumHeight(125);
        }

        if(!newList.get(position).getCoverImage().equals("")){
            holder.coverImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get().load(newList.get(position).getCoverImage())
                    .error(R.drawable.ic_game_white)
                    .into(holder.coverImage);
        }else{
            holder.coverImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.coverImage.setImageResource(R.drawable.ic_game_white);
        }

        if(newList.get(position).getFavorited() == 1){
            holder.favorited.setImageResource(R.drawable.ic_fav_red);
        }else{
            holder.favorited.setImageResource(R.drawable.ic_fav_gray);
        }

        holder.title.setText(newList.get(position).getTitle());
        holder.game.setText(newList.get(position).getGame());

    }

    @Override
    public int getItemCount() {
        if(newList == null){
            return 0;
        }else{
            return newList.size();
        }
    }

    public void setNewList(List<New> newList){
        this.newList = newList;
        notifyDataSetChanged();
    }
}
