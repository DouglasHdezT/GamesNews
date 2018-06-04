package com.debugps.gamesnews.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.debugps.gamesnews.R;
import com.debugps.gamesnews.roomTools.POJO.New;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<New> newList;

    public MainAdapter() {

    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView desc;
        TextView body;
        TextView cover;
        TextView date;
        TextView game;

        public MainViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardtitle);
            desc = itemView.findViewById(R.id.carddesc);
            body = itemView.findViewById(R.id.cardbody);
            cover = itemView.findViewById(R.id.cardcover);
            date = itemView.findViewById(R.id.carddate);
            game = itemView.findViewById(R.id.cardgame);
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
        holder.title.setText(newList.get(position).getTitle());
        holder.desc.setText(newList.get(position).getDescription());
        holder.body.setText(newList.get(position).getBody());
        holder.cover.setText(newList.get(position).getCoverImage());
        holder.date.setText(newList.get(position).getCreatedDate());
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
