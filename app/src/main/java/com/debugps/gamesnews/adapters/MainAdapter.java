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
import com.debugps.gamesnews.dialogs.NewsDialog;
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<New> newList;
    private MainTools tools;

    public MainAdapter(MainTools tools) {
        this.tools = tools;
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
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, final int position) {

        holder.coverImage.setBackgroundResource(newList.get(position).getColorId());

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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.showNewDialog(newList.get(position));
            }
        });

        holder.favorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newList.get(position).getFavorited() == 1){
                    tools.unsetFavorited(newList.get(position));
                }else {
                    tools.setFavorited(newList.get(position));
                }
            }
        });

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
