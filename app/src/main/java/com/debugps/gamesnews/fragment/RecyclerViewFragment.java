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

public class RecyclerViewFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;

    public RecyclerViewFragment() {
    }

    public static RecyclerViewFragment newInstance(RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager){
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setAdapter(adapter);
        fragment.setManager(manager);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_recycler_view_layout, container, false);

        RecyclerView rv = view.findViewById(R.id.main_recycler_view);

        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);

        return view;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public void setManager(RecyclerView.LayoutManager manager) {
        this.manager = manager;
    }
}
