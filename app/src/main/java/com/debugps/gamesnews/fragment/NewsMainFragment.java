package com.debugps.gamesnews.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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
import com.debugps.gamesnews.adapters.MainAdapter;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.viewModels.NewViewModel;

import java.util.List;

public class NewsMainFragment extends Fragment {
    private NewViewModel newViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_recycler_view_layout, container, false);

        newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);

        final MainAdapter adapter = new MainAdapter();
        RecyclerView rv = view.findViewById(R.id.main_recycler_view);

        rv.setLayoutManager(new LinearLayoutManager(NewsMainFragment.this.getContext()));
        rv.setAdapter(adapter);

        newViewModel.getAllNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> news) {
                adapter.setNewList(news);
            }
        });

        return view;
    }
}
