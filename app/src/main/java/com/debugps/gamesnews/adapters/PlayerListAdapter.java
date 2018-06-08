package com.debugps.gamesnews.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.R;
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.roomTools.POJO.Player;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    private List<Player> players;
    private MainTools tools;

    public PlayerListAdapter(MainTools tools) {
        this.tools=tools;
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePhoto;
        CardView cardView;
        TextView name;

        public PlayerViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.player_name);
            profilePhoto = itemView.findViewById(R.id.player_image);
            cardView = itemView.findViewById(R.id.player_card_view);
        }
    }

    @NonNull
    @Override
    public PlayerListAdapter.PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.players_list_card_view, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerListAdapter.PlayerViewHolder holder, final int position) {
        holder.profilePhoto.setCircleBackgroundColorResource(MainActivity.getColorId());

        if(!players.get(position).getAvatar().equals("")){
            Picasso.get().load(players.get(position).getAvatar())
                    .error(R.drawable.ic_person)
                    .into(holder.profilePhoto);
        }

        holder.name.setText(players.get(position).getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.showPlayerDialog(players.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(players == null){
            return 0;
        }else{
            return players.size();
        }
    }

    public void setPlayers(List<Player> players){
        this.players = players;
        notifyDataSetChanged();
    }
}
