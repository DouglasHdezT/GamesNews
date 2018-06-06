package com.debugps.gamesnews.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugps.gamesnews.R;
import com.debugps.gamesnews.adapters.PlayerListAdapter;

public class PlayerPerGameFragment extends Fragment {

    private PlayerListAdapter adapter;

    public PlayerPerGameFragment() {
    }

    public static PlayerPerGameFragment newInstance(PlayerListAdapter adapter){
        PlayerPerGameFragment fragment = new PlayerPerGameFragment();

        fragment.setAdapter(adapter);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_recycler_view_layout, container, false);

        RecyclerView rv = view.findViewById(R.id.main_recycler_view);

        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());

        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);

        return view;
    }

    public PlayerListAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(PlayerListAdapter adapter) {
        this.adapter = adapter;
    }
}
